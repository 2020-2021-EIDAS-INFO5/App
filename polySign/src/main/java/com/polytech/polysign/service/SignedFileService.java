package com.polytech.polysign.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.polytech.polysign.domain.SignOrder;
import com.polytech.polysign.domain.SignatureProcess;
import com.polytech.polysign.domain.SignedFile;
import com.polytech.polysign.domain.UserEntity;
import com.polytech.polysign.domain.enumeration.Status;
import com.polytech.polysign.repository.SignatureProcessRepository;
import com.polytech.polysign.repository.SignedFileRepository;
import com.polytech.polysign.domain.UserEntity;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import com.polytech.polysign.repository.UserEntityRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.spire.ms.System.Collections.Hashtable;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.*;
import com.spire.pdf.security.GraphicMode;
import com.spire.pdf.security.PdfCertificate;
import com.spire.pdf.security.PdfCertificationFlags;
import com.spire.pdf.security.PdfSignature;

import io.undertow.util.FileUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Optional;
import java.util.Random;

import javax.security.auth.x500.X500Principal;

import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;

import sun.security.x509.*;
import java.security.cert.*;
import java.security.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.examples.signature.*;
 import org.apache.pdfbox.examples.signature.CreateSignature;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.examples.signature.CreateVisibleSignature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Service Implementation for managing {@link SignedFile}.
 */
@Service
@Transactional
public class SignedFileService {

    private static final int days = 365;

    private static final String POLYSIGN = "CN=PolySign, L=Grenoble, C=FR";

    private final UserEntityRepository userEntityRepository;

    private final SignatureProcessRepository signatureProcessRepository;
    
    private final UserEntityService userEntityService;

    private final SignOrderService signOrderService;
    
	private final SignedFileRepository signedFileRepository;
	
	private final Logger log = LoggerFactory.getLogger(SignatureProcessService.class);
	
	private final static char[] password = generatePassword(6);

	
    public SignedFileService(SignedFileRepository signedFileRepository,@Lazy SignOrderService signOrderService,@Lazy UserEntityRepository userEntityRepository,UserEntityService userEntityService, SignatureProcessRepository signatureProcessRepository) {
        this.userEntityService = userEntityService;
		this.signedFileRepository = signedFileRepository;
        this.userEntityRepository = userEntityRepository;
        this.signatureProcessRepository = signatureProcessRepository;
        this.signOrderService = signOrderService;
    }



    /**
     * Save a signedFile.
     *
     * @param signedFile the entity to save.
     * @return the persisted entity.
     */
    public SignedFile saveSignedFileAndSignatureProcess(SignedFile signedFile) {

            SignedFile signedFile1 = signedFileRepository.save(signedFile);
             // Get UserEntity
             UserEntity userEntity = userEntityRepository.findByFirstname(signedFile1.getFilename());
             // Create Signature Process
             SignatureProcess signatureProcess = new SignatureProcess();
             signatureProcess.setCreator(userEntity);
             signatureProcess.setEmissionDate(Instant.now());
             Instant emissionDate = signatureProcess.getEmissionDate();
             Instant expirationDate = emissionDate.plus(14, ChronoUnit.DAYS);
             signatureProcess.setExpirationDate(expirationDate);
    

             signatureProcess.finalFile(signedFile1);
             signatureProcess.setTitle(signedFile1.getFilename());
             signatureProcess.setOrderedSigning(false);
             signatureProcess.setStatus(Status.PENDING);
             signatureProcessRepository.save(signatureProcess);

             return signedFile1;

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
	 * Delete the signedFile by id.
	 *
	 * @param id the id of the entity.
	 * @param userId 
	 * @throws Exception 
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 */
	public void certificateCreation(Long id, Long userId) throws Exception {
		
		//Get the user who signs
		UserEntity userEntity = userEntityService.findOne(userId).get();
		
		
		//Get sign order 

		SignOrder signOrder = signOrderService.findOne(id).get();
		
		// Load a pdf document
		PdfDocument doc = new PdfDocument();
		
		doc.loadFromBytes(signOrder.getFile().getFileBytes());
		// Load the certificate file
		String pass = new String(password);
		PdfCertificate cert = new PdfCertificate(SignedFileService.generatePfx(),pass);

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
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        
		signature.setNameLabel("Signer:");
		signature.setNameLabel(userEntity.getFirstname() + userEntity.getLastname());
		signature.setContactInfoLabel("Telephone:");
		signature.setContactInfoLabel(userEntity.getPhone());
		signature.setDateLabel("Date:");
		signature.setDate(new java.util.Date());
		signature.setDateLabel("email");
		signature.setDateLabel(userEntity.getEmail());
		signature.setDistinguishedNameLabel("DN: ");
		signature.setDistinguishedName(signature.getCertificate().get_IssuerName().getName());
		signature.setSignImageSource(PdfImage.fromImage(bufferedImage));
		
		// Set the document permission
		signature.setDocumentPermissions(PdfCertificationFlags.Forbid_Changes);
		signature.setCertificated(true);

		// Save to file
	
   		doc.saveToFile("/home/dima/Bureau/App/polySign/src/main/java/com/polytech/polysign/service/output/" + signOrder.getFile().getFilename() + userId + ".pdf");
		doc.close();
		
		byte[] array = Files.readAllBytes(Paths.get("/home/dima/Bureau/App/polySign/src/main/java/com/polytech/polysign/service/output/" + signOrder.getFile().getFilename() + userId + ".pdf"));
		signOrder.getFile().setFileBytesContentType("application/pdf");
    	signOrder.getFile().setFileBytes(array);
		signOrder.setSigned(true);
		signOrder.getFile().setSigningDate(Instant.now());
		signOrder.getFile().setFilename(signOrder.getFile().getFilename() + userId);
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
		X509Certificate cert = generateCertificate(POLYSIGN, pair, days, "SHA1withRSA"); 
	    KeyStore ks = KeyStore.getInstance("PKCS12", "BC");   
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
		Security.addProvider(new BouncyCastleProvider());
        try {/*ww  w.  j av  a2  s.co  m*/
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA","BC");
            keyGenerator.initialize(1024);
            return keyGenerator.genKeyPair();
        } catch (Exception ex) {
            return null;
        }
	}  
        private static char[] generatePassword(int length) {
            String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
            String specialCharacters = "!@#$?/";
            String numbers = "1234567890";
            String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
            Random random = new Random();
            char[] password = new char[length];

            password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
            password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
            password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
            password[3] = numbers.charAt(random.nextInt(numbers.length()));

            for (int i = 4; i < length; i++) {
                password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
            }
            return password;
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

