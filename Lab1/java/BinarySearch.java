
/**
 * Different implementations of binary search.
 * 
 * Common description of the below functions:
 * - Precondition: `array` is sorted according to the natural order.
 * - Precondition: all arguments are non-null (no need to check).
 * - Required complexity: O(log(n)) comparisons where n is the length of `array`.
*/
public class BinarySearch {

    /**
     * Check if the array contains the given value.
     * Iterative solution.
     * 
     * @param array an array sorted according to the natural order of its elements.
     * @param value the value to search for.
     * @return true if `value` is in `array`.
     */
    public static<V extends Comparable<? super V>> boolean containsIterative(V[] array, V value) {
        //---------- TASK 1: Iterative version of binary search -------------------//

        // Auxiliary variables
        int high = array.length - 1;
        int low = 0;


        while (high >= low){
            int middle  = low + (high - low) / 2;
            int result = value.compareTo(array[middle]);


            // if the value is the same as the number we are searching for
            if(result == 0){
                return true;
            }

            // if the value is smaller than the middle number move to the left
            else if(result < 0){
                high = middle - 1;
            }

            // if the value is bigger than the middle number move to the right
            else if(result > 0){
                low = middle + 1;
            }
        }

        // if the value doesn't exist in the array
        return false;


        // Hint: you probably need some auxiliary variables telling which part 
        // of the array you're looking at.
        //---------- END TASK 1 ---------------------------------------------------//
    }

    /**
     * Check if the array contains the given value.
     * Recursive solution.
     * 
     * @param array an array sorted according to the natural order of its elements.
     * @param value the value to search for.
     * @return true if `value` is in `array`.
     *
     */

    private static <V extends Comparable<? super V>> boolean binarySearch(V[] array, int low, int high, V value) {

        if (low > high) {
            return false;
        }

        int middle = low + (high - low) / 2;
        int result = value.compareTo(array[middle]);

        if(result == 0){
            return true;
        }
        else if (result < 0) {
            return binarySearch(array, low, middle - 1, value);
        } else if (result > 0) {
            return binarySearch(array, middle + 1, high, value);
        } else {
            return false;
        }
    }


    public static<V extends Comparable<? super V>> boolean containsRecursive(V[] array, V value) {
        //---------- TASK 2: Recursive version of binary search -------------------//
        // Hint: you need a recursive helper function with some extra
        // arguments telling which part of the array you're looking at.

        return binarySearch(array, 0, array.length - 1, value);

        //throw new UnsupportedOperationException("TODO");
        //---------- END TASK 2 ---------------------------------------------------//
    }

    /**
     * Return the *first* position in the array whose element matches the given value.
     * 
     * @param array an array sorted according to the natural order of its elements.
     * @param value the value to search for.
     * @return the first position where `value` occurs in `array`, or -1 if it doesn't occur.
     */
    public static<V extends Comparable<? super V>> int firstIndexOf(V[] array, V value) {
        //---------- TASK 3: Binary search returning the first index --------------//

        int low = 0;
        int high = array.length - 1;
        int result = -1;


        while (high >= low){
            int middle = low + (high - low) /2;
            int comparisionResult = value.compareTo(array[middle]);

            if(comparisionResult == 0){
                result = middle;
                high = middle - 1;
            }
            else if(comparisionResult > 0){
                low = middle + 1;
            } else{
                high = middle - 1;
            }
        }

        return result;


        // It's up to you if you want to use an iterative or recursive version.
        //throw new UnsupportedOperationException("TODO");
        //---------- END TASK 3 ---------------------------------------------------//
    }

    // Put your own tests here.

    public static void main(String[] args) {
        Integer[] integer_test_array = {1, 3, 5, 7, 9};



        assert containsIterative(integer_test_array, 4) == false;
        assert containsIterative(integer_test_array, 7) == true;

        assert containsRecursive(integer_test_array, 0) == false;
        assert containsRecursive(integer_test_array, 9) == true;

        String[] string_test_array = {"cat", "cat", "cat", "dog", "turtle", "turtle"};

        assert firstIndexOf(string_test_array, "cat") == 0;
        assert firstIndexOf(string_test_array, "dog") == 3;
        assert firstIndexOf(string_test_array, "turtle") == 4;
        assert firstIndexOf(string_test_array, "zebra") == -1;
    }

}

