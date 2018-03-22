/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.audio;

import com.b3dgs.lionengine.LionEngineException;

/**
 * Describe an audio interface, dedicated to long audio stream, playable with a specified volume.
 */
public interface Audio
{
    /**
     * Play the audio.
     * <p>
     * The audio will be played from the beginning until the end.
     * </p>
     * 
     * @throws LionEngineException If unable to play sound.
     */
    void play();

    /**
     * Set the midi volume.
     * 
     * @param volume The volume in percent between included range <code>[0 - 100]</code>.
     * @throws LionEngineException If invalid argument.
     */
    void setVolume(int volume);

    /**
     * Stop the audio.
     */
    void stop();
}
