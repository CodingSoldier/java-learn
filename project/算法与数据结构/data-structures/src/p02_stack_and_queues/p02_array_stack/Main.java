package p02_stack_and_queues.p02_array_stack;

import java.util.Stack;

/**
 * @author chenpq05
 * @since 2023/3/24 10:22
 */
public class Main {
    public static void main(String[] args) {

        ArrayStack<Integer> stack = new ArrayStack<>();

        for(int i = 0 ; i < 5 ; i ++){
            stack.push(i);
            System.out.println(stack);
        }

        stack.pop();
        System.out.println(stack);


        // 括号匹配
        // https://leetcode.cn/problems/valid-parentheses/submissions/
    }


    public boolean isValid(String s) {
        java.util.Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                Character topChar = stack.pop();
                if (c == ')' && topChar != '(') {
                    return false;
                } else if (c == ']' && topChar != '[') {
                    return false;
                } else if (c == '}' && topChar != '{') {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }
}
