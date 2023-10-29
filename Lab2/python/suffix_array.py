
import gzip
import pickle
import random
from pathlib import Path
from typing import List, Union, Iterable

from progress_bar import ProgressBar
from suffix_sorter import SuffixSorter

class SuffixArray:
    text: str
    index: List[int]

    textFile : Path
    indexFile : Path

    # Internal constants.
    INDEX_SUFFIX = ".ix"
    ENCODING = "utf-8"

    def setText(self, text: str):
        self.text = text
        self.textFile = self.indexFile = self.index = None  # type: ignore

    def generateRandomText(self, size: int, alphabet: str):
        chars = random.choices(alphabet, k=size)
        self.setText("".join(chars))

    def loadText(self, textFile: Union[Path,str]):
        self.textFile = Path(textFile)
        self.indexFile = Path(str(self.textFile) + self.INDEX_SUFFIX)
        openFile = gzip.open if self.textFile.suffix == ".gz" else open
        with openFile(self.textFile, "rt", encoding=self.ENCODING) as IN:  # type: ignore
            self.text = IN.read()
        self.index = None  # type: ignore

    def size(self) -> int:
        return len(self.text)

    def compareSuffixes(self, suffix1: int, suffix2: int) -> int:
        if suffix1 == suffix2:
            return 0
        text = self.text
        end = len(text)
        while suffix1 < end and suffix2 < end:
            char1 = text[suffix1]
            char2 = text[suffix2]
            if char1 != char2:
                return -1 if char1 < char2 else 1
            suffix1 += 1
            suffix2 += 1
        return -1 if suffix1 > suffix2 else 1

    def linearSearch(self, value: str) -> Iterable[int]:
        for start in range(self.size() - len(value)):
            end = start + len(value)
            if value == self.text[start : end]:
                yield start
        """
        # The following is too fast because str.find is implemented in C using a very good algorithm.
        # So if we use this one if will be unfair to the rest of our code which is implemented in Python.
        pos = self.text.find(value, 0)
        while pos >= 0:
            yield pos
            pos = self.text.find(value, pos+1)
        """

    def binarySearch(self, value: str) -> Iterable[int]:
        assert self.index, "Index is not initialised!"
        from binary_search import binarySearchFirst
        first = binarySearchFirst(self, value)
        if first < 0: 
            return
        for i in range(first, self.size()):
            start = self.index[i]
            end = start + len(value)
            if value == self.text[start : end]:
                yield start
            else:
                break

    def swap(self, i: int, j: int):
        self.index[i], self.index[j] = self.index[j], self.index[i]

    def buildIndex(self, sorter: SuffixSorter):
        self.index = list(range(len(self.text)))
        sorter.sort(self)

    def saveIndex(self):
        with open(self.indexFile, "wb") as OUT:
            pickle.dump(self.index, OUT)

    def loadIndex(self):
        with open(self.indexFile, "rb") as IN:
            self.index = pickle.load(IN)

    def checkIndex(self):
        left = self.index[0]
        for i in ProgressBar(range(1, self.size()), description="Checking index"):
            right = self.index[i]
            assert self.compareSuffixes(left, right) < 0, (
                f'Ordering error in position {i}:'
                f' {left}"{self.text[left : left+10]}..."'
                f' > {right}"{self.text[right : right+10]}..."'
            )
            left = right


    def print(self, header: str, breakpoints: List[int] = [], 
              indicators: str = "  ", context: int = 3, maxSuffix: int = 40):
        if not breakpoints:
            breakpoints = [0, self.size()]
        digits = max(3, len(str(self.size())))

        print(f"--- {header}", "-" * (75-len(header)))
        print(f"{'index':>{digits+3}s}{'textpos':>{digits+6}s}      suffix")
        dotdotdot = f"{'...':>{digits+2}s}{'...':>{digits+6}s}"

        endRange = 0
        for k in breakpoints:
            startRange = k - context
            if endRange < startRange - 1:
                print(dotdotdot)
            else:
                startRange = endRange
            endRange = k + context
            for i in range(startRange, endRange):
                if 0 <= i < self.size():
                    ind = indicators[0]
                    for bp in range(len(breakpoints)):
                        if i >= breakpoints[bp]: 
                            ind = indicators[bp+1]
                    suffixPos = self.index[i]
                    suffixString = (
                        self.text[suffixPos : suffixPos + maxSuffix] + "..."
                        if suffixPos + maxSuffix <= self.size() else
                        self.text[suffixPos :]
                    )
                    print(f"{ind} {i:{digits}d}  --> {suffixPos:{digits}d}  -->  {suffixString}")
        if endRange < self.size():
            print(dotdotdot)
        print("-" * 80)
        print()


