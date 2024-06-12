package expression;

import java.util.ArrayList;

import studentcode.StudentCode;

public class FuncExpr implements Expr {
    private String name;
    private ArrayList<Expr> params;

    public FuncExpr(String name, ArrayList<Expr> params) {
        assert params != null;
        this.name = name;
        this.params = params;
    }

    public int getValue() {
        throw new RuntimeException("FuncExpr is not a terminal expression.");
    }

    public boolean isTerminal() {
        return false;
    }

    public Expr evaluate() {
        ArrayList<Expr> finalValues = new ArrayList<Expr>();
        for (Expr param : this.params) {
            while(!(param instanceof BaseExpr)) {
                param = param.evaluate();
            }
            finalValues.add(param);
        }

        switch (name) {
            case "abs": return evaluateAbs(finalValues);
            case "sqrt": return evaluateSqrt(finalValues);
            case "pow": return evaluatePow(finalValues);
            case "sumTo": return evaluateSumTo(finalValues);
            case "gcf": return evaluateGreatestCommonFactor(finalValues);
            case "isPrime": return evaluateIsPrime(finalValues);
            case "hypot": return evaluateHypotenuse(finalValues);
            case "toBinary": return evaluateToBinary8Bits(finalValues);
            default:
                throw new RuntimeException(name + " is not a valid function.");
        }
    }

    public Expr evaluateAbs(ArrayList<Expr> baseParams) {
        if (baseParams.size() != 1) {
            throw new RuntimeException("abs() takes in 1 parameter. Got " + baseParams.size());
        }

        return new BaseExpr(StudentCode.abs(baseParams.get(0).getValue()));
    }
    
    public Expr evaluateSqrt(ArrayList<Expr> baseParams) {
        if (baseParams.size() != 1) {
            throw new RuntimeException("sqrt() takes in 1 parameter. Got " + baseParams.size());
        }

        if (baseParams.get(0).getValue() < 0) {
            throw new RuntimeException("sqrt() expects a non-negative input!");
        }

        int sqrtNum = StudentCode.sqrt(baseParams.get(0).getValue());
        return new BaseExpr((int) sqrtNum);
    }

    public Expr evaluatePow(ArrayList<Expr> baseParams) {
        if (baseParams.size() != 2) {
            throw new RuntimeException("pow() takes in 2 parameters. Got " + baseParams.size());
        }

        if (baseParams.get(1).getValue() < 0) {
            throw new RuntimeException("pow() expects a non-negative power to raise base to.");
        }

        return new BaseExpr((int) StudentCode.pow(baseParams.get(0).getValue(), baseParams.get(1).getValue()));
    }

    public Expr evaluateSumTo(ArrayList<Expr> baseParams) {
        if (baseParams.size() != 1) {
            throw new RuntimeException("sumTo() takes in 1 parameter. Got " + baseParams.size());
        }

        int n = (baseParams.get(0).getValue());

        if (n < 0) {
            throw new RuntimeException("sumTo() expects a non-negative number!");
        }

        int sumTo = StudentCode.sumTo(n);
        return new BaseExpr(sumTo);
    }

    public Expr evaluateGreatestCommonFactor(ArrayList<Expr> baseParams) {
        if (baseParams.size() != 2) {
            throw new RuntimeException("gcf() takes in 2 parameters. Got " + baseParams.size());
        }

        int a = baseParams.get(0).getValue();
        int b = baseParams.get(1).getValue();

        if (a < 0 || b < 0) {
            throw new RuntimeException("gcf() expects both numbers to be non-negative!");
        }

        return new BaseExpr(StudentCode.greatestCommonFactor(a, b));
    }

    public Expr evaluateIsPrime(ArrayList<Expr> baseParams) {
        if (baseParams.size() != 1) {
            throw new RuntimeException("isPrime() takes 1 parameter. Got " + baseParams.size());
        }

        int n = baseParams.get(0).getValue();

        if (n <= 1) {
            throw new RuntimeException("isPrime() expects an input > 1.");
        }

        return new BaseExpr(StudentCode.isPrime(n));
    }

    public Expr evaluateHypotenuse(ArrayList<Expr> baseParams) {
        if (baseParams.size() != 2) {   
            throw new RuntimeException("hypot() takes 2 parameters. Got " + baseParams.size());
        }

        int a = baseParams.get(0).getValue();
        int b = baseParams.get(1).getValue();

        if (a <= 0 || b <= 0) {
            throw new RuntimeException("hypot() expects both numbers to be positive!");
        }

        int hyp = StudentCode.hypotenuse(a, b);

        return new BaseExpr((int) hyp);
    }

    public Expr evaluateToBinary8Bits(ArrayList<Expr> baseParams) {
        if (baseParams.size() != 1) {
            throw new RuntimeException("toBinary() expects 1 input. Got " + baseParams.size());
        }

        int dec = baseParams.get(0).getValue();

        if (dec < 0) {
            throw new RuntimeException("toBinary() expects a non-negative number.");
        }

        
        int bin = StudentCode.binary8Bits(dec);
        return new BaseExpr(bin);
    }

    public String toString() {
        String paramsStr = "";
        for (final Expr param : this.params) {
            paramsStr += ", ";
            paramsStr += param.toString();
        }
        return String.format("FuncExpr(%s%s)", this.name, paramsStr);
    }
}

