package gov.uspto.openData.csModel.exception;

public class ContentStoreException extends RuntimeException {
	
	public ContentStoreException(){
		super();
	}
	
	public ContentStoreException(Throwable t){
		super(t);
	}
	
	public ContentStoreException(String msg, Throwable t){
		super(msg, t);
	}

}
