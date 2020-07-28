package ast;

/*
 * Class for all Builtin Nodes. Contains Symbols and Keywords from Lexer.
 */

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
	
	
	// * Functions to check if a Node is a specific Builtin *
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
	public static boolean isAnd(Node x) {
		Builtin and = new Builtin(Builtin.funct.AND);
		return x.toString() == and.toString();
	}
	public static boolean isCond(Node x) {
		Builtin cond = new Builtin(Builtin.funct.COND);
		return x.toString() == cond.toString();
	}
	public static boolean isDiv(Node x) {
		Builtin div = new Builtin(Builtin.funct.DIV);
		return x.toString() == div.toString();
	}
	public static boolean isEqu(Node x) {
		Builtin equal = new Builtin(Builtin.funct.EQU);
		return x.toString() == equal.toString();
	}
	public static boolean isGeq(Node x) {
		Builtin geq = new Builtin(Builtin.funct.GEQ);
		return x.toString() == geq.toString();
	}
	public static boolean isGrt(Node x) {
		Builtin grt = new Builtin(Builtin.funct.GRT);
		return x.toString() == grt.toString();
	}
	public static boolean isHd(Node x) {
		Builtin head = new Builtin(Builtin.funct.HD);
		return x.toString() == head.toString();
	}
	public static boolean isLeq(Node x) {
		Builtin leq = new Builtin(Builtin.funct.LEQ);
		return x.toString() == leq.toString();
	}
	public static boolean isLes(Node x) {
		Builtin les = new Builtin(Builtin.funct.LES);
		return x.toString() == les.toString();
	}
	public static boolean isMul(Node x) {
		Builtin mul = new Builtin(Builtin.funct.MUL);
		return x.toString() == mul.toString();
	}
	public static boolean isNeq(Node x) {
		Builtin neq = new Builtin(Builtin.funct.NEQ);
		return x.toString() == neq.toString();
	}
	public static boolean isNil(Node x) {
		Builtin nil = new Builtin(Builtin.funct.NIL);
		return x.toString() == nil.toString();
	}
	public static boolean isNot(Node x) {
		Builtin not = new Builtin(Builtin.funct.NOT);
		return x.toString() == not.toString();
	}
	public static boolean isOr(Node x) {
		Builtin or = new Builtin(Builtin.funct.OR);
		return x.toString() == or.toString();
	}
	public static boolean isTl(Node x) {
		Builtin tail = new Builtin(Builtin.funct.TL);
		return x.toString() == tail.toString();
	}
	public static boolean isColon(Node x) {
		Builtin colon = new Builtin(Builtin.funct.COLON);
		return x.toString() == colon.toString();
	}
}