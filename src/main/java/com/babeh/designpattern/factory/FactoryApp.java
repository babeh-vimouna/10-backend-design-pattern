package com.babeh.designpattern.factory;

import com.babeh.designpattern.common.Tagihan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FactoryApp {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(FactoryApp.class, args);
        System.out.println("HASIL: " + ctx.getBean(CheckoutService.class).bayar(MetodeBayar.QRIS, new Tagihan("INV-1", 50_000)));
    }
}
