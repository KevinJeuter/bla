package main;

public class Specials extends Token {
	
	private spcl special;
	
	enum spcl {
		EOF
	}
	
	public Specials(String strSpecial) {
		this.special = toEnum(strSpecial);
	}
	
	@Override
	public String toString() {
		return "<Special: " + this.special.toString() + ">";
	}
	
	private spcl toEnum(String strSpecial) {
		switch(strSpecial.toLowerCase()) {
		case "eof":
			return spcl.EOF;
		default:
			throw new RuntimeException("unknown special");
		}
	}
}
