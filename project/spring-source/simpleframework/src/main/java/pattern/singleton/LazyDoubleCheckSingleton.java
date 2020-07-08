package pattern.singleton;

/**
 * @author chenpiqian
 * @date: 2020-07-08
 */
public class LazyDoubleCheckSingleton {

    private volatile static LazyDoubleCheckSingleton instance;

    private LazyDoubleCheckSingleton(){}

    public static LazyDoubleCheckSingleton getInstance(){
        if (instance == null){
            synchronized (LazyDoubleCheckSingleton.class){
                if (instance == null){
                    /**
                     * 1、分配内存给线程内存变量  memoryInstance = allocate()
                     * 2、初始化实例  init(memoryInstance)
                     * 3、内存变量赋值给instance变量  instance = memoryInstance
                     *
                     * 不加volatile，2、3可能重排序，
                     * instance = 内存地址，但是实例没初始化完成
                     * 此时其他线程getInstance()，获取到的是未初始化完成的实例
                     */
                    instance = new LazyDoubleCheckSingleton();
                }
            }
        }
        return instance;
    }

}
