package vm;

import java.util.ArrayList;
import java.util.Stack;

import ast.At;
import ast.BooleanConst;
import ast.Builtin;
import ast.Builtin.funct;
import ast.Def;
import ast.Node;
import ast.NumberConst;
import ast.Pair;
import ast.StringConst;
import ast.Var;
import compiler.Compiler;

public class VM {
	
	Stack<Node> stack = new Stack<Node>();
	
	Node defExpr;
	
	public VM(Def defExpr) {
		
		this.defExpr = defExpr.getExpr();
	}
	
	public Node doReduce() {
		return reduction(defExpr);
	}
	
	private Node reduction(Node expr){
		if(At.isAt(expr)) {
			return atExpr(expr);
		}
		else if(Builtin.isS(expr)){
			return sExpr(expr);
		}
		else if(Builtin.isK(expr)) {
			return kExpr(expr);
		}
		else if(Builtin.isI(expr)) {
			return iExpr(expr);
		}
		else if(Builtin.isPlus(expr)) {
			return plusExpr(expr);
		}
		else if(Builtin.isPrePlus(expr)) {
			return prePlusExpr(expr);
		}
		else if(Builtin.isMinus(expr)) {
			return minusExpr(expr);
		}
		else if(Builtin.isPreMinus(expr)) {
			return preMinusExpr(expr);
		}
		else if(Builtin.isMul(expr)) {
			return mulExpr(expr);
		}
		else if(Builtin.isDiv(expr)) {
			return divExpr(expr);
		}
		else if(Builtin.isNot(expr)) {
			return notExpr(expr);
		}
		else if(Builtin.isCond(expr)) {
			return condExpr(expr);
		}
		else if(Builtin.isAnd(expr)) {
			return andExpr(expr);
		}
		else if(Builtin.isOr(expr)) {
			return orExpr(expr);
		}
		else if(Builtin.isGrt(expr)) {
			return grtExpr(expr);
		}
		else if(Builtin.isLes(expr)) {
			return lesExpr(expr);
		}
		else if(Builtin.isEqu(expr)) {
			return equExpr(expr);
		}
		else if(Builtin.isGeq(expr)) {
			return geqExpr(expr);
		}
		else if(Builtin.isLeq(expr)) {
			return leqExpr(expr);
		}
		else if(Builtin.isNeq(expr)) {
			return neqExpr(expr);
		}
		else {
			return expr;
		}
	}
	
	private Node atExpr(Node expr) {
		At exprAt = (At) expr;
		stack.push(exprAt);
		System.out.println(stack.toString());
		return reduction(exprAt.getLeft());
	}
	
	private Node sExpr(Node expr) {
		At f = (At) stack.lastElement();
		stack.pop();
		At g = (At) stack.lastElement();
		stack.pop();
		At x = (At) stack.lastElement();
		stack.pop();
		At fExpr = new At(f.getRight(), x.getRight());
		At gExpr = new At(g.getRight(), x.getRight());
		At result = new At(fExpr, gExpr);
		System.out.println(stack.toString());
		return reduction(result);
	}
	
	private Node kExpr(Node expr) {
		At exprAt = (At) stack.lastElement();
		stack.pop();
		stack.pop();
		Builtin I = new Builtin(Builtin.funct.I);
		At result = new At(I, reduction(exprAt.getRight()));
		System.out.println(stack.toString());
		return reduction(result);
	}
	
	private Node iExpr(Node expr) {
		At exprAt = (At) stack.lastElement();
		stack.pop();
		/*
		if(ast.At.isAt(exprAt.getRight())) {
			At exprResultAt = (At) exprAt.getRight();
			return reduction(exprResultAt.getLeft());
		}*/
		System.out.println(stack.toString());
		return reduction(exprAt.getRight());
	}
	
	private Node plusExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Builtin I = new Builtin(Builtin.funct.I);
		NumberConst intX = (NumberConst) reduction(expr1At.getRight());
		NumberConst intY = (NumberConst) reduction(expr2At.getRight());
		NumberConst result = new NumberConst(intX.getNumConst() + intY.getNumConst());
		At resultAt = new At(I, result);
		System.out.println(stack.toString());
		return reduction(resultAt);
	}
	
	private Node prePlusExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		Builtin I = new Builtin(Builtin.funct.I);
		NumberConst intX = (NumberConst) reduction(expr1At.getRight());
		NumberConst result = new NumberConst(+ intX.getNumConst());
		At resultAt = new At(I, result);
		return reduction(resultAt);
	}
	
	private Node minusExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Builtin I = new Builtin(Builtin.funct.I);
		NumberConst intX = (NumberConst) reduction(expr1At.getRight());
		NumberConst intY = (NumberConst) reduction(expr2At.getRight());
		NumberConst result = new NumberConst(intX.getNumConst() - intY.getNumConst());
		At resultAt = new At(I, result);
		return reduction(resultAt);
	}
	
	private Node preMinusExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		Builtin I = new Builtin(Builtin.funct.I);
		NumberConst intX = (NumberConst) reduction(expr1At.getRight());
		NumberConst result = new NumberConst(- intX.getNumConst());
		At resultAt = new At(I, result);
		return reduction(resultAt);
	}
	
	private Node mulExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Builtin I = new Builtin(Builtin.funct.I);
		NumberConst intX = (NumberConst) reduction(expr1At.getRight());
		NumberConst intY = (NumberConst) reduction(expr2At.getRight());
		NumberConst result = new NumberConst(intX.getNumConst() * intY.getNumConst());
		At resultAt = new At(I, result);
		return reduction(resultAt);
	}
	
	private Node divExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Builtin I = new Builtin(Builtin.funct.I);
		NumberConst intX = (NumberConst) reduction(expr1At.getRight());
		NumberConst intY = (NumberConst) reduction(expr2At.getRight());
		NumberConst result = new NumberConst(intX.getNumConst() / intY.getNumConst());
		At resultAt = new At(I, result);
		return reduction(resultAt);
	}
	
	private Node notExpr(Node expr) {
		At exprAt = (At) stack.lastElement();
		stack.pop();
		Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst boolExpr = (BooleanConst) reduction(exprAt.getRight());
		BooleanConst result = new BooleanConst(!boolExpr.getBoolConst());
		At resultAt = new At(I, result);
		return reduction(resultAt);
	}
	
	private Node condExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		At expr3At = (At) stack.lastElement();
		stack.pop();
		BooleanConst boolExpr3 = (BooleanConst) reduction(expr1At.getRight());
		Builtin I = new Builtin(Builtin.funct.I);
		At result;
		if(boolExpr3.getBoolConst()) {
			result = new At(I, reduction(expr2At.getRight()));
		}
		else {
			result = new At(I, reduction(expr3At.getRight()));
		}
		return reduction(result);
	}
	
	private Node andExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		BooleanConst boolExpr1 = (BooleanConst) reduction(expr1At.getRight());
		BooleanConst boolExpr2 = (BooleanConst) reduction(expr2At.getRight());
		Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst(boolExpr1.getBoolConst() && boolExpr2.getBoolConst());
		At result = new At(I, resultBool);
		return reduction(result);
	}
	
	private Node orExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		BooleanConst boolExpr1 = (BooleanConst) reduction(expr1At.getRight());
		BooleanConst boolExpr2 = (BooleanConst) reduction(expr2At.getRight());
		Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst(boolExpr1.getBoolConst() || boolExpr2.getBoolConst());
		At result = new At(I, resultBool);
		return reduction(result);
	}
	
	private Node grtExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		NumberConst numberExpr1 = (NumberConst) reduction(expr1At.getRight());
		NumberConst numberExpr2 = (NumberConst) reduction(expr2At.getRight());
		Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() > numberExpr2.getNumConst());
		At result = new At(I, resultBool);
		return reduction(result);
	}
	
	private Node lesExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		NumberConst numberExpr1 = (NumberConst) reduction(expr1At.getRight());
		NumberConst numberExpr2 = (NumberConst) reduction(expr2At.getRight());
		Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() < numberExpr2.getNumConst());
		At result = new At(I, resultBool);
		return reduction(result);
	}
	
	private Node equExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node expr1 = reduction(expr1At.getRight());
		Node expr2 = reduction(expr2At.getRight());
		if(expr1.getClass() == NumberConst.class) {
			expr1 = (NumberConst) expr1;
		}
		if(expr2.getClass() == NumberConst.class) {
			expr2 = (NumberConst) expr2;
		}
		if(expr1.getClass() == BooleanConst.class) {
			expr1 = (BooleanConst) expr1;
		}
		if(expr2.getClass() == BooleanConst.class) {
			expr2 = (BooleanConst) expr2;
		}
		if(expr1.getClass() == StringConst.class) {
			expr1 = (StringConst) expr1;
		}
		if(expr2.getClass() == StringConst.class) {
			expr2 = (StringConst) expr2;
		}
		Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst((expr1.toString().equals(expr2.toString())) && (expr1.getClass() == expr2.getClass()));
		At result = new At(I, resultBool);
		return reduction(result);
	}
	
	private Node geqExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		NumberConst numberExpr1 = (NumberConst) reduction(expr1At.getRight());
		NumberConst numberExpr2 = (NumberConst) reduction(expr2At.getRight());
		Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() >= numberExpr2.getNumConst());
		At result = new At(I, resultBool);
		return reduction(result);
	}
	
	private Node leqExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		NumberConst numberExpr1 = (NumberConst) reduction(expr1At.getRight());
		NumberConst numberExpr2 = (NumberConst) reduction(expr2At.getRight());
		Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() <= numberExpr2.getNumConst());
		At result = new At(I, resultBool);
		return reduction(result);
	}
	
	private Node neqExpr(Node expr) {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node expr1 = reduction(expr1At.getRight());
		Node expr2 = reduction(expr2At.getRight());
		if(expr1.getClass() == NumberConst.class) {
			expr1 = (NumberConst) expr1;
		}
		if(expr2.getClass() == NumberConst.class) {
			expr2 = (NumberConst) expr2;
		}
		if(expr1.getClass() == BooleanConst.class) {
			expr1 = (BooleanConst) expr1;
		}
		if(expr2.getClass() == BooleanConst.class) {
			expr2 = (BooleanConst) expr2;
		}
		if(expr1.getClass() == StringConst.class) {
			expr1 = (StringConst) expr1;
		}
		if(expr2.getClass() == StringConst.class) {
			expr2 = (StringConst) expr2;
		}
		Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst(!((expr1.toString().equals(expr2.toString())) && (expr1.getClass() == expr2.getClass())));
		At result = new At(I, resultBool);
		return reduction(result);
	}
}
