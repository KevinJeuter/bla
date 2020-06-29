package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import ast.At;
import ast.Builtin;
import ast.Def;
import ast.Node;
import ast.NumberConst;
import ast.Pair;
import ast.StringConst;
import ast.Var;
import ast.Where;
import lexer.Constants;
import lexer.Identifier;
import visitors.DotVisitor;

public class Main {

	public static void main(String[] args) throws IOException {
		
//		NumberConst x1 = new NumberConst(1);
//		NumberConst x41 = new NumberConst(41);
//		At root = new At(x1, x41);
		
		if(args.length <= 0) {
			throw new RuntimeException("No file selected.");
		}
		
		String src = fileToString(args[0]);
		
		lexer.Lexer l = new lexer.Lexer(src);
		
		parser.Parser p = new parser.Parser(l);
		
		Def pDef = p.system();
		
		DotVisitor v = new DotVisitor();
		
		v.visit(pDef);
		
		System.out.println(v.getDotResult());
		
		compiler.Compiler c = new compiler.Compiler(pDef);
		
		DotVisitor v2= new DotVisitor();
		
		v2.visit(c.doCompile());
		
		System.out.println(v2.getDotResult());

	}
	
	private static String fileToString(String fileName) throws IOException {
		return Files.readString(Path.of(fileName));
	}
	
	public static Def manualAst() {
		
		//Baue where
		NumberConst one = new NumberConst(1);
		Builtin colon = new Builtin(Builtin.funct.COLON);
		StringConst nil = new StringConst("nil");
		
		Var l = new Var("l");
		Var nul = new Var("null");
		
		ArrayList<String> whereList = new ArrayList<String>();
		whereList.add("l");
		
		//At Knoten : @ 1
		At colonOneAt = new At(colon, one);
		//At Knoten var(null) @ l
		At nullLAt = new At(nul, l);
		//At Knoten @ @ nil auf rechter Seite
		At atNilAtRight = new At(colonOneAt, nil);
		//Paar Liste mit Parameter (hier l) und dem @ knoten. 
		Pair<ArrayList<String>, Node> wherePair = new Pair<ArrayList<String>, Node>(whereList, atNilAtRight);
		//put in hashmap
		HashMap<String, Pair<ArrayList<String>, Node>> whereHash = new HashMap<String, Pair<ArrayList<String>, Node>>();
		whereHash.put("l", wherePair);
		//Where knoten
		Where bspWhere = new Where(whereHash, nullLAt);
		
		//Baue def
		
		Builtin equ = new Builtin(Builtin.funct.EQU);
		Var xs = new Var("xs");
		
		//At Knoten = @ var(xs)
		At equXsAt = new At(equ, xs);
		//at Knoten @ @ nil
		At atNilAtLeft = new At(equXsAt, nil);
		//parameter für null
		ArrayList<String> nullParam = new ArrayList<String>();
		nullParam.add("xs");
		//paar aus namen und knoten für def
		Pair<ArrayList<String>, Node> defPair = new Pair<ArrayList<String>, Node>(nullParam, atNilAtLeft);
		//hashmap für def
		HashMap<String, Pair<ArrayList<String>, Node>> defHash = new HashMap<String, Pair<ArrayList<String>, Node>>();
		defHash.put("null", defPair);
		
		Def bspDef = new Def(defHash, bspWhere);
		
		return bspDef;
		
	}

}