package parser;

import java.util.ArrayList;
import java.util.HashMap;

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
import lexer.Specials;
import lexer.Symbols;
import lexer.Token;
import lexer.Lexer;


public class Parser {
	
	private Lexer l;
	
	public Parser(Lexer l) {
		this.l = l;
	}
	//nicht 2 mal expr(), wenn dann mit variable
	
	//expr(), condexpr(), listexpr() bringt die tokens dazwischen als node
	
	private static Keywords tokenNil = new Keywords("nil");
	private static Keywords tokenHd = new Keywords("hd");
	private static Keywords tokenTl = new Keywords("tl");
	private static Keywords ifKey = new Keywords("if");
	private static Keywords thenKey = new Keywords("then");
	private static Keywords elseKey = new Keywords("else");
	private static Keywords tokenOr = new Keywords("or");
	private static Keywords tokenAnd = new Keywords("and");
	private static Keywords tokenNot = new Keywords("not");
	private static Keywords def = new Keywords("def");
	private static Symbols tokenEqu = new Symbols("=");
	private static Symbols tokenNeq = new Symbols("~=");
	private static Symbols tokenGeq = new Symbols(">=");
	private static Symbols tokenLeq = new Symbols("<=");
	private static Symbols tokenGrt = new Symbols(">");
	private static Symbols tokenLes = new Symbols("<");
	private static Symbols tokenColon = new Symbols(":");
	private static Symbols tokenSemicolon = new Symbols(";");
	private static Symbols dot = new Symbols(".");
	private static Symbols tokenPlus = new Symbols("+");
	private static Symbols tokenMinus = new Symbols("-");
	private static Symbols tokenMul = new Symbols("*");
	private static Symbols tokenDiv = new Symbols("/");
	private static Symbols tokenParenl = new Symbols("(");
	private static Symbols tokenParenr = new Symbols(")");

	private static Node nodeEqu = new Builtin(Builtin.funct.EQU);
	private static Node nodeNeq = new Builtin(Builtin.funct.NEQ);
	private static Node nodeLeq = new Builtin(Builtin.funct.LEQ);
	private static Node nodeGeq = new Builtin(Builtin.funct.GEQ);
	private static Node nodeLes = new Builtin(Builtin.funct.LES);
	private static Node nodeGrt = new Builtin(Builtin.funct.GRT);
	private static Node nodeOr = new Builtin(Builtin.funct.OR);
	private static Node nodeAnd = new Builtin(Builtin.funct.AND);
	private static Node nodeCond = new Builtin(Builtin.funct.COND);
	private static Node nodeColon = new Builtin(Builtin.funct.COLON);
	private static Node nodePlus = new Builtin(Builtin.funct.PLUS);
	private static Node nodeMinus = new Builtin(Builtin.funct.MINUS);
	private static Node nodeHd = new Builtin(Builtin.funct.HD);
	private static Node nodeTl = new Builtin(Builtin.funct.TL);
	private static Node nodeNil = new Builtin(Builtin.funct.NIL);	//noch umformen
	private static Node nodeNot = new Builtin(Builtin.funct.NOT);
	private static Node nodeMul = new Builtin(Builtin.funct.MUL);
	private static Node nodeDiv = new Builtin(Builtin.funct.DIV);
	
	private boolean equalLookAhead(Token test) {
		return l.getLookahead().toString().equals(test.toString());
	}
	
	public Def system() {
		HashMap<String, Pair<ArrayList<String>, Node>> funcdefs = new HashMap<String, Pair<ArrayList<String>, Node>>();
		if(equalLookAhead(def)) {
			funcdefs = funcdefs().returnHashMap();
			match(dot);
			Node expr = expr();
			Def system = new Def(funcdefs, expr);
			return system;
		}
		else {
			ArrayList<String> emptyList = new ArrayList<String>();
			emptyList.add("");
			Pair<ArrayList<String>, Node> emptyPair = new Pair<ArrayList<String>, Node>(emptyList, null);
			funcdefs.put("", emptyPair);
			Def expr = new Def(funcdefs, expr());
			return expr;
		}
	}
	
	private DefHashMap funcdefs() {
		DefHashMap f = new DefHashMap();
		return funcdefs1(f);
	}
	
	private DefHashMap funcdefs1(DefHashMap f) {
		if(equalLookAhead(def)) {
			match(def);
			def(f);
			return funcdefs1(f);
		}
		else {
			return f;
		}
	}
	
	private DefHashMap def(DefHashMap f) {
		f.put(name().toString(), abstraction());
		return f;
	}
	
	private ArrayList<String> var = new ArrayList<String>();
	private Node funct;
	
	private Pair<ArrayList<String>, Node> abstraction() {
		if(equalLookAhead(tokenEqu)) {
			match(tokenEqu);
			funct = expr();
		}
		else {
			var.add(name().toString());
			abstraction();
		}
		Pair<ArrayList<String>, Node> abst = new Pair<ArrayList<String>, Node>(var, funct);
		var.clear();
		
		return abst;
	}
	
	private Node condExpr() {
		if(equalLookAhead(ifKey)) {
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
	
	private Token match(Token t) {
		Token x = l.getToken();
		if (x.toString().equals(t.toString())) {
			return x;
		}
		else {
			throw new RuntimeException("Token mismatch");
		}
	}
	
	private Node expr() {
		if(expr1() == null) {
			return condExpr();
		}
		else {
			At exprAt = new At(condExpr(), expr1());
			return exprAt;
		}
	}
	
	private Node expr1() {
		return null;
	}
	
	private Node listExpr() {
		At listExprAt = new At(opExpr(), listExpr1());
		return listExprAt;
	}
	
	private Node listExpr1() {
		if(equalLookAhead(tokenColon)) {
			match(tokenColon);
			At listExpr1At = new At(nodeColon, listExpr());
			return listExpr1At;
		}
		else {
			return null;
		}
	}
	
	private Node opExpr() {
		At opExprAt = new At(conjunct(), opExpr1());
		return opExprAt;
	}
	
	private Node opExpr1() {
		if(equalLookAhead(tokenOr)) {
			match(tokenOr);
			At opExpr1AtOr = new At(nodeOr, conjunct());
			At opExpr1At = new At(opExpr1AtOr, opExpr1());
			return opExpr1At;
		}
		else {
			return null;
		}
	}
	
	private Node conjunct() {
		At conjunctAt = new At(compar(), conjunct1());
		return conjunctAt;
	}
	
	private Node conjunct1() {
		if(equalLookAhead(tokenAnd)) {
			match(tokenAnd);
			At conjunct1AtAnd = new At(nodeAnd, compar());
			At conjunct1At = new At(conjunct1AtAnd, conjunct1());
			return conjunct1At;
		}
		else {
			return null;
		}
	}
	
	private Node compar() {
		At comparAt = new At(add(), compar1());
		return comparAt;
	}
	
	private Token relopToken;
	
	private boolean isRelopToken() {
		if(equalLookAhead(tokenEqu)) {
			relopToken = tokenEqu;
			return true;
		}
		else if(equalLookAhead(tokenNeq)) {
			relopToken = tokenNeq;
			return true;
		}
		else if(equalLookAhead(tokenLes)) {
			relopToken = tokenLes;
			return true;
		}
		else if(equalLookAhead(tokenGrt)) {
			relopToken = tokenGrt;
			return true;
		}
		else if(equalLookAhead(tokenLeq)) {
			relopToken = tokenLeq;
			return true;
		}
		else if(equalLookAhead(tokenGeq)) {
			relopToken = tokenGeq;
			return true;
		}
		else {
			return false;
		}
	}
	
	private Node compar1() {
		if(isRelopToken()) {
			Node relopNode = relop();
			match(relopToken);
			At compar1RelopAt = new At(relopNode, add());
			At compar1At = new At(compar1RelopAt, compar1());
			return compar1At;
		}
		else {
			return null;
		}
	}
	
	private Node add() {
		At addAt = new At(mul(), add1());
		return addAt;
	}
	
	private Token addopToken;
	
	private boolean isAddopToken() {
		if(equalLookAhead(tokenPlus)) {
			addopToken = tokenPlus;
			return true;
		}
		else if(equalLookAhead(tokenMinus)) {
			addopToken = tokenMinus;
			return true;
		}
		else {
			return false;
		}
	}
	
	private Node add1() {
		if(isAddopToken()) {
			Node addopNode = addop();
			match(addopToken);	//hier muss token von addop
			At add1AddopAt = new At(addopNode, mul());
			At add1At = new At(add1AddopAt, add1());
			return add1At;
		}
		else {
			return null;
		}
	}
	
	private Node mul() {
		At mulAt = new At(factor(), mul1());
		return mulAt;
	}
	
	private Token mulopToken;
	
	private boolean isMulopToken() {
		if(equalLookAhead(tokenMul)) {
			mulopToken = tokenMul;
			return true;
		}
		else if(equalLookAhead(tokenDiv)) {
			mulopToken = tokenDiv;
			return true;
		}
		else {
			return false;
		}
	}
	
	private Node mul1() {
		if(isMulopToken()) {
			Node mulopNode = mulop();
			match(mulopToken); //hier muss token von mulop
			At mul1MulopAt = new At(mulopNode, factor());
			At mul1At = new At(mul1MulopAt, mul1());
			return mul1At;
		}
		else {
			return null;
		}
	}
	
	private Token prefixToken;
	
	private boolean isPrefixToken() {
		if(equalLookAhead(tokenMinus)) {
			prefixToken = tokenMinus;
			return true;
		}
		else if(equalLookAhead(tokenPlus)) {
			prefixToken = tokenPlus;
			return true;
		}
		else if(equalLookAhead(tokenNot)) {
			prefixToken = tokenNot;
			return true;
		}
		else {
			return false;
		}
	}
	
	private Node factor() {
		if(isPrefixToken()) {
			Node prefixNode = prefix();
			match(prefixToken);
			At factorAt = new At(prefixNode, comb());
			return factorAt;
		}
		else {
			return comb();
		}
	}
	
	private Node comb() {
		At combAt = new At(simple(), comb1());
		return combAt;
	}
	
	private boolean isIdClass() {
		return l.getLookahead().getClass() == lexer.Identifier.class;
	}
	
	private boolean isHdOrTl() {
		return equalLookAhead(tokenHd) || equalLookAhead(tokenTl);
	}
	
	private boolean isConstantClass() {
		return l.getLookahead().getClass() == lexer.Constants.class;
	}
	
	private boolean isParenl() {
		return equalLookAhead(tokenParenl);
	}
	
	private Node comb1() {
		if(isIdClass() || isHdOrTl() || isConstantClass() || isParenl()) {
			At comb1At = new At(simple(), comb1());
			return comb1At;
		}
		else {
			return null;
		}
	}
	
	private Node simple() {
		if(isIdClass()) {
			return name();
		}
		
		else if(isHdOrTl()) {
			return builtin();
		}
		
		else if(isConstantClass()) {
			return constant();
		}
		
		else {
			match(tokenParenl);
			Node simpleExpr = expr();
			match(tokenParenr);
			return simpleExpr;
		}
	}
	
	private Node name() {
		Var name = new Var(id());
		return name;
	}
	
	private Node builtin() {
		if(equalLookAhead(tokenHd)) {
			return nodeHd;
		}
		else {
			return nodeTl;
		}
	}
	
	String checkID = "<ID: [a-zA-Z_][a-zA-Z_0-9]*>";
	String checkNumber = "<Constant num: [0-9]+>";
	String checkBoolT = "<Constant boolean: true>";
	String checkBoolF = "<Constant boolean: false>";
	String checkString = "<Constant string: [\"][\\x00-\\x7F]*[\"]>";
	
	private boolean isId() {
		return l.getLookahead().toString().matches(checkID);
	}
	
	private boolean isNum() {
		return l.getLookahead().toString().matches(checkNumber);
	}
	
	private boolean isBool() {
		return l.getLookahead().toString().matches(checkBoolT) || l.getLookahead().toString().matches(checkBoolF);
	}
	
	private boolean isString() {
		return l.getLookahead().toString().matches(checkString);
	}
	
	private Node constant() {
		if(isNum()) {
			NumberConst num = new NumberConst(num());
			return num;
		}
		
		else if(isBool()) {
			BooleanConst bool = new BooleanConst(bool());
			return bool;
		}
		
		else if(isString()) {
			StringConst str = new StringConst(string());
			return str;
		}
		
		else if(equalLookAhead(tokenNil)) {	//eigene klasse constNil
			return nodeNil;
		}
		
		else {
			throw new RuntimeException("not implemented");
		}
	}
	
	private Node prefix() {
		if(equalLookAhead(tokenMinus)) {
			return nodeMinus;
		}
		else if(equalLookAhead(tokenPlus)) {
			return nodePlus;
		}
		else if(equalLookAhead(tokenNot)) {
			return nodeNot;
		}
		else {
			throw new RuntimeException("not a prefix");
		}
	}
	
	private Node addop() {
		if(equalLookAhead(tokenMinus)) {
			return nodeMinus;
		}
		else if(equalLookAhead(tokenPlus)) {
			return nodePlus;
		}
		else {
			throw new RuntimeException("not an addop");
		}
	}
	
	private Node mulop() {
		if(equalLookAhead(tokenMul)) {
			return nodeMul;
		}
		else if(equalLookAhead(tokenDiv)) {
			return nodeDiv;
		}
		else {
			throw new RuntimeException("not a mulop");
		}
	}
	
	private Node relop() {
		if(equalLookAhead(tokenEqu)) {
			return nodeEqu;
		}
		else if(equalLookAhead(tokenNeq)) {
			return nodeNeq;
		}
		else if(equalLookAhead(tokenLes)) {
			return nodeLes;
		}
		else if(equalLookAhead(tokenGrt)) {
			return nodeGrt;
		}
		else if(equalLookAhead(tokenLeq)) {
			return nodeLeq;
		}
		else if(equalLookAhead(tokenGeq)) {
			return nodeGeq;
		}
		else {
			throw new RuntimeException("not a relop");
		}
	}
	
	private String id() {
		if(isId()) {
			String onlyID = l.getLookahead().toString().substring(5, l.getLookahead().toString().length() - 1);
			return onlyID;
		}
		else {
			throw new RuntimeException("not an id");
		}
	}
	
	private int num() {
		if(isNum()) {
			String onlyNum = l.getLookahead().toString().substring(15, l.getLookahead().toString().length() - 1);
			int num = Integer.parseInt(onlyNum);
			return num;
		}
		else {
			throw new RuntimeException("not a number");
		}
	}
	
	private boolean bool() {
		if(l.getLookahead().toString().matches(checkBoolF)) {
			return true;
		}
		else if(l.getLookahead().toString().matches(checkBoolT)) {
			return false;
		}
		else {
			throw new RuntimeException("not a bool const");
		}
	}
	
	private String string() {
		if(isString()) {
			String onlyString = l.getLookahead().toString().substring(18, l.getLookahead().toString().length() - 1);
			return onlyString;
		}
		else {
			throw new RuntimeException("not a string const");
		}
	} 
}