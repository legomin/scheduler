package org.legomin.service.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
  public void setUp() throws Exception {
    notificationManager = new DefaultNotificationManager(tenantNotifier);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(tenantNotifier);
  }

  @Test
  public void testReserved() {
    final Tenant currentTenant = new Tenant(0L, null);
    final Tenant reservedBy = new Tenant(1L, null);
    final Flat flat = new Flat(1, currentTenant);
    final Slot slot = new Slot(1, null, null, reservedBy, Slot.Status.RESERVED);

    notificationManager.notify(flat, slot);
    verify(tenantNotifier).notify(eq(currentTenant), Matchers.contains("reseved"));
  }

  @Test
  public void testCancelled() {
    final Tenant currentTenant = new Tenant(0L, null);
    final Flat flat = new Flat(1, currentTenant);
    final Slot slot = new Slot(1, null, null, null, Slot.Status.FREE);

    notificationManager.notify(flat, slot);
    verify(tenantNotifier).notify(eq(currentTenant), Matchers.contains("cancelled"));
  }

  @Test
  public void testApproved() {
    final Tenant currentTenant = new Tenant(0L, null);
    final Tenant reservedBy = new Tenant(1L, null);
    final Flat flat = new Flat(1, currentTenant);
    final Slot slot = new Slot(1, null, null, reservedBy, Slot.Status.APPROVED);

    notificationManager.notify(flat, slot);
    verify(tenantNotifier).notify(eq(reservedBy), Matchers.contains("approved"));
  }

  @Test
  public void testRejected() {
    final Tenant currentTenant = new Tenant(0L, null);
    final Tenant reservedBy = new Tenant(1L, null);
    final Flat flat = new Flat(1, currentTenant);
    final Slot slot = new Slot(1, null, null, reservedBy, Slot.Status.REJECTED);

    notificationManager.notify(flat, slot);
    verify(tenantNotifier).notify(eq(reservedBy), Matchers.contains("rejected"));
  }

  @Test
  public void testWrongInput() {
    final Tenant reservedBy = new Tenant(1L, null);
    final Slot slot = new Slot(1, null, null, reservedBy, Slot.Status.REJECTED);

    notificationManager.notify(null, slot);
  }

  //TODO add all wrong usecases


}