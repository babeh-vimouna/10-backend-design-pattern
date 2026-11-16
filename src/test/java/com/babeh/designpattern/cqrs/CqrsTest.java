package com.babeh.designpattern.cqrs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = CqrsApp.class)
class CqrsTest {
    @Autowired PesananController controller;
    @Autowired PesananReadRepository readRepo;
    @Autowired PesananWriteRepository writeRepo;

    @Test
    void queryAliasKeProjectionLaluCommandBatalkan() throws Exception {
        var p = writeRepo.save(new Pesanan(9L, Status.AKTIF, new BigDecimal("12500.00")));
        var mvc = MockMvcBuilders.standaloneSetup(controller).build();

        var ringkas = readRepo.ringkasan(9L);
        assertThat(ringkas).hasSize(1);
        assertThat(ringkas.get(0).getId()).isEqualTo(p.id);
        assertThat(ringkas.get(0).getStatus()).isEqualTo(Status.AKTIF);

        mvc.perform(get("/pelanggan/9/pesanan"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(p.id))
            .andExpect(jsonPath("$[0].status").value("AKTIF"))
            .andExpect(jsonPath("$[0].total").value(12500.0));

        mvc.perform(delete("/pesanan/" + p.id)).andExpect(status().isOk());

        mvc.perform(get("/pelanggan/9/pesanan"))
            .andExpect(jsonPath("$[0].status").value("BATAL"));
    }
}
