/*
Anvay Panguluri
Period 1
4/17/2021
 */

public abstract class variable {
    //instance variables
    private String name;
    private boolean assigned;

    //custom constructor, takes in variable name
    public variable(String n) {
        name = n;
        assigned = false;
    }

    public String getName() {
        return name;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public boolean isAssigned() {
        return assigned;
    }

    //dummy method to allow intVar to use setVar(); uses overloading to get around the inheritance rules
    public void setVal(int x) {}

    //dummy method to allow strVar to use setVar(); uses overloading to get around the inheritance rules
    public void setVal(String x) {}

    //dummy method to allow intVar and strVar() to use getType(); uses overloading to get around the inheritance rules
    public String getType() {
        return "";
    }

    //dummy method to allow intVar to use getIntVal(); uses overloading to get around the inheritance rules
    public int getIntVal() {
        return 0;
    }

    //dummy method to allow strVar to use getStringVar(); uses overloading to get around the inheritance rules
    public String getStringVal() {
        return "";
    }

    //dummy method to allow intVar and strVar to use printVal(); uses overloading to get around the inheritance rules
    public String printVal() {
        return "";
    }

    //dummy method to allow intVar and strVar to use toString(); uses overloading to get around the inheritance rules
    public String toString() {
        return "";
    }
}