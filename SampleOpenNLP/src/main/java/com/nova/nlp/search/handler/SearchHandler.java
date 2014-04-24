package com.nova.nlp.search.handler;

import com.nova.nlp.search.SearchBean;

public interface SearchHandler {
     SearchBean process(SearchBean bean);
     void next(SearchHandler handler);
}
