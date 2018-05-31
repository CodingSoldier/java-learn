package com.demo.testgenerator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TestGeneratorExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TestGeneratorExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdTestIsNull() {
            addCriterion("id_test is null");
            return (Criteria) this;
        }

        public Criteria andIdTestIsNotNull() {
            addCriterion("id_test is not null");
            return (Criteria) this;
        }

        public Criteria andIdTestEqualTo(String value) {
            addCriterion("id_test =", value, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestNotEqualTo(String value) {
            addCriterion("id_test <>", value, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestGreaterThan(String value) {
            addCriterion("id_test >", value, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestGreaterThanOrEqualTo(String value) {
            addCriterion("id_test >=", value, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestLessThan(String value) {
            addCriterion("id_test <", value, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestLessThanOrEqualTo(String value) {
            addCriterion("id_test <=", value, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestLike(String value) {
            addCriterion("id_test like", value, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestNotLike(String value) {
            addCriterion("id_test not like", value, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestIn(List<String> values) {
            addCriterion("id_test in", values, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestNotIn(List<String> values) {
            addCriterion("id_test not in", values, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestBetween(String value1, String value2) {
            addCriterion("id_test between", value1, value2, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdTestNotBetween(String value1, String value2) {
            addCriterion("id_test not between", value1, value2, "idTest");
            return (Criteria) this;
        }

        public Criteria andIdUpCharIsNull() {
            addCriterion("idUpChar is null");
            return (Criteria) this;
        }

        public Criteria andIdUpCharIsNotNull() {
            addCriterion("idUpChar is not null");
            return (Criteria) this;
        }

        public Criteria andIdUpCharEqualTo(String value) {
            addCriterion("idUpChar =", value, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharNotEqualTo(String value) {
            addCriterion("idUpChar <>", value, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharGreaterThan(String value) {
            addCriterion("idUpChar >", value, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharGreaterThanOrEqualTo(String value) {
            addCriterion("idUpChar >=", value, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharLessThan(String value) {
            addCriterion("idUpChar <", value, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharLessThanOrEqualTo(String value) {
            addCriterion("idUpChar <=", value, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharLike(String value) {
            addCriterion("idUpChar like", value, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharNotLike(String value) {
            addCriterion("idUpChar not like", value, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharIn(List<String> values) {
            addCriterion("idUpChar in", values, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharNotIn(List<String> values) {
            addCriterion("idUpChar not in", values, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharBetween(String value1, String value2) {
            addCriterion("idUpChar between", value1, value2, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andIdUpCharNotBetween(String value1, String value2) {
            addCriterion("idUpChar not between", value1, value2, "idUpChar");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntIsNull() {
            addCriterion("UpTwoInt is null");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntIsNotNull() {
            addCriterion("UpTwoInt is not null");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntEqualTo(Byte value) {
            addCriterion("UpTwoInt =", value, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntNotEqualTo(Byte value) {
            addCriterion("UpTwoInt <>", value, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntGreaterThan(Byte value) {
            addCriterion("UpTwoInt >", value, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntGreaterThanOrEqualTo(Byte value) {
            addCriterion("UpTwoInt >=", value, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntLessThan(Byte value) {
            addCriterion("UpTwoInt <", value, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntLessThanOrEqualTo(Byte value) {
            addCriterion("UpTwoInt <=", value, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntIn(List<Byte> values) {
            addCriterion("UpTwoInt in", values, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntNotIn(List<Byte> values) {
            addCriterion("UpTwoInt not in", values, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntBetween(Byte value1, Byte value2) {
            addCriterion("UpTwoInt between", value1, value2, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andUpTwoIntNotBetween(Byte value1, Byte value2) {
            addCriterion("UpTwoInt not between", value1, value2, "upTwoInt");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateIsNull() {
            addCriterion("lower_Up_Date is null");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateIsNotNull() {
            addCriterion("lower_Up_Date is not null");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateEqualTo(Date value) {
            addCriterionForJDBCDate("lower_Up_Date =", value, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("lower_Up_Date <>", value, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateGreaterThan(Date value) {
            addCriterionForJDBCDate("lower_Up_Date >", value, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("lower_Up_Date >=", value, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateLessThan(Date value) {
            addCriterionForJDBCDate("lower_Up_Date <", value, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("lower_Up_Date <=", value, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateIn(List<Date> values) {
            addCriterionForJDBCDate("lower_Up_Date in", values, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("lower_Up_Date not in", values, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("lower_Up_Date between", value1, value2, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andLowerUpDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("lower_Up_Date not between", value1, value2, "lowerUpDate");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleIsNull() {
            addCriterion("ID_ALL_DOUBLE is null");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleIsNotNull() {
            addCriterion("ID_ALL_DOUBLE is not null");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleEqualTo(Double value) {
            addCriterion("ID_ALL_DOUBLE =", value, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleNotEqualTo(Double value) {
            addCriterion("ID_ALL_DOUBLE <>", value, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleGreaterThan(Double value) {
            addCriterion("ID_ALL_DOUBLE >", value, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleGreaterThanOrEqualTo(Double value) {
            addCriterion("ID_ALL_DOUBLE >=", value, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleLessThan(Double value) {
            addCriterion("ID_ALL_DOUBLE <", value, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleLessThanOrEqualTo(Double value) {
            addCriterion("ID_ALL_DOUBLE <=", value, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleIn(List<Double> values) {
            addCriterion("ID_ALL_DOUBLE in", values, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleNotIn(List<Double> values) {
            addCriterion("ID_ALL_DOUBLE not in", values, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleBetween(Double value1, Double value2) {
            addCriterion("ID_ALL_DOUBLE between", value1, value2, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andIdAllDoubleNotBetween(Double value1, Double value2) {
            addCriterion("ID_ALL_DOUBLE not between", value1, value2, "idAllDouble");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeIsNull() {
            addCriterion("First_two_Three is null");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeIsNotNull() {
            addCriterion("First_two_Three is not null");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeEqualTo(Integer value) {
            addCriterion("First_two_Three =", value, "firstTwoThree");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeNotEqualTo(Integer value) {
            addCriterion("First_two_Three <>", value, "firstTwoThree");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeGreaterThan(Integer value) {
            addCriterion("First_two_Three >", value, "firstTwoThree");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeGreaterThanOrEqualTo(Integer value) {
            addCriterion("First_two_Three >=", value, "firstTwoThree");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeLessThan(Integer value) {
            addCriterion("First_two_Three <", value, "firstTwoThree");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeLessThanOrEqualTo(Integer value) {
            addCriterion("First_two_Three <=", value, "firstTwoThree");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeIn(List<Integer> values) {
            addCriterion("First_two_Three in", values, "firstTwoThree");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeNotIn(List<Integer> values) {
            addCriterion("First_two_Three not in", values, "firstTwoThree");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeBetween(Integer value1, Integer value2) {
            addCriterion("First_two_Three between", value1, value2, "firstTwoThree");
            return (Criteria) this;
        }

        public Criteria andFirstTwoThreeNotBetween(Integer value1, Integer value2) {
            addCriterion("First_two_Three not between", value1, value2, "firstTwoThree");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}