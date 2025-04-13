package com.babystore.jparepository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babystore.model.Order;

public interface OrderHistoryRepository extends JpaRepository<Order, Integer>{
	@Query(value = "SELECT o.id, COUNT(od.id_product_detail) as totalProduct, ar.full_name_address, ss.name, (o.total + o.shipping_fee) AS tongTien  "
			+ "FROM orders o  "
			+ "INNER JOIN order_details od ON od.id_order = o.id "
			+ "INNER JOIN accounts a ON o.id_account = a.id "
			+ "INNER JOIN shipping_statuses ss ON o.id_shipping_status = ss.id "
			+ "INNER JOIN addresses ar ON o.id_address = ar.id  "
			+ "WHERE a.id = :idTaiKhoan "
			+ "AND ss.name LIKE %:trangThai% "
			+ "GROUP BY o.id, ar.full_name_address, ss.name, (o.total + o.shipping_fee) "
			+ "ORDER BY o.id DESC ",nativeQuery = true)
	public List<Object[]> listLichSuHoaDon(@Param("idTaiKhoan")Integer idTaiKhoan,@Param("trangThai")String trangThai);
	@Query(value = "SELECT  o.id, p.image, p.name, c.name, od.quantity, (od.quantity * od.price) AS tongTien, pd.id "
			+ "FROM orders o  "
			+ "INNER JOIN order_details od ON od.id_order = o.id "
			+ "INNER JOIN accounts a ON o.id_account = a.id "
			+ "INNER JOIN product_details pd ON od.id_product_detail = pd.id "
			+ "INNER JOIN products p ON pd.id_product = p.id "
			+ "INNER JOIN colors c ON pd.id_color = c.id "
			+ "WHERE a.id = :idTaiKhoan  "
			+ "AND o.id = :idOrder "
			+ "ORDER BY o.id DESC ", nativeQuery = true)
	public List<Object[]> listChiTietLichSuHoaDonByMaHD(@Param("idTaiKhoan")Integer idTaiKhoan,@Param("idOrder")Integer idOrder);
}
