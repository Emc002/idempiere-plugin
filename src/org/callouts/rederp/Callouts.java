package org.callouts.rederp;

import java.util.Properties;
import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAsset;

public class Callouts implements IColumnCallout {
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		mTab.setValue(MAsset.COLUMNNAME_Description, value.toString() + " is the best product in here");
		mTab.setValue("rednote", oldValue + "is the name before changes");
		return null;
	}

}