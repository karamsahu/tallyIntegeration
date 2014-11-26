package net.paxcel.tallyservices.inventory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.paxcel.tallyservies.xmlparser.XmlParser;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class Start {
	public static String productId= null;
	public static String xmlWithValue = null;
	public static String path = null;
	public static String requestType = null;
	
	public static void main(String [] args){
		loadTemplates();					//load xml templates
		requestType="show pnl"; 			//setting type of request
		path = checkRequest(requestType);	//set template path as per the request
		xmlWithValue = fillTemplate(path);	//fillTemplate method create xml with values
		try {
			String tallyXmlResponse = TallyRequest.SendToTally(xmlWithValue);		//passing xml with values to tally for to get tally response xml
			if(requestType.equals("show pnl")){
				XmlParser.getProfitAndLoss(tallyXmlResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String checkRequest(String requestType) {	//check request type and return path of template file to use by velocity
		String path=null;
		if(requestType.equals("show all")){
			path = requestMap.get("getAllStock").toString();	//read requestMap to get template path
		}else if( requestType.equals("show one")){
			productId="Iphone 6"; 							    //we can change this value dynamically
			path= requestMap.get("getOneStock").toString();
		}else if(requestType.equals("show summary")){
			path= requestMap.get("getStockSummary").toString();
		}else if(requestType.equals("show pnl")){
			path= requestMap.get("getPNL").toString();
		}else{
			System.err.println("Unknow argument , running default action");
			path="templates\\test.xml";
		}
		return path;
	}

	//returns map loaded with xml templates 
	public static Map<String,String> requestMap = null;
	public static Map<String,String> loadTemplates(){
		
		requestMap = new HashMap<String,String>();
		requestMap.put("getAllStock", "templates\\fetch_full_stock_detail.xml");
		requestMap.put("getOneStock", "templates\\fetch_inidvidual_stock_item.xml");
		requestMap.put("createStock", "templates\\insert_stock_item.xml");
		requestMap.put("getPNL", "templates\\fetch_PNL_summary.xml");
		return requestMap;
	}
		
	//supply template parameter values returns xml as string
	private static VelocityEngine ve = new VelocityEngine();
	//returns xml with values keyd as string
	private synchronized static String fillTemplate(String path){
			ve.init();
			//Map<String,String> headerMap = new HashMap<String,String>();
			//Map<String,String> dataMap = new HashMap<String,String>();
			/*  create a context and add data */
	        VelocityContext context = new VelocityContext();
	        /*  next, get the Template  */
			Template vchTmpl = ve.getTemplate(path); //eg. path = "\\templates\\voucher.tmpl" 
	        /*  create a context and add data */
	        context.put("version", "1");
	        context.put("tallyRequest", "Export");
	        context.put("type", "Object");
	        context.put("subType", "Stock Item");
	        context.put("id", productId); /*product id is the id for which xml will fetch data*/
	       
	        //setting values in template for data tag and its attribute
	        ArrayList<String> fetchList = new ArrayList<String>();
	        fetchList.add("Parent");
	        fetchList.add("Name");
	        fetchList.add("OpeningRate");
	        fetchList.add("BaseUnits");
	        fetchList.add("OpeningBalance");
	        
	        /* now render the template into a StringWriter */
	        StringWriter writer = new StringWriter();
	        vchTmpl.merge( context, writer );
	        /* show the World */
	        //System.out.println( writer.toString() );
	        
	        String xmlTmpl = writer.toString();
			
		return xmlTmpl;
	}
	
}
