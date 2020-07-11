package compiler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ast.Node;
import ast.Builtin;
import ast.Def;
import ast.At;
import ast.NumberConst;
import ast.Var;
import ast.Pair;
import lexer.Lexer;
import parser.Parser;
import java.util.ArrayList;
import parser.DefHashMap;

class CompilerTest {

	@Test
	void testCompiler() {
		Lexer l = new Lexer("def addTo41 x = 41 + x . addTo41 1");
		Parser p = new Parser(l);
		Def pDef = p.system();
		Compiler c = new Compiler(pDef);
		Def cDef = c.doCompile();

		Builtin S = new Builtin(Builtin.funct.S);
		Builtin K = new Builtin(Builtin.funct.K);
		Builtin I = new Builtin(Builtin.funct.I);
		Builtin plus = new Builtin(Builtin.funct.PLUS);
		NumberConst fourtyone = new NumberConst(41);
		NumberConst one = new NumberConst(1);
		Var addTo41 = new Var("addTo41");
		
		At plusAt = new At(K, plus);
		At fourtyOneAt = new At(K, fourtyone);
		At sPlusAt = new At(S, plusAt);
		At sPlusFourtyOneAt = new At(sPlusAt, fourtyOneAt);
		At sRestAt = new At(S, sPlusFourtyOneAt);
		At allLeftAt = new At(sRestAt, I);
		
		ArrayList<String> testDefList = new ArrayList<String>();
		testDefList.add("x");
		
		Pair<ArrayList<String>, Node> testDefPair = new Pair<ArrayList<String>, Node>(testDefList, allLeftAt);
		
		DefHashMap testDefHashMap = new DefHashMap();
		testDefHashMap.put("addTo41", testDefPair);
		
		At allRightAt = new At(addTo41, one);
		
		Def testDef = new Def(testDefHashMap.returnHashMap(), allRightAt);
		
		assertEquals(cDef.toString(), testDef.toString());
	}
	
}
