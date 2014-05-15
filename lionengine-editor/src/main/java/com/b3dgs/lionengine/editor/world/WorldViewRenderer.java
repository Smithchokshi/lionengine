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
package com.b3dgs.lionengine.editor.world;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;

import com.b3dgs.lionengine.ColorRgba;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.core.Core;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.game.CameraGame;
import com.b3dgs.lionengine.game.FactoryObjectGame;
import com.b3dgs.lionengine.game.entity.EntityGame;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.geom.Geom;
import com.b3dgs.lionengine.geom.Rectangle;

/**
 * World view paint listener, rendering the current world.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public final class WorldViewRenderer
        implements PaintListener, MouseListener, MouseMoveListener, KeyListener
{
    /** Color of the grid. */
    private static final ColorRgba COLOR_GRID = new ColorRgba(128, 128, 128, 128);
    /** Color of the selection area. */
    private static final ColorRgba COLOR_MOUSE_SELECTION = new ColorRgba(240, 240, 240, 96);
    /** Grid movement sensibility. */
    private static final int GRID_MOVEMENT_SENSIBILITY = 8;

    /**
     * Draw the grid.
     * 
     * @param g The graphic output.
     * @param tw Horizontal grid spacing (width).
     * @param th Vertical grid spacing (height).
     * @param areaX Horizontal global grid size.
     * @param areaY Vertical global grid size.
     * @param color Grid color.
     */
    private static void drawGrid(Graphic g, int tw, int th, int areaX, int areaY, ColorRgba color)
    {
        g.setColor(color);
        for (int v = 0; v <= areaY; v += tw)
        {
            g.drawLine(0, v, areaX, v);
        }
        for (int h = 0; h <= areaX; h += th)
        {
            g.drawLine(h, 0, h, areaY);
        }
    }

    /**
     * Set the camera limits.
     * 
     * @param camera The camera reference.
     * @param maxX The maximum horizontal location.
     * @param maxY The maximum vertical location.
     */
    private static void setCameraLimits(CameraGame camera, int maxX, int maxY)
    {
        if (camera.getLocationX() < 0.0)
        {
            camera.teleportX(0.0);
        }
        else if (camera.getLocationX() > maxX)
        {
            camera.teleportX(maxX);
        }
        if (camera.getLocationY() < 0.0)
        {
            camera.teleportY(0.0);
        }
        else if (camera.getLocationY() > maxY)
        {
            camera.teleportY(maxY);
        }
    }

    /** The parent. */
    private final Composite parent;
    /** The view model. */
    private final WorldViewModel model;
    /** Handler entity. */
    private final HandlerEntity handlerEntity;
    /** Current horizontal mouse location. */
    private int mouseX;
    /** Current vertical mouse location. */
    private int mouseY;
    /** Selecting flag. */
    private boolean selecting;
    /** Moving entity flag. */
    private boolean moving;

    /**
     * Constructor.
     * 
     * @param parent The parent container.
     */
    public WorldViewRenderer(Composite parent)
    {
        this.parent = parent;
        model = WorldViewModel.INSTANCE;
        handlerEntity = new HandlerEntity(model.getCamera());
    }

    /**
     * Render the world.
     * 
     * @param g The graphic output.
     * @param width The view width.
     * @param height The view height.
     */
    private void render(Graphic g, int width, int height)
    {
        final CameraGame camera = model.getCamera();
        final MapTile<?, ?> map = model.getMap();

        final int tw = map.getTileWidth();
        final int th = map.getTileHeight();
        final int areaX = UtilMath.getRounded(width, tw);
        final int areaY = UtilMath.getRounded(height, th);

        camera.setView(0, 0, areaX - tw, areaY);

        // Background
        g.setColor(ColorRgba.GRAY_LIGHT);
        g.drawRect(0, 0, width, height, true);

        // Map area
        g.setColor(ColorRgba.BLUE);
        g.drawRect(0, 0, areaX, areaY, true);

        // Renders
        if (map.getNumberPatterns() > 0)
        {
            map.render(g, camera);
        }
        handlerEntity.update(1.0);
        handlerEntity.render(g);

        drawCursor(g, tw, th, areaX, areaY);
        WorldViewRenderer.drawGrid(g, tw, th, areaX, areaY, WorldViewRenderer.COLOR_GRID);
    }

    /**
     * Draw the cursor.
     * 
     * @param g The graphic output.
     * @param tw The tile width.
     * @param th The tile height.
     * @param areaX Maximum width.
     * @param areaY Maximum height.
     */
    private void drawCursor(Graphic g, int tw, int th, int areaX, int areaY)
    {
        if (!selecting && !moving)
        {
            if (mouseX >= 0 && mouseY >= 0 && mouseX < areaX && mouseY < areaY)
            {
                final int mx = UtilMath.getRounded(mouseX, tw);
                final int my = UtilMath.getRounded(mouseY, th);

                g.setColor(WorldViewRenderer.COLOR_MOUSE_SELECTION);
                g.drawRect(mx, my, tw, th, true);
            }
        }
    }

    /**
     * Update the mouse.
     * 
     * @param mx The mouse horizontal location.
     * @param my The mouse vertical location.
     */
    private void updateMouse(int mx, int my)
    {
        mouseX = mx;
        mouseY = my;
        updateRender();
    }

    /**
     * Update the keyboard.
     * 
     * @param vx The keyboard horizontal movement.
     * @param vy The keyboard vertical movement.
     */
    private void updateKeyboard(int vx, int vy)
    {
        final CameraGame camera = model.getCamera();
        final MapTile<?, ?> map = model.getMap();
        final int tw = map.getTileWidth();
        final int th = map.getTileHeight();
        camera.moveLocation(1.0, vx * tw * WorldViewRenderer.GRID_MOVEMENT_SENSIBILITY, vy * th
                * WorldViewRenderer.GRID_MOVEMENT_SENSIBILITY);

        final int maxX = (map.getWidthInTile() - 1) * tw - camera.getViewWidth();
        final int maxY = map.getHeightInTile() * th - camera.getViewHeight();
        WorldViewRenderer.setCameraLimits(camera, maxX, maxY);

        updateRender();
    }

    /**
     * Update the rendering.
     */
    private void updateRender()
    {
        if (!parent.isDisposed())
        {
            parent.redraw();
        }
    }

    /**
     * Set the entity location.
     * 
     * @param entity The entity reference.
     * @param x The horizontal location.
     * @param y The vertical location.
     * @param side 1 for place, -1 for move.
     */
    private void setEntityLocation(EntityGame entity, int x, int y, int side)
    {
        final MapTile<?, ?> map = model.getMap();
        final int tw = map.getTileWidth();
        final int th = map.getTileHeight();
        entity.teleport(UtilMath.getRounded(x + (side == 1 ? 0 : 1) * entity.getWidth() / 2 + tw / 2, tw) + side
                * entity.getWidth() / 2, UtilMath.getRounded(y + th / 2, th));
    }

    /**
     * Add a new entity at the mouse location.
     * 
     * @param mx The mouse horizontal position.
     * @param my The mouse vertical position.
     */
    private void placeEntity(int mx, int my)
    {
        final Class<? extends EntityGame> type = WorldViewModel.INSTANCE.getSelectedEntity();
        if (type != null)
        {
            final MapTile<?, ?> map = model.getMap();
            final CameraGame camera = model.getCamera();
            final int tw = map.getTileWidth();
            final int th = map.getTileHeight();
            final int h = UtilMath.getRounded(camera.getViewHeight(), th) - map.getTileHeight();
            final int x = camera.getLocationIntX() + UtilMath.getRounded(mx, tw);
            final int y = camera.getLocationIntY() - UtilMath.getRounded(my, th) + h;

            final FactoryObjectGame<?, ?> factoryEntity = WorldViewModel.INSTANCE.getFactoryEntity();
            final EntityGame entity = factoryEntity.createUnsafe(type);
            setEntityLocation(entity, x, y, 1);
            handlerEntity.add(entity);
        }
    }

    /**
     * Get the entity at the specified mouse location.
     * 
     * @param x The horizontal location.
     * @param y The vertical location.
     * @return The entity reference, <code>null</code> if none.
     */
    private EntityGame getEntity(int x, int y)
    {
        final MapTile<?, ?> map = model.getMap();
        final CameraGame camera = model.getCamera();
        final int mx = UtilMath.getRounded(x, map.getTileWidth());
        final int my = UtilMath.getRounded(camera.getViewHeight() - y - 1, map.getTileHeight());
        for (final EntityGame entity : handlerEntity.list())
        {
            if (hitEntity(entity, mx, my, mx + map.getTileWidth(), my + map.getTileHeight()))
            {
                return entity;
            }
        }
        return null;
    }

    /**
     * Check if entity is hit.
     * 
     * @param entity The entity to check.
     * @param x1 First point x.
     * @param y1 First point y.
     * @param x2 Second point x.
     * @param y2 Second point y.
     * @return <code>true</code> if hit, <code>false</code> else.
     */
    private boolean hitEntity(EntityGame entity, int x1, int y1, int x2, int y2)
    {
        final MapTile<?, ?> map = model.getMap();
        final CameraGame camera = model.getCamera();
        if (entity != null)
        {
            final int x = UtilMath.getRounded(entity.getLocationIntX() - entity.getWidth() / 2, map.getTileWidth())
                    - camera.getLocationIntX();
            final int y = UtilMath.getRounded(entity.getLocationIntY(), map.getTileHeight()) - camera.getLocationIntY();
            final Rectangle r1 = Geom.createRectangle(x1, y1, x2 - x1, y2 - y1);
            final Rectangle r2 = Geom.createRectangle(x, y, entity.getWidth(), entity.getHeight());
            if (r1.intersects(r2))
            {
                return true;
            }
        }
        return false;
    }

    /*
     * PaintListener
     */

    @Override
    public void paintControl(PaintEvent paintEvent)
    {
        final GC gc = paintEvent.gc;
        final Graphic g = Core.GRAPHIC.createGraphic();
        g.setGraphic(gc);
        if (model.getMap() != null)
        {
            render(g, paintEvent.width, paintEvent.height);
        }
        else
        {
            gc.drawString("No map implementation defined, select it from the project view !", 0, 0, true);
        }
    }

    /*
     * MouseListener
     */

    @Override
    public void mouseDoubleClick(MouseEvent mouseEvent)
    {
        // Nothing to do
    }

    @Override
    public void mouseDown(MouseEvent mouseEvent)
    {
        final int mx = mouseEvent.x;
        final int my = mouseEvent.y;

        updateMouse(mx, my);

        final EntityGame entity = getEntity(mx, my);
        if (entity != null)
        {
            entity.destroy();
        }
    }

    @Override
    public void mouseUp(MouseEvent mouseEvent)
    {
        final int mx = mouseEvent.x;
        final int my = mouseEvent.y;

        moving = false;
        updateMouse(mx, my);
        placeEntity(mx, my);
    }

    /*
     * MouseMoveListener
     */

    @Override
    public void mouseMove(MouseEvent mouseEvent)
    {
        final int mx = mouseEvent.x;
        final int my = mouseEvent.y;

        updateMouse(mx, my);
    }

    /*
     * KeyListener
     */

    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        final int vx;
        final int vy;
        final int code = keyEvent.keyCode;
        if (code == SWT.ARROW_LEFT)
        {
            vx = -1;
        }
        else if (code == SWT.ARROW_RIGHT)
        {
            vx = 1;
        }
        else
        {
            vx = 0;
        }
        if (code == SWT.ARROW_DOWN)
        {
            vy = -1;
        }
        else if (code == SWT.ARROW_UP)
        {
            vy = 1;
        }
        else
        {
            vy = 0;
        }
        updateKeyboard(vx, vy);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
        // Nothing to do
    }
}
