package com.prototype.socialNetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class Minioservice {

        @Autowired
        private S3Client s3Client;

        @Value("${minio.bucket.name}")
        private String bucketName;

        @Value("${minio.endpoint.url}")
        private String endpointUrl;

        /**
         * Sube un archivo a MinIO y retorna la URL pública para acceder a él.
         */
        public String uploadImage(MultipartFile file) {
            try {
                // 1. Generar un nombre único para evitar colisiones (ej. "f8c3de3d... .jpg")
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                // 2. Crear la solicitud de subida (PutObject)
                PutObjectRequest putOb = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType()) // Importante para que el navegador sepa que es imagen
                        .build();

                // 3. Enviar el archivo a MinIO
                s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                // 4. Construir y retornar la URL de acceso
                // Formato: http://localhost:9000/nombre-bucket/nombre-archivo
                return endpointUrl + "/" + bucketName + "/" + fileName;

            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen a MinIO", e);
            }
        }

}
