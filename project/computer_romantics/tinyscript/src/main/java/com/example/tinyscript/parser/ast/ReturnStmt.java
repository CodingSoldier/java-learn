package com.example.tinyscript.parser.ast;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class ReturnStmt extends Stmt {
    public ReturnStmt() {
        super(ASTNodeTypes.RETURN_STMT, "return");
    }
}
