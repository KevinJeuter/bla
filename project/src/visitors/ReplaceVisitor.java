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
		
		//Gedanke: defLeft auf n.getDefinitions setzen, damit man dies auch später für Var benutzen kann. 
		//Alle Knoten der Pairs accepten, damit sie auf sich gegenseitig zeigen
		//die expr accepten, damit die variablen der expr durch die definitionen ersetzt werden
		//ein neues def daraus machen und zurückgeben.
		//Fehler an Beispiel one: 1:two ; two: 2:one
		//Wenn one accepted wird, wird bei "two" 2:one eingefügt und wenn dann 2 accepted wird,
		//wird bei 2:one für one 1:2:one eingefügt, da bei one das eingesetzt wurde.
		//Problem ist, dass es sich nicht gegenseitig aufruft und so wie es hier ist, nach dem
		//ersten Durchlauf immer one aufgerufen wird, da bei one und bei two jetzt nur auf
		//one gezeigt wird.
		
		defLeft = new DefHashMap(n.getDefinitions());
		
		Set<Entry<String, Pair<ArrayList<String>, Node>>> entrySet = defLeft.returnHashMap().entrySet();
		Iterator<Entry<String, Pair<ArrayList<String>, Node>>> it = entrySet.iterator();
		
		while(it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			System.out.println("replacing: " + me.getKey());
			Pair<ArrayList<String>, Node> value = (Pair<ArrayList<String>, Node>) me.getValue();
			defLeft.update((String) me.getKey(), value.setValue(value.getValue().accept(this)));
		}
		
		Node expr = n.getExpr().accept(this);
	
		Def newDef = new Def(defLeft.returnHashMap(), expr);
		//NEW ÄNDERN
		return newDef;
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
