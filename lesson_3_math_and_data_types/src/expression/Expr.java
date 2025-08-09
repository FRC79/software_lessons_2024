// © Sebastian Lopez-Cot 2025. Licensed CC BY-NC-SA 4.0.

package expression;

public interface Expr {
    public Expr evaluate();
    public boolean isTerminal();
    public int getValue();
    public String toString();
}
