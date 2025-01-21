package ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.entity.Cliente;
import ecommerce.entity.TipoCliente;
import ecommerce.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    
    @InjectMocks
    private ClienteService clienteService;
    
    @Mock
    private ClienteRepository clienteRepository;
    
    @BeforeEach
    void init() {
        clienteService = new ClienteService(clienteRepository);
    }
    
    @Test
    void testBuscaPorIdOk() {
        when(clienteRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(criarCliente()));
        
        var cliente = clienteService.buscarPorId(1L);
        
        assertEquals(1L, cliente.getId());
        assertEquals("Fulano da Silva", cliente.getNome());
        assertEquals("Rua das ruas, numero 17", cliente.getEndereco());
        assertEquals(TipoCliente.OURO, cliente.getTipo());
    }
    
    @Test
    void testBuscarPorIdClienteNaoEncontradoException() {
        when(clienteRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        
        Exception e = assertThrows(IllegalArgumentException.class, () -> clienteService.buscarPorId(1L));
        
        assertEquals("Cliente não encontrado", e.getMessage());
    }
    
    private Cliente criarCliente() {
        return new Cliente(1L, "Fulano da Silva", "Rua das ruas, numero 17", TipoCliente.OURO);
    }
}
