package org.legomin.service;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;

/**
 * That is getting some scheduler changes as input and decides to whom and what is need to be sent
 * And maybe even prepares some sexy messages for our dear tenants)))
 */
public interface NotificationManager {
  void notify(Slot slot);

}
