package pattern.singleton;

/**
 * @author chenpiqian
 * @date: 2020-07-08
 */
public class StarvingSingleton {
    private static final StarvingSingleton instance = new StarvingSingleton();
    private StarvingSingleton(){}
    public static StarvingSingleton getInstance(){
        return instance;
    }
}
