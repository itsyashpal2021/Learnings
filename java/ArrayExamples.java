import java.util.Arrays;
import java.util.Comparator;

public class ArrayExamples {

    public static void main(String[] args) {

        /*
         ---------------------------------------------------------
         1. WHAT IS AN ARRAY?
         ---------------------------------------------------------

         An array is a fixed-size data structure used to store
         multiple values of the same type in contiguous memory.

         Example use cases:
         - storing list of numbers
         - storing marks of students
         - storing fixed-size collections
         - when fast index access is needed

         Characteristics:
         - fixed size
         - same data type
         - index based (0...n-1)
        */


        /*
         ---------------------------------------------------------
         2. JAVA ARRAY SYNTAX
         ---------------------------------------------------------
        */

        // Declaring an array
        int[] numbers;

        // Initializing an array with size
        numbers = new int[5]; // array of size 5

        // Assigning values
        numbers[0] = 10;
        numbers[1] = 20;
        System.out.println("Example1 : " + Arrays.toString(numbers));


        // Initialization with values directly
        int[] values = {5, 2, 9, 1, 7};
        System.out.println("Example2 : " + Arrays.toString(values));


        // Another syntax
        int[] values2 = new int[]{3, 8, 6, 4};
        System.out.println("Example3 : " + Arrays.toString(values2));


        // Using variables
        int i = 10;
        int j = 20;
        int[] variableArray = {i, j};
        System.out.println("Array with variable values : " + Arrays.toString(variableArray));

        // in place new array
        i = 55;
        j = 60;
        System.out.println("Array created in place with variable values : " + Arrays.toString(new int[]{i, j}));

        /*
         ---------------------------------------------------------
         3. TRAVERSING AN ARRAY
         ---------------------------------------------------------
        */

        int[] arr = {4, 8, 1, 6, 3};

        // Using traditional for loop
        System.out.println("\nTraversal using for loop:");

        for (int index = 0; index < arr.length; index++) {
            System.out.println("Index " + index + " Value " + arr[index]);
        }


        // Using enhanced for loop (for-each)
        System.out.println("\nTraversal using enhanced for loop:");

        for (int value : arr) {
            System.out.println(value);
        }


        /*
         ---------------------------------------------------------
         4. SORTING AN ARRAY
         ---------------------------------------------------------
        */

        int[] numbersToSort = {9, 4, 2, 7, 1};

        // Default sorting (ascending)
        Arrays.sort(numbersToSort);

        System.out.println("\nSorted array (ascending):");
        System.out.println(Arrays.toString(numbersToSort));


        /*
         ---------------------------------------------------------
         CUSTOM SORTING USING COMPARATOR
         ---------------------------------------------------------

         Primitive arrays cannot use custom comparators directly.
         We must use wrapper types (Integer instead of int).

         Comparator compare function return -ve if a comes before b, +ve otherwise and 0 for equal.
        */

        Integer[] nums = {9, 4, 2, 7, 1};

        // Sort descending using custom comparator
        Arrays.sort(nums, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return b - a; // descending order
            }
        });

        System.out.println("\nSorted array (descending using comparator):");
        System.out.println(Arrays.toString(nums));


        /*
         ---------------------------------------------------------
         ALTERNATIVE: USING LAMBDA (Java 8+)
         ---------------------------------------------------------
        */

        Integer[] nums2 = {5, 1, 9, 3, 7};

        Arrays.sort(nums2, (a, b) -> b - a);

        System.out.println("\nSorted using lambda comparator:");
        System.out.println(Arrays.toString(nums2));

    }
}