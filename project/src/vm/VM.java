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
	
	//This will be the stack to reduce the program
	Stack<Node> stack = new Stack<Node>();
	
	Node defExpr;
	
	public VM(Def defExpr) {
		
		this.defExpr = defExpr.getExpr();
	}
	
	private boolean isConstant(Node x) {
		return ((x.getClass() == BooleanConst.class) || (x.getClass() == StringConst.class) || (x.getClass() == NumberConst.class));
	}

	//In the print method, the program will be reduced. Also Pairs will get further reduced, until they are printable here.
	//If there is no nil at the end of a list, throw an Error.
	public String print() {
		Node result = reduction(defExpr);
		if(isConstant(result)) {
			return result.toString();
		}
		else if(Builtin.isNil(result)) {
			return "(" + result.toString() + ")";
		}
		else if(PairNode.isPair(result)) {
			PairNode resultPair = (PairNode) result;
			reductionPair(resultPair);
			errorNoNil(resultPair);
			return result.toString();
		}
		else {
			throw new RuntimeException("The result is not a printable object. (No constant, nor a pair)");
		}
	}
	
	private void reductionPair(PairNode result) {
		result.setRight(reduction(result.getRight()));
		if(result.getRight().getClass() == PairNode.class) {
			PairNode resultRight = (PairNode) result.getRight();
			reductionPair(resultRight);
		}
		result.setLeft(reduction(result.getLeft()));
		if(result.getLeft().getClass() == PairNode.class) {
			PairNode resultLeft = (PairNode) result.getLeft();
			reductionPair(resultLeft);
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

	// * Main Part of reduction. Through recursion fill and empty Stack and reduce *
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
		At f = (At) stack.pop();
		At g = (At) stack.pop();
		At x = (At) stack.pop();
		
		At fExpr = new At(f.getRight(), x.getRight());
		At gExpr = new At(g.getRight(), x.getRight());
		At result = new At(fExpr, gExpr);
		return reduction(result);
	}
	
	private Node kExpr() {
		At exprAt = (At) stack.pop();
		stack.pop();
		return reduction(exprAt.getRight());
	}
	
	private Node iExpr() {
		At exprAt = (At) stack.pop();
		return reduction(exprAt.getRight());
	}
	
	private Node plusExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class){
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst intY = (NumberConst) reductionExpr2;
			NumberConst result = new NumberConst(intX.getNumConst() + intY.getNumConst());
			return reduction(result);
		}
		else {
			throw new RuntimeException("plus needs two integer arguments");
		}
	}
	
	private Node prePlusExpr() {
		At expr1At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class) {
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst result = new NumberConst(+ intX.getNumConst());
			return reduction(result);
		}
		else {
			throw new RuntimeException("Prefix Plus needs an integer argument");
		}
	}
	
	private Node minusExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst intY = (NumberConst) reductionExpr2;
			NumberConst result = new NumberConst(intX.getNumConst() - intY.getNumConst());
			return reduction(result);
		}
		else {
			throw new RuntimeException("Minus needs an integer argument");
		}
	}
	
	private Node preMinusExpr() {
		At expr1At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class) {
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst result = new NumberConst(- intX.getNumConst());
			return reduction(result);
		}
		else {
			throw new RuntimeException("Prefix Minus needs an int argument");
		}
	}
	
	private Node mulExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst intY = (NumberConst) reductionExpr2;
			NumberConst result = new NumberConst(intX.getNumConst() * intY.getNumConst());
			return reduction(result);
		}
		else {
			throw new RuntimeException("Multiplication needs two int arguments");
		}
	}
	
	private Node divExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst intX = (NumberConst) reductionExpr1;
			NumberConst intY = (NumberConst) reductionExpr2;
			NumberConst result = new NumberConst(intX.getNumConst() / intY.getNumConst());
			return reduction(result);
		}
		else {
			throw new RuntimeException("Division needsd two int arguments");
		}
	}
	
	private Node notExpr() {
		At exprAt = (At) stack.pop();
		Node reductionExpr = reduction(exprAt.getRight());
		if(reductionExpr.getClass() == BooleanConst.class) {
			BooleanConst boolExpr = (BooleanConst) reductionExpr;
			BooleanConst result = new BooleanConst(!boolExpr.getBoolConst());
			return reduction(result);
		}
		else {
			throw new RuntimeException("Not needs a boolean argument");
		}
	}
	
	private Node condExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		At expr3At = (At) stack.pop();
		Node expr1 = reduction(expr1At.getRight());
		if(expr1.getClass() == BooleanConst.class) {
			BooleanConst boolExpr1 = (BooleanConst) expr1;
			Node result;
			if(boolExpr1.getBoolConst()) {
				result = reduction(expr2At.getRight());
			}
			else {
				result = reduction(expr3At.getRight());
			}
			return result;
		}
		else {
			throw new RuntimeException("If argument needs to be a boolean");
		}
	}
	
	private Node andExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == BooleanConst.class && reductionExpr2.getClass() == BooleanConst.class) {
			BooleanConst boolExpr1 = (BooleanConst) reductionExpr1;
			BooleanConst boolExpr2 = (BooleanConst) reductionExpr2;
			BooleanConst resultBool = new BooleanConst(boolExpr1.getBoolConst() && boolExpr2.getBoolConst());
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("And needs two boolean arguments");
		}
	}
	
	private Node orExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == BooleanConst.class && reductionExpr2.getClass() == BooleanConst.class) {
			BooleanConst boolExpr1 = (BooleanConst) reductionExpr1;
			BooleanConst boolExpr2 = (BooleanConst) reductionExpr2;
			BooleanConst resultBool = new BooleanConst(boolExpr1.getBoolConst() || boolExpr2.getBoolConst());
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("Or needs two boolean arguments");
		}
	}
	
	private Node grtExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst numberExpr1 = (NumberConst) reductionExpr1;
			NumberConst numberExpr2 = (NumberConst) reductionExpr2;
			BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() > numberExpr2.getNumConst());
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("\">\" needs two integer arguments");
		}
	}
	
	private Node lesExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst numberExpr1 = (NumberConst) reductionExpr1;
			NumberConst numberExpr2 = (NumberConst) reductionExpr2;
			BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() < numberExpr2.getNumConst());
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("\"<\" needs two integer arguments");
		}
	}
	
	private boolean isEqu(Node expr1, Node expr2) {
		return expr1.toString().equals(expr2.toString()) && (expr1.getClass() == expr2.getClass());
	}
	
	private boolean isEquLists(PairNode l1, PairNode l2) {
		//to check if 2 lists are the same, we first set result to true. This way, as soon as there is one false
		//in the recursion it will calculate result && false, which will always be false from then on.
		//We check here for each element, if the reduction is the same by first completely reducing the first elements
		//of both lists. Then we check with isEqu(), if they are the same. We do this for every Element in the two lists,
		//until one of them is empty. Then we return the result.
		Node l1RedLeft = reduction(l1.getLeft());
		Node l2RedLeft = reduction(l2.getLeft());
		Node l1RedRight = reduction(l1.getRight());
		Node l2RedRight = reduction(l2.getRight());
		boolean result = true;
		if(l1RedLeft.getClass() == PairNode.class) {
			if(l2RedLeft.getClass() == PairNode.class) {
				PairNode l1RedLeftPair = (PairNode) l1RedLeft;
				PairNode l2RedLeftPair = (PairNode) l2RedLeft;
				result = result && isEquLists(l1RedLeftPair, l2RedLeftPair);
			}
			else {
				result = result && false;
			}
		}
		else {
			result = result && isEqu(l1RedLeft, l2RedLeft);
		}
		if(l1RedRight.getClass() == PairNode.class) {
			if(l2RedRight.getClass() == PairNode.class) {
				PairNode l1RedRightPair = (PairNode) l1RedRight;
				PairNode l2RedRightPair = (PairNode) l2RedRight;
				result = result && isEquLists(l1RedRightPair, l2RedRightPair);
			}
			else {
				result = result && false;
			}
		}
		else {
			result = result && isEqu(l1RedRight, l2RedRight);
		}
		
		return result;
	}
	
	private Node equExpr() {
		//If two Nodes aren't the same type, return false, otherwise check by isEqu()
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node expr1 = reduction(expr1At.getRight());
		Node expr2 = reduction(expr2At.getRight());
		BooleanConst falseNode = new BooleanConst(false);
		if(expr1.getClass() == NumberConst.class) {
			if(expr2.getClass() == NumberConst.class) {
				expr1 = (NumberConst) expr1;
				expr2 = (NumberConst) expr2;
			}
			else {
				return falseNode;
			}
		}
		else if(expr1.getClass() == BooleanConst.class) {
			if(expr2.getClass() == BooleanConst.class) {
				expr1 = (BooleanConst) expr1;
				expr2 = (BooleanConst) expr2;
			}
			else {
				return falseNode;
			}
		}
		
		else if(expr1.getClass() == StringConst.class) {
			if(expr2.getClass() == StringConst.class) {
				expr1 = (StringConst) expr1;
				expr2 = (StringConst) expr2;
			}
			else {
				return falseNode;
			}
		}
		else if(Builtin.isNil(expr1)) {
			if(Builtin.isNil(expr2)) {
				expr1 = (Builtin) expr1;
				expr2 = (Builtin) expr2;
			}
			else {
				return falseNode;
			}
		}		
		else if(expr1.getClass() == PairNode.class) {
			if(expr2.getClass() == PairNode.class) {
				PairNode expr1Pair = (PairNode) expr1;
				PairNode expr2Pair = (PairNode) expr2;
				BooleanConst isEqu = new BooleanConst(isEquLists(expr1Pair, expr2Pair));
				return isEqu;
			}
			else {
				return falseNode;
			}
		}
		else {
			throw new RuntimeException("\"=\" needs two arguments of the type Integer, Boolean, String or List");
		}
		BooleanConst resultBool = new BooleanConst(isEqu(expr1, expr2));
		return resultBool;
	}
	
	private Node geqExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst numberExpr1 = (NumberConst) reductionExpr1;
			NumberConst numberExpr2 = (NumberConst) reductionExpr2;
			BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() >= numberExpr2.getNumConst());
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("\">=\" needs two integer arguments");
		}
	}
	
	private Node leqExpr() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node reductionExpr1 = reduction(expr1At.getRight());
		Node reductionExpr2 = reduction(expr2At.getRight());
		if(reductionExpr1.getClass() == NumberConst.class && reductionExpr2.getClass() == NumberConst.class) {
			NumberConst numberExpr1 = (NumberConst) reductionExpr1;
			NumberConst numberExpr2 = (NumberConst) reductionExpr2;
			BooleanConst resultBool = new BooleanConst(numberExpr1.getNumConst() <= numberExpr2.getNumConst());
			return reduction(resultBool);
		}
		else {
			throw new RuntimeException("\"<=\" needs two integer arguments");
		}
	}
	
	private Node neqExpr() {
		//If two Nodes aren't the same type, return true, otherwise check by isEqu()
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node expr1 = reduction(expr1At.getRight());
		Node expr2 = reduction(expr2At.getRight());
		BooleanConst trueNode = new BooleanConst(true);
		if(expr1.getClass() == NumberConst.class) {
			if(expr2.getClass() == NumberConst.class) {
				expr1 = (NumberConst) expr1;
				expr2 = (NumberConst) expr2;
			}
			else {
				return trueNode;
			}
		}
		else if(expr1.getClass() == BooleanConst.class) {
			if(expr2.getClass() == BooleanConst.class) {
				expr1 = (BooleanConst) expr1;
				expr2 = (BooleanConst) expr2;
			}
			else {
				return trueNode;
			}
		}
		
		else if(expr1.getClass() == StringConst.class) {
			if(expr2.getClass() == StringConst.class) {
				expr1 = (StringConst) expr1;
				expr2 = (StringConst) expr2;
			}
			else {
				return trueNode;
			}
		}
		else if(Builtin.isNil(expr1)) {
			if(Builtin.isNil(expr2)) {
				expr1 = (Builtin) expr1;
				expr2 = (Builtin) expr2;
			}
			else {
				return trueNode;
			}
		}		
		else if(expr1.getClass() == PairNode.class) {
			if(expr2.getClass() == PairNode.class) {
				PairNode expr1Pair = (PairNode) expr1;
				PairNode expr2Pair = (PairNode) expr2;
				BooleanConst isEqu = new BooleanConst(!isEquLists(expr1Pair, expr2Pair));
				return isEqu;
			}
			else {
				return trueNode;
			}
		}
		else {
			throw new RuntimeException("\"=\" needs two arguments of the type Integer, Boolean, String or List");
		}
		BooleanConst resultBool = new BooleanConst(!isEqu(expr1, expr2));
		return resultBool;
	}
	
	private Node pair() {
		At expr1At = (At) stack.pop();
		At expr2At = (At) stack.pop();
		Node expr1 = expr1At.getRight();
		Node expr2 = expr2At.getRight();
		PairNode resultPair = new PairNode(expr1, expr2);
		return resultPair;
	}
	
	private Node headOrTail(Node expr) {
		//In the case of Hd or Tl we check if it is a non-empty PairNode, otherwise throw an Error.
		At exprAt = (At) stack.pop();
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
		return reduction(toReduce.getLeft());
	}

	private Node tailReduction(PairNode toReduce) {
		return reduction(toReduce.getRight());
	}
	
}
