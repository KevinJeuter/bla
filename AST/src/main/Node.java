package main;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node {
	
	public static void main(String[] args) {
		
		//Baue where
		NumberConst one = new NumberConst(1);
		Builtin colon = new Builtin(Builtin.funct.COLON);
		StringConst nil = new StringConst("nil");
		
		Var l = new Var("l");
		Var nul = new Var("null");
		
		ArrayList<String> whereList = new ArrayList<String>();
		whereList.add("l");
		
		//At Knoten : @ 1
		At colonOneAt = new At(colon, one);
		//At Knoten var(null) @ l
		At nullLAt = new At(nul, l);
		//At Knoten @ @ nil auf rechter Seite
		At atNilAtRight = new At(colonOneAt, nil);
		//Paar Liste mit Parameter (hier l) und dem @ knoten. 
		Pair<ArrayList<String>, Node> wherePair = new Pair<ArrayList<String>, Node>(whereList, atNilAtRight);
		//Where knoten
		Where bspWhere = new Where(wherePair, nullLAt);
		
		//Baue def
		
		Builtin equ = new Builtin(Builtin.funct.EQU);
		Var xs = new Var("xs");
		
		//At Knoten = @ var(xs)
		At equXsAt = new At(equ, xs);
		//at Knoten @ @ nil
		At atNilAtLeft = new At(equXsAt, nil);
		//parameter für null
		ArrayList<String> nullParam = new ArrayList<String>();
		nullParam.add("xs");
		//paar aus namen und knoten für def
		Pair<ArrayList<String>, Node> defPair = new Pair<ArrayList<String>, Node>(nullParam, atNilAtLeft);
		//hashmap für def
		HashMap<String, Pair<ArrayList<String>, Node>> defHash= new HashMap<String, Pair<ArrayList<String>, Node>>();
		defHash.put("null", defPair);
		
		Def bspDef = new Def(defHash, bspWhere);
	}
	
}
