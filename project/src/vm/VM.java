package vm;

import java.util.Stack;

import ast.At;
import ast.BooleanConst;
import ast.Builtin;
import ast.Def;
import ast.Node;
import ast.NumberConst;
import ast.StringConst;
import ast.PairNode;

public class VM {
	
	Stack<Node> stack = new Stack<Node>();
	
	Node defExpr;
	
	public VM(Def defExpr) {
		
		this.defExpr = defExpr.getExpr();
	}
	
	private boolean isConstant(Node x) {
		return ((x.getClass() == BooleanConst.class) || (x.getClass() == StringConst.class) || (x.getClass() == NumberConst.class));
	}

	public String print() {
		Node result = reduction();
		if(isConstant(result)) {
			return result.toString();
		}
		else if(PairNode.isPair(result)) {
			PairNode resultPair = (PairNode) result;
			errorNoNil(resultPair);
			return result.toString();
		}
		else {
			throw new RuntimeException("The result is not a printable object. (No constant, nor a pair)");
		}
	}
	
	private void errorNoNil(PairNode result) {
		while(result.getClass() == PairNode.class) {
			if(result.getLeft().getClass() == PairNode.class) {
				PairNode resultPairLeft = (PairNode) result.getLeft();
				errorNoNil(resultPairLeft);
			}
			if(result.getRight().getClass() == PairNode.class) {
				PairNode resultPairRight = (PairNode) result.getRight();
				result = resultPairRight;
			}
			else if(!Builtin.isNil(result.getRight())) {
				throw new RuntimeException("List is missing an end. (No nil)");
			}
			else {
				break;
			}
		}
	}
	
	public Node reduction() {
		return reduction(defExpr);
	}
	
	private Node reduction(Node expr){
		if(At.isAt(expr)) {
			return atExpr(expr);
		}
		else if(Builtin.isS(expr)){
			return sExpr();
		}
		else if(Builtin.isK(expr)) {
			return kExpr();
		}
		else if(Builtin.isI(expr)) {
			return iExpr();
		}
		else if(Builtin.isPlus(expr)) {
			return plusExpr();
		}
		else if(Builtin.isPrePlus(expr)) {
			return prePlusExpr();
		}
		else if(Builtin.isMinus(expr)) {
			return minusExpr();
		}
		else if(Builtin.isPreMinus(expr)) {
			return preMinusExpr();
		}
		else if(Builtin.isMul(expr)) {
			return mulExpr();
		}
		else if(Builtin.isDiv(expr)) {
			return divExpr();
		}
		else if(Builtin.isNot(expr)) {
			return notExpr();
		}
		else if(Builtin.isCond(expr)) {
			return condExpr();
		}
		else if(Builtin.isAnd(expr)) {
			return andExpr();
		}
		else if(Builtin.isOr(expr)) {
			return orExpr();
		}
		else if(Builtin.isGrt(expr)) {
			return grtExpr();
		}
		else if(Builtin.isLes(expr)) {
			return lesExpr();
		}
		else if(Builtin.isEqu(expr)) {
			return equExpr();
		}
		else if(Builtin.isGeq(expr)) {
			return geqExpr();
		}
		else if(Builtin.isLeq(expr)) {
			return leqExpr();
		}
		else if(Builtin.isNeq(expr)) {
			return neqExpr();
		}
		else if(Builtin.isColon(expr)) {
			return pair();
		}
		else if(Builtin.isHd(expr) || Builtin.isTl(expr)) {
			return headOrTail(expr);
		}
		else {
			return expr;
		}
	}
	
	private Node atExpr(Node expr) {
		At exprAt = (At) expr;
		stack.push(exprAt);
		return reduction(exprAt.getLeft());
	}
	
	private Node sExpr() {
		At f = (At) stack.lastElement();
		stack.pop();
		At g = (At) stack.lastElement();
		stack.pop();
		At x = (At) stack.lastElement();
		stack.pop();
		At fExpr = new At(f.getRight(), x.getRight());
		At gExpr = new At(g.getRight(), x.getRight());
		At result = new At(fExpr, gExpr);
		return reduction(result);
	}
	
	private Node kExpr() {
		At exprAt = (At) stack.lastElement();
		stack.pop();
		stack.pop();
		//Builtin I = new Builtin(Builtin.funct.I);
		//At result = new At(I, reduction(exprAt.getRight()));
		return reduction(exprAt.getRight());
	}
	
	private Node iExpr() {
		At exprAt = (At) stack.lastElement();
		stack.pop();
		return reduction(exprAt.getRight());
	}
	
	private Node plusExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		//Builtin I = new Builtin(Builtin.funct.I);
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class){
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst intY = (NumberConst) reductionExpr2;
			NumberConst result = new NumberConst(intX.getNumConst() + intY.getNumConst());
			//At resultAt = new At(I, result);
			return reduction(result);
		}
		else {
			throw new RuntimeException("plus needs two integer arguments");
		}
	}
	
	private Node prePlusExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class) {
			//Builtin I = new Builtin(Builtin.funct.I);
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst result = new NumberConst(+ intX.getNumConst());
			//At resultAt = new At(I, result);
			return reduction(result);
		}
		else {
			throw new RuntimeException("Prefix Plus needs an integer argument");
		}
	}
	
	private Node minusExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			//Builtin I = new Builtin(Builtin.funct.I);
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst intY = (NumberConst) reductionExpr2;
			NumberConst result = new NumberConst(intX.getNumConst() - intY.getNumConst());
			//At resultAt = new At(I, result);
			return reduction(result);
		}
		else {
			throw new RuntimeException("Minus needs an integer argument");
		}
	}
	
	private Node preMinusExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class) {
			//Builtin I = new Builtin(Builtin.funct.I);
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst result = new NumberConst(- intX.getNumConst());
			//At resultAt = new At(I, result);
			return reduction(result);
		}
		else {
			throw new RuntimeException("Prefix Minus needs an int argument");
		}
	}
	
	private Node mulExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			//Builtin I = new Builtin(Builtin.funct.I);
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst intY = (NumberConst) reductionExpr2;
			NumberConst result = new NumberConst(intX.getNumConst() * intY.getNumConst());
			//At resultAt = new At(I, result);
			return reduction(result);
		}
		else {
			throw new RuntimeException("Multiplication needs two int arguments");
		}
	}
	
	private Node divExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			//Builtin I = new Builtin(Builtin.funct.I);
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst intY = (NumberConst) reductionExpr2;
			NumberConst result = new NumberConst(intX.getNumConst() / intY.getNumConst());
			//At resultAt = new At(I, result);
			return reduction(result);
		}
		else {
			throw new RuntimeException("Division needsd two int arguments");
		}
	}
	
	private Node notExpr() {
		At exprAt = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr = reduction(exprAt.getRight());
		if(reductionExpr.getClass() == BooleanConst.class) {
			//Builtin I = new Builtin(Builtin.funct.I);
			BooleanConst boolExpr = (BooleanConst) reductionExpr;
			BooleanConst result = new BooleanConst(!boolExpr.getBoolConst());
			//At resultAt = new At(I, result);
			return reduction(result);
		}
		else {
			throw new RuntimeException("Not needs a boolean argument");
		}
	}
	
	private Node condExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		At expr3At = (At) stack.lastElement();
		stack.pop();
		Node expr1 = reduction(expr1At.getRight());
		if(expr1.getClass() == BooleanConst.class) {
			BooleanConst boolExpr1 = (BooleanConst) expr1;
			//Builtin I = new Builtin(Builtin.funct.I);
			Node result;
			if(boolExpr1.getBoolConst()) {
				//result = new At(I, reduction(expr2At.getRight()));
				result = expr2At.getRight();
			}
			else {
				//result = new At(I, reduction(expr3At.getRight()));
				result = expr3At.getRight();
			}
			return reduction(result);
		}
		else {
			throw new RuntimeException("If argument needs to be a boolean");
		}
	}
	
	private Node andExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			BooleanConst boolExpr1 = (BooleanConst) reductionExpr1;
			BooleanConst boolExpr2 = (BooleanConst) reductionExpr2;
			//Builtin I = new Builtin(Builtin.funct.I);
			BooleanConst resultBool = new BooleanConst(boolExpr1.getBoolConst() && boolExpr2.getBoolConst());
			//At result = new At(I, resultBool);
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("And needs two boolean arguments");
		}
	}
	
	private Node orExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			BooleanConst boolExpr1 = (BooleanConst) reductionExpr1;
			BooleanConst boolExpr2 = (BooleanConst) reductionExpr2;
			//Builtin I = new Builtin(Builtin.funct.I);
			BooleanConst resultBool = new BooleanConst(boolExpr1.getBoolConst() || boolExpr2.getBoolConst());
			//At result = new At(I, resultBool);
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("Or needs two boolean arguments");
		}
	}
	
	private Node grtExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst numberExpr1 = (NumberConst) reductionExpr1;
			NumberConst numberExpr2 = (NumberConst) reductionExpr2;
			//Builtin I = new Builtin(Builtin.funct.I);
			BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() > numberExpr2.getNumConst());
			//At result = new At(I, resultBool);
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("\">\" needs two integer arguments");
		}
	}
	
	private Node lesExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst numberExpr1 = (NumberConst) reductionExpr1;
			NumberConst numberExpr2 = (NumberConst) reductionExpr2;
			//Builtin I = new Builtin(Builtin.funct.I);
			BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() < numberExpr2.getNumConst());
			//At result = new At(I, resultBool);
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("\"<\" needs two integer arguments");
		}
	}
	
	private Node equExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node expr1 = reduction(expr1At.getRight());
		Node expr2 = reduction(expr2At.getRight());
		if(expr1.getClass() == NumberConst.class) {
			expr1 = (NumberConst) expr1;
		}
		else if(expr2.getClass() == NumberConst.class) {
			expr2 = (NumberConst) expr2;
		}
		else if(expr1.getClass() == BooleanConst.class) {
			expr1 = (BooleanConst) expr1;
		}
		else if(expr2.getClass() == BooleanConst.class) {
			expr2 = (BooleanConst) expr2;
		}
		else if(expr1.getClass() == StringConst.class) {
			expr1 = (StringConst) expr1;
		}
		else if(expr2.getClass() == StringConst.class) {
			expr2 = (StringConst) expr2;
		}
		else {
			throw new RuntimeException("\"=\" needs two arguments of the type Integer, Boolean or String");
		}
		//Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst((expr1.toString().equals(expr2.toString())) && (expr1.getClass() == expr2.getClass()));
		//At result = new At(I, resultBool);
		return reduction(resultBool);
	}
	
	private Node geqExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst numberExpr1 = (NumberConst) reductionExpr1;
			NumberConst numberExpr2 = (NumberConst) reductionExpr2;
			//Builtin I = new Builtin(Builtin.funct.I);
			BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() >= numberExpr2.getNumConst());
			//At result = new At(I, resultBool);
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("\">=\" needs two integer arguments");
		}
	}
	
	private Node leqExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst numberExpr1 = (NumberConst) reductionExpr1;
			NumberConst numberExpr2 = (NumberConst) reductionExpr2;
			//Builtin I = new Builtin(Builtin.funct.I);
			BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() <= numberExpr2.getNumConst());
			//At result = new At(I, resultBool);
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("\"<=\" needs two integer arguments");
		}
	}
	
	private Node neqExpr() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node expr1 = reduction(expr1At.getRight());
		Node expr2 = reduction(expr2At.getRight());
		if(expr1.getClass() == NumberConst.class) {
			expr1 = (NumberConst) expr1;
		}
		else if(expr2.getClass() == NumberConst.class) {
			expr2 = (NumberConst) expr2;
		}
		else if(expr1.getClass() == BooleanConst.class) {
			expr1 = (BooleanConst) expr1;
		}
		else if(expr2.getClass() == BooleanConst.class) {
			expr2 = (BooleanConst) expr2;
		}
		else if(expr1.getClass() == StringConst.class) {
			expr1 = (StringConst) expr1;
		}
		else if(expr2.getClass() == StringConst.class) {
			expr2 = (StringConst) expr2;
		}
		else {
			throw new RuntimeException("\"~=\" needs two arguments of the type Integer, Boolean or String");
		}
		//Builtin I = new Builtin(Builtin.funct.I);
		BooleanConst resultBool = new BooleanConst(!((expr1.toString().equals(expr2.toString())) && (expr1.getClass() == expr2.getClass())));
		//At result = new At(I, resultBool);
		return reduction(resultBool);
	}
	
	private Node pair() {
		At expr1At = (At) stack.lastElement();
		stack.pop();
		At expr2At = (At) stack.lastElement();
		stack.pop();
		Node expr1 = reduction(expr1At.getRight());
		Node expr2 = reduction(expr2At.getRight());
		PairNode resultPair = new PairNode(expr1, expr2);
		//Builtin I = new Builtin(Builtin.funct.I);
		//At result = new At(I, resultPair);
		return reduction(resultPair);
	}
	
	private Node headOrTail(Node expr) {
		At exprAt = (At) stack.lastElement();
		stack.pop();
		Node result;
		Node exprRightReduction = reduction(exprAt.getRight()); 
		if(Builtin.isHd(expr)) {
			if(exprRightReduction.getClass() == PairNode.class) {
				PairNode exprRightPair = (PairNode) exprRightReduction;
				result = headReduction(exprRightPair);
			}
			else if(Builtin.isNil(exprRightReduction)) {
				throw new RuntimeException("An empty list has no hd elements.");
			}
			else {
				throw new RuntimeException("\"hd\" only works on lists");
			}
		}
		else if(Builtin.isTl(expr)){
			if(exprRightReduction.getClass() == PairNode.class) {
				PairNode exprRightPair = (PairNode) exprRightReduction;
				result = tailReduction(exprRightPair);
			}
			else if(Builtin.isNil(exprRightReduction)) {
				throw new RuntimeException("An empty list has no tl elements.");
			}
			else {
				throw new RuntimeException("\"tl\" only works on lists");
			}
		}
		else {
			throw new RuntimeException("Neither hd nor tl.");
		}
		return result;
	}
	
	private Node headReduction(PairNode toReduce){
		return toReduce.getLeft();
	}

	private Node tailReduction(PairNode toReduce) {
		return toReduce.getRight();
	}
}
