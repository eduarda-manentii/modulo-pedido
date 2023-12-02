package br.com.pedido.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.pedido.Dto.Cliente;
import br.com.pedido.Dto.Cupom;
import br.com.pedido.Dto.Endereco;
import br.com.pedido.Dto.EnderecoRestaurante;
import br.com.pedido.Dto.NovaOpcaoDoPedido;
import br.com.pedido.Dto.Restaurante;
import br.com.pedido.Dto.Usuario;
import br.com.pedido.entity.enums.Pagamento;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data 
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "pedidos") 
@Entity(name = "Pedido")
public class Pedido {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@NotNull(message = "O status do pedido é obrigatório.")
	@Enumerated(value = EnumType.STRING)
	@Column(name = "status")
	private Status status;
	
	@NotNull(message = "O tipo de entrega do pedido é obrigatória.")
	@Enumerated(value = EnumType.STRING)
	@Column(name = "retirada")
	private Retirada retirada;
	
	@Column(name = "pagamento")
	@NotNull(message = "O pagamento é obrigatório.")
	@Enumerated(value = EnumType.STRING)
	private Pagamento pagamento;
	
	@DecimalMin(value = "0.0", inclusive = false, message = "O valor total não pode ser inferior a 0.0")
	@Digits(integer = 9, fraction = 2, message = "O valor total deve possuir o formato 'NNNNNNNNN.NN'")
	@Column(name = "valor_total")
	private BigDecimal valorTotal;
	
	@DecimalMin(value = "0.0", inclusive = true, message = "O valor de desconto não pode ser inferior a 0.0")
	@Digits(integer = 9, fraction = 2, message = "O valor de desconto deve possuir o formato 'NNNNNNNNN.NN'")
	@Column(name = "valor_desconto")
	private BigDecimal valorDesconto;
	
	@DecimalMin(value = "0.0", inclusive = false, message = "O valor dos itens não pode ser inferior a 0.0")
	@Digits(integer = 9, fraction = 2, message = "O valor dos itens deve possuir o formato 'NNNNNNNNN.NN'")
	@Column(name = "valor_itens")
	private BigDecimal valorItens;
	
	@DecimalMin(value = "0.0", inclusive = true, message = "O valor do frete não pode ser inferior a 0.0")
	@Digits(integer = 9, fraction = 1, message = "O valor de frete deve possuir o formato 'NNNNNNNNN.N'")
	@Column(name = "valor_frete")
	private BigDecimal valorFrete;
	
	@Column(name = "id_cliente")
	@NotNull(message = "O cliente é obrigatório.")
	private Integer idCliente;
	
	@Transient
	private Cliente cliente;
	
	@Transient
	private Usuario usuario;
	
	@Column(name = "id_cupom")
	private Integer idCupom;
	
	@Transient
	private Cupom cupom;
	
	@Column(name = "id_endereco")
	@NotNull(message = "O endereço é obrigatório.")
	private Integer idEndereco;
	
	@Transient
	private Endereco endereco;
	
	@NotNull(message = "O cardapio é obrigatório.")
	@Column(name = "id_cardapio")
	private Integer idCardapio;
	
	@Column(name = "id_restaurante")
	@NotNull(message = "O restaurante é obrigatório.")
	private Integer idRestaurante;
	
	@Transient
	private Restaurante restaurante;
	
	@Transient
	private EnderecoRestaurante enderecoRestaurante;
	
	@Column(name = "data")
	private LocalDateTime data;
	
	@OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, 
            cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OpcaoDoPedido> opcoes;

	@Transient
	private List<NovaOpcaoDoPedido> novasOpcoes;
	
	public Pedido() {
		 this.status = Status.REALIZADO;
		 this.opcoes = new ArrayList<OpcaoDoPedido>();
		 this.data = LocalDateTime.now();
		 this.novasOpcoes = new ArrayList<>();
	}
	
	@Transient
	public boolean isParaRetirada() {
		return getRetirada() == Retirada.RETIRADA;
	}
}
