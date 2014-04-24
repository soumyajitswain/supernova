package com.nova.nlp.search.handler;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.InvalidFormatException;

import com.nova.nlp.search.SearchBean;
import com.nova.nlp.search.Searchkey;

public class ParserHandler implements SearchHandler {
	private static final Logger LOG = 
			Logger.getLogger(ParserHandler.class.getName());
	private SearchHandler next;
	private static ParserModel model;
	private String name = Searchkey.REPORTING_PARTNER_ID.name();
	static {
		try {
			InputStream is = ParserHandler.class.getResourceAsStream("/en-parser-chunking.bin");
			model = new ParserModel(is);
			is.close();
		} catch (FileNotFoundException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		} catch (InvalidFormatException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	public ParserHandler() {

	}
	public ParserHandler(String name) {
		this.name = name;
	}
	public SearchBean process(SearchBean bean) {
		String input = bean.input;
		Parser parser = ParserFactory.create(model);
		Parse topParses[] = ParserTool.parseLine(input, parser, 1);
		List<String> nnResult = new ArrayList<String>(5);
		for (Parse p : topParses) {
			for(Parse ip:p.getTagNodes()) {
				if(ip.getType().contains("NN")) {
					nnResult.add(ip.getCoveredText());
				}
			}
		}	
		bean.set(name,nnResult);
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
