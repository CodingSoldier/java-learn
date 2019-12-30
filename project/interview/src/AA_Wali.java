/**
 * @author chenpiqian
 * @date: 2019-12-30
 */
public class AA_Wali {
    private String name;
    public void sayHi(String helloSentence){
        System.out.println(helloSentence + " " + name);
    }
    private String throwHello(String tag){
        return "Hello " + tag;
    }
    static {
        System.out.println("Hello AA_Wali");
    }
}