package compiler;

import java.util.ArrayList;
import java.util.HashMap;

import ast.BooleanConst;
import ast.Builtin;
import ast.Node;
import ast.NumberConst;
import ast.Pair;
import ast.StringConst;
import ast.Var;
import ast.At;
import parser.DefHashMap;

//Das gehört zsm mit newCompiler. Funktioniert jedoch nicht.

public class Abstraction {
	
	Builtin S = new Builtin(Builtin.funct.S);
	Builtin K = new Builtin(Builtin.funct.K);
	Builtin I = new Builtin(Builtin.funct.I);
	
	DefHashMap defLeft;
	HashMap<String, Pair<ArrayList<String>, Node>> x = defLeft.returnHashMap();
	
	private boolean isConst(Node x) {
		return x.getClass() == Builtin.class || x.getClass() == StringConst.class || x.getClass() == NumberConst.class || x.getClass() == BooleanConst.class;
	}
	
	private boolean isVar(Node x) {
		return x.getClass() == Var.class;
	}
	
	public Abstraction(DefHashMap defLeft) {
		this.defLeft = defLeft;
	}
	
	public HashMap<String, Pair<ArrayList<String>, Node>> AbstractDefLeft() {
		for(int i = 0; i < x.size(); i++) {
			Pair<ArrayList<String>, Node> value = x.values().stream().skip(0).findFirst().get();
			for(int y = 0; y < value.first.size(); y++) {
				value.setValue(Abstr(value.first.get(0), value.second));
			}
		}
		return x;
	}
	
	public Node Abstr(String y, Node x) {
		if(isConst(x)) {
			At constAt = new At(K, x);
			return constAt;
		}
		else if(isVar(x)) {
			if(x.toString() == y) {
				return I;
			}
			else {
				At varAt = new At(K, x);
				return varAt;
			}
		}
		else {
			return I;
		}
	}

}
