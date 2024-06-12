package com.meceextrabackgson.meceextrabackgson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;


    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
    public class MsCode {

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

        public MsCode() {
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

