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
	
	DefHashMap defLeft;
	
	public ReplaceVisitor(DefHashMap defLeft) {
		//ReplaceVisitor wird mit einem Def erstellt, bei visit(Var n) wird dann das defLeft benutzt.
		this.defLeft = defLeft;
	}
	
	@Override
	public Node visit(At n) {
		//Besuche linke und rechte Knoten von At und führe accept auf beide Seiten aus, wodurch jeder Knoten
		//besucht wird.
		Node atLeft = n.getLeft().accept(this);

		Node atRight = n.getRight().accept(this);
		
		At acceptedAt = new At(atLeft, atRight);
		return acceptedAt;
	}
	
	@Override
	public Node visit(PairNode n) {
		//Besuche linke und rechte Knoten von At und führe accept auf beide Seiten aus, wodurch jeder Knoten
		//besucht wird.
		Node pairLeft = n.getLeft().accept(this);

		Node pairRight = n.getRight().accept(this);
		
		PairNode acceptedPair = new PairNode(pairLeft, pairRight);
		return acceptedPair;
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
		HashMap<String, Pair<ArrayList<String>, Node>> defs = n.getDefinitions();
		
		Set<Entry<String, Pair<ArrayList<String>, Node>>> entrySet = defs.entrySet();
		Iterator<Entry<String, Pair<ArrayList<String>, Node>>> it = entrySet.iterator();
		
		int i = 0;
		
		while(it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			Pair<ArrayList<String>, Node> value = (Pair<ArrayList<String>, Node>) me.getValue();
			
			Pair<ArrayList<String>, Node> value2 = new Pair<ArrayList<String>, Node>(value.first, value.getValue().accept(this));
			
			defs.replace((String) defs.keySet().toArray()[i], value2);
			i++;
		}
		
		Node y = n.getExpr().accept(this);
		
		Def x = new Def(defs, y);
		
		return x;
		
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
		//ersetze die Variable durch den Node des dazugehörigen parameters. Werfe Fehler, wenn nicht existiert.
		if(defLeft.returnHashMap().containsKey(n.getVar())) {
			return defLeft.returnHashMap().get(n.getVar()).second;
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
