package lexer;

import java.util.ArrayList;

import lexer.Token;

public class Lexer {

	// point to current character
	private int ptr = 0;

	// store the final tokens
	private ArrayList<Token> tokens = new ArrayList<Token>();

	public Lexer(String file){
		
		String src = file;

		for (;;) { // forever	

			if (ptr == src.length()) { 
				lexSpecial();
				break;
			}

			else if (isComment(src)) { // Comment. Also wird bis \n übersprungen, falls vorhanden
				skipComment(src);
			}

			else if (isEmptyLine(src.charAt(ptr)) || isTab(src.charAt(ptr))) {
				skipWhitespace();
			}
			
			else if(isNewLine(src)) {
				//isNewLine ueberspringt auch newLines
			}

			else if (Character.isDigit(src.charAt(ptr))) {
				lexDigit(src); // constant:num
			}

			else if (src.charAt(ptr) == '"') {
				lexConstString(src); // constant:string
			} 
			
			else if (Character.isLetter(src.charAt(ptr))) {
				// keyword or identfier or boolean
				lexWord(src);
			}

			else if (Symbols.isSymbol(src.charAt(ptr))) {
				lexSymbol(src);
			}
			
			else if(src.charAt(ptr) == '[') {
				throw new RuntimeException("Lists in the form of \"[x:y:z]\" are not implemented. Try \"(x:y:z:nil)\" instead.");
			}

			else {
				throw new RuntimeException("there is a non-valid token");
			}

		}
		printTokenList();
		
	}

	private void lexDigit(String file) {

		// continue with lexing until NOT digit
		// -> end pointer
		// create token and insert in result

		String token = "";

		for (int i = ptr; i < file.length(); i++) {
			if (Character.isDigit(file.charAt(ptr))) {
				token = token + Character.toString(file.charAt(ptr));
				ptr++;
			} else {
				break;
			}
		}

		Constants konst = new Constants(token);

		tokens.add(konst);

	}

	private void lexConstString(String file) { // unsicher, da ich nicht wusste wie nachprüfen

		// continue with lexing until '"'
		// -> end pointer
		// create token and insert in result

		String token = "";


		token = token + Character.toString('"');
		ptr++;
		for (int i = ptr; i < file.length(); i++) {
			if (file.charAt(ptr) != '"') {
				if(ptr == file.length() - 1 && file.charAt(ptr) != '"') {
					throw new RuntimeException("not a valid string");
				}
				else {
					token = token + Character.toString(file.charAt(ptr));
					ptr++;
				}
			} else {
				token = token + Character.toString('"');
				ptr++;
				break;
			}
		}

		Constants konst = new Constants(token);

		tokens.add(konst);

	}

	private void lexWord(String file) {

		// continue with lexing until NOT word
		// -> end pointer
		// create token and insert in result
		// either ID or keyword

		String token = "";

		for (int i = ptr; i < file.length(); i++) {
			if (Character.isLetter(file.charAt(ptr)) || file.charAt(ptr) == '_' || Character.isDigit(file.charAt(ptr))) {
				token = token + Character.toString(file.charAt(ptr));
				ptr++;
			} else {
				break;
			}
		}
		
		if (Keywords.isKeyword(token)) {
			Keywords key = new Keywords(token);
			tokens.add(key);
			} 
		
		else if(Constants.isBool(token)){
			Constants konst = new Constants(token);
			
			tokens.add(konst);
			} 
		
		else {
			Identifier id = new Identifier(token);

			tokens.add(id);
			}
	}

	private void lexSymbol(String file) {

		// continue with lexing until NOT symbol
		// -> end pointer
		// create token and insert in result

		String token = "";
	
			for (int i = ptr; i < file.length(); i++) {
				if (Symbols.isSymbol(file.charAt(ptr))) {
					token = token + Character.toString(file.charAt(ptr));
					ptr++;
					if(!token.equals(Symbols.les) && !token.equals(Symbols.grt) && !token.equals(Symbols.not)) {	//Wenn es nicht mit <, >, ! anfängt, haben alle anderen Symbole nur Länge 1. Um z.b. x=(x+2) zu ermöglichen =( wäre kein Symbol sonst.
						break;
					}
				} else {
					break;
				}
			}

		
		Symbols symbol = new Symbols(token);

		tokens.add(symbol);
		
	}

	private void lexSpecial() {

		Specials spec = new Specials("eof");

		tokens.add(spec);

		ptr++;

	}
	
	private void skipWhitespace() {
		ptr++;
	}

	private boolean isEmptyLine(char test) {
		return test == ' ';
	}
	
	private boolean isTab(char test) {
		return test == '\t';
	}
	
	private boolean isNewLine(String test) {
		String newLine = "";
		
		if(test.charAt(ptr) == '\r') {
			//Windows, DOS, OS/2, CP/M, TOS (Atari)
			//Mac OS Classic, Apple II, C64
			for (int i = ptr; i < test.length(); i++) {
				if ((test.charAt(i) == '\n' || test.charAt(i) == '\r') && newLine.length() < 2) {
					newLine = newLine + Character.toString(test.charAt(i));
					ptr++;
				} else {
					break;
				}
			}
		}
		
		else if(test.charAt(ptr) == '\n') {
			//Unix, Linux, Android, macOS, AmigaOS, BSD, weitere
			for (int i = ptr; i < test.length(); i++) {
				if (test.charAt(i) == '\n' && newLine.length() < 1) {
					newLine = newLine + Character.toString(test.charAt(i));
					ptr++;
				} else {
					break;
				}
			}
		}
		
		if(newLine.equals("\n") || newLine.equals("\r\n") || newLine.equals("\r")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isComment(String file) {
		return (ptr < file.length() && file.charAt(ptr) == '|' && file.charAt(ptr + 1) == '|');
	}
	
	private void skipComment(String file) {
		for(int i = ptr; i < file.length(); i++) {
			if(!isNewLine(file)) {
				ptr++;
			}
			else {
				break;
			}
		}
	}

	private void printTokenList() { 	//toString
		for (Token i : tokens) {
			System.out.println(i);
		}
	}
	
	public ArrayList<Token> getTokens() {
		return tokens;
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
