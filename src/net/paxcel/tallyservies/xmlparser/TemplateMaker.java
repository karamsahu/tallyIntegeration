package net.paxcel.tallyservies.xmlparser;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class TemplateMaker {
	public static String makeTemplate(String templatePath, String productName)
	        throws Exception
	    {
	        /*  first, get and initialize an engine  */
	        VelocityEngine ve = new VelocityEngine();
	        ve.init();
	        /*  next, get the Template  */
	        Template t = ve.getTemplate(templatePath);
	        /*  create a context and add data */
	        VelocityContext context = new VelocityContext();
	        context.put("id", productName);
	        /* now render the template into a StringWriter */
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        /* show the World */
	        return writer.toString();     
	    }
}
