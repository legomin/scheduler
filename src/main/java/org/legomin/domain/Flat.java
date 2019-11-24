package org.legomin.domain;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * class for flat representation
 *
 * In current implementation is need only to distinguish current tenant from others
 */
public class Flat {
  private final Long id;
  private final Tenant currentTenant;

  public Flat(Long id, Tenant currentTenant) {
    this.id = requireNonNull(id);
    this.currentTenant = currentTenant;
  }

  public Long getId() {
    return id;
  }

  public Tenant getCurrentTenant() {
    return currentTenant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Flat flat = (Flat) o;
    return getId() == flat.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
