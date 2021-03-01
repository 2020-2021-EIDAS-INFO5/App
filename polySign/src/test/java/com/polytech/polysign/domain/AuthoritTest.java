package com.polytech.polysign.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.polytech.polysign.web.rest.TestUtil;

public class AuthoritTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authorit.class);
        Authorit authorit1 = new Authorit();
        authorit1.setId(1L);
        Authorit authorit2 = new Authorit();
        authorit2.setId(authorit1.getId());
        assertThat(authorit1).isEqualTo(authorit2);
        authorit2.setId(2L);
        assertThat(authorit1).isNotEqualTo(authorit2);
        authorit1.setId(null);
        assertThat(authorit1).isNotEqualTo(authorit2);
    }
}
