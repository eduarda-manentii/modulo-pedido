package br.com.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pedido.entity.OpcaoDoPedido;
import br.com.pedido.entity.composite.OpcaoDoPedidoId;

@Repository
public interface OpcoesDoPedidoRepository extends JpaRepository<OpcaoDoPedido, OpcaoDoPedidoId> {

}
