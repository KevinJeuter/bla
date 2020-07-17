package ast;

import java.util.Objects;

//Klasse Pair mit Datentypen F, S. first und second sind die Werte.

public class Pair<F, S> {
    private final F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Überprüft, ob 2 Paare gleich sind oder nicht.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second);
    }
    
    @Override
    public String toString() {
    	return "Pair: <" + this.first + ", " + this.second + ">";
    }
    
    public F getKey() {
    	return this.first;
    }
    
    public S getValue() {
    	return this.second;
    }
    
    public Pair<F, S> setValue(S x) {
    	this.second = x;
    	Pair<F, S> newPair = new Pair<F, S>(this.first, this.second);
    	return newPair;
    }
}
