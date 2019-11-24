package org.legomin.domain;

import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

/**
 * Tenants representation class
 */
public class Tenant {
  private final Long id;

  private final transient ZoneId timeZone;

  public Tenant(Long id, ZoneId timeZone) {
    this.id = id;
    this.timeZone = Optional.ofNullable(timeZone).orElse(ZoneId.systemDefault());
  }

  public long getId() {
    return id;
  }

  public ZoneId getTimeZone() {
    return timeZone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tenant tenant = (Tenant) o;
    return getId() == tenant.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

}
