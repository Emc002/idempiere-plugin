package org.events.rederp;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAsset;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.model.rederp.X_RED_Assignment;
import org.model.rederp.X_RED_Assignment_Line;
import org.osgi.service.event.Event;
import org.zkoss.poi.ss.formula.functions.T;



public class RedHandler extends AbstractEventHandler {
  private int asignmentlineID;

  @Override
  protected void doHandleEvent(Event event) {
    if (
      event.getTopic().equals(IEventTopics.DOC_AFTER_COMPLETE)
    ) {
      PO po = getPO(event);
      if (po.get_TableName().equals(X_RED_Assignment.Table_Name)) {
        if (asignmentlineID != 0) {
          X_RED_Assignment_Line assignmentLine = new X_RED_Assignment_Line(
            Env.getCtx(),
            asignmentlineID,
            null
          );
          int userId = assignmentLine.getAD_User_ID();
          int assetId = assignmentLine.getA_Asset_ID();
          MAsset asset = new MAsset(Env.getCtx(), assetId, null);
          asset.setAD_User_ID(userId);
          asset.saveEx();
        } else {
        	throw new AdempiereException("You must input data in Assignment line");
        }
      } else if (po.get_TableName().equals(X_RED_Assignment_Line.Table_Name)) {
        asignmentlineID = po.get_ID();
      }
    } else if (event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE) || event.getTopic().equals(IEventTopics.PO_BEFORE_NEW) ) {
    	System.out.println("Event: " + event.getProperty("tableName"));
    	PO po = getPO(event);
      if (po != null) {
      if(po.get_TableName().equals(X_RED_Assignment_Line.Table_Name)) {
      asignmentlineID = po.get_ID();
      Integer assetID = (Integer) po.get_Value("A_Asset_ID");
      Integer userID = (Integer) po.get_Value("AD_User_ID");
      Integer activityID = (Integer) po.get_Value("C_Activity_ID");
      Integer locationID = (Integer) po.get_Value("C_Location_ID");
      
      StringBuilder errorMessage = new StringBuilder("Please input the data in Column: ");
      int num = 0;
      if (assetID == null) {
    	  num++;
          errorMessage.append(num + ". Asset  ");
      }

      if (userID == null) {
    	  num++;
          errorMessage.append(num + ". User  ");
      }

      if (activityID == null) {
    	  num++;
          errorMessage.append(num + ". Activity  ");
      }

      if (locationID == null) {
    	  num++;
          errorMessage.append(num + ". Address  ");
      }

      if (errorMessage.length() > 0) {
          throw new AdempiereException(errorMessage.toString());
      }
      
      }
    }
      
    }
  }

  @Override
  protected void initialize() {
    registerTableEvent(
      IEventTopics.PO_BEFORE_NEW,
      X_RED_Assignment_Line.Table_Name
    );
    registerTableEvent(
      IEventTopics.PO_AFTER_CHANGE,
      X_RED_Assignment_Line.Table_Name
    );
    registerTableEvent(
      IEventTopics.DOC_AFTER_COMPLETE,
      X_RED_Assignment.Table_Name
    );
  }
  
}
