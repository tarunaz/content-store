package gov.uspto.openData.csService.model;

import java.io.InputStream;
import java.util.Date;

public class FileData extends FileMetadata {

	private Date createdDate;
	private InputStream fileContent;
	
	public InputStream getFileContent() {
		return fileContent;
	}
	public void setFileContent(InputStream fileContent) {
		this.fileContent = fileContent;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	

}
