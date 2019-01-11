package ssm.testgenerator.model;

import java.util.Date;

public class TestGenerator {
    private String idTest;

    private String idUpChar;

    private Byte upTwoInt;

    private Date lowerUpDate;

    private Double idAllDouble;

    private Integer firstTwoThree;

    public String getIdTest() {
        return idTest;
    }

    public void setIdTest(String idTest) {
        this.idTest = idTest == null ? null : idTest.trim();
    }

    public String getIdUpChar() {
        return idUpChar;
    }

    public void setIdUpChar(String idUpChar) {
        this.idUpChar = idUpChar == null ? null : idUpChar.trim();
    }

    public Byte getUpTwoInt() {
        return upTwoInt;
    }

    public void setUpTwoInt(Byte upTwoInt) {
        this.upTwoInt = upTwoInt;
    }

    public Date getLowerUpDate() {
        return lowerUpDate;
    }

    public void setLowerUpDate(Date lowerUpDate) {
        this.lowerUpDate = lowerUpDate;
    }

    public Double getIdAllDouble() {
        return idAllDouble;
    }

    public void setIdAllDouble(Double idAllDouble) {
        this.idAllDouble = idAllDouble;
    }

    public Integer getFirstTwoThree() {
        return firstTwoThree;
    }

    public void setFirstTwoThree(Integer firstTwoThree) {
        this.firstTwoThree = firstTwoThree;
    }
}