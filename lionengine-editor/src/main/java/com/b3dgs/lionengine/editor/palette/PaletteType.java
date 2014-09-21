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
package com.b3dgs.lionengine.editor.palette;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import com.b3dgs.lionengine.editor.UtilEclipse;

/**
 * Represents the different standard palette types.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public enum PaletteType
{
    /** Pointer. Allows to interact with map and objects */
    POINTER(Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW)),
    /** Hand. Allows to navigate on map with mouse. */
    HAND(Display.getDefault().getSystemCursor(SWT.CURSOR_HAND)),
    /** Pipet. Allows to pick an element (duplicate). */
    PIPET(new Cursor(Display.getDefault(), UtilEclipse.getIcon("toolbar", "pipet.png").getImageData(), 0, 15)),
    /** Selection. Allows to select many objects. */
    SELECTION(Display.getDefault().getSystemCursor(SWT.CURSOR_CROSS));

    /** The associated cursor. */
    private final Cursor cursor;

    /**
     * Constructor.
     * 
     * @param cursor The cursor reference.
     */
    private PaletteType(Cursor cursor)
    {
        this.cursor = cursor;
    }

    /**
     * Get the palette cursor.
     * 
     * @return The palette cursor.
     */
    public Cursor getCursor()
    {
        return cursor;
    }
}
