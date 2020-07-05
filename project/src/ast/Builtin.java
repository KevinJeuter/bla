package ast;

import visitors.Visitor;

public class Builtin extends Node{
	
	private funct function;
	
	public enum funct{
		prePLUS,
		PLUS,
		MINUS,
		preMINUS,
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
		NIL,
		NOT,
		S,
		K,
		I
	}
	
	public Builtin(funct function) {
		this.function = function;
	}
	
	public funct getFunct() {
		return this.function;
	}
	
	@Override 
	public String toString() {
		return this.function.name();
	}

	@Override
	public Node accept(Visitor v) {
		return v.visit(this);
	}
	
	public static boolean isS(Node x) {
		Builtin S = new Builtin(Builtin.funct.S);
		return x.toString() == S.toString();
	}
	
	public static boolean isK(Node x) {
		Builtin K = new Builtin(Builtin.funct.K);
		return x.toString() == K.toString();
	}
	
	public static boolean isI(Node x) {
		Builtin I = new Builtin(Builtin.funct.I);
		return x.toString() == I.toString();
	}
	
	public static boolean isPlus(Node x) {
		Builtin plus = new Builtin(Builtin.funct.PLUS);
		return x.toString() == plus.toString();
	}
	public static boolean isPrePlus(Node x) {
		Builtin prePlus = new Builtin(Builtin.funct.prePLUS);
		return x.toString() == prePlus.toString();
	}
	public static boolean isMinus(Node x) {
		Builtin minus = new Builtin(Builtin.funct.MINUS);
		return x.toString() == minus.toString();
	}
	public static boolean isPreMinus(Node x) {
		Builtin preMinus = new Builtin(Builtin.funct.preMINUS);
		return x.toString() == preMinus.toString();
	}
}