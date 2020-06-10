package ast;

import visitors.Visitor;

public class Builtin extends Node{
	
	private funct function;
	
	public enum funct{
		PLUS,
		MINUS,
		MUL,
		DIV,
		LEQ,
		EQU,
		NEQ,
		GEQ,
		LES,
		GRT,
		COND,
		COLON,
		AND,
		OR,
		HD,
		TL,
		NIL
	}
	
	public Builtin(funct function) {
		this.function = function;
	}
	
	public funct getFunct() {
		return this.function;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}