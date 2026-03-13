package br.com.gradehorarios.api.auth.domain.service;

public interface OAuthProviderService {
    OAuthUserInfo getUserInfo(String token) throws Exception;
    String getProviderName();
}
