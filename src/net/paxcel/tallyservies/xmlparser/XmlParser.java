package net.paxcel.tallyservies.xmlparser;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class XmlParser {
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
}
