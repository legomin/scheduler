package org.legomin.service.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.Instant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;
import org.legomin.service.NotificationManager;
import org.legomin.service.TenantNotifier;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultNotificationManagerTest {

  @Mock
  private TenantNotifier tenantNotifier;

  private NotificationManager notificationManager;

  @Before
  public void setUp() {
    notificationManager = new DefaultNotificationManager(tenantNotifier);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(tenantNotifier);
  }

  @Test
  public void testReserved() {
    final Tenant currentTenant = new Tenant(0L, null);
    final Tenant reservedBy = new Tenant(1L, null);
    final Flat flat = new Flat(1, currentTenant);
    final Slot slot = new Slot(1L, flat, Instant.now(), Instant.now(), reservedBy, Slot.Status.RESERVED);

    notificationManager.notify(slot);
    verify(tenantNotifier).notify(eq(currentTenant), Matchers.contains("reseved"));
  }

  @Test
  public void testCancelled() {
    final Tenant currentTenant = new Tenant(0L, null);
    final Flat flat = new Flat(1, currentTenant);
    final Slot slot = new Slot(1L, flat, Instant.now(), Instant.now(), null, Slot.Status.FREE);

    notificationManager.notify(slot);
    verify(tenantNotifier).notify(eq(currentTenant), Matchers.contains("cancelled"));
  }

  @Test
  public void testApproved() {
    final Tenant currentTenant = new Tenant(0L, null);
    final Tenant reservedBy = new Tenant(1L, null);
    final Flat flat = new Flat(1, currentTenant);
    final Slot slot = new Slot(1L, flat, Instant.now(), Instant.now(), reservedBy, Slot.Status.APPROVED);

    notificationManager.notify(slot);
    verify(tenantNotifier).notify(eq(reservedBy), Matchers.contains("approved"));
  }

  @Test
  public void testRejected() {
    final Tenant currentTenant = new Tenant(0L, null);
    final Tenant reservedBy = new Tenant(1L, null);
    final Flat flat = new Flat(1, currentTenant);
    final Slot slot = new Slot(1L, flat, Instant.now(), Instant.now(), reservedBy, Slot.Status.REJECTED);

    notificationManager.notify(slot);
    verify(tenantNotifier).notify(eq(reservedBy), Matchers.contains("rejected"));
  }

  @Test
  public void testWrongInput() {
    final Slot slot = new Slot(1L, new Flat(1L, null), Instant.now(), Instant.now(), null, Slot.Status.REJECTED);

    notificationManager.notify(slot);
  }

  //TODO add all wrong usecases


}