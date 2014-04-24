package com.nova.nlp.search;

import com.nova.nlp.search.handler.DateHandler;
import com.nova.nlp.search.handler.ParserHandler;
import com.nova.nlp.search.handler.TokenizeHandler;
import com.nova.nlp.search.handler.WhiteSpaceDateHandler;

public class SearchClient {
	final SearchProcessor processor = new SearchProcessor(); 
	private String result;
	public SearchClient() {
		createProcessor();
	}
	private void createProcessor() {
		processor.addHandler(new TokenizeHandler());
		processor.addHandler(new DateHandler());
		processor.addHandler(new WhiteSpaceDateHandler());
		processor.addHandler(new ParserHandler());
	}
	public String processRequest(String searchSentence) {
		result = processor.process(searchSentence);
		return result;
	}
	public String result() {
		return result;
	}
}
