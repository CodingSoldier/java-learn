package com.example.tinyscript.parser.ast;

import com.example.tinyscript.lexer.Token;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class Variable extends Factor {
    private Token typeLexeme = null;
    public Variable(Token token) {
        super(token);
        this.type = ASTNodeTypes.VARIABLE;
    }
    public void setTypeLexeme(Token token) {
        this.typeLexeme = token;
    }
    public Token getTypeLexeme(){
        return this.typeLexeme;
    }
}
