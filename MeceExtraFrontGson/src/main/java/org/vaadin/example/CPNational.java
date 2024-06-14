package org.vaadin.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CPNational {
    // Declaramos las variables de los datos que vamos a tratar del fichero, incluido el id
    @JsonProperty("_id")
    private String _id;
    @JsonProperty("mscode")
    private String mscode;
    @JsonProperty("year")
    private String year;
    @JsonProperty("estCode")
    private String estCode;
    @JsonProperty("estimate")
    private float estimate;
    @JsonProperty("se")
    private float se;
    @JsonProperty("lowerCIB")
    private float lowerCIB;
    @JsonProperty("upperCIB")
    private float upperCIB;
    @JsonProperty("flag")
    private String flag;

    public CPNational(String _id, String mscode,String year,String estCode,float estimate,float se,
                          float lowerCIB,float upperCIB,String flag) {
        this._id = _id;
        this.mscode = mscode;
        this.year = year;
        this.estCode = estCode;
        this.estimate = estimate;
        this.se = se;
        this.lowerCIB = lowerCIB;
        this.upperCIB = upperCIB;
        this.flag = flag;
    }

    public CPNational() {

    }

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

    @Override
    public String toString() {
        return "{" +
                "_id='" + _id + '\'' +
                ", mscode='" + mscode + '\'' +
                ", year='" + year + '\'' +
                ", estCode=" + estCode + '\'' +
                ", estimate='" + estimate + '\'' +
                ", se='" + se + '\'' +
                ", lowerCIB='" + lowerCIB + '\'' +
                ", upperCIB='" + upperCIB + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}


