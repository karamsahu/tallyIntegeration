package com.paxcel.tally.xmlparser;

import java.util.HashMap;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

//STAX XML parser
public class xmlParser {
	
	public static void main(String [] args) throws XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		HashMap<String,Object> map 	= null; //hold variables and their value
		List<Object> list 	= null; //  hold map
		String tagContent = null; // store data inside tags
		XMLStreamReader reader = factory.createXMLStreamReader(ClassLoader.getSystemResourceAsStream("d:\\response.xml"));

		while(reader.hasNext()){
			System.out.println(reader.next());
		}
		
		/*try {
			XMLStreamReader reader = factory.createXMLStreamReader(ClassLoader.getSystemResourceAsStream("\\templates\response.tmpl"));
			while(reader.hasNext()){
				int event = reader.next();
				switch(event){
				case XMLStreamConstants.START_ELEMENT:
					if("VERSION".equals(reader.getLocalName())){
						map.put("version", reader.getAttributeValue(0));
					}
					if("STATUS".equals(reader.getLocalName())){
						map.put("status", reader.getAttributeValue(1));
					}
					if("HEADER".equals(reader.getLocalName())){
						
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					tagContent = reader.getText().trim();
					break;
				case XMLStreamConstants.END_ELEMENT:
					switch(reader.getLocalName()){
					case 
					}
					
				}
			}
			
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
}
