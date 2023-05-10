/*
Anvay Panguluri
Period 1
4/17/2021
 */

//this class allows me to store string variables; inherited from the variable class in order to allow intVars and strVars to be stored in the same ArrayLists and arrays
public class stringVar extends variable {
    private String val;

    //custom constructor; takes in variable name
    public stringVar(String s) {
        super(s);
    }

    public void setVal(String val) {
        this.val = val;
        setAssigned(true);
    }

    public String getStringVal() {
        return val;
    }

    public String printVal() {
        return val;
    }

    public String getType() {
        return "string";
    }

    public String toString() {
        return val;
    }
}