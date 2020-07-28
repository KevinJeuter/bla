package lexer;

/*
 * Class for Identifier Tokens
 */

public class Identifier extends Token {	
	
	private String id;

	public Identifier(String id) {
		this.id = id;	
	}
	
	public String getID() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return "<ID: " + this.id.toString() + ">";
	}
	
}
