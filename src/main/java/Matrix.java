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

        for (List<String> tokens : map.values())
            allTokens.addAll(tokens);
        List<String> documents = new ArrayList<>();
        TF = new double[documents.size()][allTokens.size()];
        IDF = new double[allTokens.size()];
        for (String documentName : map.keySet())
            documents.add(documentName);

        Collections.sort(documents);
        Collections.sort(allTokenKList);
    }
     double[][] constructTFIDFMatrix(HashMap<String, List<String>> map) {

        for (int i = 0; i < documents.size(); i++) {
            for (int j = 0; j < allTokenKList.size(); j++) {
                TF[i][j] = Collections.frequency(map.get(documents.get(i)), allTokenKList.get(j)) / map.get(documents.get(i)).size();
            }
        }

        for (int i = 0; i < allTokenKList.size(); i++) {
            int nonZeroDocumentsCount = 0;
            for (int j = 0; j < documents.size(); j++) {
                if (TF[j][i] != 0)
                    nonZeroDocumentsCount++;
            }
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
            String folderName = document.substring(index+1);
            if(folderDocumentMapping.containsKey(folderName))
                folderDocumentMapping.get(folderName).add(i);
            else{
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
            SortedSet<String> keys = new TreeSet<>((String e1, String e2) ->
                    - (tokenScore.get(e1).compareTo(tokenScore.get(e1))));
            keys.addAll(tokenScore.keySet());
            keywords.put(entry.getKey(), keys);
        }
    return keywords;
    }
}