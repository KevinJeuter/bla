package parser;

import ast.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ast.Node;

/*
 * Class for the HashMaps in the Def Nodes to hide the complex structure. 
 */

public class DefHashMap {
	
	private HashMap<String, Pair<ArrayList<String>, Node>> definitions;
	
	//Create new empty HashMap with the call DefHashMap()
	public DefHashMap() {
		HashMap<String, Pair<ArrayList<String>, Node>> definitions = new HashMap<String, Pair<ArrayList<String>, Node>>();
		this.definitions = definitions;
	}
	
	//Create a DefHashMap built from another HashMap with the call DefHashMap(HashMap...)
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
	
	@Override
	public String toString() {
		return this.definitions.toString();
	}
}
