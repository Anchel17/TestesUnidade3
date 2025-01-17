package ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.dto.CompraDTO;
import ecommerce.dto.DisponibilidadeDTO;
import ecommerce.dto.EstoqueBaixaDTO;
import ecommerce.dto.PagamentoDTO;
import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.TipoCliente;
import ecommerce.external.IEstoqueExternal;
import ecommerce.external.IPagamentoExternal;
import jakarta.transaction.Transactional;

@Service
public class CompraService {

	private final CarrinhoDeComprasService carrinhoService;
	private final ClienteService clienteService;

	private final IEstoqueExternal estoqueExternal;
	private final IPagamentoExternal pagamentoExternal;

	@Autowired
	public CompraService(CarrinhoDeComprasService carrinhoService, ClienteService clienteService,
			IEstoqueExternal estoqueExternal, IPagamentoExternal pagamentoExternal) {
		this.carrinhoService = carrinhoService;
		this.clienteService = clienteService;

		this.estoqueExternal = estoqueExternal;
		this.pagamentoExternal = pagamentoExternal;
	}

	@Transactional
	public CompraDTO finalizarCompra(Long carrinhoId, Long clienteId) {
		Cliente cliente = clienteService.buscarPorId(clienteId);
		CarrinhoDeCompras carrinho = carrinhoService.buscarPorCarrinhoIdEClienteId(carrinhoId, cliente);

		List<Long> produtosIds = carrinho.getItens().stream().map(i -> i.getProduto().getId())
				.collect(Collectors.toList());
		List<Long> produtosQtds = carrinho.getItens().stream().map(i -> i.getQuantidade()).collect(Collectors.toList());

		DisponibilidadeDTO disponibilidade = estoqueExternal.verificarDisponibilidade(produtosIds, produtosQtds);

		if (!disponibilidade.disponivel()) {
			throw new IllegalStateException("Itens fora de estoque.");
		}

		BigDecimal custoTotal = calcularCustoTotal(carrinho);

		PagamentoDTO pagamento = pagamentoExternal.autorizarPagamento(cliente.getId(), custoTotal.doubleValue());

		if (!pagamento.autorizado()) {
			throw new IllegalStateException("Pagamento não autorizado.");
		}

		EstoqueBaixaDTO baixaDTO = estoqueExternal.darBaixa(produtosIds, produtosQtds);

		if (!baixaDTO.sucesso()) {
			pagamentoExternal.cancelarPagamento(cliente.getId(), pagamento.transacaoId());
			throw new IllegalStateException("Erro ao dar baixa no estoque.");
		}

		CompraDTO compraDTO = new CompraDTO(true, pagamento.transacaoId(), "Compra finalizada com sucesso.");

		return compraDTO;
	}

	public BigDecimal calcularCustoTotal(CarrinhoDeCompras carrinho) {
	    var custoTotal = BigDecimal.ZERO;
	    var itens = carrinho.getItens();
	    var cliente = carrinho.getCliente();
	    AtomicInteger frete = new AtomicInteger(0);
	    
	    calcularFrete(frete, itens);
	    
	    custoTotal.add(somarValoresDosProdutos(itens));
	    //tipo Bronze pagam o valor integral.
	    if(!TipoCliente.BRONZE.equals(cliente.getTipo())){
	        aplicarDescontoNoFrete(frete, cliente);
	    }
	    
	    if(custoTotal.compareTo(BigDecimal.valueOf(500)) > 0) {
	        aplicarDescontoNoCustoTotal(custoTotal);
	    }
	    
	    custoTotal.add(BigDecimal.valueOf(frete.get()));
	    
		return custoTotal;
	}
	
	private void calcularFrete(AtomicInteger frete, List<ItemCompra> itens) {
//	     até 5 kg não é cobrado frete;
        itens.forEach(item -> {
            var produto = item.getProduto();
            // acima de 5 kg e abaixo de 10 kg é cobrado R$ 2,00 por kg;
            if(produto.getPeso() >= 5 && produto.getPeso() < 10) {
                //ver se não teria que trocar addAndGet por accumulateAndGet
                frete.addAndGet(2 * produto.getPeso());
            }
            // acima de 10 kg e abaixo de 50 kg é cobrado R$ 4,00 por kg; 
            else if(produto.getPeso() >= 10 && produto.getPeso() < 50) {
                //ver se não teria que trocar addAndGet por accumulateAndGet
                frete.addAndGet(4 * produto.getPeso());
            }
            // acima de 50 kg é cobrado R$ 7,00 por kg.
            else if(produto.getPeso() >= 50){
                //ver se não teria que trocar addAndGet por accumulateAndGet
                frete.addAndGet(7 * produto.getPeso());
            }
        });
	}
	
	private BigDecimal somarValoresDosProdutos(List<ItemCompra> itens) {
	    var soma = BigDecimal.ZERO;
	    
	    itens.forEach(item -> soma.add(item.getProduto().getPreco()));
	    
	    return soma;
	}
	
	private void aplicarDescontoNoFrete(AtomicInteger frete, Cliente cliente) {
	    //clientes do tipo Ouro possuem isenção total do valor do frete;
	    if(TipoCliente.OURO.equals(cliente.getTipo())) {
	        frete.set(0);
	        return;
	    }
	    
	    //tipo Prata possuem desconto de 50%;
	    var novoFrete = frete.get();
	    frete.set((int) (novoFrete/2));
	}

    /* 
     Carrinhos de compras que custam mais de R$ 500,00 recebem um desconto de 10%;
     aqueles que custam mais de R$ 1000,00 recebem 20% de desconto. 
    */
	private void aplicarDescontoNoCustoTotal(BigDecimal custoTotal) {
	    if(custoTotal.compareTo(BigDecimal.valueOf(1000)) < 0) {
	        custoTotal = custoTotal.multiply(BigDecimal.valueOf(0.1));
	        return;
	    }
	    
	    custoTotal = custoTotal.multiply(BigDecimal.valueOf(0.2));
	}
}
