package com.demo.old.beanhibernate;

public class Family{
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
