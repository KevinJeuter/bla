package lexer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

public class LexerTest {

	@Test
	public void testLexerTokens() {
		ArrayList<Token> tokens = new ArrayList<Token>();
		Keywords ifKey = new Keywords("if");
		Constants three = new Constants("3");
		Symbols les = new Symbols("<");
		Constants two = new Constants("2");
		Keywords thenKey = new Keywords("then");
		Identifier x = new Identifier("x");
		Keywords elseKey = new Keywords("else");
		Identifier y = new Identifier("y");
		Specials eof = new Specials("eof");
		tokens.add(ifKey);
		tokens.add(three);
		tokens.add(les);
		tokens.add(two);
		tokens.add(thenKey);
		tokens.add(x);
		tokens.add(elseKey);
		tokens.add(y);
		tokens.add(eof);
		
		Lexer testLex = new Lexer("if 3 < 2 then x else y");
		
		assertEquals(tokens.toString(), testLex.getTokens().toString());
	}
	
	@Test
	public void testLexerComment() {
		ArrayList<Token> tokens = new ArrayList<Token>();
		Keywords ifKey = new Keywords("if");
		Constants three = new Constants("3");
		Symbols les = new Symbols("<");
		Constants two = new Constants("2");
		Keywords thenKey = new Keywords("then");
		Identifier x = new Identifier("x");
		Keywords elseKey = new Keywords("else");
		Identifier y = new Identifier("y");
		Specials eof = new Specials("eof");
		tokens.add(ifKey);
		tokens.add(three);
		tokens.add(les);
		tokens.add(two);
		tokens.add(thenKey);
		tokens.add(x);
		tokens.add(elseKey);
		tokens.add(y);
		tokens.add(eof);
		
		Lexer testLex = new Lexer("if 3 < 2 then x else y || x + 2 - 3 * y _ g");
		
		assertEquals(tokens.toString(), testLex.getTokens().toString());
	}
	
	@Test
	public void testLexerWhitespace() {
		Lexer testLex1 = new Lexer("if               3	 <		 2 then		 x else y");
		
		Lexer testLex2 = new Lexer("if 3 < 2 then x else y || x + 2 - 3 * y _ g");
		
		assertEquals(testLex1.getTokens().toString(), testLex2.getTokens().toString());
	}
	
	@Test
	public void testLexerNewLine() {
		Lexer testLex1 = new Lexer("if 3 < 2" + "\r\n" +  "then x" + "\r\n" + "else y || x + 2 - 3 * y _ g");
		
		Lexer testLex2 = new Lexer("if 3 < 2 then x else y || x + 2 - 3 * y _ g");
		
		assertEquals(testLex1.getTokens().toString(), testLex2.getTokens().toString());
	}
	
	@Test
	public void testLexerConst() {
		//Check if "2" is StringConst and 2 is NumConst
		
		Lexer testLex1 = new Lexer("\"2\"");
		
		Lexer testLex2 = new Lexer("2");
		
		assertNotEquals(testLex1.getTokens().toString(), testLex2.getTokens().toString());
		assertTrue(testLex1.getTokens().get(0).getClass() == Constants.class);
		assertTrue(testLex2.getTokens().get(0).getClass() == Constants.class);
		Constants testLex1Const = (Constants) testLex1.getTokens().get(0);
		Constants testLex2Const = (Constants) testLex2.getTokens().get(0);
		String constString = "<Constant string: \"2\">";
		String constNum = "<Constant num: 2>";
		assertEquals(testLex1Const.toString(), constString);
		assertEquals(testLex2Const.toString(), constNum);
	}
}