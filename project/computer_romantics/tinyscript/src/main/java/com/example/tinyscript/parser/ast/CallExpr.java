package com.example.tinyscript.parser.ast;

import com.example.tinyscript.util.ParseException;
import com.example.tinyscript.util.PeekTokenIterator;

/**
 * @author chenpiqian
 * @date: 2020-09-11
 */
public class CallExpr extends Expr {

    public CallExpr() {
        super();
        this.label = "call";
        this.type = ASTNodeTypes.CALL_EXPR;
    }

    public static ASTNode parse(ASTNode factor, PeekTokenIterator it) throws ParseException{
        CallExpr expr = new CallExpr();
        expr.addChild(factor);
        it.nextMatch("(");
        ASTNode p = null;
        while ((p = Expr.parse(it)) != null){
            expr.addChild(p);
            if (!it.peek().getValue().equals(")")){
                it.nextMatch(",");
            }
        }
        it.nextMatch(")");
        return expr;
    }

}
