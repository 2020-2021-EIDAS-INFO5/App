package com.polytech.polysign.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.polytech.polysign.config.KeycloakConfig;
import com.polytech.polysign.domain.UserKeycloak;
import com.polytech.polysign.service.KeycloakAdminClientService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.polytech.polysign.config.Constants.*;

@RestController
@RequestMapping("/api")
public class KeycloakUserRessource {


    @Autowired
    KeycloakAdminClientService kcAdminClient;

     String  cmd = " --location --request POST 'http://localhost:9080/auth/realms/jhipster/protocol/openid-connect/token' --header 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'username=admin' --data-urlencode 'password=admin' --data-urlencode 'grant_type=password' --data-urlencode 'client_id=web_app'";

    @GetMapping("/getAccessToken")
    public void getAccessToken(){
        String[] command = {"/usr/bin/curl","location","--request","POST","http://localhost:9080/auth/realms/jhipster/protocol/openid-connect/token","--header","Content-Type: application/x-www-form-urlencoded","--data-urlencode","grant_type=client_credentials","--data-urlencode","client_id=web_app","--data-urlencode","grant_type=password","--data-urlencode","client_secret=f6284326-017f-45d6-8f1d-696af00e83da"};

        ProcessBuilder process = new ProcessBuilder(command);
        Process p;
        try
        {
            p = process.start();
             BufferedReader reader =  new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ( (line = reader.readLine()) != null) {
                        builder.append(line);
                        builder.append(System.getProperty("line.separator"));
                }

                String result = builder.toString();
                String access_token = (result.substring(result.lastIndexOf("{") + -1));

                System.out.println(result);
                try {
                    JSONObject jsonObject = new JSONObject(access_token);
                    System.out.print(jsonObject.getString("access_token"));

               }catch (JSONException err){
               }
        }
        catch (IOException e)
        {   System.out.print("error");
            e.printStackTrace();
        }
    }

 /*   @GetMapping("/createKeycloakUser")
    public void createKeycloakUser(){

        String command = "curl -v http://localhost:9080/auth/admin/realms/jhipster/users -H "Content-Type: application/json" -H "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJTWTNLbksxTDdMUU9hZVZTYjZEQkVFOFNKWE5wX1V3b1E0ekVTdGJqMzVvIn0.eyJleHAiOjE2MTQ5NTY0MTYsImlhdCI6MTYxNDk1NjExNiwianRpIjoiNGY2ZDljYTMtYTA1OS00YjQyLWE5MzgtNGUwYTc3YmU1YWMwIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDgwL2F1dGgvcmVhbG1zL2poaXBzdGVyIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6ImVmZWI4NjZlLWMwOGMtNDA5ZS04NzVmLTJkNjRkNmViNzYxMSIsInR5cCI6IkJlYXJlciIsImF6cCI6IndlYl9hcHAiLCJzZXNzaW9uX3N0YXRlIjoiYWEwODU4ZTAtZjA4Zi00ODRkLTgwNjMtNzA3ZmU1OTczMGM1IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LXJlYWxtIiwidmlldy1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJ3ZWJfYXBwIjp7InJvbGVzIjpbInVtYV9wcm90ZWN0aW9uIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImpoaXBzdGVyIGVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoid2ViX2FwcCIsImNsaWVudEhvc3QiOiIxNzIuMTguMC4xIiwicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtd2ViX2FwcCIsImNsaWVudEFkZHJlc3MiOiIxNzIuMTguMC4xIn0.BmB1lZKqzL4jvzLT75Sbr55t0N1-GMmxjo8VJrDjB__xtpxGaXPS10V2dF_q_L0jq_L9bB7DehOoSZGM8Ie9SDBlEfZctcTHTwvB0Wqv0cxuJNJhfjzlk9JIPQezQvZII81m_sFdL8ij_wd1xOoYO13gTK365yKBvao5eFoFYpI1o6sowzCi5b3osDKjEfWzCpcugu5K2K-HSE8PLNnyzdFF0riTpe-nUSJU_A2V-gNhSfig4zlyr0lIPqJ7Cg5T595kKNvcfpklmm_oDM3S2FyQ4hcG7suGCPeAh9pUF29d5YNxrvFQi-bqoG-HAAv02rP-5h1LT_mI3QTSLcwZmQ"   --data '{"firstName":"otba","lastName":"otba", "username":"bato","email":"bato@gmail.com", "enabled":"true","credentials":[{"type":"password","value":"test123","temporary":true}]}'";

        String access_token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJTWTNLbksxTDdMUU9hZVZTYjZEQkVFOFNKWE5wX1V3b1E0ekVTdGJqMzVvIn0.eyJleHAiOjE2MTQ5NjI2ODIsImlhdCI6MTYxNDk2MjM4MiwianRpIjoiNTBjM2FkZTUtN2EzNy00ZGViLWEzMmUtMjZiNmE2YjAzZWIyIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDgwL2F1dGgvcmVhbG1zL2poaXBzdGVyIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6ImVmZWI4NjZlLWMwOGMtNDA5ZS04NzVmLTJkNjRkNmViNzYxMSIsInR5cCI6IkJlYXJlciIsImF6cCI6IndlYl9hcHAiLCJzZXNzaW9uX3N0YXRlIjoiOTIwYjgxYTItMjZkNy00N2QwLTkyNDUtOGM1NTdiYjcyNjA1IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LXJlYWxtIiwidmlldy1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJ3ZWJfYXBwIjp7InJvbGVzIjpbInVtYV9wcm90ZWN0aW9uIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImpoaXBzdGVyIGVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoid2ViX2FwcCIsImNsaWVudEhvc3QiOiIxNzIuMTguMC4xIiwicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtd2ViX2FwcCIsImNsaWVudEFkZHJlc3MiOiIxNzIuMTguMC4xIn0.PZh9iDasnbZPslV5TpmCJ_bG-P6tAXLuaw1r1-FwQMmUjDCJn1OvW6alBf_qPt6wtIxruzxuZQdJMZukQ6pKL0c5GbML0XtLA8sL9o3AnbVgRSV3zF_-5MkosLtMKDAUm2ryNCUsutScbHLNWRG73Esdh2Y8g-13Xt676wBQoGGBgHkApprVcDmoHYSa7n14lHGgd5Fq-n17pVq-kPqb8rZm1pq8698ekw9oErIvohl5gMlDn1RKIAHoddeuvRLDvcmQteIXn6-M41Jsa1Ysq6YcbNIzdQLGRFd6Z8_o5Rjd6y_oXK3NdhqGLIiAcr3W-PIdgv1xUZzE_PpoHth-CQ";
        String firstname = "aleck";
        String lastName = "bilounga";
        String username = "aleck";
        String email = "aleck@gmail.com";
        Boolean activated = true;
        String type = "password";
        String password = "1234";
        //String[] command = {"/usr/bin/curl","-v","http://localhost:9080/auth/admin/realms/jhipster/users","-H", "Content-Type: application/json", "-H", "Authorization: bearer "+access_token, "--data", "'{\"firstName\":"+"\""+firstname+"\""+","+"\"lastName\":"+"\""+lastName+"\""+","+"\"username\":"+"\""+username+"\""+","+"\"email\":"+"\""+email+"\""+","+"\"enabled\":"+"\""+activated+"\""+","+"[{"+"\"password\":"+"\""+type+"\""+","+"\"value\":"+"\""+password+"\"";

       /* ProcessBuilder process = new ProcessBuilder(command);
        Process p;
        try
        {
            p = process.start();
             BufferedReader reader =  new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ( (line = reader.readLine()) != null) {
                        builder.append(line);
                        builder.append(System.getProperty("line.separator"));
                }

                String result = builder.toString();

                System.out.println(result);

        }
        catch (IOException e)
        {   System.out.print("error");
            e.printStackTrace();
        }


    }*/

    @GetMapping("/login")
        public String login() {
            String authServerUrl = "http://localhost:9080/auth"; // Your keycloak auth entpoint
            String realm = "jhipster"; // Realm
            String clientId = "web_app"; // Client
            Map<String, Object> clientCredentials = new LinkedHashMap<String, Object>();
            clientCredentials.put("secret", "f6284326-017f-45d6-8f1d-696af00e83da"); // Client secret (Access Type: Confidential)

            Configuration configuration = new Configuration(
                authServerUrl,
                realm,
                clientId,
                clientCredentials,
                null
            );

            AuthzClient authzClient = AuthzClient.create(configuration);

            AuthorizationRequest request = new AuthorizationRequest();
            request.setScope("offline_access");

            AuthorizationResponse response = authzClient.authorization("admin", "admin").authorize(request);

            System.out.println(response.getRefreshToken());
         return response.getRefreshToken(); // response.getToken() returns the bearer token
        }

    @PostMapping("/createKeycloakUser")
    public UserRepresentation createUser(@RequestBody UserKeycloak user) {
            return kcAdminClient.addUser(user);
        }



    @GetMapping("/assignRoleToUser")
    public void addRealmRoleToUser(String userName, String role_name){
        Keycloak keycloak = KeycloakConfig.getInstance();

        String client_id = keycloak
                          .realm(realm)
                          .clients()
                          .findByClientId(clientId)
                          .get(0)
                          .getId();

         String userId = keycloak
                      .realm(realm)
                      .users()
                      .search(userName)
                      .get(0)
                      .getId();


        System.out.println(client_id);

        System.out.println(userId);


         UserResource user = keycloak
                            .realm(realm)
                            .users()
                            .get(userId);

        List<RoleRepresentation> roleToAdd = new LinkedList<RoleRepresentation>();

         roleToAdd.add(keycloak
                      .realm(realm)
                      .roles()
                      .get(role_name)
                      .toRepresentation()
                     );
        

        user.roles().realmLevel().add(roleToAdd);

       }



       @GetMapping("/deleteUser")
       public void deleteUser(String userName)  {
      
          Keycloak keycloak = KeycloakConfig.getInstance();
      
          String userId = keycloak
          .realm(realm)
          .users()
          .search(userName)
          .get(0)
          .getId();
      
          keycloak.realm(realm).users().delete(userId);
      }

      @GetMapping("/removeRoleOfUser")
      public void removeRoleOfUser(String userName, String role_name)  {
        Keycloak keycloak = KeycloakConfig.getInstance();

        String client_id = keycloak
                          .realm(realm)
                          .clients()
                          .findByClientId(clientId)
                          .get(0)
                          .getId();

         String userId = keycloak
                      .realm(realm)
                      .users()
                      .search(userName)
                      .get(0)
                      .getId();


        System.out.println(client_id);

        System.out.println(userId);


         UserResource user = keycloak
                            .realm(realm)
                            .users()
                            .get(userId);

        List<RoleRepresentation> roleToAdd = new LinkedList<RoleRepresentation>();

         roleToAdd.add(keycloak
                      .realm(realm)
                      .roles()
                      .get(role_name)
                      .toRepresentation()
                     );

        user.roles().realmLevel().remove(roleToAdd);
        
     }
       
}


