package visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ast.At;
import ast.BooleanConst;
import ast.Builtin;
import ast.Def;
import ast.Node;
import ast.NumberConst;
import ast.Pair;
import ast.StringConst;
import ast.Var;
import ast.PairNode;
import ast.Where;
import main.Main;
import parser.DefHashMap;

public class ReplaceVisitor extends Visitor{
	
	private DefHashMap defLeft;

	@Override
	public Node visit(At n) {
		//Besuche linke und rechte Knoten von At und führe accept auf beide Seiten aus, wodurch jeder Knoten
		//besucht wird.
		
		n.setLeft(n.getLeft().accept(this));
		
		n.setRight(n.getRight().accept(this));

		return n;
	}
	
	@Override
	public Node visit(PairNode n) {
		throw new RuntimeException("PairNode found in compiling stage");
	}

	@Override
	public Node visit(BooleanConst n) {
		return n;
	}

	@Override
	public Node visit(Builtin n) {
		return n;
	}
	
	@Override
	public Node visit(Def n) {
		//defLeft is the HashMap of n
		defLeft = new DefHashMap(n.getDefinitions().returnHashMap());
		
		Set<Entry<String, Pair<ArrayList<String>, Node>>> entrySet = defLeft.returnHashMap().entrySet();
		Iterator<Entry<String, Pair<ArrayList<String>, Node>>> it = entrySet.iterator();
		
		//As long as there is a next definition name, update defLeft to accept all the Nodes.
		while(it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			Pair<ArrayList<String>, Node> value = (Pair<ArrayList<String>, Node>) me.getValue();
			defLeft.update((String) me.getKey(), value.setValue(value.getValue().accept(this)));
		}
		
		//Accept the right side Node of the Def
		Node expr = n.getExpr().accept(this);
		
		//Update n to the accepted n
		n.updateDefinitions(defLeft);
		n.updateExpr(expr);

		return n;
	}

	@Override
	public Node visit(NumberConst n) {
		return n;
	}

	@Override
	public Node visit(StringConst n) {
		return n;
	}
	@Override
	public Node visit(Var n) {
		//Replace Variable with Node of the fitting Parameter. If there is no fitting Parameter throw an Error.
		if(defLeft.returnHashMap().containsKey(n.getVar())) {
			return defLeft.returnHashMap().get(n.getVar()).getValue();
		}
		else {
			throw new RuntimeException(n + " is not defined.");
		}
	}
	
	@Override
	public Node visit(Where n) {
		return n;
	}
}
