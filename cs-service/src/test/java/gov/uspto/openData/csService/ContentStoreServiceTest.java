package gov.uspto.openData.csService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import gov.uspto.openData.csService.model.FileData;
import gov.uspto.openData.csService.service.ContentStoreService;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ServiceConfigurationTest.class, loader=AnnotationConfigContextLoader.class)
public class ContentStoreServiceTest {
	
	@Autowired
	private ContentStoreService csService;
	
	@Test
	public void testCreateAndReadFile() throws Exception {
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		FileData fileData = new FileData();
		fileData.setApplicationName("uspto.airs");
		fileData.setFileName("pdf.pdf");
		
		InputStream is = new FileInputStream(new File("/Users/greensod/temp/pdf.pdf"));
		is = new DigestInputStream(is, md);
		fileData.setFileContent(is);
		fileData = csService.createFile(fileData);
		Long fileId = fileData.getFileId();
		Assert.assertNotNull(fileId);
		String requestDigest = new String(md.digest());
		
		MessageDigest responseMd = MessageDigest.getInstance("MD5");
		fileData = csService.readFile("uspto.airs", 1l);
		FileOutputStream fos = new FileOutputStream(new File("/Users/greensod/temp/pdf1.pdf"));
		IOUtils.copy(new DigestInputStream(fileData.getFileContent(), responseMd), fos);
		String responseDigest = new String(responseMd.digest());
		
		Assert.assertEquals(requestDigest, responseDigest);
	}
	

}
