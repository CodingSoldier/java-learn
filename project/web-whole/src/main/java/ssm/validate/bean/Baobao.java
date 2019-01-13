package ssm.validate.bean;

import java.math.BigDecimal;
import java.util.List;

public class Baobao{
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
