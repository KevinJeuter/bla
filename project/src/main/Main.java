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
import visitors.ReplaceVisitor;
import parser.DefHashMap;

public class Main {

	public static void main(String[] args) throws IOException {
		
		//If no file is selected: Error
		if(args.length <= 0) {
			throw new RuntimeException("No file selected.");
		}
		
		// *Lexer*
		String src = fileToString(args[0]); //Eingabeprogramm	
		
		//If file is empty: Error
		if(src.isEmpty()) {
			throw new RuntimeException("File is empty.");
		}

		lexer.Lexer l = new lexer.Lexer(src); //Lexe das Eingabeprogramm
		
		// *Parser*
		parser.Parser p = new parser.Parser(l); //instanziiere Parser
		Def ast = p.system();	//parse das gelexte Programm
		
		// *AST Debugging*
		DotVisitor v = new DotVisitor();
		v.visit(ast);
		System.out.println(v.getDotResult()); //printe den erzeugten Baum vom Parser
		
		// *Compiler*
		
		// *Stage 1: Abstraction*
		compiler.Compiler c = new compiler.Compiler(ast); //Instanziiere den Compiler
		Def byteCode = c.getCompilerResult(); //Abstraktion nach David Turner
		//DefHashMap byteCodeDefHashMap = new DefHashMap(byteCode.getDefinitions()); //Mache ein DefHashMap aus der Abstr.
		
		// *Stage 2: Replace Visitor*
		ReplaceVisitor v2 = new ReplaceVisitor(); 
		Def replacedByteCode = (Def) v2.visit(byteCode); //Ersetze die Parameter durch die Nodes
		
		// *VM*
		vm.VM vm = new vm.VM(replacedByteCode); //Füge das kompilierte Programm in die Reduktionsmaschine

		// *Reduce and Print*
		System.out.println(vm.print()); //Reduziere das Programm und printe das Ergebnis
		
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
		DefHashMap whereHash = new DefHashMap();
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
		DefHashMap defHash = new DefHashMap();
		defHash.put("null", defPair);
		
		Def bspDef = new Def(defHash, bspWhere);
		
		return bspDef;
		
	}

}