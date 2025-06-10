package co.mw.gf_dashboard_service.config;

import co.mw.gf_dashboard_service.common.LoggerUtil;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoClientConfig {

private static final Logger logger = LoggerUtil.getLogger(MongoClientConfig.class);

    public @Bean MongoClient mongoClient(@Value("${spring.data.mongodb.uri}") String uri) {
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(new ConnectionString(uri))
                .applyToSslSettings(builder -> {
                    builder.enabled(true);
                    builder.context(build());
                })
                .build();
        logger.info("DB Client initialized.");
        return MongoClients.create(settings);
    }

    public static SSLContext build() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            ClassPathResource resource = new ClassPathResource("aws-ap-south-1.jks");
            InputStream is = resource.getInputStream();
            keyStore.load(is, "changeit".toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            logger.info("TLS context initialized.");
            return sslContext;
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException |
                 KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}