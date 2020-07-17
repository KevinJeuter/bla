package parser;

import ast.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ast.Node;

public class DefHashMap {
	
	private HashMap<String, Pair<ArrayList<String>, Node>> definitions;
	
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
	
	/**
	 * Replace Object "name" in HashMap with new value
	 * @param name
	 * @param value
	 */
	public HashMap<String, Pair<ArrayList<String>, Node>> update(String name, Pair<ArrayList<String>, Node> value) {
		HashMap<String, Pair<ArrayList<String>, Node>> defs = definitions;
		defs.replace(name , value);
		return defs;
	}
	
	public String getName(int position) {
		HashMap<String, Pair<ArrayList<String>, Node>> defs = definitions;
		return (String) defs.keySet().toArray()[position];
	}
}
