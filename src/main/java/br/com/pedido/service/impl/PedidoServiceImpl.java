package br.com.pedido.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.pedido.Dto.NovaOpcaoDoPedido;
import br.com.pedido.Dto.NovoPedido;
import br.com.pedido.entity.OpcaoDoPedido;
import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.composite.OpcaoDoPedidoId;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;
import br.com.pedido.repository.PedidosRepository;
import br.com.pedido.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {
	
	@Autowired
	private PedidosRepository repository;
	
	@Override
	public Pedido salvar(NovoPedido novoPedido) {
		Pedido pedido = new Pedido();
	    pedido.setRetirada(novoPedido.getRetirada());
	    pedido.setPagamento(novoPedido.getPagamento());
	    pedido.setStatus(novoPedido.getStatus());
	    pedido.setIdCliente(novoPedido.getIdCliente());
	    pedido.setIdCupom(novoPedido.getIdCupom());
	    pedido.setIdEndereco(novoPedido.getIdEndereco());
	    pedido.setIdRestaurante(novoPedido.getIdRestaurante());
	    pedido.setCupom(novoPedido.getCupom());
	    pedido.setIdCardapio(novoPedido.getIdDoCardapio());
	    
	    BigDecimal frete = novoPedido.getValorFrete();
	    BigDecimal freteArredondado = frete.setScale(1, RoundingMode.HALF_UP);
	    pedido.setValorFrete(freteArredondado);
	    
	    BigDecimal valorItens = novoPedido.getValorItens();
	    BigDecimal desconto = BigDecimal.ZERO;

	    if (pedido.getIdCupom() != null && pedido.getIdCupom() != 0) {
	        BigDecimal porcentagemDesconto = pedido.getCupom().getValor();
	        desconto = (porcentagemDesconto.multiply(valorItens)).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
	    }
	    
	    
	    pedido.setValorDesconto(desconto);
	    
	    BigDecimal valorItensComDesconto = valorItens.subtract(desconto);
	    pedido.setValorItens(valorItensComDesconto);
	    BigDecimal subtotal = valorItensComDesconto.add(freteArredondado);

	    pedido.setValorTotal(subtotal);

	    Pedido pedidoSalvo = repository.save(pedido);

	    for (NovaOpcaoDoPedido novaOpcao : novoPedido.getOpcoes()) {
	    	OpcaoDoPedidoId id = new OpcaoDoPedidoId();
			id.setIdDaOpcao(novaOpcao.getIdDaOpcao());
			id.setIdDoPedido(pedidoSalvo.getId());	
			OpcaoDoPedido opcaoDoPedido = new OpcaoDoPedido();
			opcaoDoPedido.setId(id);
			opcaoDoPedido.setPedido(pedidoSalvo);
			opcaoDoPedido.setQtdeItens(novaOpcao.getQtdeItens());
			BigDecimal valorItem = novaOpcao.getValorItem().setScale(2, RoundingMode.HALF_UP);
			opcaoDoPedido.setValorItem(valorItem);
			BigDecimal subTotal = valorItem.multiply(new BigDecimal(novaOpcao.getQtdeItens()));
			opcaoDoPedido.setSubtotal(subTotal);
			opcaoDoPedido.setNome(novaOpcao.getNome());
			opcaoDoPedido.setPromocao(novaOpcao.getPromocao());
			
	        pedidoSalvo.getOpcoes().add(opcaoDoPedido);
	    }
	    this.repository.save(pedidoSalvo);
	    return repository.buscarPor(pedidoSalvo.getId());
	}

	@Override
	public void atualizarStatusPor(Integer id, Status novoStatus) {
	    Pedido pedidoEncontrado = repository.buscarPor(id);
	    Preconditions.checkNotNull(pedidoEncontrado, "Não existe pedido para o id informado");

	    Status statusAtual = pedidoEncontrado.getStatus();
	    Preconditions.checkArgument(
	            novoStatus.ordinal() >= statusAtual.ordinal(),
	            "O novo status não pode ser anterior ao status atual");
		boolean isTramitacaoLiberada = false;
	    switch (statusAtual) {
			case REALIZADO:
				isTramitacaoLiberada = novoStatus.equals(Status.ACEITO_PELO_RESTAURANTE) || 
						novoStatus.equals(Status.CANCELADO);
				Preconditions.checkArgument(isTramitacaoLiberada, 
						"Próximo status inválido para o status atual REALIZADO." 
							+ "Próximo status esperado: ACEITO_PELO_RESTAURANTE ou CANCELADO.");
				break;
			case ACEITO_PELO_RESTAURANTE:
				boolean isRetiradaBalcao = pedidoEncontrado.isParaRetirada();
			    isTramitacaoLiberada = novoStatus.equals(Status.PRONTO_PARA_COLETA) || isRetiradaBalcao;
			    if (!isRetiradaBalcao) {
			    	Preconditions.checkArgument(isTramitacaoLiberada, 
							"Próximo status inválido para o status atual ACEITO_PELO_RESTAURANTE." 
								+ " Próximo status esperado: PRONTO_PARA_COLETA.");
				} else {
				Preconditions.checkArgument(isTramitacaoLiberada, 
						"Próximo status inválido para o status atual ACEITO_PELO_RESTAURANTE." 
							+ " Próximo status esperado: PRONTO_PARA_COLETA ou ENTREGUE.");
				}
				break;
			case PRONTO_PARA_COLETA:
			 isTramitacaoLiberada = novoStatus.equals(Status.ACEITO_PARA_ENTREGA);
				Preconditions.checkArgument(isTramitacaoLiberada,
						"Próximo status inválido para o status atual PRONTO_PARA_COLETA. "
							+ "Próximo status esperado: ACEITO_PARA_ENTREGA.");
				break;
			case ACEITO_PARA_ENTREGA:
				isTramitacaoLiberada = novoStatus.equals(Status.ENTREGUE);
				Preconditions.checkArgument(isTramitacaoLiberada,
						"Próximo status inválido para o status atual ACEITO_PARA_ENTREGA. "
							+ "Próximo status esperado: ENTREGUE.");
				break;
			case ENTREGUE:
				throw new IllegalArgumentException("Não é permitido alterar o status a partir de 'ENTREGUE'");
			default:
				break;
		}
	    repository.atualizarStatusPor(id, novoStatus);
	}
	
	@Override
	public Page<Pedido> listarPor(Optional<Integer> idRestaurante, Status status, 
			Optional<Retirada> retirada, Optional<Integer> resumo, 
			Optional<Integer> idUltimoPedido, Optional<Integer> idCliente, Pageable paginacao) {
		return repository.listarPor(idRestaurante, status, retirada, 
				idUltimoPedido, idCliente, paginacao);
	}

	@Override
	public Pedido buscarPor(Integer id) {
		Pedido pedidoEncontrado = repository.buscarPor(id);
		Preconditions.checkNotNull(pedidoEncontrado, 
				"Não existe pedido para o id informado");
		return pedidoEncontrado;
	}

}