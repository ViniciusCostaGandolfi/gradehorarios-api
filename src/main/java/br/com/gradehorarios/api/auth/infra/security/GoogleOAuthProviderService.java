package br.com.gradehorarios.api.auth.infra.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import br.com.gradehorarios.api.auth.domain.service.OAuthProviderService;
import br.com.gradehorarios.api.auth.domain.service.OAuthUserInfo;

@Service
public class GoogleOAuthProviderService implements OAuthProviderService {

    @Value("${google.api.client}")
    private String googleClientId;

    @Override
    public OAuthUserInfo getUserInfo(String token) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(this.googleClientId))
                .build();
        GoogleIdToken idToken = verifier.verify(token);
        
        if (idToken == null) {
            throw new IllegalArgumentException("Token do Google inválido.");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        return new OAuthUserInfo(email, name);
    }

    @Override
    public String getProviderName() {
        return "google";
    }
}
