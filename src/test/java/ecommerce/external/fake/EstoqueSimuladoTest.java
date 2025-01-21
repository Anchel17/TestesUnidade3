package ecommerce.external.fake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EstoqueSimuladoTest {
    @InjectMocks
    private EstoqueSimulado estoqueSimulado;
    
    @Test
    void testDarBaixaOk() {
        var produtosIds = List.of(1L, 2L);
        var produtosQuantidades = List.of(5L, 5L);
        
        var estoqueBaixaDTO = estoqueSimulado.darBaixa(produtosIds, produtosQuantidades);
        
        assertTrue(estoqueBaixaDTO.sucesso());
    }
    
    @Test
    void testDarBaixaFalha() {
        var produtosIds = List.of(1L, 2L);
        var produtosQuantidades = List.of(5L, 0L);
        
        var estoqueBaixaDTO = estoqueSimulado.darBaixa(produtosIds, produtosQuantidades);
        
        assertFalse(estoqueBaixaDTO.sucesso());
    }
    
    @Test
    void testVerificarDisponibilidadeOk() {
        var produtosIds = List.of(1L, 2L);
        var produtosQuantidades = List.of(5L, 5L);
        
        var disponibilidadeTO = estoqueSimulado.verificarDisponibilidade(produtosIds, produtosQuantidades);
        
        assertTrue(disponibilidadeTO.disponivel());
        assertEquals(0, disponibilidadeTO.idsProdutosIndisponiveis().size());
    }
    
    @Test
    void testVerificarDisponibilidadeOkSomenteUmProdutoIndisponivel() {
        var produtosIds = List.of(1L, 2L);
        var produtosQuantidades = List.of(5L, 0L);
        
        var disponibilidadeTO = estoqueSimulado.verificarDisponibilidade(produtosIds, produtosQuantidades);
        
        assertTrue(disponibilidadeTO.disponivel());
        assertEquals(1, disponibilidadeTO.idsProdutosIndisponiveis().size());
    }
    
    @Test
    void testVerificarDisponibilidadeProdutosIndisponiveis() {
        var produtosIds = List.of(1L, 2L);
        var produtosQuantidades = List.of(0L, 0L);
        
        var disponibilidadeTO = estoqueSimulado.verificarDisponibilidade(produtosIds, produtosQuantidades);
        
        assertFalse(disponibilidadeTO.disponivel());
        assertEquals(2, disponibilidadeTO.idsProdutosIndisponiveis().size());
    }
    
    @Test
    void testVerificarDisponibilidadeProdutosIdsVazio() {
        var produtosQuantidades = List.of(5L, 5L);
        
        var disponibilidadeTO = estoqueSimulado.verificarDisponibilidade(List.of(), produtosQuantidades);
        
        assertFalse(disponibilidadeTO.disponivel());
        assertEquals(0, disponibilidadeTO.idsProdutosIndisponiveis().size());
    }
    
    @Test
    void testVerificarDisponibilidadeProdutosQuantidadesVazio() {
        var produtosIds = List.of(1L, 2L);
        
        var disponibilidadeTO = estoqueSimulado.verificarDisponibilidade(produtosIds, List.of());
        
        assertFalse(disponibilidadeTO.disponivel());
        assertEquals(0, disponibilidadeTO.idsProdutosIndisponiveis().size());
    }
    
}
