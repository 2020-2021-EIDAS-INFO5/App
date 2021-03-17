package com.polytech.polysign.service;

import com.polytech.polysign.config.Constants;
import com.polytech.polysign.config.KeycloakConfig;
import com.polytech.polysign.domain.UserKeycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.polytech.polysign.config.Constants.*;

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
        credentialRepresentation.setTemporary(true);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getPassword());
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

        addRealmRoleToUser(user.getEmail(), "ROLE_USER");

        mailService.sendNotificationEmail(user);

        return kcUser;

    }


    public UserRepresentation addUserSignature(UserKeycloak user) {

        UsersResource usersResource = KeycloakConfig.getInstance().realm(Constants.realm).users();
        //CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());
        CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
        credentialRepresentation.setTemporary(true);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getPassword());
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

        addRealmRoleToUser(user.getEmail(), "ROLE_USER");

        return kcUser;

    }


    public void addRealmRoleToUser(String userName, String role_name){
        Keycloak keycloak = KeycloakConfig.getInstance();

        String client_id = keycloak
                          .realm(Constants.realm)
                          .clients()
                          .findByClientId(Constants.clientId)
                          .get(0)
                          .getId();

         String userId = keycloak
                      .realm(Constants.realm)
                      .users()
                      .search(userName)
                      .get(0)
                      .getId();


        System.out.println(client_id);

        System.out.println(userId);


         UserResource user = keycloak
                            .realm(Constants.realm)
                            .users()
                            .get(userId);

        List<RoleRepresentation> roleToAdd = new LinkedList<RoleRepresentation>();

         roleToAdd.add(keycloak
                      .realm(Constants.realm)
                      .roles()
                      .get(role_name)
                      .toRepresentation()
                     );
        

        user.roles().realmLevel().add(roleToAdd);

       }
  

}
