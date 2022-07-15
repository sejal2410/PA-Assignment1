import java.lang.reflect.Array;
import java.util.*;

public class Kmeans {
    SimilarityMeasure measure;
    double[][] matrix;
    int[] clusterNamesIndex;
    double[][]  centres;
    int[] intialCentres ;
    int k;
    private static final Random random = new Random();
    public Kmeans(int k, double[][] matrix){
        this.matrix = matrix;
        intialCentres = new int[k];
        clusterNamesIndex = new int[matrix.length];
        centres = new double[k][matrix[0].length];
        Arrays.fill(clusterNamesIndex,-1);
        this.k=k;
    }
    int[] centroidsLabels(int[] documentFolderMapping){
        int[] mapping = new int[k];
        for(int i=0;i<k;i++)
            mapping[i] = documentFolderMapping[intialCentres[i]];
        return mapping;
    }
    int[] getClusters(String mea, int maxIterations){

        if(mea.toLowerCase().equals("cosine"))
            measure = new CosineSimilarityMeasure();
        else
            measure = new euclideanSimilarityMeasure();
        intializeCentroids(centres);
        int iterations = 0;
        double[][] lastClustersAllocated = new double[k][matrix[0].length];
        for (int i=0; i<centres.length; i++) {
            for (int j=0; j<centres[0].length; j++) {
                lastClustersAllocated[i][j]=centres[i][j];
            }
        }
        while(iterations!=maxIterations){
            iterations++;
            for (int i=0; i<centres.length; i++) {
                for (int j=0; j<centres[0].length; j++) {
                    lastClustersAllocated[i][j]=centres[i][j];
                }
            }
            for(int i=0;i< matrix.length;i++){
                //if(!centres.contains(i)) {
                int nearestIndex = nearestNeighBour(i);
                clusterNamesIndex[i] = nearestIndex;

            }
            updateCentres();
            if(equalCentres(lastClustersAllocated,centres))
                break;

        }
        return clusterNamesIndex;
    }

    boolean equalCentres(double[][] lastClustersAllocated, double[][] centres){
        for (int i=0; i<centres.length; i++) {
            for (int j=0; j<centres[0].length; j++) {
                if(lastClustersAllocated[i][j]!=centres[i][j])
                    return false;
            }
        }
        return true;
    }
    private void updateCentres() {
        for(int i=0;i<centres.length;i++)
            for(int j=0;j<centres[0].length;j++)
                centres[i][j]=0;
        int[] count = new int[matrix.length];
        for (int i=0; i<matrix.length; i++) {
            int cl = clusterNamesIndex[i];
            for (int j=0; j<matrix[0].length; j++) {
                centres[cl][j] += matrix[i][j];
            }
            count[cl]++;
        }
        for (int i=0; i<k; i++) {
            for (int j=0; j<matrix[0].length; j++) {
                centres[i][j] /= count[i];
            }
        }
    }

    private int nearestNeighBour(int j) {
        double distance = Double.MAX_VALUE;
        int nearIndex = 0;
        for(int i=0;i<k;i++){

            double d= measure.distance(matrix[j], centres[i]);
            if(distance>d){
                distance = d;
                nearIndex = i;
            }
        }
        return nearIndex;
    }

    void intializeCentroids(double[][] centres){
        HashSet<Integer> set = new HashSet<>( Arrays.asList(1,2,3,4,6,7,8,9,10,11,12,14,15,16,17,18,19,20,21,22));
        int i=0;
        while(i!=k){
            int index = random.nextInt(matrix.length);
            if(!set.contains(index)) {
                set.add(index);
                for(int j=0;j< matrix[0].length;j++)
                    centres[i][j] = matrix[index][j];
                intialCentres[i] = index;
                i++;
                clusterNamesIndex[index] = index;
                System.out.print(index+"    ");
            }
        }
        System.out.println();
    }

}