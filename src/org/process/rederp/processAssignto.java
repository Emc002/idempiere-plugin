package org.process.rederp;

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
		
		return "Updated " + updatedRows + " rows";
	}
}

