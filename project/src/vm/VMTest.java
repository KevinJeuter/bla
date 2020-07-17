package vm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import ast.Def;
import ast.BooleanConst;
import ast.NumberConst;
import compiler.Compiler;
import lexer.Lexer;
import parser.DefHashMap;
import parser.Parser;
import visitors.ReplaceVisitor;

class VMTest {

	@Test
	void testVM1() {
		Lexer l = new Lexer("def addTo41 x = 41 + x . addTo41 1");
		Parser p = new Parser(l);
		Def pDef = p.system();
		Compiler c = new Compiler(pDef);
		Def cDef = c.getResult();
		DefHashMap cDefDefinitions = new DefHashMap(cDef.getDefinitions());
		ReplaceVisitor v2 = new ReplaceVisitor();
		Def newCDef = (Def) v2.visit(cDef); 
		vm.VM reductionOfCompilerDef = new vm.VM(newCDef);
		
		NumberConst fourtytwo = new NumberConst(42);
		
		assertEquals(reductionOfCompilerDef.reduction().toString(), fourtytwo.toString());
	}
	
	@Test
	void testVM2() {
		Lexer l = new Lexer("def isSmaller x y = if x < y then true else false. isSmaller 1 3");
		Parser p = new Parser(l);
		Def pDef = p.system();
		Compiler c = new Compiler(pDef);
		Def cDef = c.getResult();
		DefHashMap cDefDefinitions = new DefHashMap(cDef.getDefinitions());
		ReplaceVisitor v2 = new ReplaceVisitor();
		Def newCDef = (Def) v2.visit(cDef); 
		vm.VM reductionOfCompilerDef = new vm.VM(newCDef);
		
		BooleanConst result = (BooleanConst) reductionOfCompilerDef.reduction();
		
		assertTrue(result.getBoolConst());
	}
	
	@Test
	void testVM3() {
		Lexer l = new Lexer("def isSmaller x y = if x < y then true else false. isSmaller 3 3");
		Parser p = new Parser(l);
		Def pDef = p.system();
		Compiler c = new Compiler(pDef);
		Def cDef = c.getResult();
		DefHashMap cDefDefinitions = new DefHashMap(cDef.getDefinitions());
		ReplaceVisitor v2 = new ReplaceVisitor();
		Def newCDef = (Def) v2.visit(cDef); 
		vm.VM reductionOfCompilerDef = new vm.VM(newCDef);
		
		BooleanConst result = (BooleanConst) reductionOfCompilerDef.reduction();
		
		assertFalse(result.getBoolConst());
	}
	
	@Test
	void testVMList() {
		Lexer l = new Lexer("def getHeadOfTail l = hd (tl l) . getHeadOfTail (1:2:3:nil)");
		Parser p = new Parser(l);
		Def pDef = p.system();
		Compiler c = new Compiler(pDef);
		Def cDef = c.getResult();
		DefHashMap cDefDefinitions = new DefHashMap(cDef.getDefinitions());
		ReplaceVisitor v2 = new ReplaceVisitor();
		Def newCDef = (Def) v2.visit(cDef); 
		vm.VM reductionOfCompilerDef = new vm.VM(newCDef);
		
		NumberConst two = new NumberConst(2);
		
		assertEquals(reductionOfCompilerDef.reduction().toString(), two.toString());
	}

}
