package com.example.tinyscript.util;

import com.example.tinyscript.common.PeekIterator;
import com.example.tinyscript.lexer.Token;
import com.example.tinyscript.lexer.TokenType;

import java.util.stream.Stream;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class PeekTokenIterator extends PeekIterator<Token> {

    public PeekTokenIterator(Stream<Token> stream) {
        super(stream);
    }

    public Token nextMatch(String value) throws ParseException{
        Token token = this.next();
        if (!token.getValue().equals(value)){
            throw new ParseException(token);
        }
        return token;
    }

    public Token nextMatch(TokenType type) throws ParseException{
        Token token = this.next();
        if (!token.getType().equals(type)){
            throw new ParseException(token);
        }
        return token;
    }

}
