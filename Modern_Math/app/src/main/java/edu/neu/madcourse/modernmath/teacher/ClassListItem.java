package edu.neu.madcourse.modernmath.teacher;

public class ClassListItem {
    private String className;
    private String classPeriod;
    private String classCode;
    private int logoID;

    public ClassListItem(String className, String classPeriod, String classCode, int logoID) {
        this.className = className;
        this.classPeriod = classPeriod;
        this.classCode = classCode;
        this.logoID = logoID;
    }

    public String getClassName() {
        return className;
    }

    public String getClassPeriod() {
        return classPeriod;
    }

    public String getClassCode() {
        return classCode;
    }

    public int getLogoID() {
        return logoID;
    }
}
