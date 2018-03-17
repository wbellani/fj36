package br.com.caelum.payfast.oauth2;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenDao {
	private List<String> accessTokens = new ArrayList<>();

	public void adicionaAccessToken(String token) {
		System.out.println("Adicionando	token	" + token);
		accessTokens.add(token);
	}

	public boolean existeAccessToken(String token) {
		System.out.println("Verificando	token	" + token);
		return accessTokens.contains(token);
	}
}
