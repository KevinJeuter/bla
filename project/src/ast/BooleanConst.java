package ast;

/*
 * Class for Boolean Nodes.
 */

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
	public String toString() {
		return "" + this.bool + "";
	}

	@Override
	public Node accept(Visitor v) {
		return v.visit(this);
	}

}