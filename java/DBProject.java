/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;
import java.text.ParseException;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class DBProject {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of DBProject
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public DBProject (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end DBProject

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            DBProject.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      
      Greeting();
      DBProject esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the DBProject object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new DBProject (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add new customer");
				System.out.println("2. Add new room");
				System.out.println("3. Add new maintenance company");
				System.out.println("4. Add new repair");
				System.out.println("5. Add new Booking"); 
				System.out.println("6. Assign house cleaning staff to a room");
				System.out.println("7. Raise a repair request");
				System.out.println("8. Get number of available rooms");
				System.out.println("9. Get number of booked rooms");
				System.out.println("10. Get hotel bookings for a week");
				System.out.println("11. Get top k rooms with highest price for a date range");
				System.out.println("12. Get top k highest booking price for a customer");
				System.out.println("13. Get customer total cost occurred for a give date range"); 
				System.out.println("14. List the repairs made by maintenance company");
				System.out.println("15. Get top k maintenance companies based on repair count");
				System.out.println("16. Get number of repairs occurred per year for a given hotel room");
				System.out.println("17. < EXIT");

            switch (readChoice()){
				   case 1: addCustomer(esql); break;
				   case 2: addRoom(esql); break;
				   case 3: addMaintenanceCompany(esql); break;
				   case 4: addRepair(esql); break;//done
				   case 5: bookRoom(esql); break;//done
				   case 6: assignHouseCleaningToRoom(esql); break;//done
				   case 7: repairRequest(esql); break;
				   case 8: numberOfAvailableRooms(esql); break;
				   case 9: numberOfBookedRooms(esql); break;
				   case 10: listHotelRoomBookingsForAWeek(esql); break;
				   case 11: topKHighestRoomPriceForADateRange(esql); break;
				   case 12: topKHighestPriceBookingsForACustomer(esql); break;
				   case 13: totalCostForCustomer(esql); break;
				   case 14: listRepairsMade(esql); break;
				   case 15: topKMaintenanceCompany(esql); break;
				   case 16: numberOfRepairsForEachRoomPerYear(esql); break;
				   case 17: keepon = false; break;
				   default : System.out.println("Unrecognized choice!"); break;
            }//end switch
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main
   
   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice
   
   
  public static void addCustomer(DBProject esql){
	// Given customer details add the customer in the DB
	try {
     
      //~ System.out.println("Enter the customer ID:");
      //~ String cID = in.readLine();
      	String query = "SELECT MAX(customerID) FROM Customer";
		Statement stmt = esql._connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int cID = rs.getInt(1) + 1;
		//System.out.print(cID);
      System.out.println("Enter the first name:");
      String fN = in.readLine();
      System.out.println("Enter the last name:");
      String lN = in.readLine();
      System.out.println("Enter the customer's address:");
      String addr = in.readLine();
      System.out.println("Enter the phone number:");
      String phone = in.readLine();
      System.out.println("Enter the date of birth:");
      String DOB = in.readLine();
      System.out.println("Enter Male/Female/Other for gender:");
		String gender = in.readLine();
		query = "INSERT INTO Customer (customerID,fName,lName,Address,phNo,DOB,gender) VALUES (";
		query += cID + ",'" + fN + "','" + lN + "','" + addr + "'," + phone + ",'" + DOB + "','" + gender + "');";
		//System.out.println(query);
		esql.executeUpdate(query);

		//~ String temp = "SELECT C.fname FROM Customer C WHERE C.customerID = ";
		//~ temp += cID + ";";
		//~ esql.executeQuery(temp);
	}
	catch (Exception e){
		System.err.println (e.getMessage());
	}
   }//end addCustomer

   public static void addRoom(DBProject esql){
	  // Given room details add the room in the DB
	try {
     
      System.out.println("Enter the hotel ID:");
      String hID = in.readLine();
      String query = String.format("SELECT MAX(roomNo) FROM Room WHERE hotelID = '%s'", hID);
		Statement stmt = esql._connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int rID = rs.getInt(1) + 1;
		//System.out.println(rID);
      //~ System.out.println("Enter the room number:");
      //~ String rID = in.readLine();
      System.out.println("Enter the room type:");
		String rTy = in.readLine();
		query = "INSERT INTO ROOM (hotelID, roomNo, roomType) VALUES (";
		query += hID + "," + rID + ",'" + rTy + "');";
		//~ System.out.println(query);
		esql.executeUpdate(query);

		//~ String temp = "SELECT R.roomType FROM Room R WHERE R.hotelID = ";
		//~ temp += hID + "AND R.roomNo = " + rID + ";";
		//~ esql.executeQuery(temp);
	}
	catch (Exception e) {
		System.err.println(e.getMessage());
	}
	
   }//end addRoom

   public static void addMaintenanceCompany(DBProject esql){
      // Given maintenance Company details add the maintenance company in the DB
	try {
		String query = "SELECT MAX(cmpID) FROM MaintenanceCompany";
		Statement stmt = esql._connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int cmpID = rs.getInt(1) + 1;
		System.out.println(cmpID);
      query = "INSERT INTO MaintenanceCompany (cmpID,name,address,isCertified) VALUES (";
      //~ System.out.println("Enter the company ID:");
      //~ String cmpID = in.readLine();
      System.out.println("Enter the name of the company:");
      String name = in.readLine();
      System.out.println("Enter the address of the company:");
      String addr = in.readLine();
      System.out.println("Enter TRUE or FALSE if the company is certified:");
		String isCer = in.readLine();

		query += cmpID + ",'" + name + "','" + addr + "','" + isCer + "');";
		System.out.println(query);
		esql.executeUpdate(query);

		String temp = "SELECT M.name FROM MaintenanceCompany M WHERE M.cmpID = " + cmpID;
		esql.executeQuery(temp);
	}
	catch (Exception e) {
		System.err.println(e.getMessage());
	}
   }//end addMaintenanceCompany
	//DONE KATHLEEN MAYBE
 public static void addRepair(DBProject esql){
	  // Given repair details add repair in the DB
      // Your code goes here.
	try
	{
	  //Generate rID by incrementing from largest rID 
		String query = "SELECT MAX(rID) FROM Repair";
		Statement stmt = esql._connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int rID_i = rs.getInt(1) + 1;
		//System.out.println(rID_i); // Testing that rID_i increments properly
      //Read rID from User input
	    //~ System.out.print("\nEnter rID: ");
	    //~ int rID_i = Integer.parseInt(in.readLine());
	  //Read hotelID from User input
	  	System.out.print("\nEnter hotelID: ");
		int hotelID_i = Integer.parseInt(in.readLine());
	  //Read roomNo from User input
	  	System.out.print("\nEnter roomNo: ");
		int roomNo_i = Integer.parseInt(in.readLine());
	  //Read mCompany from User input
	  	System.out.print("\nEnter mCompany: ");
		int mCompany_i = Integer.parseInt(in.readLine());
	  //Read repairDate from User input
		System.out.print("\nEnter the repairDate (dd/mm/yyyy): ");
		//String repairDate_i = in.readLine();
		String dateFormat = "MM/dd/YYYY";
		Date repairDate_i = new SimpleDateFormat(dateFormat).parse(in.readLine());
		
		//Read description from User input
		System.out.print("\nEnter a description: ");
		String description_i = in.readLine();
	  
	  //Read repairType from User input
		System.out.print("\nEnter the repairType: ");
		String repairType_i = in.readLine();
		
		//Execute Query
		query = String.format("INSERT INTO Repair (rID, hotelID, roomNo, mCompany, repairDate, description, repairType) VALUES (%d, %d, %d, %d, '%s', '%s', '%s')", rID_i, hotelID_i, roomNo_i, mCompany_i, repairDate_i, description_i, repairType_i);
	    esql.executeUpdate(query);
	
	}
	catch(Exception e)
	{
		System.err.println(e.getMessage());
	}
		
   }//end addRepair
	//DONE KATHLEEN MAYBE
   public static void bookRoom(DBProject esql){
	  // Given hotelID, roomNo and customer Name create a booking in the DB 
      // Your code goes here.
		try
		{
		//Generate bID by incrementing from largest bID 	
		String query = "SELECT MAX(bID) FROM Booking";
		Statement stmt = esql._connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int bID_i = rs.getInt(1) + 1;
		//System.out.println(bID_i); // Testing that bID_i increments properly
    
		
		//Read hotelID from User input
	  	System.out.print("\nEnter hotelID: ");
		int hotelID_i = Integer.parseInt(in.readLine());
		
		//Read roomNo from User input
	  	System.out.print("\nEnter roomNo: ");
		int roomNo_i = Integer.parseInt(in.readLine());
		
		//Read Customer Name
		System.out.print("\nEnter the customer's first name: ");
		String fName_i = in.readLine();
		System.out.print("\nEnter the customer's last name: ");
		String lName_i = in.readLine();
		
		//Retrieve CustomerID
		query = String.format("SELECT C.customerID FROM Customer C WHERE fName = '%s' AND lName ='%s'", fName_i, lName_i);
		rs = stmt.executeQuery(query);
		rs.next();
		String customerID_i = rs.getString(1);
		//System.out.println(customerID_i); //Testing proper customerID retrieval
		

		//Read bookingDate from User input
		System.out.print("\nEnter the booking Date (dd/mm/yyyy): ");
		//String bookingDate_i = in.readLine();
		String dateFormat = "MM/dd/YYYY";
		Date bookingDate_i = new SimpleDateFormat(dateFormat).parse(in.readLine());
		 
		//Get Number of people from User
		System.out.print("\nEnter the number of people: ");
		int noOfPeople_i = Integer.parseInt(in.readLine());
		
		//Get Price from User
		System.out.print("\nEnter the price of the book: ");
		String price_i = in.readLine();
		query = String.format("INSERT INTO Booking (bID, customer, hotelID, roomNo, bookingDate, noOfPeople, price ) VALUES (%d, %d, %d, %d, %d, '%s', %d, %.2d)", bID_i, customerID_i, hotelID_i, roomNo_i, bookingDate_i, noOfPeople_i, price_i);
	    esql.executeUpdate(query);
	    System.out.print("END bookRoom");
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
   }//end bookRoom
	//DONE KATHLEEN MAYBE
   public static void assignHouseCleaningToRoom(DBProject esql){
	  // Given Staff SSN, HotelID, roomNo Assign the staff to the room 
      // Your code goes here.
		try
		{
		//Generate asgID by incrementing from largest asgID 	
		String query = "SELECT MAX(asgID) FROM Assigned";
		Statement stmt = esql._connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int asgID_i = rs.getInt(1) + 1;
		
		//System.out.println(asgID_i); // Testing that asgID_i increments properly
		
		//Read SSN from User input
	  	System.out.print("\nEnter Staff SSN: ");
		int staffID_i = Integer.parseInt(in.readLine());
		
		//Read hotelID from User input
	  	System.out.print("\nEnter hotelID: ");
		int hotelID_i = Integer.parseInt(in.readLine());
		
		//~ query = String.format("SELECT SSN FROM Staff WHERE SSN = %d AND employerID = %d", staffID_i, hotelID_i);
		//~ rs = stmt.executeQuery(query);
		//~ rs.next()
		//~ if(rs.next())
		//~ {
			//~ System.out.print("Staff is assigned to proper hotel");
		//~ }
		//~ else
		//~ {
			//~ System.out.print("Staff doesnt work at this hotel");
			//~ break;
		
		//Read roomNo from User input
	  	System.out.print("\nEnter roomNo: ");
		int roomNo_i = Integer.parseInt(in.readLine());
		
		query = String.format("INSERT INTO Assigned(asgID, staffID, hotelID, roomNo) VALUES (%d, %d, %d, %d)", asgID_i, staffID_i, hotelID_i, roomNo_i);
		esql.executeUpdate(query);
	}
	catch (Exception e)
	{
				System.err.println(e.getMessage());
	}
			
		
   }//end assignHouseCleaningToRoom
   
     public static void repairRequest(DBProject esql){
	  // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
	try {
      //INSERT INTO Repair (rID,hotelID,roomNo,mCompany,repairDate) VALUES ((SELECT MAX(rID) + 1 FROM Repair R), 1, 1, 0, '2000-1-1');
	  System.out.println("Enter the hotel ID:");
      String hotelID = in.readLine();
      System.out.println("Enter the staff SSN:");
      String staffssn = in.readLine();
      System.out.println("Enter the room number:");
      String roomNo = in.readLine();
      System.out.println("Enter the request date:");
      String date = in.readLine();
      String query = "INSERT INTO Repair (rID,hotelID,roomNo,mCompany,repairDate) VALUES ((SELECT MAX(rID) + 1 FROM Repair R)," + hotelID + ", " + roomNo + ",0,'2000-1-1')";
		
		System.out.println(query);
      esql.executeUpdate(query);

		String temp ="SELECT MAX(rID) FROM Repair R";
      esql.executeQuery(temp);
      
      String query1 = "INSERT INTO Request (reqID, managerID, repairID, requestDate) VALUES ((SELECT MAX(R.reqID) + 1 FROM Request R)," + staffssn + ", (SELECT MAX(rID) FROM Repair R),'" + date + "');";

      System.out.println(query1);
      esql.executeUpdate(query1);

      String temp1 ="SELECT MAX(reqID) FROM Request R";
      esql.executeQuery(temp1);
	}
	catch (Exception e){
		System.err.println(e.getMessage());
	}
   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){
      // Given a hotelID, get the count of rooms available
      //SELECT R FROM Room R WHERE R.hotelID = 381 AND R.roomno NOT IN (SELECT R.roomno FROM Booking B, Room R WHERE B.hotelID = 381 AND R.hotelID = 381 AND B.roomno = R.roomno);
      try {
          String query = "SELECT COUNT(R) FROM Room R WHERE R.hotelID = ";
          System.out.println("Enter the hotel ID:");
          String hotelID = in.readLine();
  
          query += hotelID + " AND R.roomno NOT IN (SELECT R.roomno FROM Booking B, Room R WHERE B.hotelID = " + hotelID +  "AND R.hotelID = " + hotelID + " AND B.roomno = R.roomno);";
          System.out.println(query);
          esql.executeQuery(query);
      } catch (Exception e) {
          System.err.println(e.getMessage());
      }
  }//end numberOfAvailableRooms
  
  public static void numberOfBookedRooms(DBProject esql){
      // Given a hotelID, get the count of rooms booked
      try {
          String query = "SELECT COUNT(B) FROM Booking B WHERE B.hotelId = ";
          System.out.println("Enter the hotel ID:");
          String hotelID = in.readLine();
  
          query += hotelID + ";";
          System.out.println(query);
          esql.executeQuery(query);
      } catch (Exception e) {
          System.err.println(e.getMessage());
      }
   }//end numberOfBookedRooms
  
   public static void listHotelRoomBookingsForAWeek(DBProject esql){
      // Given a hotelID, date - list all the rooms available for a week(including the input date) 
      try {
          String query = "SELECT B FROM Booking B WHERE B.hotelId = ";
          System.out.println("Enter the hotel ID:");
          String hotelID = in.readLine();
          System.out.println("Enter the starting date:");
          String date = in.readLine();
          
          query += hotelID + " and B.bookingDate <= (DATE '" + date + "' + INTERVAL '7 day') and B.bookingDate > '" + date + "';";
          System.out.println(query);
          esql.executeQuery(query);
      } catch (Exception e) {
          System.err.println(e.getMessage());
      }
   }//end listHotelRoomBookingsForAWeek
  
   public static void topKHighestRoomPriceForADateRange(DBProject esql){
      // List Top K Rooms with the highest price for a given date range
      try {
          String query = "SELECT B FROM Booking B WHERE B.bookingDate >= '";
          System.out.println("Enter first date:");
          String date1 = in.readLine();
          System.out.println("Enter second date:");
          String date2 = in.readLine();
          System.out.println("Enter max number of rooms to display:");
          String k = in.readLine();
  
          query += date1 + "' AND B.bookingDate <= '" + date2 + "' ORDER BY B.price DESC LIMIT " + k;
          System.out.println(query);
          esql.executeQuery(query);
      } catch (Exception e) {
          System.err.println(e.getMessage());
      }
   }//end topKHighestRoomPriceForADateRange
   
   //DONE KATHLEEN MAYBE
   public static void topKHighestPriceBookingsForACustomer(DBProject esql){
	  // Given a customer Name, List Top K highest booking price for a customer 
      // Your code goes here.
		try
		{
		//Read K from User input
	  	System.out.print("\nEnter input for K: ");
		int K_i = Integer.parseInt(in.readLine());
		
		//Read Customer Name
		System.out.print("\nEnter the customer's first name: ");
		String fName_i = in.readLine();
		System.out.print("\nEnter the customer's last name: ");
		String lName_i = in.readLine();
		
		//Retrieve CustomerID
		Statement stmt = esql._connection.createStatement();
		String query = String.format("SELECT customerID FROM Customer WHERE fName = '%s' AND lName ='%s'", fName_i, lName_i);
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int customerID_i = Integer.parseInt(rs.getString(1));
		
		query = String.format("SELECT B.bID, B.price FROM Booking B WHERE customer = %d ORDER BY B.price DESC LIMIT %d", customerID_i, K_i);
		rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData ();
        int numCol = rsmd.getColumnCount ();
        int rowCount = 0;

		while (rs.next()) {
			for(int i = 1; i <= numCol; ++i)
			{
				
				System.out.print(rsmd.getColumnName(i) + " = " + rs.getString(i));
				System.out.print("\t");
				++rowCount;
			}
			System.out.println();
		}
	}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		
		
   }//end topKHighestPriceBookingsForACustomer
   //DONE KATHLEEN MAYBE
   public static void totalCostForCustomer(DBProject esql){
	  // Given a hotelID, customer Name and date range get the total cost incurred by the customer
      // Your code goes here.
      try
      {
		//Read Customer Name
		System.out.print("\nEnter the customer's first name: ");
		String fName_i = in.readLine();
		System.out.print("\nEnter the customer's last name: ");
		String lName_i = in.readLine();
		
		//Retrieve CustomerID
		Statement stmt = esql._connection.createStatement();
		String query = String.format("SELECT customerID FROM Customer WHERE fName = '%s' AND lName ='%s'", fName_i, lName_i);
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int customerID_i = Integer.parseInt(rs.getString(1));
		
		//Read hotelID from User input
	  	System.out.print("\nEnter hotelID: ");
		int hotelID_i = Integer.parseInt(in.readLine());
		
		//Read start date from User input
		System.out.print("\nEnter the start Date (dd/mm/yyyy): ");
		//String start Date_i = in.readLine();
		String dateFormat = "MM/dd/YYYY";
		Date startDate_i = new SimpleDateFormat(dateFormat).parse(in.readLine());
		
		//Read end date from User input
		System.out.print("\nEnter the end Date (dd/mm/yyyy): ");
		//String end Date_i = in.readLine();
		Date endDate_i = new SimpleDateFormat(dateFormat).parse(in.readLine());
		
		query = String.format("SELECT SUM(price) FROM Booking WHERE hotelID = %d AND customer = %d AND bookingDate >= '%s' AND bookingDate <= '%s'", hotelID_i, customerID_i, startDate_i, endDate_i);
		rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData ();
        int numCol = rsmd.getColumnCount ();
        int rowCount = 0;

		while (rs.next()) {
			for(int i = 1; i <= numCol; ++i)
			{
				System.out.print(rsmd.getColumnName(i) + " = " + rs.getString(i) + "\t");
				++rowCount;
			}
			System.out.println();
		}
	  }
	  catch (Exception e)
	  {
		  System.err.println(e.getMessage());
	  }
		
   }//end totalCostForCustomer   
   //DONE KATHLEEN MAYBE
   public static void listRepairsMade(DBProject esql){
	  // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
      // Your code goes here.
		//Read Company Name
		try
		{
		System.out.print("\nEnter the company's name: ");
		String name_i = in.readLine();
		
		//Retrieve cmpID
		Statement stmt = esql._connection.createStatement();
		String query = String.format("SELECT cmpID FROM MaintenanceCompany WHERE name ='%s'", name_i);
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int cmpID_i = Integer.parseInt(rs.getString(1));
      
		query = String.format("SELECT rID, hotelID, roomNo, repairType FROM Repair WHERE mCompany = %d ORDER BY hotelID DESC", cmpID_i);
		rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData ();
        int numCol = rsmd.getColumnCount ();
        int rowCount = 0;

		while (rs.next()) {
			for(int i = 1; i <= numCol; ++i)
			{
				System.out.print(rsmd.getColumnName(i) + " = " + rs.getString(i) + "\t");
				++rowCount;
			}
			System.out.println();
		}
	}
	catch(Exception e)
	{
		System.err.println(e.getMessage());
	}
			
   }//end listRepairsMade
   //DONE KATHLEEN MAYBE
   public static void topKMaintenanceCompany(DBProject esql){
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      // Your code goes here.
	try{
		//Read K from User input
	  	System.out.print("\nEnter input for K: ");
		int K_i = Integer.parseInt(in.readLine());
		
		Statement stmt = esql._connection.createStatement();
		String query = String.format("SELECT C.name, COUNT(R.rid) FROM MaintenanceCompany C, Repair R WHERE C.cmpID = R.mCompany GROUP BY C.name ORDER BY count(R.rid) DESC LIMIT %d", K_i);
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData ();
        int numCol = rsmd.getColumnCount ();
        int rowCount = 0;
        while (rs.next()) {
			for(int i = 1; i <= numCol; ++i)
			{
				System.out.print(rsmd.getColumnName(i) + " = " + rs.getString(i));
				System.out.print("\t");
				++rowCount;
			}
			System.out.println();
		}
	}
	 catch(Exception e)
	{
		System.err.println(e.getMessage());
	}
		
		
   }//end topKMaintenanceCompany
  //DONE KATHLEEN MAYBE 
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){
	  // Given a hotelID, roomNo, get the count of repairs per year
      // Your code goes here.
 
		try
		{
		//Read hotelID from User input
	  	System.out.print("\nEnter hotelID: ");
		int hotelID_i = Integer.parseInt(in.readLine());
		//Read roomNo from User input
	  	System.out.print("\nEnter roomNo: ");
		int roomNo_i = Integer.parseInt(in.readLine());
		
		String query = String.format("SELECT COUNT(rID), Extract(YEAR FROM repairDate) FROM Repair WHERE hotelID = %d AND roomNo = %d GROUP BY Extract(YEAR FROM repairDate)", hotelID_i, roomNo_i);
		Statement stmt = esql._connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData ();
        int numCol = rsmd.getColumnCount ();
        int rowCount = 0;
        while (rs.next()) {
			for(int i = 1; i <= numCol; ++i)
			{
				System.out.print(rsmd.getColumnName(i) + " = " + rs.getString(i));
				System.out.print("\t");
				++rowCount;
			}
			System.out.println();
		}	
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
   }//end listRepairsMade

}//end DBProject
