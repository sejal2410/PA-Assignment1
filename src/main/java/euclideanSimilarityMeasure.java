public class euclideanSimilarityMeasure extends SimilarityMeasure{

    @Override
    public double distance(double[] x, double[] y) {

        double sum = 0.0;

        for (int i=0; i<x.length; i++) {
            sum = sum + Math.pow(x[i] - y[i],2);
        }
        return Math.sqrt(sum);
    }
}
