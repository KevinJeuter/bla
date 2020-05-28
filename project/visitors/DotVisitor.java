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
	public void visit(At n) {
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
	}

	@Override
	public void visit(BooleanConst n) {
		printNode(Boolean.toString(n.getBoolConst()));
	}

	@Override
	public void visit(Builtin n) {
		printNode(n.getFunct().toString());
	}

	@Override
	public void visit(Def n) {
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
	}

	@Override
	public void visit(NumberConst n) {
		printNode(Integer.toString(n.getNumConst()));
	}

	@Override
	public void visit(StringConst n) {
		printNode(n.getStringConst());		
	}

	@Override
	public void visit(Var n) {
		printNode("VAR: "+n.getVar());
	}

	@Override
	public void visit(Where n) {
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
	}
	
	

}
