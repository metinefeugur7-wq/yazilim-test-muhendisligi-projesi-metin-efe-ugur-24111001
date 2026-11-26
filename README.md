# Yazılım Test Mühendisliği Proje Ödevi — Rest Assured Otomatik API Regresyon Testi

Bu proje, **Yazılım Test Mühendisliği** proje ödevi kapsamında **Java 17**, **Maven**, **JUnit 5** ve **Rest Assured** kütüphanesi kullanılarak geliştirilmiş katmanlı bir otomatik API regresyon testi projesidir.

Hedef test servisi olarak herkese açık, API anahtarı gerektirmeyen [JSONPlaceholder](https://jsonplaceholder.typicode.com/) REST API kullanılmıştır.

## Karşılanan Ödev Gereksinimleri

- **Teknoloji Yığını:** Java 17, Maven, JUnit 5 (`5.13.4`), [Rest Assured](https://rest-assured.io/) (`5.5.6`), Jackson (`2.18.2`).
- **HTTP Metotları:** Hem **GET** hem de **POST** istekleri için ayrı test sınıfları ve birden fazla senaryo içerir.
- **Request Body:** POST testlerinde hem **POJO (`Post`)** hem de **`Map<String, Object>`** ile JSON request body gönderimi yapılır.
- **Üçlü Doğrulama:** Her test senaryosunda:
  1. **Status Code Kontrolü** (`200 OK`, `201 Created`, `404 Not Found`)
  2. **Response Body Beklenen Değer Kontrolleri** (Hamcrest matcher'ları + Jackson POJO deserialization + JUnit 5 `assertAll`)
  3. **Yanıt Süresi Kontrolü** (`< 3000 ms`, `config.properties` üzerinden yapılandırılabilir)
- **Temiz Kod ve Regresyon Pratikleri:** Katmanlı mimari (`config`, `base`, `client`, `model`, `tests`), yeniden kullanılabilir `RequestSpecification` / `ResponseSpecification`, başarısızlık durumunda otomatik loglama (`enableLoggingOfRequestAndResponseIfValidationFails`), `@Tag("regression")` / `@Tag("smoke")` etiketleme ve negatif test senaryosu.
- **Sunum Dosyası:** `docs/Yapay_Zeka_Destekli_Yazilim_Test_Muhendisligi_Sunumu.pptx` dosyası depo içerisinde yer almaktadır.

## Proje Klasör Yapısı

```text
rest-assured-api-regression-tests/
├── docs/
│   └── Yapay_Zeka_Destekli_Yazilim_Test_Muhendisligi_Sunumu.pptx
├── pom.xml
├── README.md
└── src/test/
    ├── java/com/testmuhendisligi/api/
    │   ├── base/BaseApiTest.java
    │   ├── client/PostApiClient.java
    │   ├── config/ApiConfig.java
    │   ├── model/Post.java
    │   └── tests/
    │       ├── PostGetRegressionTest.java
    │       └── PostCreateRegressionTest.java
    └── resources/
        └── config.properties
```

## Gereksinimler

- **JDK 17** veya üzeri (`java -version`)
- **Apache Maven 3.8+** (`mvn -version`)
- İnternet bağlantısı (Maven bağımlılıkları ve JSONPlaceholder API erişimi için)

## Testleri Çalıştırma

Tüm otomatik regresyon testlerini çalıştırmak için proje kök dizininde:

```bash
mvn clean test
```

Yalnızca `smoke` etiketli temel testleri çalıştırmak için:

```bash
mvn test -Dgroups=smoke
```

Maksimum yanıt süresi eşiğini komut satırından geçersiz kılmak için (ör. `2000 ms`):

```bash
mvn test -Dapi.max.response.time.ms=2000
```

## Public GitHub Reposuna Yükleme Adımları

1. GitHub üzerinde yeni bir **Public** repository oluşturun (ör. `rest-assured-api-regression-tests`).
2. ZIP arşivini çıkarıp proje klasörüne girin ve aşağıdaki komutları çalıştırın:

```bash
git init
git add .
git commit -m "feat: Rest Assured otomatik regresyon test projesi ve sunum eklendi"
git branch -M main
git remote add origin https://github.com/<KULLANICI_ADINIZ>/rest-assured-api-regression-tests.git
git push -u origin main
```

3. Oluşturduğunuz public GitHub repository URL'sini ödev tesliminde paylaşın.

## Kaynaklar

- [Rest Assured Resmi Dokümantasyonu](https://rest-assured.io/)
- [Rest Assured GitHub Deposu](https://github.com/rest-assured/rest-assured)
- [JSONPlaceholder Free Fake REST API](https://jsonplaceholder.typicode.com/)
