package gov.uspto.openData.csService.service.s3;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.Upload;

import gov.uspto.openData.csService.model.FileData;
import gov.uspto.openData.csService.service.StorageService;

@Service
public class S3StorageServiceImpl implements StorageService {
	
	@Autowired
	private AmazonS3Client s3Client;
	@Autowired
	private AWSCredentials awsCredentials;

	public String writeFile(FileData fileData) {

		TransferManager tm = new TransferManager(awsCredentials);
		TransferManagerConfiguration txConf = new TransferManagerConfiguration();
		txConf.setMinimumUploadPartSize(6 * 1024 * 1024);
		txConf.setMultipartUploadThreshold(10 * 1024 * 1024);
		tm.setConfiguration(txConf);
		// generate a random key
		String storageId = UUID.randomUUID().toString();

		ObjectMetadata metadata = new ObjectMetadata();
		if (fileData.getContentLength() != null && fileData.getContentLength() != 0l) {
			metadata.setContentLength(fileData.getContentLength());
		}
		Upload upload = tm.upload(fileData.getApplicationName(), storageId, fileData.getFileContent(), metadata);
		try {
			upload.waitForCompletion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (AmazonClientException e) {

			e.printStackTrace();
		}

		return storageId;
	}

	public InputStream readFile(String applicationName, String storageId) {
		GetObjectRequest request = new GetObjectRequest(applicationName, storageId);
		S3Object object = s3Client.getObject(request);
		InputStream is = object.getObjectContent();
		return is;
	}

}
