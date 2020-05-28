package main;

import java.util.HashMap;
import java.util.ArrayList;

public class Def extends Node{
	
	// def: List<(name, List<param>, Node)>, Node
	// hashmap(string, pair(list, node) kommentare
	
	//hier bin ich mir noch unsicher, ob es so stimmt. Meine Idee war, dass ich eine HashMap mache aus den Strings
	//von den Namen und dann noch ein Pair mache aus der Liste von den Parametern und der Node.
	//Die Liste von den Paramtern dachte ich, muss ich vlt auch als HashMap machen, damit jeder Parameter
	//nur ein mal verwendet wird und in die Liste würden dann immer nur die keys kommen? also die HashMap
	//wäre dann z.B. ("xs", 1) ("ys", 2) usw und ich benutze dann immer nur die keys für die liste.
	
	private Node right;
	private HashMap<String, Pair<ArrayList<String>, Node>> left;

	public Def(HashMap<String, Pair<ArrayList<String>, Node>> left, Node right) {
		this.left = left;
		this.right = right;
	}
}
