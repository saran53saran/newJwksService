package com.example.jwks.service;


import com.example.jwks.config.certificateConfigs;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@Service
public class publicKeyGeneratorService {

    private final certificateConfigs certificateConfig;

    public publicKeyGeneratorService(certificateConfigs certificateConfig) {
        this.certificateConfig = certificateConfig;

    }

    public String publicKeyGenerator() throws CertificateException {
        String certPEM = certificateConfig.getCertificate()
                .replace("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replaceAll("\n", "");

        // Decode certificate from base64
        byte[] certBytes = Base64.getDecoder().decode(certPEM);

        // Generate X509Certificate object from decoded bytes
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));

        // Get the public key from the certificate
        String publicKeyBase64 = Base64.getEncoder().encodeToString(certificate.getPublicKey().getEncoded());

        return publicKeyBase64;
    }
}

