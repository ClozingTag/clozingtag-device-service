package com.clozingtag.clozingtag.device.service.custom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomRoleEntity implements GrantedAuthority {
  private Long id;

  private String name;

  private String description;

  @Override
  public String getAuthority() {
    return name;
  }
}
