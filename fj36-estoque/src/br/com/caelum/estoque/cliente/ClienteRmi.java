package br.com.caelum.estoque.cliente;

import java.rmi.Naming;

import br.com.caelum.estoque.rmi.EstoqueRmi;
import br.com.caelum.estoque.rmi.ItemEstoque;

public class ClienteRmi {
	public static void main(String[] args) throws Exception {
		EstoqueRmi estoque = (EstoqueRmi) Naming.lookup("rmi://localhost:1099/estoque");
		ItemEstoque item = estoque.getItemEstoque("ARQ");
		System.out.println("Quantidade	disponível:	" + item.getQuantidade());
	}
}