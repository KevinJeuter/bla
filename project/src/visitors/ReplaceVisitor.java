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
import ast.Where;
import main.Main;

public class ReplaceVisitor extends Visitor{
	
	HashMap<String, Pair<ArrayList<String>, Node>> defLeft;
	
	public ReplaceVisitor(HashMap<String, Pair<ArrayList<String>, Node>> defLeft) {
		this.defLeft = defLeft;
	}
	
	@Override
	public Node visit(At n) {

		Node x = n.getLeft().accept(this);

		Node y = n.getRight().accept(this);
		
		At z = new At(x, y);
		return z;
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
		if(defLeft.containsKey(n.getVar())) {
			return defLeft.get(n.getVar()).second;
		}
		else {
			return n;
		}
	}
	
	@Override
	public Node visit(Where n) {
		return n;
	}
}
