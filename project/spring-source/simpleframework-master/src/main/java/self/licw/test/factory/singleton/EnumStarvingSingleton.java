package self.licw.test.factory.singleton;

public class EnumStarvingSingleton {
    private EnumStarvingSingleton(){}
    public static EnumStarvingSingleton getInstance(){
        return ContainerHolder.HOLDER.instance;
    }
    //注意该枚举的使用方法，该枚举只有一个常量值HOLDER，使用该枚举的常量值可以直接获得其对应的属性（也就是其实例）
    private enum ContainerHolder{
        HOLDER;
        private EnumStarvingSingleton instance;
        //在加载枚举的时候会加载枚举的构造函数，也就是相当于饿汉模式了。
        ContainerHolder(){
            instance = new EnumStarvingSingleton();
        }
    }
}
