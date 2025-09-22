package org.pablofsc.model;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;

@RegisterForReflection
@AllArgsConstructor
public class SimulacaoResponse {

  public ProdutoEntity produto;
  public Double valorSolicitado;
  public Integer prazoMeses;
  public Double taxaJurosEfetivaMensal;
  public Double valorTotalComJuros;
  public Double parcelaMensal;
  public List<MemoriaCalculo> memoriaCalculo;
}
