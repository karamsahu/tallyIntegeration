package com.paxcel.tally.voucher;

import java.io.StringWriter;
import java.util.Date;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class PurchaseOrder {
	private static  String xmlTmpl = null;
	private static VelocityEngine ve = new VelocityEngine();
    
	public static String createOrder(String fileName, String tagName, int tagValue, String action, String vchType, String narration, Date date){
		xmlTmpl = "";
		ve.init();	
		/*  create a context and add data */
        VelocityContext context = new VelocityContext();
        /*  next, get the Template  */
        Template vchTmpl = ve.getTemplate(fileName); //eg. path = "\\templates\\voucher.tmpl" 
        /*  create a context and add data */
        context.put("version", "1");
        context.put("tallyRequest", "Import");
        context.put("type", "Data");
        context.put("id", "Vouchers");
        context.put("desc", "");
        context.put("action", action);
        context.put("date", date.toString());
        context.put("tagName", tagName);
        context.put("tagValue", tagValue);
        context.put("vchType", vchType);
        context.put("narration", narration);
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        vchTmpl.merge( context, writer );
        /* show the World */
        //System.out.println( writer.toString() );
        xmlTmpl = writer.toString();
		return xmlTmpl;
	}
	
	public static void RetriveOrder(String orderId){
			
	}

	public static void DeleteOrder(){
		
	}

	public static void UpdateOrder(){
		
	}

}
