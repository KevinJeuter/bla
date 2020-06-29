package ast;

import visitors.Visitor;

public class At extends Node{
	
	private Node left;
	private Node right;
	
	public At(Node left, Node right) {
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
		return this.left.toString() + " @ " + this.right.toString();
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}