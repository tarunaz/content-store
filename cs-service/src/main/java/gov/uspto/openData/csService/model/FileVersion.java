package gov.uspto.openData.csService.model;

import java.util.Date;

public class FileVersion extends FileMetadata{
	
	private Long versionId;
	private Date fileCreatedDate;
	private Date createdDate;
	
	public FileVersion(){
		
	}
	
	public FileVersion(FileData fileData){
		this.fileId = fileData.getFileId();
		this.storageId = fileData.getStorageId();
		this.fileName = fileData.getFileName();
		this.applicationName = fileData.getApplicationName();
		this.contentLength = fileData.getContentLength();
		this.contentType = fileData.getContentType();
		this.fileCreatedDate = fileData.getCreatedDate();
	}
	
	public Long getVersionId() {
		return versionId;
	}
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
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
