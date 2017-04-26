package gov.uspto.openData.csClient;

import gov.uspto.openData.csModel.dto.CreateFileResponse;
import gov.uspto.openData.csModel.dto.ReadFileResponse;
import gov.uspto.openData.csModel.exception.ContentStoreException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.ParseException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class ContentStoreClient {
	
	private String apiLocation = "";
	private WebTarget target;
	
	public ContentStoreClient(){
		buildTarget();
	}
	
	public ContentStoreClient(String apiLocation){
		this.apiLocation = apiLocation;
		buildTarget();
	}
	
	private void buildTarget(){
		Client client = ClientBuilder.newClient();
		this.target = client.register(JacksonFeature.class)
							.register(MultiPartFeature.class)
							.target(apiLocation);
	}
	
	public ReadFileResponse readFile(String applicationName, Long fileId){
		Response response =  target.path("/files/{applicationName}/{fileId}")
						.resolveTemplate("applicationName", applicationName)
						.resolveTemplate("fileId", fileId)
						.request(MediaType.APPLICATION_OCTET_STREAM_TYPE)
						.get();
		if(response.getStatusInfo().getStatusCode() == Response.Status.NOT_FOUND.getStatusCode()){
			return null;
		}
		InputStream is = response.readEntity(InputStream.class);
		
		ReadFileResponse fileResponse = new ReadFileResponse();
		fileResponse.setFileContent(is);
		try {
			ContentDisposition disposition = new ContentDisposition(response.getHeaderString("content-disposition"));
			fileResponse.setFileName(disposition.getFileName());
			fileResponse.setCreatedDate(disposition.getCreationDate());
			fileResponse.setContentLength(disposition.getSize());
		} catch (ParseException e) {
			throw new ContentStoreException("Exception while parsing the response from server", e);
		}
		
		return fileResponse;
	}
	
	
	
	public CreateFileResponse createFile(String applicationName, File file, String contentType){
		CreateFileResponse createFileResponse;
		try {
			createFileResponse = createFile(applicationName, file.getName(), file.length(), contentType, new FileInputStream(file));
			
		} catch (IOException e) {
			throw new ContentStoreException("Exception occured while determining the content type of the given file.", e);
		}
		return createFileResponse;
	}
	
	public CreateFileResponse createFile(String applicationName, String fileName, Long contentLength, String contentType, InputStream is){
		ContentDisposition contentDisposition = ContentDisposition.type("attachement").fileName(fileName).size(contentLength).build();
		CreateFileResponse createFileResponse = target.path("/files/"+applicationName)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
				.header(HttpHeaders.CONTENT_LENGTH, contentLength)
				.post(Entity.entity(is, contentType), CreateFileResponse.class);
		return createFileResponse;
	}
	
	public void updateFile(String applicationName, Long fileId, File file, String contentType){
		try {
			updateFile(applicationName, fileId, file.getName(), file.length(), contentType, new FileInputStream(file));
		} catch (IOException e) {
			throw new ContentStoreException("Exception occured while determining the content type of the given file.", e);
		}
	}
	
	public void updateFile(String applicationName, Long fileId, String fileName, Long contentLength, String contentType, InputStream is){
		ContentDisposition contentDisposition = ContentDisposition.type("attachement").fileName(fileName).size(contentLength).build();
		target.path("/files/"+applicationName+"/"+fileId)
				.request()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
				.header(HttpHeaders.CONTENT_LENGTH, contentLength)
				.put(Entity.entity(is, contentType));
	}
	
	public void deleteFile(String applicationName, Long fileId){
		target.path("/files/"+applicationName+"/"+fileId)
				.request()
				.delete();
	}
	
	public static void main(String[] args) throws Exception{
		ContentStoreClient client = new ContentStoreClient("http://localhost:8080/cs-rest/rest");
		File file = new File("/Users/greensod/temp/pdf.pdf");
//		CreateFileResponse createFileResponse = client.createFile("uspto.airs", file.getName(), file.length(), new FileInputStream(file));
		CreateFileResponse createFileResponse = client.createFile("uspto.airs", file, "application/pdf");
		
		ReadFileResponse response = client.readFile("uspto.airs", createFileResponse.getFileId());
		System.out.println("Content Length - "+response.getContentLength());
	}

}
