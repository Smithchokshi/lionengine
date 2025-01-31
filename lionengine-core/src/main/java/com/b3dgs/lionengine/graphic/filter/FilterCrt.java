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
package com.b3dgs.lionengine.graphic.filter;

import com.b3dgs.lionengine.graphic.Filter;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.ImageBuffer;
import com.b3dgs.lionengine.graphic.Transform;

/**
 * CRT implementation.
 * <p>
 * This class is Thread-Safe.
 * </p>
 */
public final class FilterCrt implements Filter
{
    /** Scale value. */
    private final int scale;

    /** Cache width. */
    private int width;
    /** Cache height. */
    private int height;
    /** Cache data. */
    private int[] srcData;
    /** Cache image. */
    private ImageBuffer image;
    /** Cache scaler. */
    private CrtScale scaler;

    /**
     * Create a CRT filter.
     * 
     * @param scale The scale value.
     */
    public FilterCrt(int scale)
    {
        super();

        this.scale = scale;
    }

    /*
     * Filter
     */

    @Override
    public ImageBuffer filter(ImageBuffer source)
    {
        if (width != source.getWidth() || height != source.getHeight())
        {
            width = source.getWidth();
            height = source.getHeight();
            srcData = new int[width * height];
            if (scaler != null)
            {
                scaler.close();
            }
            scaler = new CrtScale(width, height, scale);
            image = Graphics.createImageBuffer(width * scale, height * scale, source.getTransparentColor());
        }
        srcData = source.getRgbRef();
        image.setRgb(0, 0, width * scale, height * scale, scaler.getScaled(srcData), 0, width * scale);

        return image;
    }

    @Override
    public Transform getTransform(double scaleX, double scaleY)
    {
        final Transform transform = Graphics.createTransform();
        transform.scale(scaleX / scale, scaleY / scale);
        return transform;
    }

    @Override
    public int getScale()
    {
        return scale;
    }

    @Override
    public void close()
    {
        if (scaler != null)
        {
            scaler.close();
        }
    }
}
