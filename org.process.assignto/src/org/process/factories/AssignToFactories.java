package org.process.factories;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.process.assigninto.AssignTo;

public class AssignToFactories implements IProcessFactory {

	@Override
	public ProcessCall newProcessInstance(String className) {
		// TODO Auto-generated method stub
		if(className.equals("org.evenos.processes.AssignTo"))
			return new AssignTo();
		return null;
	}

}
