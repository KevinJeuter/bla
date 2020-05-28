package main;

public class At extends Node{
	
	private Node left;
	private Node right;
	
	public At(Node left, Node right) {
		this.left = left;
		this.right = right;
	}
	
	public String toString() {
		return this.left.toString() + " @ " + this.right.toString();
	}

}
