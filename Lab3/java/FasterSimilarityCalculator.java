import java.nio.file.Path;
import java.security.Policy;
import java.util.ArrayList;


public class FasterSimilarityCalculator extends SimilarityCalculator {

    /** 
     * Phase 2: Build index of n-grams.
     * Uses the instance varable `fileNgrams`.
     * Stores the result in the instance variable `ngramIndex`.
     */
    @Override
    public void buildNgramIndex() {
        //---------- TASK 2a: Build n-gram index ----------------------------//
        // Note: You can use a ProgressBar in your outermost loop if you want.
        // See in `similarityCalculator.java` how it is used.


        // For each path inside fileNgrams
        for (Path path: this.fileNgrams) {

           // Get the set of ngrams inside the path
           SimpleSet<Ngram> ngrams = this.fileNgrams.get(path);

           // For each one of these ngrams
           for (Ngram ngram: ngrams) {

            // Add path to set of paths given ngram key
            this.ngramIndex.get(ngram).add(path);

           }

       }

        //throw new UnsupportedOperationException();
        //---------- END TASK 2a --------------------------------------------//
    }

    /**
     * Phase 3: Count how many n-grams each pair of files has in common.
     * This version should use the `ngramIndex` to make this function much more efficient.
     * Stores the result in the instance variable `intersections`.
     */
    @Override
    public void computeIntersections() {
        //---------- TASK 2b: Compute n-gram intersections ------------------//
        // Note 1: Intersection is a reflexive operation, i.e., A ∩ B == B ∩ A.
        // This means that you restrict yourself to only compute the intersection 
        // for path pairs (p,q) where p < q (in Java: p.compareTo(q) < 0).

        // Note 2: You can use a ProgressBar in your outermost loop if you want.
        // See in `similarityCalculator.java` how it is used.


        for (Ngram ngram: this.ngramIndex) {

            SimpleSet<Path> pathSet = this.ngramIndex.get(ngram);

            for (Path path1: pathSet) {
                for (Path path2: pathSet) {

                    if (path1.compareTo(path2) < 0) {
                        PathPair pair = new PathPair(path1, path2);
                        this.intersections.get(pair).add(ngram);

                    }

                }

            }

        }

        // TODO: Replace these lines with your solution! 
        //throw new UnsupportedOperationException();
        //---------- END TASK 2b --------------------------------------------//
    }

    /*
    public void computeIntersections() {
        //---------- TASK 2b: Compute n-gram intersections ------------------//
        // Note 1: Intersection is a reflexive operation, i.e., A ∩ B == B ∩ A.
        // This means that you restrict yourself to only compute the intersection
        // for path pairs (p,q) where p < q (in Java: p.compareTo(q) < 0).

        // Note 2: You can use a ProgressBar in your outermost loop if you want.
        // See in `similarityCalculator.java` how it is used.
        PathPair pair;
        for(Ngram ngram: this.ngramIndex) {
            SimpleSet<Path> pathSet = this.ngramIndex.get(ngram);
            for(Path path1: pathSet){
                for(Path path2: pathSet){
                    if(path1.compareTo(path2) <= 0){
                        pair = new PathPair(path1, path2);
                        SimpleSet<Ngram> intersection = this.intersections.get(pair);
                        intersection.add(ngram);
                    }
                }
            }
        }
        //throw new UnsupportedOperationException();
        //---------- END TASK 2b --------------------------------------------//
    }
     */

}

