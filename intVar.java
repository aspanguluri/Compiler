/*
Anvay Panguluri
Period 1
4/17/2021
 */

//this class allows me to store integer variables; inherited from the variable class in order to allow intVars and strVars to be stored in the same ArrayLists and arrays
public class intVar extends variable {
    private int val;

    //custom constructor, takes in the name of the variable
    public intVar(String s) {
        super(s);
    }

    public void setVal(int val) {
        this.val = val;
        setAssigned(true);
    }

    public int getIntVal() {
        return val;
    }

    public String getType() {
        return "int";
    }

    public String toString() {
        return ""+val;
    }
}