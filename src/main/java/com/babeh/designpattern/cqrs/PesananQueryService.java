package com.babeh.designpattern.cqrs;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
class PesananQueryService {           // baca: versi ringan, database sama
  private final PesananReadRepository readRepo;
  PesananQueryService(PesananReadRepository readRepo) { this.readRepo = readRepo; }   // tambahan

  List<RingkasanPesanan> daftar(Long id) { return readRepo.ringkasan(id); }
}
