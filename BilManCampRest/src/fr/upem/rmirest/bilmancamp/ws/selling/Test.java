package fr.upem.rmirest.bilmancamp.ws.selling;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class Test {

	public static void main(String[] args) throws RemoteException, ServiceException {
		
		
		System.out.println(new SellingServiceServiceLocator().getSellingService().change("EUR", "USD", 200));
	}
}
