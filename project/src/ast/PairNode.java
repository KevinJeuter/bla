package ast;

import visitors.Visitor;

public class PairNode extends Node{
	
	private Node left;
	private Node right;
	
	public PairNode(Node left, Node right) {
		this.left = left;
		this.right = right;
	}
	
	public Node getLeft() {
		return this.left;
	}
	
	public Node getRight() {
		return this.right;
	}
	
	public String toString() {
		return this.left.toString() + ", " + this.right.toString();
	}

	@Override
	public Node accept(Visitor v) {
		return v.visit(this); 
	}
	
	//Check if Node is a pair
	public static boolean isPair(Node x) {
		return x.getClass() == PairNode.class;
	}

}