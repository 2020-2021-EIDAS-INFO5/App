package com.polytech.polysign.service;

import com.polytech.polysign.domain.SignOrder;
import com.polytech.polysign.domain.SignedFile;
import com.polytech.polysign.repository.SignOrderRepository;
import com.polytech.polysign.repository.SignedFileRepository;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spire.ms.System.Collections.Hashtable;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.*;
import com.spire.pdf.security.GraphicMode;
import com.spire.pdf.security.PdfCertificate;
import com.spire.pdf.security.PdfCertificationFlags;
import com.spire.pdf.security.PdfSignature;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import javax.security.auth.x500.X500Principal;

import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import sun.security.x509.*;
import java.security.cert.*;
import java.security.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Service Implementation for managing {@link SignedFile}.
 */
@Service
@Transactional
public class SignedFileService {

	private static final String POLYSIGN = null;

	private final Logger log = LoggerFactory.getLogger(SignedFileService.class);

	private final SignedFileRepository signedFileRepository;

	private final SignOrderService signOrderService;



	public SignedFileService(SignedFileRepository signedFileRepository,SignOrderService signOrderService) {
		this.signedFileRepository = signedFileRepository;
		this.signOrderService = signOrderService;
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
	 * @throws Exception 
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 */
	public void certificateCreation(Long id) throws Exception {
		//Get sign order 

		SignOrder signOrder = signOrderService.findOne(id).get();
	
		// Load a pdf document
		PdfDocument doc = new PdfDocument();
		doc.loadFromFile("/home/dima/Bureau/App/polySign/src/main/java/com/polytech/polysign/service/Rapport.pdf");

		// Load the certificate file
		PdfCertificate cert = new PdfCertificate(SignedFileService.generatePfx(),"abc");

		// Create a PdfSignature object and specify its position and size
		PdfSignature signature = new PdfSignature(doc, doc.getPages().get(doc.getPages().getCount() - 1), cert,
				"MySignature");
		Rectangle2D rect = new Rectangle2D.Float();
		rect.setFrame(new Point2D.Float((float) doc.getPages().get(0).getActualSize().getWidth() - 300,
				(float) doc.getPages().get(0).getActualSize().getHeight() - 170), new Dimension(250, 120));
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
		signature.setSignImageSource(PdfImage
				.fromFile("/home/dima/Bureau/App/polySign/src/main/java/com/polytech/polysign/service/Mind.png"));

		// Set the signature font
		// signature.setSignDetailsFont(new PdfTrueTypeFont(new Font("Arial Unicode MS",
		// Font.PLAIN, 11)));

		// Set the document permission
		signature.setDocumentPermissions(PdfCertificationFlags.Forbid_Changes);
		signature.setCertificated(true);

		// Save to file
		doc.saveToFile("/home/dima/Bureau/App/polySign/src/main/java/com/polytech/polysign/service/output/TextAndImageSignature.pdf");
		doc.close();
		byte[] array = Files.readAllBytes(Paths.get("/home/dima/Bureau/App/polySign/src/main/java/com/polytech/polysign/service/output/TextAndImageSignature.pdf"));
		signOrder.getFile().setFileBytes(array);
		signOrder.setSigned(true);
	}


	public static X509Certificate generateCertificate(String dn, KeyPair pair, int days, String algorithm)
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

	public static String generatePfx() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
	    KeyPair pair = generateRSAKeyPair();
	    int days = 365;
		X509Certificate cert = generateCertificate(POLYSIGN, pair, days, "SHA1withRSA"); 
	    KeyStore ks = KeyStore.getInstance("PKCS12", "BC");   
	    char[] password = "abc".toCharArray();
	    ks.load(null,null);
	    ks.setCertificateEntry(cert.getSerialNumber().toString(), cert);
	    ks.setKeyEntry(cert.getSerialNumber().toString(), pair.getPrivate(), password, new Certificate[]{cert,cert});
	    File myFile = new File("keystore.pfx");
	    FileOutputStream fos = new FileOutputStream(myFile);
	    ks.store(fos, password);
	    fos.close();
	    String absolutePath = myFile.getAbsolutePath();
	    return absolutePath;
	}
	
	public static KeyPair generateRSAKeyPair(){
        try {/*ww  w.  j av  a2  s.co  m*/
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
            keyGenerator.initialize(1024);
            return keyGenerator.genKeyPair();
        } catch (Exception ex) {
            return null;
        }
    }



	/**
     * Delete the signedFile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SignedFile : {}", id);
        signedFileRepository.deleteById(id);
    }

    }	

