package com.demo.boy.model;

import java.util.ArrayList;
import java.util.List;

public class BoyExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BoyExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andBigNameIsNull() {
            addCriterion("big_name is null");
            return (Criteria) this;
        }

        public Criteria andBigNameIsNotNull() {
            addCriterion("big_name is not null");
            return (Criteria) this;
        }

        public Criteria andBigNameEqualTo(String value) {
            addCriterion("big_name =", value, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameNotEqualTo(String value) {
            addCriterion("big_name <>", value, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameGreaterThan(String value) {
            addCriterion("big_name >", value, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameGreaterThanOrEqualTo(String value) {
            addCriterion("big_name >=", value, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameLessThan(String value) {
            addCriterion("big_name <", value, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameLessThanOrEqualTo(String value) {
            addCriterion("big_name <=", value, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameLike(String value) {
            addCriterion("big_name like", value, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameNotLike(String value) {
            addCriterion("big_name not like", value, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameIn(List<String> values) {
            addCriterion("big_name in", values, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameNotIn(List<String> values) {
            addCriterion("big_name not in", values, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameBetween(String value1, String value2) {
            addCriterion("big_name between", value1, value2, "bigName");
            return (Criteria) this;
        }

        public Criteria andBigNameNotBetween(String value1, String value2) {
            addCriterion("big_name not between", value1, value2, "bigName");
            return (Criteria) this;
        }

        public Criteria andLoyaltyIsNull() {
            addCriterion("loyalty is null");
            return (Criteria) this;
        }

        public Criteria andLoyaltyIsNotNull() {
            addCriterion("loyalty is not null");
            return (Criteria) this;
        }

        public Criteria andLoyaltyEqualTo(Integer value) {
            addCriterion("loyalty =", value, "loyalty");
            return (Criteria) this;
        }

        public Criteria andLoyaltyNotEqualTo(Integer value) {
            addCriterion("loyalty <>", value, "loyalty");
            return (Criteria) this;
        }

        public Criteria andLoyaltyGreaterThan(Integer value) {
            addCriterion("loyalty >", value, "loyalty");
            return (Criteria) this;
        }

        public Criteria andLoyaltyGreaterThanOrEqualTo(Integer value) {
            addCriterion("loyalty >=", value, "loyalty");
            return (Criteria) this;
        }

        public Criteria andLoyaltyLessThan(Integer value) {
            addCriterion("loyalty <", value, "loyalty");
            return (Criteria) this;
        }

        public Criteria andLoyaltyLessThanOrEqualTo(Integer value) {
            addCriterion("loyalty <=", value, "loyalty");
            return (Criteria) this;
        }

        public Criteria andLoyaltyIn(List<Integer> values) {
            addCriterion("loyalty in", values, "loyalty");
            return (Criteria) this;
        }

        public Criteria andLoyaltyNotIn(List<Integer> values) {
            addCriterion("loyalty not in", values, "loyalty");
            return (Criteria) this;
        }

        public Criteria andLoyaltyBetween(Integer value1, Integer value2) {
            addCriterion("loyalty between", value1, value2, "loyalty");
            return (Criteria) this;
        }

        public Criteria andLoyaltyNotBetween(Integer value1, Integer value2) {
            addCriterion("loyalty not between", value1, value2, "loyalty");
            return (Criteria) this;
        }

        public Criteria andYanValIsNull() {
            addCriterion("yan_val is null");
            return (Criteria) this;
        }

        public Criteria andYanValIsNotNull() {
            addCriterion("yan_val is not null");
            return (Criteria) this;
        }

        public Criteria andYanValEqualTo(Float value) {
            addCriterion("yan_val =", value, "yanVal");
            return (Criteria) this;
        }

        public Criteria andYanValNotEqualTo(Float value) {
            addCriterion("yan_val <>", value, "yanVal");
            return (Criteria) this;
        }

        public Criteria andYanValGreaterThan(Float value) {
            addCriterion("yan_val >", value, "yanVal");
            return (Criteria) this;
        }

        public Criteria andYanValGreaterThanOrEqualTo(Float value) {
            addCriterion("yan_val >=", value, "yanVal");
            return (Criteria) this;
        }

        public Criteria andYanValLessThan(Float value) {
            addCriterion("yan_val <", value, "yanVal");
            return (Criteria) this;
        }

        public Criteria andYanValLessThanOrEqualTo(Float value) {
            addCriterion("yan_val <=", value, "yanVal");
            return (Criteria) this;
        }

        public Criteria andYanValIn(List<Float> values) {
            addCriterion("yan_val in", values, "yanVal");
            return (Criteria) this;
        }

        public Criteria andYanValNotIn(List<Float> values) {
            addCriterion("yan_val not in", values, "yanVal");
            return (Criteria) this;
        }

        public Criteria andYanValBetween(Float value1, Float value2) {
            addCriterion("yan_val between", value1, value2, "yanVal");
            return (Criteria) this;
        }

        public Criteria andYanValNotBetween(Float value1, Float value2) {
            addCriterion("yan_val not between", value1, value2, "yanVal");
            return (Criteria) this;
        }

        public Criteria andSmallNameIsNull() {
            addCriterion("small_name is null");
            return (Criteria) this;
        }

        public Criteria andSmallNameIsNotNull() {
            addCriterion("small_name is not null");
            return (Criteria) this;
        }

        public Criteria andSmallNameEqualTo(String value) {
            addCriterion("small_name =", value, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameNotEqualTo(String value) {
            addCriterion("small_name <>", value, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameGreaterThan(String value) {
            addCriterion("small_name >", value, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameGreaterThanOrEqualTo(String value) {
            addCriterion("small_name >=", value, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameLessThan(String value) {
            addCriterion("small_name <", value, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameLessThanOrEqualTo(String value) {
            addCriterion("small_name <=", value, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameLike(String value) {
            addCriterion("small_name like", value, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameNotLike(String value) {
            addCriterion("small_name not like", value, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameIn(List<String> values) {
            addCriterion("small_name in", values, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameNotIn(List<String> values) {
            addCriterion("small_name not in", values, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameBetween(String value1, String value2) {
            addCriterion("small_name between", value1, value2, "smallName");
            return (Criteria) this;
        }

        public Criteria andSmallNameNotBetween(String value1, String value2) {
            addCriterion("small_name not between", value1, value2, "smallName");
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