package visitors;

import ast.At;
import ast.BooleanConst;
import ast.Builtin;
import ast.Def;
import ast.NumberConst;
import ast.StringConst;
import ast.Var;
import ast.Where;

// visitor pattern

// this class is the *abstract visitor* for AST nodes
public abstract class Visitor {

	public abstract void visit(At n);
	public abstract void visit(BooleanConst n);
	public abstract void visit(Builtin n);
	public abstract void visit(Def n);
	public abstract void visit(NumberConst n);
	public abstract void visit(StringConst n);
	public abstract void visit(Var n);
	public abstract void visit(Where n);

}
