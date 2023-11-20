import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.pedido.InitApp;
import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.repository.PedidosRepository;
import br.com.pedido.service.impl.PedidoServiceImpl;

@SpringBootTest(classes = InitApp.class)
public class AtualizarStatusTeste {

    @InjectMocks
    private PedidoServiceImpl service;

    @Mock
    private PedidosRepository repository;

    @Test
    public void testAtualizarStatusPorPedidoExistente() {
        Integer idPedido = 70;
        Status novoStatus = Status.ACEITO_PELO_RESTAURANTE;  

        Pedido pedidoEncontrado = new Pedido();
        pedidoEncontrado.setId(idPedido);
        pedidoEncontrado.setStatus(Status.ACEITO_PELO_RESTAURANTE);  
        when(repository.buscarPor(idPedido)).thenReturn(pedidoEncontrado);

        service.atualizarStatusPor(idPedido, novoStatus);

        verify(repository, times(1)).atualizarStatusPor(idPedido, novoStatus);

        assertEquals(novoStatus, pedidoEncontrado.getStatus());
    }
    
    @Test
    public void testBuscarPorPedidoNaoExistente() {
        assertThrows(NullPointerException.class, () -> {
            service.buscarPor(1);
        });
    }
}