import gov.uspto.openData.csClient.ContentStoreClient;
import gov.uspto.openData.csModel.dto.CreateFileResponse;
import gov.uspto.openData.csModel.dto.ReadFileResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import static org.junit.Assert.*;

import org.apache.commons.io.IOUtils;
import org.junit.Test;


public class ContentStoreClientTest {
	
	ContentStoreClient client = new ContentStoreClient("http://localhost:8080/cs-rest/rest");
	String applicationName = "uspto.edh";
	
	@Test
	public void testCrud() throws Exception {
		File file = new File("/Users/greensod/temp/pdf.pdf");
		CreateFileResponse createResponse = client.createFile(applicationName, file, "application/pdf");
		assertEquals(createResponse.getFileName(), "pdf.pdf");
		assertEquals(createResponse.getContentType(), "application/pdf");
		
		ReadFileResponse readResponse = client.readFile(applicationName, createResponse.getFileId());
		assertTrue(isContentSame(new FileInputStream(file), readResponse.getFileContent()));
		
		File file2 = new File("/Users/greensod/temp/image.jpg");
		client.updateFile(applicationName, createResponse.getFileId(), file2, "image/jpg");
		
		readResponse = client.readFile(applicationName, createResponse.getFileId());
		assertTrue(isContentSame(new FileInputStream(file2), readResponse.getFileContent()));
		
		client.deleteFile(applicationName, createResponse.getFileId());
		
		readResponse = client.readFile(applicationName, createResponse.getFileId());
		assertNull(readResponse);
	}
	
	@Test
	public void testRead(){
		ReadFileResponse readResponse = client.readFile(applicationName, 1234534223l);
		System.out.println(readResponse);
	}
	
	
	private boolean isContentSame(InputStream inputContent, InputStream savedContent) throws Exception {
		MessageDigest inputMd5 = MessageDigest.getInstance("MD5");
		InputStream inputIs = new DigestInputStream(inputContent, inputMd5);
		IOUtils.copy(inputIs, new ByteArrayOutputStream());
		String inputDigest = new String(inputMd5.digest());

		MessageDigest savedMd5 = MessageDigest.getInstance("MD5");
		InputStream savedIs = new DigestInputStream(savedContent, savedMd5);
		IOUtils.copy(savedIs, new ByteArrayOutputStream());
		String savedDigest = new String(savedMd5.digest());
		
		if(inputDigest.equals(savedDigest)){
			return true;
		}
		return false;
	}

}
