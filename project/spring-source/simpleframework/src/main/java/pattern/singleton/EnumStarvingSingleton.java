package pattern.singleton;

/**
 * @author chenpiqian
 * @date: 2020-07-09
 */
public class EnumStarvingSingleton {

    private EnumStarvingSingleton(){}

    public static EnumStarvingSingleton getInstance(){
        return SingletonHolder.HOLDER.instance;
    }

    private enum SingletonHolder{
        HOLDER;
        private EnumStarvingSingleton instance;
        SingletonHolder(){
            instance = new EnumStarvingSingleton();
        }
    }

}
