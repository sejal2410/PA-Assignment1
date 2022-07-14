import java.util.*;
import java.io.*;

public class TextMining {

    public static void main(String[] args) throws Exception {
        String foldersfile = "data.txt";
        String folderPath = "";
        File data_file = new File(foldersfile);
        BufferedReader br1 = new BufferedReader(new FileReader(data_file));
        String folderName;
        ArrayList<String> stopwords = new ArrayList();
        String stopwordsFileName = "stopwords.txt";
        File stopwordsFile = new File(stopwordsFileName);
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
            File folder = new File(folderName);
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

        HashMap<String,  SortedSet<String>> keywords = ma.generateKeywords();
        Kmeans k = new Kmeans(3, TFIDF);
        int[] kmeansClusters = k.getClusters("euclidean", 50);
        int[] mapping = k.centroidsLabels(original_labels);
        System.out.println(documentCount);
        System.out.println("KMeans Labels: " + Arrays.toString(kmeansClusters));
        for(int i=0;i<kmeansClusters.length;i++)
            kmeansClusters[i] = mapping[kmeansClusters[i]];
//        for(int i: kmeansClusters)
//            System.out.print(i+"    ");
        KmeansPlusPlus kmeansplusplus = new KmeansPlusPlus(3,TFIDF);
        int[] kmeansplusplusLabels = kmeansplusplus.getClusters("euclidian", 100);
        for(int i=0;i<kmeansClusters.length;i++)
            kmeansplusplusLabels[i] = original_labels[kmeansplusplusLabels[i]];
        System.out.println("Original Labels: " + Arrays.toString(original_labels));
        System.out.println("KMeans Labels: " + Arrays.toString(kmeansClusters));
   //     System.out.println("KMeans++ Labels: " + Arrays.toString(kmeansplusplusLabels));
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