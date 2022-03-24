package model.exceptions;

public class ErroAoConsultarBaseException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErroAoConsultarBaseException(String message, Exception cause) {
		super(message, cause);
	}
}
