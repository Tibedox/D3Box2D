package ru.samsung.d3box2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    public static final float WORLD_WIDTH = 16, WORLD_HEIGHT = 9;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    KinematicBody platform;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        StaticBody floor = new StaticBody(world, 8, 0.5f, 15.5f, 0.4f);
        StaticBody wall1 = new StaticBody(world, 1, 4.5f, 0.4f, 7);
        StaticBody wall2 = new StaticBody(world, 15, 4.5f, 0.4f, 7);

        DynamicBodyCircle[] ball = new DynamicBodyCircle[100];
        for (int i = 0; i < ball.length; i++) {
            ball[i] = new DynamicBodyCircle(world, 8+MathUtils.random(-0.1f, 0.1f), 5+i, 0.3f);
        }
        DynamicBodyBox[] boxes = new DynamicBodyBox[100];
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new DynamicBodyBox(world, 6, 5+i, 0.6f, 0.3f);
        }
        DynamicBodyTriangle[] triangles = new DynamicBodyTriangle[100];
        for (int i = 0; i < triangles.length; i++) {
            triangles[i] = new DynamicBodyTriangle(world, 10, 5+i, 0.5f, 0.5f);
        }

        platform = new KinematicBody(world, 0, 3, 4, 0.6f);
    }

    @Override
    public void render() {
        // события
        platform.move();

        // отрисовка
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        debugRenderer.render(world, camera.combined);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();
        world.step(1/60f, 6, 2);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
