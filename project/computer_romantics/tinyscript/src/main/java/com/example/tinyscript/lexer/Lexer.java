package com.example.tinyscript.lexer;

import com.example.tinyscript.common.AlphabetHelper;
import com.example.tinyscript.common.PeekIterator;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public class Lexer {

    public ArrayList<Token> analyse(PeekIterator<Character> it) throws LexicalException{
        ArrayList<Token> tokens = new ArrayList();
        while (it.hasNext()){
            char c = it.next();
            if (c == 0){
                break;
            }
            char lookahead = it.peek();
            if (c == ' ' || c == '\n'){
                continue;
            }
            // 删除注释
            if (c == '/'){
                if (lookahead == '/'){
                    while (it.hasNext() && (c = it.next()) != '\n'){};
                    continue;
                }else if (lookahead == '*'){
                    it.next(); //多读一个*避免/*/通过
                    boolean valid = false;
                    while (it.hasNext()){
                        char p = it.next();
                        if (p == '*' && it.peek() == '/'){
                            it.next();
                            valid = true;
                            break;
                        }
                    }
                    if (!valid){
                        throw new LexicalException("comments not match");
                    }
                    continue;
                }
            }

            if (c == '{' || c=='}' || c=='(' || c==')'){
                tokens.add(new Token(TokenType.BRACKET, c+""));
                continue;
            }

            if (c=='"' || c=='\''){
                it.putBack();
                tokens.add(Token.makeString(it));
                continue;
            }

            if (AlphabetHelper.isLetter(c)){
                it.putBack();
                tokens.add(Token.makeVarOrKeyword(it));
                continue;
            }

            if(AlphabetHelper.isNumber(c)) {
                it.putBack();
                tokens.add(Token.makeNumber(it));
                continue;
            }

            if ((c == '+' || c == '-' || c == '.') && AlphabetHelper.isNumber(lookahead)){
                Token lastToken = tokens.size() == 0 ? null : tokens.get(tokens.size() -1);
                if (lastToken == null || !lastToken.isValue() || lastToken.isOperator()){
                    it.putBack();
                    tokens.add(Token.makeNumber(it));
                    continue;
                }
            }

            if (AlphabetHelper.isOperator(c)){
                it.putBack();
                tokens.add(Token.makeOp(it));
                continue;
            }
            throw new LexicalException(c);
        }
        return tokens;
    }

    public ArrayList<Token> analyse(Stream source) throws LexicalException{
        PeekIterator<Character> it = new PeekIterator<Character>(source, (char) 0);
        return this.analyse(it);
    }




}
