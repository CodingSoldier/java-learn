package pattern.singleton;

/**
 * @author chenpiqian
 * @date: 2020-07-08
 */
public class TestSingleton {

    public static void main(String[] args) {

        //System.out.println("######"+StarvingSingleton.getInstance());
        //System.out.println("######"+StarvingSingleton.getInstance());
        //System.out.println("######"+LazyDoubleCheckSingleton.getInstance());
        //System.out.println("######"+LazyDoubleCheckSingleton.getInstance());

        //System.out.println("######"+Singleton.getInstance());
        //System.out.println("######"+Singleton.getInstance());


    }

    private static class Singleton{
        private Singleton(){}
        public static Singleton getInstance(){
            return SingletonHolder.HOLDER.instance;
        }

        private enum SingletonHolder{
            HOLDER;
            private Singleton instance;
            SingletonHolder(){
                instance = new Singleton();
            }
        }
    }

}
