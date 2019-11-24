package org.legomin.domain;

public class Flat {
  private final long id;
  private final Tenant currentTenant;

  public Flat(long id, Tenant currentTenant) {
    this.id = id;
    this.currentTenant = currentTenant;
  }

  public long getId() {
    return id;
  }

  public Tenant getCurrentTenant() {
    return currentTenant;
  }
}
