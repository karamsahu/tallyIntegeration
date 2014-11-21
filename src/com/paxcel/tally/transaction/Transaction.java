package com.paxcel.tally.transaction;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class Transaction {
	//voucher transaction
	private static VelocityEngine ve = new VelocityEngine();
	//xml request elements
	private static String statiicVariables="";
	private static String reapeatVariables="";
	private static String fetchList="";
	private static String functionList="";
	private static String tdl="";
	private static String data="";

	
	
	public static String getReports(HashMap<String,String> headerMap, HashMap<String,String> dataMap, String fileName ){
		String xmlTmpl = "";
		ve.init();	
		/*  create a context and add data */
        VelocityContext context = new VelocityContext();
        /*  next, get the Template  */
        Template vchTmpl = ve.getTemplate(fileName); //eg. path = "\\templates\\voucher.tmpl" 
        /*  create a context and add data */
        context.put("version", (String)headerMap.get("version"));
        context.put("tallyRequest", (String)headerMap.get("tallyRequest"));
        context.put("type", (String)headerMap.get("type"));
        context.put("id", (String)headerMap.get("id"));
        context.put("desc", (String)headerMap.get("desc"));
       
        //setting values in template for data tag and its attribute
        context.put("action", (String)dataMap.get("action"));
        context.put("date", (String)dataMap.get("date"));
        context.put("tagName",(String)dataMap.get("tagName") );
        context.put("tagValue", (String)dataMap.get("tagValue") );
        context.put("vchType", (String)dataMap.get("vchType") );
        context.put("narration", (String)dataMap.get("narration") );
        
        
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        vchTmpl.merge( context, writer );
        /* show the World */
        //System.out.println( writer.toString() );
        xmlTmpl = writer.toString();
		return xmlTmpl;		
	}

	public static String requestService(){
		String version="";
		String tallyRequest="";
		String type="";
		String subType="";
		String id="";
		
		
		String xmlRequest = null;	
		
        VelocityContext context = new VelocityContext(); /*  create a context and add data */
        Template vchTmpl = ve.getTemplate("\\templates\\request.tmpl"); /*  next, get the Template  */ //eg. path = "\\templates\\voucher.tmpl"
             
        context.put("version", version);
        context.put("tallyRequest", tallyRequest);
        context.put("type", type);
        context.put("subType", subType);
        context.put("id", id);
        
        context.put("statiicVariables", statiicVariables);
        context.put("reapeatVariables", reapeatVariables);
        context.put("functionList", functionList);
        context.put("tdl", tdl);
        context.put("data", data);
        
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        vchTmpl.merge( context, writer );
        /* show the World */
        //System.out.println( writer.toString() );
        xmlRequest = writer.toString();
        return xmlRequest;
        
	}

	
}
