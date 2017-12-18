package ssm.projectweb.json;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/18
 */
public class JsonVo implements Serializable{
    private String p1;
    private List<String> l1;
    private Map<String, String> m1;
    private Date t1;

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public List<String> getL1() {
        return l1;
    }

    public void setL1(List<String> l1) {
        this.l1 = l1;
    }

    public Map<String, String> getM1() {
        return m1;
    }

    public void setM1(Map<String, String> m1) {
        this.m1 = m1;
    }

    public Date getT1() {
        return t1;
    }

    public void setT1(Date t1) {
        this.t1 = t1;
    }
}
