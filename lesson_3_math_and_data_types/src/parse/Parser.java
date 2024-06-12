package parse;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;

import javax.swing.text.html.Option;

import expression.BaseExpr;
import expression.ErrorExpr;
import expression.Expr;
import expression.FuncExpr;
import expression.StmtExpr;
import expression.TermExpr;

public class Parser {
    private String text;

    private Stack<Cursor> cursors;
    
    public Parser(String text) {
        this.text = text;
        this.cursors = new Stack<Cursor>();
    }

    public Expr parse() {
        Optional<Expr> expr = parseStmtExprs();

        if (cursors.peek().position <= text.length() - 1) {
            return new ErrorExpr("Invalid expression. Could not parse.");
        }

        if (expr.isEmpty()) {
            return new ErrorExpr("Empty expression. Could not parse.");
        }

        System.out.println(expr.get().toString());

        return expr.get();
    }

    public boolean allCharsRead() {
        return cursors.peek().position > text.length();
    }

    public Optional<Character> nextChar() {
        if (cursors.peek().position > text.length() - 1) {
            cursors.peek().position++;
            return Optional.empty();
        } else {
            char c = text.charAt(cursors.peek().position);
            cursors.peek().position++;
            return Optional.of(c);
        }
    }

    public void createScopedCursor() {
        if (cursors.size() == 0) {
            cursors.push(new Cursor());
            return;
        }

        Cursor current = cursors.peek();
        Cursor newCursor = new Cursor();
        newCursor.position = current.position;
        newCursor.marker = current.position;
        cursors.push(newCursor);
    }

    public void backtrackCursor() {
        cursors.peek().position = cursors.peek().marker;
    }

    public void setMarker() {
        cursors.peek().marker = cursors.peek().position;
    }

    public Optional<Expr> parseBaseExpr() {
        Optional<Expr> parenExpr = parseParenExpr();
        if (!parenExpr.isEmpty()) {
            return parenExpr;
        }

        Optional<Expr> num = parseNumber();
        if (!num.isEmpty()) {
            setMarker();
            return num;
        }

        Optional<Expr> funcExpr = parseFuncExpr();
        if (!funcExpr.isEmpty()) {
            return funcExpr;
        }
        
        backtrackCursor();
        return Optional.empty();
    }

    public Optional<Expr> parseFuncExpr() {
        Optional<String> funcName = parseFuncName();
        if (funcName.isEmpty()) {
            backtrackCursor();
            return Optional.empty();
        }

        Optional<Character> c = nextChar();
        if (c.isEmpty()) {
            backtrackCursor();
            return Optional.empty();
        }

        if (c.get() != '(') {
            backtrackCursor();
            return Optional.empty();
        }

        ArrayList<Expr> funcParams = parseFuncParams();
        if (funcParams.isEmpty()) {
            cursors.pop();
            backtrackCursor();
            return Optional.empty();
        }

        c = nextChar();
        if (c.isEmpty()) {
            backtrackCursor();
            return Optional.empty();
        }

        if (c.get() != ')') {
            backtrackCursor();
            return Optional.empty();
        }
        setMarker();        

        return Optional.of(new FuncExpr(funcName.get(), funcParams));
    }

    public Optional<String> parseFuncName() {
        String buffer = "";
        Optional<Character> c;
        while (true) {
            c = nextChar();
            if (c.isEmpty()) {
                cursors.peek().position--;
                break;
            }

            if (!isAlpha(c.get())) {
                cursors.peek().position--;
                break;
            }

            buffer += c.get();
        }

        if (buffer.length() > 0) {
            return Optional.of(buffer);
        } else {
            return Optional.empty();
        }
    }

    public ArrayList<Expr> parseFuncParams() {
        ArrayList<Expr> params = new ArrayList<Expr>();

        Optional<Expr> lastParam = parseStmtExprs();
        if (lastParam.isEmpty()) {
            return params;
        }
        params.add(lastParam.get());

        while (!allCharsRead()) {
            Optional<Character> c = nextChar();
            if (c.get() != ',') {
                cursors.peek().position--;
                return params;
            }

            Optional<Expr> nextParam = parseStmtExprs();
            if (nextParam.equals(lastParam)) {
                return params;
            }
            lastParam = nextParam;
            params.add(nextParam.get());
        }

        return params;
    }

    public Optional<Expr> parseParenExpr() {
        Optional<Character> c = nextChar();
        if (c.isEmpty()) {
            backtrackCursor();
            return Optional.empty();
        }

        if (c.get() != '(') {
            backtrackCursor();
            return Optional.empty();
        }

        Optional<Expr> stmtExprs = parseStmtExprs();
        if (stmtExprs.isEmpty()) {
            cursors.pop();
            backtrackCursor();
            return Optional.empty();
        }

        c = nextChar();
        if (c.isEmpty()) {
            backtrackCursor();
            return Optional.empty();
        }

        if (c.get() != ')') {
            backtrackCursor();
            return Optional.empty();
        }
        setMarker();

        return stmtExprs;
    }

    public Optional<Expr> parseStmtExprs() {
        createScopedCursor();
        Optional<Expr> lastTerm = parseStmtExpr();
        if (lastTerm.isEmpty()) {
            return Optional.empty();
        }

        while (!allCharsRead()) {
            Optional<Expr> nextTerm = parseStmtExpr(lastTerm);
            if (nextTerm.equals(lastTerm)) {
                return lastTerm;
            }
            lastTerm = nextTerm;
        }

        return lastTerm;
    }

    public Optional<Expr> parseStmtExpr() {
        return parseStmtExpr(Optional.empty());
    }

    public Optional<Expr> parseStmtExpr(Optional<Expr> inputLeft) {
        Optional<Expr> left;
        if (!inputLeft.isEmpty()) {
            left = inputLeft;
        } else {
            left = parseTermExprs();
            if (left.isEmpty()) {
                backtrackCursor();
                return Optional.empty();
            }
            setMarker();
        }

        Optional<Character> c = nextChar();
        if (c.isEmpty()) {
            return left;
        }

        StmtExpr.Type type;
        if (c.get() == '+') {
            type = StmtExpr.Type.ADD;
        } else if (c.get() == '-') {
            type = StmtExpr.Type.SUBTRACT;
        } else {
            backtrackCursor();
            return left;
        }
        setMarker();

        Optional<Expr> right = parseTermExprs();
        if (right.isEmpty()) {
            backtrackCursor();
            return Optional.empty();
        }
        setMarker();

        return Optional.of(new StmtExpr(left.get(), right.get(), type));
    }

    public Optional<Expr> parseTermExprs() {
        Optional<Expr> lastTerm = parseTermExpr();
        if (lastTerm.isEmpty()) {
            return Optional.empty();
        }

        while (!allCharsRead()) {
            Optional<Expr> nextTerm = parseTermExpr(lastTerm);
            if (nextTerm.equals(lastTerm)) {
                return lastTerm;
            }
            lastTerm = nextTerm;
        }

        return lastTerm;
    }

    public Optional<Expr> parseTermExpr() {
        return parseTermExpr(Optional.empty());
    } 

    public Optional<Expr> parseTermExpr(Optional<Expr> inputLeft) {
        Optional<Expr> left;
        if (!inputLeft.isEmpty()) {
            left = inputLeft;
        } else {
            left = parseBaseExpr();
            if (left.isEmpty()) {
                return Optional.empty();
            }
        }

        Optional<Character> c = nextChar();
        if (c.isEmpty()) {
            return left;
        }

        TermExpr.Type type;
        if (c.get() == '*') {
            type = TermExpr.Type.MULTIPLY;
        } else if (c.get() == '/') {
            type = TermExpr.Type.DIVIDE;
        } else {
            backtrackCursor();
            return left;
        }
        setMarker();

        Optional<Expr> right = parseBaseExpr();
        if (right.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new TermExpr(left.get(), right.get(), type));
    }
    
    public Optional<Expr> parseNumber() {
        String buffer = "";
        Optional<Character> c;
        while (true) {
            c = nextChar();
            if (c.isEmpty()) {
                cursors.peek().position--;
                break;
            }

            if (!isDigit(c.get())) {
                cursors.peek().position--;
                break;
            }

            buffer += c.get();
        }

        if (buffer.length() > 0) {
            return Optional.of(new BaseExpr(Integer.parseInt(buffer)));
        } else {
            return Optional.empty();
        }
    }

    public static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    public static boolean isAlpha(char c) {
        return Character.isAlphabetic(c);
    }
}
