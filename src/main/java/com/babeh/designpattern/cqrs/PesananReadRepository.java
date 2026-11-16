package com.babeh.designpattern.cqrs;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface PesananReadRepository extends JpaRepository<Pesanan, Long> {
  @Query("select p.id as id, p.status as status, p.total as total "
       + "from Pesanan p where p.pelangganId = :pid")
  List<RingkasanPesanan> ringkasan(@Param("pid") Long pid);   // tanpa entity penuh
}
