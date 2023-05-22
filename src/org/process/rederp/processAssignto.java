package org.process.rederp;

import org.adempiere.util.Callback;
import org.compiere.model.MAsset;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class processAssignto extends SvrProcess {
	private Integer userId;
	private Integer assetId;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] paras = getParameter();
		for(ProcessInfoParameter para : paras) {
			String paraName = para.getParameterName();
			
			if(paraName.equalsIgnoreCase("ad_user_id")) {
				userId = para.getParameterAsInt();
			} else if(paraName.equalsIgnoreCase("a_asset_id")) {
				assetId = para.getParameterAsInt();
			}
		}
	}

	@Override
	protected String doIt() throws Exception {
	    String tableName = "a_asset";
	    String columnName = "ad_user_id";

	    final StringBuilder yesNo = new StringBuilder();
	    final Object lock = new Object();
	    processUI.ask("Yes or No?", new Callback<Boolean>() {
	        @Override
	        public void onCallback(Boolean result) {
	            yesNo.append(result);
	            synchronized (lock) {
	                lock.notify();
	            }
	        }
	    });

	    synchronized (lock) {
	        while (yesNo.length() == 0) {
	            lock.wait();
	        }
	    }

	    final StringBuilder string = new StringBuilder();
	    final Object stringLock = new Object();
	    processUI.askForInput("Please Enter a String", new Callback<String>() {
	        @Override
	        public void onCallback(String result) {
	            string.append(result);
	            synchronized (stringLock) {
	                stringLock.notify();
	            }
	        }
	    });

	    synchronized (stringLock) {
	        while (string.length() == 0) {
	            stringLock.wait();
	        }
	    }

	    try {
	        // Load the asset record
	        MAsset asset = new MAsset(getCtx(), assetId, get_TrxName());

	        // Update the column value
	        asset.setAD_User_ID(userId);

	        // Save the changes
	        asset.saveEx();

	        return "Updated 1 row. Note: " + string;
	    } catch (Exception e) {
	        // Handle the exception
	        e.printStackTrace();
	        return "Failed to update the value of " + columnName + " in table " + tableName + ": " + e.getMessage();
	    }
	}

}

