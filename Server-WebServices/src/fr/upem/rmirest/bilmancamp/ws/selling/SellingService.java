/**
 * SellingService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.upem.rmirest.bilmancamp.ws.selling;

public interface SellingService extends java.rmi.Remote {
    public double change(java.lang.String currencyFrom, java.lang.String currencyTo, double amount) throws java.rmi.RemoteException;
    public boolean sellBook(long id, java.lang.String password, double price, java.lang.String currency) throws java.rmi.RemoteException;
}
