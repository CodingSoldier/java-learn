package com.example.tinyscript.parser.ast;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class Block extends Stmt {
    public Block() {
        super(ASTNodeTypes.BLOCK, "block");
    }
}
