package com.polytech.polysign.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.polytech.polysign.web.rest.TestUtil;

public class SignOrderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignOrder.class);
        SignOrder signOrder1 = new SignOrder();
        signOrder1.setId(1L);
        SignOrder signOrder2 = new SignOrder();
        signOrder2.setId(signOrder1.getId());
        assertThat(signOrder1).isEqualTo(signOrder2);
        signOrder2.setId(2L);
        assertThat(signOrder1).isNotEqualTo(signOrder2);
        signOrder1.setId(null);
        assertThat(signOrder1).isNotEqualTo(signOrder2);
    }
}
