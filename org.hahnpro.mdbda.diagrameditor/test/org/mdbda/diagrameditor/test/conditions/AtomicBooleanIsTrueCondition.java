package org.mdbda.diagrameditor.test.conditions;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;

public class AtomicBooleanIsTrueCondition implements ICondition {
	AtomicBoolean aBool ;
	
	public AtomicBooleanIsTrueCondition(AtomicBoolean aBool) {
		this.aBool = aBool;
	}
	
	
	@Override
	public boolean test() throws Exception {
		return !aBool.get();
	}

	@Override
	public void init(SWTBot bot) {
	}

	@Override
	public String getFailureMessage() {
		return "The AtomicBoolean never got true";
	}

}
