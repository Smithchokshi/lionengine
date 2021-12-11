/*
 * Copyright (C) 2013-2021 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.graphic.engine;

import java.util.Optional;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.Context;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.graphic.Filter;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.ImageBuffer;
import com.b3dgs.lionengine.graphic.Renderable;
import com.b3dgs.lionengine.graphic.Scanline;
import com.b3dgs.lionengine.graphic.Screen;
import com.b3dgs.lionengine.graphic.Transform;

/**
 * Sequence rendering.
 */
public final class SequenceRenderer
{
    /** Filter graphic. */
    private final Graphic graphic;
    /** Config reference. */
    private final Config config;
    /** Renderer target. */
    private final Renderable target;
    /** Source resolution. */
    private Resolution source;
    /** Filter reference. */
    private Filter filter = FilterNone.INSTANCE;
    /** Scanline reference. */
    private Scanline scanline = ScanlineNone.INSTANCE;
    /** Image buffer (can be <code>null</code> for direct rendering). */
    private ImageBuffer buf;
    /** Filter used (can be <code>null</code> for direct rendering). */
    private Transform transform;
    /** Current screen used (<code>null</code> if not started). */
    private Screen screen;
    /** Pending cursor visibility. */
    private Boolean cursorVisibility = Boolean.TRUE;

    /**
     * Constructor base.
     * 
     * @param context The context reference (must not be <code>null</code>).
     * @param resolution The resolution source reference (must not be <code>null</code>).
     * @param target The renderer target.
     * @throws LionEngineException If invalid arguments.
     */
    protected SequenceRenderer(Context context, Resolution resolution, Renderable target)
    {
        super();

        Check.notNull(context);
        Check.notNull(resolution);

        source = resolution;
        config = context.getConfig();
        graphic = Graphics.createGraphic();
        this.target = target;
    }

    /**
     * Set the current screen to use.
     * 
     * @param screen The screen to use.
     */
    void setScreen(Screen screen)
    {
        this.screen = screen;
    }

    /**
     * Set the filter to use.
     * 
     * @param filter The filter to use (if <code>null</code> then {@link FilterNone#INSTANCE} is used).
     */
    void setFilter(Filter filter)
    {
        this.filter = Optional.ofNullable(filter).orElse(FilterNone.INSTANCE);
        transform = getTransform();
    }

    /**
     * Set the scanline to use.
     * 
     * @param scanline The scanline to use (if <code>null</code> then {@link ScanlineNone#INSTANCE} is used).
     */
    void setScanline(Scanline scanline)
    {
        this.scanline = Optional.ofNullable(scanline).orElse(ScanlineNone.INSTANCE);
    }

    /**
     * Initialize resolution.
     * 
     * @param source The resolution source (must not be <code>null</code>).
     * @throws LionEngineException If invalid argument.
     */
    void initResolution(Resolution source)
    {
        Check.notNull(source);

        setSystemCursorVisible(cursorVisibility.booleanValue());
        this.source = source;
        screen.onSourceChanged(source);
        buf = Graphics.createImageBuffer(source.getWidth(), source.getHeight());
        transform = getTransform();

        final Graphic gbuf = buf.createGraphic();
        graphic.setGraphic(gbuf.getGraphic());

        scanline.prepare(config);
    }

    /**
     * Set the system cursor visibility.
     * 
     * @param visible <code>true</code> if visible, <code>false</code> else.
     */
    void setSystemCursorVisible(boolean visible)
    {
        if (screen == null)
        {
            cursorVisibility = Boolean.valueOf(visible);
        }
        else
        {
            if (visible)
            {
                screen.showCursor();
            }
            else
            {
                screen.hideCursor();
            }
        }
    }

    /**
     * Get the transform associated to the filter keeping screen scale independent.
     * 
     * @return The associated transform instance.
     */
    private Transform getTransform()
    {
        final Resolution output = config.getOutput();

        final double scaleX = output.getWidth() / (double) source.getWidth();
        final double scaleY = output.getHeight() / (double) source.getHeight();

        return filter.getTransform(scaleX, scaleY);
    }

    /**
     * Local render routine.
     */
    void render()
    {
        if (screen.isReady())
        {
            target.render(graphic);

            final Graphic g = screen.getGraphic();
            g.drawImage(filter.filter(buf), transform, 0, 0);
            scanline.render(g);
        }
    }
}
