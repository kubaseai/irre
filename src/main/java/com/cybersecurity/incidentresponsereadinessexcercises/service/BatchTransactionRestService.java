package com.cybersecurity.incidentresponsereadinessexcercises.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cybersecurity.incidentresponsereadinessexcercises.config.MainConfiguration;
import com.cybersecurity.incidentresponsereadinessexcercises.model.Transaction;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
//@RequiredArgsConstructor
@OpenAPIDefinition(
	info = @Info(
		title = "Transaction batch service",
		version = "0.9"
	)
)
public class BatchTransactionRestService {
	
	private final static Logger logger = LoggerFactory.getLogger(BatchTransactionRestService.class);
	private final MainConfiguration cfg;
	
	public BatchTransactionRestService(MainConfiguration cfg) {
		this.cfg = cfg;
	}
	
	@Secured("ROLE_USER")
	@Operation(summary = "This operation queues transaction batch" /* in a local folder */)
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Success", 
	    content = { @Content(mediaType = "text/plain", 
	      schema = @Schema(implementation = Transaction.class)) })})
	@RequestMapping(value = "/rest/api/1.0/transaction/{id}",
			method = RequestMethod.POST,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> queueTransaction(HttpServletRequest req, @PathVariable("id") String id, @RequestHeader(name="BatchId",required=false) String batchId) throws Exception {
		String path = (batchId!=null ? cfg.getBatchFolder() + "/" + batchId : cfg.getBatchFolder()) + "/" + id;
		if (batchId!=null) {
			Paths.get(path).getParent().toFile().mkdir();
		}
		logger.info("Processing file "+path);
		OutputStream os = null;
		
		try (InputStream is = req.getInputStream(); OutputStream fos = new FileOutputStream(new File(path))) {
			byte[] buff = new byte[4096];
			int n = 0;
			while ((n = is.read(buff)) > 0) {
				String preamble = new String(buff, 0, n);
				if (preamble.contains(Transaction.URGENT) && os==null) {
					fos.close();
					os = new ByteArrayOutputStream();
				}
				else if (os==null) {
					os = fos;
				}
				os.write(buff, 0, n);				
			}
			os.flush();
			os.close();
			String status = "Queued";
			if (os instanceof ByteArrayOutputStream) { // urgent
				status = TransactionProcessor.process(((ByteArrayOutputStream)os).toByteArray()) ?
					"Success" : "Failure";
			}
			logger.info("Status for "+id+" is "+status);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
	}
}