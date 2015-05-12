/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.tutorials.mario.d;

import com.b3dgs.lionengine.anim.Animation;
import com.b3dgs.lionengine.anim.Animator;
import com.b3dgs.lionengine.game.StateGame;

/**
 * Mario die state implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
class StateDieMario
        extends StateGame
{
    /** Entity reference. */
    private final Entity entity;
    /** Animator reference. */
    private final Animator animator;
    /** Animation reference. */
    private final Animation animation;

    /**
     * Create the die state.
     * 
     * @param entity The entity reference.
     * @param animation The associated animation.
     */
    public StateDieMario(Entity entity, Animation animation)
    {
        super(EntityState.DEATH_MARIO);
        this.entity = entity;
        this.animation = animation;
        animator = entity.getSurface();
    }

    @Override
    public void enter()
    {
        animator.play(animation);
        entity.getMovement().setDestination(0.0, 0.0);
        entity.getJump().setDirection(0.0, 9.0);
    }

    @Override
    public void update(double extrp)
    {
        // Nothing to do
    }
}
