package parser;

import ast.Pair;

import java.util.ArrayList;
import java.util.HashMap;

import ast.Node;

public class DefHashMap {
	
	HashMap<String, Pair<ArrayList<String>, Node>> definitions;
	
	public DefHashMap() {
		HashMap<String, Pair<ArrayList<String>, Node>> definitions = new HashMap<String, Pair<ArrayList<String>, Node>>();
		this.definitions = definitions;
	}
	
	public DefHashMap(HashMap<String, Pair<ArrayList<String>, Node>> x) {
		this.definitions = x;
	}
	
	public void put(String defName, Pair<ArrayList<String>, Node> param) {
		definitions.put(defName, param);
	}
	
	public HashMap<String, Pair<ArrayList<String>, Node>> returnHashMap(){
		return definitions;
	}
}
