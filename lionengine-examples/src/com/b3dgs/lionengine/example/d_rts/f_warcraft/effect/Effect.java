package com.b3dgs.lionengine.example.d_rts.f_warcraft.effect;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.anim.AnimState;
import com.b3dgs.lionengine.anim.Animation;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.SpriteAnimated;
import com.b3dgs.lionengine.game.CameraGame;
import com.b3dgs.lionengine.game.SetupSurfaceGame;
import com.b3dgs.lionengine.game.effect.EffectGame;

/**
 * Effect implementation.
 */
public class Effect
        extends EffectGame
{
    /** Sprite. */
    private final SpriteAnimated sprite;

    /**
     * Constructor.
     * 
     * @param setup The setup reference.
     */
    public Effect(SetupSurfaceGame setup)
    {
        super(setup.configurable);
        final int horizontalFrames = getDataInteger("horizontal", "frames");
        final int verticalFrames = getDataInteger("vertical", "frames");
        sprite = Drawable.loadSpriteAnimated(setup.surface, horizontalFrames, verticalFrames);
        setSize(sprite.getFrameWidth(), sprite.getFrameHeight());
    }

    /**
     * Start the effect.
     * 
     * @param x The horizontal location.
     * @param y The vertical location.
     */
    public void start(int x, int y)
    {
        teleport(x, y);
    }

    /**
     * Play the animation.
     * 
     * @param animation The animation to play.
     */
    public void play(Animation animation)
    {
        sprite.play(animation);
    }

    /**
     * Set the effect frame.
     * 
     * @param frame The frame.
     */
    public void setFrame(int frame)
    {
        sprite.setFrame(frame);
    }

    /**
     * Get the current frame.
     * 
     * @return The current frame.
     */
    public int getFrame()
    {
        return sprite.getFrame();
    }

    /*
     * EffectGame
     */

    @Override
    public void update(double extrp)
    {
        sprite.updateAnimation(extrp);
        if (sprite.getAnimState() == AnimState.FINISHED)
        {
            destroy();
        }
    }

    @Override
    public void render(Graphic g, CameraGame camera)
    {
        sprite.render(g, camera.getViewpointX(getLocationIntX()),
                camera.getViewpointY(getLocationIntY() + sprite.getFrameHeight()));
    }
}
