package com.demo.testvalidate.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class UserVo extends User {

    private Friend friend;
    private Family family;
    private Goddess goddess;
    private List<String> hobbyList;
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
}

class Friend{
    private Boolean nice;
    private String name;

    public Boolean getNice() {
        return nice;
    }

    public void setNice(Boolean nice) {
        this.nice = nice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Family{
    private Integer num;
    private Mom mom;
    private House house;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Mom getMom() {
        return mom;
    }

    public void setMom(Mom mom) {
        this.mom = mom;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    static class Mom {
        private String momName;
        private String kindness;

        public String getMomName() {
            return momName;
        }

        public void setMomName(String momName) {
            this.momName = momName;
        }

        public String getKindness() {
            return kindness;
        }

        public void setKindness(String kindness) {
            this.kindness = kindness;
        }
    }

    static class House{
        private Double area;
        private String houseName;
        private Window window;

        public Double getArea() {
            return area;
        }

        public void setArea(Double area) {
            this.area = area;
        }

        public String getHouseName() {
            return houseName;
        }

        public void setHouseName(String houseName) {
            this.houseName = houseName;
        }

        public Window getWindow() {
            return window;
        }

        public void setWindow(Window window) {
            this.window = window;
        }

        static class Window{
            private Integer windowHeight;
            private Integer windowWidth;

            public Integer getWindowHeight() {
                return windowHeight;
            }

            public void setWindowHeight(Integer windowHeight) {
                this.windowHeight = windowHeight;
            }

            public Integer getWindowWidth() {
                return windowWidth;
            }

            public void setWindowWidth(Integer windowWidth) {
                this.windowWidth = windowWidth;
            }
        }
    }
}

class Goddess{
    private Boolean marry;
    private String goddessName;
    private List<Backup> backupList;

    public Boolean getMarry() {
        return marry;
    }

    public void setMarry(Boolean marry) {
        this.marry = marry;
    }

    public String getGoddessName() {
        return goddessName;
    }

    public void setGoddessName(String goddessName) {
        this.goddessName = goddessName;
    }

    public List<Backup> getBackupList() {
        return backupList;
    }

    public void setBackupList(List<Backup> backupList) {
        this.backupList = backupList;
    }

    static class Backup{
        private Integer height;
        private Boolean rich;
        private Boolean handsome;
        private String result;
        private Girl girl;

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Boolean getRich() {
            return rich;
        }

        public void setRich(Boolean rich) {
            this.rich = rich;
        }

        public Boolean getHandsome() {
            return handsome;
        }

        public void setHandsome(Boolean handsome) {
            this.handsome = handsome;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Girl getGirl() {
            return girl;
        }

        public void setGirl(Girl girl) {
            this.girl = girl;
        }

        static class Girl{
            private String girlName;
            private Integer age;

            public String getGirlName() {
                return girlName;
            }

            public void setGirlName(String girlName) {
                this.girlName = girlName;
            }

            public Integer getAge() {
                return age;
            }

            public void setAge(Integer age) {
                this.age = age;
            }
        }
    }
}

class Dream{
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

class Baobao{
    private String brand;
    private double price;
    private Card card;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    static class Card{
        private BigDecimal num;
        private String cardName;
        private Consume consume;

        public BigDecimal getNum() {
            return num;
        }

        public void setNum(BigDecimal num) {
            this.num = num;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public Consume getConsume() {
            return consume;
        }

        public void setConsume(Consume consume) {
            this.consume = consume;
        }

        static class Consume{
            private List<Integer> orderList;
            private String input;
            private String output;

            public List<Integer> getOrderList() {
                return orderList;
            }

            public void setOrderList(List<Integer> orderList) {
                this.orderList = orderList;
            }

            public String getInput() {
                return input;
            }

            public void setInput(String input) {
                this.input = input;
            }

            public String getOutput() {
                return output;
            }

            public void setOutput(String output) {
                this.output = output;
            }
        }
    }
}
