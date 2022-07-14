import java.lang.reflect.Array;
import java.util.*;

public class Kmeans {
    SimilarityMeasure measure;
    double[][] matrix;
    int[] clusterNamesIndex;
    List<Integer> centres = new ArrayList<>();
    int k;
    private static final Random random = new Random();
    public Kmeans(int k, double[][] matrix){
        this.matrix = matrix;
        clusterNamesIndex = new int[matrix.length];
        Arrays.fill(clusterNamesIndex,-1);
        this.k=k;
    }

    int[] getClusters(String mea, int maxIterations){

        if(mea.toLowerCase().equals("cosine"))
            measure = new CosineSimilarityMeasure();
        if(mea.toLowerCase().equals("cosine"))
            measure = new euclideanSimilarityMeasure();
        intializeCentroids(centres);
        int iterations = 0;
        int[] lastClustersAllocated = Arrays.copyOf(clusterNamesIndex,clusterNamesIndex.length);
        while(iterations!=maxIterations){
            iterations++;
            for(int i=0;i< matrix.length;i++){
                if(!centres.contains(i)) {
                    int nearestIndex = nearestNeighBour(i);
                    clusterNamesIndex[i] = nearestIndex;
                }
            }
            if(Arrays.equals(lastClustersAllocated,clusterNamesIndex))
                break;
            updateCentres(centres);
        }
        return clusterNamesIndex;
    }

    private void updateCentres(List<Integer> centres) {
        HashMap<Integer, Integer> countCentroids = new HashMap<>();
        for(int i=0;i< matrix.length;i++){
            if(!centres.contains(i)){

            }
        }
    }

    private int nearestNeighBour(int i) {
        double distance = Double.MAX_VALUE;
        int nearIndex = 0;
        for(int c : centres){
            double d= measure.distance(matrix[i], matrix[c]);
            if(distance>d){
                distance = d;
                nearIndex = c;
            }
        }
        return nearIndex;
    }

    void intializeCentroids( List<Integer> centres){
        while(centres.size()!=k){
            int index = random.nextInt(matrix.length);
            if(!centres.contains(index))
                centres.add(index);
        }
    }
}
