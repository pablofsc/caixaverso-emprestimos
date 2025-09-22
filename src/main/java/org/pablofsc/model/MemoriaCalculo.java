package org.pablofsc.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;

@RegisterForReflection
@AllArgsConstructor
public class MemoriaCalculo {

  public Integer mes;
  public Double saldoDevedorInicial;
  public Double juros;
  public Double amortizacao;
  public Double saldoDevedorFinal;
}
