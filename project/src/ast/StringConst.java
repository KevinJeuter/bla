package ast;

import visitors.Visitor;

public class StringConst extends Node{
	private String konst;
	
	public StringConst(String konst) {
		this.konst = konst;
	}
	
	public String getStringConst() {
		return this.konst;
	}
	
	@Override
	public String toString() {
		return this.konst.toString();
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
