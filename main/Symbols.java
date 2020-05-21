package main;

public class Symbols extends Token {

	private sym symbol;
	
	enum sym {
		PLUS,
		MINUS,
		MUL,
		DIV,
		LEQ,
		EQU,
		NEQ,
		GEQ,
		LES,
		GRT,
		DOT,
		COMMA,
		PARENL,
		PARENR,
		SEMICOLON
	}
	
	public Symbols(String strSymbol) {
		symbol = toEnum(strSymbol);
	}
	
	@Override
	public String toString() {
		return "<Symbol: " + this.symbol.toString() + ">";
	}
	
	private sym toEnum(String strSymbol) {
		switch(strSymbol) {
		case "+":
			return sym.PLUS;
		case "-":
			return sym.MINUS;
		case "*":
			return sym.MUL;
		case "/":
			return sym.DIV;
		case "<=":
			return sym.LEQ;
		case "=":
			return sym.EQU;
		case "!=":
			return sym.NEQ;
		case ">=":
			return sym.GEQ;
		case "<":
			return sym.LES;
		case ">":
			return sym.GRT;
		case ".":
			return sym.DOT;
		case ",":
			return sym.COMMA;
		case "(":
			return sym.PARENL;
		case ")":
			return sym.PARENR;
		case ";":
			return sym.SEMICOLON;
		default:
			// error: unknown keyword
			throw new RuntimeException("unknown symbol");
		}
	}
}
