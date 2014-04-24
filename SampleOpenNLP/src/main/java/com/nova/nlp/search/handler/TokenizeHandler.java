package com.nova.nlp.search.handler;

import opennlp.tools.tokenize.SimpleTokenizer;

import com.nova.nlp.search.SearchBean;

public class TokenizeHandler implements SearchHandler {
	private final SimpleTokenizer mTokenizer = SimpleTokenizer.INSTANCE;
	private SearchHandler next;
	public SearchBean process(SearchBean bean) {
		String tokens[] = mTokenizer.tokenize(bean.input);
		bean.setTokens(tokens);
		if(null != next) {
			next.process(bean);
		}
		return bean;
	}

	public void next(SearchHandler handler) {
		next = handler; 
	}

}
