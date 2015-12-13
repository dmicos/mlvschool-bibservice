/**
 * SellingServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.upem.rmirest.bilmancamp.ws.selling;

public class SellingServiceServiceLocator extends org.apache.axis.client.Service implements fr.upem.rmirest.bilmancamp.ws.selling.SellingServiceService {

    public SellingServiceServiceLocator() {
    }


    public SellingServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SellingServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SellingService
    private java.lang.String SellingService_address = "http://localhost:8080/BilManCampRest/services/SellingService";

    public java.lang.String getSellingServiceAddress() {
        return SellingService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SellingServiceWSDDServiceName = "SellingService";

    public java.lang.String getSellingServiceWSDDServiceName() {
        return SellingServiceWSDDServiceName;
    }

    public void setSellingServiceWSDDServiceName(java.lang.String name) {
        SellingServiceWSDDServiceName = name;
    }

    public fr.upem.rmirest.bilmancamp.ws.selling.SellingService getSellingService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SellingService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSellingService(endpoint);
    }

    public fr.upem.rmirest.bilmancamp.ws.selling.SellingService getSellingService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            fr.upem.rmirest.bilmancamp.ws.selling.SellingServiceSoapBindingStub _stub = new fr.upem.rmirest.bilmancamp.ws.selling.SellingServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getSellingServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSellingServiceEndpointAddress(java.lang.String address) {
        SellingService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (fr.upem.rmirest.bilmancamp.ws.selling.SellingService.class.isAssignableFrom(serviceEndpointInterface)) {
                fr.upem.rmirest.bilmancamp.ws.selling.SellingServiceSoapBindingStub _stub = new fr.upem.rmirest.bilmancamp.ws.selling.SellingServiceSoapBindingStub(new java.net.URL(SellingService_address), this);
                _stub.setPortName(getSellingServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SellingService".equals(inputPortName)) {
            return getSellingService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://selling.ws.bilmancamp.rmirest.upem.fr", "SellingServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://selling.ws.bilmancamp.rmirest.upem.fr", "SellingService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SellingService".equals(portName)) {
            setSellingServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
