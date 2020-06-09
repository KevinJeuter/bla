package parser;

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
import lexer.Constants;
import lexer.Identifier;
import lexer.Keywords;
import lexer.Main;
import lexer.Specials;
import lexer.Symbols;
import lexer.Token;
import lexer.Lexer;


public class Parser {
	
	//Habe es nicht richtig verstanden. Bei meinem public Node condExpr wären die Lexer.getToken() alle noch
	//keine Nodes. Auch ob ich das match richtig verstanden habe, weiss ich nicht. Und es gibt ja mehrere tokens
	//zwischen if then und then else. Das müsste man dann für das @ auch noch anders machen.
	
	private static Lexer l = new Lexer(lexer.Main.getTokens());
	//nicht 2 mal expr(), wenn dann mit variable
	
	//expr(), condexpr(), listexpr() bringt die tokens dazwischen als node
	private static Identifier tokenNil = new Identifier("nil");
	private static Identifier tokenHd = new Identifier("hd");
	private static Keywords ifKey = new Keywords("if");
	private static Keywords thenKey = new Keywords("then");
	private static Keywords elseKey = new Keywords("else");
	private static Keywords tokenOr = new Keywords("or");
	private static Keywords tokenAnd = new Keywords("and");
	private static Symbols tokenColon = new Symbols(":");
	private static Symbols tokenPlus = new Symbols("+");
	private static Symbols tokenMinus = new Symbols("-");
	private static Symbols tokenParenl = new Symbols("(");
	private static Symbols tokenParenr = new Symbols(")");
	private static Node nodeOr = new Builtin(Builtin.funct.OR);
	private static Node nodeAnd = new Builtin(Builtin.funct.AND);
	private static Node nodeCond = new Builtin(Builtin.funct.COND);
	private static Node nodeColon = new Builtin(Builtin.funct.COLON);
	private static Node nodePlus = new Builtin(Builtin.funct.PLUS);
	private static Node nodeMinus = new Builtin(Builtin.funct.MINUS);
	private static Node nodeHd = new Builtin(Builtin.funct.HD);
	private static Node nodeTl = new Builtin(Builtin.funct.TL);
	private static Node nodeNil = new Builtin(Builtin.funct.NIL);
	private static Node empty = new StringConst("");
	
	public static Node condExpr() {
		if(l.getLookahead().toString().equals(ifKey.toString())) {
			match(ifKey);
			At ifAt = new At(nodeCond, expr());
			match(thenKey);
			At thenAt = new At(ifAt, condExpr());
			match(elseKey);
			At elseAt = new At(thenAt, condExpr());
			return elseAt;
		}
		else {
			return listExpr();
		}
	}
	
	//bei listen braucht man parameter
	
	private static Token match(Token t) {
		Token x = l.getToken();
		if (x.toString().equals(t.toString())) {
			return x;
		}
		else {
			throw new RuntimeException("Token mismatch");
		}
	}
	
	private static Node expr() {
		At exprAt = new At(condExpr(), empty);
		return exprAt;
	}
	
	private static Node listExpr() {
		At listExprAt = new At(opExpr(), listExpr1());
		return listExprAt;
	}
	
	private static Node listExpr1() {
		if(l.getLookahead().toString().equals(tokenColon.toString())) {
			match(tokenColon);
			At listExpr1At = new At(nodeColon, listExpr());
			return listExpr1At;
		}
		else {
			return empty;
		}
	}
	
	private static Node opExpr() {
		At opExprAt = new At(conjunct(), opExpr1());
		return opExprAt;
	}
	
	private static Node opExpr1() {
		if(l.getLookahead().toString().equals(tokenOr.toString())) {
			match(tokenOr);
			At opExpr1AtOr = new At(nodeOr, conjunct());
			At opExpr1At = new At(opExpr1AtOr, opExpr1());
			return opExpr1At;
		}
		else {
			return empty;
		}
	}
	
	private static Node conjunct() {
		At conjunctAt = new At(compar(), conjunct1());
		return conjunctAt;
	}
	
	private static Node conjunct1() {
		if(l.getLookahead().toString().contentEquals(tokenAnd.toString())) {
			match(tokenAnd);
			At conjunct1AtAnd = new At(nodeAnd, compar());
			At conjunct1At = new At(conjunct1AtAnd, conjunct1());
			return conjunct1At;
		}
		else {
			return empty;
		}
	}
	
	private static Node compar() {
		At comparAt = new At(add(), compar1());
		return comparAt;
	}
	
	private static Node compar1() {
		if() {
			match(relop());	//hier muss token von relop
			At compar1RelopAt = new At(relop(), add());
			At compar1At = new At(compar1RelopAt, compar1());
			return compar1At;
		}
		else {
			return empty;
		}
	}
	
	private static Node add() {
		At addAt = new At(mul(), add1());
		return addAt;
	}
	
	private static Node add1() {
		if() {
			match(addop());	//hier muss token von addop
			At add1AddopAt = new At(addop(), mul());
			At add1At = new At(add1AddopAt, add1());
			return add1At;
		}
		else {
			return empty;
		}
	}
	
	private static Node mul() {
		At mulAt = new At(factor(), mul1());
		return mulAt;
	}
	
	private static Node mul1() {
		if() {
			match(mulop()); //hier muss token von mulop
			At mul1MulopAt = new At(mulop(), factor());
			At mul1At = new At(mul1MulopAt, mul1());
			return mul1At;
		}
		else {
			return empty;
		}
	}
	
	private static Node factor() {
		if() {
			At factorAt = new At(prefix(), comb());
			return factorAt;
		}
		else {
			return comb();
		}
	}
	
	private static Node comb() {
		At combAt = new At(simple(), comb1());
		return combAt;
	}
	
	private static Node comb1() {
		if() {
			At comb1At = new At(simple(), comb1());
			return comb1At;
		}
		else {
			return empty;
		}
	}
	
	private static Node simple() {
		if() {
			return name();
		}
		
		else if() {
			return builtin();
		}
		
		else if() {
			return constant();
		}
		
		else {
			match(tokenParenl);
			Node simpleExpr = expr();
			match(tokenParenr);
			return simpleExpr;
		}
	}
	
	private static Node name() {
		return id();
	}
	
	private static Node builtin() {
		if(l.getLookahead().toString().equals(tokenHd.toString())) {
			return nodeHd;
		}
		else {
			return nodeTl;
		}
	}
	
	private static Node constant() {
		if() {
			return num();
		}
		
		else if() {
			return bool();
		}
		
		else if() {
			return string();
		}
		
		else if(l.getLookahead().toString().equals(tokenNil.toString())) {
			return nodeNil;
		}
		
		else {
			throw new RuntimeException("not implemented");
		}
	}
	
	private static Node prefix() {
		
	}
	
	private static Node addop() {
		
	}
	
	private static Node mulop() {
		
	}
	
	private static Node relop() {
		
	}
	
	private static Node id() {
		
	}
	
	private static Node num() {
		
	}
	
	private static Node bool() {
		
	}
	
	private static Node string() {
		
	}
	
}