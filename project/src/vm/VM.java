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
		System.out.println(stack.toString());
		return exprAt.getRight();
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
	//bei plus fehler werfen z.b.
}
