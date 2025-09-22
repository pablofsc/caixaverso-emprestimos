package org.pablofsc.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RegisterForReflection
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoriaCalculo {

  public Integer mes;
  public Double saldoDevedorInicial;
  public Double juros;
  public Double amortizacao;
  public Double saldoDevedorFinal;
}
