package com.polytech.polysign.service;

import com.polytech.polysign.domain.SignedFile;
import com.polytech.polysign.repository.SignedFileRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.*;
import com.spire.pdf.security.GraphicMode;
import com.spire.pdf.security.PdfCertificate;
import com.spire.pdf.security.PdfCertificationFlags;
import com.spire.pdf.security.PdfSignature;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Optional;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import sun.security.x509.*;
import java.security.cert.*;
import java.security.*;
import java.math.BigInteger;
import java.util.Date;

/**
 * Service Implementation for managing {@link SignedFile}.
 */
@Service
@Transactional
public class SignedFileService {

        private final Logger log = LoggerFactory.getLogger(SignedFileService.class);

        private final SignedFileRepository signedFileRepository;

        public SignedFileService(SignedFileRepository signedFileRepository) {
                this.signedFileRepository = signedFileRepository;
        }

        /**
         * Save a signedFile.
         *
         * @param signedFile the entity to save.
         * @return the persisted entity.
         */
        public SignedFile save(SignedFile signedFile) {
                log.debug("Request to save SignedFile : {}", signedFile);
                return signedFileRepository.save(signedFile);
        }

        /**
         * Get all the signedFiles.
         *
         * @param pageable the pagination information.
         * @return the list of entities.
         */
        @Transactional(readOnly = true)
        public Page<SignedFile> findAll(Pageable pageable) {
                log.debug("Request to get all SignedFiles");
                return signedFileRepository.findAll(pageable);
        }

        /**
         * Get one signedFile by id.
         *
         * @param id the id of the entity.
         * @return the entity.
         */
        @Transactional(readOnly = true)
        public Optional<SignedFile> findOne(Long id) {
                log.debug("Request to get SignedFile : {}", id);
                return signedFileRepository.findById(id);
        }

        /**
         * Delete the signedFile by id.
         *
         * @param id the id of the entity.
         * @throws NoSuchProviderException
         * @throws NoSuchAlgorithmException
         * @throws KeyStoreException
         * @throws IOException
         * @throws CertificateException
         */
        public void delete(Long id) {

                // Load a pdf document
                PdfDocument doc = new PdfDocument();
                doc.loadFromFile(
                                "/home/dima/Vidéos/App/polySign/src/main/java/com/polytech/polysign/service/Rapport.pdf");

                // Load the certificate file
                PdfCertificate cert = new PdfCertificate(
                                "/home/dima/Vidéos/App/polySign/src/main/java/com/polytech/polysign/service/certificate.pfx",
                                "az12er12");

                // Create a PdfSignature object and specify its position and size
                PdfSignature signature = new PdfSignature(doc, doc.getPages().get(doc.getPages().getCount() - 1), cert,
                                "MySignature");
                Rectangle2D rect = new Rectangle2D.Float();
                rect.setFrame(new Point2D.Float((float) doc.getPages().get(0).getActualSize().getWidth() - 300,
                                (float) doc.getPages().get(0).getActualSize().getHeight() - 170),
                                new Dimension(250, 120));
                signature.setBounds(rect);

                // Set the graphics mode
                signature.setGraphicMode(GraphicMode.Sign_Image_And_Sign_Detail);

                // Set the signature content
                signature.setNameLabel("Singer:");
                signature.setName("Gary");
                signature.setContactInfoLabel("Telephone:");
                signature.setContactInfo("010333555");
                signature.setDateLabel("Date:");
                signature.setDate(new java.util.Date());
                signature.setLocationInfoLabel("Location:");
                signature.setLocationInfo("USA");
                signature.setReasonLabel("Reason:");
                signature.setReason("I am the author");
                signature.setDistinguishedNameLabel("DN: ");
                signature.setDistinguishedName(signature.getCertificate().get_IssuerName().getName());
                signature.setSignImageSource(PdfImage.fromFile(
                                "/home/dima/Vidéos/App/polySign/src/main/java/com/polytech/polysign/service/Mind.png"));

                // Set the signature font
                // signature.setSignDetailsFont(new PdfTrueTypeFont(new Font("Arial Unicode MS",
                // Font.PLAIN, 11)));

                // Set the document permission
                signature.setDocumentPermissions(PdfCertificationFlags.Forbid_Changes);
                signature.setCertificated(true);

                // Save to file
                doc.saveToFile("/home/dima/Vidéos/App/polySign/src/main/java/com/polytech/polysign/service/output/TextAndImageSignature.pdf");
                doc.close();
        }

        public void sign(Long id) {
                // Load a pdf document
                PdfDocument doc = new PdfDocument();
                doc.loadFromFile("/home/dima/Téléchargements/Rapport final.pdf");

                // Load the certificate file
                PdfCertificate cert = new PdfCertificate(
                                "/home/dima/Vidéos/App/polySign/src/main/java/com/polytech/polysign/service/baeldung.p12",
                                "e-iceblue");

                // Create a PdfSignature object and specify its position and size
                PdfSignature signature = new PdfSignature(doc, doc.getPages().get(doc.getPages().getCount() - 1), cert,
                                "MySignature");
                Rectangle2D rect = new Rectangle2D.Float();
                rect.setFrame(new Point2D.Float((float) doc.getPages().get(0).getActualSize().getWidth() - 300,
                                (float) doc.getPages().get(0).getActualSize().getHeight() - 170),
                                new Dimension(250, 120));
                signature.setBounds(rect);

                // Set the graphics mode
                signature.setGraphicMode(GraphicMode.Sign_Image_And_Sign_Detail);

                // Set the signature content
                signature.setNameLabel("Singer:");
                signature.setName("Gary");
                signature.setContactInfoLabel("Telephone:");
                signature.setContactInfo("010333555");
                signature.setDateLabel("Date:");
                signature.setDate(new java.util.Date());
                signature.setLocationInfoLabel("Location:");
                signature.setLocationInfo("USA");
                signature.setReasonLabel("Reason:");
                signature.setReason("I am the author");
                signature.setDistinguishedNameLabel("DN: ");
                signature.setDistinguishedName(signature.getCertificate().get_IssuerName().getName());
                signature.setSignImageSource(PdfImage.fromFile("/home/dima/Téléchargements/Mind Map.png"));

                // Set the signature font
                signature.setSignDetailsFont(new PdfTrueTypeFont(new Font("Arial Unicode MS", Font.PLAIN, 11)));

                // Set the document permission
                signature.setDocumentPermissions(PdfCertificationFlags.Forbid_Changes);
                signature.setCertificated(true);

                // Save to file
                doc.saveToFile("/home/dima/Téléchargements/TextAndImageSignature.pdf");
                doc.close();
        }

        public X509Certificate generateCertificate(String dn, KeyPair pair, int days, String algorithm)
                        throws GeneralSecurityException, IOException {
                PrivateKey privkey = pair.getPrivate();
                X509CertInfo info = new X509CertInfo();
                Date from = new Date();
                Date to = new Date(from.getTime() + days * 86400000l);
                CertificateValidity interval = new CertificateValidity(from, to);
                BigInteger sn = new BigInteger(64, new SecureRandom());
                X500Name owner = new X500Name(dn);

                info.set(X509CertInfo.VALIDITY, interval);
                info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
                info.set(X509CertInfo.SUBJECT, owner);
                info.set(X509CertInfo.ISSUER, owner);
                info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
                info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
                AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
                info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

                // Sign the cert to identify the algorithm that's used.
                X509CertImpl cert = new X509CertImpl(info);
                cert.sign(privkey, algorithm);

                // Update the algorith, and resign.
                algo = (AlgorithmId) cert.get(X509CertImpl.SIG_ALG);
                info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
                cert = new X509CertImpl(info);
                cert.sign(privkey, algorithm);
                return cert;
        }

}
