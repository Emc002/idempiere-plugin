//package org.callouts.rederp;
//
//import java.sql.ResultSet;
//import java.util.Properties;
//
//import org.adempiere.base.IColumnCallout;
//import org.compiere.model.GridField;
//import org.compiere.model.GridTab;
//import org.model.rederp.MOfficeRoom;
//
//public class CalloutMOfficeRoom implements IColumnCallout {
//    private String trxName;
//
//	private ResultSet rs;
//
//	private Properties properties;
//
//	MOfficeRoom officeRoom = new MOfficeRoom(properties, rs, trxName);
//    
//    // Use the methods from MOfficeRoom
//    boolean isVacant = officeRoom.isVacant();
//    String roomCode = officeRoom.generateRoomCode();
//	@Override
//	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
//		// TODO Auto-generated method stub
//		mTab.setValue("document", roomCode);
//		System.out.print(roomCode);
//		return null;
//	}
//
//}

package org.callouts.rederp;

//import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
//import org.model.rederp.MOfficeRoom;	

public class CalloutMOfficeRoom implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
//		String trxName = mTab.getName();
//		ResultSet rs = null; // Replace with your ResultSet
//		Properties properties = ctx; // Replace with your Properties

//		MOfficeRoom officeRoom = new MOfficeRoom(properties, rs, trxName);

		// Use the methods from MOfficeRoom
//		boolean isVacant = officeRoom.isVacant();
//		String roomCode = officeRoom.generateRoomCode();
//
//		mTab.setValue("document", roomCode);
//		System.out.print(roomCode);
		return null;
	}

}

