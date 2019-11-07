import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.Math;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

public class MyBigIntegers {
    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
    static String ResultsFolderPath = "/home/matt/Results/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;
    static int iterations = 10;


    public static void main(String[] args) {
        ThreadCpuStopWatch stopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials
        long elapsedTime = 0;
        System.gc();
        stopwatch.start(); // Start timer in nano secs

        // To open a file to write to
        try {
            resultsFile = new FileWriter(ResultsFolderPath + "lab6");
            resultsWriter = new PrintWriter(resultsFile);
        } catch(Exception e) {
            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+"lab6");
            return;
        }

        resultsWriter.println("#Trial      AvgTime"); // # marks a comment in gnuplot data
        resultsWriter.flush();


        for (int trial = 0; trial < 3; ++trial) {
            for (int i = 0; i < iterations; ++i) {
                //System.out.println("Trial: " + i);
                // Declare variables
                int ranNum, len1, len2, size = 0;
                String result;
                ranNum = createRandom();        // Create a random number for number of digits
                int[] num1 = new int[ranNum];   // Use created random number to initialize arrays
                ranNum = createRandom();
                int[] num2 = new int[ranNum];
                len1 = num1.length;
                len2 = num2.length;

                // Create 2 big integers randomly
                for (int j = 0; j < 2; ++j) {
                    if (j == 0) {
                        num1 = createBigInt(num1, len1);
                    } else {
                        num2 = createBigInt(num2, len2);
                    }
                }

                /* Functions for adding and multiplying */
                result = Plus(num1, num2, len1, len2);
                //result = Times(num1, num2, len1, len2);

                /* result = create(num1, num2); */


                // Print out the string
                toString(result);
            }
            elapsedTime = stopwatch.elapsedTime();
            double averageTime = (double) elapsedTime / (double) iterations;
            System.out.printf("%-5f \n", averageTime);

            resultsWriter.printf("%-5d %20f \n", trial, averageTime);
            resultsWriter.flush();
        }
    }

    static void toString (String results) {
        System.out.println("The answer is: " + results);
    }

    static String create (String num1, String num2) {
        num1 = "55555";
        num2 = "22222222222222222222";
        String result, mul;
        int len1 = num1.length();
        int len2 = num2.length();
        int[] A = new int[len1];
        int[] B = new int[len2];
        for (int i = 0; i < len1; ++i) {
            A[i] = num1.charAt(i)-48;
            // System.out.println("char: " + A[i]);
        }
        for (int i = 0; i < len2; ++i) {
            B[i] = num2.charAt(i)-48;
        }

        // Functions for adding and multiplying
        result = Plus (A, B, len1, len2);
        //result = Times (A, B, len1, len2);

        return result;

        // System.out.println("The sum is: " + sum);
        // System.out.println("The mul is: " + mul);
    }

    static String Times(int[] A, int[] B, int lenA, int lenB) {
        String result;
        int sum, multiply = 0, smaller = 0, larger = 0, i, j;
        long elapsedTime = 0;

        if (lenA >= lenB) {
            larger = lenA;
            smaller = lenB;
        }
        else {
            larger = lenB;
            smaller = lenA;
        }

        int[] mul = new int[lenA+lenB];

        if (lenA >= lenB) {
            for (i = larger-1; i >= 0; --i) {
                for (j = smaller-1; j >= 0; --j) {
                    multiply = A[i] * B[j];
                    sum = mul[i + j + 1] + multiply;
                    mul[i + j] += sum / 10;
                    mul[i + j + 1] = sum % 10;
                }
            }

        }
        else {
            for (i = larger-1; i >= 0; --i) {
                for (j = smaller-1; j >= 0; --j) {
                    multiply = B[i] * A[j];
                    sum = mul[i + j + 1] + multiply;
                    mul[i + j] += sum / 10;
                    mul[i + j + 1] = sum % 10;
                }
            }
        }

        //System.out.print(Arrays.toString(A));
        //System.out.print("*");
        //System.out.println(Arrays.toString(B));

        result = Arrays.toString(mul);

        return result;
    }

    static String Plus (int[] A, int[] B, int lenA, int lenB) {
        int add = 0, carry = 0, larger = 0, i, j, k;
        long elapsedTime = 0;
        String result;

        if (lenA >= lenB)
            larger = lenA;
        else
            larger = lenB;

        int[] sum = new int[larger+1];

        if (lenA >= lenB) {
            i = lenA-1;
            j = lenB-1;
            k = lenA;

            while (j >= 0) {
                add = A[i] + B[j] + carry;
                sum[k] = (add % 10);
                carry = add / 10;
                i--;
                j--;
                k--;
            }

            while (i >= 0) {
                add = A[i] + carry;
                sum[k] = (add % 10);
                carry = add / 10;
                i--;
                k--;
            }
        }

        // lenB > lenA
        else {
            i = lenB-1;
            j = lenA-1;
            k = lenB;

            while (j >= 0) {
                add = A[j] + B[i] + carry;
                sum[k] = (add % 10);
                // System.out.print("sum[k] = " + sum[k]);
                carry = add / 10;
                // System.out.println("carry: " + carry);
                i--;
                j--;
                k--;
            }

            while (i >= 0) {
                add = B[i] + carry;
                // System.out.println("sum: " + B[i]);
                sum[k] = (add % 10);
                carry = add / 10;
                i--;
                k--;
            }
        }
        if (carry == 1)
            sum[0] = 1;


        System.out.print(Arrays.toString(A));
        System.out.print("+");
        System.out.println(Arrays.toString(B));

        result = Arrays.toString(sum);

        return result;
    }

    public static int createRandom() {
        int number = (int)(Math.random()*20 + 1);
        return number;
    }

    public static int[] createBigInt(int[] A, int size) {
        int max = 9, min = 0;
        for (int i = 0; i < size; ++i) {
            Random digit = new Random();
            A[i] = digit.nextInt((max-min)+1)+min;

            //A[i] = (int)(Math.random()*10);
        }
        //System.out.println(Arrays.toString(A));
        return A;
    }
}

