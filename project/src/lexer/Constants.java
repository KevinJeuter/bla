package lexer;

/*
 * Class for Constant tokens (Strings, Numbers, Booleans)
 */

public class Constants extends Token {

	private String konst;
	
	public Constants(String strKonst) {
		konst = strKonst;
	}
	
	//Checks if it contains only numbers
	private boolean isNum() {
		if(konst.matches("-?\\d+")) {
			return true;
		}
		return false;
	}
	
	//It is only a string, if it starts and ends with a '"'. 
	private boolean isString() {
		if(konst.charAt(0) == '"' && konst.charAt(konst.length() - 1) == '"') {
			return true;
		}
		return false;
	}
	
	private boolean isBool() {
		if(konst.equalsIgnoreCase("true") || konst.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}
	
	public static boolean isBool(String test) {
		if(test.equalsIgnoreCase("true") || test.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}
	
	public int getNum() {
		if(isNum()) {
			return Integer.parseInt(konst);
		}
		else {
			throw new RuntimeException("not a number");
		}
	}
	
	public boolean getBool() {
		if(isBool()) {
			return Boolean.parseBoolean(konst);
		}
		else {
			throw new RuntimeException("not a boolean");
		}
	}
	
	public String getString() {
		if(isString()){
			return konst;
		}
		else {
			throw new RuntimeException("not a string");
		}
	}
	
	@Override
	public String toString() {
		if(isNum()) {
			return "<Constant num: " + this.konst.toString() + ">";
		}
		else if(isBool()) {
			return "<Constant boolean: " + this.konst.toString() + ">";
		}
		else if(isString()){
			return "<Constant string: " + this.konst.toString() + ">";
		}	
		else {
			throw new RuntimeException("not a constant");
		}
	}

}
