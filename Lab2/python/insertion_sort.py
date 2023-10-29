
from suffix_array import SuffixArray
from suffix_sorter import SuffixSorter
from progress_bar import ProgressBar


class InsertionSort(SuffixSorter):

    def sort(self, sa: SuffixArray):
        index = sa.index
        swap = sa.swap

        for i in ProgressBar(range(len(index)), description="Insertion sorting"):
            #---------- TASK 3a: Insertion sort --------------------------#
            # TODO: Replace these lines with your solution!
            raise NotImplementedError()
            #---------- END TASK 3 -------------------------------------------#

            if self.debug:
                # When debugging, print an excerpt of the suffix array.
                sa.print(f"i = {i}", [0, i+1], " * ")


def main():
    sorter: SuffixSorter = InsertionSort()
    sa: SuffixArray = SuffixArray()

    # Run this for debugging.
    sorter.setDebugging(True)
    sa.setText("ABRACADABRA")
    sa.buildIndex(sorter)
    sa.checkIndex()
    sa.print("ABRACADABRA")

    """
    # Some example performance tests.
    # Wait with these until you're pretty certain that your code works.
    sorter.setDebugging(False);
    alphabet = "ABCD"
    for k in range(1, 6):
        size = k * 1_000
        sa.generateRandomText(size, alphabet)
        sa.buildIndex(sorter)
        sa.checkIndex()
        sa.print(f"size: {size:,d}, alphabet: '{alphabet}'")
    """

    # What happens if you try different alphabet sizes?
    # (E.g., smaller ("AB") or larger ("ABC....XYZ")

    # What happens if you use only "A" as alphabet?
    # (Hint: try much smaller test sizes)


if __name__ == "__main__":
    main()

