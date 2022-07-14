import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Matrix {

    List<String> allTokenKList;
    List<String> documents;
    double[][] TF;
    double[][] TFIDF;
    HashMap<String, List<String>> map;
    double[] IDF;

    Matrix( HashMap<String, List<String>> map){
        this.map = map;
        HashSet<String> allTokens = new HashSet<>();
        documents = new ArrayList<>();
        allTokenKList = new ArrayList<>();
        documents = new ArrayList<>();
        for (List<String> tokens : map.values())
            allTokens.addAll(tokens);

        for (String documentName : map.keySet())
            documents.add(documentName);
        TF = new double[documents.size()][allTokens.size()];
        IDF = new double[allTokens.size()];
        allTokenKList.addAll(allTokens);
        Collections.sort(documents);
        Collections.sort(allTokenKList);
    }

    List<String> getDocIds(){
        return documents;
    }
     double[][] constructTFIDFMatrix(HashMap<String, List<String>> map) {

        for (int i = 0; i < documents.size(); i++) {
            for (int j = 0; j < allTokenKList.size(); j++) {
                double fre = Collections.frequency(map.get(documents.get(i)), allTokenKList.get(j));
                TF[i][j] = ( fre)/ map.get(documents.get(i)).size();
            }
        }

        for (int i = 0; i < allTokenKList.size(); i++) {
            int nonZeroDocumentsCount = 0;
            for (int j = 0; j < documents.size(); j++) {
                if (TF[j][i] != 0)
                    nonZeroDocumentsCount++;
            }
            if(nonZeroDocumentsCount!=0)
                IDF[i] = (Math.log(documents.size() / nonZeroDocumentsCount));
        }
        for (int i = 0; i < documents.size(); i++) {
            for (int j = 0; j < allTokenKList.size(); j++) {
                TF[i][j] *= IDF[j];
            }
        }
        this.TFIDF = TF;
        return TF;
    }
    HashMap<String,  SortedSet<String>> generateKeywords(){
        HashMap<String, ArrayList<Integer>> folderDocumentMapping = new HashMap<>();
        HashMap<String,  SortedSet<String>> keywords = new HashMap<>();
        for(int i=0;i<documents.size();i++){
            String document = documents.get(i);
            int index = document.lastIndexOf('/');
            String folderName = document.substring(0,index+1);
            if(folderDocumentMapping.containsKey(folderName))
                folderDocumentMapping.get(folderName).add(i);
            else{
                System.out.println(folderName);
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                folderDocumentMapping.put(folderName,list);
                //folderTokenScore.put(folderName,  new HashMap<String,Double>());
            }
        }

        for (Map.Entry<String, ArrayList<Integer>> entry: folderDocumentMapping.entrySet()) {
            List<Integer> folderDocs = entry.getValue();
            HashMap<String, Double> tokenScore = new HashMap<>();
            for(Integer docIndex: folderDocs){
                String documentName = documents.get(docIndex);
                HashSet<String> tokensInDoc = new HashSet<>(map.get(documentName));
                for(int i=0;i< allTokenKList.size();i++){
                    String token = allTokenKList.get(i);
                     if(tokensInDoc.contains(token))
                        tokenScore.put(token, tokenScore.getOrDefault(token,0.0)+TFIDF[docIndex][i]);
                }
            }

            Comparator<String> sortOnValue = (e1, e2) -> tokenScore.get(e1) < tokenScore.get(e2) ? -1 : tokenScore.get(e1) > tokenScore.get(e2)? 1
                    : e1.compareTo(e2);
            SortedSet<String> keys = new TreeSet<>(sortOnValue);
            for(String k:tokenScore.keySet())
                keys.add(k);

            keywords.put(entry.getKey(), keys);
        }

            List<String> folderNames = new ArrayList<>();
            TreeMap<String,SortedSet<String>> sortedTopics = new TreeMap<>();
            sortedTopics.putAll(keywords);
            try {
                FileWriter fw = new FileWriter("topics.txt");
                for (Map.Entry<String, SortedSet<String>> entry : sortedTopics.entrySet()) {
                    String topicString = String.join(", ", entry.getValue());
                    String h = entry.getKey();
                    int index = nthLastIndexOf(2,"/",h);
                    String[] arr = h.substring(index+1).split("/");
                    if(!(folderNames.contains(arr[0]))) {
                        fw.write("Folder Name: " + arr[0]+ "\n");
                        folderNames.add(arr[0]);
                    }
                    fw.write("Topics: \n" + topicString + "\n\n");
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    return keywords;
    }
    static int nthLastIndexOf(int nth, String ch, String string) {
        if (nth <= 0) return string.length();
        return nthLastIndexOf(--nth, ch, string.substring(0, string.lastIndexOf(ch)));
    }
}
