package main;

public class Keywords extends Token {
	
	private kw key;

	enum kw {
		DEFINE,
		IF,
		THEN,
		ELSE,
		WHERE,
		NOT
	}
	
	public Keywords(String strKey) {
		key = toEnum(strKey);
	}
	
	@Override
	public String toString() {
		return "<Keyword: " + this.key.toString() + ">";
	}
		
	private kw toEnum(String strKey) {
		switch(strKey.toLowerCase()) {
		case "def":
		case "define":
			return kw.DEFINE;
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
		default:
			// error: unknown keyword
			throw new RuntimeException("unknown symbol");	
		}
	}

}