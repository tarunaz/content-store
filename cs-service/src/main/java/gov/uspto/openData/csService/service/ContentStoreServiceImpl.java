package gov.uspto.openData.csService.service;

import gov.uspto.openData.csModel.exception.NotFoundException;
import gov.uspto.openData.csService.model.FileData;
import gov.uspto.openData.csService.model.FileVersion;

import java.io.InputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContentStoreServiceImpl implements ContentStoreService {
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private DatabaseService databaseService;
	
	public FileData createFile(FileData fileData) {
		// store the input stream and get path
		String storageId = storageService.writeFile(fileData);
		fileData.setStorageId(storageId);
		fileData.setCreatedDate(new Date());
		
		databaseService.saveFileMetadata(fileData);
		
		return fileData;
	}
	
	
	public FileData readFile(String applicationName, Long fileId) {
		FileData fileData = databaseService.getFileMetadataById(fileId);
		if(fileData == null){
			throw new NotFoundException(); 
		}
		InputStream fileContent = storageService.readFile(applicationName, fileData.getStorageId());
		fileData.setFileContent(fileContent);
		return fileData; 
	}
	
	
	public FileData updateFile(FileData fileData){
		FileData existingFileData = databaseService.getFileMetadataById(fileData.getFileId());
		if(existingFileData == null){
			throw new NotFoundException(); 
		}
		
		// we have to create a new record in versions table
		FileVersion fileVersion = new FileVersion(existingFileData);
		fileVersion.setCreatedDate(new Date());
		
		databaseService.createFileVersion(fileVersion);
		
		String storageId = storageService.writeFile(fileData);
		fileData.setCreatedDate(new Date());
		fileData.setStorageId(storageId);
		
		databaseService.updateFileMetadata(fileData);
		
		return fileData;
	}
	
	
	public void deleteFile(String applicationName, Long fileId){
		FileData fileData = databaseService.getFileMetadataById(fileId);
		if(fileData == null){
			throw new NotFoundException(); 
		}
		
		// copy metadata to archive table
		databaseService.copyFileMetadataToArchive(fileId);
		databaseService.copyFileVersionsToArchive(fileId);
		
		databaseService.deleteFileMetadata(fileId);
		databaseService.deleteFileVersions(fileId);
	}
	
	

}
