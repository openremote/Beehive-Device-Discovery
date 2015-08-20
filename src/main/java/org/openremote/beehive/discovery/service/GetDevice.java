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

import org.openremote.model.DeviceDiscovery;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashSet;
import java.util.Map;


/**
 * Beehive Device Discovery REST API for listing devices.
 *
 * @author Eric Bariaux
 */

@Path("/devicediscovery")

public class GetDevice
{

  public static final String DEVICE_DISCOVERY_LIST_JSON_HTTP_CONTENT_TYPE = "application/vnd.openremote.device-discovery-list+json";

  // Instance Fields ------------------------------------------------------------------------------

  /**
   * Security context associated with the incoming HTTP request provided by the host HTTP servlet
   * service.
   */
  @Context private SecurityContext security;

  @Context private ServletContext webapp;


  // REST API Implementation ----------------------------------------------------------------------

  @Produces(DEVICE_DISCOVERY_LIST_JSON_HTTP_CONTENT_TYPE)
  @GET
  public Response list()
  {
    System.err.println("List discovery information...");


    Map<String, DeviceDiscovery> devices = (Map<String, DeviceDiscovery>) webapp.getAttribute("devicesMap");
    if (devices != null) {
      return Response.ok(DeviceDiscovery.toJSONString(new HashSet(devices.values()))).build();
    } else {
      return Response.ok(DeviceDiscovery.toJSONString(new HashSet())).build();
    }
  }

}
