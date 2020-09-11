package com.example.tinyscript.parser.ast;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public enum ASTNodeTypes {
    BLOCK,
    BINARY_EXPR, // 1+1
    UNARY_EXPR, // ++i
    CALL_EXPR,
    VARIABLE,
    SCALAR, // 1.0, true
    IF_STMT,
    WHILE_STMT,
    FOR_STMT,
    RETURN_STMT,
    ASSIGN_STMT,
    FUNCTION_DECLARE_STMT,
    DECLARE_STMT
}
