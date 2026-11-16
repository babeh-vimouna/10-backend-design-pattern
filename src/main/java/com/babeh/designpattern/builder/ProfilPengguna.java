package com.babeh.designpattern.builder;

import lombok.Builder;

@Builder
class ProfilPengguna {
  private String nama;
  private String email;
  private String telepon;
  private String alamat;
}
