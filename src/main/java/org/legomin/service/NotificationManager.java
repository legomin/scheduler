package org.legomin.service;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;

public interface NotificationManager {
  void notify(Flat flat, Slot slot);

}
