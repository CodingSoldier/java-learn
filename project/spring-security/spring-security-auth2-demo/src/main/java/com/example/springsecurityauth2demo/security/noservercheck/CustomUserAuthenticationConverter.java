package com.example.springsecurityauth2demo.security.noservercheck;

import com.example.springsecurityauth2demo.security.SSOUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-30
 */
public class CustomUserAuthenticationConverter implements UserAuthenticationConverter {
    private static final String USER_MOBILE = "userMobile";
    private static final String N_A = "N/A";

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);

            String username = (String) map.get(USERNAME);
            String userMobile = (String) map.get(USER_MOBILE);
            SSOUserDetails user = new SSOUserDetails();
            user.setUsername(username);
            user.setUserMobile(userMobile);
            return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
        }
        return null;
    }

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication){
        return new HashMap<>();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
