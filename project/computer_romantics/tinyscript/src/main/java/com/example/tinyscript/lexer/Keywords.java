package com.example.tinyscript.lexer;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author chenpiqian
 * @date: 2020-09-09
 */
public class Keywords {

    static String[] keywords = {
            "var",
            "int",
            "float",
            "bool",
            "void",
            "string",
            "if",
            "else",
            "for",
            "while",
            "break",
            "func",
            "return"
    };

    static HashSet<String> set = new HashSet<>(Arrays.asList(keywords));

    public static boolean isKeyword(String word){
        return set.contains(word);
    }

}
