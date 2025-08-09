// © Sebastian Lopez-Cot 2025. Licensed CC BY-NC-SA 4.0.

package expression;

public class StmtExpr implements Expr {
    private Expr left;
    private Expr right;
    public enum Type {
        ADD,
        SUBTRACT
    };
    private Type type;

    public StmtExpr(Expr left, Expr right, Type type) {
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public int getValue() {
        throw new RuntimeException("StmtExpr is not a terminal expression.");
    }

    public boolean isTerminal() {
        return false;
    }

    public Expr evaluate() {
        Expr finalLeft = left;
        Expr finalRight = right;
        while (!(finalLeft instanceof BaseExpr)) {
            finalLeft = finalLeft.evaluate();
        }
        while (!(finalRight instanceof BaseExpr)) {
            finalRight = finalRight.evaluate();
        }

        final int leftValue = finalLeft.getValue();
        final int rightValue = finalRight.getValue();

        if (type == Type.ADD) {
            return new BaseExpr(leftValue + rightValue);    
        } else if (type == Type.SUBTRACT) {
            return new BaseExpr(leftValue - rightValue);
        } else {
            throw new RuntimeException("Invalid StmtExpr type passed.");
        }
    }

    public String toString() {
        return String.format("StmtExpr(%s, %s, %s)", left.toString(), right.toString(), type.name());
    }
}
