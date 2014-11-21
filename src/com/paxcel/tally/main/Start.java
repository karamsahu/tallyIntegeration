package com.paxcel.tally.main;

import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import com.paxcel.tally.transaction.Transaction;

public class Start {
	private static String template= null;
	private static String listMasterTmpl= null;
	private static String errorTmpl= null;
	private static String responseTmpl= null;
	private static String requestType = null; // used for header element i.e import || export || execute
	private static String requestId=null; // name of request i.e Purchase || Trial Balance etc.

	private static HashMap<String, String> tagMap = null; 	
	public static void main(String[] args) {
		System.err.println("Enter request & press return key : ");
		Scanner sc = new Scanner(System.in);
		String request = sc.nextLine();
		sc.close();
		System.err.println("your request is : "+request);
		//call method to check type of request provided by user and perform related operation further
		checkRequest(request);
	}
			
	//to check for what purpose the request is made
	public static String checkRequest(String request){
		if(request!=null){
			if (request.equals("purchase order")){
				// create purchase voucher code
				template = "\\templates\\request\\voucher.tmpl";
				//set header values
				String version = "1";
				String tallyRequest = "Import";
				String subRequest = "";
				String type = "";
				String id = "";
				
				//set data tab value
				//retrive values for template file
				String fileName =template;
				
				//setting attribute of <DATA> tag
				String 	tagName = "Voucher Number";
				String 	tagValue = "3";
				//setting values for <DATA> tag
				String 	action = "Alter";
				String 	vchType = "Purchase";
				String 	narration = "being goods purchased";
				Date 	date = new java.util.Date();
				
				doTransaction(
						createHeader(version, tallyRequest, subRequest, type, id),// to crete values for xml header for po createion.
						createdataTag(tagName, tagValue, action, vchType, narration, date), //create map for dataDesc tag
						template);
			} else if(request.equals("add item")){
				template = "\\templates\\createInventory.xml";
				//set header values
				String version = "1";
				String tallyRequest = "Export";
				String subRequest = "";
				String type = "Data";
				String id = "Trial Balance";
				
				//setting attribute of <DATA> tag
				String 	tagName = "Voucher Number";
				String 	tagValue = "3";
				//setting values for <DATA> tag
				String 	action = "Alter";
				String 	vchType = "Purchase";
				String 	narration = "being goods purchased";
				Date 	date = new java.util.Date();
				
				doTransaction(
						createHeader(version, tallyRequest, subRequest, type, id), 
						createdataTag(tagName, tagValue, action, vchType, narration, date), //create map for dataDesc tag
						template);			
				
			}else if(request.equals("show stock")){
				template = "\\templates\\request\\stockItem.tmpl";
				//set header values
				String version = "1";
				String tallyRequest = "Export";
				String subRequest = "";
				String type = "Data";
				String id = "Trial Balance";
				
				//setting attribute of <DATA> tag
				String 	tagName = "Voucher Number";
				String 	tagValue = "3";
				//setting values for <DATA> tag
				String 	action = "Alter";
				String 	vchType = "Purchase";
				String 	narration = "being goods purchased";
				Date 	date = new java.util.Date();
				
				doTransaction(
						createHeader(version, tallyRequest, subRequest, type, id), 
						createdataTag(tagName, tagValue, action, vchType, narration, date), //create map for dataDesc tag
						template);			
				
			}else if (request.equals("sales order")){
				//create sales order code
				template = "\\templates\\request\\voucher.tmpl";
				//set header values
				String version = "1";
				String tallyRequest = "Import";
				String subRequest = "";
				String type = "";
				String id = "";
				
				//setting attribute of <DATA> tag
				String 	tagName = "Voucher Number";
				String 	tagValue = "3";
				//setting values for <DATA> tag
				String 	action = "Alter";
				String 	vchType = "Purchase";
				String 	narration = "being goods purchased";
				Date 	date = new java.util.Date();
				
				doTransaction(
						createHeader(version, tallyRequest, subRequest, type, id), 
						createdataTag(tagName, tagValue, action, vchType, narration, date), //create map for dataDesc tag
						template);
			}else if(request.equals("trial balance")){
				//create All list containing all inventory
				//set template 
				template = "\\templates\\request\\trialBalance.tmpl";
				//set header values
				String version = "1";
				String tallyRequest = "Export";
				String subRequest = "";
				String type = "Data";
				String id = "Trial Balance";
				
				//setting attribute of <DATA> tag
				String 	tagName = "Voucher Number";
				String 	tagValue = "3";
				//setting values for <DATA> tag
				String 	action = "Alter";
				String 	vchType = "Purchase";
				String 	narration = "being goods purchased";
				Date 	date = new java.util.Date();
				
				doTransaction(
						createHeader(version, tallyRequest, subRequest, type, id), 
						createdataTag(tagName, tagValue, action, vchType, narration, date), //create map for dataDesc tag
						template);			
				
			}else if(request.equals("inventory")){
				//create All list containing all inventory
				//set template 
				template = "\\templates\\request\\inventory.tmpl";
				//set header values
				String version = "1";
				String tallyRequest = "Export";
				String subRequest = "";
				String type = "Data";
				String id = "Stock Category Summary";
				
				//setting attribute of <DATA> tag
				String 	tagName = "Voucher Number";
				String 	tagValue = "3";
				//setting values for <DATA> tag
				String 	action = "Alter";
				String 	vchType = "Purchase";
				String 	narration = "being goods purchased";
				Date 	date = new java.util.Date();
				
				doTransaction(
						createHeader(version, tallyRequest, subRequest, type, id), 
						createdataTag(tagName, tagValue, action, vchType, narration, date), //create map for dataDesc tag
						template);			}else {
				System.err.println("Unknow request");
			}
			
		}else{
			System.err.println("value of request parameter missing");
		}
		return null;
	}
	
	//to perform transaction as per the request made
	public static void doTransaction(HashMap<String,String> headerMap, HashMap<String,String> dataMap,  String tmplName){
		TallyRequest tr = new TallyRequest();
		
		//sending xml with data to tally procesing engine
        String tallyRequest = Transaction.getReports(headerMap, dataMap,  template);
    	System.out.println(tallyRequest);
    	try {
			tr.SendToTally(tallyRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
	}
	
	//creating map to store data of data tag
	private static HashMap<String,String> createdataTag(String tagName, String tagValue, String action, String vchType, String narration, Date date){
		tagMap = new HashMap<String,String>();
		//tagName,tagValue,action,vchType,narration,date	
		tagMap.put("tagName",tagName);
		tagMap.put("tagValue",tagValue);
		tagMap.put("action",action);
		tagMap.put("vchType",vchType);
		tagMap.put("narration",narration);
		tagMap.put("date",date.toString());	
		return tagMap;
	}

	//to perform header creation 
	private static HashMap<String,String> createHeader(String version, String tallyRequest, String subRequest, String type, String id){
		tagMap = new HashMap<String, String>();
		tagMap.put("version", version);
		tagMap.put("tallyRequest", tallyRequest);
		tagMap.put("subRequest", subRequest);
		tagMap.put("type",type);
		tagMap.put("id", id);
		return tagMap;
	}

	
}
