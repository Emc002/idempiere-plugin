package org.model.rederp;

import java.io.File;
import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.process.DocAction;

public class MAssignmentLine extends X_RED_Assignment_Line {

	private static final long serialVersionUID = -7899079406308321250L;

	public MAssignmentLine(Properties ctx, int RED_Assignment_Line_ID, String trxName, String[] virtualColumns) {
		super(ctx, RED_Assignment_Line_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MAssignmentLine(Properties ctx, int RED_Assignment_Line_ID, String trxName) {
		super(ctx, RED_Assignment_Line_ID, trxName);
		// TODO Auto-generated constructor stub
	}
}
