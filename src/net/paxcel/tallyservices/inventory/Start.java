package net.paxcel.tallyservices.inventory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.RepaintManager;

import net.paxcel.tallyservies.xmlparser.XmlParser;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class Start {
	public static String productId= null;
	public static String xmlWithValue = null;
	public static String path = null;
	public static String requestType = null;
	public static String fromDate=null;
	public static String toDate=null;
	/*Possible request:-
	 * show all //this will list all stock items
	 * show one //this will get one stock item listed inside checkRequest method of Start.java file
	 * show summary //show stock summary new inventory
	 * show pnl //calculate profit and loss
	 * daily profit // gives you day wise profitability report from tally
	 * These request can be set in main method in Start.java class file.
	 * */
	
	
	public static void main(String [] args){
		loadTemplates();					//load xml templates
		requestType="daily profit"; 			//setting type of request
		path = checkRequest(requestType);	//set template path as per the request
		try {
			if(requestType.equals("show pnl")){
				xmlWithValue = fillTemplate(path);	//fillTemplate method create xml with values
				String tallyXmlResponse = TallyRequest.SendToTally(xmlWithValue);		//passing xml with values to tally for to get tally response xml
				XmlParser.getProfitAndLoss(tallyXmlResponse);
			}else if(requestType.equals("daily profit")){
				fromDate="1-Dec-2014"; 
				toDate="31-Dec-2014"; 
				xmlWithValue = fillTemplate(path);	//fillTemplate method create xml with values
				String tallyXmlResponse = TallyRequest.SendToTally(xmlWithValue);		//passing xml with values to tally for to get tally response xml
				XmlParser.getDayWiseProfit(tallyXmlResponse);
			}else{
				xmlWithValue = fillTemplate(path);	//fillTemplate method create xml with values
				String tallyXmlResponse = TallyRequest.SendToTally(xmlWithValue);		//passing xml with values to tally for to get tally response xml
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
			productId="iphone 6"; 							    //we can change this value dynamically
			path= requestMap.get("getOneStock").toString();
		}else if(requestType.equals("show summary")){
			path= requestMap.get("getStockSummary").toString();
		}else if(requestType.equals("show pnl")){
			path= requestMap.get("getPNL").toString();
		}else if(requestType.equals("daily profit")){
			path = requestMap.get("getDayWiseProfit");
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
		requestMap.put("getOneStock", "templates\\fetch_individual_stock_item.xml");
		requestMap.put("createStock", "templates\\insert_stock_item.xml");
		requestMap.put("getPNL", "templates\\fetch_PNL_summary.xml");
		requestMap.put("getDayWiseProfit", "templates\\sales_voucher_details.xml");
		return requestMap;
	}
		
	//supply template parameter values returns xml as string
	private static VelocityEngine ve = new VelocityEngine();
	//returns xml with values keyd as string
	private synchronized static String fillTemplate(String path){
			ve.init();
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
	        context.put("toDate",toDate);
	        context.put("fromDate", fromDate);
	       
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
	
	/*below methods not used in the current code, will be updated further in future*/
	static List<Map<String,Map<String,Map<String,String>>>> requestList = new ArrayList<>();
	static Map<String, Map<String,Map<String,String>>> requestToTemplateMap = new HashMap<String, Map<String, Map<String,String>>>();
	
	public static void createOrAlterStockItem(String stockItemName, String stockItemParent, String stockItemOpeningBalance, String stockItemOpenginValue, String stockItemOpeningRate, String stockItemAction){				
		//create map to hold key value pari to be used in template engine
		Map<String,String> createOrAlterStockItemMap = new HashMap<String,String>();
		createOrAlterStockItemMap.put("stockItemName", stockItemName);
		createOrAlterStockItemMap.put("stockItemParent", stockItemParent);
		createOrAlterStockItemMap.put("stockItemOpeningBalance", stockItemOpeningBalance);
		createOrAlterStockItemMap.put("stockItemOpeingValue", stockItemOpenginValue);
		createOrAlterStockItemMap.put("stockItemOpeingRate", stockItemOpeningRate);
		createOrAlterStockItemMap.put("stockItemAction", stockItemAction);
		Map<String,Map<String,String>> xmlTemplateToDateMap = new HashMap<>();
		xmlTemplateToDateMap.put("/templates/import/crate_or_alter_stockitem.xml", createOrAlterStockItemMap);
		requestToTemplateMap.put("createStockItem", xmlTemplateToDateMap ); //adding map to map
		requestList.add(requestToTemplateMap);
	}
	
	public static void fetchDataFromTally(String id, String headerType){	
		//create map to hold key value pair template engine
		Map<String,String> fetchDataFromTallyMap = new HashMap<String,String>();
		String fetchList = ""
				+ "Name,"
				+ "Parent,"
				+ "BaseUnits,"
				+ "OpeningBalance,"
				+ "OpeningRate,"
				+ "OpeningValue,"
				+ "ClosingBalance,"
				+ "ClosingRate,"
				+ "ClosingValue";
		fetchDataFromTallyMap.put("id", id);	
		fetchDataFromTallyMap.put("collectionId", id);	
		Map<String,Map<String,String>> xmlTemplateToDateMap = new HashMap<>();

		if(headerType.equals("collection")){			
			fetchDataFromTallyMap.put("collectionType", "All Masters");
			fetchDataFromTallyMap.put("fetchList", fetchList);
			xmlTemplateToDateMap.put("/templates/export/collection_request_to_export.xml", fetchDataFromTallyMap);
		}
		if(headerType.equals("object")){			
			fetchDataFromTallyMap.put("idType", "Profit and Loss");
			xmlTemplateToDateMap.put("/templates/export/object_request_to_export.xml", fetchDataFromTallyMap);			
		}
		if(headerType.equals("data")){			
			xmlTemplateToDateMap.put("/templates/export/data_request_to_export.xml", fetchDataFromTallyMap);		
		}
		
		requestToTemplateMap.put("createStockItem", xmlTemplateToDateMap ); //adding map to map
		requestList.add(requestToTemplateMap);	
	}

	public static void templateToTally(String searchFor){
		ve.init();
		/*  create a context and add data */
        VelocityContext context = new VelocityContext();
        /*  next, get the Template  */
		Template vchTmpl = ve.getTemplate(path); //eg. path = "\\templates\\voucher.tmpl" 
        /*  create a context and add data */
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
		
//	return xmlTmpl;
		
	}
}
