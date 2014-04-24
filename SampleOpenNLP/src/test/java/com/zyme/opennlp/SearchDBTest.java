package com.zyme.opennlp;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Test;

import com.nova.nlp.dao.DBClient;
import com.nova.nlp.search.SearchClient;

public class SearchDBTest {
	private static final Logger LOG = Logger.getLogger(
			   SearchDBTest.class.getName());
	private final DBClient client = new DBClient();
	private final SearchClient searchClient = new SearchClient();
	@Test
    public void testDB() {
    	List<Object[]> datas = client.getData(null);
    	for(Object[] d:datas) {
    		LOG.info( d[0].toString()+"| "+ d[1].toString()+"| "+ d[2].toString()+"| "+ d[3].toString()+"| "+  d[4].toString()+"| "+  d[5].toString());
    	}
    	Assert.assertEquals(true, datas.size() > 0);
    	Assert.assertEquals(10, datas.size());
    }
	@Test
	public void testWithSearchKey() {
        final String searchSentence= "AUTO uploaded on 2013-02-26";
        final String queryString = searchClient.processRequest(searchSentence);
        List<Object[]> datas = client.getData(queryString);
        Assert.assertEquals(true, datas.size() > 0);
	}
}
