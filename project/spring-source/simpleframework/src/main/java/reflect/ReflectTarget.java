package reflect;

/**
 * @author chenpiqian
 * @date: 2020-07-01
 */
public class ReflectTarget extends ReflectTargetOrigin {

	ReflectTarget(String string){
		System.out.println("默认构造方法 s = "+ string);
	}

	public ReflectTarget(){
		System.out.println("调用了公有的无参构造方法。。。");
	}

	public ReflectTarget(char name){
		System.out.println("调用了带有一个参数的构造方法，参数值为 "+name);
	}

	public ReflectTarget(String name, int index) {
		System.out.println("调用了带有多个参数的构造方法，参数值为【目标名】： " + name + " 【序号】" + index);
	}

	protected ReflectTarget(boolean n){
		System.out.println("受保护的构造方法 n = "+n);
	}

	private ReflectTarget(int index){
		System.out.println("私有构造方法 index = "+index);
	}

	public String name;
	protected int index;
	char type;
	private String targetInfo;

	@Override
	public String toString() {
		return "ReflectTarget{" +
				"name='" + name + '\'' +
				", index=" + index +
				", type=" + type +
				", targetInfo='" + targetInfo + '\'' +
				'}';
	}

	public static void main(String[] args) throws ClassNotFoundException {
		//getClass()获取class对象
		Class<? extends ReflectTarget> aClass = new ReflectTarget().getClass();
		//静态方式获取Class对象
		Class bClass = ReflectTarget.class;
		// Class.forName后去class对象。常用，可以不导入包
		Class<?> cClass = Class.forName("baselearn.pattern.reflect.ReflectTarget");
		System.out.println(aClass == bClass);
		System.out.println(aClass == cClass);
		/**
		 * class对象都相等，在运行期间，一个类只有一个与之对应的class对象产生
		 * 通过类可以查看类中的成员变量，方法，构造函数，就像一面镜子，所以称为反射
		 */
	}

}
