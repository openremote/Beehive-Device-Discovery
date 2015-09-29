/*
 * OpenRemote, the Home of the Digital Home.
 * Copyright 2008-2014, OpenRemote Inc.
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openremote.beehive.discovery.model.persistence.jpa;

import org.openremote.model.DeviceDiscovery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * TODO
 *
 * @author <a href="mailto:juha@openremote.org">Juha Lindfors</a>
 */

@Entity
@Table(name = "discovered_device")

public class PersistentDeviceDiscovery extends DeviceDiscovery
{

  // Instance Fields ------------------------------------------------------------------------------

  /**
   * Auto-generated primary key for this persistent entity.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name="account_id")
  private String accountId;

  /**
   * Private constructor required by the ORM framework. Creates an instance in an uninitialized
   * state.
   */
  private PersistentDeviceDiscovery()
  {
    super(new DeviceDiscovery("<undefined>", "<undefined>", "<undefined>", ""));
  }

  /**
   * Creates a persistent instance from a given domain object.
   *
   * @param copy    domain object instance to persist.
   */
  public PersistentDeviceDiscovery(DeviceDiscovery copy)
  {
    super(copy);
  }

  public String getAccountId()
  {
    return accountId;
  }

  public void setAccountId(String accountId)
  {
    this.accountId = accountId;
  }

  @Override public String toString()
  {
    return "Persistent Device Discovery (ID = " + id + ")";
  }
}

