package com.polytech.polysign.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.polytech.polysign.web.rest.TestUtil;

public class AuthenticatedUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthenticatedUser.class);
        AuthenticatedUser authenticatedUser1 = new AuthenticatedUser();
        authenticatedUser1.setId(1L);
        AuthenticatedUser authenticatedUser2 = new AuthenticatedUser();
        authenticatedUser2.setId(authenticatedUser1.getId());
        assertThat(authenticatedUser1).isEqualTo(authenticatedUser2);
        authenticatedUser2.setId(2L);
        assertThat(authenticatedUser1).isNotEqualTo(authenticatedUser2);
        authenticatedUser1.setId(null);
        assertThat(authenticatedUser1).isNotEqualTo(authenticatedUser2);
    }
}
