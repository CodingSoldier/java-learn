package com.example.tinyscript.lexer;

import com.example.tinyscript.common.PeekIterator;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chenpiqian
 * @date: 2020-09-09
 */
class TokenTest {

    void assertToken(Token token, String value, TokenType type){
        assertEquals(value, token.getValue());
        assertEquals(type, token.getType());
    }

    @Test
    public void test_varOrKeyword(){
        PeekIterator<Character> it1 = new PeekIterator<>("if abc".chars().mapToObj(x -> (char) x));
        PeekIterator<Character> it2 = new PeekIterator<>("true abc".chars().mapToObj(x -> (char) x));

        Token token1 = Token.makeVarOrKeyword(it1);
        Token token2 = Token.makeVarOrKeyword(it2);
        assertToken(token1, "if", TokenType.KEYWORD);
        assertToken(token2, "true", TokenType.BOOLEAN);

        it1.next();
        Token token3 = Token.makeVarOrKeyword(it1);
        assertToken(token3, "abc", TokenType.VARIABLE);
    }

    @Test
    public void test_makeString() throws LexicalException {
        String[] tests = {"\"123\"", "\'123\'"};
        for (String elem:tests){
            Stream<Character> charStream = elem.chars().mapToObj(x -> (char) x);
            PeekIterator<Character> it = new PeekIterator<>(charStream);
            Token token = Token.makeString(it);
            assertToken(token, elem, TokenType.STRING);
        }
    }

    @Test
    public void test_makeOp() throws LexicalException{
        String[] tests = {
                "+ xxx",
                "++mmm",
                "/=g",
                "==1",
                "&=3982",
                "&777",
                "||xxx",
                "^=111",
                "%7"
        };
        String[] results = {"+", "++", "/=", "==", "&=", "&", "||", "^=", "%"};
        int i = 0;
        for (String test:tests){
            PeekIterator<Character> it = new PeekIterator<>(test.chars().mapToObj(x -> (char) x));
            Token token = Token.makeOp(it);
            assertToken(token, results[i++], TokenType.OPERATOR);
        }
    }



}