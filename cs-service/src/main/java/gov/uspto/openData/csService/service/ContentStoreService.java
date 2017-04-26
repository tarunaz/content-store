package gov.uspto.openData.csService.service;

import gov.uspto.openData.csService.model.FileData;

public interface ContentStoreService {
	
	public FileData createFile(FileData fileData);
	
	public FileData readFile(String applicationName, Long fileId);
	
	public FileData updateFile(FileData fileData);
	
	public void deleteFile(String applicationName, Long fileId);

}
