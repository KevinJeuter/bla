package main;

public class Builtin extends Node{
	
	private funct function;
	
	enum funct{
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
		COND
	}
	
	public Builtin(funct function) {
		this.function = function;
	}


}
