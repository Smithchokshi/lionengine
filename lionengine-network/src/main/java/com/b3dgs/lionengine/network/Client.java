/*
 * Copyright (C) 2013-2022 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.b3dgs.lionengine.network;

import com.b3dgs.lionengine.LionEngineException;

/**
 * List of services provided by a client.
 */
interface Client extends Networker<ConnectionListener>
{
    /**
     * Connect to a server.
     * 
     * @param ip The server ip.
     * @param port The server port.
     * @throws LionEngineException If unable to connect.
     */
    void connect(String ip, int port);

    /**
     * Check if the client is connected to a server.
     * 
     * @return <code>true</code> if connected, <code>false</code> else.
     */
    boolean isConnected();

    /**
     * Set a new client name, and notify the server.
     * 
     * @param name The new client name.
     */
    void setName(String name);

    /**
     * Get the client name.
     * 
     * @return The client name.
     */
    String getName();

    /**
     * Get the ping from the server (time elapsed between the ping request and response).
     * 
     * @return The ping from the server.
     */
    int getPing();

    /**
     * Get the client id.
     * 
     * @return The client id.
     */
    byte getId();
}
