package lexer;

/*
 * Class for Keywords
 */

public class Keywords extends Token {
	
	private kw key;

	enum kw {
		DEF,
		IF,
		THEN,
		ELSE,
		WHERE,
		NOT,
		AND,
		OR,
		HD,
		TL,
		NIL
	}
	
	public Keywords(String strKey) {
		key = toEnum(strKey);
	}
	
	//Checks the whole list of Keywords if it contains the String "key"
	public static boolean isKeyword(String key) {
		for (kw k : kw.values()) {
			if (k.toString().toLowerCase().equals(key.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "<Keyword: " + this.key.toString() + ">";
	}
		
	private kw toEnum(String strKey) {
		switch(strKey.toLowerCase()) {
		case "def":
			return kw.DEF;
		case "if":
			return kw.IF;
		case "then":
			return kw.THEN;
		case "else":
			return kw.ELSE;
		case "where":
			return kw.WHERE;
		case "not":
			return kw.NOT;
		case "and":
			return kw.AND;
		case "or":
			return kw.OR;
		case "hd":
			return kw.HD;
		case "tl":
			return kw.TL;
		case "nil":
			return kw.NIL;
		default:
			// error: unknown keyword
			throw new RuntimeException("unknown symbol");	
		}
	}

}