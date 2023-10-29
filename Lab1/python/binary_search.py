
###############################################################################
# Comparable types: only used for type checking.
# You don't have to understand this definition.

from abc import abstractmethod
from typing import Protocol, TypeVar, List

class ComparableProtocol(Protocol):
    """Protocol for annotating comparable types."""
    @abstractmethod
    def __lt__(self: 'Comparable', other: 'Comparable', /) -> bool: ...

Comparable = TypeVar('Comparable', bound=ComparableProtocol)


###############################################################################
# Different implementations of binary search.
#
# Common description of the below functions:
# - Precondition: `array` is sorted according to the natural order.
# - Precondition: all arguments are non-None (no need to check).
# - Required complexity: O(log(n)) comparisons where n is the length of `array`.


def containsIterative(array: List[Comparable], value: Comparable) -> bool:
    """Check if the array contains the given value.
    Iterative solution.
    
    Args:
        array: an array sorted according to the natural order of its elements.
        value: the value to search for.

    Returns:
        true if `value` is in `array`
    """

    #---------- TASK 1: Iterative version of binary search -------------------#
    # Hint: You probably need some auxiliary variables telling 
    # which part of the array you're looking at.

    raise NotImplementedError("TODO")
    #---------- END TASK 1 ---------------------------------------------------#


def containsRecursive(array: List[Comparable], value: Comparable) -> bool:
    """Check if the array contains the given value.
    Recursive solution.
    
    Args:
        array: an array sorted according to the natural order of its elements.
        value: the value to search for.

    Returns:
        true if `value` is in `array`
    """

    #---------- TASK 2: Recursive version of binary search -------------------#
    # Hint: you probably need a recursive helper function with some 
    # extra arguments telling which part of the array you're looking at.

    raise NotImplementedError("TODO")
    #---------- END TASK 2 ---------------------------------------------------#


def firstIndexOf(array: List[Comparable], value: Comparable) -> int:
    """Return the *first* position in the array whose element matches the given value.
    
    Args:
        array: an array sorted according to the natural order of its elements.
        value: the value to search for.

    Returns:
        the first position where `value` occurs in `array`, or -1 if it doesn't occur.
    """

    #---------- TASK 3: Binary search returning the first index --------------#
    # It's up to you if you want to implement an iterative or recursive version.

    raise NotImplementedError("TODO")
    #---------- END TASK 3 ---------------------------------------------------#


# Put your own tests here.

def main():
    integerTestArray = [1, 3, 5, 7, 9]

    assert containsIterative(integerTestArray, 4) == False
    assert containsIterative(integerTestArray, 7) == True

    assert containsRecursive(integerTestArray, 0) == False
    assert containsRecursive(integerTestArray, 9) == True

    stringTestArray = ["cat", "cat", "cat", "dog", "turtle", "turtle"]

    assert firstIndexOf(stringTestArray, "cat") == 0
    assert firstIndexOf(stringTestArray, "dog") == 3
    assert firstIndexOf(stringTestArray, "turtle") == 4
    assert firstIndexOf(stringTestArray, "zebra") == -1


if __name__ == '__main__':
    main()

