package com.cybersecurity.incidentresponsereadinessexcercises.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cybersecurity.incidentresponsereadinessexcercises.service.BatchTransactionRestService;

import lombok.Data;

@Data
public class Transaction {

	public static final String URGENT = "urgent";
	private final static Logger logger = LoggerFactory.getLogger(Transaction.class);	
	
	private String type = "queued";
	private String ibanSrc = "INVALID";
	private String ibanDst = "INVALID";
	private String currency = "INVALID";
	private double value = 0;
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getIbanSrc() {
		return ibanSrc;
	}
	public void setIbanSrc(String ibanSrc) {
		this.ibanSrc = ibanSrc;
	}
		
	public String getIbanDst() {
		return ibanDst;
	}
	public void setIbanDst(String ibanDst) {
		this.ibanDst = ibanDst;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void transfer() throws Exception {
		try {
			Thread.sleep(10000);
			this.value = 0;
			logger.info("Delivered money to "+ibanDst);			
		}
		catch (InterruptedException e) {
			throw e;
		}		
	}
	public static Transaction random() {
		Transaction t = new Transaction();
		t.setCurrency("USD");
		t.setIbanSrc("IBAN"+System.nanoTime()+"1");
		t.setIbanDst("IBAN"+System.nanoTime()+"2");
		t.setValue(System.nanoTime() % 10000 / 100);
		return t;
	}
}
