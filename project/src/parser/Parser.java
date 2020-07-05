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
	private static Node nodePrePlus = new Builtin(Builtin.funct.prePLUS);
	private static Node nodeMinus = new Builtin(Builtin.funct.MINUS);
	private static Node nodePreMinus = new Builtin(Builtin.funct.preMINUS);
	private static Node nodeHd = new Builtin(Builtin.funct.HD);
	private static Node nodeTl = new Builtin(Builtin.funct.TL);
	private static Node nodeNil = new Builtin(Builtin.funct.NIL);	//noch umformen
	private static Node nodeNot = new Builtin(Builtin.funct.NOT);
	private static Node nodeMul = new Builtin(Builtin.funct.MUL);
	private static Node nodeDiv = new Builtin(Builtin.funct.DIV);
	
	public static HashMap<String, Pair<ArrayList<String>, Node>> funcdefs = new HashMap<String, Pair<ArrayList<String>, Node>>();
	
	//Funktion zum überprüfen, ob Token_test gleich nächstes Token im Stream ist
	private boolean equalLookAhead(Token test) {
		return l.getLookahead().toString().equals(test.toString());
	}
	
	//Überprüfen, ob Token gleich und ein Token weiter gehen im Stream
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
		//Wenn nächstes Token = Token_Def, mache neues Def aus funcdefs und expr
		if(equalLookAhead(def)) {
			funcdefs = funcdefs().returnHashMap();
			match(dot);
			Node expr = expr();
			Def system = new Def(funcdefs, expr);
			return system;
		}
		//Wenn nächstes token != Token_Def mache neues Def aus empty und expr
		else {
			ArrayList<String> emptyList = new ArrayList<String>();
			emptyList.add("");
			StringConst emptyNode = new StringConst("");
			Pair<ArrayList<String>, Node> emptyPair = new Pair<ArrayList<String>, Node>(emptyList, emptyNode);
			funcdefs.put("", emptyPair);
			Def expr = new Def(funcdefs, expr());
			return expr;
		}
	}
	
	private DefHashMap funcdefs() {
		//Neue Hashmap für Def 
		DefHashMap f = new DefHashMap();
		return funcdefs1(f);
	}
	
	private DefHashMap funcdefs1(DefHashMap f) {
		//Fülle Hashmap mit def und gebe die Hashmap durch funcdefs1(f) aus
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
		//Array var, um die Variablen zu speichern
		ArrayList<String> var = new ArrayList<String>();
		//Fülle Hashmap.
		f.put(name().toString(), abstraction(var));
		return f;
	}
	
	private Pair<ArrayList<String>, Node> abstraction(ArrayList<String> var) {
		if(equalLookAhead(tokenEqu)) {
			match(tokenEqu);
			//Wenn ein =, dann gib das Pair von Variablen und expr aus
			Pair<ArrayList<String>, Node> abst = new Pair<ArrayList<String>, Node>(var, expr());
			return abst;
		}
		else {
			//Füge die variablen zur liste var hinzu
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
	
	//bei listen braucht man parameter
	
	private Node expr() {
		//Wenn expr1 null ist, dann gib nur condExpr() aus, sonst mache ein At mit condExpr und expr
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
		//gib null aus, rest nicht implementiert
		return null;
	}

	private Node listExpr() {
		Node opEx = opExpr();
		Node listexp1 = listExpr1(opEx);
		//Wenn listExpr1 leer ist, gib nunr opEx aus
		if(listexp1 == null) {
			return opEx;
		}
		else {
		//Sonst gibt listexp1 aus
			return listexp1;
		}
	}
	
	private Node listExpr1(Node expr) {
		//Wenn ein : kommt, mache ein at von : und expr und dann ein at von dem vorherigen und listExpr, sonst kommt null raus.
		if(equalLookAhead(tokenColon)) {
			match(tokenColon);
			At listExpr1At = new At(nodeColon, expr);
			At y = new At(listExpr1At, listExpr());
			return y;
		}
		else {
			return null;
		}
	}
	
	private Node opExpr() {
		//Wenn opExpr1 leer ist, gib nur con aus, sonst ein At von con und opExpr1
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
		if(equalLookAhead(tokenOr)) {
			match(tokenOr);
			Node con = conjunct();
			Node opexp1 = opExpr1(con);
			At opExpr1AtOr = new At(nodeOr, opExprCon);
			if(opexp1 == null) {
				At y = new At(opExpr1AtOr, con);
				return y;
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
		//Wenn conjunct1 leer ist, gib nur compar aus. Sonst mache ein At aus compar und conjunct
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
		//Wenn das nächste token ein and ist, mache ein at aus and und compar.
		//Wenn compar1 leer ist, gebe dieses at aus. Sonst mache ein neues at
		//aus dem letzten at und conjunct1
		//Wenn kein and kommt, gibt conjunct1 null aus
		if(equalLookAhead(tokenAnd)) {
			match(tokenAnd);
			Node com = compar();
			Node con1 = conjunct1(com);
			At conjunct1AtAnd = new At(nodeAnd, conCom);
			if(con1 == null) {
				At y = new At(conjunct1AtAnd, com);
				return y;
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
		//Wenn compar1 leer ist, gebe add aus. Sonst mache ein at aus add und compar1
		Node add = add();
		Node comp1 = compar1(add);
		if(comp1 == null) {
			return add;
		}
		else {
			return comp1;
		}
	}
	
	private boolean isRelopToken() {
		//Überprüfe, ob das nächste token ein relop ist.
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
	
	private Node compar1(Node comAdd) {
		//Wenn das nächste token ein relop ist, mache eine zugehörige node.
		//Erstelle dann ein at von der Node und add. Wenn compar1 leer ist, gebe dieses at aus
		//Sonst mache einn neues at mit dem letzten at und compar1
		if(isRelopToken()) {
			Node relopNode = relop();
			Node add = add();
			Node comp1 = compar1(add);
			At compar1RelopAt = new At(relopNode, comAdd);
			if(comp1 == null) {
				At y = new At(compar1RelopAt, add);
				return y;
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
	
	
	private Node add() {
		Node mul = mul();
		Node add1 = add1(mul);
		if(add1 == null) {
			return mul;
		}
		else {
			return add1;
		}
	}
	
	 private boolean isAddopToken() {
		//Überprüfe, ob das nächste token ein addop ist.
		if(equalLookAhead(tokenPlus)) {
			return true;
		}
		else if(equalLookAhead(tokenMinus)) {
			return true;
		}
		else {
			return false;
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

	private Node mul() {
		//Wenn mul1 leer ist, gebe fac aus. Sonst mache ein at aus fac und mul1 und gebe dieses raus.
		Node fac = factor();
		Node mul1 = mul1(fac);
		if(mul1 == null) {
			return fac;
		}
		else {
			return mul1;
		}
	}
	
	private boolean isMulopToken() {
		//überprüfe ob das nächste token ein mulop ist.
		if(equalLookAhead(tokenMul)) {
			return true;
		}
		else if(equalLookAhead(tokenDiv)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private Node mul1(Node mulFac) {
		//wenn das nächste token ein mulop ist, mache ein at aus mulopnode unnd fac. Wenn mul1 leer ist gebe
		//diese aus. Sonst mache ein at aus dem letzten at und mul1
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

	private boolean isPrefixToken() {
		//überprüfe ob das nächste token ein prefix ist
		if(equalLookAhead(tokenMinus)) {
			return true;
		}
		else if(equalLookAhead(tokenPlus)) {
			return true;
		}
		else if(equalLookAhead(tokenNot)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private Node factor() {
		//Wenn das nächste Token ein prefix ist, gebe ein at vonn prefixnode und comb zurück,
		//Sonst gebe nur comb zurück
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
		//Wenn comb1 leer ist, returne simple. Sonst mache ein at aus simple und comb1 und gebe dieses zurück.
		Node simple = simple();
		Node comb1 = comb1();
		if(comb1 == null) {
			return simple;
		}
		else {
			At combAt = new At(simple, comb1);
			return combAt;
		}
	}
	
	private boolean isIdClass() {
		//Überprüfe, ob die Klasse des Tokens ein Identifier ist
		return l.getLookahead().getClass() == Identifier.class;
	}
	
	private boolean isHdOrTl() {
		//Überprüfe ob das token ein hd odder tl ist
		return equalLookAhead(tokenHd) || equalLookAhead(tokenTl);
	}
	
	private boolean isConstantClass() {
		//Überprüfe ob die klasse des tokens constants ist
		return l.getLookahead().getClass() == Constants.class;
	}
	
	private boolean isParenl() {
		//Überprüfe, ob das token ein ( ist.
		return equalLookAhead(tokenParenl);
	}
	
	private boolean isNil() {
		//Überprüfe, ob das token ein nil ist.
		return equalLookAhead(tokenNil);
	}
	
	private Node comb1() {
		//wenn comb1 ein identifier, hd or tl, eine konstante oder eine offene klammer ist
		//gebe entweder simple zurück(wenn das nächste comb1 empty ist) odedr ein at von simple und comb1 zurück
		//(wenn das nächste token wieder ein comb1 ist). Sonst null.
		if(isIdClass() || isHdOrTl() || isConstantClass() || isNil() || isParenl()) {
			Node simp = simple();
			Node comb1 = comb1();
			if(comb1 == null) {
				return simp;
			}
			else {
				At comb1At = new At(simp, comb1);
				return comb1At;
			}
		}
		
		else {
			return null;
		}
	}

	private Node simple() {
		//Wenn das token eine id ist, gebe name zurück.
		//Wenn es ein hd oder tl ist, gebe builtin zurück.
		//Wenn es eine konstante ist, gebe constant zurück.
		//sonst, wenn es eine klammer auf, dann expr, dann klammer zu ist, gebe expr zurück.
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
	
	private Node name() {
		//erstelle ein node var aus id() und gebe dieses zurück
		Var name = new Var(id());
		return name;
	}
	
	private Node builtin() {
		//wenn das token hd ist, matche es und gebe hd zurück, sonst matche auf tl und gebe tl zurück
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
	
	private boolean isId() {
		//Überprüfe, ob es eine ID ist
		return l.getLookahead().toString().matches(checkID);
	}
	
	private boolean isNum() {
		//Überprüfe, ob es eine num ist
		return l.getLookahead().toString().matches(checkNumber);
	}
	
	private boolean isBool() {
		//Überprüfe, ob es ein bool ist
		return l.getLookahead().toString().matches(checkBoolT) || l.getLookahead().toString().matches(checkBoolF);
	}
	
	private boolean isString() {
		//Überprüfe ob es ein string ist
		return l.getLookahead().toString().matches(checkString);
	}
	
	private Node constant() {
		//Wenn das token ein num ist, mache eine neue node aus num() und gebe es zurück
		//Wenn das token ein bool ist, mache eine neue node aus bool() und gebe es zurück
		//Wenn das token ein string ist, mache eine neue node aus string() und gebe es zurück
		//Wenn das token ein nil ist, matche es auf nil und gebe es zurück
		//sonst fehler.
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
			match(tokenNil);
			return nodeNil;
		}
		
		else {
			throw new RuntimeException("not implemented");
		}
	}
	
	private Node prefix() {
		//Überprüfe, ob token - + oder not und matche und returne dann darauf. sonst fehler
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
	
	private Node addop() {
		//überprüfe ob token - + matche darauf und gebe es aus. sonst fehler
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
	
	private Node mulop() {
		//überprüfe ob token * / matche darauf und gebe es aus. sonst fehler
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
	
	private Node relop() {
		//Überprüfe ob token = ~= >= <= > <, matche darauf und gebe es zurück, sonst fehler.
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
	
	private String id() {
		//führe getToken aus und returne den string.
		if(isId()) {
			Token id = l.getToken();
			String onlyID = id.toString().substring(5, id.toString().length() - 1);
			return onlyID;
		}
		else {
			throw new RuntimeException("not an id");
		}
	}
	
	private int num() {
		//führe getToken aus und returne die num.
		Token numToken = l.getToken();
		String onlyNum = numToken.toString().substring(15, numToken.toString().length() - 1);
		int num = Integer.parseInt(onlyNum);
		return num;
	}
	
	private boolean bool() {
		//führe getToken aus und returne das bool.
		Token bool = l.getToken();
		if(bool.toString().matches(checkBoolT)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private String string() {
		//führe getToken aus und returne den string.
		Token string = l.getToken();
		String onlyString = string.toString().substring(18, string.toString().length() - 1);
		return onlyString;
	} 
}