package fr.upem.rmirest.bilmancamp.bank.poxy;

public class BankServiceProxy implements fr.upem.rmirest.bilmancamp.bank.poxy.BankService {
  private String _endpoint = null;
  private fr.upem.rmirest.bilmancamp.bank.poxy.BankService bankService = null;
  
  public BankServiceProxy() {
    _initBankServiceProxy();
  }
  
  public BankServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initBankServiceProxy();
  }
  
  private void _initBankServiceProxy() {
    try {
      bankService = (new fr.upem.rmirest.bilmancamp.bank.poxy.BankServiceServiceLocator()).getBankService();
      if (bankService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)bankService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)bankService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (bankService != null)
      ((javax.xml.rpc.Stub)bankService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public fr.upem.rmirest.bilmancamp.bank.poxy.BankService getBankService() {
    if (bankService == null)
      _initBankServiceProxy();
    return bankService;
  }
  
  public double change(java.lang.String currencyFrom, java.lang.String currencyTo, double amount) throws java.rmi.RemoteException{
    if (bankService == null)
      _initBankServiceProxy();
    return bankService.change(currencyFrom, currencyTo, amount);
  }
  
  public double balance(long id, java.lang.String password, java.lang.String currency) throws java.rmi.RemoteException{
    if (bankService == null)
      _initBankServiceProxy();
    return bankService.balance(id, password, currency);
  }
  
  public boolean withdraw(long id, java.lang.String password, java.lang.String currency, double amount) throws java.rmi.RemoteException{
    if (bankService == null)
      _initBankServiceProxy();
    return bankService.withdraw(id, password, currency, amount);
  }
  
  public long createAccount(java.lang.String currency, java.lang.String password) throws java.rmi.RemoteException{
    if (bankService == null)
      _initBankServiceProxy();
    return bankService.createAccount(currency, password);
  }
  
  public boolean deposit(long id, java.lang.String password, java.lang.String currency, double amount) throws java.rmi.RemoteException{
    if (bankService == null)
      _initBankServiceProxy();
    return bankService.deposit(id, password, currency, amount);
  }
  
  
}