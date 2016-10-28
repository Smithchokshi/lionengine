/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.map.feature.viewer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.game.camera.Camera;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.map.MapTileGame;
import com.b3dgs.lionengine.game.map.MapTileRenderer;
import com.b3dgs.lionengine.game.tile.Tile;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Transparency;
import com.b3dgs.lionengine.mock.GraphicMock;
import com.b3dgs.lionengine.mock.ImageBufferMock;

/**
 * Test the map tile viewer class.
 */
public class MapTileViewerModelTest
{
    private final MapTileViewer mapViewer = new MapTileViewerModel();

    /**
     * Prepare test.
     */
    @Before
    public void prepare()
    {
        final Services services = new Services();
        services.add(new Camera());
        final MapTileGame map = new MapTileGame();
        map.create(80, 80, 1, 1);
        map.loadSheets(Arrays.asList(Drawable.loadSpriteTiled(new ImageBufferMock(80, 80, Transparency.OPAQUE),
                                                              80,
                                                              80)));
        map.setTile(map.createTile(Integer.valueOf(0), 0, 0, 0));
        services.add(map);
        mapViewer.prepare(new FeaturableModel(), services);
    }

    /**
     * Test the viewer functions.
     */
    @Test
    public void testViewer()
    {
        final AtomicBoolean rendered = new AtomicBoolean();
        final MapTileRenderer renderer = new MapTileRenderer()
        {
            @Override
            public void renderTile(Graphic g, MapTile map, Tile tile, int x, int y)
            {
                rendered.set(true);
            }
        };

        final Graphic g = new GraphicMock();

        mapViewer.render(g);

        Assert.assertFalse(rendered.get());

        mapViewer.addRenderer(renderer);

        mapViewer.render(g);

        Assert.assertTrue(rendered.get());

        rendered.set(false);
        mapViewer.removeRenderer(renderer);
        mapViewer.render(g);

        Assert.assertFalse(rendered.get());

        mapViewer.addRenderer(renderer);
        mapViewer.render(g);

        Assert.assertTrue(rendered.get());

        rendered.set(false);
        mapViewer.clear();
        mapViewer.render(g);

        Assert.assertFalse(rendered.get());
    }
}
