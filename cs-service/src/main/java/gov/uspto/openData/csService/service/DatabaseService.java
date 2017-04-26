package gov.uspto.openData.csService.service;

import gov.uspto.openData.csService.model.FileArchive;
import gov.uspto.openData.csService.model.FileData;
import gov.uspto.openData.csService.model.FileVersion;

public interface DatabaseService {
	
	public FileData saveFileMetadata(FileData fileData);
	
	public FileData getFileMetadataById(Long fileId);
	
	public FileVersion createFileVersion(FileVersion fileVersion);
	
	public FileData updateFileMetadata(FileData fileData);
	
	public FileArchive createFileArchive(FileArchive fileArchive);
	
	public void copyFileMetadataToArchive(Long fileId);
	
	public void copyFileVersionsToArchive(Long fileId);
	
	public void deleteFileMetadata(Long fileId);
	
	public void deleteFileVersions(Long fileId);

}
