/*
 * Copyright 2013-2015, Juha Lindfors. All rights reserved.
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

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


/**
 * Beehive Device Discovery REST API for adding new devices.
 *
 *   TODO  :
 *     - path annotation
 *     - servlet container
 *     - API
 *
 * @author Juha Lindfors
 */

@Path(AddDevice.RESOURCE_PATH)

public class AddDevice
{

  // Constants ------------------------------------------------------------------------------------

  public static final String RESOURCE_PATH = "add";



  // Instance Fields ------------------------------------------------------------------------------

  /**
   * Security context associated with the incoming HTTP request provided by the host HTTP servlet
   * service.
   */
  @Context private SecurityContext security;



  // REST API Implementation ----------------------------------------------------------------------

  @Consumes(DeviceDiscoveryReader.JSON_HTTP_CONTENT_TYPE)

  @POST public Response create(DeviceDiscovery discovery)
  {
//    log.info(
//        "CREATE ACCOUNT: [Service admin: ''{0}''] created new account for user ''{1}''.",
//        security.getUserPrincipal().getName(), user.getName()
//    );


    System.err.println(discovery.toJSONString());

    return Response.ok().build();
  }

}
