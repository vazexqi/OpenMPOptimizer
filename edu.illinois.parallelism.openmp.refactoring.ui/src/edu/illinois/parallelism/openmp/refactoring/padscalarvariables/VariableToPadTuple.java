package edu.illinois.parallelism.openmp.refactoring.padscalarvariables;

import org.eclipse.cdt.core.dom.ast.IASTName;

public class VariableToPadTuple {
	IASTName name;
	boolean shouldPad;
	int bytesToPad = 8;

	public VariableToPadTuple(IASTName _name) {
		name = _name;
		shouldPad = false;
	}

	public IASTName getName() {
		return name;
	}

	public void setShouldPad(boolean value) {
		shouldPad = value;
	}

	public boolean getShouldPad() {
		return shouldPad;
	}

	@Override
	public String toString() {
		return new String(name.getSimpleID());
	}

	public void setBytesToPad(int bytes) {
		bytesToPad = bytes;
	}

	public int getBytesToPad() {
		return bytesToPad;
	}

}