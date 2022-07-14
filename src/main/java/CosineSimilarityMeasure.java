public class CosineSimilarityMeasure extends SimilarityMeasure {

    @Override
    public double distance(double[] x, double[] y) {
        double numerator = 0.0;
        double den1 = 0.0;
        double den2 = 0.0;

        for (int i=0; i<x.length; i++) {
            numerator += x[i] * y[i];
            den1 += x[i]*x[i];
            den2 += y[i]*y[i];
        }
        return numerator / (Math.sqrt(den1) * Math.sqrt(den2));
    }
}
