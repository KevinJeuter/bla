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
	
	/*
	public String toString() {
		return this.left.toString() + " @ " + this.right.toString();
	}*/
	
	public void setLeft(Node left) {
		this.left = left;
	}
	
	public void setRight(Node right) {
		this.right = right;
	}

	@Override
	public Node accept(Visitor v) {
		return v.visit(this);
	}
	
	//Check if Node is an At
	public static boolean isAt(Node x) {
		return x.getClass() == At.class;
	}

}
