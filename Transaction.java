package banksystem;
class Transaction implements Runnable{

    private BankAccount acc;
    private double amount;
    
    private int depositOrWithdraw;

    public Transaction(){
        acc = null;
        amount = 0;
    }

    public Transaction(BankAccount acc,double amount, int depositOrWithdraw){
        this.acc = acc;
        this.amount = amount;
        this.depositOrWithdraw = depositOrWithdraw;
    }

    public void run() 
    {
    	if(depositOrWithdraw == 1)//deposit
    	{
	        for(int i =0; i<2; i++)
	        {
	            try
	            {
	            	//show balance + withdraw + show balance
	            	System.out.println(Thread.currentThread()+ " has access");
		            System.out.println("Balance: €"+acc.getBalance());
		            acc.deposit(amount);
		            System.out.println("Deposit: € "+amount);
		            System.out.println("Balance: €"+acc.getBalance()+"\n");
		            
	                Thread.sleep(500);
	            }
	            catch (InterruptedException e) 
	            {
	                e.printStackTrace();
	            } 
	            
	        }
    	}
    
    	else//withdraw
    	{
	        for(int i =0; i<2; i++)
	        {
	            try
	            {
	            	//show balance + withdraw + show balance
	            	System.out.println(Thread.currentThread()+ " has access");
	            	System.out.println("Balance: €"+acc.getBalance());
		            acc.withdraw(amount);
		            System.out.println("Withdraw: € "+amount);
		            System.out.println("Balance: €"+acc.getBalance()+"\n");
		            
		            Thread.sleep(500);
	            }
	            catch (InterruptedException e) 
	            {
	                e.printStackTrace();
	            } 
	            
	        }
    	}
    }

}