package com.example.tinyscript.parser.ast;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class ForStmt extends  Stmt {
    public ForStmt() {
        super(ASTNodeTypes.FOR_STMT, "for");
    }
}
