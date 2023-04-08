package p07_set_map.p01_set;

import java.util.ArrayList;
import p07_set_map.p01_set.BSTSet;
import p07_set_map.p01_set.FileOperation;

public class Main {

    public static void main(String[] args) {

        System.out.println("Pride and Prejudice");

        ArrayList<String> words1 = new ArrayList<>();
        if(FileOperation.readFile("D:\\mycode\\java-learn\\project\\算法与数据结构\\data-structures\\src\\pride-and-prejudice.txt", words1)) {
            System.out.println("Total words: " + words1.size());

            BSTSet<String> set1 = new BSTSet<>();
            for (String word : words1)
                set1.add(word);
            System.out.println("Total different words: " + set1.getSize());
        }

        System.out.println();


        System.out.println("A Tale of Two Cities");

        ArrayList<String> words2 = new ArrayList<>();
        if(FileOperation.readFile("D:\\mycode\\java-learn\\project\\算法与数据结构\\data-structures\\src\\a-tale-of-two-cities.txt", words2)){
            System.out.println("Total words: " + words2.size());

            BSTSet<String> set2 = new BSTSet<>();
            for(String word: words2)
                set2.add(word);
            System.out.println("Total different words: " + set2.getSize());
        }
    }
}
