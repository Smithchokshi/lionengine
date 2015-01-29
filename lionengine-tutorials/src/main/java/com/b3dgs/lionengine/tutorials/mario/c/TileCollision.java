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
package com.b3dgs.lionengine.tutorials.mario.c;

import java.util.Collection;

import com.b3dgs.lionengine.game.collision.CollisionFormula;
import com.b3dgs.lionengine.game.map.CollisionFunction;

/**
 * List of tile collisions.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
enum TileCollision implements CollisionFormula
{
    /** No collision. */
    NONE;

    /** Model. */
    private final CollisionFormula model = new CollisionFormula(this);

    @Override
    public void addCollisionFunction(CollisionFunction function)
    {
        model.addCollisionFunction(function);
    }

    @Override
    public void removeCollisionFunction(CollisionFunction function)
    {
        model.removeCollisionFunction(function);
    }

    @Override
    public void removeCollisions()
    {
        model.removeCollisionFormulas();
    }

    @Override
    public Collection<CollisionFunction> getCollisionFunctions()
    {
        return model.getCollisionFunctions();
    }

    @Override
    public Enum<?> getValue()
    {
        return model.getValue();
    }
}
