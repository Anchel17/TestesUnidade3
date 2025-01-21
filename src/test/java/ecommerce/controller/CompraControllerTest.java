package ecommerce.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;

import ecommerce.dto.CompraDTO;
import ecommerce.service.CompraService;

@ExtendWith(MockitoExtension.class)
public class CompraControllerTest {
    @InjectMocks
    private CompraController compraController;
    
    @Mock
    private CompraService compraService;
    
    
    @BeforeEach
    void init() {
        compraController = new CompraController(compraService);
    }
    
    @Test
    void testFinalizarCompraOk(){
        when(compraService.finalizarCompra(Mockito.anyLong(), Mockito.anyLong()))
            .thenReturn(new CompraDTO(true, 1L, "Compra finalizada com sucesso."));
        
        var response = compraController.finalizarCompra(1L, 1L);
        
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("Compra finalizada com sucesso.", response.getBody().mensagem());
    }
    
    @Test
    void testTestFinalizarCompraIllegalArgumentException() {
        when(compraService.finalizarCompra(Mockito.anyLong(), Mockito.anyLong()))
            .thenThrow(IllegalArgumentException.class);
        
        var response = compraController.finalizarCompra(1L, 1L);
        
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }
    
    @Test
    void testFinalizarCompraIllegalStateException() {
        when(compraService.finalizarCompra(Mockito.anyLong(), Mockito.anyLong()))
        .thenThrow(IllegalStateException.class);
    
    var response = compraController.finalizarCompra(1L, 1L);
    
    assertEquals(HttpStatusCode.valueOf(409), response.getStatusCode());
    }
    
    @Test
    void testFinalizarCompraInternalServerError() {
        when(compraService.finalizarCompra(Mockito.anyLong(), Mockito.anyLong()))
        .thenThrow(NullPointerException.class);
    
    var response = compraController.finalizarCompra(1L, 1L);
    
    assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
    assertEquals("Erro ao processar compra.", response.getBody().mensagem());
    }
}
