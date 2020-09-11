package com.example.tinyscript.parser.ast;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class FunctionDeclareStmt extends Stmt {
    public FunctionDeclareStmt() {
        super( ASTNodeTypes.FUNCTION_DECLARE_STMT, "func");
    }

}
