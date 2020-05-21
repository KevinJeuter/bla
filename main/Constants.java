package main;

public class Constants extends Token {

	private String konst;
	
	public Constants(String strKonst) {
		konst = strKonst;
	}
	
	public int getNum() {
		if(konst.matches("-?\\d+")) {
			return Integer.parseInt(konst);
		}
		else {
			throw new RuntimeException("not a number");
		}
	}
	
	public boolean getBool() {
		if(konst.toLowerCase() == "true" || konst.toLowerCase() == "false") {
			return Boolean.parseBoolean(konst);
		}
		else {
			throw new RuntimeException("not a boolean");
		}
	}
	
	public String getString() {
		if(!(konst.matches("-?\\d+")) && ((konst.toLowerCase() != "true") || (konst.toLowerCase() == "false"))){
			return konst;
		}
		else {
			throw new RuntimeException("not a string");
		}
	}
	
	@Override
	public String toString() {
		if(konst.matches("-?\\d+")) {
			return "<Constant num: " + this.konst.toString() + ">";
		}
		else if(konst.toLowerCase() == "true" || konst.toLowerCase() == "false") {
			return "<Constant boolean: " + this.konst.toString() + ">";
		}
		else {
			return "<Constant string: \"" + this.konst.toString() + "\">";
		}	
	}

}
