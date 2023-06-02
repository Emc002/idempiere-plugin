package org.events.rederp;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAsset;
import org.compiere.model.MAssetAddition;
import org.compiere.model.MAssetDisposed;
import org.compiere.model.MAssetGroup;
import org.compiere.model.MDepreciationWorkfile;
import org.compiere.model.PO;
import org.compiere.model.X_A_Asset_Disposed;
import org.compiere.model.X_A_Depreciation_Workfile;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.model.rederp.X_RED_Asset_Transfer;
import org.osgi.service.event.Event;

public class RedAssetTransfer extends AbstractEventHandler {
    private Trx m_trx;

    @Override
    protected void doHandleEvent(Event event) {
        PO po = getPO(event);
        Integer depreWorkFileIDOne = 0;
        Integer aBalanceOrgIDOne = 0;
        m_trx = Trx.get(Trx.createTrxName("TRF"), true);
        if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
            if (po.get_TableName().equals(X_RED_Asset_Transfer.Table_Name)) {
                validateAssetTransfer(po);

                try {
                    m_trx.start();

                    MAsset originalAsset = loadOriginalAsset((Integer) po.get_Value("A_Asset_ID"));
                    MAsset newAsset = createNewAsset(originalAsset);
                    MAssetDisposed assetDisposed = disposeAsset(originalAsset, newAsset);
                    MAssetAddition assetAddition = createAssetAddition(newAsset, (Integer) po.get_Value("AD_Org_ID"));

                    m_trx.commit();

                    updateDepreciationWorkfile(depreWorkFileIDOne, aBalanceOrgIDOne);
                } catch (Exception e) {
                    m_trx.rollback();
                    e.printStackTrace();
                    throw new AdempiereException(e);
                } finally {
                    m_trx.close();
                }
            }
        } else if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
            if (po.get_TableName().equals(X_A_Depreciation_Workfile.Table_Name)) {
                depreWorkFileIDOne = (Integer) po.get_Value("a_depreciation_workfile_id");
                aBalanceOrgIDOne = (Integer) po.get_Value("a_asset_acct");
            }
        }
    }

    private void validateAssetTransfer(PO po) {
        // Validation logic
    }

    private MAsset loadOriginalAsset(int assetID) {
        return new MAsset(Env.getCtx(), assetID, m_trx.getTrxName());
    }

    private MAsset createNewAsset(MAsset originalAsset) {
        MAsset newAsset = new MAsset(originalAsset, 0, originalAsset.getCreated());
        newAsset.set_ValueNoCheck("Parent_Asset_ID", originalAsset.getA_Asset_ID());
        newAsset.setA_Asset_Group_ID(originalAsset.getA_Asset_Group_ID());
        newAsset.setA_Asset_Status_ID(MAsset.A_ASSET_STATUS_Active);
        newAsset.setHelp(originalAsset.getHelp());
        newAsset.saveEx(m_trx.getTrxName());

        return newAsset;
    }

    private MAssetDisposed disposeAsset(MAsset originalAsset, MAsset newAsset) {
        MAssetDisposed assetDisposed = new MAssetDisposed(originalAsset);
        assetDisposed.setA_Asset_Disposed_ID(0);
        assetDisposed.setA_Asset_ID(originalAsset.getA_Asset_ID());
        assetDisposed.setAssetServiceDate(originalAsset.getAssetServiceDate());
        assetDisposed.setC_AcctSchema_ID(originalAsset.getC_AcctSchema_ID());
        assetDisposed.setC_BPartner_ID(originalAsset.getC_BPartner_ID());
        assetDisposed.setC_Currency_ID(originalAsset.getC_Currency_ID());
        assetDisposed.setC_Location_ID(originalAsset.getC_Location_ID());
        assetDisposed.setC_Period_ID(originalAsset.getC_Period_ID());
        assetDisposed.setGL_Category_ID(originalAsset.getGL_Category_ID());
        assetDisposed.setM_Product_ID(originalAsset.getM_Product_ID());
        assetDisposed.setName(originalAsset.getName());
        assetDisposed.setQty(originalAsset.getQty());
        assetDisposed.setProductionDate(originalAsset.getProductionDate());
        assetDisposed.setA_Asset_Disposed_MaintType(originalAsset.getA_Asset_MaintType());
        assetDisposed.setA_Asset_Disposed_SalvageType(originalAsset.getA_Asset_SalvageType());
        assetDisposed.setA_Asset_Disposed_KeepAsset(originalAsset.getA_Asset_KeepAsset());
        assetDisposed.setA_Asset_Disposed_Units(originalAsset.getA_Asset_Units());
        assetDisposed.setA_Asset_Disposed_Value(originalAsset.getA_Asset_Value());
        assetDisposed.setA_Asset_Disposed_ProcessDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        assetDisposed.setA_Asset_Disposed_Processed(true);
        assetDisposed.saveEx(m_trx.getTrxName());

        // // Link the disposed asset with the new asset
        // newAsset.setA_Asset_Disposed_ID(assetDisposed.getA_Asset_Disposed_ID());
        // newAsset.saveEx(m_trx.getTrxName());

        return assetDisposed;
    }

    private MAssetAddition createAssetAddition(MAsset newAsset, int orgID) {
        MAssetAddition assetAddition = new MAssetAddition(newAsset, 0, newAsset.getCreated());
        assetAddition.setAD_Org_ID(orgID);
        assetAddition.setIsDisposed(false);
        assetAddition.setQty(BigDecimal.ONE);
        assetAddition.setProcessed(false);
        assetAddition.saveEx(m_trx.getTrxName());

        return assetAddition;
    }

    private void updateDepreciationWorkfile(int depreWorkFileID, int balanceOrgID) {
        String sql = "UPDATE A_Depreciation_Workfile SET A_Asset_Acct = ? WHERE A_Depreciation_Workfile_ID = ?";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, m_trx.getTrxName())) {
            pstmt.setInt(1, balanceOrgID);
            pstmt.setInt(2, depreWorkFileID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new AdempiereException("Error updating depreciation workfile", e);
        }
    }
}
