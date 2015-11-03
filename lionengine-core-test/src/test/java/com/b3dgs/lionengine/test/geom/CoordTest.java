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
package com.b3dgs.lionengine.test.geom;

import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.geom.Coord;
import com.b3dgs.lionengine.geom.Geom;

/**
 * Test the coordinate class.
 */
public class CoordTest
{
    /**
     * Test the coordinate.
     */
    @Test
    public void testCoord()
    {
        final Coord coord = Geom.createCoord();
        Assert.assertEquals(coord.getX(), 0.0, 0.000001);
        Assert.assertEquals(coord.getY(), 0.0, 0.000001);

        coord.setX(4.0);
        coord.setY(5.0);
        Assert.assertEquals(coord.getX(), 4.0, 0.000001);
        Assert.assertEquals(coord.getY(), 5.0, 0.000001);

        coord.set(1.0, 2.0);
        Assert.assertEquals(coord.getX(), 1.0, 0.000001);
        Assert.assertEquals(coord.getY(), 2.0, 0.000001);

        coord.translate(-2.0, -1.0);
        Assert.assertEquals(coord.getX(), -1.0, 0.000001);
        Assert.assertEquals(coord.getY(), 1.0, 0.000001);
    }

    /**
     * Test the coordinate equality.
     */
    @Test
    public void testCoordEquals()
    {
        final Coord coord = Geom.createCoord();

        Assert.assertEquals(coord, coord);
        Assert.assertEquals(Geom.createCoord(), Geom.createCoord());
        Assert.assertNotEquals(Geom.createCoord(1.0, 0.0), null);
        Assert.assertNotEquals(Geom.createCoord(1.0, 0.0), new Object());
        Assert.assertNotEquals(Geom.createCoord(1.0, 0.0), Geom.createCoord());
        Assert.assertNotEquals(Geom.createCoord(0.0, 1.0), Geom.createCoord());
    }

    /**
     * Test the coordinate hash.
     */
    @Test
    public void testCoordHash()
    {
        Assert.assertEquals(Geom.createCoord().hashCode(), Geom.createCoord().hashCode());
        Assert.assertNotEquals(Geom.createCoord(1.0, 0.0).hashCode(), Geom.createCoord().hashCode());
        Assert.assertNotEquals(Geom.createCoord(0.0, 1.0).hashCode(), Geom.createCoord().hashCode());
    }
}
