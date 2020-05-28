package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Where extends Node{
	
	private Node right;
	//muss hier überhaupt eine hashmap mit string? weil die parameter von where sind bei uns im bsp. links im at knoten
	private Pair<ArrayList<String>, Node> left;

	public Where(Pair<ArrayList<String>, Node> left, Node right) {
		this.left = left;
		this.right = right;
	}

}
