package com.example.tinyscript.parser.ast;

import com.example.tinyscript.lexer.Token;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author chenpiqian
 * @date: 2020-09-10
 */
public abstract class ASTNode {

    /* 树 */
    protected ArrayList<ASTNode> children = new ArrayList<>();
    protected ASTNode parent;

    /* 关键信息 */
    protected Token lexeme; // 词法单元
    protected String label; // 备注(标签)
    protected ASTNodeTypes type; // 类型


    private HashMap<String, Object> _props = new HashMap<>();

    public ASTNode() {
    }

    public ASTNode(ASTNodeTypes _type, String _label) {
        this.type = _type;
        this.label = _label;
    }

    public ASTNode getChild(int index) {
        if(index >= this.children.size()) {
            return null;
        }
        return this.children.get(index);
    }

    public void addChild(ASTNode node) {
        node.parent = this;
        children.add(node);
    }

    public Token getLexeme(){
        return lexeme;
    }

    public List<ASTNode> getChildren(){
        return children;
    }


    public void setLexeme(Token token) {
        this.lexeme = token;
    }

    public void setLabel(String s){
        this.label = s;
    }

    public ASTNodeTypes getType(){
        return this.type;
    }

    public void setType(ASTNodeTypes type) {
        this.type = type;
    }

    public void print(int indent) {
        if(indent == 0) {
            System.out.println("print:" + this);
        }

        System.out.println(StringUtils.leftPad(" ", indent *2) + label);
        for(ASTNode child : children) {
            child.print(indent + 1);
        }
    }



    public String getLabel() {
        return this.label;
    }

    public void replaceChild(int i, ASTNode node) {
        this.children.set(i, node);
    }

    public HashMap<String, Object> props() {
        return this._props;
    }

    public Object getProp(String key) {
        if(!this._props.containsKey(key)) {
            return null;
        }
        return this._props.get(key);
    }

    public void setProp(String key, Object value) {
        this._props.put(key, value);
    }


    public boolean isValueType() {
        return this.type == ASTNodeTypes.VARIABLE || this.type == ASTNodeTypes.SCALAR;
    }

    public void replace(ASTNode node) {
        if(this.parent != null) {
            int idx = this.parent.children.indexOf(this);
            this.parent.children.set(idx, node);
            //this.parent = null;
            //this.children = null;
        }
    }

}









