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
	private int counter = 0;
	Builtin S = new Builtin(Builtin.funct.S);
	Builtin K = new Builtin(Builtin.funct.K);
	Builtin I = new Builtin(Builtin.funct.I);
	HashMap<String, Pair<ArrayList<String>, Node>> newDefLeft = new HashMap<String, Pair<ArrayList<String>, Node>>();
	
	public Compiler(Def parser) {
		pDef = parser;
		//Get Key at position counter (Def name)
		String key = pDef.getDefinitions().keySet().stream().skip(counter).findFirst().get();
		//Get Value at position counter (Def Var + Node)
		Pair<ArrayList<String>, Node> value = pDef.getDefinitions().values().stream().skip(counter).findFirst().get();
		
		//Make array list of all variables
		ArrayList<String> variables = value.first;
		
		//Make new Pair of variables and compiled Node
		Pair<ArrayList<String>, Node> newDefLeftPair;
		
		//Compile for each variable
		for(int i = variables.size() - 1; i >= 0; i--) {
			newDefLeftPair = new Pair<ArrayList<String>, Node>(variables, compile(value.second, variables.get(i)));
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
	
	//Compile the Node
	private Node compile(Node x, String y) {
		if(isConst(x)) {
			At constant = new At(K, x);
			return constant;
		}
		else if(isVar(x)) {
			if(y == x.toString()) {
				return I;
			}
			else {
				At varAt = new At(K, x);
				return varAt;
			}
		}
		else {
			return I;
			//compileAt(x) geht jedoch nicht, da wir Node haben und kein At. 
		}
	}
	
	/*
	private Node compileAt(At x) {
		At sAt1 = new At(S, compile(x.getLeft()));
		At sAt2 = new At(sAt1, compile(x.getRight()));
		return sAt2;
	}
	*/
}
