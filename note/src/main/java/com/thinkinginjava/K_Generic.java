package com.thinkinginjava;

import typeinfo.pets.Mouse;
import typeinfo.pets.Pet;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.*;

import static com.thinkinginjava.Insect.print;

public class K_Generic {
}



// P354  Holder3持有Pet，那么Holder3也可以持有Pet的导出类
class Holder3<T>{
    private T a;
    public Holder3(T a){this.a = a;}

    public T get() {
        return a;
    }

    public void set(T a) {
        this.a = a;
    }
}
class E01_PetsHolder {
    public static void main(String[] args) {
        Holder3<Pet> h3 = new Holder3<Pet>(new Mouse("Mickey"));
        System.out.println(h3.get());
    }
}




//P357 实现一个堆栈存储类
class LinkedStack<T> {
    private class Node {
        T item;
        Node next;
        Node() {
            item = null;
            next = null;
        }
        Node(T item, Node next) {
            this.item = item;
            this.next = next;
        }
        boolean end() {
            return item == null && next == null;
        }
    }
    private Node top = new Node(); // End sentinel
    public void push(T item) {
        top = new Node(item, top);
    }
    public T pop() {
        T result = top.item;
        if(!top.end())
            top = top.next;
        return result;
    }

    public static void main(String[] args){
        LinkedStack<String> lss = new LinkedStack<String>();
        for (String s : new String[]{"str000", "str111", "str222"}){
            lss.push(s);
        }

        String s;
        while ((s = lss.pop()) != null){
            System.out.println(s);
        }
    }
}




// P362
class E09_GenericMethods2 {
    public <A,B,C> void f(A a, B b, C c) {
        System.out.println(a.getClass().getName());
        System.out.println(b.getClass().getName());
        System.out.println(c.getClass().getName());
    }
    public static void main(String[] args) {
        E09_GenericMethods2 gm = new E09_GenericMethods2();
        gm.f("", 1, 1.0);
        gm.f(1.0F, 'c', gm);
    }
}








// P362
class New {
    public static <K,V> Map<K,V> map() {
        return new HashMap<K,V>();
    }
    public static <T> List<T> list() {
        return new ArrayList<T>();
    }
    public static <T> LinkedList<T> lList() {
        return new LinkedList<T>();
    }
    public static <T> Set<T> set() {
        return new HashSet<T>();
    }
    public static <T> Queue<T> queue() {
        return new LinkedList<T>();
    }
    // Examples:
    public static void main(String[] args) {
        Map<String, List<String>> sls = New.map();
        List<String> ls = New.list();
        LinkedList<String> lls = New.lList();
        Set<String> ss = New.set();
        Queue<String> qs = New.queue();
    }
}
class SixTuple<A,B,C,D,E,F>{
    public SixTuple(A a, B b, C c, D d, E e, F f) {}
}
class Sequence<A>{
    public Sequence(int a) {}
}
class E11_NewTest {
    public static void main(String[] args) {
        List<SixTuple<Byte,Short,String,Float,Double,Integer>>
                list = New.list();
        list.add(
                new SixTuple<Byte,Short,String,Float,Double,Integer>(
                        (byte)1, (short)1, "A", 1.0F, 1.0, 1));
        System.out.println(list);

        Set<Sequence<String>> set = New.set();
        set.add(new Sequence<String>(5));
        System.out.println(set);
    }
}



/*
//静态泛型方法前面要加上<T>，下面的类编译不通过
class NewT<T> {
    public static List<T> list() {
        return new ArrayList<T>();
    }
}
*/



//  P364
class E13_OverloadedFill {
    public static <T> List<T>
    fill(List<T> list) {
        System.out.println("list");
        return list;
    }
    public static <T> Queue<T>
    fill(Queue<T> queue) {
        System.out.println("queue");
        return queue;
    }
    public static <T> LinkedList<T>
    fill(LinkedList<T> llist) {
        System.out.println("LinkedList");
        return llist;
    }
    public static <T> Set<T>
    fill(Set<T> set) {
        System.out.println("Set");
        return set;
    }
    public static void main(String[] args) {

        //调用 public static <T> List<T>
        List<String> coffeeList = fill( new ArrayList<String>() );
        fill(new ArrayList<String>());

        //调用 public static <T> Queue<T>
        Queue<Integer> iQueue = fill( (Queue<Integer>)new LinkedList<Integer>() );
        fill( (Queue<Integer>)new LinkedList<Integer>() );

        //调用 public static <T> LinkedList<T>
        LinkedList<Character> cLList = fill(  new LinkedList<Character>() );
        fill(  new LinkedList<Character>() );

        //调用 public static <T> Set<T>
        Set<Boolean> bSet = fill( new HashSet<Boolean>() );
        fill(  new HashSet<Boolean>() );

    }
}



class Coffee {
    private static long counter = 0;
    private final long id = counter++;
    public String toString() {
        return getClass().getSimpleName() + " " + id;
    }
}
class Latte extends Coffee {}
class Mocha extends Coffee {}
class Cappuccino extends Coffee {}
class Americano extends Coffee {}
class Breve extends Coffee {}

interface Generator<T> { T next(); }
class Fibonacci implements Generator<Integer> {
    private int count = 0;
    public Integer next() { return fib(count++); }
    private int fib(int n) {
        if(n < 2) return 1;
        return fib(n-2) + fib(n-1);
    }
    public static void main(String[] args) {
        Fibonacci gen = new Fibonacci();
        for(int i = 0; i < 18; i++)
            System.out.print(gen.next() + " ");
    }
}
class CoffeeGenerator
        implements Generator<Coffee>, Iterable<Coffee> {
    private Class[] types = { Latte.class, Mocha.class,
            Cappuccino.class, Americano.class, Breve.class, };
    private static Random rand = new Random(47);
    public CoffeeGenerator() {}
    // For iteration:
    private int size = 0;
    public CoffeeGenerator(int sz) { size = sz; }
    public Coffee next() {
        try {
            return (Coffee)
                    types[rand.nextInt(types.length)].newInstance();
            // Report programmer errors at run time:
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    class CoffeeIterator implements Iterator<Coffee> {
        int count = size;
        public boolean hasNext() { return count > 0; }
        public Coffee next() {
            count--;
            return CoffeeGenerator.this.next();
        }
        public void remove() { // Not implemented
            throw new UnsupportedOperationException();
        }
    };
    public Iterator<Coffee> iterator() {
        return new CoffeeIterator();
    }
    public static void main(String[] args) {
        CoffeeGenerator gen = new CoffeeGenerator();
        for(int i = 0; i < 5; i++)
            System.out.println(gen.next());
        for(Coffee c : new CoffeeGenerator(5))
            System.out.println(c);
    }
}
class Generators {
    public static <T> Collection<T>
    fill(Collection<T> coll, Generator<T> gen, int n) {
        for(int i = 0; i < n; i++)
            coll.add(gen.next());
        return coll;
    }
    public static void main(String[] args) {
        Collection<Coffee> coffee = fill(
                new ArrayList<Coffee>(), new CoffeeGenerator(), 4);
        for(Coffee c : coffee)
            System.out.println(c);
        Collection<Integer> fnumbers = fill(
                new ArrayList<Integer>(), new Fibonacci(), 12);
        for(int i : fnumbers)
            System.out.print(i + ", ");
    }
}






// P375
interface Top {
    void a();
    void b();
}
class CombinedImpl implements Top {
    public void a() { System.out.println("Top::a()"); }
    public void b() { System.out.println("Top::b()"); }
    public void c() {
        System.out.println("CombinedImpl::c()");
    }
}
class E20_Bounds {
    static <T extends Top> T f(T obj) {
        obj.a();
        obj.b();
// c() is not part of an interface
// obj.c();
        return obj;
    }
    public static void main(String[] args) {
        System.out.println( f(new CombinedImpl()).getClass().getTypeParameters().toString() );
    }
}







// P380
class AM<T>{
    private Class<T> k;
    public void f(Object arg){
        //if(T instanceof Object){ }   //报错，T没有任何类型信息
    }
    public static void main(String[] args) {
        System.out.println( new ArrayList<>() instanceof Object );
    }
}









// P383
class ClassAsFactory<T> {
    Class<T> kind;
    public ClassAsFactory(Class<T> kind) { this.kind = kind; }
    public T create(int arg) {
        try {
// Technique 1 (verbose)
            for(Constructor<?> ctor : kind.getConstructors()) {
// Look for a constructor with a single parameter:
                Class<?>[] params = ctor.getParameterTypes();
                if(params.length == 1)
                    if(params[0] == int.class)
                        return kind.cast(ctor.newInstance(arg));
            }
// Technique 2 (direct)
// Constructor<T> ct = kind.getConstructor(int.class);
// return ct.newInstance(arg);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
class Employee{}
class E22_InstantiateGenericType2 {
    public static void main(String[] args) {
        ClassAsFactory<Employee> fe =  new ClassAsFactory<Employee>(Employee.class);
        Employee emp = fe.create(1);
        if(emp == null)
            print("Employee cannot be instantiated!");
        ClassAsFactory<Integer> fi =  new ClassAsFactory<Integer>(Integer.class);
        Integer i = fi.create(0);
        if(i == null)
            print("Integer cannot be instantiated!");
    }
}
















// P385
class E22_Instant {
    public static void main(String[] args) {
    //    Object[] oa = new Object[1];
    //    oa[0] = new Integer(11);
    //    //报错，数组是底层数据结构，创建时就已经去确定Object类型数组，不能根据元素类型向下转型为Integer[]
    //    System.out.println( (Integer[])oa );

        Integer[] oi = new Integer[1];
        oi[0] = new Integer(11);
        //数组能向上转型为Object[]
        System.out.println( (Object[])oi );

    }
}







// P385
class GenericArray2<T> {
    private Object[] array;
    public GenericArray2(int sz) {
        array = new Object[sz];
    }
    public void put(int index, T item) {
        array[index] = item;
    }
    @SuppressWarnings("unchecked")
    public T get(int index) { return (T)array[index]; }
    @SuppressWarnings("unchecked")
    public T[] rep() {
        return (T[])array; // Warning: unchecked cast
    }
    public static void main(String[] args) {
        GenericArray2<Integer> gai =
                new GenericArray2<Integer>(10);
        for(int i = 0; i < 10; i ++)
            gai.put(i, i);
        for(int i = 0; i < 10; i ++)
            System.out.print(gai.get(i) + " ");

        try {
            Integer[] ia = gai.rep();
            //GenericArray2已经定义了数组类型是Object，不能转换为Integer[]
        } catch(Exception e) {
            System.out.println("异常："+e);
        }

    }
}
// P385
class GenericArrayWithT<T>{
    private T[] array;
    public GenericArrayWithT(int sz){
        array = (T[]) new Object[sz];
        //array = new T[sz];  //报错，数组不能 new T[sz]
    }
    public void put(int index, T item){
        array[index] = item;
    }
    public T get(int index){
        return array[index];
    }
    public T[] rep(){
        return array;
    }

    public static void main(String[] args) {
        GenericArrayWithT<Integer> gai = new GenericArrayWithT<Integer>( 10);
        Integer[] ia = gai.rep();
    }
}
// P385，通过Array.newInstance(Class<T> type, int sz)在创建数组时指定数组类型为type
class GenericArrayWithTypeToken<T>{
    private T[] array;
    public GenericArrayWithTypeToken(Class<T> type, int sz){
        array = (T[]) Array.newInstance(type, sz);
    }
    public void put(int index, T item){
        array[index] = item;
    }
    public T get(int index){
        return array[index];
    }
    public T[] rep(){
        return array;
    }

    public static void main(String[] args) {
        GenericArrayWithTypeToken<Integer> gai = new GenericArrayWithTypeToken<Integer>(Integer.class, 10);
        Integer[] ia = gai.rep();
    }
}







// P390
class CovariantArrays {
    public static void main(String[] args) {
        //Fruit[]的引用可以指向Apple[]数组实体，但是运行时仍然时Apple[]，添加Orange、Fruit等实体回报错
        Fruit[] fruit = new Apple[10];
        fruit[0] = new Apple(); // OK
        fruit[1] = new Jonathan(); // OK
        // Runtime type is Apple[], not Fruit[] or Orange[]:
        try {
            // Compiler allows you to add Fruit:
            fruit[0] = new Fruit(); // ArrayStoreException
        } catch(Exception e) {
            System.out.println("异常："+e);
        }
        try {
            // Compiler allows you to add Oranges:
            fruit[0] = new Orange(); // ArrayStoreException
        } catch(Exception e) { System.out.println("异常："+e); }
    }
}




//查看知乎 //https://www.zhihu.com/question/20400700
// P390-391
class FruitParent{}
class Fruit{}
class Apple extends Fruit{}
class AppleSub extends Apple{}
class Jonathan extends Apple{}
class Orange extends Fruit{}
class T11{
    public static void main(String[] args) {
        //编译报错  P390-391
        //List<Fruit> listFruit = new ArrayList<Apple>();

        List<? extends Fruit> flist = new ArrayList<Apple>();
        //都报错，只能添加null  P391
        //flist.add(new Apple());
        //flist.add(new Fruit());
        //flist.add(new Object());
        flist.add(null);

        //Apple a = flist.get(0);
        Fruit f = flist.get(0);
        Object o = flist.get(0);

        //超类通配符 super ，集合元素下界是Apple，
        List<? super Apple> apples = new ArrayList<Apple>();
        apples.add(new AppleSub()); //正确，任何List<? super Apple>的引用可以指向new AppleSub()
        apples.add(new Apple()); //正确，任何List<? super Apple>的引用可以指向new Apple()
        //apples.add(new Fruit());
        //错误，FruitParent也是Apple的超类，满足List<? super Apple>，但是不能指向new Fruit()

        Object a = apples.get(0);
        //正确，Object 可以指向 List<? super Apple> 任何实体
        //Fruit af = apples.get(0);
        //错误，Fruit 不能指向 List<? super Apple> 中的FruitBrother类型
        //Apple aa = apples.get(0);
        //错误
    }
}






// 数组拥有协变性 P391
class E26_CovariantArrays2 {
    public static void main(String[] args) {
        Number[] nums = new Integer[10];
        nums[0] = Integer.valueOf(1); // OK
// Runtime type is Integer[], not Float[] or Byte[]:
        try {
// Compiler allows you to add Float:
            nums[1] = new Float(1.0); // ArrayStoreException
        } catch(Exception e) { System.out.println(e); }
        try {
// Compiler allows you to add Byte:
            nums[2] = Byte.valueOf((byte)1);
// Above line produces an ArrayStoreException
        } catch(Exception e) { System.out.println(e); }
    }
}

//  List却没有协变性
class E27_GenericsAndCovariance2 {
    public static void main(String[] args) {
// Compile Error: incompatible types:
// List<Number> nlist = new ArrayList<Integer>();
// Wildcards allow covariance:
        List<? extends Number> nlist = new ArrayList<Integer>();
// Compile Error: can't add any type of object:
// nlist.add(new Integer(1));
// nlist.add(new Float(1.0));
// nlist.add(new Object());
        nlist.add(null); // Legal but uninteresting
// We know that it returns at least Number:
        Number n = nlist.get(0);
    }
}















// P395
class T111{
    public static void main(String[] args) {
        List l = new ArrayList<String>();
        l.add("1");
        //String e = l.get(0);
        Object e = l.get(0);

    }

    public static void test(Collection<TT1> t){
        System.out.println(t);
    }

}







// P397
class Holder<T> {
    private T value;
    public Holder() {}
    public Holder(T val) { value = val; }
    public void set(T val) { value = val; }
    public T get() { return value; }
    public boolean equals(Object obj) {
        return value.equals(obj);
    }

}
class Wildcards {
    // Raw argument:
    static void rawArgs(Holder holder, Object arg) {
         holder.set(arg); // Warning:
        //   Unchecked call to set(T) as a
        //   member of the raw type Holder
         holder.set(new Wildcards()); // Same warning

        // Can't do this; don't have any 'T':
        // T t = holder.get();

        // OK, but type information has been lost:
        Object obj = holder.get();
    }
    // Similar to rawArgs(), but errors instead of warnings:
    static void unboundedArg(Holder<?> holder, Object arg) {
         //holder.set(arg); // Error:
        //   set(capture of ?) in Holder<capture of ?>
        //   cannot be applied to (Object)
        // holder.set(new Wildcards()); // Same error

        // Can't do this; don't have any 'T':
        // T t = holder.get();

        // OK, but type information has been lost:
        Object obj = holder.get();
    }
    static <T> T exact1(Holder<T> holder) {
        T t = holder.get();
        return t;
    }
    static <T> T exact2(Holder<T> holder, T arg) {
        holder.set(arg);
        T t = holder.get();
        return t;
    }
    static <T> T wildSubtype(Holder<? extends T> holder, T arg) {
        // holder.set(arg); // Error:
        //   set(capture of ? extends T) in
        //   Holder<capture of ? extends T>
        //   cannot be applied to (T)
        T t = holder.get();
        return t;
    }
    static <T> void wildSupertype(Holder<? super T> holder, T arg) {
        holder.set(arg);
        // T t = holder.get();  // Error:
        //   Incompatible types: found Object, required T

        // OK, but type information has been lost:
        Object obj = holder.get();
    }


    public static void main(String[] args) {
        Holder raw = new Holder<Long>();
        // Or:
        raw = new Holder();
        Holder<Long> qualified = new Holder<Long>();
        Holder<?> unbounded = new Holder<Long>();
        Holder<? extends Long> bounded = new Holder<Long>();
        Long lng = 1L;

        //rawArgs(raw, lng);
        //rawArgs(qualified, lng);
        //rawArgs(unbounded, lng);
        //rawArgs(bounded, lng);

        //unboundedArg(raw, lng);
        //unboundedArg(qualified, lng);
        //unboundedArg(unbounded, lng);
        //unboundedArg(bounded, lng);

        // Object r1 = exact1(raw); // Warnings:
        //   //Unchecked conversion from Holder to Holder<T>
        //   //Unchecked method invocation: exact1(Holder<T>)
        //   //is applied to (Holder)
        //Long r2 = exact1(qualified);
        //Object r3 = exact1(unbounded); // Must return Object
        //Long r4 = exact1(bounded);

        //Long r5 = exact2(raw, lng); // Warnings:
        ////Unchecked conversion from Holder to Holder<Long>
        ////Unchecked method invocation: exact2(Holder<T>,T)
        ////is applied to (Holder,Long)
        //Long r6 = exact2(qualified, lng);
        //Long r7 = exact2(unbounded, lng); // Error:
        ////exact2(Holder<T>,T) cannot be applied to
        ////(Holder<capture of ?>,Long)
        //Long r8 = exact2(bounded, lng); // Error:
        ////exact2(Holder<T>,T) cannot be applied
        ////to (Holder<capture of ? extends Long>,Long)

        Long r9 = wildSubtype(raw, lng); // Warnings:
        //Unchecked conversion from Holder
        //to Holder<? extends Long>
        //Unchecked method invocation:
        //wildSubtype(Holder<? extends T>,T) is
        //applied to (Holder,Long)
        Long r10 = wildSubtype(qualified, lng);
        //OK, but can only return Object:
        Object r11 = wildSubtype(unbounded, lng);
        Long r12 = wildSubtype(bounded, lng);

        wildSupertype(raw, lng); // Warnings:
        //Unchecked conversion from Holder
        //to Holder<? super Long>
        //Unchecked method invocation:
        //wildSupertype(Holder<? super T>,T)
        //is applied to (Holder,Long)

        wildSupertype(qualified, lng);
        //wildSupertype(unbounded, lng); // Error:
        //  wildSupertype(Holder<? super T>,T) cannot be
        //  applied to (Holder<capture of ?>,Long)
        //wildSupertype(bounded, lng); // Error:
        //  wildSupertype(Holder<? super T>,T) cannot be
        // applied to (Holder<capture of ? extends Long>,Long)
    }
}








// P399
class CaptureConversion {
    static <T> void f1(Holder<T> holder) {
        T t = holder.get();
        System.out.println(t.getClass().getSimpleName());
    }
    static void f2(Holder<?> holder) {
        // f2方法捕获了泛型的类型，并将泛型传给f1
        f1(holder); // Call with captured type
    }
    //@SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Holder raw = new Holder<Integer>(1);
        f1(raw); // Produces warnings
        f2(raw); // No warnings
        Holder rawBasic = new Holder();
        rawBasic.set(new Object()); // Warning
        f2(rawBasic); // No warnings
        // Upcast to Holder<?>, still figures it out:
        Holder<?> wildcarded = new Holder<Double>(1.0);
        f2(wildcarded);
    }
}















