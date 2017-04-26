package gov.uspto.openData.csRest.resource;

import gov.uspto.openData.csModel.dto.CreateFileResponse;
import gov.uspto.openData.csService.model.FileData;
import gov.uspto.openData.csService.service.ContentStoreService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Singleton
@Path("/")
@Component
public class ContentStoreResource {
	
	@Autowired
	private ContentStoreService contentStoreService;
	
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/files/{applicationName}/{fileId}")
	public Response readFile(@PathParam("applicationName") String applicationName, @PathParam("fileId") Long fileId){
		final FileData fileData = contentStoreService.readFile(applicationName, fileId);
		StreamingOutput streamingOutput = new StreamingOutput() {
			public void write(OutputStream output) throws IOException,
					WebApplicationException {
				IOUtils.copy(fileData.getFileContent(), output);
				IOUtils.closeQuietly(fileData.getFileContent());
			}
		};
		
		String contentDispositionHeader = ContentDisposition.type("attachment").fileName(fileData.getFileName()).creationDate(fileData.getCreatedDate()).size(fileData.getContentLength()).build().toString();
		
		Response response = Response.ok(streamingOutput)
							.header(HttpHeaders.CONTENT_DISPOSITION,contentDispositionHeader)
							.header(HttpHeaders.CONTENT_TYPE, fileData.getContentType())
							.header(HttpHeaders.CONTENT_LENGTH, fileData.getContentLength())
//							.header("Accept-Ranges", "bytes")
							.cookie(getDownloadCookie()).build();
		
		return response;
	}
	
	private NewCookie getDownloadCookie(){
		return new NewCookie("fileDownload", "true", "/", null, null, -1, false);
	}
	
	@POST
	@Path("/files/{applicationName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFile(@PathParam("applicationName") String applicationName, @HeaderParam(HttpHeaders.CONTENT_DISPOSITION) ContentDisposition fileDisposition, 
											@HeaderParam(HttpHeaders.CONTENT_TYPE) String contentType, @HeaderParam(HttpHeaders.CONTENT_LENGTH) Long contentLength,	InputStream inputStream){
		if(contentLength == null){
			contentLength = 0l;
		}
		if(StringUtils.isEmpty(contentType)){
			contentType = MediaType.APPLICATION_OCTET_STREAM;
		}
		
		FileData fileData = new FileData();
		fileData.setFileName(fileDisposition.getFileName());
		fileData.setContentLength(contentLength);
		fileData.setApplicationName(applicationName);
		fileData.setFileContent(inputStream);
		fileData.setContentType(contentType);
		
		fileData = contentStoreService.createFile(fileData);
		
		CreateFileResponse response = new CreateFileResponse();
		response.setApplicationName(applicationName);
		response.setFileId(fileData.getFileId()); 
		response.setFileName(fileData.getFileName());
		response.setContentLength(fileData.getContentLength());
		response.setCreatedDate(fileData.getCreatedDate());
		response.setContentType(fileData.getContentType());
		
		return Response.ok(response).build();
	}
	
	@PUT
	@Path("/files/{applicationName}/{fileId}")
	public void updateFile(@PathParam("applicationName") String applicationName, @PathParam("fileId") Long fileId, 
			@HeaderParam(HttpHeaders.CONTENT_DISPOSITION) ContentDisposition fileDisposition, @HeaderParam(HttpHeaders.CONTENT_TYPE) String contentType, @HeaderParam(HttpHeaders.CONTENT_LENGTH) Long contentLength,	InputStream inputStream){
		
		if(contentLength == null){
			contentLength = 0l;
		}
		if(StringUtils.isEmpty(contentType)){
			contentType = MediaType.APPLICATION_OCTET_STREAM;
		}
		
		FileData fileData = new FileData();
		fileData.setFileId(fileId);
		fileData.setFileName(fileDisposition.getFileName());
		fileData.setContentLength(contentLength);
		fileData.setApplicationName(applicationName);
		fileData.setContentType(fileDisposition.getType());
		fileData.setFileContent(inputStream);
		fileData.setContentType(contentType);
		
		fileData = contentStoreService.updateFile(fileData);

	}

	@DELETE
	@Path("/files/{applicationName}/{fileId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteFile(@PathParam("applicationName") String applicationName, @PathParam("fileId") Long fileId){
		contentStoreService.deleteFile(applicationName, fileId);
	}

}
