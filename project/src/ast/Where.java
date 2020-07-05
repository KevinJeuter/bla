package ast;

import java.util.ArrayList;
import java.util.HashMap;

import visitors.Visitor;

public class Where extends Node{
	
	private Node right;

	private HashMap<String, Pair<ArrayList<String>, Node>> left;

	public Where(HashMap<String, Pair<ArrayList<String>, Node>> left, Node right) {
		this.left = left;
		this.right = right;
	}

	public HashMap<String, Pair<ArrayList<String>, Node>> getDefinitions() {
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