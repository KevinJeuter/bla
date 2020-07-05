package ast;


import visitors.Visitor;

public abstract class Node {
	
	public abstract Node accept(Visitor v); //node
	
}
