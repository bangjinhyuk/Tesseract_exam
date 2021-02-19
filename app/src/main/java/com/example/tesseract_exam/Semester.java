package com.example.tesseract_exam;

public class Semester {
    private String major1,major2;
    private String Class1,Class2,Class3,Class4,Class5;


    public Semester(){
    }

    @Override
    public String toString(){
        return  "semester{" +
                "major1='" + major1 + '\'' +
                ", major2=" + major2 +
                ", Class1='" + Class1 + '\'' +
                ", Class2='" + Class2 + '\'' +
                ", Class3='" + Class3 + '\'' +
                ", Class4='" + Class4 + '\'' +
                ", Class5='" + Class5 + '\'' +
                '}';
    }
    public Semester(String major1, String major2, String Class1, String Class2, String Class3, String Class4, String Class5){
        this.major1= major1;
        this.major2 = major2;
        this.Class1 = Class1;
        this.Class2 = Class2;
        this.Class3 = Class3;
        this.Class4 = Class4;
        this.Class5 = Class5;
    }

    public String getClass1() {
        return Class1;
    }

    public void setClass1(String class1) {
        Class1 = class1;
    }

    public String getClass2() {
        return Class2;
    }

    public void setClass2(String class2) {
        Class2 = class2;
    }

    public String getClass3() {
        return Class3;
    }

    public void setClass3(String class3) {
        Class3 = class3;
    }

    public String getClass4() {
        return Class4;
    }

    public void setClass4(String class4) {
        Class4 = class4;
    }

    public String getClass5() {
        return Class5;
    }

    public void setClass5(String class5) {
        Class5 = class5;
    }

    public String getMajor1() {
        return major1;
    }

    public void setMajor1(String major1) {
        this.major1 = major1;
    }

    public String getMajor2() {
        return major2;
    }

    public void setMajor2(String major2) {
        this.major2 = major2;
    }
}

