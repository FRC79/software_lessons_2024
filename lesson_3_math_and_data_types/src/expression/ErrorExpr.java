package expression;

public class ErrorExpr implements Expr {
    private String msg;

    public ErrorExpr(String msg) {
        this.msg = msg;
    }

    public int getValue() {
        throw new RuntimeException("Cannot get value from ErrorExpr");
    }

    public boolean isTerminal() {
        return true;
    }

    public Expr evaluate() {
        return this;
    }

    public String toString() {
        return String.format("ErrorExpr(%s)", msg);
    }
}
