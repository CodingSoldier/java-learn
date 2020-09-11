package com.example.tinyscript.parser.ast;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public abstract class Stmt extends ASTNode{

    public Stmt(ASTNodeTypes _type, String _label) {
        super(_type, _label);
    }
}
