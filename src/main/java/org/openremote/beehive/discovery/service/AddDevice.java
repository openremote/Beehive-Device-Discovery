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
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.Map;


/**
 * Beehive Device Discovery REST API for adding new devices.
 *
 * @author Eric Bariaux
 */

@Path("/devicediscovery/{deviceIdentifier}")

public class AddDevice
{

  // Instance Fields ------------------------------------------------------------------------------

  /**
   * Security context associated with the incoming HTTP request provided by the host HTTP servlet
   * service.
   */
  @Context private SecurityContext security;

  @Context private ServletContext webapp;


  // REST API Implementation ----------------------------------------------------------------------

  @Consumes(DeviceDiscoveryReader.JSON_HTTP_CONTENT_TYPE)

  @POST public Response create(DeviceDiscovery discovery, @PathParam("deviceIdentifier") String deviceIdentifier)
  {
    System.err.println("Process and persist discovery...");

    System.err.println(discovery.toJSONString());

    Map<String, DeviceDiscovery> devices = (Map<String, DeviceDiscovery>) webapp.getAttribute("devicesMap");
    if (devices == null) {
      devices = new HashMap<String, DeviceDiscovery>();
      webapp.setAttribute("devicesMap", devices);
    }

    if (devices.get(deviceIdentifier) != null) {
      return Response.status(Response.Status.CONFLICT).build();
    }

    devices.put(deviceIdentifier, discovery);

    return Response.noContent().build();
  }

}
