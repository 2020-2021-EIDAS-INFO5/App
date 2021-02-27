package com.polytech.polysign.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.polytech.polysign.web.rest.TestUtil;

public class AuthTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Auth.class);
        Auth auth1 = new Auth();
        auth1.setId(1L);
        Auth auth2 = new Auth();
        auth2.setId(auth1.getId());
        assertThat(auth1).isEqualTo(auth2);
        auth2.setId(2L);
        assertThat(auth1).isNotEqualTo(auth2);
        auth1.setId(null);
        assertThat(auth1).isNotEqualTo(auth2);
    }
}
