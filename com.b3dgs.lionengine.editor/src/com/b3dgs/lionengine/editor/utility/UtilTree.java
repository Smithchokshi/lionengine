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
package com.b3dgs.lionengine.editor.utility;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.b3dgs.lionengine.LionEngineException;

/**
 * Series of tool functions around the trees.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public final class UtilTree
{
    /**
     * Get the selected item number from the tree.
     * 
     * @param tree The tree reference.
     * @param item The item to search.
     * @return The item index.
     */
    public static int getItemIndex(Tree tree, TreeItem item)
    {
        int i = 0;
        for (final TreeItem current : tree.getItems())
        {
            if (current.equals(item))
            {
                break;
            }
            i++;
        }
        return i;
    }

    /**
     * Auto size tree column and sub items.
     * 
     * @param item The item parent.
     */
    public static void autoSize(TreeItem item)
    {
        for (final TreeColumn column : item.getParent().getColumns())
        {
            column.pack();
        }
    }

    /**
     * Create the auto size listener for tree items which will auto size properties.
     * 
     * @return The created listener.
     */
    public static Listener createAutosizeListener()
    {
        return new Listener()
        {
            @Override
            public void handleEvent(Event e)
            {
                final TreeItem item = (TreeItem) e.item;
                item.getDisplay().asyncExec(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        autoSize(item);
                    }
                });
            }
        };
    }

    /**
     * Private constructor.
     */
    private UtilTree()
    {
        throw new LionEngineException(LionEngineException.ERROR_PRIVATE_CONSTRUCTOR);
    }
}
