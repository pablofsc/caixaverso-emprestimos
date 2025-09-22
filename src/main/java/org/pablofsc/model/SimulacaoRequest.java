package org.pablofsc.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RegisterForReflection
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulacaoRequest {

  public Long idProduto;
  public Float valorSolicitado;
  public Integer prazoMeses;
}
