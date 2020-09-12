package com.cpq.gradle02java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(App.class);
        int i=0;
        Scanner scanner = new Scanner(System.in);
        while (++i > 0){
            logger.info(i + ". 请输入: ");
            TodoItem todoItem = new TodoItem(scanner.nextLine());
            System.out.println(todoItem);
        }
    }
}
