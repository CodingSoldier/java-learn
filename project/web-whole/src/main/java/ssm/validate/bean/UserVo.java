package ssm.validate.bean;

import java.util.List;

public class UserVo extends User {

    private Friend friend;
    private Family family;
    private Goddess goddess;
    private List<String> hobbyList;
    private List<List<String>> schoolClassList;
    private List<List<Cate>> diningHallList;
    private List<Dream> dreamList;
    private List<Baobao> baoBaoList;

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Goddess getGoddess() {
        return goddess;
    }

    public void setGoddess(Goddess goddess) {
        this.goddess = goddess;
    }

    public List<String> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<String> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public List<List<String>> getSchoolClassList() {
        return schoolClassList;
    }

    public void setSchoolClassList(List<List<String>> schoolClassList) {
        this.schoolClassList = schoolClassList;
    }

    public List<Dream> getDreamList() {
        return dreamList;
    }

    public void setDreamList(List<Dream> dreamList) {
        this.dreamList = dreamList;
    }

    public List<Baobao> getBaoBaoList() {
        return baoBaoList;
    }

    public void setBaoBaoList(List<Baobao> baoBaoList) {
        this.baoBaoList = baoBaoList;
    }

    public List<List<Cate>> getDiningHallList() {
        return diningHallList;
    }

    public void setDiningHallList(List<List<Cate>> diningHallList) {
        this.diningHallList = diningHallList;
    }
}


