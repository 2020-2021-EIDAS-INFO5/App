package com.polytech.polysign.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.polytech.polysign.web.rest.TestUtil;

public class SignatureProcessTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignatureProcess.class);
        SignatureProcess signatureProcess1 = new SignatureProcess();
        signatureProcess1.setId(1L);
        SignatureProcess signatureProcess2 = new SignatureProcess();
        signatureProcess2.setId(signatureProcess1.getId());
        assertThat(signatureProcess1).isEqualTo(signatureProcess2);
        signatureProcess2.setId(2L);
        assertThat(signatureProcess1).isNotEqualTo(signatureProcess2);
        signatureProcess1.setId(null);
        assertThat(signatureProcess1).isNotEqualTo(signatureProcess2);
    }
}
