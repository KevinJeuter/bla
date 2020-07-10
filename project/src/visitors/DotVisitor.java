package visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ast.At;
import ast.BooleanConst;
import ast.Builtin;
import ast.Def;
import ast.Node;
import ast.NumberConst;
import ast.Pair;
import ast.StringConst;
import ast.Var;
import ast.Where;
import ast.PairNode;


public class DotVisitor extends Visitor {
	
	// counts /produces identifiers for dot nodes
	private int counter = 0;
	
	// accumulator:
	// contents will be the dot-formatted AST
	private String res = "";

	public String getDotResult() {
		String header = "digraph {\n";
		String footer = "}\n";
		return header + res + footer;
	}
		
	// produce / return a new identifier
	private int fresh() {
		this.counter++;
		return this.counter;
	}
	
	// get the latest identifier
	// this is a *hacky* solution: be aware that order matters
	private int getPreviousId() {
		return this.counter;
	}
	
	// create tree node (in dot)
	private int printNode(String name) {
		// example output: 2 [label = "@"]
		int myId = fresh();
		this.res += myId + " [label = \"" + name + "\"]\n";
		return myId;
	}
	
	// create tree edge (in dot)
	private void printEdge(int parent, int child) {
		// example output: 1 -> 2
		this.res += parent + " -> " + child + "\n";
	}
	
	@Override
	public Node visit(At n) {
		// print left node
		n.getLeft().accept(this);
		int leftId = getPreviousId();
		
		// print right node
		n.getRight().accept(this);
		int rightId = getPreviousId();
		
		// print this node
		printNode("@");
		int atId = getPreviousId();
		
		// print edges
		printEdge(atId, leftId);
		printEdge(atId, rightId);
		
		return null;
	}
	
	@Override
	public Node visit(PairNode n) {
		// print left node
		n.getLeft().accept(this);
		int leftId = getPreviousId();
		
		// print right node
		n.getRight().accept(this);
		int rightId = getPreviousId();
		
		// print this node
		printNode("pair");
		int pairId = getPreviousId();
		
		// print edges
		printEdge(pairId, leftId);
		printEdge(pairId, rightId);
		
		return null;
	}

	@Override
	public Node visit(BooleanConst n) {
		printNode(Boolean.toString(n.getBoolConst()));
		return null;
	}

	@Override
	public Node visit(Builtin n) {
		printNode(n.getFunct().toString());
		return null;
	}

	@Override
	public Node visit(Def n) {
		int listId = printNode("definitions");
		
		HashMap<String, Pair<ArrayList<String>, Node>> defs = n.getDefinitions();
		
		Set<Entry<String, Pair<ArrayList<String>, Node>>> entrySet = defs.entrySet();
		Iterator<Entry<String, Pair<ArrayList<String>, Node>>> it = entrySet.iterator();
		
		while(it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			String key = (String) me.getKey();
			Pair<ArrayList<String>, Node> value = (Pair<ArrayList<String>, Node>) me.getValue();
			
			printNode(key.toString());
			int curDef = getPreviousId();
			printEdge(listId, curDef);
			
			value.getValue().accept(this);
			int subExprId = getPreviousId();
			printEdge(curDef, subExprId);
		}
		
		n.getExpr().accept(this);
		int exprId = getPreviousId();
		
		int defId = printNode("DEF");
		printEdge(defId, exprId);
		printEdge(defId, listId);
		return null;
	}

	@Override
	public Node visit(NumberConst n) {
		printNode(Integer.toString(n.getNumConst()));
		return null;
	}

	@Override
	public Node visit(StringConst n) {
		printNode(n.getStringConst());	
		return null;
	}

	@Override
	public Node visit(Var n) {
		printNode("VAR: "+n.getVar());
		return null;
	}

	@Override
	public Node visit(Where n) {
		int listId = printNode("defintions");
		
		HashMap<String, Pair<ArrayList<String>, Node>> defs = n.getDefinitions();
		
		Set<?> entrySet = defs.entrySet();
		Iterator<?> it = entrySet.iterator();
		
		while(it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			String key = (String) me.getKey();
			Pair<ArrayList<String>, Node> value = (Pair<ArrayList<String>, Node>) me.getValue();
			
			printNode(key.toString());
			int curDef = getPreviousId();
			printEdge(listId, curDef);
			
			value.getValue().accept(this);
			int subExprId = getPreviousId();
			printEdge(curDef, subExprId);
		}
		
		n.getExpr().accept(this);
		int exprId = getPreviousId();
		
		int defId = printNode("WHERE");
		printEdge(defId, exprId);
		printEdge(defId, listId);
		return null;
	}
	
	

}