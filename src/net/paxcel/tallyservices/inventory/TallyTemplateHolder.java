package net.paxcel.tallyservices.inventory;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class TallyTemplateHolder {
	VelocityEngine ve = new VelocityEngine();
	
	private Map<String, String> templateLocation = new HashMap<String, String>();
	//private String templatePath = "\\templates\\request\";
	
	Map<String, Map<String, String>> sendData = new HashMap<String, Map<String, String>>();
	
	public TallyTemplateHolder() {
		ve.init();
	}
	
	private void loadAllTemplate (){
		
		/// list all files of dir
		/// xml files
		// create template 
		// store in local map
		
	}
	
	public synchronized String getRequestData (String action, Map<String, String> replaceData){
		String file = templateLocation.get(action);
		VelocityContext context = new VelocityContext();
		while(replaceData.keySet().iterator().hasNext()){
			String key = replaceData.keySet().iterator().next();
			context.put(key, replaceData.get(key));
		}
		
		Template t = ve.getTemplate(file);
		StringWriter sw = new StringWriter();
		t.merge(context, sw);
		return sw.toString();
		
	}

	

	
}
