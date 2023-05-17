package org.evenos.factories;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;
import org.compiere.model.MProduct;
import org.evenos.callouts.MyCallout;

public class MyCalloutFactory implements IColumnCalloutFactory{

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName, String columnName) {

		List<IColumnCallout> list = new ArrayList<IColumnCallout>();
		
		if(tableName.equalsIgnoreCase(MProduct.Table_Name) && columnName.equalsIgnoreCase(MProduct.COLUMNNAME_Description))
			list.add(new MyCallout());
		
		if(tableName.equalsIgnoreCase(MProduct.Table_Name) && columnName.equalsIgnoreCase(MProduct.COLUMNNAME_Name))
			list.add(new MyCallout());
		
		if(tableName.equalsIgnoreCase(MProduct.Table_Name) && columnName.equalsIgnoreCase(MProduct.COLUMNNAME_AD_Org_ID))
			list.add(new MyCallout());
			
		System.out.println(list.toArray(new IColumnCallout[1])+ "test");	
		return list != null ? list.toArray(new IColumnCallout[1]) : new IColumnCallout[1];
	}

}
