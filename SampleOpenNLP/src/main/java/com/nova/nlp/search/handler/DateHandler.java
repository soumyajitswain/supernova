package com.nova.nlp.search.handler;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.Span;

import com.nova.nlp.search.SearchBean;
import com.nova.nlp.search.Searchkey;

public class DateHandler implements SearchHandler, DateTemplate {
	private SearchHandler next;
	private String name = Searchkey.DATE_TIME.name();
	public DateHandler() {
		
	}
	public DateHandler(String name) {
		this.name = name;
	}
	public SearchBean process(SearchBean bean) {
		String tokens[] = (String[]) bean.getTokens();
		Span[] result = finder.find(tokens);
		List<String> dateResult = 
				new ArrayList<String>(result.length);
		for(Span s:result) {
			String date = tokens[s.getStart()];
			dateResult.add(DateUtil.parseDate(date));
		}
		if(dateResult.size() != 0)
			bean.set(name, dateResult);
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
