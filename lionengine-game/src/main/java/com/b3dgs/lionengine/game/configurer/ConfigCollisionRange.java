/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.game.configurer;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.Axis;
import com.b3dgs.lionengine.game.collision.CollisionRange;
import com.b3dgs.lionengine.game.collision.CollisionSource;
import com.b3dgs.lionengine.stream.XmlNode;

/**
 * Represents the collision range from a configurer node.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 * @see Configurer
 */
public class ConfigCollisionRange
{
    /** Output axis attribute. */
    public static final String AXIS = "output";
    /** Input min X attribute. */
    public static final String MIN_X = "minX";
    /** Input max X attribute. */
    public static final String MAX_X = "maxX";
    /** Input min Y attribute. */
    public static final String MIN_Y = "minY";
    /** Input max Y attribute. */
    public static final String MAX_Y = "maxY";
    /** Input source attribute. */
    public static final String SOURCE = "source";
    /** Axis type error. */
    private static final String ERROR_TYPE = "Unknown type: ";

    /**
     * Create the collision range from its configuration.
     * 
     * @param node The node reference.
     * @return The collision range instance.
     * @throws LionEngineException If error when reading node.
     */
    public static CollisionRange create(XmlNode node) throws LionEngineException
    {
        final String axisName = node.readString(AXIS);
        try
        {
            return new CollisionRange(Axis.valueOf(axisName), node.readInteger(MIN_X), node.readInteger(MAX_X),
                    node.readInteger(MIN_Y), node.readInteger(MAX_Y), CollisionSource.valueOf(node.readString(SOURCE)));
        }
        catch (final IllegalArgumentException exception)
        {
            throw new LionEngineException(ERROR_TYPE, axisName);
        }
    }

    /**
     * Constructor.
     */
    private ConfigCollisionRange()
    {
        // Private constructor
    }
}
