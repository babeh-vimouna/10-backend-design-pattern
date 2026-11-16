package com.babeh.designpattern.repository;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
class PesananService {
  private final PesananRepository repo;
  PesananService(PesananRepository repo) { this.repo = repo; }
  List<Pesanan> pesananAktif(Long id) {
    return repo.findByPelangganIdAndStatus(
        id, Status.AKTIF);
  }
}
