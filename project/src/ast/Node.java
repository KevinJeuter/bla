package ast;


import visitors.Visitor;

public abstract class Node {
	
	public abstract void accept(Visitor v);
	
}
