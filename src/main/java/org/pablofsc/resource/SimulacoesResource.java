package org.pablofsc.resource;

import org.pablofsc.model.SimulacaoRequest;
import org.pablofsc.model.SimulacaoResponse;
import org.pablofsc.service.SimulacaoService;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/simulacoes")
public class SimulacoesResource {

  @Inject
  SimulacaoService simulacaoService;

  @POST
  public Response simular(SimulacaoRequest request) {
    try {
      SimulacaoResponse result = simulacaoService.simular(request);
      return Response.ok(result).build();
    } catch (IllegalArgumentException e) {
      return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
    }
  }
}
