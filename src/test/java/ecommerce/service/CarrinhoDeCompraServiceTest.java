package ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Produto;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;
import ecommerce.repository.CarrinhoDeComprasRepository;

@ExtendWith(MockitoExtension.class)
public class CarrinhoDeCompraServiceTest {
    @InjectMocks
    private CarrinhoDeComprasService carrinhoService;
    
    @Mock
    private CarrinhoDeComprasRepository carrinhoRepository;
    
    private Cliente cliente;
    
    @BeforeEach
    void init() {
        carrinhoService = new CarrinhoDeComprasService(carrinhoRepository);
        cliente = criarCliente();
    }
    
    @Test
    void testBuscarPorCarrinhoIdEClienteOk() {
        when(carrinhoRepository.findByIdAndCliente(Mockito.anyLong(), Mockito.any(Cliente.class))).thenReturn(Optional.of(criarCarrinhoDeCompras()));
        
        var carrinhoDeCompras = carrinhoService.buscarPorCarrinhoIdEClienteId(1L, cliente);
        
        assertEquals(1L, cliente.getId());
        assertEquals(1L, carrinhoDeCompras.getId());
        assertEquals(LocalDate.of(2025, 1, 1), carrinhoDeCompras.getData());
        assertNotNull(carrinhoDeCompras.getItens());
        assertEquals(1L, carrinhoDeCompras.getItens().get(0).getId());
        assertEquals("Computador", carrinhoDeCompras.getItens().get(0).getProduto().getNome());
        assertEquals("Pc da xuxa", carrinhoDeCompras.getItens().get(0).getProduto().getDescricao());
        assertEquals(TipoProduto.ELETRONICO, carrinhoDeCompras.getItens().get(0).getProduto().getTipo());
    }
    
    @Test
    void testBuscarPorCarrinhoIdEClienteCarrinhoNaoEncontradoException() {
        when(carrinhoRepository.findByIdAndCliente(Mockito.anyLong(), Mockito.any(Cliente.class))).thenReturn(Optional.empty());
        
        Exception e = assertThrows(IllegalArgumentException.class, () -> carrinhoService.buscarPorCarrinhoIdEClienteId(1L, cliente));
        
        assertEquals("Carrinho não encontrado.", e.getMessage());
    }
    
    private Cliente criarCliente() {
        return new Cliente(1L, "Fulano da Silva", "Rua das ruas, numero 17", TipoCliente.OURO);
    }
    
    private CarrinhoDeCompras criarCarrinhoDeCompras() {
        var itens  = criarListaDeCompras();
        
        return new CarrinhoDeCompras(1L, cliente, itens, LocalDate.of(2025, 1, 1));
    }
    
    private  List<ItemCompra> criarListaDeCompras(){
        var produto = criarProduto();
        
        return List.of(new ItemCompra(1L, produto, 1L));
    }
    
    private Produto criarProduto() {
        return new Produto(1L, "Computador", "Pc da xuxa", BigDecimal.valueOf(1000), 5, TipoProduto.ELETRONICO);
    }
}
