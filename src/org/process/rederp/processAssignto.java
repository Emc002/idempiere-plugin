package org.process.rederp;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.Callback;
import org.compiere.model.MAsset;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Msg;
import org.model.rederp.X_RED_Assignment;
import org.model.rederp.MAssignmentLine;

public class processAssignto extends SvrProcess {
  private Integer userId;
  private Integer assetId;
  private Integer locationId;
  private Integer activityId;

  @Override
  protected void prepare() {
    ProcessInfoParameter[] paras = getParameter();
    for (ProcessInfoParameter para : paras) {
      String paraName = para.getParameterName();
      if (paraName.equalsIgnoreCase("ad_user_id")) {
        userId = para.getParameterAsInt();
      } else if (paraName.equalsIgnoreCase("a_asset_id")) {
        assetId = para.getParameterAsInt();
      } else if (paraName.equalsIgnoreCase("c_location_id")) {
        locationId = para.getParameterAsInt();
      } else if (paraName.equalsIgnoreCase("c_activity_id")) {
        activityId = para.getParameterAsInt();
      }
    }
  }

  @Override
  protected String doIt() throws Exception {
    final StringBuilder yesNo = new StringBuilder();
    final Object lock = new Object();
    processUI.ask(
      "Are you sure?",
      new Callback<Boolean>() {

        @Override
        public void onCallback(Boolean result) {
          yesNo.append(result);
          synchronized (lock) {
            lock.notify();
          }
        }
      }
    );

    synchronized (lock) {
      while (yesNo.length() == 0) {
        lock.wait();
      }
    }

    try {
    	
      MAsset asset = new MAsset(getCtx(), assetId, get_TrxName());
      int isAssign = asset.getAD_User_ID();
      System.out.println(isAssign);
      if (isAssign == 0) {
    	    asset.setAD_User_ID(userId);
    	    asset.saveEx();
    	} else {
    		throw new AdempiereException("Cannot Assign user to this Asset. The Asset Already Assign into another user .");
    	}
      int assetId = asset.get_ID();
      X_RED_Assignment redAssignment = new X_RED_Assignment(
        getCtx(),
        0,
        get_TrxName()
      );
      redAssignment.setAD_Org_ID(0);
      redAssignment.setDocStatus("DR");
      redAssignment.setC_DocType_ID(200001);
      redAssignment.setC_DocTypeTarget_ID(1000000);
      redAssignment.setProcessed(false);
      redAssignment.setProcessedOn(null);
      redAssignment.setProcessing(false);
      redAssignment.setIsApproved(false);
      redAssignment.saveEx();

      int redAssignmentId = redAssignment.get_ID();
      MAssignmentLine redAssignmentLine = new MAssignmentLine(
        getCtx(),
        0,
        get_TrxName()
      );
      redAssignmentLine.setAD_Org_ID(0);
      redAssignmentLine.setAD_User_ID(userId);
      redAssignmentLine.setA_Asset_ID(assetId);
      redAssignmentLine.setC_Activity_ID(activityId);
      redAssignmentLine.setC_Location_ID(locationId);
      redAssignmentLine.setRED_Assignment_ID(redAssignmentId);
      redAssignmentLine.saveEx();
      commitEx();
      return "Updated and Create Success. Note: ";
    } catch (Exception e) {
      rollback();
      e.printStackTrace();
      throw new AdempiereException(e.getMessage());
    }
  }
}
