F-- Documentation

Introduction:
    This language is called F--. It was developed for Mr. Jan's computer science class. It is somewhat modeled after Python and is intentionally kept
    simple and easy-to-understand.

Basic Rules:
 - Whenever referring to values, the keywords 'var' or 'raw' must precede them; if the value entered is a variable, you must use 'var' and if the value is a raw value, meaning that the value is not associated with a variable, 'raw' must be used.
    - One important exception to the above rule: 'raw' does not need to be used in loop headers.
 - Make sure that there are no empty lines in the code. The program will not compile properly and the error messages will have the wrong line numbers if there are spaces.
 - Put one space in between all words, including '==', '(', and ')'. This is very important; the compiler will not work if there are no spaces in between all words and the aforementioned symbols
 - There are no semicolons
 - Words in the Protected Words list may not be used as names of if and loop blocks
 - All user-written code should be written in 'main.txt'

Protected Words:
 - 'new'
 - 'int'
 - 'string'
 - '\c'
 - 'var'
 - 'raw'
 - 'out'
 - 'in'
 - 'if'
 - 'endif'
 - 'loop'
 - 'endloop'

Data Types:
 - int
    - Handles integers from -2^31 -> 2^31-1
 - string
    - Handles string values
    - There are no quotation marks necessary when dealing with strings; just type the string value.


Declaring A Variable:
 - use the 'new' keyword, then either 'int' or 'string', depending on the type of variable you desire, then the name of the variable
 - examples:
    new int x
    new string y
 - NOTE: if the name entered is already being used in a previously declared variable, then the previously declared one will be erased and deleted
 - Note: You cannot declare and initialize variables in the same line; they must be done in different lines

Initializing/Changing The Value Of A Variable:
 - write 'var', then the name of the variable, then '='. Note that this a single equals sign and not a double equals sign. Then put that value that you want the variable to hold.
 - Examples:
    var x = 4
    var y = hello
 - NOTE: The variable mentioned must be declared previously or the compiler will raise an error.
 - NOTE: For integer variables, only put integers, or the compiler will not work.

Input:
 - write 'in', then 'var', then the name of the variable that will be taking the input. In the command prompt, the user will be able to input a value and it will be saved into the variable mentioned.
 - Examples:
    in var x
        NOTE: the user will be able to enter an input through the command prompt before the program resumes.
    in var y
        NOTE: the user will be able to enter an input through the command prompt before the program resumes.
 - NOTE: The variable mentioned must be declared previously or the compiler will raise an error.
 - NOTE: If the variable mentioned is an integer variable, then the user must enter an integer into the prompt or the compiler will not work properly.
 - NOTE: All variables mentioned must have been previously initialized
Output:
 - write 'out' then 'var' or 'raw', then what needs to be outputted.
 - Examples:
    out var x
        (assuming x is an integer variable that contains the integer '10' as its value)
        printstream: 10\n
    out var y
        (assuming y is a string variable that contains the string 'hello!' as its value)
        printstream: hello!\n
    out raw Hello World!
        printstream: Hello World!\n

 - NOTE: This print statement will contain a new line at the end.
 - NOTE: if you wrote 'var', then the compiler will check that the variable passed has been declared and initialized. If it has not been both declared and initialized, then the compiler will raise an error.
 - NOTE: if you wrote 'raw' then the compiler will print to the screen whatever comes after 'raw'.

Math:
 - write 'math', then 'var', then the name of the variable that the result of the math operation is being stored to. Then write '=', then the two operands with '+', '-', '*', or '/' between them.
 - the operands are in the form 'raw' or 'var' and then the raw value or the variable name.
 - Example:
    math var x = var x + raw 1
 - NOTE: All variables mentioned must have been previously initialized
 - NOTE: All variables used as operands must have been previously declared
 - NOTE: There can only be one operation in each line.
 - NOTE: All variables and all raw values must be integers or the compiler will not work properly.
 - NOTE: Division in this language is similar to java division; any decimal places will be truncated.
    math var x = raw 5 / raw 2
    out var x
        printstream: 2
 - NOTE: math operations supported: addition, subtraction, multiplication, division, and modulation.

If Statements:
 - write 'if', then a name for the if block, then the two statements being compared. For each statement, write '(', then 'var' or 'raw', then ')'. In between the statements, write '=='. Write whatever code will be executed if the conditional is true, and then write 'endif' and the if block header name.
 - Example;
     if ifName ( var x ) == ( raw 10 )
     out raw x is equals to 10!
     endif ifName
 - NOTE: For the purposes of evaluating statements, string and integers are equivalent.
    Example:
        10 == '10' <- 10 the integer is equal to 10 the string.
 - NOTE: there are no 'not equal' or 'else' operators
 - NOTE: nested 'if' statements must be closed in order or the compiler will raise an error.
 - NOTE: All variables mentioned must have been previously initialized

Loops:
 - write 'loop', then a name for the loop, then the number of times the block of code must be looped. Then write whatever code must be executed, then write 'endloop' and the name of the loop.
 - Example:
    loop loopName 5
    out raw Hello!
    endloop loopName
        printstream: Hello!\nHello!\nHello!\nHello!\nHello!\n
 - NOTE: the block must be looped 1 or more times or the compiler will raise an error.

Comments:
 - write '\c' at the beginning of the sentence, and write whatever you want after it. The compiler will skip this line of code.
 - Examples:
    \c This programming language is called F--!
    \c out raw This code will not be executed!

 - NOTE: This is a single line comment; unless the next line is commented as well, it will be executed.
 - NOTE: The slash used is a forward slash '\', not a backslash '/'.

Happy coding!