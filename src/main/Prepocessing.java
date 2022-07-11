import java.util.*;
public class PreProcessing{
    ArrayList<String> stopwords;

    PreProcessing(ArrayList<String> stopwords){
       this.stopwords = stopwords;
    }

    StringBuilder removeStopwords(BufferedReader buffer){
        String s = "";
        StringBuilder sb = new StringBuilder();
        while((s=buffer.readLine())!=null){
            for(String stopword:stopwords)
                s.replace(s,"");
            sb.append(s);
        }
        return sb;
    }

    public process(BufferedReader buffer){
        StringBuilder output = removeStopwords(BufferedReader buffer);

    }
}