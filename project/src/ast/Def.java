package ast;

import java.util.HashMap;

import visitors.Visitor;

import java.util.ArrayList;

public class Def extends Node{
	
	// def: List<(name, List<param>, Node)>, Node
	// hashmap(string, pair(list, node) kommentare
	
	private Node right;
	private HashMap<String, Pair<ArrayList<String>, Node>> left;

	public Def(HashMap<String, Pair<ArrayList<String>, Node>> left, Node right) {
		this.left = left;
		this.right = right;
	}
	
	public HashMap<String, Pair<ArrayList<String>, Node>> getDefinitions() {
		//return (HashMap<String, Pair<ArrayList<String>, Node>>) left.clone();
		return left;
	}
	
	public Node getExpr() {
		return right;
	}
	
	@Override
	public String toString() {
		return left.toString() + " DEF " + right.toString();
	}

	@Override
	public Node accept(Visitor v) {
		return v.visit(this);
	}
}
