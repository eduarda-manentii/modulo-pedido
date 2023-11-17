import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.repository.PedidosRepository;
import br.com.pedido.service.impl.PedidoServiceImpl;

public class AtualizarStatusTeste {
	
	@Autowired
    private PedidoServiceImpl service;
    
	@Autowired
    private PedidosRepository repository;
	
	@Test
	public void atualizarStatus() {
		
		int idPedido = 66;
        Status statusAtual = Status.REALIZADO; 
        Status novoStatus = Status.ACEITO_PELO_RESTAURANTE;

        assertDoesNotThrow(() -> service.atualizarStatusPor(idPedido, novoStatus));

        Pedido pedidoAtualizado = repository.buscarPor(idPedido);
        assertNotNull(pedidoAtualizado);
        assertEquals(novoStatus, pedidoAtualizado.getStatus());

        assertNotEquals(statusAtual, novoStatus, "O status atual não deve ser igual ao novo status");
        assertTrue(statusAtual.ordinal() < novoStatus.ordinal(), "O novo status deve ser uma progressão válida do status atual");

		
	}

}
