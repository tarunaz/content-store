package gov.uspto.openData.csService.service;

import gov.uspto.openData.csService.model.FileData;

import java.io.InputStream;

public interface StorageService {
	
	
	public String writeFile(FileData fileData);
	
	public InputStream readFile(String applicationName, String storageId);

}
