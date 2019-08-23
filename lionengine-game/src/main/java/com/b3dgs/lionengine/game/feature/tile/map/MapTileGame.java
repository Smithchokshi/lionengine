/*
 * Copyright (C) 2013-2019 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.feature.tile.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.ListenableModel;
import com.b3dgs.lionengine.Localizable;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Surface;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.tile.Tile;
import com.b3dgs.lionengine.game.feature.tile.TileGame;
import com.b3dgs.lionengine.game.feature.tile.TilesExtractor;
import com.b3dgs.lionengine.graphic.ImageBuffer;
import com.b3dgs.lionengine.graphic.drawable.Drawable;
import com.b3dgs.lionengine.graphic.drawable.SpriteTiled;

/**
 * Abstract representation of a standard tile based map. This class uses a List of List to store tiles, a TreeMap to
 * store sheets references ({@link SpriteTiled}), and collisions.
 * <p>
 * The way to prepare a map is the following:
 * </p>
 * 
 * <pre>
 * {@link #create(int, int, int, int)} // prepare memory to store tiles
 * {@link #loadSheets(Media)} // load tile sheets
 * </pre>
 * 
 * <p>
 * Or import a map from a level rip with {@link #create(Media, Media)}.
 * </p>
 */
public class MapTileGame extends FeaturableModel implements MapTile
{
    /** Inconsistent tile size. */
    private static final String ERROR_TILE_SIZE = "Tile size is inconsistent between sheets !";
    /** Inconsistent tile count. */
    private static final String ERROR_TILE_COUNT = "Tile count is inconsistent between sheets !";

    /** Tile set listeners. */
    private final ListenableModel<TileSetListener> listenable = new ListenableModel<>();
    /** Sheets defined. */
    private SpriteTiled[] sheets;
    /** Tiles number per sheet. */
    private int tilesPerSheet = -1;
    /** Sheet configuration file. */
    private Media sheetsConfig;
    /** Tile width. */
    private int tileWidth;
    /** Tile height. */
    private int tileHeight;
    /** Number of horizontal tiles. */
    private int widthInTile;
    /** Number of vertical tiles. */
    private int heightInTile;
    /** Map radius. */
    private int radius;
    /** Tiles map. */
    private List<List<TileGame>> tiles;

    /**
     * Create a map tile.
     */
    public MapTileGame()
    {
        super();
    }

    /**
     * Resize map with new size.
     * 
     * @param newWidth The new width in tile.
     * @param newHeight The new height in tile.
     */
    final void resize(int newWidth, int newHeight)
    {
        final int oldWidth = widthInTile;
        final int oldheight = heightInTile;

        // Adjust height
        for (int v = 0; v < newHeight - oldheight; v++)
        {
            tiles.add(new ArrayList<TileGame>(newWidth));
        }
        // Adjust width
        for (int v = 0; v < newHeight; v++)
        {
            final int width;
            if (v < oldheight)
            {
                width = newWidth - oldWidth;
            }
            else
            {
                width = newWidth;
            }
            for (int h = 0; h < width; h++)
            {
                tiles.get(v).add(null);
            }
        }

        widthInTile = newWidth;
        heightInTile = newHeight;
        radius = (int) Math.ceil(StrictMath.sqrt(newWidth * (double) newWidth + newHeight * (double) newHeight));
    }

    /**
     * Check tiles per sheet integrity.
     * 
     * @param sheet The sheet to check.
     */
    private void checkTilesPerSheet(SpriteTiled sheet)
    {
        final int tilesCount = sheet.getTilesHorizontal() * sheet.getTilesVertical();
        if (tilesPerSheet < 0)
        {
            tilesPerSheet = tilesCount;
        }
        else if (tilesCount != tilesPerSheet)
        {
            throw new LionEngineException(ERROR_TILE_COUNT);
        }
    }

    /**
     * Check tile size integrity.
     * 
     * @param sheet The sheet to check.
     */
    private void checkTileSize(SpriteTiled sheet)
    {
        if ((tileWidth != 0 || tileHeight != 0)
            && tileWidth != sheet.getTileWidth()
            && tileHeight != sheet.getTileHeight())
        {
            throw new LionEngineException(ERROR_TILE_SIZE);
        }
    }

    /*
     * MapTile
     */

    @Override
    public void create(int tileWidth, int tileHeight, int widthInTile, int heightInTile)
    {
        Check.superiorStrict(tileWidth, 0);
        Check.superiorStrict(tileHeight, 0);
        Check.superiorStrict(widthInTile, 0);
        Check.superiorStrict(heightInTile, 0);

        clear();

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.widthInTile = widthInTile;
        this.heightInTile = heightInTile;

        radius = (int) Math.ceil(StrictMath.sqrt(widthInTile * widthInTile + heightInTile * (double) heightInTile));
        tiles = new ArrayList<>(heightInTile);

        for (int v = 0; v < heightInTile; v++)
        {
            tiles.add(v, new ArrayList<TileGame>(widthInTile));
            for (int h = 0; h < widthInTile; h++)
            {
                tiles.get(v).add(h, null);
            }
        }
    }

    @Override
    public void create(Media levelrip, int tileWidth, int tileHeight, int horizontalTiles)
    {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        final TilesExtractor tilesExtractor = new TilesExtractor();
        final Collection<ImageBuffer> buffers = tilesExtractor.extract(tileWidth, tileHeight, Arrays.asList(levelrip));
        loadSheets(SheetsExtractor.extract(buffers, horizontalTiles));

        for (final ImageBuffer tileBuffer : buffers)
        {
            tileBuffer.dispose();
        }

        LevelRipConverter.start(levelrip, this);
    }

    @Override
    public void create(Media levelrip)
    {
        create(levelrip, Medias.create(levelrip.getParentPath(), TileSheetsConfig.FILENAME));
    }

    @Override
    public void create(Media levelrip, Media sheetsConfig)
    {
        clear();

        loadSheets(sheetsConfig);
        final int errors = LevelRipConverter.start(levelrip, this);
        if (errors > 0)
        {
            Verbose.warning(getClass(), "create", "Number of missing tiles: ", String.valueOf(errors));
        }
        this.sheetsConfig = sheetsConfig;
    }

    @Override
    public void loadSheets(List<SpriteTiled> sheets)
    {
        final int sheetsCount = sheets.size();
        this.sheets = new SpriteTiled[sheetsCount];

        for (int sheetId = 0; sheetId < sheetsCount; sheetId++)
        {
            final SpriteTiled sheet = sheets.get(sheetId);
            checkTilesPerSheet(sheet);
            checkTileSize(sheet);

            this.sheets[sheetId] = sheet;
            tileWidth = sheet.getTileWidth();
            tileHeight = sheet.getTileHeight();
        }
    }

    @Override
    public void loadSheets(Media sheetsConfig)
    {
        this.sheetsConfig = sheetsConfig;

        final TileSheetsConfig config = TileSheetsConfig.imports(sheetsConfig);
        tileWidth = config.getTileWidth();
        tileHeight = config.getTileHeight();

        final List<String> configSheets = config.getSheets();
        final int sheetsCount = configSheets.size();
        sheets = new SpriteTiled[sheetsCount];

        for (int sheetId = 0; sheetId < sheetsCount; sheetId++)
        {
            final String sheetFile = configSheets.get(sheetId);
            final Media sheetMedia = Medias.create(sheetsConfig.getParentPath(), sheetFile);
            final SpriteTiled sheet = Drawable.loadSpriteTiled(sheetMedia, tileWidth, tileHeight);
            sheet.load();
            sheet.prepare();

            checkTilesPerSheet(sheet);
            checkTileSize(sheet);

            sheets[sheetId] = sheet;
        }
        configSheets.clear();
    }

    @Override
    public void clear()
    {
        if (tiles != null)
        {
            for (final List<TileGame> list : tiles)
            {
                list.clear();
            }
            tiles.clear();
            widthInTile = 0;
            heightInTile = 0;
        }
    }

    @Override
    public void addListener(TileSetListener listener)
    {
        listenable.addListener(listener);
    }

    @Override
    public void removeListener(TileSetListener listener)
    {
        listenable.removeListener(listener);
    }

    @Override
    public void setTile(int tx, int ty, int number)
    {
        Check.inferiorStrict(tx, getInTileWidth());
        Check.inferiorStrict(ty, getInTileHeight());

        TileGame tile = tiles.get(ty).get(tx);
        if (tile == null)
        {
            tile = new TileGame(number, tx, ty, tileWidth, tileHeight);
            tiles.get(ty).set(tx, tile);
        }
        tile.set(number);
        if (tilesPerSheet > 0)
        {
            tile.setSheet((int) Math.floor(number / (double) tilesPerSheet));
        }

        for (int i = 0; i < listenable.size(); i++)
        {
            listenable.get(i).onTileSet(tile);
        }
    }

    @Override
    public Tile getTile(int tx, int ty)
    {
        if (!UtilMath.isBetween(tx, 0, getInTileWidth() - 1) || !UtilMath.isBetween(ty, 0, getInTileHeight() - 1))
        {
            return null;
        }
        return tiles.get(ty).get(tx);
    }

    @Override
    public Tile getTile(Localizable localizable, int offsetX, int offsetY)
    {
        return getTileAt(localizable.getX() + offsetX, localizable.getY() + offsetY);
    }

    @Override
    public Tile getTileAt(double x, double y)
    {
        final int tx = (int) Math.floor(x / getTileWidth());
        final int ty = (int) Math.floor(y / getTileHeight());
        return getTile(tx, ty);
    }

    @Override
    public Collection<Tile> getNeighbors(Tile tile)
    {
        final int tx = tile.getInTileX();
        final int ty = tile.getInTileY();
        final Collection<Tile> neighbors = new HashSet<>(8);
        for (int ox = -1; ox < 2; ox++)
        {
            for (int oy = -1; oy < 2; oy++)
            {
                final Tile neighbor = getTile(tx + ox, ty + oy);
                if (neighbor != null && (ox != 0 || oy != 0))
                {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    @Override
    public Collection<Tile> getTilesHit(double ox, double oy, double x, double y)
    {
        final Force force = Force.fromVector(ox, oy, x, y);
        final double sx = force.getDirectionHorizontal();
        final double sy = force.getDirectionVertical();

        double h = ox;
        double v = oy;

        final Collection<Tile> found = new ArrayList<>();
        for (int count = 0; count < force.getVelocity(); count++)
        {
            v += sy;
            Tile tile = getTileAt(UtilMath.getRound(sx, h), UtilMath.getRound(sy, v));
            if (tile != null && !found.contains(tile))
            {
                found.add(tile);
            }

            h += sx;
            tile = getTileAt(UtilMath.getRound(sx, h), UtilMath.getRound(sy, v));
            if (tile != null && !found.contains(tile))
            {
                found.add(tile);
            }
        }
        return found;
    }

    @Override
    public int getInTileX(Localizable localizable)
    {
        return (int) Math.floor(localizable.getX() / getTileWidth());
    }

    @Override
    public int getInTileY(Localizable localizable)
    {
        return (int) Math.floor(localizable.getY() / getTileHeight());
    }

    @Override
    public int getInTileWidth(Surface surface)
    {
        return (int) Math.floor(surface.getWidth() / (double) getTileWidth());
    }

    @Override
    public int getInTileHeight(Surface surface)
    {
        return (int) Math.floor(surface.getHeight() / (double) getTileHeight());
    }

    @Override
    public Media getMedia()
    {
        return sheetsConfig;
    }

    @Override
    public SpriteTiled getSheet(int sheetId)
    {
        return sheets[sheetId];
    }

    @Override
    public int getSheetsNumber()
    {
        if (sheets == null)
        {
            return 0;
        }
        return sheets.length;
    }

    @Override
    public int getTilesPerSheet()
    {
        return tilesPerSheet;
    }

    @Override
    public int getTilesNumber()
    {
        int tilesNumber = 0;
        for (int ty = 0; ty < heightInTile; ty++)
        {
            for (int tx = 0; tx < widthInTile; tx++)
            {
                final Tile tile = getTile(tx, ty);
                if (tile != null)
                {
                    tilesNumber++;
                }
            }
        }
        return tilesNumber;
    }

    @Override
    public int getTileWidth()
    {
        return tileWidth;
    }

    @Override
    public int getTileHeight()
    {
        return tileHeight;
    }

    @Override
    public int getInTileWidth()
    {
        return widthInTile;
    }

    @Override
    public int getInTileHeight()
    {
        return heightInTile;
    }

    @Override
    public int getInTileRadius()
    {
        return radius;
    }

    @Override
    public boolean isCreated()
    {
        return tiles != null;
    }

    /*
     * Surface
     */

    @Override
    public int getWidth()
    {
        return getInTileWidth() * getTileWidth();
    }

    @Override
    public int getHeight()
    {
        return getInTileHeight() * getTileHeight();
    }
}
