# 10 Backend Design Pattern (demo Spring Boot)

Demo kode untuk video Babeh Vimouna "10 Backend Design Pattern". Satu project, sepuluh pola, masing-masing punya package, aplikasi kecil (`*App`), dan test yang bisa dijalankan.

Spring Boot 3.3 · Java 17 (dicoba di JDK 21) · Maven 3.9+ · H2 (in-memory) · Resilience4j 2.2

## Menjalankan

```bash
git clone https://github.com/babeh-vimouna/10-backend-design-pattern.git
cd 10-backend-design-pattern
mvn test        # 30 test; CircuitBreakerTest sekitar 40 detik karena menunggu 10 detik nyata
```

Menjalankan contoh Adapter dengan provider pilihan:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--pembayaran.provider=ovo
```

## Isi

| Pola | Package | Yang dibuktikan test |
|---|---|---|
| Repository | `repository` | derived query `findByPelangganIdAndStatus` jalan di H2 |
| Factory | `factory` | `PembayaranFactory` memilih gateway lewat `enum MetodeBayar`; GoPay aktif atau tidak lewat `pembayaran.gopay.aktif` |
| Singleton | `singleton` | dua service memegang satu `KonfigurasiToko`; scope `prototype` beda instance |
| Strategy | `strategy` | `HitungHarga` memilih diskon yang berlaku tanpa `if-else` |
| Observer | `observer` | `@EventListener` terpanggil tanpa mengubah `OrderService` |
| Builder | `builder` | Lombok `@Builder`, field yang tidak diisi bernilai `null` |
| Adapter | `adapter` | `pembayaran.provider` memilih adapter (midtrans, xendit, ovo) tanpa mengubah kode |
| Decorator | `decorator` | `@Bean @Primary` merakit Cache membungkus Log membungkus Db |
| CQRS | `cqrs` | command dan query terpisah, query memakai projection interface, database sama |
| Circuit Breaker | `circuitbreaker` | CLOSED, OPEN, HALF-OPEN dengan konfigurasi di `application-cb.yml` |

## Catatan

- **Adapter dan Factory dipisah per package.** Keduanya memakai interface `PembayaranGateway`. Di satu aplikasi, Factory butuh semua bean sedangkan Adapter butuh tepat satu, jadi bean-nya bentrok bila digabung.
- **Urutan Strategy bukan kontrak.** `List<StrategiDiskon>` tanpa `@Order` berurut nama class. Kalau urutan prioritas penting, tentukan eksplisit dengan `@Order`.
- **`@ConditionalOnProperty("pembayaran.gopay.aktif")`** aktif untuk nilai apa pun selain `false`. Untuk tegas, tambahkan `havingValue = "true"`.
- Payment gateway, client, dan data di dalamnya fiktif (simulasi), tanpa SDK asli.
- Error Maven `PKIX path building failed` biasanya karena sertifikat antivirus/proxy; pada Windows coba `MAVEN_OPTS="-Djavax.net.ssl.trustStoreType=WINDOWS-ROOT"`.

Lisensi: [MIT](LICENSE). Kode ini bahan belajar.
