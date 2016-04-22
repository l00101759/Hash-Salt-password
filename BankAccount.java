package banksystem;

class BankAccount {
	
	private String password;
	private int accountNum;
    private volatile double balance;
 
    public BankAccount()
	{
		password = null;
		accountNum = 0;
		balance = 0;
	}
    
    public BankAccount(int ac, String pw, double bal)
	{
		password = pw;
		accountNum = ac;
		balance = bal;
	}
    //for database use (program stores balance, database stores acc+password
    public BankAccount(double bal)
	{
		balance = bal;
	}

    synchronized public double getBalance(){
        return balance;
    }
    
    
    synchronized void deposit(double amount)
	{
		double newBalance = this.balance + amount;
		setBalance(newBalance);
		
	}
    
    synchronized void setBalance(double amount)
    {
    	this.balance = amount;
    }
    synchronized void withdraw(double amount)
	{
		double bal = getBalance();
		
		if(bal > amount)	
			balance = balance - amount;
		else
			System.out.println("Insufficient Funds");
	}
     boolean validateUser(int ac, String pw) 
	{
		boolean flag = false;
		if(ac == this.accountNum && pw.equals(this.password))
	    {
	    	flag = true;
	    }
		
		return flag;
	}
}