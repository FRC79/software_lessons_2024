package expression;

public class BaseExpr implements Expr {
    private int number;

    public BaseExpr(int number) {
        this.number = number;
    }

    public int getValue() {
        return number;
    }

    public boolean isTerminal() {
        return true;
    }

    public Expr evaluate() {
        return this;
    }

    public String toString() {
        return String.format("BaseExpr(%d)", number);
    }
}
