package com.example.tinyscript.common;

import java.util.regex.Pattern;

/**
 * @author chenpiqian
 * @date: 2020-09-09
 */
public class AlphabetHelper {

    static Pattern ptnLetter = Pattern.compile("^[a-zA-Z]$");
    static Pattern ptnNumber = Pattern.compile("^[0-9]$");
    static Pattern ptnLiteral = Pattern.compile("^[_a-zA-Z0-9]$");
    static Pattern ptnOperator = Pattern.compile("^[+-\\\\*<><=!&|^%/]$");

    public static boolean isLetter(char c){
        return ptnLetter.matcher(String.valueOf(c)).matches();
    }

    public static boolean isNumber(char c){
        return ptnNumber.matcher(String.valueOf(c)).matches();
    }

    public static boolean isLiteral(char c){
        return ptnLiteral.matcher(String.valueOf(c)).matches();
    }

    public static boolean isOperator(char c){
        return ptnOperator.matcher(String.valueOf(c)).matches();
    }

}
