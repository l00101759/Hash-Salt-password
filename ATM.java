
package banksystem;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Statement;

public class ATM{
	
	final static String dbURL = "jdbc:mysql://localhost/bank";
	static Connection conn = null;

	static BankAccount newAccount = new BankAccount();
	
    public static void main(String[] args) 
    {
    	Scanner kb = new Scanner(System.in);
    	Password password = new Password();

    	BankAccount newAccount = null; //create account
    	
    	//show welcome message - create account method - login method
    	int option = 0;

		System.out.println("--Welcome to Bank--\n Create Account  ");
		
		getConnection();//connect to database
		
		newAccount = createAccount(); //add account/password to DB
		
		System.out.println("\n-- Please Login using acc num and password--");
		if(logIn()) //if user exists then allow them access to main menue
		{
			System.out.println("1 - Deposit  \t\t\t Withdraw - 2");
			option = kb.nextInt();
			kb.nextLine();//clear the buffer
			System.out.println("Enter amount: ");
			double amt = kb.nextDouble();
			
			//create a Transaction with account and amount of 
	        Transaction w = new Transaction(newAccount,amt, option);
	        
	        //Two threads represent joint Account
	        Thread firstWithdraw = new Thread(w);
	        Thread secondWithdraw = new Thread(w);
	        
	        firstWithdraw.setName(" LOG IN 1 ");
        	secondWithdraw.setName(" LOG IN 2 ");
        	
			firstWithdraw.start();			
			secondWithdraw.start();
			
	        try 
	        {
				firstWithdraw.join();			
				secondWithdraw.join();			
			}
	        catch (InterruptedException e) 
	        {
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Log in Failed, Goodbye");
		}
    }
    //method creates an account
    //asks user for account num and password
    //pass is hashed and stored as variable(bad)
    public static BankAccount createAccount()
    {
    	Scanner kb = new Scanner(System.in);
    	Password password = new Password();
    	
    	newAccount = new BankAccount();
    	
    	boolean flag = false;
    	String pwInp;
    	String acNumInp;
    	int acNum = 0;
    	do
    	{
	    	System.out.print("Account Num: ");
			acNumInp = kb.next();				
			System.out.println("Password: ");//get password
			pwInp = kb.next();//******nextLine caused empty empty line to has d41d8cd98f00b204e9800998ecf8427e
			kb.nextLine();//clear the buffer
		//	pwInp = pwInp.trim();//ensure no spaces
			try
			{
				acNum = Integer.parseInt(acNumInp);//validation
				flag = true; //exit loop
			}
			catch(NumberFormatException  e)
			{
				System.out.println("Error! Enter an Integer value for account number");
			}
			
    	}while(flag != true);

		//connect / hash Password / add User
		String hashPass = null;
		byte[] salt;
		byte [] hashSalt;
		
		
		try {
			
			salt = password.getSalt(); //get a SecureRandom salt
			
			hashSalt = password.getHash(pwInp, salt);//get the hash of password and salt
			String saltString = new String(salt);//convert the salt to String for storage
			hashPass = new String(hashSalt);//convert the byte[] to a string for storage
			
			//previous method of hash
			//hashPass = saltString + pwInp; //prepend salt to password
			//hashPass = password.hashPassword(hashPass);//hash salt+password 

			newAccount = new BankAccount(500); //default balance of 500 for the new account
			addUser(acNum, hashPass, saltString);//add user to database with salt
	
		} 
		catch (NoSuchAlgorithmException | UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}

		return newAccount;
    	
    }
    //user asked for login details
    //validation occurs - inputs tested against stored value (hash is done again)
    //return true or false
    static boolean logIn ()
	{
		Scanner kb = new Scanner(System.in);
		//BankAccount account = new BankAccount();
		Password password = new Password();
		
		Boolean flag = false;
		Boolean flagParse = false;
		String acNumInp;
		String pwInp; 
		int acNum = 0;
		
		do
		{
			System.out.print("Account Num: ");//get acc number
			acNumInp = kb.next();
			
			System.out.println("Password: ");//get password
			pwInp = kb.next();
			//pwInp = pwInp.trim();//ensure no spaces
			
			try
			{
				acNum = Integer.parseInt(acNumInp);//validation
				flagParse = true; //exit loop
			}
			catch(NumberFormatException e)
			{
				System.out.println("Error! Enter an Integer value for account number");
			}
			
		}while(flagParse != true);
		
		
		String hashPass = null;
		String salt;
		byte [] hashSalt;
		byte [] saltByte;
		
		try
		{
			salt = getSaltDB(acNum);//get salt from DB of matching account number
			saltByte = salt.getBytes();//convert back to bytes

			//hashPass = saltString + pwInp; //prepend salt to password
			//hashPass = password.hashPassword(hashPass);//hash salt+password
			
			hashSalt = password.getHash(pwInp, saltByte);//get the hash of password and salt
			
			hashPass = new String(hashSalt);//convert the byte[] to a string for storage

			if(validateUser(acNum, hashPass))
			{
				flag = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Error: Account Number + Password need to be integers");
			flag = false ;	
		}
		//finally
		//{
		//	kb.nextLine();//clear the buffer
		//}
		return flag;
	}
    public static void getConnection() {
    	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("ok connected");
			conn = DriverManager.getConnection(dbURL, "root", "");					
			//conn.close();	
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("where is this driver ???");
			System.out.println("ok not connected");
			e.printStackTrace();
		} 
	}
	public void close()  {
		try {				
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static String getSaltDB(int ac)  {
		
		String saltFromDB = null;
		
		String selectSQL = "SELECT salt FROM credentials WHERE accountNum = ?";
	    PreparedStatement preparedStatement;
		try {
			
			preparedStatement = conn.prepareStatement(selectSQL);
		    preparedStatement.setInt(1, ac);
		    
		    ResultSet rs = preparedStatement.executeQuery();
		    
		    while (rs.next()) {
		    	saltFromDB = rs.getString("salt");
		    		
		    }
		}
	    catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saltFromDB; 
	}
	//add user to the database - username and password(hashed)
	public static void addUser(int ac, String pw, String salt){

		try{
			PreparedStatement addUser = conn.prepareStatement("insert into credentials values(?,?,?)");
			addUser.setInt(1,ac);
			addUser.setString(2,pw);
			addUser.setString(3,salt);
			addUser.executeUpdate();
		}
		catch(SQLException e)
		{
			System.out.println("User exists");
		}

	}
	//check if user exists - rehash and check for match
	public static boolean validateUser(int ac, String pw) {
		
		boolean flag = false;

	    //shows login easiness
	    //ac = 100;
	    //pw = "admin' OR '1'='1";

	    //PREPARED STATEMENT
		String SQL = "SELECT * FROM credentials WHERE accountNum = ? and password = ?";
		
	    PreparedStatement preparedStatement;
		try {
				preparedStatement = conn.prepareStatement(SQL);
			 
		    preparedStatement.setInt(1, ac);
		    preparedStatement.setString(2, pw);
		    
		    ResultSet rs = preparedStatement.executeQuery();
		    
		    if(rs.next())
		    {
		    	flag = true;	
		    }
		    
		}
	    catch (SQLException e) {
			e.printStackTrace();
		}
	   
		return flag;
	}
}
