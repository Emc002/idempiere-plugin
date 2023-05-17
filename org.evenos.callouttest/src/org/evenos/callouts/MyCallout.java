package org.evenos.callouts;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MProduct;
import org.compiere.util.CLogger;

public class MyCallout implements IColumnCallout {

	CLogger log = CLogger.getCLogger(MyCallout.class);
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		
		log.warning("ColumnName" + mField.getColumnName());
		log.warning("NewValue" + value.toString());
		log.warning("Old Value: " + oldValue.toString());


		String productName = (String) mTab.getValue(MProduct.COLUMNNAME_Name);
		mTab.setValue(MProduct.COLUMNNAME_Description, productName + " is the best product in here");
		return null;
	}

}
	