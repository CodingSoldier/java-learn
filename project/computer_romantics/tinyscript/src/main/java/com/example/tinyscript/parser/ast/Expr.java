package com.example.tinyscript.parser.ast;

import com.example.tinyscript.lexer.Token;
import com.example.tinyscript.util.ExprHOF;
import com.example.tinyscript.util.ParseException;
import com.example.tinyscript.util.PeekTokenIterator;
import com.example.tinyscript.util.PriorityTable;

/**
 * @author chenpiqian
 * @date: 2020-09-11
 */
public class Expr extends ASTNode {

    private static PriorityTable table = new PriorityTable();


    public Expr() {
        super();
    }

    public Expr(ASTNodeTypes type, Token lexeme) {
        super();
        this.type = type;
        this.label = lexeme.getValue();
        this.lexeme = lexeme;
    }

    // left:E(k) -> E(k) op(k) E(k+1) | E(k+1)
    // right:
    //    E(k) -> E(k+1) E_(k)
    //       var e = new Expr(); e.left = E(k+1); e.op = op(k); e.right = E(k+1) E_(k)
    //    E_(k) -> op(k) E(k+1) E_(k) | ε
    // 最高优先级处理:
    //    E(t) -> F E_(k) | U E_(k)
    //    E_(t) -> op(t) E(t) E_(t) | ε

    private static ASTNode E(int k, PeekTokenIterator it) throws ParseException {
        if(k < table.size() - 1) {
            return combine( it, () -> E( k+1, it), () -> E_(k, it));
        } else {
            return race(
                    it,
                    () -> combine( it, () -> F(it), () -> E_( k, it)),
                    () -> combine( it, () -> U(it), () -> E_( k, it))
            );
        }
    }

    private static ASTNode E_(int k, PeekTokenIterator it) throws ParseException {
        Token token = it.peek();
        String value = token.getValue();

        if(table.get(k).indexOf(value) != -1) {
            Expr expr = new Expr(ASTNodeTypes.BINARY_EXPR, it.nextMatch(value));
            expr.addChild(combine(it,
                    () -> E(k+1, it),
                    () -> E_(k, it)
            ));
            return expr;

        }
        return null;
    }

    private static ASTNode U(PeekTokenIterator it) throws ParseException {
        Token token = it.peek();
        String value = token.getValue();

        if(value.equals("(")) {
            it.nextMatch("(");
            ASTNode expr = E(0, it);
            it.nextMatch(")");
            return expr;
        }
        else if (value.equals("++") || value.equals("--") || value.equals("!")) {
            Token t = it.peek();
            it.nextMatch(value);
            Expr unaryExpr = new Expr(ASTNodeTypes.UNARY_EXPR, t);
            unaryExpr.addChild(E(0, it));
            return unaryExpr;
        }
        return null;
    }


    private static ASTNode F(PeekTokenIterator it) throws ParseException {
        ASTNode factor = Factor.parse(it);
        if(factor == null) {
            return null;
        }
        if(it.hasNext() && it.peek().getValue().equals("(")) {
            return CallExpr.parse(factor,it);
        }
        return factor;
    }

    private static ASTNode combine(PeekTokenIterator it, ExprHOF aFunc, ExprHOF bFunc) throws ParseException {
        ASTNode a = aFunc.hoc();
        if(a == null) {
            return it.hasNext() ? bFunc.hoc() : null;
        }
        ASTNode b = it.hasNext() ? bFunc.hoc() : null;
        if(b == null) {
            return a;
        }

        Expr expr = new Expr(ASTNodeTypes.BINARY_EXPR, b.lexeme);
        expr.addChild(a);
        expr.addChild(b.getChild(0));
        return expr;

    }

    private static ASTNode race(PeekTokenIterator it, ExprHOF aFunc, ExprHOF bFunc) throws ParseException {
        if(!it.hasNext()) {
            return null;
        }
        ASTNode a = aFunc.hoc();
        if(a != null) {
            return a;
        }
        return bFunc.hoc();
    }


    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        return E(0, it);
    }

}
