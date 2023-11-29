package br.com.pedido.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pedido.entity.Pedido;
import br.com.pedido.entity.enums.Retirada;
import br.com.pedido.entity.enums.Status;

@Repository
public interface PedidosRepository extends JpaRepository<Pedido, Integer> {

	@Modifying
	@Query("UPDATE Pedido p SET p.status = :status WHERE p.id = :id")
	void atualizarStatusPor(@Param("id") Integer idDoPedido, @Param("status") Status novoStatus);

	@Query(value = 
			"SELECT p "
			+ "FROM Pedido p "
			+ "WHERE (:idRestaurante IS NULL OR p.idRestaurante = :idRestaurante) " 
			+ "AND p.status = :status "
			+ "AND (:retirada IS NULL OR p.retirada = :retirada) "
			+ "AND (:idUltimoPedido IS NULL OR p.id > :idUltimoPedido) "
			+ "ORDER BY p.data", 
			countQuery = "SELECT Count(p) "
						+ "FROM Pedido p "
						+ "WHERE (:idRestaurante IS NULL OR p.idRestaurante = :idRestaurante) " 
						+ "AND p.status = :status "
						+ "AND (:retirada IS NULL OR p.retirada = :retirada) "
						+ "AND (:idUltimoPedido IS NULL OR p.id > :idUltimoPedido)")
		public Page<Pedido> listarPor(Optional<Integer> idRestaurante, Status status, Optional<Retirada> retirada, Optional<Integer> idUltimoPedido, Pageable paginacao);
	
	@Query("SELECT p "
			+ "FROM Pedido p "
			+ "JOIN FETCH p.opcoes op "
			+ "WHERE p.id = :id "
			+ "ORDER BY p.data DESC")
	Pedido buscarPor(@Param("id") Integer idDoPedido);

}
