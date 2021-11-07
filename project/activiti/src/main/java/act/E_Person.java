package act;

import java.io.Serializable;

public class E_Person implements Serializable {

    // 设置序列化Id
    private static final long serialVersionUID = -1069711686759278564L;

    private Integer id; // 编号
    private String name; //姓名

    private String xxxxxx;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXxxxxx() {
        return xxxxxx;
    }

    public void setXxxxxx(String xxxxxx) {
        this.xxxxxx = xxxxxx;
    }

    @Override
    public String toString() {
        return "E_Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", xxxxxx='" + xxxxxx + '\'' +
                '}';
    }
}
