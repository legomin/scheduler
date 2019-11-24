package org.legomin.service;

import org.legomin.domain.Tenant;

/**
 * It's implementations could send messages to tenants by mail or push,
 * maybe even by sms or dove mail))
 */
public interface TenantNotifier {

  void notify(Tenant tenant, String message);

}
