package compiler;

import java.util.ArrayList;
import java.util.HashMap;

import ast.Def;
import ast.Node;
import ast.NumberConst;
import ast.Pair;
import ast.StringConst;
import ast.Var;
import ast.At;
import ast.BooleanConst;
import ast.Builtin;

public class Compiler {
	
	private Def pDef;
	HashMap<String, Pair<ArrayList<String>, Node>> newDefLeft = new HashMap<String, Pair<ArrayList<String>, Node>>();
	
	public Compiler(Def parser) {
		pDef = parser;
		for(int y = 0; y < pDef.getDefinitions().size(); y++) {
			//Get Key at position counter (Def name)
			String key = pDef.getDefinitions().keySet().stream().skip(y).findFirst().get();
			//Get Value at position counter (Def Var + Node)
			Pair<ArrayList<String>, Node> value = pDef.getDefinitions().values().stream().skip(y).findFirst().get();
		
			//Make array list of all variables
			ArrayList<String> variables = value.first;
		
			//Make new Pair of variables and compiled Node
			Pair<ArrayList<String>, Node> newDefLeftPair;
		
			Node x = abstraction(value.second, variables.get(variables.size() - 1));
			
			//Compile for each variable
			for(int i = variables.size() - 2; i >= 0; i--) {
				x = abstractionParameters(x, variables.get(i));
			}
			
			newDefLeftPair = new Pair<ArrayList<String>, Node>(variables, x);
			newDefLeft.put(key, newDefLeftPair);
		}
	}
	//Make new Def with newDef and original Expr
	public Def doCompile() {
		Def x = new Def(newDefLeft, pDef.getExpr());
		return x;
	}
	
	//Check if Node is a constant
	private boolean isConst(Node x) {
		return x.getClass() == Builtin.class || x.getClass() == StringConst.class || x.getClass() == NumberConst.class || x.getClass() == BooleanConst.class;
	}
	
	//Check if Node is a variable
	private boolean isVar(Node x) {
		return x.getClass() == Var.class;
	}
	
	//Check if Node is an At
	private boolean isAt(Node x) {
		return x.getClass() == At.class;
	}
	
	//Compile the Node
	private Node abstraction(Node x, String y) {
		//If there is an at, make a new at node with S and the abstraction of the left at and the abstraction of the right at
		if(isAt(x)) {
			Builtin S = new Builtin(Builtin.funct.S);
			At z = (At) x;
			At sAt1 = new At(S, abstraction(z.getLeft(), y));
			At sAt2 = new At(sAt1, abstraction(z.getRight(), y));
			return sAt2;
		}
		//If it is a constant, make an at with K and the constant
		else if(isConst(x)) {
			Builtin K = new Builtin(Builtin.funct.K);
			At constant = new At(K, x);
			return constant;
		}
		else if(isVar(x)) {
			//if it is a variable, check if it is a defined method of the def node. if yes, return the node.
			if(newDefLeft.containsKey(x.toString())) {
				return x;
			}
			//If its a variable, check if the variable is the same as the parameter of the def, if yes return I otherwise return at of K and var
			else if(y.contentEquals(x.toString())) {
				Builtin I = new Builtin(Builtin.funct.I);
				return I;
			}
			else {
				Builtin K = new Builtin(Builtin.funct.K);
				At varAt = new At(K, x);
				return varAt;
			}
		}
		else {
			throw new RuntimeException("not a valid node");
		}
	}
	
	//Method recursion for other parameters
	private Node abstractionParameters(Node x, String y) {
		if(isAt(x)) {
			//if there is an at go through left and right node with recursion
			At z = (At) x;
			At newX = new At(abstractionParameters(z.getLeft(), y), abstractionParameters(z.getRight(), y));
			return newX;
		}
		/* 
		 * oder: (falls kein K mehr kommen soll) anstatt 106-111
		 * if(isAt(x)) {
			At z = (At) x;
			if(z.getLeft().toString().equals(K.toString()) && z.getRight().getClass() == Var.class) {
				return abstractionParameters(z.getRight(), y);
			}
			else {
				At newX = new At(abstractionParameters(z.getLeft(), y), abstractionParameters(z.getRight(), y));
				return newX;
			}
		}
		 */
		else if(isConst(x)) {
			//if the node is a constant return it
			return x;
		}
		else if(isVar(x)) {
			//if it is a variable, check if it is a defined method of the def node. if yes, return the node.
			if(newDefLeft.containsKey(x.toString())) {
				return x;
			}
			else if(y.contentEquals(x.toString())) {
				//If its a variable, check if the variable is the same as the parameter of the def, if yes return I otherwise return at of K and var
				Builtin I = new Builtin(Builtin.funct.I);
				return I;
			}
			else {		
				Builtin K = new Builtin(Builtin.funct.K);	//**
				At varAt = new At(K, x);					//wenn das at kommentar von zeile 110-121 stimmt, kann das hier weg
				return varAt;								//**
			}												//**
		}
		else {
			throw new RuntimeException("not a valid node");
		}
	}
	
}
