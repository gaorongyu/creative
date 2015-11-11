package com.fngry.nlp.opennlp.fufan;

import org.fnlp.nlp.cn.tag.CWSTagger;

import java.util.ArrayList;

/**
 * Created by gaorongyu on 15/10/24.
 */
public class Demo {


    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {
        String segModelFilePath="/Users/gaorongyu/dev/workspace/test/creative/nlp/src/main/resources/fudannlp/FudanNLP-1.6.1/models/seg.m";
        String orgString="刘开瑛2000 第4章78248个交集型歧义字段中，研究进展复旦分词系统 首先 使用正向最小匹配和逆向最大匹配对文本进行双向扫描 .";
        ArrayList<String> dicList =new ArrayList<String>();
        dicList.add("逆向最大匹配");
//        edu.fudan.ml.types.Dictionary dictionary=new edu.fudan.ml.types.Dictionary(dicList);
        CWSTagger tag = new CWSTagger(segModelFilePath);
//        CWSTagger tagdic= new CWSTagger(segModelFilePath, dictionary);

        String segString = tag.tag(orgString);
//        String segdicString=tagdic.tag(orgString);
        System.out.println("未加入词典："+segString);
//        System.out.println("加入词典：  "+segdicString);
    }

}
