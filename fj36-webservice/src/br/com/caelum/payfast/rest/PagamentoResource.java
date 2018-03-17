package br.com.caelum.payfast.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import br.com.caelum.payfast.modelo.Pagamento;
import br.com.caelum.payfast.modelo.Transacao;
import br.com.caelum.payfast.oauth2.TokenDao;

@Singleton
@Path("/v1/pagamentos")
public class PagamentoResource {
	@Inject
	private TokenDao tokenDao;
	@Inject
	private HttpServletRequest request;
	
	private Map<Integer, Pagamento> repositorio = new HashMap<>();
	private Integer idPagamento = 1;

	public PagamentoResource() {
		
		Pagamento pagamento = new Pagamento();
		pagamento.setId(idPagamento++);
		pagamento.setValor(BigDecimal.TEN);
		pagamento.comStatusCriado();
		repositorio.put(pagamento.getId(), pagamento);

	}

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML }) // cuidado
																			// javax.ws.rs
	public Pagamento buscaPagamento(@PathParam("id") Integer id) {
		return repositorio.get(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response criarPagamento(Transacao transacao) throws Exception {
		Response unauthorized = Response.status(Status.BAD_GATEWAY).build();
		try {
			OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request);
			String accessToken = oauthRequest.getAccessToken();
			if (tokenDao.existeAccessToken(accessToken)) {

				Pagamento pagamento = new Pagamento();
				pagamento.setId(idPagamento++);
				pagamento.setValor(transacao.getValor());

				pagamento.comStatusCriado();
				repositorio.put(pagamento.getId(), pagamento);
				System.out.println("PAGAMENTO	CRIADO	" + pagamento);
				return Response.created(new URI("/pagamentos/" + pagamento.getId())).entity(pagamento)
						.type(MediaType.APPLICATION_JSON_TYPE).build();
			} else {
				return unauthorized;
			}
		} catch (OAuthProblemException | OAuthSystemException e) {
			throw new BadRequestException();
		}
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON) // cuidado javax.ws.rs
	public Pagamento confirmarPagamento(@PathParam("id") Integer pagamentoId) {
		Pagamento pagamento = repositorio.get(pagamentoId);
		pagamento.comStatusConfirmado();
		System.out.println("Pagamento	confirmado:	" + pagamento);
		return pagamento;
	}
}