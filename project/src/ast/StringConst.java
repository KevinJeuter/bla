package ast;

/*
 * Class for String Nodes
 */

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
	public Node accept(Visitor v) {
		return v.visit(this);
	}
}
