package com;

public class Test01 {

}

class Solution {

    public static String addStrings(String num1, String num2) {
        int i = num1.length() - 1, j = num2.length() - 1, add = 0;
        StringBuffer ans = new StringBuffer();
        while (i >= 0 || j >= 0 || add != 0) {
            int x;
            if (i >= 0) {
                char num1CharI = num1.charAt(i);
                x = num1CharI - '0';
            } else {
                x = 0;
            }
            int y;
            if (j >= 0) {
                char num2CharJ = num2.charAt(j);
                y = num2CharJ - '0';
            } else {
                y = 0;
            }
            int result = x + y + add;
            int mode = result % 10;
            ans.append(mode);
            add = result / 10;
            i--;
            j--;
        }
        // 计算完以后的答案需要翻转过来
        ans.reverse();
        return ans.toString();
    }


    public static void main(String[] args) {
        String num1 = "123";
        String num2 = "789";
        addStrings(num1, num2);
    }

}
