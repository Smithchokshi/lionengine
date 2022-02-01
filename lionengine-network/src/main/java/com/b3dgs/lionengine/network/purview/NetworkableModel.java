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
package com.b3dgs.lionengine.network.purview;

import java.util.ArrayList;
import java.util.Collection;

import com.b3dgs.lionengine.network.message.NetworkMessage;

/**
 * Networkable model implementation.
 */
public class NetworkableModel implements Networkable
{
    /** List of messages. */
    private final Collection<NetworkMessage> messages;
    /** The client id. */
    private Byte clientId;

    /**
     * Constructor.
     */
    public NetworkableModel()
    {
        messages = new ArrayList<>();
        clientId = Byte.valueOf((byte) -1);
    }

    @Override
    public void applyMessage(NetworkMessage message)
    {
        // Nothing to do
    }

    @Override
    public void addNetworkMessage(NetworkMessage message)
    {
        messages.add(message);
    }

    @Override
    public Collection<NetworkMessage> getNetworkMessages()
    {
        return messages;
    }

    @Override
    public void clearNetworkMessages()
    {
        messages.clear();
    }

    @Override
    public void setClientId(Byte id)
    {
        clientId = id;
    }

    @Override
    public Byte getClientId()
    {
        return clientId;
    }
}
