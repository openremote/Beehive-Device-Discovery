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
package org.openremote.beehive.discovery.model.rest;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.openremote.model.DeviceDiscovery;
import org.openremote.model.data.json.DeserializationException;
import org.openremote.model.data.json.DeviceDiscoveryTransformer;


/**
 * TODO
 *
 * @author Juha Lindfors
 */
@Consumes(DeviceDiscoveryReader.JSON_HTTP_CONTENT_TYPE)

public class DeviceDiscoveryReader implements MessageBodyReader<DeviceDiscovery>
{

  // Constants ------------------------------------------------------------------------------------

  public static final String JSON_HTTP_CONTENT_TYPE =
      "application/vnd.openremote.device-discovery+json";



  // Implements MessageBodyReader -----------------------------------------------------------------

  @Override public boolean isReadable(Class<?> type, Type genericType,
                                      Annotation[] annotations, MediaType mediaType)
  {
    return type == DeviceDiscovery.class;
  }



  @Override public DeviceDiscovery readFrom(Class<DeviceDiscovery> type, Type genericType,
                                            Annotation[] annotations, MediaType mediaType,
                                            MultivaluedMap<String, String> httpHeaders,
                                            InputStream entityStream)
  {
    try
    {
      // TODO : set upper limit to request document size.
      // TODO : enforce a request timeout

      // TODO : log.info("Deserializing device discovery JSON document...");
      System.err.println("Deserializing device discovery JSON document...");


      // Deserialize device discovery instance from JSON stream...

      return new DeviceDiscoveryTransformer().read(
          new BufferedReader(new InputStreamReader(entityStream))
      );
    }

    catch (DeserializationException exception)
    {
      // TODO : log.error(
      //    "Deserializing device discovery failed: {0}",
      //    exception, exception.getMessage()
      //);
      System.err.println("Deserializing discovery failed: " + exception.getMessage());
      exception.printStackTrace();

      // TODO : propagate back to client
      throw new WebApplicationException("Unable to parse device discovery from JSON: " + exception.getMessage());
      //throw new HttpBadRequest(
      //    exception, "Unable to parse device discovery from JSON: " + exception.getMessage()
      //);
    }

//    catch (ClassCastException exception)
//    {
//      String msg =
//          "Type mismatch. Was expecting type ''{0}'' but the resulting object from a " +
//          "JSON transformer could not be converted to this type.";
//
//      log.error(msg, DeviceDiscovery.class.getSimpleName());
//
//      throw new HttpInternalError(msg, DeviceDiscovery.class.getSimpleName());
//    }
//
    catch (Exception exception)
    {
      // TODO : log.error("Unknown error: " + exception.getMessage());
      System.err.println("Unknown error: " + exception.getMessage());
      exception.printStackTrace();

      // TODO : propagate back to client
      throw new WebApplicationException(exception.getMessage());
      //throw new HttpInternalError(exception, exception.getMessage());
    }
  }

}
