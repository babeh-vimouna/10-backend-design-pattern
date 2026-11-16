package com.babeh.designpattern.cqrs;

import org.springframework.data.jpa.repository.JpaRepository;

interface PesananWriteRepository extends JpaRepository<Pesanan, Long> {}   // tambahan
