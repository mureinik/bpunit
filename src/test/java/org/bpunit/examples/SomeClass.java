package org.bpunit.examples;

import org.junit.Assert;

import java.util.Date;

/**
 * A class with several properties, for testing assertions.
 */
public class SomeClass {
    private Integer myInt;
    private Date myDate;
    private String myString;
    private Boolean someBoolean;
    private Boolean someOtherBoolean;
    private double myPrimitiveDouble = Double.NaN;
    private Object myObject;

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public Date getMyDate() {
        return myDate;
    }

    public void setMyDate(Date myDate) {
        this.myDate = myDate;
    }

    public Integer getMyInt() {
        return myInt;
    }

    public void setMyInt(Integer myInt) {
        this.myInt = myInt;
    }

    public Boolean getSomeBoolean() {
        return someBoolean;
    }

    public void setSomeBoolean(Boolean someBoolean) {
        this.someBoolean = someBoolean;
    }

    public Boolean isSomeOtherBoolean() {
        return someOtherBoolean;
    }

    public void setSomeOtherBoolean(Boolean someOtherBoolean) {
        this.someOtherBoolean = someOtherBoolean;
    }

    public double getMyPrimitiveDouble() {
        return myPrimitiveDouble;
    }

    public void setMyPrimitiveDouble(double myPrimitiveDouble) {
        this.myPrimitiveDouble = myPrimitiveDouble;
    }

    public Object getMyObject() {
        return myObject;
    }

    public void setMyObject(Object myObject) {
        this.myObject = myObject;
    }

    public void setSomethingBad(int i, int j) {
        Assert.fail("setSomethingBad should fail as it has two arguments");
    }

    public void setWithoutGetter(int ignore) {
    }

    public void setWithWrongType(int ignore) {
    }

    public String getWithWrongType() {
        return null;
    }
}
