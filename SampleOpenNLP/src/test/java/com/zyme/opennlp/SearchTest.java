package com.zyme.opennlp;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Test;

import com.nova.nlp.search.SearchClient;

public class SearchTest {
	private static final Logger LOG = 
			Logger.getLogger(SearchTest.class.getName());
	
	@Test
	public void searchSingleDate() {
		SearchClient client = new SearchClient();
		LOG.log(Level.INFO, client.processRequest("Nokia is a very huge and useful website."));
		Assert.assertEquals(true, client.result().length()>0);
	}
	@Test
    public void searchDateFromFileName() {
		SearchClient client = new SearchClient();
		LOG.log(Level.INFO, client.processRequest("record for 20131018_180441_000_10309-Channel_IP1-INV_Logitech - Copy (2).xls"));
		Assert.assertEquals(true, client.result().length()>0);

    }
	@Test
    public void searchDateWithSept1() {
		SearchClient client = new SearchClient();
		LOG.log(Level.INFO, client.processRequest("record for 2012-01-18 20131018_180441_000_10309-Channel_IP1-INV_Logitech - Copy (2).xls"));
		Assert.assertEquals(true, client.result().length()>0);

    }

}
