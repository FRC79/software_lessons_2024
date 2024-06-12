package expression;

public class TermExpr implements Expr {
    private Expr left;
    private Expr right;
    public enum Type {
        MULTIPLY,
        DIVIDE
    };
    private Type type;

    public TermExpr(Expr left, Expr right, Type type) {
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public int getValue() {
        throw new RuntimeException("TermExpr is not a terminal expression.");
    }

    public boolean isTerminal() {
        return false;
    }

    public Expr evaluate() {
        Expr newLeft = left;
        Expr newRight = right;
        while(!(newLeft instanceof BaseExpr)) {
            newLeft = newLeft.evaluate();
        }
        while(!(newRight instanceof BaseExpr)) {
            newRight = newRight.evaluate();
        }

        if (type == Type.MULTIPLY) {
            return new BaseExpr(newLeft.getValue() * newRight.getValue());
        } else if (type == Type.DIVIDE) {
            return new BaseExpr(newLeft.getValue() / newRight.getValue());
        } else {
            throw new RuntimeException("Invalid TermExpr type passed.");
        }   
    }

    public String toString() {
        return String.format("TermExpr(%s, %s, %s)", left.toString(), right.toString(), type.name());
    }
}
