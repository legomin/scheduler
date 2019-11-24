package org.legomin.service.impl;

import static java.util.Objects.requireNonNull;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;
import org.legomin.service.NotificationManager;
import org.legomin.service.TenantNotifier;

public class DefaultNotificationManager implements NotificationManager {

  private final TenantNotifier notifier;

  public DefaultNotificationManager(TenantNotifier notifier) {
    this.notifier = requireNonNull(notifier);
  }

  @Override
  public void notify(final Flat flat, final Slot slot) {

    if (slot == null || flat == null) {
      //TODO log error
      return;
    }
    final Slot.Status status = slot.getStatus();
    final Tenant currentTenant = flat.getCurrentTenant();

    String message = null;
    Tenant tenantToNotify = null;
    switch (status) {
      case RESERVED:
        message = String.format("Dear %s, The new reservation is appeared. details: \n" +
          "start date: %s\n end date: %s\n reseved by %s\n Regards,\n The Team of the best service in the World",
          formatTenant(currentTenant),
          slot.getStartDate(),
          slot.getFinishDate(),
          formatTenant(slot.getReservedBy()));
        tenantToNotify = currentTenant;
        break;
      case FREE:
        message = String.format("Dear %s, The reservation has been cancelled. Reservation details: \n" +
          "start date: %s\n end date: %s\n cancelled by %s\n Regards,\n The Team of the best service in the World",
          formatTenant(currentTenant),
          slot.getStartDate(),
          slot.getFinishDate(),
          formatTenant(slot.getReservedBy()));
        tenantToNotify = currentTenant;
        break;
      case APPROVED:
        tenantToNotify = slot.getReservedBy();
        message = String.format("Dear %s, The your reservation has been approved. Reservation details: \n" +
            "start date: %s\n end date: %s\n approved by %s\n Regards,\n The Team of the best service in the World",
          formatTenant(tenantToNotify),
          slot.getStartDate(),
          slot.getFinishDate(),
          formatTenant(currentTenant));
        break;
      case REJECTED:
        tenantToNotify = slot.getReservedBy();
        message = String.format("Dear %s, The your reservation has been rejected. Reservation details: \n" +
            "start date: %s\n end date: %s\n approved by %s\n Regards,\n The Team of the best service in the World",
          formatTenant(tenantToNotify),
          slot.getStartDate(),
          slot.getFinishDate(),
          formatTenant(currentTenant));
    }
    if (tenantToNotify != null && message != null) {
      notifier.notify(tenantToNotify, message);
    } else {
      //TODO log error here
    }

  }

  private String formatTenant(final Tenant tenant) {
    return tenant == null ? "" : String.valueOf(tenant.getId());
  }
}
