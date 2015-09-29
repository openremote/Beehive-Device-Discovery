/*
 * OpenRemote, the Home of the Digital Home.
 * Copyright 2008-2015, OpenRemote Inc.
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
package org.openremote.beehive.discovery.service;

import org.openremote.beehive.EntityTransactionFilter;
import org.openremote.beehive.discovery.model.persistence.jpa.MinimalPersistentUser;
import org.openremote.beehive.discovery.model.persistence.jpa.PersistentDeviceDiscovery;
import org.openremote.beehive.discovery.model.rest.DeviceDiscoveryReader;
import org.openremote.model.DeviceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Beehive Device Discovery REST API for adding/removing/listing devices.
 *
 * @author Eric Bariaux
 */
@Path("/")
public class DeviceDiscoveryService {

  private static final Logger LOG = LoggerFactory.getLogger(DeviceDiscoveryService.class);

  public static final String DEVICE_DISCOVERY_LIST_JSON_HTTP_CONTENT_TYPE = "application/vnd.openremote.device-discovery-list+json";

  @Context
  private SecurityContext security;

  @Context
  private ServletContext webapp;

  @GET
  @Produces(DEVICE_DISCOVERY_LIST_JSON_HTTP_CONTENT_TYPE)
  public Response list(@Context HttpServletRequest request) {
    LOG.info("### List discovery information...");

    // We explicitly forbid access by users with service-admin role (even if user has account-owner role)
    if (security.isUserInRole("service-admin")) {
      return Response.status(Response.Status.FORBIDDEN).build();
    }

    EntityManager entityManager = getEntityManager(request);

    String accountId = getAccountId(entityManager, security.getUserPrincipal().getName());

    Set<DeviceDiscovery> devices = getDeviceDiscoveryList(entityManager, accountId);

    if (devices != null) {
      return Response.ok(DeviceDiscovery.toJSONString(devices)).build();
    } else {
      return Response.ok(DeviceDiscovery.toJSONString(new HashSet<DeviceDiscovery>())).build();
    }
  }

  @Path("{deviceIdentifier}")
  @POST
  @Consumes(DeviceDiscoveryReader.JSON_HTTP_CONTENT_TYPE)
  public Response create(@Context HttpServletRequest request, DeviceDiscovery discovery, @PathParam("deviceIdentifier") String deviceIdentifier) {
    LOG.info("### Process and persist discovery...");

    // We explicitly forbid access by users with service-admin role (even if user has account-owner role)
    if (security.isUserInRole("service-admin")) {
      return Response.status(Response.Status.FORBIDDEN).build();
    }

    LOG.info("### " + discovery.toJSONString());

    EntityManager entityManager = getEntityManager(request);
    String accountId = getAccountId(entityManager, security.getUserPrincipal().getName());

    DeviceDiscovery deviceDiscovery = getDeviceDiscovery(entityManager, accountId, deviceIdentifier);

    if (deviceDiscovery != null) {
      return Response.status(Response.Status.CONFLICT).build();
    }

    PersistentDeviceDiscovery persistentDeviceDiscovery = new PersistentDeviceDiscovery(discovery);
    persistentDeviceDiscovery.setAccountId(accountId);
    getEntityManager(request).persist(persistentDeviceDiscovery);

    return Response.noContent().build();
  }

  @Path("{deviceIdentifier}")
  @DELETE
  public Response delete(@Context HttpServletRequest request, @PathParam("deviceIdentifier") String deviceIdentifier) {
    LOG.info("### Delete device discovery...");

    // We explicitly forbid access by users with service-admin role (even if user has account-owner role)
    if (security.isUserInRole("service-admin")) {
      return Response.status(Response.Status.FORBIDDEN).build();
    }

    EntityManager entityManager = getEntityManager(request);

    String accountId = getAccountId(entityManager, security.getUserPrincipal().getName());

    DeviceDiscovery deviceDiscovery = getDeviceDiscovery(entityManager, accountId, deviceIdentifier);

    if (deviceDiscovery == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    getEntityManager(request).remove(deviceDiscovery);
    return Response.ok().build();
  }

  private EntityManager getEntityManager(HttpServletRequest request)
  {
    return (EntityManager)request.getAttribute(EntityTransactionFilter.PERSISTENCE_ENTITY_MANAGER_LOOKUP);
  }

  private String getAccountId(EntityManager entityManager, String userName)
  {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MinimalPersistentUser> userQuery = criteriaBuilder.createQuery(MinimalPersistentUser.class);
    Root<MinimalPersistentUser> userRoot = userQuery.from(MinimalPersistentUser.class);
    userQuery.select(userRoot);
    userQuery.where(criteriaBuilder.equal(userRoot.get("username"), userName));

    MinimalPersistentUser user = entityManager.createQuery(userQuery).getSingleResult();
    return Long.toString(user.getAccountId());
  }

  private Set<DeviceDiscovery> getDeviceDiscoveryList(EntityManager entityManager, String accountId)
  {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PersistentDeviceDiscovery> deviceDiscoveryQuery = criteriaBuilder.createQuery(PersistentDeviceDiscovery.class);
    Root<PersistentDeviceDiscovery> deviceDiscoveryRoot = deviceDiscoveryQuery.from(PersistentDeviceDiscovery.class);
    deviceDiscoveryQuery.select(deviceDiscoveryRoot);
    deviceDiscoveryQuery.where(criteriaBuilder.equal(deviceDiscoveryRoot.get("accountId"), accountId));
    List<PersistentDeviceDiscovery> deviceDiscoveryList = entityManager.createQuery(deviceDiscoveryQuery).getResultList();

    return new HashSet<DeviceDiscovery>(deviceDiscoveryList);
  }

  private DeviceDiscovery getDeviceDiscovery(EntityManager entityManager, String accountId, String deviceIdentifier)
  {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PersistentDeviceDiscovery> deviceDiscoveryQuery = criteriaBuilder.createQuery(PersistentDeviceDiscovery.class);
    Root<PersistentDeviceDiscovery> deviceDiscoveryRoot = deviceDiscoveryQuery.from(PersistentDeviceDiscovery.class);
    deviceDiscoveryQuery.select(deviceDiscoveryRoot);
    deviceDiscoveryQuery.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(deviceDiscoveryRoot.get("accountId"), accountId),
        criteriaBuilder.equal(deviceDiscoveryRoot.get("deviceIdentifier"), deviceIdentifier)));

    try
    {
      return entityManager.createQuery(deviceDiscoveryQuery).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}