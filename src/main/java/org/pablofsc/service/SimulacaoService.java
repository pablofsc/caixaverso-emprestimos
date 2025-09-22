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

    // Validar prazo
    if (simulacao.prazoMeses > produto.prazoMaximoMeses) {
      throw new IllegalArgumentException("Prazo solicitado (" + simulacao.prazoMeses + " meses) excede o limite do produto (" + produto.prazoMaximoMeses + " meses)");
    }

    // Calcular taxa efetiva mensal
    double taxaAnual = produto.taxaJurosAnual / 100.0;
    double taxaEfetivaMensal = Math.pow(1 + taxaAnual, 1.0 / 12.0) - 1;

    // Calcular parcela mensal PRICE
    double valor = simulacao.valorSolicitado;
    int prazo = simulacao.prazoMeses;
    double parcela = valor * (taxaEfetivaMensal * Math.pow(1 + taxaEfetivaMensal, prazo)) / (Math.pow(1 + taxaEfetivaMensal, prazo) - 1);

    // Valor total com juros
    double valorTotal = parcela * prazo;

    // Calcular memória de cálculo
    List<MemoriaCalculo> memoria = new ArrayList<>();
    double saldo = valor;

    for (int mes = 1; mes <= prazo; mes++) {
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
}
