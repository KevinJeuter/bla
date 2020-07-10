package visitors;

import ast.At;
import ast.BooleanConst;
import ast.Builtin;
import ast.Def;
import ast.NumberConst;
import ast.StringConst;
import ast.Var;
import ast.Where;
import ast.Node;
import ast.PairNode;

// visitor pattern

// this class is the *abstract visitor* for AST nodes
public abstract class Visitor {

	public abstract Node visit(At n);
	public abstract Node visit(BooleanConst n);
	public abstract Node visit(Builtin n);
	public abstract Node visit(Def n);
	public abstract Node visit(NumberConst n);
	public abstract Node visit(StringConst n);
	public abstract Node visit(Var n);
	public abstract Node visit(Where n);
	public abstract Node visit(PairNode n);
}