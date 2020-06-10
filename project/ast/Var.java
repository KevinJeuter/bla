package ast;

import visitors.Visitor;

public class Var extends Node{

	private String var;
	
	public Var(String var) {
		this.var = var;
	}
	
	public String getVar() {
		return this.var;
	}
	
	@Override
	public String toString() {
		return "var(" + this.var + ")";
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}