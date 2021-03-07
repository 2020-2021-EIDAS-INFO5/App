package com.polytech.polysign.service;

import com.polytech.polysign.config.Constants;
import com.polytech.polysign.config.KeycloakConfig;
import com.polytech.polysign.domain.UserKeycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class KeycloakAdminClientService {

    private final MailService mailService;

    public KeycloakAdminClientService(MailService mailService) {
        this.mailService = mailService;
    }


    public UserRepresentation addUser(UserKeycloak user) {
        UsersResource usersResource = KeycloakConfig.getInstance().realm(Constants.realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstName());
        kcUser.setLastName(user.getLastName());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        usersResource.create(kcUser);


        return kcUser;
    }
    private static CredentialRepresentation  createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(true);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public UserRepresentation addUserTest(UserKeycloak user) {

        UsersResource usersResource = KeycloakConfig.getInstance().realm(Constants.realm).users();
        //CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());
        CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(true);
        credentialRepresentation.setValue("1234");
        UserRepresentation kcUser = new UserRepresentation();
        List<String> requiredAction= new ArrayList<String>();
        requiredAction.add("CONFIGURE_TOTP");
        requiredAction.add("VERIFY_EMAIL");
        kcUser.setRequiredActions(requiredAction);
        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstName());
        kcUser.setLastName(user.getLastName());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        usersResource.create(kcUser);

        mailService.sendNotificationEmail(user);

        return kcUser;

    }


    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }


}
