package br.com.caelum.camel;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import br.com.caelum.livraria.modelo.Livro;

public class ProcessadorMensagem implements Processor {
	@Override
	public void process(Exchange exchange) throws Exception {
		List<?> listaDeLivros = (List<?>) exchange.getIn().getBody();
		ArrayList<Livro> livros = (ArrayList<Livro>) listaDeLivros.get(0);
		Message message = exchange.getIn();
		message.setBody(livros);
	}
}
