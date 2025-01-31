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
package com.b3dgs.lionengine.audio.adlmidi;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.b3dgs.lionengine.Generated;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.audio.AudioFormat;
import com.b3dgs.lionengine.audio.AudioVoidFormat;
import com.sun.jna.Native;

/**
 * Handle AdlMidi audio.
 */
public final class AdlMidiFormat implements AudioFormat
{
    /** Load library error. */
    public static final String ERROR_LOAD_LIBRARY = "Error on loading Library: ";
    /** Standard library name. */
    private static final String LIBRARY_NAME = "adlmidi";
    /** Audio extensions as read only. */
    private static final Collection<String> FORMATS = Collections.unmodifiableCollection(Arrays.asList("xmi",
                                                                                                       "midi",
                                                                                                       "mdi",
                                                                                                       "rmi",
                                                                                                       "imf",
                                                                                                       "mus"));
    /** Default sound bank (<code>null</code> if not defined). */
    private static Integer bank;

    /**
     * Get the library, or void format if not found.
     * 
     * @return The audio format.
     */
    @Generated
    public static AudioFormat getFailsafe()
    {
        try
        {
            return new AdlMidiFormat();
        }
        catch (final LionEngineException exception)
        {
            Verbose.exception(exception, ERROR_LOAD_LIBRARY);
            return new AudioVoidFormat(FORMATS);
        }
    }

    /**
     * Set the default sound bank.
     * 
     * @param bank The bank id (<code>null</code> if none).
     */
    public static void setDefaultBank(Integer bank)
    {
        AdlMidiFormat.bank = bank;
    }

    /**
     * Get the default bank used.
     * 
     * @return The default bank id, <code>null</code> if not defined.
     */
    static Integer getDefaultBank()
    {
        return bank;
    }

    /**
     * Load the library.
     * 
     * @return The library binding.
     * @throws LionEngineException If error on loading.
     */
    @Generated
    private static AdlMidiBinding loadLibrary()
    {
        try
        {
            return getLibrary();
        }
        catch (final LinkageError exception)
        {
            throw new LionEngineException(exception, ERROR_LOAD_LIBRARY + LIBRARY_NAME);
        }
    }

    /**
     * Load the library.
     * 
     * @return The library binding.
     */
    private static AdlMidiBinding getLibrary()
    {
        final AdlMidiBinding binding = Native.load(LIBRARY_NAME, AdlMidiBinding.class);
        Verbose.info("Library ", LIBRARY_NAME, " loaded");
        return binding;
    }

    /** Midi binding. */
    private final AdlMidiBinding bind;

    /**
     * Create format.
     * 
     * @throws LionEngineException If unable to load library.
     */
    public AdlMidiFormat()
    {
        super();

        bind = loadLibrary();
    }

    /*
     * AudioFormat
     */

    @Override
    public AdlMidi loadAudio(Media media)
    {
        return new AdlMidiPlayer(media, bind);
    }

    @Override
    public Collection<String> getFormats()
    {
        return FORMATS;
    }

    @Override
    public void close()
    {
        bind.adlStop();
    }
}
