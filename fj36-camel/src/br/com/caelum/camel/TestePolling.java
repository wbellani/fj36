package br.com.caelum.camel;

import java.util.Scanner;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import br.com.caelum.livraria.modelo.Livro;

public class TestePolling {

	public static void main(String[] args) throws Exception {
		MysqlConnectionPoolDataSource mysqlDs = new MysqlConnectionPoolDataSource();
		mysqlDs.setDatabaseName("fj36_camel");
		mysqlDs.setServerName("localhost");
		mysqlDs.setPort(3306);
		mysqlDs.setUser("root");
		mysqlDs.setPassword("caelum");

		JndiContext jndi = new JndiContext();
		jndi.rebind("mysqlDataSource", mysqlDs);
		CamelContext ctx = new DefaultCamelContext(jndi);

		// CamelContext ctx = new DefaultCamelContext();
		ctx.addRoutes(new RouteBuilder() {
			public void configure() throws Exception {
				from("http://localhost:8088/fj36-livraria/loja/livros/mais-vendidos").delay(1000).unmarshal().json()
						.log("${body}").process(new ProcessadorMensagem()).to("direct:livros");
				from("direct:livros").split(body()).process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						Message inbound = exchange.getIn();
						Livro livro = (Livro) inbound.getBody();
						String nomeAutor = livro.getNomeAutor();
						inbound.setHeader("nomeAutor", nomeAutor);
					}
				}).setBody(simple("insert	into	livros	(nomeauto)	values	(:?nomeAutor)"))
						.to("jdbc:mysqlDataSource?useHeadersAsParameters=true");
			}
		});

		ctx.start();
		new Scanner(System.in).nextLine();
		ctx.stop();
	}
}
