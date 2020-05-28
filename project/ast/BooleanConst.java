package ast;

import visitors.Visitor;

public class BooleanConst extends Node{
	private boolean bool;
	
	public BooleanConst(boolean bool) {
		this.bool = bool;
	}
	
	public boolean getBoolConst() {
		return this.bool;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
