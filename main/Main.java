package main;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public abstract class Main {
	
	//BUG: Wenn es kein Comment gibt, gibt es einen Fehler, wenn eine neue Zeile begonnen wird.

	// point to current character
	private static int ptr = 0;

	// store the final tokens
	private static ArrayList<Token> tokens = new ArrayList<Token>();

	public static void main(String[] args) throws IOException{
		
		if(args.length <= 0) {
			throw new RuntimeException("No file selected.");
		}
		
		String src = fileToString(args[0]);

		for (;;) { // forever	

			if (ptr == src.length()) { 
				lexSpecial();
				break;
			}

			else if (isComment(src)) { // Comment. Also wird bis \n übersprungen, falls vorhanden
				skipComment(src);
			}

			else if (isWhitespace(src.charAt(ptr)) || isTab(src.charAt(ptr)) || isNewLine(src)) {
				skipWhitespace();
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

			else {
				throw new RuntimeException("there is a non-valid token");
			}

		}

		//System.out.print(src);
		printTokenList();
		
	}
	
	private static String fileToString(String fileName) throws IOException {
		return Files.readString(Path.of(fileName));
	}

	private static void lexDigit(String file) {

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

	private static void lexConstString(String file) { // unsicher, da ich nicht wusste wie nachprüfen

		// continue with lexing until '"'
		// -> end pointer
		// create token and insert in result

		String token = "";


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
				ptr++;
				break;
			}
		}

		Constants konst = new Constants(token);

		tokens.add(konst);

	}

	private static void lexWord(String file) {

		// continue with lexing until NOT word
		// -> end pointer
		// create token and insert in result
		// either ID or keyword

		String token = "";

		for (int i = ptr; i < file.length(); i++) {
			if (Character.isLetter(file.charAt(ptr))) {
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

	private static void lexSymbol(String file) {

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

	private static void lexSpecial() {

		Specials spec = new Specials("eof");

		tokens.add(spec);

		ptr++;

	}
	
	private static void skipWhitespace() {
		ptr++;
	}

	private static boolean isWhitespace(char test) {
		return test == ' ';
	}
	
	private static boolean isTab(char test) {
		return test == '\t';
	}
	
	private static boolean isNewLine(String test) {
		//return test == '\n'; //windows \r\n , sonst \r
		
		String newLine = "";
		
		for (int i = ptr; i < test.length(); i++) {
			if (test.charAt(ptr) == '\n' || test.charAt(ptr) == '\r') {
				newLine = newLine + Character.toString(test.charAt(ptr));
				ptr++;
			} else {
				break;
			}
		}
		if(newLine.equals("\n") || newLine.equals("\r\n") || newLine.equals("\r")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static boolean isComment(String file) {
		return (ptr < file.length() && file.charAt(ptr) == '|' && file.charAt(ptr + 1) == '|');
	}
	
	private static void skipComment(String file) {
		for(int i = ptr; i < file.length(); i++) {
			if(!isNewLine(file)) {
				ptr++;
			}
			else {
				break;
			}
		}
	}

	private static void printTokenList() { 	//toString
		for (Token i : tokens) {
			System.out.println(i);
		}
	}

}