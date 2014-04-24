package com.nova.nlp.search.handler;

import java.util.regex.Pattern;

import opennlp.tools.namefind.RegexNameFinder;

public interface DateTemplate {
	 Pattern YYYYMMdd = Pattern.compile("([0-9]{4})([0-9]{2})([0-9]{2})");
     Pattern YYYY_MM_dd = Pattern.compile("([0-9]{4})-([0-9]{2})-([0-9]{2})");
     RegexNameFinder finder = 
                     new RegexNameFinder(new Pattern[]{YYYYMMdd,YYYY_MM_dd});
}
