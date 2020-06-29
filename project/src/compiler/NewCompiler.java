package compiler;

import java.util.ArrayList;
import java.util.HashMap;

import ast.Def;
import ast.Node;
import ast.Pair;
import parser.DefHashMap;

//Das gehört zsm mit Abstraction. Funktioniert jedoch nicht.

public class NewCompiler {

	Def parser;
	DefHashMap defLeft = new DefHashMap(parser.getDefinitions());
	
	public NewCompiler(Def parser) {
		this.parser = parser;
	}
	
	public Def abstractParser() {
		Abstraction x = new Abstraction(defLeft);
		Def y = new Def(x.AbstractDefLeft(), parser.getExpr());
		return y;
	}

}
