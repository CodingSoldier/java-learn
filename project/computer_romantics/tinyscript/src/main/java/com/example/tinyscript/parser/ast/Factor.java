package com.example.tinyscript.parser.ast;

import com.example.tinyscript.lexer.Token;
import com.example.tinyscript.lexer.TokenType;
import com.example.tinyscript.util.PeekTokenIterator;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class Factor extends ASTNode {

    public Factor(Token token) {
        super();
        this.lexeme = token;
        this.label = token.getValue();
    }

    public static ASTNode parse(PeekTokenIterator it){
        Token token = it.peek();
        TokenType type = token.getType();
        if (type == TokenType.VARIABLE){
            it.next();
            return new Variable(token);
        }else if (token.isScalar()){
            it.next();
            return new Scalar(token);
        }
        return null;
    }
}
