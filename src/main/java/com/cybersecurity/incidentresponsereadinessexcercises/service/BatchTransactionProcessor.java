package com.cybersecurity.incidentresponsereadinessexcercises.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cybersecurity.incidentresponsereadinessexcercises.config.MainConfiguration;
import com.cybersecurity.incidentresponsereadinessexcercises.model.Transaction;

@Service
public class BatchTransactionProcessor {
	
	private final static Logger logger = LoggerFactory.getLogger(BatchTransactionProcessor.class);
	private final static LinkedList<Transaction> allTransactions = new LinkedList<>();
	static {
		for (int i=0; i < 1024*1024; i++) {
			allTransactions.add(Transaction.random());
		}
	}
	private final MainConfiguration cfg;
	
	public BatchTransactionProcessor(MainConfiguration cfg) {
		this.cfg = cfg;
	}
	
	@Scheduled(fixedDelay = 30000)
	public synchronized void processAll() throws IOException {
		_processAll();
	}
	
	public void _processAll() throws IOException {
		if (!new File(cfg.getBatchFolder()).exists()) {
			return;
		}
		Files.list(Path.of(cfg.getBatchFolder())).forEach( file -> {
			try {
				byte[] payload = Files.readAllBytes(file);
				Transaction t = TransactionProcessor.getTransaction(payload);
				allTransactions.add(t);
			}
			catch (Exception e) {}
			finally {
				try {
					Files.delete(file);
				} 
				catch (IOException e) {}
			}
		});
		allTransactions.forEach( t -> {
			try {
				logger.info("Transfer of "+t.getValue()+t.getCurrency()+" from IBAN "+t.getIbanSrc()+" to "+t.getIbanDst());
				t.transfer();
			}
			catch (Exception e) {}
		});
		Iterator<Transaction> it = allTransactions.iterator();
		while (it.hasNext()) {
			Transaction t = it.next();
			if (t.getValue() == 0) {
				it.remove();
			}
		}
	}
}
