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
package com.b3dgs.lionengine.game.feature;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.ListenableModel;
import com.b3dgs.lionengine.game.Configurer;
import com.b3dgs.lionengine.game.Direction;
import com.b3dgs.lionengine.game.Mover;
import com.b3dgs.lionengine.game.MoverModel;
import com.b3dgs.lionengine.game.SizeConfig;

/**
 * Transformable model implementation.
 */
public class TransformableModel extends FeatureModel implements Transformable, Recyclable
{
    /** Listeners. */
    private final ListenableModel<TransformableListener> listenable = new ListenableModel<>();
    /** Mover model. */
    private final Mover mover = new MoverModel();
    /** Body width. */
    private int width;
    /** Body height. */
    private int height;
    /** Body old width. */
    private int oldWidth;
    /** Body old height. */
    private int oldHeight;

    /**
     * Create feature.
     * <p>
     * The {@link Configurer} can provide a valid {@link SizeConfig}.
     * </p>
     * 
     * @param services The services reference (must not be <code>null</code>).
     * @param setup The setup reference (must not be <code>null</code>).
     * @throws LionEngineException If invalid arguments.
     */
    public TransformableModel(Services services, Setup setup)
    {
        super(services, setup);

        if (setup.hasNode(SizeConfig.NODE_SIZE))
        {
            final SizeConfig config = SizeConfig.imports(setup);
            width = config.getWidth();
            height = config.getHeight();
            oldWidth = width;
            oldHeight = height;
        }
    }

    /**
     * Notify transformable modification.
     * 
     * @param teleport <code>true</code> if teleport, <code>false</code> else.
     */
    private void notifyTransformed(boolean teleport)
    {
        // CHECKSTYLE IGNORE LINE: BooleanExpressionComplexity
        if (teleport
            || Double.compare(getX(), getOldX()) != 0
            || Double.compare(getY(), getOldY()) != 0
            || Double.compare(getWidth(), getOldWidth()) != 0
            || Double.compare(getHeight(), getOldHeight()) != 0)
        {
            for (int i = 0; i < listenable.size(); i++)
            {
                listenable.get(i).notifyTransformed(this);
            }
        }
    }

    /*
     * Transformable
     */

    @Override
    public void addListener(TransformableListener listener)
    {
        listenable.addListener(listener);
    }

    @Override
    public void removeListener(TransformableListener listener)
    {
        listenable.removeListener(listener);
    }

    @Override
    public void backup()
    {
        mover.backup();
    }

    @Override
    public void moveLocation(double extrp, Direction direction, Direction... directions)
    {
        mover.moveLocation(extrp, direction, directions);
        notifyTransformed(false);
    }

    @Override
    public void moveLocationX(double extrp, double vx)
    {
        mover.moveLocationX(extrp, vx);
        notifyTransformed(false);
    }

    @Override
    public void moveLocationY(double extrp, double vy)
    {
        mover.moveLocationY(extrp, vy);
        notifyTransformed(false);
    }

    @Override
    public void moveLocation(double extrp, double vx, double vy)
    {
        mover.moveLocation(extrp, vx, vy);
        notifyTransformed(false);
    }

    @Override
    public void teleport(double x, double y)
    {
        mover.teleport(x, y);
        notifyTransformed(true);
    }

    @Override
    public void teleportX(double x)
    {
        mover.teleportX(x);
        notifyTransformed(true);
    }

    @Override
    public void teleportY(double y)
    {
        mover.teleportY(y);
        notifyTransformed(true);
    }

    @Override
    public void setLocation(double x, double y)
    {
        mover.setLocation(x, y);
        notifyTransformed(false);
    }

    @Override
    public void setLocationX(double x)
    {
        mover.setLocationX(x);
        notifyTransformed(false);
    }

    @Override
    public void setLocationY(double y)
    {
        mover.setLocationY(y);
        notifyTransformed(false);
    }

    @Override
    public void transform(double x, double y, int width, int height)
    {
        mover.teleport(x, y);
        oldWidth = this.width;
        oldHeight = this.height;
        this.width = width;
        this.height = height;
        notifyTransformed(true);
    }

    @Override
    public void setSize(int width, int height)
    {
        oldWidth = this.width;
        oldHeight = this.height;
        this.width = width;
        this.height = height;
        notifyTransformed(false);
    }

    @Override
    public double getX()
    {
        return mover.getX();
    }

    @Override
    public double getY()
    {
        return mover.getY();
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public double getOldX()
    {
        return mover.getOldX();
    }

    @Override
    public double getOldY()
    {
        return mover.getOldY();
    }

    @Override
    public int getOldWidth()
    {
        return oldWidth;
    }

    @Override
    public int getOldHeight()
    {
        return oldHeight;
    }

    /*
     * Recyclable
     */

    @Override
    public void recycle()
    {
        mover.teleport(0.0, 0.0);
    }
}
