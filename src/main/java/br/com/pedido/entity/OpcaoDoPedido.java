package br.com.pedido.entity;

import java.math.BigDecimal;

import br.com.pedido.entity.composite.OpcaoDoPedidoId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data 
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "pedidos_opcoes") 
@Entity(name = "OpcaoDoPedido")
@ToString
public class OpcaoDoPedido {
	
	@EmbeddedId
	@NotNull(message = "O id da opção do pedido é obrigatório")
	private OpcaoDoPedidoId id;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idDoPedido")
	@JoinColumn(name = "id_pedido")
	@NotNull(message = "O pedido é obrigatório")
	private Pedido pedido;
	
	@Column(name = "valor_item")
	@DecimalMin(message = "O preço não pode ser inferior a R$0.01", value = "0.0", inclusive = false)
    @Digits(message = "O preço deve possuir o formato 'NNNNNNNNN.NN'", integer = 9, fraction = 2)
	private BigDecimal valorItem;
	
	@Column(name = "subtotal")
	@DecimalMin(message = "O subtotal não pode ser inferior a R$0.01", value = "0.0", inclusive = false)
    @Digits(message = "O subtotal deve possuir o formato 'NNNNNNNNN.NN'", integer = 9, fraction = 2)
	private BigDecimal subtotal;
	
	@Column(name = "qtde_itens")
	@Positive(message = "A quantidade de itens deve ser positiva.")
	@NotNull(message = "A quantidade de item não pode ser nula.")
	private Integer qtdeItens;
	
	@Column(name = "nome")
	@NotNull(message = "O nome é obrigatório")
	private String nome;
	
	@Column(name = "recomendado")
	@NotNull(message = "A opção recomendada é obrigatória")
	private String promocao;
	
}
