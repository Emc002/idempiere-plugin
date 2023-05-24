package org.events.rederp;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MAsset;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.model.rederp.X_RED_Assignment;
import org.model.rederp.X_RED_Assignment_Line;
import org.osgi.service.event.Event;

public class RedHandler extends AbstractEventHandler {
  private int asignmentlineID;

  @Override
  protected void doHandleEvent(Event event) {
    if (
      event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE) ||
      event.getTopic().equals(IEventTopics.DOC_AFTER_COMPLETE)
    ) {
      PO po = getPO(event);
      if (po.get_TableName().equals(X_RED_Assignment.Table_Name)) {
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
      } else if (po.get_TableName().equals(X_RED_Assignment_Line.Table_Name)) {
        asignmentlineID = po.get_ID();
      }
    } else if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
      PO po = getPO(event);
      asignmentlineID = po.get_ID();
    }
  }

  @Override
  protected void initialize() {
    registerTableEvent(
      IEventTopics.PO_AFTER_CHANGE,
      X_RED_Assignment.Table_Name
    );
    registerTableEvent(
      IEventTopics.PO_AFTER_NEW,
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
