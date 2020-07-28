package parser;

/*
 * Main Class for the Parser. Turn Tokens into Nodes.
 */

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
	
	//Create all Tokens and Nodes necessary for the Program
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
	private static Node nodePrePlus = new Builtin(Builtin.funct.prePLUS);
	private static Node nodeMinus = new Builtin(Builtin.funct.MINUS);
	private static Node nodePreMinus = new Builtin(Builtin.funct.preMINUS);
	private static Node nodeHd = new Builtin(Builtin.funct.HD);
	private static Node nodeTl = new Builtin(Builtin.funct.TL);
	private static Node nodeNil = new Builtin(Builtin.funct.NIL);
	private static Node nodeNot = new Builtin(Builtin.funct.NOT);
	private static Node nodeMul = new Builtin(Builtin.funct.MUL);
	private static Node nodeDiv = new Builtin(Builtin.funct.DIV);
	
	//Check if the next Token is the same as the Token "test"
	private boolean equalLookAhead(Token test) {
		return l.getLookahead().toString().equals(test.toString());
	}
	
	//Same as equalLookAhead, but also move one Token forward
	private Token match(Token t) {
		Token x = l.getToken();
		if (x.toString().equals(t.toString())) {
			return x;
		}
		else {
			throw new RuntimeException("Token mismatch");
		}
	}
	
	public Def system() {
		//Make a new Def Node, this will be the result of the Parser
		if(equalLookAhead(def)) {
			DefHashMap funcdefs = funcdefs();
			match(dot);
			Node expr = expr();
			Def system = new Def(funcdefs, expr);
			return system;
		}
		//If there is no "def" in the Program, make an empty Def node. This step is needed, because the 
		//Visitor needs a specific Node. So system() has to be a Def Node, no matter what.
		else {
			DefHashMap funcdefs = new DefHashMap();
			ArrayList<String> emptyList = new ArrayList<String>();
			emptyList.add("empty");
			StringConst emptyNode = new StringConst("empty");
			Pair<ArrayList<String>, Node> emptyPair = new Pair<ArrayList<String>, Node>(emptyList, emptyNode);
			funcdefs.put("empty", emptyPair);
			Def expr = new Def(funcdefs, expr());
			return expr;
		}
	}
	
	private DefHashMap funcdefs() {
		DefHashMap f = new DefHashMap();
		return funcdefs1(f);
	}
	
	private DefHashMap funcdefs1(DefHashMap f) {
		//If there is a def, fill it and repeat until there is no def left anymore. Then return the HashMap.
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
		//Array var, to save the Variables
		ArrayList<String> var = new ArrayList<String>();
		//Fill HashMap
		f.put(name().toString(), abstraction(var));
		return f;
	}
	
	private Pair<ArrayList<String>, Node> abstraction(ArrayList<String> var) {
		if(equalLookAhead(tokenEqu)) {
			match(tokenEqu);
			Pair<ArrayList<String>, Node> abst = new Pair<ArrayList<String>, Node>(var, expr());
			return abst;
		}
		else {
			var.add(name().toString());
			return abstraction(var);
		}	
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
	
	private Node expr() {
		Node cond = condExpr();
		Node exp1 = expr1();
		if(exp1 == null) {
			return cond;
		}
		else {
			At exprAt = new At(cond, exp1);
			return exprAt;
		}
	}
	
	private Node expr1() {
		//Rest not implemented.
		return null;
	}

	private Node listExpr() {
		//If there is a ":".
		Node opEx = opExpr();
		Node listexp1 = listExpr1(opEx);
		if(listexp1 == null) {
			return opEx;
		}
		else {
			return listexp1;
		}
	}
	
	private Node listExpr1(Node expr) {
		if(equalLookAhead(tokenColon)) {
			match(tokenColon);
			At listExpr1At = new At(nodeColon, expr);
			At resultAt = new At(listExpr1At, listExpr());
			return resultAt;
		}
		else {
			return null;
		}
	}
	
	private Node opExpr() {
		//If there is an "or".
		Node con = conjunct();
		Node opexp1 = opExpr1(con);
		if(opexp1 == null) {
			return con;
		}
		else {
			return opexp1;
		}
	}
	
	private Node opExpr1(Node opExprCon) {
		//Wenn ein or kommt mache ein At von or und conjunct, wenn es kein weiteres or mehr gibt,
		//also opExpr1 ist leer, gib nur dieses at aus. Sonst mache ein At aus dem letzten At und 
		//der opExpr1 und gebe dieses aus.
		//Funktioniert bei and, plus, minus,... gleich
		if(equalLookAhead(tokenOr)) {
			match(tokenOr);
			Node con = conjunct();
			Node opexp1 = opExpr1(con);
			At opExpr1AtOr = new At(nodeOr, opExprCon);
			if(opexp1 == null) {
				At resultAt = new At(opExpr1AtOr, con);
				return resultAt;
			}
			else {
				At opExpr1At = new At(opExpr1AtOr, opexp1);
				return opExpr1At;
			}
		}
		else {
			return null;
		}
	}
	
	private Node conjunct() {
		//If there is an "and".
		Node com = compar();
		Node con1 = conjunct1(com);
		if(con1 == null) {
			return com;
		}
		else {
			return con1;
		}
	}
	
	private Node conjunct1(Node conCom) {
		if(equalLookAhead(tokenAnd)) {
			match(tokenAnd);
			Node com = compar();
			Node con1 = conjunct1(com);
			At conjunct1AtAnd = new At(nodeAnd, conCom);
			if(con1 == null) {
				At resultAt = new At(conjunct1AtAnd, com);
				return resultAt;
			}
			else {
				At conjunct1At = new At(conjunct1AtAnd, con1);
				return conjunct1At;
			}
		}
		else {
			return null;
		}
	}
	
	private Node compar() {
		//If there is a comparison Symbol
		Node add = add();
		Node comp1 = compar1(add);
		if(comp1 == null) {
			return add;
		}
		else {
			return comp1;
		}
	}
	
	
	private Node compar1(Node comAdd) {
		if(isRelopToken()) {
			Node relopNode = relop();
			Node add = add();
			Node comp1 = compar1(add);
			At compar1RelopAt = new At(relopNode, comAdd);
			if(comp1 == null) {
				At resultAt = new At(compar1RelopAt, add);
				return resultAt;
			}
			else {
				At compar1At = new At(compar1RelopAt, comp1);
				return compar1At;
			}
		}
		else {
			return null;
		}
	}
	
	private boolean isRelopToken() {
		//Check if next token is a comparison Symbol
		if(equalLookAhead(tokenEqu)) {
			return true;
		}
		else if(equalLookAhead(tokenNeq)) {
			return true;
		}
		else if(equalLookAhead(tokenLes)) {
			return true;
		}
		else if(equalLookAhead(tokenGrt)) {
			return true;
		}
		else if(equalLookAhead(tokenLeq)) {
			return true;
		}
		else if(equalLookAhead(tokenGeq)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private Node add() {
		//If next token is a Plus or Minus
		Node mul = mul();
		Node add1 = add1(mul);
		if(add1 == null) {
			return mul;
		}
		else {
			return add1;
		}
	}
	
	private Node add1(Node addMul) {
		if(isAddopToken()) {
			Node addopNode = addop();
			Node mul = mul();
			Node add1 = add1(mul);
			At add1AddopAt = new At(addopNode, addMul);
			if(add1 == null) {
				At y = new At(add1AddopAt, mul);
				return y;
			}
			else {
				At add1At = new At(add1AddopAt, add1);
				return add1At;
			}
		}
		else {
			return null;
		}
	}
	
	 private boolean isAddopToken() {
		//Check if next token is Plus or Minus
		return equalLookAhead(tokenPlus) || equalLookAhead(tokenMinus);
	}

	private Node mul() {
		//If there is a * or /
		Node fac = factor();
		Node mul1 = mul1(fac);
		if(mul1 == null) {
			return fac;
		}
		else {
			return mul1;
		}
	}
	
	private Node mul1(Node mulFac) {
		if(isMulopToken()) {
			Node mulopNode = mulop();
			Node fac = factor();
			Node mul1 = mul1(fac);
			At mul1MulopAt = new At(mulopNode, mulFac);
			if(mul1 == null) {
				At y = new At(mul1MulopAt, fac);
				return y;
			}
			else {
				At mul1At = new At(mul1MulopAt, mul1);
				return mul1At;
			}
		}
		else {
			return null;
		}
	}
	
	private boolean isMulopToken() {
		//Check if next Token is * or /
		return equalLookAhead(tokenMul) || equalLookAhead(tokenDiv);
	}

	private boolean isPrefixToken() {
		//Check if next Token is a prefix
		return equalLookAhead(tokenMinus) || equalLookAhead(tokenPlus) || equalLookAhead(tokenNot);
	}
	
	private Node factor() {
		//If there is a Prefix.
		if(isPrefixToken()) {
			Node prefixNode = prefix();
			Node comb = comb();
			At factorAt = new At(prefixNode, comb);
			return factorAt;
		}
		else {
			return comb();
		}
	}
	
	private Node comb() {
		//If there is another Id, Hd or Tl, Constant, Nil or "(".
		Node simple = simple();
		Node comb1 = comb1(simple);
		if(comb1 == null) {
			return simple;
		}
		else {
			return comb1;
		}
	}
	
	private boolean isIdClass() {
		//Check if Token is an Identifier
		return l.getLookahead().getClass() == Identifier.class;
	}
	
	private boolean isHdOrTl() {
		//Check if Token is a Hd or Tl
		return equalLookAhead(tokenHd) || equalLookAhead(tokenTl);
	}
	
	private boolean isConstantClass() {
		//Check if Token is a Constant
		return l.getLookahead().getClass() == Constants.class;
	}
	
	private boolean isParenl() {
		//Check if Token is a "("
		return equalLookAhead(tokenParenl);
	}
	
	private boolean isNil() {
		//Check if Token is a "nil"
		return equalLookAhead(tokenNil);
	}
	
	private Node comb1(Node x) {
		//wenn comb1 ein identifier, hd or tl, eine konstante oder eine offene klammer ist
		//gebe entweder simple zurück(wenn das nächste comb1 empty ist) odedr ein at von simple und comb1 zurück
		//(wenn das nächste token wieder ein comb1 ist). Sonst null.
		if(isIdClass() || isHdOrTl() || isConstantClass() || isNil() || isParenl()) {
			Node simp = simple();
			At w = new At(x, simp);
			Node comb1 = comb1(w);
			if(comb1 == null) {
				return w;
			}
			else {
				return comb1;
			}
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
		
		else if(isConstantClass() || isNil()) {
			return constant();
		}
		
		else if(isParenl()) {
			match(tokenParenl);
			Node simpleExpr = expr();
			match(tokenParenr);
			return simpleExpr;
		}

		else {
			throw new RuntimeException("not a simple");
		}
	}
	
	//Make a Node out of an ID Token
	private Node name() {
		Var name = new Var(id());
		return name;
	}
	
	//Make a Node out of Hd or Tl Token
	private Node builtin() {
		if(equalLookAhead(tokenHd)) {
			match(tokenHd);
			return nodeHd;
		}
		else {
			match(tokenTl);
			return nodeTl;
		}
	}
	
	String checkID = "<ID: [a-zA-Z_][A-Za-z0-9_]*>";
	String checkNumber = "<Constant num: [0-9]+>";
	String checkBoolT = "<Constant boolean: true>";
	String checkBoolF = "<Constant boolean: false>";
	String checkString = "<Constant string: [\"][\\x00-\\x7F]*[\"]>";
	
	// * Check if the Token is a valid Token. *
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
	
	// * If Token is a valid Token make a fitting Const Node out of it *
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
		
		else if(equalLookAhead(tokenNil)) {
			match(tokenNil);
			return nodeNil;
		}
		
		else {
			throw new RuntimeException("not implemented");
		}
	}
	
	// * Make Symbol Node out of Prefix Tokens *
	private Node prefix() {
		if(equalLookAhead(tokenMinus)) {
			match(tokenMinus);
			return nodePreMinus;
		}
		else if(equalLookAhead(tokenPlus)) {
			match(tokenPlus);
			return nodePrePlus;
		}
		else if(equalLookAhead(tokenNot)) {
			match(tokenNot);
			return nodeNot;
		}
		else {
			throw new RuntimeException("not a prefix");
		}
	}
	
	// * Make Node out of "+" and "-" *
	private Node addop() {
		if(equalLookAhead(tokenMinus)) {
			match(tokenMinus);
			return nodeMinus;
		}
		else if(equalLookAhead(tokenPlus)) {
			match(tokenPlus);
			return nodePlus;
		}
		else {
			throw new RuntimeException("not an addop");
		}
	}
	
	// * Make Node out of "*" and "/" *
	private Node mulop() {
		if(equalLookAhead(tokenMul)) {
			match(tokenMul);
			return nodeMul;
		}
		else if(equalLookAhead(tokenDiv)) {
			match(tokenDiv);
			return nodeDiv;
		}
		else {
			throw new RuntimeException("not a mulop");
		}
	}
	
	// * Make Node out of Comparison Tokens *
	private Node relop() {
		if(equalLookAhead(tokenEqu)) {
			match(tokenEqu);
			return nodeEqu;
		}
		else if(equalLookAhead(tokenNeq)) {
			match(tokenNeq);
			return nodeNeq;
		}
		else if(equalLookAhead(tokenLes)) {
			match(tokenLes);
			return nodeLes;
		}
		else if(equalLookAhead(tokenGrt)) {
			match(tokenGrt);
			return nodeGrt;
		}
		else if(equalLookAhead(tokenLeq)) {
			match(tokenLeq);
			return nodeLeq;
		}
		else if(equalLookAhead(tokenGeq)) {
			match(tokenGeq);
			return nodeGeq;
		}
		else {
			throw new RuntimeException("not a relop");
		}
	}
	
	//Return String of the Token only and go to the next Token
	private String id() {
		//"<Id: ...>" has 5 Characters before "<Id: " and 1 after ">" the name of the Token.
		if(isId()) {
			Token id = l.getToken();
			String onlyID = id.toString().substring(5, id.toString().length() - 1);
			return onlyID;
		}
		else {
			throw new RuntimeException("not an id");
		}
	}
	
	//Return int of the Token and go to the next Token
	private int num() {
		Token numToken = l.getToken();
		String onlyNum = numToken.toString().substring(15, numToken.toString().length() - 1);
		int num = Integer.parseInt(onlyNum);
		return num;
	}
	
	//Return boolean of the Token and go to the next Token
	private boolean bool() {
		Token bool = l.getToken();
		if(bool.toString().matches(checkBoolT)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Return the string of the Token and go to the next Token
	private String string() {
		Token string = l.getToken();
		String onlyString = string.toString().substring(18, string.toString().length() - 1);
		return onlyString;
	} 
}