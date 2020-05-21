package main;

import java.util.ArrayList;

import main.Symbols.sym;

public abstract class Lexer {

	// point to current character
	private static int ptr = 0;

	// store the final tokens
	private static ArrayList<String> tokens = new ArrayList<String>();

	private static char[] symbols = { '+', '-', '*', '/', '=', '!', '<', '>', '.', '(', ')', ',', ';' }; // Vlt. nicht
																											// optimal,
																											// wusste
																											// aber
																											// nicht wie
																											// ich sonst
																											// an die
																											// Zeichen
																											// kommen
																											// sollte.
	// sasl program to be lexed
	private static String src = "hallo, mein name ist kevin. def x = x+1 || hdsljsdgsdgdsfg";

	public static void main(String[] args) {

		for (;;) { // forever

			if (ptr > src.length()) {
				break;
			}

			else if (ptr == src.length()) { // Mir ist keine andere Bedingung eingefallen... Gilt nur, weil nur "eof"
				lexSpecial();
			}

			else if (isComment()) { // Comment. Also wird rest übersprungen
				break;
			}

			else if (Character.isDigit(src.charAt(ptr))) {
				lexDigit(); // constant:num
			}

			else if (src.charAt(ptr) == '"') {
				lexConstString(); // constant:string
			} else if (Character.isLetter(src.charAt(ptr))) {
				// keyword or identfier
				lexWord();
			}

			else if (src.charAt(ptr) == ' ') {
				skipWhitespace();
			}

			else if (isSymb(src.charAt(ptr))) {
				lexSymbol();
			}

			else {
				throw new RuntimeException("there is a non-valid token");
			}

		}

		printTokenList();

	}

	private static boolean isSymb(char test) {

		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i] == test) {
				return true;
			}
		}

		return false;
	}

	private static void lexDigit() {

		// continue with lexing until NOT digit
		// -> end pointer
		// create token and insert in result

		String token = "";

		for (int i = ptr; i < src.length(); i++) {
			if (Character.isDigit(src.charAt(ptr))) {
				token = token + Character.toString(src.charAt(ptr));
				ptr++;
			} else {
				break;
			}
		}

		Constants konst = new Constants(token);

		tokens.add(konst.toString());

	}

	private static void lexConstString() { // unsicher, da ich nicht wusste wie nachprüfen

		// continue with lexing until '"'
		// -> end pointer
		// create token and insert in result

		String token = "";

		for (int i = ptr; i < src.length(); i++) {
			if (src.charAt(ptr) != '"') {
				token = token + Character.toString(src.charAt(ptr));
				ptr++;
			} else {
				break;
			}
		}

		Constants konst = new Constants(token);

		tokens.add(konst.toString());

	}

	private static void lexWord() {

		// continue with lexing until NOT word
		// -> end pointer
		// create token and insert in result
		// either ID or keyword

		String token = "";

		for (int i = ptr; i < src.length(); i++) {
			if (Character.isLetter(src.charAt(ptr))) {
				token = token + Character.toString(src.charAt(ptr));
				ptr++;
			} else {
				break;
			}
		}

		int notKW = 0;
		for (Keywords.kw k : Keywords.kw.values()) {
			if (k.toString().toLowerCase().equals(token.toLowerCase()) || token.toLowerCase().equals("def")) {

				Keywords key = new Keywords(token);

				tokens.add(key.toString());
				notKW = 0;
				break;
			} else {
				notKW = 1;
			}
		}

		if (notKW == 1) {
			Identifier id = new Identifier(token);

			tokens.add(id.toString());
		}

	}

	private static void lexSymbol() {

		// continue with lexing until NOT symbol
		// -> end pointer
		// create token and insert in result

		String token = "";

		for (int i = ptr; i < src.length(); i++) {
			if (isSymb(src.charAt(ptr))) {
				token = token + Character.toString(src.charAt(ptr));
				ptr++;
			} else {
				break;
			}
		}

		switch (token) { // vielleicht auch nicht die beste lösung
		case "+":
		case "-":
		case "*":
		case "/":
		case "<=":
		case "=":
		case "!=":
		case ">=":
		case "<":
		case ">":
		case ".":
		case ",":
		case "(":
		case ")":
		case ";":
			Symbols symbol = new Symbols(token);

			tokens.add(symbol.toString());
			break;
		}
	}

	private static void lexSpecial() {

		Specials spec = new Specials("eof");

		tokens.add(spec.toString());

		ptr++;

	}

	private static void skipWhitespace() {
		ptr++;
	}
	
	private static boolean isComment() {
		return (ptr < src.length() && src.charAt(ptr) == '|' && src.charAt(ptr + 1) == '|');
	}

	private static void printTokenList() {
		for (String i : tokens) {
			System.out.println(i);
		}
	}

}