package org.pablofsc.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.pablofsc.model.SimulacaoRequest;
import org.pablofsc.model.SimulacaoResponse;
import org.pablofsc.service.SimulacaoService;

@QuarkusTest
public class SimulacoesResourceTest {

  @InjectMock
  SimulacaoService simulacaoService;

  @Test
  public void deveRetornarSimulacaoComSucesso() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    SimulacaoResponse response = new SimulacaoResponse(null, 1000.0, 12, 0.01, 1120.0, 93.33, null);

    when(simulacaoService.simular(any(SimulacaoRequest.class))).thenReturn(response);

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(200)
        .body("valorTotalComJuros", is(1120.0f))
        .body("parcelaMensal", is(93.33f));
  }

  @Test
  public void deveRetornarErroParaRequestInvalido() {
    SimulacaoRequest request = new SimulacaoRequest();
    // request inválido, e.g., idProduto null

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(400);
  }

  @Test
  public void deveRetornarErroQuandoServicoLancaExcecao() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    when(simulacaoService.simular(any(SimulacaoRequest.class)))
        .thenThrow(new IllegalArgumentException("Erro na simulação"));

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(400)
        .body("error", is("Erro na simulação"));
  }
}
