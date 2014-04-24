package com.nova.nlp.search.handler;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;

import com.nova.nlp.search.SearchBean;
import com.nova.nlp.search.Searchkey;

public class WhiteSpaceDateHandler implements SearchHandler,DateTemplate {
    private SearchHandler next;
    private String name = Searchkey.DATE_TIME.name();
    private static final WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
    public WhiteSpaceDateHandler() {
    	
    }
    public WhiteSpaceDateHandler(String name) {
    	this.name = name;
    }
	public SearchBean process(SearchBean bean) {
		String input = bean.input;
		String tokens[] = tokenizer.tokenize(input);
		Span result[] = finder.find(tokens);
		List<String> dates = bean.getPrev().get(name) == null?
				new ArrayList<String>():bean.getPrev().get(name);
		for(Span s:result) {
			dates.add(DateUtil.parseDate(tokens[s.getStart()]) );
		}
		bean.set(name, dates);
		if(null != next) {
			next.process(bean);
		}
		return bean;
	}

	public void next(SearchHandler handler) {
		next = handler;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
