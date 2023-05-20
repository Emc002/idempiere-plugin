/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.model.rederp;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for B_OfficeRoom
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="B_OfficeRoom")
public class X_B_OfficeRoom extends PO implements I_B_OfficeRoom, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230520L;

    /** Standard Constructor */
    public X_B_OfficeRoom (Properties ctx, int B_OfficeRoom_ID, String trxName)
    {
      super (ctx, B_OfficeRoom_ID, trxName);
      /** if (B_OfficeRoom_ID == 0)
        {
			setB_OfficeRoom_ID (0);
			setName (null);
			setV_String (null);
        } */
    }

    /** Standard Constructor */
    public X_B_OfficeRoom (Properties ctx, int B_OfficeRoom_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, B_OfficeRoom_ID, trxName, virtualColumns);
      /** if (B_OfficeRoom_ID == 0)
        {
			setB_OfficeRoom_ID (0);
			setName (null);
			setV_String (null);
        } */
    }

    /** Load Constructor */
    public X_B_OfficeRoom (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_B_OfficeRoom[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set B_OfficeRoom.
		@param B_OfficeRoom_ID B_OfficeRoom
	*/
	public void setB_OfficeRoom_ID (int B_OfficeRoom_ID)
	{
		if (B_OfficeRoom_ID < 1)
			set_ValueNoCheck (COLUMNNAME_B_OfficeRoom_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_B_OfficeRoom_ID, Integer.valueOf(B_OfficeRoom_ID));
	}

	/** Get B_OfficeRoom.
		@return B_OfficeRoom	  */
	public int getB_OfficeRoom_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_B_OfficeRoom_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set B_OfficeRoom_UU.
		@param B_OfficeRoom_UU B_OfficeRoom_UU
	*/
	public void setB_OfficeRoom_UU (String B_OfficeRoom_UU)
	{
		set_Value (COLUMNNAME_B_OfficeRoom_UU, B_OfficeRoom_UU);
	}

	/** Get B_OfficeRoom_UU.
		@return B_OfficeRoom_UU	  */
	public String getB_OfficeRoom_UU()
	{
		return (String)get_Value(COLUMNNAME_B_OfficeRoom_UU);
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Comment/Help.
		@param Help Comment or Hint
	*/
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp()
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set RoomNumber.
		@param T_Integer RoomNumber
	*/
	public void setT_Integer (int T_Integer)
	{
		set_Value (COLUMNNAME_T_Integer, Integer.valueOf(T_Integer));
	}

	/** Get RoomNumber.
		@return RoomNumber	  */
	public int getT_Integer()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_Integer);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** Set FloorNumber.
		@param V_String FloorNumber
	*/
	public void setV_String (String V_String)
	{
		set_Value (COLUMNNAME_V_String, V_String);
	}

	/** Get FloorNumber.
		@return FloorNumber	  */
	public String getV_String()
	{
		return (String)get_Value(COLUMNNAME_V_String);
	}
}