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
	public static String toDate,fromDate;
	public static double totalCost;
	public static double vat;
	public static double totalBillAmt;

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
	
	public static void getDayWiseProfit(String tallyXmlResponse){
		totalCost = 0;
		totalBillAmt=0;
		try {
			  //File file = new File("templates/response/pnl_response.xml");
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  //Document doc = db.parse(file);
			  InputSource is = new InputSource();
			   			  is.setCharacterStream(new StringReader(tallyXmlResponse)); //reading string as stream
			  Document doc = db.parse(is);
			  doc.getDocumentElement().normalize();
			  
			  
			  
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
					  String vat = getValue("TAXCLASSIFICATIONNAME", element);
					  if(vat.equals("")){
						  XmlParser.vat=0;
					  }else{
						  vat = vat.substring(vat.length()-2);
						  XmlParser.vat = Double.parseDouble(vat.substring(0, 1));
					  }
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
			
			
			System.out.println("Total cost is : "+ totalCost);
			System.out.println("Total Gross profit is Rs."+totalprofit+" for "+item+" items sold");
			double totalVat = totalBillAmt*vat/100;
			System.out.println("Total Vat amount : "+totalVat);
			double netProfit =  (totalprofit-totalVat) ;
			System.out.println("Total Net profit is Rs. : "+netProfit);
			double profitPercent = ((netProfit/totalCost)*100);
			System.out.println("Total profit percentage is : "+ profitPercent);
			System.out.println("Total Bill Amount :" + totalBillAmt);
			
		}
	
	private static void calculatePnlData(String name, String rate, String disc,
			String billAmt, String billQty) throws NumberFormatException, Exception {
			String cp = getItemPrice(name);  
			costPrice = Double.parseDouble(cp.substring(0, cp.length()-4));
			System.out.println("-------------------------\nItem Name : " + name);
			System.out.println("Item cost price : " +costPrice);
			System.out.println("Item selling price : " + rate);
			System.out.println("Item discount : " + disc);
			System.out.println("bill amount : " + billAmt);
			System.out.println("Quantity sold :" + billQty+"\n----------------------------------------");
			double qty = Double.parseDouble(billQty.substring(0,billQty.length()-3));
			double sellingPrice = Double.parseDouble(rate.substring(0, rate.length()-4));
			double discount = Double.parseDouble(disc);
			double cost = costPrice*qty;

			double profit =  (sellingPrice * qty) - cost - discount;
			System.out.println(name +" : "+ profit);
			totalCost = totalCost+cost;
			totalBillAmt = totalBillAmt+Double.parseDouble(billAmt);
			profitArray.add(profit);
			
			/*Map<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("name", name);
			dataMap.put("rate", rate);
			dataMap.put("disc", disc);
			dataMap.put("billAmt", billAmt);
			dataMap.put("billQty", billQty);
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
			  for(int n=0; n<nodes.getLength(); n++){
				  Node node = nodes.item(n);
				  if (node.getNodeType() == Node.ELEMENT_NODE) {
					  Element element = (Element) node;
					  cp = getValue("OPENINGRATE", element);
					  }
			  }
		}catch(Exception e){
			e.printStackTrace();
		}	
		return cp;
	}
	
	//to get values of elements
	private static String getValue(String tag, Element element) {
		String value="";
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		if(nodes.item(0)!=null){
			Node node = (Node) nodes.item(0);
			value = node.getNodeValue();
		}
		return value;
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
