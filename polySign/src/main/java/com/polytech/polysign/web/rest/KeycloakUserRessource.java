package com.polytech.polysign.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.polytech.polysign.domain.UserKeycloak;
import com.polytech.polysign.service.KeycloakAdminClientService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.spring.web.json.Json;

@RestController
@RequestMapping("/api")
public class KeycloakUserRessource {

     String  cmd = " --location --request POST 'http://localhost:9080/auth/realms/jhipster/protocol/openid-connect/token' --header 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'username=admin' --data-urlencode 'password=admin' --data-urlencode 'grant_type=password' --data-urlencode 'client_id=web_app'";

    @GetMapping("/getAccessToken")
    public void getAccessToken(){
        String[] command = {"/usr/bin/curl","location","--request","POST","http://localhost:9080/auth/realms/jhipster/protocol/openid-connect/token","--header","Content-Type: application/x-www-form-urlencoded","--data-urlencode","grant_type=client_credentials","--data-urlencode","client_id=web_app","--data-urlencode","grant_type=password","--data-urlencode","client_secret=4c4e94f6-68c5-434e-83c8-9d2fadf459bc"};

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

}


