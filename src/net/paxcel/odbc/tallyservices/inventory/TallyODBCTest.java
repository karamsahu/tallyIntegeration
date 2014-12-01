package net.paxcel.odbc.tallyservices.inventory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TallyODBCTest {
	
     static DatabaseMetaData databaseMetaData;
     static Connection con;
	public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException{
		PrintStream out = new PrintStream(new FileOutputStream("d:\\output.txt"));
		System.setOut(out);
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		con = DriverManager.getConnection("jdbc:odbc:TallyODBC64_9000", "", "");       
		databaseMetaData = con.getMetaData();
		listTablesName();
	}
	static String schema="<schema>";
	public static  void listTablesName() throws SQLException{
		//Lising Tables
        String   catalog          = null;
        String   schemaPattern    = null;
        String   tableNamePattern = null;
        String[] types            = null;

        ResultSet result = databaseMetaData.getTables(
            catalog, schemaPattern, tableNamePattern, types );
        System.out.println("<tallyschema>");
        while(result.next()) {
            String tableName = result.getString(3);
            System.out.println("<table name=\""+tableName+"\">");
            listColumNames(tableName);
            System.out.println("</table>");
            
        }
        System.out.println("</tallyschema>");
	}
	
	public static void listColumNames(String tableName) throws SQLException{
		String   catalog           = null;
		String   schemaPattern     = null;
		String   tableNamePattern  = tableName;
		String   columnNamePattern = null;


		ResultSet result = databaseMetaData.getColumns(catalog, schemaPattern,  tableNamePattern, columnNamePattern);

		String columnNames="";
		while(result.next()){
		    String columnName = result.getString(4);
		    columnNames+=columnName+",";
		    //int    columnType = result.getInt(5);
		    System.out.println("\t<columname>"+columnName+"</columnname>");
		    //System.out.println("<columnType>"+columnType+"</columnType>");
		}
		 //listTableData(tableName, columnNames);
		
	}
	
	public static void listTableData(String tableName, String columNames) throws SQLException{
		Statement stmt = (Statement) con.createStatement();
        String s1 ="select "+columNames.substring(0, columNames.length()-1)+" from "+tableName;
        stmt.executeQuery(s1);
        
        ResultSet rs1 = stmt.getResultSet();
        int numOfCol = rs1.getMetaData().getColumnCount();
        System.err.println(tableName);
        System.err.println("\n----------------------------------------------------------");
        while(rs1.next()){
        	   for(int j=1; j<numOfCol; j++){
        		   if(rs1.getString(j)!=null){
        			   System.err.print(rs1.getMetaData().getColumnLabel(j)+"--> ");
        			   System.err.println(rs1.getString(j));
        		   }else{
        			   //System.err.println("noData");
        		   }
        	   }
        	   System.err.println("\n----------------------------------------------------------");
        	   
           }
	}
		
}
