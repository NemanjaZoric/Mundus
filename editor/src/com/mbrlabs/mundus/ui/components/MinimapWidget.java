package com.mbrlabs.mundus.ui.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.mbrlabs.mundus.terrain.Terrain;

/**
 * @author Marcus Brummer
 * @version 01-12-2015
 */
public class MinimapWidget extends Widget {

    private static final float WORLD_TO_MINIMAP = 1f;

    protected Array<Terrain> terrains;

    protected Texture terrainTexture;
    protected Texture backgroundTexture;
    protected Texture redTexture;

    private OrthographicCamera cam;

    private Matrix4 originalBatchProjMatrix = new Matrix4();

    public MinimapWidget(Array<Terrain> terrains) {
        super();
        this.terrains = terrains;
        // terrain texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f,0.714f,0.586f, 1.0f);
        pixmap.drawPixel(0, 0);
        terrainTexture = new Texture(pixmap);

        // background texture
        Pixmap pixmap2 = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap2.setColor(0.2f,0.2f,0.2f, 1.0f);
        pixmap2.drawPixel(0, 0);
        backgroundTexture = new Texture(pixmap2);

        // red texture
        Pixmap pixmap3 = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap3.setColor(1,0,0, 1.0f);
        pixmap3.drawPixel(0, 0);
        redTexture = new Texture(pixmap3);

        cam = new OrthographicCamera();
        cam.setToOrtho(false);


        addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                super.drag(event, x, y, pointer);
                cam.translate(getDeltaX(), getDeltaY());
                cam.update();
            }
        });

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        int width = (int)getWidth();
        int height = (int)getHeight();
        float x = getX();
        float y = getY();

        originalBatchProjMatrix.set(batch.getProjectionMatrix());

        // background not affected by cam
        batch.draw(backgroundTexture, x, y, width, height);

        batch.setProjectionMatrix(cam.combined);
        // draw terrains
        for(Terrain terrain : terrains) {
            drawTerrain(batch, terrain);
        }

        // batch.draw(terrainTexture, x, y, 20, 20);
        batch.setProjectionMatrix(originalBatchProjMatrix);
    }

    public void zoom(float zoom) {
        cam.zoom += zoom;
        cam.update();
    }

    private void drawTerrain(Batch batch, Terrain terrain) {
        float x = getX() + terrain.position.x * WORLD_TO_MINIMAP;
        float y = getY() + terrain.position.z * WORLD_TO_MINIMAP;
        float width = terrain.terrainWidth * WORLD_TO_MINIMAP;
        float height = terrain.terrainDepth * WORLD_TO_MINIMAP;

        batch.draw(terrainTexture, x, y, width, height);
    }

}