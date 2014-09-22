//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 2008 Jan 26: Created this file. - dj@opennms.org
//
// Copyright (C) 2008 Daniel J. Gregor, Jr..  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
package org.opennms.netmgt.eventd;

import org.springframework.dao.DataAccessException;

/**
 * <p>EventdServiceManager interface.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public interface EventdServiceManager {
    /**
     * Lookup the service ID for a specific service by name.
     *
     * @return service ID for the given service name or -1 if not found
     * @exception DataAccessException if there is an error accessing the database
     * @param serviceName a {@link java.lang.String} object.
     * @throws org.springframework.dao.DataAccessException if any.
     */
    public abstract int getServiceId(String serviceName) throws DataAccessException;

    /**
     * Synchronize the in-memory cache with the service table in the database.
     */
    public abstract void dataSourceSync();
}