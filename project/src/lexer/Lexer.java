package lexer;

import java.util.ArrayList;

/*
 * Main Class of the Lexer. Creates an ArrayList of Tokens, to be used in other classes
 */

import lexer.Token;

public class Lexer {

	// point to current character
	private int ptr = 0;

	// store the final tokens
	private ArrayList<Token> tokens = new ArrayList<Token>();

	public Lexer(String file){
		
		String src = file;
		
		for (;;) { // forever	

			if (ptr == src.length()) { // Make an EOF Token at the end of the Program
				lexSpecial();
				break;
			}

			else if (isComment(src)) { // Comment. If there is a comment (||...) it skips everything until there is a new Line
				skipComment(src);
			}

			else if (isEmptyLine(src.charAt(ptr)) || isTab(src.charAt(ptr))) { //If there is an Empty Line or a Tab, skip it
				skipWhitespace();
			}
			
			else if(isNewLine(src)) { //If there is a NewLine, skip it
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
				// Not implemented, so don't allow lists with [ ... ]
				throw new RuntimeException("Lists in the form of \"[x:y:z]\" are not implemented. Try \"(x:y:z:nil)\" instead.");
			}

			else {
				throw new RuntimeException("there is a non-valid token");
			}

		}
		
		System.out.println(toString()); //Visualization
		
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

	private void lexConstString(String file) {

		// continue with lexing until '"'
		// -> end pointer
		// create token and insert in result

		String token = "";

		//Because we know the Character at the pointer is " (checked already in Lexer() for loop) we can set
		//the first character of our String to " and then set the pointer to pointer+1
		//Then lex until there is another " and make token out of it. If there is no other ", throw an Error.
		
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
					//All Symbols that don't start with <, >, ! are length 1. So here we check if it starts with one of those symbols.
					//Otherwise the program would have problems with x=(x+2) as it would see =( as one Symbol.
					if(!token.equals(Symbols.les) && !token.equals(Symbols.grt) && !token.equals(Symbols.not)) {
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
		//Check if Comment really starts with ||, otherwise throw Error
		if((ptr == file.length()-1 && file.charAt(ptr) == '|') || (ptr < file.length()-1 && file.charAt(ptr) == '|' && file.charAt(ptr + 1) != '|')) {
			throw new RuntimeException("Comments need to be written as ||...");
		}
		else {
			return (ptr < file.length()-1 && file.charAt(ptr) == '|' && file.charAt(ptr + 1) == '|');
		}
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

	@Override
	public String toString() { 	//toString
		return tokens.toString();
	}
	
	public ArrayList<Token> getTokens() {
		ArrayList<Token> tokensClone = (ArrayList<Token>) tokens.clone();
		return tokensClone;
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
