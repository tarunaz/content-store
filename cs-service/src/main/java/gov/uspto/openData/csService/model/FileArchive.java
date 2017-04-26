package gov.uspto.openData.csService.model;

import java.util.Date;

public class FileArchive extends FileMetadata{
	
	private Long archiveId;
	private Date fileCreatedDate;
	private Date createdDate;
	
	public FileArchive(){
		
	}
	
	public FileArchive(FileData fileData){
		this.fileId = fileData.getFileId();
		this.storageId = fileData.getStorageId();
		this.fileName = fileData.getFileName();
		this.applicationName = fileData.getApplicationName();
		this.contentLength = fileData.getContentLength();
		this.contentType = fileData.getContentType();
		this.fileCreatedDate = fileData.getCreatedDate();
	}
	
	public Long getArchiveId() {
		return archiveId;
	}
	public void setArchiveId(Long archiveId) {
		this.archiveId = archiveId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getFileCreatedDate() {
		return fileCreatedDate;
	}

	public void setFileCreatedDate(Date fileCreatedDate) {
		this.fileCreatedDate = fileCreatedDate;
	}
	
	
	

}
