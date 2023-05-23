package org.events.rederp;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MAsset;
import org.compiere.model.PO;
import org.model.rederp.X_RED_Assignment;
import org.model.rederp.X_RED_Assignment_Line;
import org.osgi.service.event.Event;

public class RedHandler extends AbstractEventHandler {

  @Override
  protected void doHandleEvent(Event event) {
    String topic = event.getTopic();

    if (topic.equals(IEventTopics.DOC_AFTER_COMPLETE)) {
      PO po = getPO(event);
      X_RED_Assignment assignment = (X_RED_Assignment) po;
      assignment.setDocStatus("CO");
      assignment.saveEx();
    } else if (topic.equals(IEventTopics.PO_AFTER_CHANGE)) {
      PO po = getPO(event);
      X_RED_Assignment_Line assignmentLine = (X_RED_Assignment_Line) po;
      int userId = assignmentLine.getAD_User_ID();
      int assetId = assignmentLine.getA_Asset_ID();
      MAsset asset = new MAsset(null, assetId, null);
      asset.setAD_User_ID(userId);
      asset.saveEx();
      System.out.println(userId);
    }
  }

  @Override
  protected void initialize() {
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
