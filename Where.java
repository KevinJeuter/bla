package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Where extends Node{
	
	private Node right;
	private HashMap<String, Pair<ArrayList<String>, Node>> left = new HashMap<String, Pair<ArrayList<String>, Node>>();

	public Where(HashMap<String, Pair<ArrayList<String>, Node>> left, Node right) {
		this.left = left;
		this.right = right;
	}

}
