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

    /**
     * Check if the office room is vacant.
     * A room is considered vacant if it has no assigned value.
     * 
     * @return true if the room is vacant, false otherwise
     */
    public boolean isVacant() {
        return value == null || value.isEmpty();
    }
    
    /**
     * Generate a room code based on the room number and floor number.
     * The room code format: [FloorNumber]-[RoomNumber]
     * 
     * @return the generated room code
     */
    public String generateRoomCode() {
        return getV_String() + "-" + getT_Integer();
    }
    

	/**
     * Set the room as active.
     */
    public void activate() {
        setIsActive(true);
    }
    
    /**
     * Set the room as inactive.
     */
    public void deactivate() {
        setIsActive(false);
    }
    
    /**
     * Update the room's description with the provided text.
     * 
     * @param newDescription the new description for the room
     */
    public void updateDescription(String newDescription) {
        setDescription(newDescription);
    }
    
    /**
     * Get the full name of the office room, including the floor number and room number.
     * 
     * @return the full name of the office room
     */
    public String getFullName() {
        return getV_String() + " - Room " + getT_Integer();
    }
}
