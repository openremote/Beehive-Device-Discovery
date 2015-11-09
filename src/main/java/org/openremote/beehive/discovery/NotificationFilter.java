/*
 * OpenRemote, the Home of the Digital Home.
 * Copyright 2014-2015, OpenRemote Inc.
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
package org.openremote.beehive.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Filter to notify of Device Discovery knowledge change.
 *
 * @author Eric Bariaux
 */
public class NotificationFilter implements Filter
{
  private static final Logger LOG = LoggerFactory.getLogger(NotificationFilter.class);
  public static final String NOTIFICATION_USER_NAME = "org.openremote.beehive.discovery.NotificationFilter_username";

  private String notificationURI = null;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException
  {
    String configParameterURI = filterConfig.getInitParameter("org.openremote.beehive.discovery.notificationURI");
    if (configParameterURI != null && !"".equals(configParameterURI.trim())) {
      notificationURI = configParameterURI;
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    // Nothing to do on the way in
    chain.doFilter(request, response);

    if (notificationURI == null) {
      return;
    }
    if (((HttpServletResponse)response).getStatus() >= 400) {
      // Don't notify in case of error
      return;
    }
    Object username = request.getAttribute(NOTIFICATION_USER_NAME);
    if (username != null) {
      try {
        Client client = ClientBuilder.newClient();
        Invocation.Builder invocationBuilder = client.target(notificationURI).request();
        invocationBuilder.post(Entity.entity("{\"username\" : \"" + username + "\"}", MediaType.APPLICATION_JSON));
      } catch (Exception e) {
        LOG.warn("Could not post notification", e);
      }
    }
  }

  @Override
  public void destroy()
  {
     // No cleanup required
  }
}
