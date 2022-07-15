import java.util.*;
import java.io.*;

public class TextMining {

    public static void main(String[] args) throws Exception {

        int noClusters= 3;
        String measure= "euclidian";
        String foldersfile ="data.txt";
        String folderPath = "";
        File data_file = new File(foldersfile);
        BufferedReader br1 = new BufferedReader(new FileReader(data_file));
        String folderName;
        ArrayList<String> stopwords = new ArrayList();
        String stopwordsFileName = "stopwords.txt";
        BufferedReader buffer = new BufferedReader(new FileReader(stopwordsFileName));
        String word = "";
        while ((word = buffer.readLine()) != null) {
            stopwords.add(word);
        }
        int documentCounter = 0;
        Map<Integer,Integer> documentCount = new HashMap<>();
        PreProcessing preProcessor = new PreProcessing(stopwords);
        HashMap<String, List<String>> documentTokens = new HashMap();
        while ((folderName = br1.readLine()) != null) {
            File folder = new File("data/"+folderName);
            for (File file : folder.listFiles()) {
                buffer = new BufferedReader(new FileReader(file));
                List<String> tokens = preProcessor.process(buffer);
                documentTokens.put(file.getAbsolutePath(), tokens);
            }
            int index = folderName.lastIndexOf('/');
            folderName = folderName.substring(index+1);
            documentCount.put(folderName.charAt(1)-'0',documentCounter++ );
        }


        preProcessor.slidingWindow(documentTokens);
        Matrix ma = new Matrix(documentTokens);
        double[][] TFIDF  = ma.constructTFIDFMatrix(documentTokens);
        List<String> docIds = ma.getDocIds();
        int[] original_labels = OriginalLabels(docIds, documentCount);
        System.out.println("ori "+original_labels.length);
        HashMap<String,  SortedSet<String>> keywords = ma.generateKeywords();
        Kmeans k = new Kmeans(noClusters, TFIDF);

        int[] kmeansClusters =  k.getClusters(measure, 50);
        int[] mapping = k.centroidsLabels(original_labels);

        for(int i=0;i<kmeansClusters.length;i++)
            kmeansClusters[i] = mapping[kmeansClusters[i]];
       // System.out.println("kmeans "+kmeansClusters.length);

        KmeansPlusPlus kmeansplusplus = new KmeansPlusPlus(noClusters,TFIDF);
        int[] kmeansplusplusLabels = kmeansplusplus.getClusters(measure, 100);
        mapping = k.centroidsLabels(original_labels);

        for(int i=0;i<kmeansplusplusLabels.length;i++)
            kmeansplusplusLabels[i] = mapping[kmeansplusplusLabels[i]];

        // Dimensionality Reduction
        PCA pca = new PCA(TFIDF, 2);
        double[][] reduced_matrix = pca.reduceDimensions();

        // Visualization
        pca.visualize(reduced_matrix, original_labels, noClusters, "Original_Clusters");
        pca.visualize(reduced_matrix, kmeansClusters, noClusters, "Kmeans_Clusters");
        pca.visualize(reduced_matrix, kmeansplusplusLabels, noClusters, "KmeansPlusPlus_Clusters");

        System.out.println("Original Labels: " + Arrays.toString(original_labels));
        System.out.println("KMeans Labels: " + Arrays.toString(kmeansClusters));
        System.out.println("KMeansPlusPlus Labels: " + Arrays.toString(kmeansplusplusLabels));

        ModelPerformance performance = new ModelPerformance(original_labels,kmeansClusters,3, "kmeans");
        performance.performance();

        performance = new ModelPerformance(original_labels,kmeansplusplusLabels,3, "kmeanspluplus");
        performance.performance();
    }

    private static int[] OriginalLabels(List<String> docIds, Map<Integer, Integer> documentCount) {
       int[] l = new int[docIds.size()];
       int x=0;
        for(String folderName : docIds)  {
            int index = nthLastIndexOf(2,"/",folderName);
            String[] name = folderName.substring(index+1).split("/");
            l[x] = documentCount.get(name[0].charAt(1) - '0');
            x++;
        }
        return l;
    }
    static int nthLastIndexOf(int nth, String ch, String string) {
        if (nth <= 0) return string.length();
        return nthLastIndexOf(--nth, ch, string.substring(0, string.lastIndexOf(ch)));
    }

}