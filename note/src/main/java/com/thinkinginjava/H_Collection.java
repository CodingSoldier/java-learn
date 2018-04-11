package com.thinkinginjava;

import org.junit.Test;

import java.util.*;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/3/31
 */

class Snow{}
class Powder extends Snow{}
class Light extends Powder{}
class Heavy extends Powder{}
class Crusty extends Snow{}
class Slush extends Snow{}

public class H_Collection {
    @Test
    public void t(){
        List<Snow> snows = Arrays.asList(new Light(), new Heavy());

        List<Integer> l2 = new ArrayList<Integer>();
        Collections.addAll(l2, 1,2,3);
        System.out.println(l2);
    }
}





class E05_IntegerListFeatures {
    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));

        ints.remove(1);  //优先根据index删除
        System.out.println(ints);

        ints.remove(Integer.valueOf(1)); //根据value删除
        System.out.println(ints);


        //List<int> is = new ArrayList<int>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        //报错，泛型不能是原始数据类型

    }
}





class IDClass {
    private static int counter;
    private int count = counter++;
    public String toString() {
        return "IDClass " + count;
    }
}
class E07_TestList {
    public static void main(String args[]) {
        IDClass[] idc = new IDClass[10];
        for (int i = 0; i < idc.length; i++)
            idc[i] = new IDClass();
        List<IDClass> lst = new ArrayList<IDClass>(Arrays.asList(idc));
        System.out.println("lst = " + lst);
        List<IDClass> subSet = lst.subList(lst.size() / 4, lst.size() / 2);
        System.out.println("subSet = " + subSet);
// The semantics of the sub list become undefined if the
// backing list is structurally modified!
//        lst.removeAll(subSet);
        subSet.clear();  // subList得到子集后，对子集的操作会影响到原始集合
        System.out.println("lst = " + lst);
    }
}



//230，泛型T的使用
class Stack<T> {
    private LinkedList<T> storage = new LinkedList<T>();
    public void push(T v) {
        storage.addFirst(v);  //把元素添加到开头
    }
    public T peek() {
        return storage.getFirst();
    }
    public T pop(){
        return storage.removeFirst();
    }
    public boolean empty() {
        return storage.isEmpty();
    }
    public String toString(){
        return storage.toString();
    }
}
class TT{
    public static void main(String args[]) {
        Stack<String> storage = new Stack<String>();
        storage.push("1");
        storage.push("2");
        storage.push("3");
        //storage.push(111); //new Stack的时候将泛型T指定为了String，所以push方法参数类型也被限制为String，不能push非string类型

    }
}





//P245
class Pet{
}
class Pets{
    public static Pet[] createArray(int i){
        return new Pet[i];
    }
}
class PetSequence {
    protected Pet[] pets = Pets.createArray(8);
}
class NonCollectionSequence extends PetSequence implements Iterable<Pet> {
    public Iterable<Pet> reversed() {
        return new Iterable<Pet>() {
            public Iterator<Pet> iterator() {
                return new Iterator<Pet>() {
                    int current = pets.length - 1;
                    public boolean hasNext() { return current > -1; }
                    public Pet next() { return pets[current--]; }
                    public void remove() { // Not implemented
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
    public Iterable<Pet> randomized() {
        return new Iterable<Pet>() {
            public Iterator<Pet> iterator() {
                List<Pet> shuffled =
                        new ArrayList<Pet>(Arrays.asList(pets));
                Collections.shuffle(shuffled, new Random(47));
                return shuffled.iterator();
            }
        };
    }
    public Iterator<Pet> iterator() {
        return new Iterator<Pet>() {
            private int index = 0;
            public boolean hasNext() {
                return index < pets.length;
            }
            public Pet next() { return pets[index++]; }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
class E32_MultiIterableNonCollectionSeq {
    public static void main(String[] args) {
        NonCollectionSequence nc = new NonCollectionSequence ();
        for(Pet pet : nc.reversed())
            System.out.print(pet + " ");
        System.out.println();
        for(Pet pet : nc.randomized())
            System.out.print(pet + " ");
        System.out.println();
        for(Pet pet : nc)
            System.out.print(pet + " ");
    }
}










//P271
class BaseballException extends Exception {}
class Foul extends BaseballException {}
class Strike extends BaseballException {}
class UmpireArgument extends BaseballException {}
abstract class Inning {
    Inning() throws BaseballException {}
    Inning(String s1, String s2) {}
    abstract void event() throws BaseballException;
    abstract void atBat() throws Strike, Foul, UmpireArgument;
    abstract void decision() throws UmpireArgument;
    void walk() {} // Throws nothing
}
class StormException extends Exception {}
class RainedOut extends StormException {}
class PopFoul extends Foul {}
interface Storm {
    public void event() throws RainedOut;
    public void rainHard() throws RainedOut;
}
class StormyInning extends Inning  implements Storm {
    //子类构造函数抛出父类构造函数抛出的异常
    StormyInning() throws RainedOut,  BaseballException {}
    StormyInning(String s) throws Foul,  BaseballException {}
    StormyInning(String s1, String s2) {
        super(s1, s2);
    }

    // 接口中rainHard抛出异常，实现类不可以抛出异常。反过来则不行，接口不抛异常，实现类抛异常，则编译失败
    public void rainHard() throws RainedOut {}

    //父类方法抛出异常，子类方法可以不抛异常。同样，反过来是不行的，父类方法不抛异常，子类方法抛异常，则编译失败
    public void event() {}

    void atBat() throws PopFoul, UmpireArgument {
        throw new UmpireArgument();
    }

    /**
     * 说明： 父类、接口不抛异常，子类、实现类不能抛异常
     *    调用 父类.方法 时可以不捕获异常，但是当换成 子类.方法 时就可能会抛出异常，必须catch 子类.方法
     *    若代码为 父类的引用指向子类的实体.方法 ，对象为父类类型，可以catch异常，但运行时实际上运行的是子类方法，是必须要catch的。这样的结果是程序设计上有BUG
     */

    void decision() throws UmpireArgument {
        throw new UmpireArgument();
    }
}
class E20_UmpireArgument {
    public static void main(String[] args) {

        /**
         * 说明： 父类抛异常，子类可以不抛异常
         *     调用 父类.方法 的时候必须catch，即使catch 子类.方法 也无所谓
         *     当明确对象i2是子类类型时，子类.方法 可以不被catch
         *     此设计没问题
         */
        Inning i1 = new StormyInning("s1", "s2");
        //i1.event();  //编译失败，必须catch
        StormyInning i2 = new StormyInning("s1", "s2");
        i2.event();

        try {
            StormyInning si = new StormyInning();
            si.atBat();
        } catch(PopFoul e) {
            System.out.println("Pop foul");
        } catch(RainedOut e) {
            System.out.println("Rained out");
        } catch(BaseballException e) {
            System.out.println("Generic error");
        }
// Strike not thrown in derived version.
        try {
            Inning i = new StormyInning();
            i.atBat();
        } catch(Strike e) {
            System.out.println("Strike");
        } catch(Foul e) {
            System.out.println("Foul");
        } catch(RainedOut e) {
            System.out.println("Rained out");
        } catch(BaseballException e) {
            System.out.println("Generic baseball exception");
        }

// Or you can add code to catch the
// specific type of exception:
        try {
            StormyInning si = new StormyInning();
            si.atBat();
            si.decision();
        } catch(PopFoul e) {
            System.out.println("Pop foul");
        } catch(RainedOut e) {
            System.out.println("Rained out");
        } catch(UmpireArgument e) {
            System.out.println(
                    "Argument with the umpire");
        } catch(BaseballException e) {
            System.out.println("Generic error");
        }
    }
}











//P275
class Except1 extends Exception {
    public Except1(String s) { super(s); }
}
class BaseWithException {
    public BaseWithException() throws Except1 {
        throw new Except1("thrown by BaseWithException");
    }
}
class DerivedWE extends BaseWithException {
    // Produces compile-time error:
// unreported exception Except1
// public DerivedWE() {}

//编译错误，super()必须是构造函数中的第一行语句
//    public DerivedWE() {
//        try {
//            super();
//        } catch(Except1 ex1) {
//        }
//    }
    public DerivedWE() throws Except1 {}
}
class E21_ConstructorExceptions {
    public static void main(String args[]) {
        try {
            new DerivedWE();
        } catch(Except1 ex1) {
            System.out.println("Caught " + ex1);
        }
    }
}






//P281
class Annoyance extends Exception{}
class Sneeze extends RuntimeException{}
class E30_Human {

    //把抛出的异常用RuntimeException包装起来，方法里面抛出RuntimeException，方法上不需要声明抛出异常
    static void throwRuntimeException(int type) {
        try {
            switch(type) {
                case 0: throw new Annoyance();
                case 1: throw new Sneeze();
                default: return;
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        //抛出RuntimeException的方法不需要try catch
        throwRuntimeException(2);

        for(int i = 0; i < 2; i++)
        try {
            throwRuntimeException(i);
        } catch(RuntimeException re) {
            try {
                //通过getCause()方法获取被RuntimeException包装起来的异常
                throw re.getCause();
            } catch(Sneeze e) {
                System.out.println("Caught Sneeze");
            } catch(Annoyance e) {
                System.out.println("Caught Annoyance");
            } catch(Throwable t) {
                System.out.println(t);
            }
        }
    }
}









