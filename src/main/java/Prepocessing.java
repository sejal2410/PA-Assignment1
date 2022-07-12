import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.io.*;
import java.util.*;

class PreProcessing{
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

    String removeStopwords(BufferedReader buffer) throws IOException {
        String s = "";
        StringBuilder sb = new StringBuilder();
        while((s=buffer.readLine())!=null){
            s = s.toLowerCase();
            String[] words = s.split(" ");
            for(String word: words){
                if(word!=" " && !stopwords.contains(word))
                    sb.append(word+" ");
            }
        }
        return sb.toString();
    }

    public List<String>  process(BufferedReader buffer) throws IOException {
        String output = removeStopwords(buffer).trim();
        Document document = new Document(output);
        CoreDocument document1 = new CoreDocument(output);
        pipeline.annotate(document1);

        List<String> tokens = new ArrayList<String>();
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
                    }
                    else {
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

    // Use a sliding window approach to merge remaining phrases that belong together.
    public void slidingWindow(Map<String, List<String>> map) throws IOException {

        List<String> ngramList = new ArrayList<String>();

        Map<String, Integer> frequency = new HashMap<String, Integer>();
        for (Map.Entry<String, List<String>> entry: map.entrySet()) {
            for (String ngram: getNGrams(entry.getValue(), 2)) {
                frequency.put(ngram, frequency.getOrDefault(ngram, 0) + 1);
            }
            for (String ngram: getNGrams(entry.getValue(), 3)) {
                frequency.put(ngram, frequency.getOrDefault(ngram, 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry: frequency.entrySet()) {
            if (entry.getValue()>=10) {
                ngramList.add(entry.getKey());
            }
        }

        for (Map.Entry<String, List<String>> entry: map.entrySet()) {

            String sentences = String.join(" ", entry.getValue());

            for (String ngram: ngramList) {
                sentences= sentences.replace(ngram, ngram.replace(" ", "_"));
            }

            List<String> tokensList = Arrays.asList(sentences.split(" "));
            entry.setValue(tokensList);
        }
    }

    List<String> getNGrams(List<String> words, int n){
        List<String> ngrams = new ArrayList<String>();
        int size = words.size();
        for(int i = 0; i < size; i++) {
            if((i + n - 1) < size) {
                int stop = i + n;
                String ngramWords = words.get(i);

                for(int j = i + 1; j < stop; j++) {
                    ngramWords +=" "+ words.get(j);
                }

                ngrams.add(ngramWords);
            }
        }
        System.out.println(ngrams);
        return ngrams;
    }
}