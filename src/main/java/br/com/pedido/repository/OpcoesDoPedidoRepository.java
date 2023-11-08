package br.com.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pedido.entity.OpcaoDoPedido;
import br.com.pedido.entity.composite.OpcaoDoPedidoId;

@Repository
public interface OpcoesDoPedidoRepository extends JpaRepository<OpcaoDoPedido, OpcaoDoPedidoId> {

	@Query("SELECT op FROM OpcaoDoPedido op WHERE op.id.idDoPedido = :idDoPedido " + "AND op.id.idDaOpcao = :idDaOpcao")
	public OpcaoDoPedido buscarOpcaoPor(@Param("idDoPedido") Integer idDoPedido, @Param("idDaOpcao") Integer idDaOpcao);

	@Query("SELECT COUNT(op) FROM OpcaoDoPedido op WHERE op.id.idDoPedido = :idDoPedido"
			+ " AND op.id.idDaOpcao = :idDaOpcao")
	public Long contarPor(@Param("idDoPedido") Integer idDoPedido, @Param("idDaOpcao") Integer idDaOpcao);

}
