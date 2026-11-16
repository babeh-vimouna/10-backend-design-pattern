package com.babeh.designpattern.builder;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class BuilderTest {
    static Object baca(Object o, String f) throws Exception {
        Field field = o.getClass().getDeclaredField(f);
        field.setAccessible(true);
        return field.get(o);
    }

    @Test
    void builderLombokJalanFieldKosongNull() throws Exception {
        ProfilPengguna profil = ProfilPengguna.builder()
            .nama("Budi")
            .email("budi@contoh.id")
            .telepon("0812xxxxxxx")
            .build();

        assertThat(baca(profil, "nama")).isEqualTo("Budi");
        assertThat(baca(profil, "email")).isEqualTo("budi@contoh.id");
        assertThat(baca(profil, "telepon")).isEqualTo("0812xxxxxxx");
        assertThat(baca(profil, "alamat")).isNull();
    }
}
