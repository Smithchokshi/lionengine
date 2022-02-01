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
package com.b3dgs.lionengine.game.feature.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.b3dgs.lionengine.LionEngineException;

/**
 * State base implementation.
 * 
 * @see State
 */
public abstract class StateAbstract implements State
{
    /** Error add itself. */
    static final String ERROR_ADD_ITSELF = "Add transition to itself not allowed !";

    /** Transitions list. */
    private final Map<Class<? extends State>, StateChecker> transitions = new HashMap<>();

    /**
     * Create the state.
     */
    protected StateAbstract()
    {
        super();
    }

    /**
     * Called once update is done and no next transition found. Does nothing by default.
     */
    protected void postUpdate()
    {
        // Nothing by default
    }

    /*
     * State
     */

    @Override
    public final void addTransition(Class<? extends State> next, StateChecker checker)
    {
        if (next == getClass())
        {
            throw new LionEngineException(ERROR_ADD_ITSELF);
        }
        transitions.put(next, checker);
    }

    @Override
    public final void clearTransitions()
    {
        transitions.clear();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Does nothing by default. Can be overridden.
     * </p>
     */
    @Override
    public void exit()
    {
        // Nothing by default
    }

    @Override
    public Class<? extends State> checkTransitions(Class<? extends State> last)
    {
        for (final Entry<Class<? extends State>, StateChecker> entry : transitions.entrySet())
        {
            final StateChecker checker = entry.getValue();
            if (checker.getAsBoolean())
            {
                checker.exit();
                final Class<? extends State> next;
                if (entry.getKey() == StateLast.class)
                {
                    next = last;
                }
                else
                {
                    next = entry.getKey();
                }
                return next;
            }
        }
        postUpdate();
        return null;
    }
}
