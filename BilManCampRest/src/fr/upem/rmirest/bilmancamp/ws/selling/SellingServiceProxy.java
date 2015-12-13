package fr.upem.rmirest.bilmancamp.ws.selling;

public class SellingServiceProxy implements fr.upem.rmirest.bilmancamp.ws.selling.SellingService {
  private String _endpoint = null;
  private fr.upem.rmirest.bilmancamp.ws.selling.SellingService sellingService = null;
  
  public SellingServiceProxy() {
    _initSellingServiceProxy();
  }
  
  public SellingServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initSellingServiceProxy();
  }
  
  private void _initSellingServiceProxy() {
    try {
      sellingService = (new fr.upem.rmirest.bilmancamp.ws.selling.SellingServiceServiceLocator()).getSellingService();
      if (sellingService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sellingService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sellingService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sellingService != null)
      ((javax.xml.rpc.Stub)sellingService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public fr.upem.rmirest.bilmancamp.ws.selling.SellingService getSellingService() {
    if (sellingService == null)
      _initSellingServiceProxy();
    return sellingService;
  }
  
  public boolean sellBook(long id, java.lang.String password, double price, java.lang.String currency) throws java.rmi.RemoteException{
    if (sellingService == null)
      _initSellingServiceProxy();
    return sellingService.sellBook(id, password, price, currency);
  }
  
  public double change(java.lang.String currencyFrom, java.lang.String currencyTo, double amount) throws java.rmi.RemoteException, java.lang.IllegalArgumentException{
    if (sellingService == null)
      _initSellingServiceProxy();
    return sellingService.change(currencyFrom, currencyTo, amount);
  }
  
  
}