package com.nova.nlp.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchBean implements Cloneable {
	private final Map<String, List<String>> beans;
	public final String input;
	private Object result;
	private String[] tokens;
    public SearchBean(String input) {
    	beans = new HashMap<String, List<String>>();
    	this.input = input;
    }
    public Map<String, List<String>> getPrev() {
    	return beans;
    }
    public String[] getTokens() {
		return tokens;
	}
	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}
	public void set(String key,List<String> o) {
		beans.put(key, o);
    	result = o;
    }
    public Object output() {
    	return result;
    }
}
