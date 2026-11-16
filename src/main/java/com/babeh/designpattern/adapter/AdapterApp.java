package com.babeh.designpattern.adapter;

import com.babeh.designpattern.common.Tagihan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdapterApp {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(AdapterApp.class, args);
        System.out.println("HASIL: " + ctx.getBean(CheckoutService.class).bayar(new Tagihan("INV-1", 50_000)));
    }
}
