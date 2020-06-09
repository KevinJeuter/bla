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


public class Parser {
	
	//Habe es nicht richtig verstanden. Bei meinem public Node condExpr wären die Lexer.getToken() alle noch
	//keine Nodes. Auch ob ich das match richtig verstanden habe, weiss ich nicht. Und es gibt ja mehrere tokens
	//zwischen if then und then else. Das müsste man dann für das @ auch noch anders machen.
	
	private static Lexer l = new Lexer(lexer.Main.getTokens());
	//nicht 2 mal expr(), wenn dann mit variable
	
	//expr(), condexpr(), listexpr() bringt die tokens dazwischen als node
	private static Keywords ifKey = new Keywords("if");
	private static Keywords thenKey = new Keywords("then");
	private static Keywords elseKey = new Keywords("else");
	private static Keywords tokenOr = new Keywords("or");
	private static Keywords tokenAnd = new Keywords("and");
	private static Symbols tokenColon = new Symbols(":");
	private static Node nodeOr = new Builtin(Builtin.funct.OR);
	private static Node nodeAnd = new Builtin(Builtin.funct.AND);
	private static Node nodeCond = new Builtin(Builtin.funct.COND);
	private static Node nodeColon = new Builtin(Builtin.funct.COLON);
	private static Node empty = new StringConst("");
	
	public static Node condExpr() {
		if(l.getLookahead() == ifKey) {
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
		if (x == t) {
			return x;
		}
		else {
			throw new RuntimeException("Token mismatch");
		}
	}
	
	private static Node expr() {
		At exprAt = new At(condExpr(), expr1());
		return exprAt;
	}
	
	private static Node expr1() {
		return empty;
	}
	
	private static Node listExpr() {
		At listExprAt = new At(opExpr(), listExpr1());
		return listExprAt;
	}
	
	private static Node listExpr1() {
		if(l.getLookahead() == tokenColon) {
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
		if(l.getLookahead() == tokenOr) {
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
		if(l.getLookahead() == tokenAnd) {
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
		if(l.getLookahead() == relop()) {
			match(relop());
		}
		else {
			return empty;
		}
	}
}