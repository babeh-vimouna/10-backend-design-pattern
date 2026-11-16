package com.babeh.designpattern.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesananRepository
    extends JpaRepository<Pesanan, Long> {
  List<Pesanan> findByPelangganIdAndStatus(
      Long pelangganId, Status status);
}
