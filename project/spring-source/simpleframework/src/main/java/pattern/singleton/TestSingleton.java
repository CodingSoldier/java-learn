package pattern.singleton;

/**
 * @author chenpiqian
 * @date: 2020-07-08
 */
public class TestSingleton {

    public static void main(String[] args) {
        System.out.println("######"+StarvingSingleton.getInstance());
        System.out.println("######"+StarvingSingleton.getInstance());
        System.out.println("######"+LazyDoubleCheckSingleton.getInstance());
        System.out.println("######"+LazyDoubleCheckSingleton.getInstance());
    }

}
