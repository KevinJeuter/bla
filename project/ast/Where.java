package ast;

import java.util.ArrayList;
import java.util.HashMap;

import visitors.Visitor;

public class Where extends Node{
	
	private Node right;
	//muss hier �berhaupt eine hashmap mit string? weil die parameter von where sind bei uns im bsp. links im at knoten
	//private Pair<ArrayList<String>, Node> left;
	
	// gefixt:
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
	public void accept(Visitor v) {
		v.visit(this);
	}

}
