package com.fngry.nlp.opennlp;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by gaorongyu on 15/10/23.
 */
public class Sentence {

    public static void main(String[] args) throws Exception {

//        sentenceDetect();

        POSTag();

    }

    /**
     * 句子识别
     * @throws Exception
     */
    public static void sentenceDetect() throws Exception {
        //        String paragraph = "Hi. How are you? This is Mike.";
        String paragraph = " You say hey. what is your name? ";
//        String paragraph = " 你好。 你是谁？ ";

        //
        InputStream is = new FileInputStream("/Users/gaorongyu/dev/workspace/test/creative/nlp/src/main/resources/en-sent.bin");
        SentenceModel sentenceModel = new SentenceModel(is);

        SentenceDetectorME detectorME = new SentenceDetectorME(sentenceModel);

        String[] sentences = detectorME.sentDetect(paragraph);

        for(int i = 0; i < sentences.length; i++) {
            System.out.println(sentences[i]);
        }
        is.close();
    }

    /**
     * 符号识别
     * @throws Exception
     */
    public static void tokenize() throws Exception {

        String paragraph = " You say hey. what is your name? ";

        InputStream is = new FileInputStream("/Users/gaorongyu/dev/workspace/test/creative/nlp/src/main/resources/en-token.bin");
        TokenizerModel model = new TokenizerModel(is);
        Tokenizer tokenizer = new TokenizerME(model);

        String[] tokens = tokenizer.tokenize(paragraph);

        print(tokens);

        is.close();
    }

    public static void findName() throws Exception {

        InputStream is = new FileInputStream("/Users/gaorongyu/dev/workspace/test/creative/nlp/src/main/resources/en-ner-person.bin");

        TokenNameFinderModel model = new TokenNameFinderModel(is);
        NameFinderME nameFinderME = new NameFinderME(model);

        String[] sentence = new String[]{
                "Mike",
                "Smith",
                "is",
                "a",
                "good",
                "person",
                "Roger",
                "JAck"
        };
        Span[] nameSpans = nameFinderME.find(sentence);

        print(nameSpans);

    }

    public static void POSTag() throws Exception {

        POSModel model = new POSModelLoader().load(
                new File("/Users/gaorongyu/dev/workspace/test/creative/nlp/src/main/resources/en-pos-maxent.bin"));
        PerformanceMonitor monitor = new PerformanceMonitor(System.err, "sent");
        POSTaggerME posTaggerME = new POSTaggerME(model);

        String input = "Hi. How are you? This is Mike.";
        ObjectStream<String> os = new PlainTextByLineStream(new StringReader(input));

        monitor.start();
        String line;
        while((line = os.read()) != null) {
            String[] whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(line);
            String[] tags = posTaggerME.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample);

            monitor.incrementCounter();
        }
        monitor.stopAndPrintFinalResult();
    }

    public static void print(Object[] obj) {

        if(obj == null) {
            return;
        }

        for(int i = 0; i < obj.length; i++) {
            System.out.println(obj[i]);
        }

    }

}
