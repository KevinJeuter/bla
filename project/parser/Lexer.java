package parser;

import java.util.ArrayList;

import lexer.Token;

public class Lexer {

	private static ArrayList<Token> tokens = new ArrayList<Token>();
	
	public Lexer(ArrayList<Token> tokens) {
		Lexer.tokens = tokens;
	}
	
	public Token getLookahead() {
		return tokens.get(0);
	}
	
	public Token getToken() {
		if(tokens.size() > 0) {
			Token x = tokens.get(0);
			tokens.remove(0);
			return x;
		}
		else {
			throw new RuntimeException("there are no more tokens");
		}
	}
}
