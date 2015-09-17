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
package org.openremote.beehive.discovery;

import org.openremote.beehive.discovery.model.rest.DeviceDiscoveryReader;
import org.openremote.beehive.discovery.service.DeviceDiscoveryService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * A JAX-RS 2.0 application of Beehive Device Discovery Service. <p>
 * <p/>
 * This JAX-RS application aggregates the relevant REST resources, providers and
 * features that compose the device discovery service. <p>
 *
 * @author Juha Lindfors
 */
public class DeviceDiscoveryApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(DeviceDiscoveryReader.class);
        s.add(DeviceDiscoveryService.class);
        return s;
    }
}

