package ast;

import visitors.Visitor;

public class NumberConst extends Node{
	private int konst;
	
	public NumberConst(int konst) {
		this.konst = konst;
	}
	
	public int getNumConst() {
		return this.konst;
	}
	
	@Override
	public String toString() {
		return "" + this.konst + "";
	}

	@Override
	public Node accept(Visitor v) {
		return v.visit(this);
	}
}