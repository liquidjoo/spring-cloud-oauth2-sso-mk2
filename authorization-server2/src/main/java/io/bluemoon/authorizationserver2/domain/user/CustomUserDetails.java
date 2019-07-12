package io.bluemoon.authorizationserver2.domain.user;

import io.bluemoon.authorizationserver2.domain.oauth.OAuthUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 6396079419309274853L;
    private Integer id;
    private String username;
    private String password;
    private List<String> userRole;

    public CustomUserDetails(OAuthUser user, List<String> userRoles) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.userRole = userRoles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role: userRole) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
