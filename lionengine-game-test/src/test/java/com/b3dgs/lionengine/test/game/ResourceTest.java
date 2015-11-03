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
package com.b3dgs.lionengine.test.game;

import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.game.Resource;

/**
 * Test resource class.
 */
public class ResourceTest
{
    /**
     * Test resource functions.
     */
    @Test
    public void testResource()
    {
        final Resource resource = new Resource();
        Assert.assertEquals(0, resource.get());
        final int amount = 10;
        resource.add(amount);
        Assert.assertFalse(resource.canSpend(amount + 1));
        Assert.assertTrue(resource.canSpend(amount));
        resource.spend(amount);
        Assert.assertEquals(0, resource.get());
        Assert.assertFalse(resource.canSpend(amount));
        resource.spend(amount);
        Assert.assertEquals(0, resource.get());

        Assert.assertNotNull(new Resource(10));
    }
}
