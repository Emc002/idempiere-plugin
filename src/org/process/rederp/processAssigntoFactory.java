package org.process.rederp;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;

public class processAssigntoFactory implements IProcessFactory {

	@Override
	public ProcessCall newProcessInstance(String className) {
		if(className.equals("org.evenos.processes.AssignTo")) {
			return new processAssignto();
	}
		return null;
	}

}