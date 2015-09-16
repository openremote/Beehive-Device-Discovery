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

import org.openremote.beehive.discovery.model.rest.DeviceDiscoveryReader;
import org.openremote.model.DeviceDiscovery;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Beehive Device Discovery REST API for adding/removing/listing devices.
 *
 * @author Eric Bariaux
 */
@Path("/")
public class DeviceDiscoveryService {

    public static final String DEVICE_DISCOVERY_LIST_JSON_HTTP_CONTENT_TYPE = "application/vnd.openremote.device-discovery-list+json";

    @Context
    private SecurityContext security;

    @Context
    private ServletContext webapp;

    @GET
    @Produces(DEVICE_DISCOVERY_LIST_JSON_HTTP_CONTENT_TYPE)
    public Response list() {
        System.err.println("List discovery information...");

        // We explicitly forbid access by users with service-admin role (even if user has account-owner role)
        if (security.isUserInRole("service-admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        @SuppressWarnings("unchecked")
        Map<String, DeviceDiscovery> devices = (Map<String, DeviceDiscovery>) webapp.getAttribute("devicesMap");

        if (devices != null) {
            return Response.ok(DeviceDiscovery.toJSONString(new HashSet<>(devices.values()))).build();
        } else {
            return Response.ok(DeviceDiscovery.toJSONString(new HashSet<DeviceDiscovery>())).build();
        }
    }

    @Path("{deviceIdentifier}")
    @POST
    @Consumes(DeviceDiscoveryReader.JSON_HTTP_CONTENT_TYPE)
    public Response create(DeviceDiscovery discovery, @PathParam("deviceIdentifier") String deviceIdentifier) {
        System.err.println("Process and persist discovery...");

        // We explicitly forbid access by users with service-admin role (even if user has account-owner role)
        if (security.isUserInRole("service-admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        System.err.println(discovery.toJSONString());

        @SuppressWarnings("unchecked")
        Map<String, DeviceDiscovery> devices = (Map<String, DeviceDiscovery>) webapp.getAttribute("devicesMap");

        if (devices == null) {
            devices = new HashMap<>();
            webapp.setAttribute("devicesMap", devices);
        }

        if (devices.get(deviceIdentifier) != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        devices.put(deviceIdentifier, discovery);

        return Response.noContent().build();
    }

    @Path("{deviceIdentifier}")
    @DELETE
    public Response delete(@PathParam("deviceIdentifier") String deviceIdentifier) {
        System.err.println("Delete device discovery...");

        // We explicitly forbid access by users with service-admin role (even if user has account-owner role)
        if (security.isUserInRole("service-admin")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        @SuppressWarnings("unchecked")
        Map<String, DeviceDiscovery> devices = (Map<String, DeviceDiscovery>) webapp.getAttribute("devicesMap");

        if (devices == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (devices.get(deviceIdentifier) == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            devices.remove(deviceIdentifier);
        }

        return Response.ok().build();
    }

}
