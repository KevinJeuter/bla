package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.*;

import lexer.Lexer;
import ast.Def;
import ast.Node;
import ast.At;
import ast.NumberConst;
import ast.Pair;
import ast.StringConst;
import ast.Builtin;
import ast.Var;
import parser.DefHashMap;

class ParserTest {
	
	//Need to activate toString Code in At Class

	@Test
	void testParser1() {
		Lexer l = new Lexer("(41 + 1) = 42");
		Parser p = new Parser(l);
		Def pDef = p.system();
		
		NumberConst fourtyone = new NumberConst(41);
		NumberConst one = new NumberConst(1);
		NumberConst fourtytwo = new NumberConst(42);
		Builtin plus = new Builtin(Builtin.funct.PLUS);
		Builtin equ = new Builtin(Builtin.funct.EQU);
		
		DefHashMap testDefHash = new DefHashMap();
		
		//Make empty left side of Def
		ArrayList<String> emptyList = new ArrayList<String>();
		emptyList.add("empty");
		StringConst emptyNode = new StringConst("empty");
		Pair<ArrayList<String>, Node> emptyPair = new Pair<ArrayList<String>, Node>(emptyList, emptyNode);
		testDefHash.put("empty", emptyPair);
				
		//Make right side of Def
		At plusAt41 = new At(plus, fourtyone);
		At plusAt1 = new At(plusAt41, one);
		At equPlus = new At(equ, plusAt1);
		At equ42 = new At(equPlus, fourtytwo);
				
		Def testDef = new Def(testDefHash, equ42);
		
		assertEquals(pDef.toString(), testDef.toString());
	}
	
	@Test
	void testParserParensRemove() {
		Lexer l1 = new Lexer("(41 + 1) = 42");
		Parser p1 = new Parser(l1);
		Def pDef1 = p1.system();
		
		Lexer l2 = new Lexer("41 + 1 = 42");
		Parser p2 = new Parser(l2);
		Def pDef2 = p2.system();
		
		assertEquals(pDef1.toString(), pDef2.toString());
	}
	
	@Test
	void testParserCond() {
		Lexer l1 = new Lexer("if x then y else z");
		Parser p1 = new Parser(l1);
		Def pDef1 = p1.system();
		
		Builtin cond = new Builtin(Builtin.funct.COND);
		Var x = new Var("x");
		Var y = new Var("y");
		Var z = new Var("z");
		At cond1At = new At(cond, x);
		At cond2At = new At(cond1At, y);
		At cond3At = new At(cond2At, z);
		
		assertEquals(pDef1.getExpr().toString(), cond3At.toString());
	}

}
