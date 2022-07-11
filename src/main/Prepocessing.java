mport edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import java.util.*;

public class PreProcessing{
    ArrayList<String> stopwords;
    StanfordCoreNLP pipeline;
    PreProcessing(ArrayList<String> stopwords){
       this.stopwords = stopwords;
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
       pipeline = new StanfordCoreNLP(props);
    }

    String removeStopwords(BufferedReader buffer){
        String s = "";
        StringBuilder sb = new StringBuilder();
        while((s=buffer.readLine())!=null){
            for(String stopword:stopwords)
                s.replace(stopword,"");
            sb.append(s);
        }
        return sb.toString();
    }

    public List<String>  process(BufferedReader buffer){
        String output = removeStopwords(buffer);

        List<String> tokens = new ArrayList<String>();
        Document document = new Document(output);
        List<Sentence> sentences = document.sentences();

        for (Sentence sentence: sentences) {
            String nerWord = "";

            for (int i=0; i<sentence.length(); i++){

                String currentTag = sentence.nerTag(i);
                String word = sentence.word(i);
                // Apply named-entity extraction (NER).
                if (!word.matches("[\\p{Punct}\\p{IsPunctuation}]")) {
                    String nextTag = "O";
                    if (i < sentence.length() - 1) {
                        nextTag = sentence.nerTag(i+1);
                    }
                    if (currentTag.equals("O")) {
                        tokens.add(sentence.lemma(i));
                    } else {
                        if (nextTag.equals(currentTag)) {
                            nerWord += word + "_";
                        } else {
                            nerWord += word;
                            tokens.add(nerWord);
                            nerWord = "";
                        }
                    }
                }
            }
        }
        return tokens;
    }
}