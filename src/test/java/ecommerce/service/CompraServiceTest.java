package ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.dto.DisponibilidadeDTO;
import ecommerce.dto.EstoqueBaixaDTO;
import ecommerce.dto.PagamentoDTO;
import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Produto;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;
import ecommerce.external.fake.EstoqueSimulado;
import ecommerce.external.fake.PagamentoSimulado;

@ExtendWith(MockitoExtension.class)
public class CompraServiceTest {
    @InjectMocks
    private CompraService compraService;
    
    @Mock
    private CarrinhoDeComprasService carrinhoService;;
    
    @Mock
    private ClienteService clienteService;
    
    @Mock
    private EstoqueSimulado estoqueSimulado;
    
    @Mock
    private PagamentoSimulado pagamentoSimulado;
    
    @BeforeEach
    void init() {
        compraService = new CompraService(carrinhoService, clienteService, estoqueSimulado, pagamentoSimulado);
    }
    
    @Test
    void finalizarCompra_compraOkSemTaxaDeFrete() {
        var cliente = criarCliente(TipoCliente.BRONZE, 1L);
        
        when(clienteService.buscarPorId(Mockito.anyLong())).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(Mockito.anyLong(), Mockito.any(Cliente.class))).thenReturn(criarCarrinhoDeCompras(cliente, 1L, 4, BigDecimal.valueOf(300)));
        when(estoqueSimulado.verificarDisponibilidade(Mockito.anyList(), Mockito.anyList())).thenReturn(new DisponibilidadeDTO(true, List.of()));
        when(pagamentoSimulado.autorizarPagamento(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(new PagamentoDTO(true, 1L));
        when(estoqueSimulado.darBaixa(Mockito.anyList(), Mockito.anyList())).thenReturn(new EstoqueBaixaDTO(true));
        
        var compra = compraService.finalizarCompra(1L, 1L);
        
        assertTrue(compra.sucesso());
        assertEquals(1L, compra.transacaoPagamentoId());
        assertEquals("Compra finalizada com sucesso.", compra.mensagem());
    }
    
    @Test
    void finalizarCompra_compraComDescontroClientePrata() {
        var cliente = criarCliente(TipoCliente.PRATA, 1L);
        
        when(clienteService.buscarPorId(Mockito.anyLong())).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(Mockito.anyLong(), Mockito.any(Cliente.class))).thenReturn(criarCarrinhoDeCompras(cliente, 1L, 5, BigDecimal.valueOf(500)));
        when(estoqueSimulado.verificarDisponibilidade(Mockito.anyList(), Mockito.anyList())).thenReturn(new DisponibilidadeDTO(true, List.of()));
        when(pagamentoSimulado.autorizarPagamento(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(new PagamentoDTO(true, 1L));
        when(estoqueSimulado.darBaixa(Mockito.anyList(), Mockito.anyList())).thenReturn(new EstoqueBaixaDTO(true));
        
        var compra = compraService.finalizarCompra(1L, 1L);
        
        assertTrue(compra.sucesso());
        assertEquals(1L, compra.transacaoPagamentoId());
        assertEquals("Compra finalizada com sucesso.", compra.mensagem());
    }
    
    @Test
    void finalizarCompra_compraComDescontroClientePrataProdutoComPeso50() {
        var cliente = criarCliente(TipoCliente.PRATA, 1L);
        
        when(clienteService.buscarPorId(Mockito.anyLong())).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(Mockito.anyLong(), Mockito.any(Cliente.class))).thenReturn(criarCarrinhoDeCompras(cliente, 1L, 50, BigDecimal.valueOf(500)));
        when(estoqueSimulado.verificarDisponibilidade(Mockito.anyList(), Mockito.anyList())).thenReturn(new DisponibilidadeDTO(true, List.of()));
        when(pagamentoSimulado.autorizarPagamento(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(new PagamentoDTO(true, 1L));
        when(estoqueSimulado.darBaixa(Mockito.anyList(), Mockito.anyList())).thenReturn(new EstoqueBaixaDTO(true));
        
        var compra = compraService.finalizarCompra(1L, 1L);
        
        assertTrue(compra.sucesso());
        assertEquals(1L, compra.transacaoPagamentoId());
        assertEquals("Compra finalizada com sucesso.", compra.mensagem());
    }
    
    @Test
    void finalizarCompra_compraComDescontroClienteOuro() {
        var cliente = criarCliente(TipoCliente.OURO, 1L);
        
        when(clienteService.buscarPorId(Mockito.anyLong())).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(Mockito.anyLong(), Mockito.any(Cliente.class))).thenReturn(criarCarrinhoDeCompras(cliente, 1L, 10, BigDecimal.valueOf(1000)));
        when(estoqueSimulado.verificarDisponibilidade(Mockito.anyList(), Mockito.anyList())).thenReturn(new DisponibilidadeDTO(true, List.of()));
        when(pagamentoSimulado.autorizarPagamento(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(new PagamentoDTO(true, 1L));
        when(estoqueSimulado.darBaixa(Mockito.anyList(), Mockito.anyList())).thenReturn(new EstoqueBaixaDTO(true));
        
        var compra = compraService.finalizarCompra(1L, 1L);
        
        assertTrue(compra.sucesso());
        assertEquals(1L, compra.transacaoPagamentoId());
        assertEquals("Compra finalizada com sucesso.", compra.mensagem());
    }
    
    @Test
    void testFinalizarCompraItensForaDeEstoqueException() {
        var cliente = criarCliente(TipoCliente.PRATA, 1L);
        
        when(clienteService.buscarPorId(Mockito.anyLong())).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(Mockito.anyLong(), Mockito.any(Cliente.class))).thenReturn(criarCarrinhoDeCompras(cliente, 1L, 50, BigDecimal.valueOf(500)));
        when(estoqueSimulado.verificarDisponibilidade(Mockito.anyList(), Mockito.anyList())).thenReturn(new DisponibilidadeDTO(false, List.of()));
        
        Exception e = assertThrows(IllegalStateException.class, () -> compraService.finalizarCompra(1L, 1L));
        
        assertEquals("Itens fora de estoque.", e.getMessage());
    }
    
    @Test
    void testFinalizarCompraPagamentoNaoAutorizadoException() {
        var cliente = criarCliente(TipoCliente.PRATA, 1L);
        
        when(clienteService.buscarPorId(Mockito.anyLong())).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(Mockito.anyLong(), Mockito.any(Cliente.class))).thenReturn(criarCarrinhoDeCompras(cliente, 1L, 50, BigDecimal.valueOf(500)));
        when(estoqueSimulado.verificarDisponibilidade(Mockito.anyList(), Mockito.anyList())).thenReturn(new DisponibilidadeDTO(true, List.of()));
        when(pagamentoSimulado.autorizarPagamento(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(new PagamentoDTO(false, 1L));
        
        Exception e = assertThrows(IllegalStateException.class, () -> compraService.finalizarCompra(1L, 1L));
        
        assertEquals("Pagamento não autorizado.", e.getMessage());
    }
    
    @Test
    void testFinalizarCompraErroAoDarBaixaNoEstoqueException() {
        var cliente = criarCliente(TipoCliente.PRATA, 1L);
        
        when(clienteService.buscarPorId(Mockito.anyLong())).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(Mockito.anyLong(), Mockito.any(Cliente.class))).thenReturn(criarCarrinhoDeCompras(cliente, 1L, 50, BigDecimal.valueOf(500)));
        when(estoqueSimulado.verificarDisponibilidade(Mockito.anyList(), Mockito.anyList())).thenReturn(new DisponibilidadeDTO(true, List.of()));
        when(pagamentoSimulado.autorizarPagamento(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(new PagamentoDTO(true, 1L));
        when(estoqueSimulado.darBaixa(Mockito.anyList(), Mockito.anyList())).thenReturn(new EstoqueBaixaDTO(false));
        
        Exception e = assertThrows(IllegalStateException.class, () -> compraService.finalizarCompra(1L, 1L));
        
        assertEquals("Erro ao dar baixa no estoque.", e.getMessage());
    }
    
    
    private Cliente criarCliente(TipoCliente tipoCliente, Long idCliente) {
        var cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setNome("Osvaldo da Silva");
        cliente.setEndereco("Rua das ruas, numero 17");
        cliente.setTipo(tipoCliente);
        
        return cliente;
    }
    
    private CarrinhoDeCompras criarCarrinhoDeCompras(Cliente cliente, Long idCarrinho, Integer pesoItemCompra, BigDecimal valor) {
        var carrinho = new CarrinhoDeCompras();
        carrinho.setCliente(cliente);
        carrinho.setData(LocalDate.of(2025, 1, 1));
        carrinho.setId(idCarrinho);
        carrinho.setItens(criarItensCompra(1L, pesoItemCompra, valor));
        
        return carrinho;
    }
    
    private List<ItemCompra> criarItensCompra(Long idItemCompra, Integer peso, BigDecimal valor){
        var itemCompra = new ItemCompra();
        
        itemCompra.setId(idItemCompra);
        itemCompra.setProduto(criarProduto(1L, "Computador pessoal", "Pc da Xuxa", peso, valor, TipoProduto.ELETRONICO));
        itemCompra.setQuantidade(1L);
        
        return List.of(itemCompra);
    }
    
    private Produto criarProduto(Long idProduto, String descricao, String nome, Integer peso, BigDecimal preco, TipoProduto tipoProduto) {
        var produto = new Produto();
        
        produto.setId(idProduto);
        produto.setDescricao(descricao);
        produto.setNome(nome);
        produto.setPeso(peso);
        produto.setPreco(preco);
        produto.setTipo(tipoProduto);
        
        return produto;
    }
}
