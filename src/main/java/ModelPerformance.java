import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ModelPerformance {
    int k;
    int[][] confusionMatrix;
    int[] actualLabels;
    int[] predictedLabels;
    String model;
    //int[][]
    ModelPerformance(int[] actualLabels, int[] predictedLabels, int k,String model){
        this.actualLabels = actualLabels;
        this.predictedLabels = predictedLabels;
        confusionMatrix = new int[k][k];
        this.model = model;
        this.k = k;
    }

    void performance() throws IOException {
        double[] precision = new double[k];
        double[] recall = new double[k];
        double[] f1Score = new double[k];
        int[] tp = new int[k];
        int[] fp = new int[k];
        int[] fn = new int[k];
        int tpTotal = 0, fpTotal = 0, fnTotal = 0;
        FileWriter writer = new FileWriter(model+"_model_performance.txt");
       // writer.write("\nClass\tPrecision\tRecall\tF1-Score\n");
        for(int i=0;i<actualLabels.length;i++) {
            confusionMatrix[actualLabels[i]][predictedLabels[i]]++;
            //System.out.println(i);
            if(actualLabels[i]!=predictedLabels[i]){
                fp[predictedLabels[i]]++;
                fn[actualLabels[i]]++;
            }
            else tp[actualLabels[i]]++;
        }
        System.out.println("Confusion Matrix: ");
        writer.write("Confusion Matrix: "+"\n");
        System.out.println("Class");
        writer.write("Class"+"\n");
        for(int i=0;i<k;i++) {
            System.out.print(i + "\t");
            writer.write(i + "\t");
        }
        System.out.println();
        int temp=0;
        for(int[] a: confusionMatrix) {
            System.out.println(temp+"\t"+Arrays.toString(a));
            writer.write(temp+"\t"+Arrays.toString(a)+"\n");
            temp++;
        }

        System.out.println("\nClass Precision Recall F1-Score");
        writer.write("\nClass Precision Recall F1-Score"+"\n");
        for (int i = 0; i<k; i++) {
            precision[i] = ((double) tp[i])/(tp[i]+fp[i]);
            recall[i] = ((double) tp[i])/(tp[i]+fn[i]);
            f1Score[i] = (2.0 * precision[i] * recall[i]) / (precision[i] + recall[i]);
            fpTotal += fp[i];
            fnTotal += fn[i];
            tpTotal += tp[i];


            System.out.println(i+"\t\t"+String.format("%.2f", precision[i])+"\t\t"+String.format("%.2f", recall[i])+"\t"+String.format("%.2f", f1Score[i]));
            writer.write(i+"\t\t"+String.format("%.2f", precision[i])+"\t\t"+String.format("%.2f", recall[i])+"\t"+String.format("%.2f", f1Score[i])+"\n");
        }

        double total_precision = (1.0*tpTotal)/(tpTotal+fpTotal);
        double total_recall = (1.0*tpTotal)/(tpTotal+fnTotal);
        double total_f1 = (2.0 * total_precision * total_recall) / (total_precision + total_recall);

        System.out.println("\nTotal Precision:\t" + String.format("%.2f", total_precision));
        System.out.println("Total Recall:\t\t" + String.format("%.2f", total_recall));
        System.out.println("Total F1-Score:\t\t" + String.format("%.2f", total_f1));
        writer.write("\nTotal Precision:\t" + String.format("%.2f", total_precision)+"\n");
        writer.write("Total Recall:\t\t" + String.format("%.2f", total_recall)+"\n");
        writer.write("Total F1-Score:\t\t" + String.format("%.2f", total_f1)+"\n");

        writer.close();
    }

}
