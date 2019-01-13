package ssm.validate.bean;

import java.util.List;

public class Goddess{
    private Boolean marry;
    private String goddessName;
    private List<MaleGod> maleGodList;

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

    public List<MaleGod> getMaleGodList() {
        return maleGodList;
    }

    public void setMaleGodList(List<MaleGod> maleGodList) {
        this.maleGodList = maleGodList;
    }

    static class MaleGod {
        private Integer height;
        private Boolean rich;
        private Boolean handsome;
        private String result;
        private Guy guy;

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

        public Guy getGuy() {
            return guy;
        }

        public void setGuy(Guy guy) {
            this.guy = guy;
        }

        static class Guy {
            private String guyName;
            private Integer age;

            public String getGuyName() {
                return guyName;
            }

            public void setGuyName(String guyName) {
                this.guyName = guyName;
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
