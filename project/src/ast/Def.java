package ast;

import visitors.Visitor;
import parser.DefHashMap;

/*
 * Class for Def Nodes
 */

public class Def extends Node{
	
	// def: List<(name, List<param>, Node)>, Node
	// hashmap(string, pair(list, node) kommentare
	
	private Node right;
	private DefHashMap left;

	public Def(DefHashMap left, Node right) {
		this.left = left;
		this.right = right;
	}
	
	public DefHashMap getDefinitions() {
		//return (HashMap<String, Pair<ArrayList<String>, Node>>) left.clone();
		return left;
	}
	
	public void updateDefinitions(DefHashMap left) {
		//return (HashMap<String, Pair<ArrayList<String>, Node>>) left.clone();
		this.left = left;
	}
	
	public Node getExpr() {
		return right;
	}
	
	public void updateExpr(Node right) {
		this.right = right;
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
