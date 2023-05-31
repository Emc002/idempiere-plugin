package org.events.rederp;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAsset;
import org.compiere.model.MAssetAddition;
import org.compiere.model.MAssetDisposed;
import org.compiere.model.MAssetGroup;
//import org.compiere.model.MDepreciationWorkfile;
import org.compiere.model.PO;
import org.compiere.model.X_A_Asset_Disposed;
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
    m_trx = Trx.get(Trx.createTrxName("TRF"), true);
    if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
      if (po.get_TableName().equals(X_RED_Asset_Transfer.Table_Name)) {
        StringBuilder errorMessage = new StringBuilder(
          "Please input the data in Column: "
        );
        StringBuilder err = new StringBuilder();
        try {
          Timestamp now = new Timestamp(System.currentTimeMillis());
          Integer orgID = (Integer) po.get_Value("AD_Org_ID");
          Integer assetID = (Integer) po.get_Value("A_Asset_ID");
          Integer docTypeID = (Integer) po.get_Value("C_DocType_ID");
          int num = 0;
          if (orgID == null) {
            num++;
            err.append(num + ". Transfer to Organization  ");
          }

          if (assetID == null) {
            num++;
            err.append(num + ". Asset  ");
          }
          if (docTypeID == null) {
            num++;
            err.append(num + ". Document Type  ");
          }
          if (err.length() > 0) {
            throw new AdempiereException(
              errorMessage.toString() + err.toString()
            );
          }

          m_trx.start();

          MAsset originalAsset = new MAsset(
            Env.getCtx(),
            assetID,
            m_trx.getTrxName()
          );
          String originalValue = originalAsset.getValue();
          String originalName = originalAsset.getName();
          String originalInventoryNo = originalAsset.getInventoryNo();
          String originalDescription = originalAsset.getDescription();
          MAssetGroup originalAssetGroup = originalAsset.getAssetGroup();
          int originalM_Product_ID = originalAsset.getM_Product_ID();
          int originalManufacturedYear = originalAsset.getManufacturedYear();
          Timestamp originalCreated = originalAsset.getCreated();
          Timestamp originalAssetServiceDate = originalAsset.getAssetServiceDate();

          int assetTODisposed = originalAsset.get_ID();
          MAsset newAsset = new MAsset(Env.getCtx(), 0, m_trx.getTrxName());
          newAsset.setName("TRF - " + originalName);
          newAsset.setValue("TRF - " + originalName);
          newAsset.setAD_Org_ID(12);
          newAsset.setAssetActivationDate(now);
          newAsset.setIsActive(true);
          newAsset.setA_Asset_Status(MAsset.A_ASSET_STATUS_New);
          newAsset.setValue(originalValue);
          newAsset.setInventoryNo(originalInventoryNo);
          newAsset.setDescription(originalDescription);
          newAsset.setAssetGroup(originalAssetGroup);
          newAsset.setM_Product_ID(originalM_Product_ID);
          newAsset.setManufacturedYear(originalManufacturedYear);
          newAsset.setA_Asset_CreateDate(originalCreated);
          newAsset.setAssetServiceDate(originalAssetServiceDate);
          MAssetDisposed assetDisposed = new MAssetDisposed(
            Env.getCtx(),
            0,
            m_trx.getTrxName()
          );
          assetDisposed.setC_DocType_ID(200003);
          assetDisposed.setA_Asset_ID(assetTODisposed);
          assetDisposed.setA_Disposed_Method(
            X_A_Asset_Disposed.A_DISPOSED_METHOD_Trade
          );
          assetDisposed.setDateAcct(now);
          assetDisposed.setDateDoc(now);
          assetDisposed.setA_Disposed_Date(now);
          
          newAsset.saveEx(m_trx.getTrxName());
          int newAssetId = newAsset.get_ID();
          int originalAssetID = originalAsset.get_ID();
          String sql =
            "SELECT A_Depreciation_Workfile_ID, a_salvage_value, a_qty_current, a_asset_remaining, a_asset_remaining_f FROM A_Depreciation_Workfile WHERE A_Asset_ID = ?";
          int assetRemaining = 0;
          int currentQty = 0;
          int salvageValue = 0;
          PreparedStatement pstmt = null;
          ResultSet rs = null;
          try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, originalAssetID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
              assetRemaining = rs.getInt("a_asset_remaining_f");
              currentQty = rs.getInt("a_qty_current");
              salvageValue = rs.getInt("a_salvage_value");
            }
          } catch (SQLException e) {
            e.printStackTrace();
          } finally {
            DB.close(rs, pstmt);
          }
          assetDisposed.saveEx();
          assetDisposed.completeIt();
          BigDecimal assetRemainingDEcimal = BigDecimal.valueOf(assetRemaining);
          BigDecimal currentQtyDecimal = BigDecimal.valueOf(currentQty);
          BigDecimal salvageValueDecimal = BigDecimal.valueOf(salvageValue);
          MAssetAddition assetAddition = new MAssetAddition(
            Env.getCtx(),
            0,
            m_trx.getTrxName()
          );
          assetAddition.setA_Asset_ID(newAssetId);
          assetAddition.setAD_Org_ID(orgID);
          assetAddition.setC_DocType_ID(1000001);
          assetAddition.setM_Product_ID(originalM_Product_ID);
          assetAddition.setA_SourceType(MAssetAddition.A_SOURCETYPE_Manual);
          assetAddition.setAssetAmtEntered(assetRemainingDEcimal);
          assetAddition.setA_QTY_Current(currentQtyDecimal);
          assetAddition.setA_Salvage_Value(salvageValueDecimal);
          assetAddition.setAssetValueAmt(assetRemainingDEcimal);
          assetAddition.setSourceAmt(assetRemainingDEcimal);
          assetAddition.setDateAcct(now);
          assetAddition.setDateDoc(now);
          String completeAdd = assetAddition.completeIt();
          assetAddition.setDocStatus(completeAdd);
          assetAddition.saveEx(m_trx.getTrxName());
          m_trx.commit();
        } catch (Exception e) {
          m_trx.rollback();
          e.printStackTrace();
          throw new AdempiereException(e);
        } finally {
          m_trx.close();
        }
      }
    }
  }

  @Override
  protected void initialize() {
    registerTableEvent(
      IEventTopics.DOC_BEFORE_COMPLETE,
      X_RED_Asset_Transfer.Table_Name
    );
  }
}
