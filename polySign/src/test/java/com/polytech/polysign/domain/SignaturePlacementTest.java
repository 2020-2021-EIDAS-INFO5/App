package com.polytech.polysign.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.polytech.polysign.web.rest.TestUtil;

public class SignaturePlacementTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignaturePlacement.class);
        SignaturePlacement signaturePlacement1 = new SignaturePlacement();
        signaturePlacement1.setId(1L);
        SignaturePlacement signaturePlacement2 = new SignaturePlacement();
        signaturePlacement2.setId(signaturePlacement1.getId());
        assertThat(signaturePlacement1).isEqualTo(signaturePlacement2);
        signaturePlacement2.setId(2L);
        assertThat(signaturePlacement1).isNotEqualTo(signaturePlacement2);
        signaturePlacement1.setId(null);
        assertThat(signaturePlacement1).isNotEqualTo(signaturePlacement2);
    }
}
