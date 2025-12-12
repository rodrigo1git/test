package com.prototype.socialNetwork.minIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class MinioConfig {

    // 1. Inyección de Propiedades (desde application.properties)
    @Value("${minio.access.key}")
    private String accessKey;

    @Value("${minio.secret.key}")
    private String secretKey;

    @Value("${minio.endpoint.url}")
    private String endpointUrl;

    @Value("${minio.region}")
    private String region;

    // 2. Definición del Bean S3Client
    @Bean
    public S3Client s3Client() {

        // a. Carga las credenciales
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        // b. Construye y configura el cliente para MinIO local
        return S3Client.builder()
                .endpointOverride(URI.create(endpointUrl)) // <--- Obliga a usar la URL de MinIO (localhost:9000)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .forcePathStyle(true) // <--- NECESARIO para que MinIO funcione correctamente
                .build();
    }
}