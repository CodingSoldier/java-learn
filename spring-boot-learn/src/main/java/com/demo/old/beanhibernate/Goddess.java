package com.demo.old.beanhibernate;

import java.util.List;

public class Goddess{
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
