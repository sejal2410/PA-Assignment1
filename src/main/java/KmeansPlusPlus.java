import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KmeansPlusPlus extends Kmeans {

    public KmeansPlusPlus(int k, double[][] matrix) {
        super(k, matrix);
    }

    @Override
    void intializeCentroids(double[][] centres) {

            double[][] centroids = new double[k][matrix[0].length];
            List<Integer> index = new ArrayList<Integer>();

            int randonIndex = (int)(Math.random() * matrix.length);

            index.add(randonIndex);
            for(int i=0; i<matrix[0].length; i++) {
                centroids[0][i] = matrix[randonIndex][i];
            }

            for (int c=1; c<k; c++) {

                double[] dist = new double[matrix.length];
                int newCentroid = -1;
                double p = Math.random(), dSum = 0.0, cumulativeSum = 0.0;
                for (int i=0; i<matrix.length; i++) {
                    if (!index.contains(i)) {
                        double[] distances = new double[c];
                        for (int j=0; j<c; j++) {
                            distances[j] = measure.distance(matrix[i], centroids[j]);
                        }

                        int m = 0;
                        for (int k=0; k<c; k++) {
                            if (distances[k] < distances[m]) {
                                m = k;
                            }
                        }
                        dist[i] = distances[m] * distances[m];
                        dSum += dist[i];
                    }
                }

                int possibleCentre = 0;
                for (int s = 0; s < matrix.length * 2; s++) {
                    cumulativeSum += dist[possibleCentre] / dSum;
                    if (cumulativeSum >= p && !index.contains(possibleCentre)) {
                        newCentroid = possibleCentre;
                        index.add(newCentroid);
                        break;
                    }
                    possibleCentre = (possibleCentre+1)%dist.length;
                }

                for (int i=0; i<matrix[0].length; i++) {
                    centroids[c][i] = matrix[newCentroid][i];
                }
            }
            Collections.sort(index);

            for (int i = 0; i<k; i++) {
                for (int j=0; j<matrix[0].length; j++) {
                    centroids[i][j] = matrix[index.get(i)][j];
                }
            }
        }
    }

