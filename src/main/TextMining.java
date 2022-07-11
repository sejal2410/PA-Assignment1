import java.util.*;
import java.io.*;

public class TextMining{

    public static void main(String[] args) throws Exception{
        String foldersfile = "data.txt";
        String folderPath = "";
        File data_file = new File(foldersfile);
        BufferedReader br1 = new BufferedReader(new FileReader(data_file));
        String folderName;
        ArrayList<String> stopwords= new ArrayList();
        String stopwordsFileName = "stopwords.txt";
        File stopwordsFile = new File(stopwordsFileName);
        BufferedReader buffer = new BufferedReader(new FileReader(stopwordsFileName));
        String word="";
        while((word=buffer.readLine())!=null){
            stopwords.add(word);
        }
        PreProcessing preProcessor= new PreProcessing(stopwords);

        while ((folderName = br1.readLine()) != null) {
            File folder = new File(folderName);
            for (File file : folder.listFiles()) {
                 buffer = new BufferedReader(new FileReader(file));
                 List<String>  tokens = preProcessor.process(buffer);

            }
        }

}