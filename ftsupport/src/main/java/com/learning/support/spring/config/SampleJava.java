package com.learning.support.spring.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class SampleJava {
	public static void main(String args[]) throws JSONException{
		SampleJava s=new SampleJava();
		String csvFile = "C:\\Users\\sswain\\Desktop\\ext\\ftconfig\\FT_CONFIG_SUPPORT_Latest20170428APJPROD.CSV";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                System.out.println(s.creatingJsonArray(data));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	public String creatingJsonArray(String[] data) throws IOException, JSONException{
		JSONObject object=new JSONObject();
		object.put("clientId", data[0]);
		if(StringUtils.equalsIgnoreCase(data[1], "Daily")){
			object.put("frequency", 1);
		}else if(StringUtils.equalsIgnoreCase(data[1], "Weekly")){
			object.put("frequency", 2);
		}else{
			object.put("frequency", 3);
		}
		if(!StringUtils.isEmpty(data[2])){
			object.put("partnerCode", data[2]);
		}else{
			object.put("partnerCode", JSONObject.NULL);
		}
		
		object.put("category", "Reminder");
		object.put("beforeAfter", data[3]);
		object.put("time", data[4]);
		object.put("label", data[5]);
		object.put("mailType", data[6]);
		object.put("active", true);
		object.put("enforced", data[7].toLowerCase());
		object.put("subscriptionStatus", data[8].toLowerCase());
		object.put("rule", data[9]);
		object.put("invocationLabel", "AB");
		object.put("description", data[10]);
		
		URL url = new URL("http://10.101.101.129:9327/config/v1/mail/configuration");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		String input = object.toString();
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}
		conn.disconnect();
		return object.toString();
	}
}
