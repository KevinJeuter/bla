package ast;

/*
 * Class for Where Node
 */

import parser.DefHashMap;

import visitors.Visitor;

public class Where extends Node{
	
	private Node right;

	private DefHashMap left;

	public Where(DefHashMap left, Node right) {
		this.left = left;
		this.right = right;
	}

	public DefHashMap getDefinitions() {
		return left;
	}
	
	public Node getExpr() {
		return right;
	}
	
	@Override
	public Node accept(Visitor v) {
		return v.visit(this);
	}

}