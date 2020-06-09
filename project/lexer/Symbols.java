package lexer;

public class Symbols extends Token {

	private sym symbol;
	
	public static final String plus = "+";
	public static final String minus = "-";
	public static final String mul = "*";
	public static final String div = "/";
	public static final String leq = "<=";
	public static final String equ = "=";
	public static final String neq = "~=";
	public static final String geq = ">=";
	public static final String les = "<";
	public static final String grt = ">";
	public static final String not = "~";
	public static final String dot = ".";
	public static final String comma = ",";
	public static final String parenl = "(";
	public static final String parenr = ")";
	public static final String semicolon = ";";
	public static final String colon = ":";

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
		SEMICOLON,
		COLON
	}
	
	public Symbols(String strSymbol) {
		symbol = toEnum(strSymbol);
	}
	

	public static boolean isSymbol(char test) {
		if(test == plus.charAt(0) || test == minus.charAt(0) || test == mul.charAt(0) || test == div.charAt(0)
		   || test == equ.charAt(0) || test == les.charAt(0) || test == grt.charAt(0) || test == dot.charAt(0) 
		   || test == comma.charAt(0) || test == parenl.charAt(0) || test == parenr.charAt(0) || test == semicolon.charAt(0)
		   || test == not.charAt(0) || test == colon.charAt(0)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "<Symbol: " + this.symbol.toString() + ">";
	}
	
	private sym toEnum(String strSymbol) {
		switch(strSymbol) {
		case plus:
			return sym.PLUS;
		case minus:
			return sym.MINUS;
		case mul:
			return sym.MUL;
		case div:
			return sym.DIV;
		case leq:
			return sym.LEQ;
		case equ:
			return sym.EQU;
		case neq:
			return sym.NEQ;
		case geq:
			return sym.GEQ;
		case les:
			return sym.LES;
		case grt:
			return sym.GRT;
		case dot:
			return sym.DOT;
		case comma:
			return sym.COMMA;
		case parenl:
			return sym.PARENL;
		case parenr:
			return sym.PARENR;
		case semicolon:
			return sym.SEMICOLON;
		case colon:
			return sym.COLON;
		default:
			// error: unknown keyword
			throw new RuntimeException("unknown symbol");
		}
	}
}
