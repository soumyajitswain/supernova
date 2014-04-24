package com.zyme.opennlp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.junit.Test;

public class OpenNLPTest {
	
	@Test
	public void findNameTest()  {
		
		try {
		InputStream is = new FileInputStream(
				"D:\\work\\workspace\\searchutility\\src\\test\\resources\\en-ner-date.bin");
		
		TokenNameFinderModel model = new TokenNameFinderModel(is);
		is.close();
		NameFinderME nameFinder = new NameFinderME(model);
		String []sentence = new String[]{
				
				"2012", "JAN", "Wed"
		};

		Span nameSpans[] = nameFinder.find(sentence);
		
		for(Span s: nameSpans)
			System.out.println(s.toString());
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void Tokenize() throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream("D:\\work\\workspace\\searchutility\\src\\test\\resources\\en-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		Tokenizer tokenizer = new TokenizerME(model);
		String tokens[] = tokenizer.tokenize("Hi. How are you? This is f-Mike.");
		for (String a : tokens)
			System.out.println(a);
 
		is.close();
	}
}
