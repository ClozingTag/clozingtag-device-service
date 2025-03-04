package com.clozingtag.clozingtag.device.service.custom;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
public class CustomUserEntity implements UserDetails {

  private Long id;

  private String username;

  private String lastname;

  private String firstname;

  private String password;

  @Builder.Default
  private boolean enabled = true;

  @Builder.Default
  private boolean accountNonLocked = true;

  @Builder.Default
  private boolean accountNonExpired = true;

  @Builder.Default
  private boolean credentialsNonExpired = true;


  private Collection<CustomRoleEntity> roles;

  private String name;

  public String getName() {
    return String.format("%s %s", getLastname(), getFirstname());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles().stream().map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getName())).toList();
  }


}
