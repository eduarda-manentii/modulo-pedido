package br.com.pedido.repository;

import java.util.List;

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

	@Query("SELECT p FROM Pedido p WHERE p.status = :status")
	public List<Pedido> listarPedidosPor(Status status);

	@Query(value = 
			"SELECT p "
			+ "FROM Pedido p "
			+ "WHERE p.idRestaurante = :idRestaurante " 
			+ "AND p.status = :status "
			+ "AND p.retirada = :retirada", 
			countQuery = "SELECT Count(p) "
						+ "FROM Pedido p "
						+ "WHERE p.idRestaurante = :idRestaurante " 
						+ "AND p.status = :status "
						+ "AND p.retirada = :retirada")
	public Page<Pedido> listarPor(Integer idRestaurante, Status status, Retirada retirada, Pageable paginacao);
	
	@Query(value = "SELECT p FROM Pedido p WHERE p.idRestaurante = :idRestaurante " + "AND p.status = :status", 
			countQuery = "SELECT Count(p) "
					+ " FROM Pedido p WHERE p.idRestaurante = :idRestaurante " + "AND p.status = :status")
	public Page<Pedido> listarPor(Integer idRestaurante, Status status, Pageable paginacao);

	@Query("SELECT p FROM Pedido p JOIN FETCH p.opcoes op WHERE p.id = :id ORDER BY p.data DESC")
	Pedido buscarPor(@Param("id") Integer idDoPedido);

}
