/**
 * BankService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.upem.rmirest.bilmancamp.bank.poxy;

public interface BankService extends java.rmi.Remote {
    public double change(java.lang.String currencyFrom, java.lang.String currencyTo, double amount) throws java.rmi.RemoteException;
    public double balance(long id, java.lang.String password, java.lang.String currency) throws java.rmi.RemoteException;
    public boolean withdraw(long id, java.lang.String password, java.lang.String currency, double amount) throws java.rmi.RemoteException;
    public long createAccount(java.lang.String currency, java.lang.String password) throws java.rmi.RemoteException;
    public boolean deposit(long id, java.lang.String password, java.lang.String currency, double amount) throws java.rmi.RemoteException;
}
