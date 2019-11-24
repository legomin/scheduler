package org.legomin.service.impl;

import org.legomin.domain.Tenant;
import org.legomin.service.TenantNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ooops, wassup, it's empty)) Couldn't send anything
 */
public class MockTenantNotifier implements TenantNotifier {
  private static final Logger log = LoggerFactory.getLogger(MockTenantNotifier.class);

  @Override
  public void notify(Tenant tenant, String message) {
    log.info("Tenant {} has been notified with {}", tenant, message);
  }
}
