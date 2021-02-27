package com.polytech.polysign.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.polytech.polysign.web.rest.TestUtil;

public class SignedFileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignedFile.class);
        SignedFile signedFile1 = new SignedFile();
        signedFile1.setId(1L);
        SignedFile signedFile2 = new SignedFile();
        signedFile2.setId(signedFile1.getId());
        assertThat(signedFile1).isEqualTo(signedFile2);
        signedFile2.setId(2L);
        assertThat(signedFile1).isNotEqualTo(signedFile2);
        signedFile1.setId(null);
        assertThat(signedFile1).isNotEqualTo(signedFile2);
    }
}
