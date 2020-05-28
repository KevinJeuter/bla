package main;

public class BooleanConst extends Node{
	private boolean bool;
	
	public BooleanConst(boolean bool) {
		this.bool = bool;
	}
	
	public boolean getBoolConst() {
		return this.bool;
	}

}
