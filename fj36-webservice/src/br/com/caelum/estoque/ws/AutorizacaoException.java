package br.com.caelum.estoque.ws;

import javax.xml.ws.WebFault;

@WebFault(name="AutorizacaoFault")
public class AutorizacaoException extends	RuntimeException	{

	private	static	final	long	serialVersionUID	=	1L;
	public	AutorizacaoException(String	message)	{
					super(message);
	}
}
