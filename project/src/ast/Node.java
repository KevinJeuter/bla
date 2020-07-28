package ast;

/*
 * Abstract Class for all Nodes
 */

import visitors.Visitor;

public abstract class Node implements Cloneable{
	
	public abstract Node accept(Visitor v); //node

	
}
