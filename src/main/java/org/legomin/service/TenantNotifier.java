package org.legomin.service;

import org.legomin.domain.Tenant;

public interface TenantNotifier {

  void notify(Tenant tenant, String message);

}
