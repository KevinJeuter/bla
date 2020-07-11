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
import parser.DefHashMap;

public class Compiler {
	
	private Def pDef;
	DefHashMap newDefLeft = new DefHashMap();
	
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
		
			Node x = value.second;
			
			//Compile for each variable
			for(int i = variables.size() - 1; i >= 0 ; i--) {
				x = abstraction(x, variables.get(i));
			}
			
			newDefLeftPair = new Pair<ArrayList<String>, Node>(variables, x);
			newDefLeft.put(key, newDefLeftPair);
		}
	}
	//Make new Def with newDef and original Expr
	public Def doCompile() {
		Def x = new Def(newDefLeft.returnHashMap(), pDef.getExpr());
		return x;
	}
	
	//Check if Node is a constant
	private boolean isConst(Node x) {
		return x.getClass() == Builtin.class || x.getClass() == StringConst.class || x.getClass() == NumberConst.class || x.getClass() == BooleanConst.class;
	}
	
	//Compile the Node
	private Node abstraction(Node x, String y) {
		//If there is an at, make a new at node with S and the abstraction of the left at and the abstraction of the right at
		if(At.isAt(x)) {
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
		else if(Var.isVar(x)) {
			//If its a variable, check if the variable is the same as the parameter of the def, if yes return I otherwise return at of K and var
			if(y.contentEquals(x.toString())) {
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
	
}
