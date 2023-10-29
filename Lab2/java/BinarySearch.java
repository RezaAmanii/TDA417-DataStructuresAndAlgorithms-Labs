
import java.io.IOException;

public class BinarySearch {

    public static int binarySearchFirst(SuffixArray sa, String value) {
        int[] index = sa.index;
        String text = sa.text;

        int result = -1;

        int high = index.length - 1;
        int low = 0;
        int mid;

        while (low <= high) {

            mid = (high + low) / 2;

            String suffix = text.substring(index[mid]);

            if (suffix.startsWith(value)) {
                result = mid;
                high = mid - 1;
            }
            else if (suffix.compareTo(value) > 0) {
                high = mid - 1;
            }
            else {
                low = mid + 1;
            }

        }

        return result;
    }




    public static void main(String[] args) throws IOException {
        SuffixSorter sorter = new Quicksort();
        SuffixArray sa = new SuffixArray();


        sa.setText("ABRACADABRA");
        sa.buildIndex(sorter);
        sa.checkIndex();
        sa.print("Suffix array", new int[]{0, sa.size()}, "   ");

        // Search for some strings, e.g.: "ABRA", "RAC", "RAD", "AA"
        String value = "ABRA";
        System.out.format("Searching for: '%s'%n", value);
        int i = binarySearchFirst(sa, value);
        if (i < 0) {
            System.out.format("--> String not found%n");
        } else {
            int pos = sa.index[i];
            System.out.format("--> String found at index: %d --> text position: %d%n", i, pos);
        }




/*
        // Next step is to search in a slightly larger text file, such as:
        sa.loadText("texts/bnc-medium.txt.gz");
        sa.buildIndex(sorter);
        System.out.println(binarySearchFirst(sa, "University"));
*/

        // Try, e.g., to search for the following strings:
        // "and", "ands", "\n\n", "zz", "zzzzz"

    }
}

