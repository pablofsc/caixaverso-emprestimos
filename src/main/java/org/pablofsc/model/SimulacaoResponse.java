package org.pablofsc.model;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;

@RegisterForReflection
@AllArgsConstructor
@Schema(description = "Resultado da simulação de empréstimo")
public class SimulacaoResponse {

  @Schema(description = "Dados do produto financeiro utilizado na simulação")
  public ProdutoEntity produto;

  @Schema(description = "Valor solicitado para o empréstimo", example = "10000.00")
  public Double valorSolicitado;

  @Schema(description = "Prazo do empréstimo em meses", example = "12")
  public Integer prazoMeses;

  @Schema(description = "Taxa de juros efetiva mensal aplicada", example = "0.015")
  public Double taxaJurosEfetivaMensal;

  @Schema(description = "Valor total a ser pago com juros", example = "10956.18")
  public Double valorTotalComJuros;

  @Schema(description = "Valor da parcela mensal", example = "913.02")
  public Double parcelaMensal;

  @Schema(description = "Detalhamento mês a mês da evolução do empréstimo")
  public List<MemoriaCalculo> memoriaCalculo;
}
