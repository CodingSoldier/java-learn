package ssm.validate.bean;

import java.util.Date;
import java.util.List;

public class Dream{
    private String txt;
    private String exe;
    private Date time;
    private int money;
    private List<String> achieveList;

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getExe() {
        return exe;
    }

    public void setExe(String exe) {
        this.exe = exe;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<String> getAchieveList() {
        return achieveList;
    }

    public void setAchieveList(List<String> achieveList) {
        this.achieveList = achieveList;
    }
}
