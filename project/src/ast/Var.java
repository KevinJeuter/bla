package ast;

/*
 * Class for Variable Nodes
 */

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
		return this.var;
	}

	@Override
	public Node accept(Visitor v) {
		return v.visit(this);
	}
	
	//Check if Node is a variable
	public static boolean isVar(Node x) {
		return x.getClass() == Var.class;
	}
}