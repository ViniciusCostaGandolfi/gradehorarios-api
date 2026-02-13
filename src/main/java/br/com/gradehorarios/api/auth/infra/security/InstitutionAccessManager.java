package br.com.gradehorarios.api.auth.infra.security;


import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import br.com.gradehorarios.api.auth.domain.model.RoleName;


@Component
public class InstitutionAccessManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals(RoleName.ROLE_ADMIN.toString()));

        if (isAdmin) {
            return new AuthorizationDecision(true);
        }

        String institutionIdStr = context.getVariables().get("institutionId");
        
        if (institutionIdStr == null) {
            return new AuthorizationDecision(false);
        }

        String requiredAuthManager = "INSTITUTION_" + institutionIdStr + "_MANAGER";
        String requiredAuthOwner = "INSTITUTION_" + institutionIdStr + "_OWNER";

        boolean hasPermission = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(a -> a.equals(requiredAuthManager) || a.equals(requiredAuthOwner));

        return new AuthorizationDecision(hasPermission);
    }
}
