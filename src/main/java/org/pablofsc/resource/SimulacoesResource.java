package org.pablofsc.resource;

import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.pablofsc.model.SimulacaoRequest;
import org.pablofsc.model.SimulacaoResponse;
import org.pablofsc.service.SimulacaoService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/simulacoes")
@Tag(name = "Simulações", description = "Operações para simulação de empréstimos")
public class SimulacoesResource {

  @Inject
  SimulacaoService simulacaoService;

  @POST
  @Operation(summary = "Simular empréstimo", description = "Realiza uma simulação de empréstimo com base nos parâmetros fornecidos")
  @APIResponses({
      @APIResponse(responseCode = "200", description = "Simulação realizada com sucesso", content = @Content(schema = @Schema(implementation = SimulacaoResponse.class))),
      @APIResponse(responseCode = "400", description = "Parâmetros inválidos para simulação")
  })
  public Response simular(@Valid SimulacaoRequest request) {
    try {
      SimulacaoResponse result = simulacaoService.simular(request);
      return Response.ok(result).build();
    } catch (IllegalArgumentException e) {
      return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
    }
  }
}
