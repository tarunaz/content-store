package gov.uspto.openData.csRest.mapper;

import gov.uspto.openData.csModel.exception.NotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

	@Override
	public Response toResponse(NotFoundException notFoundException) {
		return Response.status(Response.Status.NOT_FOUND).entity(notFoundException.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}
}
