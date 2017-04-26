package gov.uspto.openData.csService.model;


public class FileMetadata {

	protected Long fileId;
	protected String storageId;
	protected String fileName;
	protected String applicationName;
	protected Long contentLength;
	protected String contentType;
	
	public FileMetadata(){
		
	}
	
	public FileMetadata(FileMetadata metadata){
		this.fileId = metadata.getFileId();
		this.storageId = metadata.getStorageId();
		this.fileName = metadata.getFileName();
		this.applicationName = metadata.getApplicationName();
		this.contentLength = metadata.getContentLength();
		this.contentType = metadata.getContentType();
		
	}
	
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public Long getContentLength() {
		return contentLength;
	}
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	

}
