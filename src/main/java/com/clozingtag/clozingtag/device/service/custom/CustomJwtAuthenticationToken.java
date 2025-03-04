package com.clozingtag.clozingtag.device.service.custom;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class CustomJwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

  private final String name;

  public CustomJwtAuthenticationToken(
      Jwt jwt,
      Collection<? extends GrantedAuthority> authorities,
      Function<? super Jwt, CustomUserEntity> userFromJwtFunc) {
    super(jwt, userFromJwtFunc.apply(jwt), jwt, authorities);
    setAuthenticated(true);
    name = jwt.getTokenValue();
  }

  //    public CustomJwtAuthenticationToken(Jwt source, Collection<GrantedAuthority> authorities,
  // Function<? super Jwt, CustomUserEntity> userFromJwtFunc) {
  //        super(source, authorities);
  //        tokenValue = source.getTokenValue();
  //    }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Map<String, Object> getTokenAttributes() {
    return getToken().getClaims();
  }
}
