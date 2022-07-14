import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import org.math.plot.*;

public class PCA {

    double[][] matrix;
    int dimension;
    PCA (double[][] matrix, int dimension) {
        this.dimension = dimension;
        this.matrix = matrix;
    }

    @SuppressWarnings("unused")
    public double[][] reduceDimensions() {

        Matrix M = new Matrix(matrix);
        Matrix A = M.transpose();

        SingularValueDecomposition s = A.svd();
        Matrix S = s.getS().transpose();
        Matrix V = s.getU().transpose();
        Matrix U = s.getV().transpose();
        Matrix USigma = U.times(S);

        double[][] projected = USigma.transpose().getArray();

        double[][] reduced = new double[projected.length][dimension];
        for (int i=0; i<projected.length; i++) {
            for (int j=0; j<dimension; j++) {
                reduced[i][j] = projected[i][j];
            }
        }
        return reduced;
    }

    public void visualize(double[][] reduced_matrix, int[] labels, int k, String name) throws IOException {

        HashMap<Integer, String> legend_map = new HashMap<>();
        legend_map.put(0, "civil_aeronautics");
        legend_map.put(1, "hoof_mouth_disease");
        legend_map.put(2, "mortgage_rate");
        List<List<Double>> x_list = new ArrayList<List<Double>>();
        List<List<Double>> y_list = new ArrayList<List<Double>>();

        for (int i = 0; i<k; i++) {
            x_list.add(new ArrayList<>());
            y_list.add(new ArrayList<>());
        }

        for (int i = 0; i<labels.length; i++) {
            x_list.get(labels[i]).add(reduced_matrix[i][0]);
            y_list.get(labels[i]).add(reduced_matrix[i][1]);
        }

        Plot2DPanel plot = new Plot2DPanel();
        plot.setSize(500, 500);

        for (int i = 0; i<k; i++) {
            double[] x = new double[x_list.get(i).size()];
            double[] y = new double[y_list.get(i).size()];
            for (int j = 0; j<x.length; j++) {
                x[j] = x_list.get(i).get(j);
                y[j] = y_list.get(i).get(j);
            }

            plot.addScatterPlot(legend_map.get(i), y, x);
        }

        plot.addLegend("NORTH");
        BufferedImage image = new BufferedImage(plot.getWidth(), plot.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        JFrame frame = new JFrame(name);

        frame.setSize(500,500);
        frame.setContentPane(plot);
        frame.setVisible(true);

        plot.paintAll(graphics);
        ImageIO.write(image,"png", new File(name+".png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
