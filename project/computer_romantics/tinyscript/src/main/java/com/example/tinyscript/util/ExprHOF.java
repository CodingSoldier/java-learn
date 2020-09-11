package com.example.tinyscript.util;

import com.example.tinyscript.parser.ast.ASTNode;



/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
@FunctionalInterface
public interface ExprHOF {
    ASTNode hoc() throws ParseException;
}
