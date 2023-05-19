package org.process.rederp;

import org.adempiere.util.Callback;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
        
		int updatedRows = DB.executeUpdate(
				"UPDATE " + tableName +
				" SET " + columnName + " = ?" +
				" WHERE " + "a_asset_id" + " = " + assetId,
				userId,
				get_TrxName()
			);
			
			if (updatedRows == -1) {
				
				throw new Exception("Failed to update the value of " + columnName + " in table " + tableName);
			}
		
			
        return "Updated " + updatedRows + " rows. Note:" + string;
	}
}

