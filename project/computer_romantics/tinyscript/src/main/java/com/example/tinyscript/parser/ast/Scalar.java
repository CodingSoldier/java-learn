package com.example.tinyscript.parser.ast;

import com.example.tinyscript.lexer.Token;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class Scalar extends Factor{
    public Scalar(Token token) {
        super(token);
        this.type = ASTNodeTypes.SCALAR;
    }
}
