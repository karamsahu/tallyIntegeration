package net.paxcel.odbc.tallyservices.inventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class TallyODBC {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Stock item name to get details :");
		String itemName = sc.nextLine();
		getStockItem(itemName);
	}
	
	private static  void getStockItem(String itemName){
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

            Connection con = DriverManager.getConnection("jdbc:odbc:TallyODBC64_9000", "", "");
            Statement stmt = (Statement) con.createStatement();
        String s = ""
            		+ "SELECT "
            		+ "StockItem.`$Parent`, "
            		+ "StockItem.`$Name`, "
            		+ "StockItem.`$OpeningRate`, "
            		+ "StockItem.`$BaseUnits`, "
            		+ "StockItem.`$OpeningBalance`, "
            		+ "StockItem.`$_InwardQuantity`, "
            		+ "StockItem.`$_OutwardQuantity`, "
            		+ "StockItem.`$_ClosingBalance` FROM "
            		+ "Demo.TallyUser.StockItem StockItem where StockItem.$Name LIKE '%"+itemName+"%' ORDER BY "
            		+ "StockItem.`$Parent`";
        String s1 ="select * from Stockitem";
        System.out.println(s1);
        stmt.executeQuery(s1);
            
         //stmt.executeQuery("SELECT $Parent, StockItem.$Name FROM StockItem  order by $parent");//("Select * from Demo.TallyUser.StockItem Where StockItem.$Name=");
            
          ResultSet rs1 = stmt.getResultSet();
          int numOfCol = rs1.getMetaData().getColumnCount();
          
          while(rs1.next()){
       	   for(int j=1; j<numOfCol; j++){
       		   if(rs1.getString(j)!=null){
       			   System.out.print(rs1.getMetaData().getColumnLabel(j)+"--");
       			   System.out.println(rs1.getString(j));
       		   }else{
       			   System.err.println("noData");
       		   }
       	   }
       	   System.err.println("\n----------------------------------------------------------");
       	   
          }
            
            /*int currentRow = rs.getRow();
            
            int rowCount = 1;
            int i=1;
            while(rs.next()){
           	 rs.getString(i);
           	 System.out.println(rs.getString(i).toString());
           	 i++;
            }
            */
            
           /* while (rs.next()) { 
                //for(int i = 0; i < 7; i++){
                     int op = Integer.parseInt(rs.getString("StockItem.`$OpeningBalance`").substring(0, (rs.getString("StockItem.`$OpeningBalance`").length() - 3)).trim());
                     int in = Integer.parseInt(rs.getString("StockItem.`$_InwardQuantity`").substring(0, (rs.getString("StockItem.`$_InwardQuantity`").length() - 3)).trim());
                     int out = Integer.parseInt(rs.getString("StockItem.`$_OutwardQuantity`").substring(0, (rs.getString("StockItem.`$_OutwardQuantity`").length() - 3)).trim());
                     int cl = Integer.parseInt(rs.getString("StockItem.`$_ClosingBalance`").substring(0, (rs.getString("StockItem.`$_ClosingBalance`").length() - 3)).trim());
                     System.out.println(rs.getString("StockItem.`$Parent`") + " \t " + rs.getString("StockItem.`$Name`") + " \t " + rs.getString("StockItem.`$BaseUnits`") + " \t " + op + " \t " + in + " \t " + out + " \t " + cl);
                //}             
              rowCount++;
            }*/
            //System.out.println(rowCount);
            stmt.close();
            con.close();
        } catch (Exception e) {
        System.out.println(e);
      }
	}

}
