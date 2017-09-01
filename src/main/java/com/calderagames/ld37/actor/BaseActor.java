package com.calderagames.ld37.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

public class BaseActor extends Group {

    protected TextureRegion region;
    protected boolean flipX;
    protected int align;
    protected Vector2 position;

    public BaseActor(TextureRegion region) {
        position = new Vector2();
        setRegion(region);

    }

    public void setRegion(TextureRegion region) {
        this.region = region;

        if(region != null) {
            setSize(region.getRegionWidth(), region.getRegionHeight());
            setPosition(position.x, position.y, align);
        }
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
        position.set(x, y);
        this.align = alignment;
    }

    public void setFlipX(boolean b) {
        flipX = b;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if(region == null)
            return;

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(region, (int) getX() + (flipX?getWidth():0), (int) getY(), (int) getOriginX(), (int) getOriginY(),
                getWidth(), getHeight(), getScaleX() * (flipX?-1:1), getScaleY(), getRotation());
    }

}
