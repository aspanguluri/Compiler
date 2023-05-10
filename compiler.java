/*
Anvay Panguluri
Period 1
4/17/2021
 */

import java.util.*;
import java.io.*;

public class compiler {
    public static void main(String[] args) throws FileNotFoundException {

        //creates the line counter
        int lineCounter = 1;

        //creates the arraylist which keeps track of all the declared variables.
        //it holds variable objects, whose subclasses are intvars and stringvars to hold integer variables and string variables respectively.
        ArrayList<variable> declaredVars = new ArrayList<variable>();

        //creates the arraylist which holds the list of currently running loops
        ArrayList<String> currentLoops = new ArrayList<String>();

        //creates the arraylist which holds the list of currently active if blocks
        //initially has a 'false' in it so that it can by changed from 'false' to 'true' when necessary in order for the program to distinguish whether it is in a loop or not
        ArrayList<String> currentIfs = new ArrayList<String>();
        currentIfs.add("false");

        //creates the list of word which are 'protected'; they cannot be used as variable names, if block names, or loop block names
        String[] protectedWords = {
            "new",
            "int",
            "string",
            "\\c",
            "var",
            "raw",
            "out",
            "in",
            "if",
            "endif",
            "loop",
            "endloop"
        };

        //opens the file in which the user-entered code is written, removes all the empty lines, and writes it to a new 'processed' file
        File fin = new File("main.txt");
        processFile(fin);
        File processedIn = new File("processed.txt");
        Scanner scan = new Scanner(processedIn);

        //calls the function which actually compiles and runs the code
        compile(scan, declaredVars, lineCounter, protectedWords, processedIn, null, false, currentLoops, currentIfs);

        //closes the 'scan' scanner
        scan.close();
    }

    public static void compile(Scanner scan, ArrayList<variable> declaredVars, int lineCounter, String[] protectedWords, File processedIn, String loopEndName, boolean isInLoop, ArrayList<String> currentLoops, ArrayList<String> currentIfs) throws FileNotFoundException {
        while (scan.hasNext()) {
            //by default, after every line, the line counter advances by one; however, there are cases where the line counter has to not advance to keep the line count correct
            int timesToAdvanceLine = 1;

            //takes the first word of the line which determines what the line does
            String currentWord = scan.next();

            //if the program is currently in an if-block, the first word of the line is 'endif', and the if-block name indicated is the latest one opened by the code, then the if-block will end
            if (currentIfs.get(0).equals("true")) {
                if (currentWord.equals("endif")) {
                    String name = scan.next();

                    if (name.equals(currentIfs.get(currentIfs.size()-1))) {
                        currentIfs.remove(currentIfs.size()-1);
                        currentIfs.set(0,"false");
                    } else {
                        throw new IllegalArgumentException("Error on line "+lineCounter+": if blocks closed out of order");
                    }
                }
            }

            // deals with new variables
            else if (currentWord.equals("new")) {
                //gets the type of the variable
                String type = scan.next();

                //checks that the variable name is allowed
                String name = validName(scan.next(), protectedWords, lineCounter);

                //if the variable name already exists then the previous variable is deleted
                int index = isDeclared(declaredVars, name);
                if (index != -1) {
                    declaredVars.remove(index);
                }

                //creates an intVar object to hold integer variables, stores it in the declaredVars arraylist
                if (type.equals("int")) {
                    intVar newVar = new intVar(name);
                    declaredVars.add(newVar);

                //creates a stringVar object to hold string variables, stores it in the declaredVars arraylist
                } else if (type.equals("string")) {
                    stringVar newVar = new stringVar(name);
                    declaredVars.add(newVar);
                } else {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": illegal variable type, must be int or string");
                }
            }

            //deals with assigning values to variables
            else if (currentWord.equals("var")) {

                //gets the name of the variable
                String name = scan.next();

                //checks that the variable referred to has been declared
                int index = isDeclared(declaredVars, name);
                if (index!=-1) {

                    //checks that '=' follows the variable name
                    String equals = scan.next();
                    if (!equals.equals("=")) {
                        throw new IllegalArgumentException("Line " + lineCounter + ": Expected =, got " + equals + " instead.");
                    }

                    //if the variable type is an integer, then the program expects an integer to be after the assignment operator; ele, it throws an error
                    if (declaredVars.get(index).getType().equals("int")) {
                        try {
                            //assigns the integer following the assignment operator to the variable named
                            int val = scan.nextInt();
                            declaredVars.get(index).setVal(val);
                            declaredVars.get(index).setAssigned(true);
                        }
                        catch(Exception e) {
                            System.out.println("Line "+lineCounter+": entered value was not an int");
                        }

                    }

                    // the only two types of variables are 'int' and 'string', so the only other case is for the referenced variable to be a string
                    else {

                        //nextLine() returns the rest of the line; this is used to get everything after the space following the assignment operator
                        String substring = scan.nextLine().substring(1);
                        //because nextLine() was called, the program does not need to call nextLine() at the end of the cycle before moving on the the next one
                        timesToAdvanceLine = 0;
                        declaredVars.get(index).setVal(substring);
                    }
                } else {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": variable not declared");
                }
            }

            //if the program encounters a comment then it just moves on to the next line
            else if (currentWord.equals("\\c")) {}

            //deals with printing to the screen
            else if (currentWord.equals("out")) {
                String outType = scan.next();

                //if the type of the thing being outputted is var, the variable is retrieved and printed
                if (outType.equals("var")) {
                    String toBePrinted = scan.next();
                    int index = checkDeclaredAndInitialized(declaredVars, toBePrinted, lineCounter);
                    System.out.println(declaredVars.get(index));
                }
                //if the type of the thing being outputted is raw, the rest of the line is retrieved and printed
                else if (outType.equals("raw")) {
                    String toBePrinted = scan.nextLine().substring(1);
                    timesToAdvanceLine = 0;
                    System.out.println(toBePrinted);
                }
                //if the type of the thing being outputted is not var or raw, then an error is raised
                else {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": 'var' or 'raw' expected; got "+outType+" instead.");
                }
            }

            //deals with inputs
            else if (currentWord.equals("in")) {
                Scanner input = new Scanner(System.in);

                //if 'var' does not follow 'in', then an error is raised
                String variable = scan.next();
                if (!variable.equals("var")) {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": 'var' expected; got "+variable+" instead.");
                }

                //gets the name of the variable that the input is going to be stored in and checks whether it has been declared. If it has not, then an error is raised
                String toBeAssigned = scan.nextLine().substring(1);
                int index = isDeclared(declaredVars, toBeAssigned);
                if (index == -1) {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": variable has not been declared");
                }

                //if the input has to be saved to an intVar, then the program uses nextInt(). Else, it uses NextLine() to get the value to be stored
                if (declaredVars.get(index).getType().equals("int")) {
                    int in = input.nextInt();
                    declaredVars.get(index).setVal(in);
                } else {
                    String in = input.nextLine();
                    declaredVars.get(index).setVal(in);
                }
            }

            //deals with math operations
            else if (currentWord.equals("math")) {
                String ansType = scan.next();
                if (!ansType.equals("var")) {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": expected var, got "+ansType);
                }

                String ansName = validName(scan.next(), protectedWords, lineCounter);

                String equals = scan.next();
                if (!equals.equals("=")) {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": expected = , got "+equals+" instead.");
                }

                //gets the first operand
                int operand1 = getMathOperand(declaredVars, scan, lineCounter);

                //stores the type of operation to be performed
                String operation = scan.next();

                //gets the second operand
                int operand2 = getMathOperand(declaredVars, scan, lineCounter);

                int ans;

                //performs the indicated operation
                if (operation.equals("+")) {
                    ans = operand1+operand2;
                } else if (operation.equals("-")) {
                    ans = operand1-operand2;
                } else if (operation.equals("*")) {
                    ans = operand1*operand2;
                } else if (operation.equals("/")) {
                    if (operand2==0) {
                        throw new IllegalArgumentException("Error on line "+lineCounter+": cannot divide by 0");
                    }
                    ans = operand1 / operand2;
                } else if (operation.equals("%")) {
                    if (operand2==0) {
                        throw new IllegalArgumentException("Error on line "+lineCounter+": cannot take the modulus of 0");
                    }
                    ans = operand1%operand2;
                }
                else {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": operation not recognized");
                }

                //saves the result of the math operation to the indicated variable after checking that it has been declared
                int index = isDeclared(declaredVars, ansName);
                if (index!=-1) {
                    declaredVars.get(index).setVal(ans);
                } else {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": variable "+ansName+" not declared");
                }
            }

            //deals with loops
            else if (currentWord.equals("loop")) {
                //gets the name of the loop and checks that it is valid
                String loopName = validName(scan.next(), protectedWords, lineCounter);

                // gets the number of times the loop needs to run
                int timesToLoop = scan.nextInt()-1;

                //checks that the number of times to loop is more that 1 (keep in mind that 1 was subtracted from timesToLoop earier)
                if (timesToLoop<0) {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": must loop 1 or more times");
                }

                else{
                    currentLoops.add(loopName);

                    //recursively calls the compile() function in order to run the code block inside the loop repeatedly
                    for (int i = 0; i<timesToLoop; ++i) {
                        Scanner loopScan = new Scanner(processedIn);
                        bringToIndex(loopScan, lineCounter+1);
                        compile(loopScan, declaredVars, lineCounter+1, protectedWords, processedIn, loopName, true, currentLoops, currentIfs);
                    }
                }
            }

            //deals with ending loops
            else if (currentWord.equals("endloop")) {
                //gets the name of the loop that the user is requesting to end
                String name = scan.next();

                //if loops are closed properly, then the user should be requesting to close the latest loop that was opened
                if (name.equals(currentLoops.get(currentLoops.size()-1))) {
                    if (isInLoop) {
                        break;
                    } else {
                        currentLoops.remove(currentLoops.size()-1);
                    }
                }
                //needed for technical reasons involving the final loop of the loop cycle
                else if (loopEndName!=null) {
                    throw new IllegalArgumentException("Error on line "+(lineCounter+1)+": loops closed out of order");
                }
            }

            //deals with if statements
            else if (currentWord.equals("if")) {
                //gets the name of the if block and checks that it is valid
                String ifName = validName(scan.next(), protectedWords, lineCounter);

                currentIfs.add(ifName);

                //gets the first thing being compared in the comparison
                String val1 = getIfOperand(declaredVars, scan, lineCounter);

                //checks that '==' is used as opposed to anything else
                String equalSign = scan.next();
                if (!equalSign.equals("==")) {
                    throw new IllegalArgumentException("Error on line "+lineCounter+": == expected, got "+equalSign+" instead");
                }

                //gets the second thing being compared in the comparison
                String val2 = getIfOperand(declaredVars, scan, lineCounter);

                //if the comparison is true, then the program needs to know that it has to execute that block
                if (!val1.equals(val2)) {
                    currentIfs.set(0, "true");
                }
            }

//            endif is already handled at the beginning of the function, but this still needs to be here to avoid unwanted errors where there are none
            else if (currentWord.equals("endif")) {
//                does not need to do anything
            }

            //if the first word of any line is not one of the cases above, then the program throws an error
            else {
                throw new IllegalArgumentException("Error on line "+lineCounter+": Symbol "+ currentWord +" not recognized.");
            }

            ++lineCounter;

            //advances the scanner
            for (int i = 0; i<timesToAdvanceLine; ++i) {
                scan.nextLine();
            }
        }
    }

    //gets an operand from the conditional of an 'if' statement
    public static String getIfOperand(ArrayList<variable> declaredVars, Scanner scan, int lineCounter) {
        //checks that there is '(' at the beginning of the if operand
        String parenthesis = scan.next();
        if (!parenthesis.equals("(")) {
            throw new IllegalArgumentException("Error on line "+lineCounter+": expected '(', got "+parenthesis+"instead.");
        }

        String val = "";
        String type = scan.next();

        if (type.equals("raw")) {
            val = getRest(scan);
            return val;
        }
        //if the type of the thing in the if operand is a var, the program checks that the referenced variable is declared and initialized
        else if (type.equals("var")) {
            String varName = getRest(scan);

            int index = checkDeclaredAndInitialized(declaredVars, varName, lineCounter);
            if (declaredVars.get(index).getType().equals("int")) {
                val = ""+declaredVars.get(index).getIntVal();
            }
            // the type is a string
            else {

                val = declaredVars.get(index).getStringVal();
            }
            return val;
        } else {
            throw new IllegalArgumentException("Error on line "+lineCounter+": var or raw expected, got "+type+" instead");
        }
    }

    //for use in getIfOperand(); isolates the variable or raw value from one set of parenthesis
    private static String getRest(Scanner scan) {
        String val = "";
        String next = scan.next();
        while(!next.equals(")")) {
            val+=next;
            next = scan.next();
        }
        return val;
    }

    //gets one of the operands from a 'math' statement
    public static int getMathOperand(ArrayList<variable> declaredVars, Scanner scan, int lineCounter) {
        String type = scan.next();

        if (type.equals("var")) {
            String name = scan.next();

            int index = checkDeclaredAndInitialized(declaredVars, name, lineCounter);

            int val = declaredVars.get(index).getIntVal();
            return val;
        } else if (type.equals("raw")) {
            int val = scan.nextInt();
            return val;
        } else {
            throw new IllegalArgumentException("Error on line "+lineCounter+": invalid identifier: "+type);
        }
    }

    //checks if a given variable has been declared; if it has, its index in declaredVariables is returned; else, -1 is returned
    public static int isDeclared(ArrayList<variable> declaredVars, String s) {
        for (int i = 0; i < declaredVars.size(); ++i) {
            if (declaredVars.get(i).getName().equals(s)) {
                return i;
            }
        }
        return -1;
    }

    //checks if a given variable name has been both declared and initialized
    public static int checkDeclaredAndInitialized(ArrayList<variable> declaredVars, String name, int lineCounter) {
        int index = isDeclared(declaredVars, name);
        if (index == -1) {
            throw new IllegalArgumentException("Error on line "+lineCounter+": variable "+name+" not declared.");
        }

        if (!declaredVars.get(index).isAssigned()) {
            throw new IllegalArgumentException("Error on line "+lineCounter+": variable "+name+" not initialized.");
        }

        return index;
    }

    //checks if a given name is part of the protected words list
    public static String validName(String name, String[] protectedWords, int lineCounter) {
        for (String i : protectedWords) {
            if (i.equals(name)) {
                throw new IllegalArgumentException("Error on line "+lineCounter+": "+name+" is a protected keyword, cannot be used as a variable name");
            }
        }
        return name;
    }

    //takes in the main file writes it to a 'processed' file ('processed.txt'). It removes all the empty lines and adds dummy comment lines after 'in' and 'loop' statements to make the code function properly
    public static void processFile(File fin) throws FileNotFoundException {
        Scanner read = new Scanner(fin);
        try {
            FileWriter fout = new FileWriter("processed.txt");
            while (read.hasNextLine()) {
                String toRead = read.nextLine();
                if (!toRead.equals("")) {
                    fout.write(toRead + "\n");
                }
                Scanner line = new Scanner(toRead);
                String first = line.next();
                if (first.equals("loop")) {
                    fout.write("\\c placeholder\n");
                }
                Scanner scan = new Scanner(toRead);
                if (first.equals("in")) {
                    fout.write("\\c placement\n");
                }
                line.close();
                scan.close();
            }
            fout.write("\n");
            fout.close();
        } catch(IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
        read.close();
    }

    //takes a scanner which is at the top of whatever it is scanning and brings it down to a certain given line
    public static void bringToIndex(Scanner scan, int lineNumber) {
        for (int i = 0; i<lineNumber+1; ++i) {
            scan.nextLine();
        }
    }
}