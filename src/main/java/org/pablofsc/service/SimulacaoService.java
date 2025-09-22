package org.pablofsc.service;

import java.util.ArrayList;
import java.util.List;

import org.pablofsc.model.MemoriaCalculo;
import org.pablofsc.model.ProdutoEntity;
import org.pablofsc.model.SimulacaoRequest;
import org.pablofsc.model.SimulacaoResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimulacaoService {

  public SimulacaoResponse simular(SimulacaoRequest simulacao) {
    ProdutoEntity produto = ProdutoEntity.findById(simulacao.idProduto);

    if (produto == null) {
      throw new IllegalArgumentException("Produto não encontrado");
    }

    if (simulacao.prazoMeses > produto.prazoMaximoMeses) {
      throw new IllegalArgumentException("Prazo solicitado (" + simulacao.prazoMeses + " meses) excede o limite do produto (" + produto.prazoMaximoMeses + " meses)");
    }

    double taxaEfetivaMensal = calcularTaxaEfetivaMensal(produto);
    double parcela = calcularParcela(simulacao, taxaEfetivaMensal);
    double valorTotal = calcularValorTotal(parcela, simulacao.prazoMeses);
    List<MemoriaCalculo> memoria = calcularMemoriaCalculo(simulacao, taxaEfetivaMensal, parcela);

    return new SimulacaoResponse(
      produto,
      (double) simulacao.valorSolicitado,
      simulacao.prazoMeses,
      Math.round(taxaEfetivaMensal * 100000.0) / 100000.0,
      Math.round(valorTotal * 100.0) / 100.0,
      Math.round(parcela * 100.0) / 100.0,
      memoria
    );
  }

  private double calcularTaxaEfetivaMensal(ProdutoEntity produto) {
    double taxaAnual = produto.taxaJurosAnual / 100.0;
    return Math.pow(1 + taxaAnual, 1.0 / 12.0) - 1;
  }

  private double calcularParcela(SimulacaoRequest simulacao, double taxaEfetivaMensal) {
    double valor = simulacao.valorSolicitado;
    int prazo = simulacao.prazoMeses;
    return valor * (taxaEfetivaMensal * Math.pow(1 + taxaEfetivaMensal, prazo)) / (Math.pow(1 + taxaEfetivaMensal, prazo) - 1);
  }

  private double calcularValorTotal(double parcela, int prazo) {
    return parcela * prazo;
  }

  private List<MemoriaCalculo> calcularMemoriaCalculo(SimulacaoRequest simulacao, double taxaEfetivaMensal, double parcela) {
    List<MemoriaCalculo> memoria = new ArrayList<>();
    double saldo = simulacao.valorSolicitado;

    for (int mes = 1; mes <= simulacao.prazoMeses; mes++) {
      double juros = saldo * taxaEfetivaMensal;
      double amortizacao = parcela - juros;
      double saldoFinal = saldo - amortizacao;

      MemoriaCalculo memCalc = new MemoriaCalculo(
        mes,
        Math.round(saldo * 100.0) / 100.0,
        Math.round(juros * 100.0) / 100.0,
        Math.round(amortizacao * 100.0) / 100.0,
        Math.round(saldoFinal * 100.0) / 100.0
      );

      memoria.add(memCalc);

      saldo = saldoFinal;
    }

    return memoria;
  }
}
