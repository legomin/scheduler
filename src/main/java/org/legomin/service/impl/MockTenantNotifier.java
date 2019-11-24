package org.legomin.service.impl;

import org.legomin.domain.Tenant;
import org.legomin.service.TenantNotifier;

/**
 * Ooops, it's empty)) Couldn't send anything
 */
public class MockTenantNotifier implements TenantNotifier {

  @Override
  public void notify(Tenant tenant, String message) {
    //TODO log.info() here
  }
}
