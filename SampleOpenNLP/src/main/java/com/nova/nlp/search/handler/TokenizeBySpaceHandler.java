package com.nova.nlp.search.handler;

import com.nova.nlp.search.SearchBean;

public class TokenizeBySpaceHandler implements SearchHandler {
    private SearchHandler next;
	public SearchBean process(SearchBean bean) {
		if(null != next) {
			next.process(bean);
		}
		return bean;
	}

	public void next(SearchHandler handler) {
           next = handler;
	}

}
