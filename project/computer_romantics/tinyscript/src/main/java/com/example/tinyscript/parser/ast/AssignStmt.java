package com.example.tinyscript.parser.ast;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class AssignStmt extends Stmt {
    public AssignStmt() {
        super(ASTNodeTypes.ASSIGN_STMT, "assign");
    }
}
