package org.vaadin.example;

public class MsCode {

    private String _id;
    private String mscode;
    private String year;
    private String estCode;
    private float estimate;
    private float se;
    private float lowerCIB;
    private float upperCIB;
    private String flag;

    // Getters and Setters

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMscode() {
        return mscode;
    }

    public void setMscode(String mscode) {
        this.mscode = mscode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEstCode() {
        return estCode;
    }

    public void setEstCode(String estCode) {
        this.estCode = estCode;
    }

    public float getEstimate() {
        return estimate;
    }

    public void setEstimate(float estimate) {
        this.estimate = estimate;
    }

    public float getSe() {
        return se;
    }

    public void setSe(float se) {
        this.se = se;
    }

    public float getLowerCIB() {
        return lowerCIB;
    }

    public void setLowerCIB(float lowerCIB) {
        this.lowerCIB = lowerCIB;
    }

    public float getUpperCIB() {
        return upperCIB;
    }

    public void setUpperCIB(float upperCIB) {
        this.upperCIB = upperCIB;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}