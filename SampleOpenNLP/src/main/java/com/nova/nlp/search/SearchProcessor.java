package com.nova.nlp.search;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nova.nlp.search.handler.SearchHandler;

public class SearchProcessor {
	private SearchHandler previous;
	private SearchHandler parent;
	public SearchProcessor() {

	}
	public void addHandler(SearchHandler handler) {
		if(null != previous) {
			previous.next(handler);
		} else {
			parent = handler;
		}
		previous = handler;
	}
	public String process(String searchSentence) {
		Map<String, List<String>> beans = (Map<String, List<String>>) 
				parent.process(new SearchBean(searchSentence)).getPrev();
		return generatePartSQL(beans);
	}
	private String generatePartSQL(Map<String, List<String>> beans) {
		StringBuilder builder = new StringBuilder();
		Iterator<?> it = beans.entrySet().iterator();
		int i = 0;
		while(it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String,  List<String>> element = 
			(Entry<String, List<String>>) it.next();
			if(i!=0) {
				builder.append(" AND ");
			}
			if(element.getKey() == Searchkey.DATE_TIME.name()) {
				for(int j=0;j < element.getValue().size();j++) {
					if(j!=0) {
						builder.append(" OR ");
					}
					builder.append(element.getKey()).append(">='").
					append(element.getValue().get(j)).append("'");
				}
			} else {
				for(int j=0;j < element.getValue().size();j++) {
					if(j!=0) {
						builder.append(" OR ");
					}
					builder.append(element.getKey()).append(" like '%").
					append(element.getValue().get(j)).append("%'");
				}
			}
			i++;
		}

		return builder.toString();
	}
	@SuppressWarnings("unused")
	private String getCommaSepValues(List<String> values) {
		StringBuilder sb = new StringBuilder();
		for(String item: values){
			if(sb.length() > 0){
				sb.append(',');
			}
			sb.append(item);
		}
		return sb.toString();
	}
}
