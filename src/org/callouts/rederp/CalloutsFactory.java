package org.callouts.rederp;

import java.util.ArrayList;
import java.util.List;
import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;
import org.compiere.model.MAsset;
import org.model.rederp.MOfficeRoom;


public class CalloutsFactory implements IColumnCalloutFactory{

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName, String columnName) {

		List<IColumnCallout> list = new ArrayList<IColumnCallout>();
		
		//Model Asset
		if(tableName.equalsIgnoreCase(MAsset.Table_Name) && columnName.equalsIgnoreCase(MAsset.COLUMNNAME_Description))
			list.add(new Callouts());
		
		if(tableName.equalsIgnoreCase(MAsset.Table_Name) && columnName.equalsIgnoreCase(MAsset.COLUMNNAME_Name))
			list.add(new Callouts());
			
		if(tableName.equalsIgnoreCase(MAsset.Table_Name) && columnName.equalsIgnoreCase(MAsset.COLUMNNAME_AD_Org_ID))
			list.add(new Callouts());
		
		//Model Office Room (Custom Model)
		if(tableName.equalsIgnoreCase(MOfficeRoom.Table_Name) && columnName.equalsIgnoreCase(MOfficeRoom.COLUMNNAME_T_Integer))
			list.add(new CalloutMOfficeRoom());
			
		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
	}

}

