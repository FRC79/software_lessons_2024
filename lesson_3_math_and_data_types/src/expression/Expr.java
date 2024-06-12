package expression;

public interface Expr {
    public Expr evaluate();
    public boolean isTerminal();
    public int getValue();
    public String toString();
}
