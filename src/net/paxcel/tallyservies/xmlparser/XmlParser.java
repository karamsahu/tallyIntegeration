package net.paxcel.tallyservies.xmlparser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;

import net.paxcel.tallyservices.inventory.TallyRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class XmlParser {
	public static double costPrice;
	public static double sellingPrice;
	
	
	public static void getProfitAndLoss(String tallyXmlResponse) throws XMLStreamException{
		try {
			  //File file = new File("templates/response/pnl_response.xml");
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  //Document doc = db.parse(file);
			  InputSource is = new InputSource();
			   			  is.setCharacterStream(new StringReader(tallyXmlResponse)); //reading string as stream
			  Document doc = db.parse(is);
			  doc.getDocumentElement().normalize();
			  
			 // System.out.println("Root element " + doc.getDocumentElement().getNodeName());
			  
			  NodeList nodeLst = doc.getElementsByTagName("PLAMT");
			  
			  System.out.println("Information of all Profit and Loss account");
			    ArrayList<Object> pnlList = new ArrayList<Object>();

			  for (int s = 0; s < nodeLst.getLength(); s++) {

			    Node fstNode = nodeLst.item(s);

			    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {  
			      Element fstElmnt = (Element) fstNode;
			      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("BSMAINAMT");
			      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
			      NodeList fstNm = fstNmElmnt.getChildNodes();

			      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("PLSUBAMT");
			      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
			      NodeList lstNm = lstNmElmnt.getChildNodes();
			      
			      if(fstNm.item(0)!=null){
			    	  pnlList.add(s,fstNm.item(0).getNodeValue());
			    	  //System.out.println("BSMAINAMT : "  + pnlList.get(s)) ;
			      }else if(lstNm.item(0)!=null){
			    	  pnlList.add(s,lstNm.item(0).getNodeValue());
			    	  //System.out.println("PLSUBAMT : " + pnlList.get(s));
			      }else{
			    	  pnlList.add(s,0);
			    	  //System.out.println("Nod data");
			      }
			      			      
			    }
			    
			  }
			    double sales =Double.valueOf(pnlList.get(0).toString());
			    double cogs =Double.valueOf(pnlList.get(1).toString());
			    double openingStock =Double.valueOf(pnlList.get(2).toString());
			    double purchases =Double.valueOf(pnlList.get(3).toString());
			    double closingStock =Double.valueOf(pnlList.get(4).toString());
			    double indirectExpenses =Double.valueOf(pnlList.get(5).toString());
			    
			   double grossProfit = (sales-(closingStock)+(openingStock)+(purchases));
			   double netProfit = (grossProfit+(indirectExpenses));
			   
			   System.out.println("Total Sales  : " + sales);
			   System.out.println("Gross Profit : " +grossProfit);
			   System.out.println("Net Profit   : " +netProfit);
			   System.out.println("Cost of Sales : " +(-cogs));
			   
			  } catch (Exception e) {
			    e.printStackTrace();
			  }
		
	}
	
	public static void getProfitAndLoss(String tallyXmlResponse, String fromDate, String toDate){

		try {
			  //File file = new File("templates/response/pnl_response.xml");
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  //Document doc = db.parse(file);
			  InputSource is = new InputSource();
			   			  is.setCharacterStream(new StringReader(tallyXmlResponse)); //reading string as stream
			  Document doc = db.parse(is);
			  doc.getDocumentElement().normalize();
			  
			  //get the size of node
			  NodeList nodes = doc.getElementsByTagName("ALLINVENTORYENTRIES.LIST");
			  
			  for(int n=0; n<nodes.getLength(); n++){
				  Node node = nodes.item(n);
				  if (node.getNodeType() == Node.ELEMENT_NODE) {
					  Element element = (Element) node;
					  String name = getValue("STOCKITEMNAME", element);
					  String rate = getValue("RATE", element);
					  String disc = getValue("DISCOUNT", element);
					  String billAmt = getValue("AMOUNT", element);
					  String billQty = getValue("BILLEDQTY", element);
					  calculatePnlData(name,rate,disc,billAmt,billQty); // call method to calculate
					  }
				  System.out.println("------------------------------------------------------------");
			  }
			  
			  generatePnLReport();// print final pln report
			  

		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	static ArrayList<Double> profitArray = new ArrayList<Double>();

	static Map<String, Map<String,Object>> reportMap = new HashMap<String,Map<String,Object>>();
	
	static void generatePnLReport(){
		double totalprofit =0;
		int item = 0;
			for(Double profit : profitArray){
				totalprofit = totalprofit+profit;
				item++;
			}
			System.out.println("Total profit is Rs."+totalprofit+" for "+item+"s sold");
	}
	
	private static void calculatePnlData(String name, String rate, String disc,
			String billAmt, String billQty) throws NumberFormatException, Exception {
			String cp = getItemPrice(name);  
			costPrice = Double.parseDouble(cp.substring(0, cp.length()-4));
			/*System.out.println("-------------------------\n STOCK ITEM NAME: " + name);
			System.out.println("Stock Item cost : " +costPrice);
			System.out.println("RATE: " + rate);
			System.out.println("DISCOUNT: " + disc);
			System.out.println("Stock bill amount : " + billAmt);
			System.out.println("Stock bill Quantity:" + billQty+"\n----------------------------------------");*/
			double sellingPrice = Double.parseDouble(rate.substring(0, rate.length()-4));
			double discount = Double.parseDouble(disc);
			double vat = 0;
			
			
			double profit =  sellingPrice - vat - costPrice - discount;
			System.out.println(name +" : "+ profit);
			
			profitArray.add(profit);
			
			
			/*
			Map<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("name", name);
			dataMap.put("rate", rate);
			dataMap.put("disc", disc);
			dataMap.put("billAmt", billAmt);
			dataMap.put("cp", cp);
			dataMap.put("profit", profit);
			reportMap.put(name, dataMap);*/
	}

	//read response xml and fetch cost prices of stock item and returns cost price needs to be parsed as double
	public static String getStockInfo(String responseXmll){
		  String cp="";
		try {
			  //File file = new File("templates/response/pnl_response.xml");
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  //Document doc = db.parse(file);
			  InputSource is = new InputSource();
			   			  is.setCharacterStream(new StringReader(responseXmll.replace('&', ' '))); //reading string as stream
			  Document doc = db.parse(is);
			  doc.getDocumentElement().normalize();
			  
			  //get the size of node
			  NodeList nodes = doc.getElementsByTagName("TALLYMESSAGE");
			  System.out.println(nodes.getLength());
			  for(int n=0; n<nodes.getLength(); n++){
				  Node node = nodes.item(n);
				  if (node.getNodeType() == Node.ELEMENT_NODE) {
					  Element element = (Element) node;
					  cp = getValue("OPENINGRATE", element);
					  System.out.println("Item Cost :: " + cp );
					  }
			  }
		}catch(Exception e){
			e.printStackTrace();
		}	
		return cp;
	}
	
	//to get values of elements
	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
		}

	private static String getItemPrice(String ProductName) throws Exception{
		String responseXml = TallyRequest.SendToTally(
				TemplateMaker.makeTemplate("templates\\fetch_individual_stock_item.xml", ProductName)
				);
		//parse tally xml response to get costprice
		String costPrice = getStockInfo(responseXml);
		
		//Key productName in xml and get response
		return costPrice;
	}
	
	
	
}
