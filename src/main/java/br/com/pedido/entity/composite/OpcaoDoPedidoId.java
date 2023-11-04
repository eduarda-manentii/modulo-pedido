package br.com.pedido.entity.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OpcaoDoPedidoId {
	
	@Column(name = "id_cardapio")
	private Integer idDoPedido;
	
	@Column(name = "id_opcao")
	private Integer idDaOpcao;
	
}
