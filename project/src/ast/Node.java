package ast;


import visitors.Visitor;

public abstract class Node implements Cloneable{
	
	public abstract Node accept(Visitor v); //node
	
	@Override
	protected Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
	
}
