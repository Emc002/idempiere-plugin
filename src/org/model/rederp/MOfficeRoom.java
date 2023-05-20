package org.model.rederp;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Office Room Model
 * 
 * @author emc2
 * @version $id: MOfficeRoom.java, v 1.0 2023/05/20
 *
 */

public class MOfficeRoom extends X_B_OfficeRoom {

    private static final long serialVersionUID = 5606191293667404034L;

    private String value;

    public MOfficeRoom(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
    
    public MOfficeRoom(Properties ctx, int B_OfficeRoom_ID, String trxName) {
        super(ctx, B_OfficeRoom_ID, trxName);
    }

    public boolean isVacant() {
        return value == null || value.isEmpty();
    }
    
    public String generateRoomCode() {
        return getV_String() + "-" + getT_Integer();
    }
    
    public void activate() {
        setIsActive(true);
    }
    
    public void deactivate() {
        setIsActive(false);
    }
    
    public void updateDescription(String newDescription) {
        setDescription(newDescription);
    }
    
    
    public String getFullName() {
        return getV_String() + " - Room " + getT_Integer();
    }
    
    @Override
    protected boolean beforeDelete() {
    	// TODO Auto-generated method stub
    	
    	
    	return super.beforeDelete();
    }
}
