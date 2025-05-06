package ru.samsung.d3box2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
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

    Texture textureAtlas;
    TextureRegion imgColob;

    KinematicBody platform;
    KinematicBodyCross cross;
    DynamicBodyCircle[] balls = new DynamicBodyCircle[1];
    DynamicBodyBox[] boxes = new DynamicBodyBox[4];
    DynamicBodyBox box;
    DynamicBodyTriangle[] triangles = new DynamicBodyTriangle[2];

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(new MyInputProcessor());

        textureAtlas = new Texture("colobog.png");
        imgColob = new TextureRegion(textureAtlas, 0, 0, 100, 100);

        StaticBody floor = new StaticBody(world, 8, 0.5f, 15.5f, 0.4f);
        StaticBody wall1 = new StaticBody(world, 1, 4.5f, 0.4f, 7);
        StaticBody wall2 = new StaticBody(world, 15, 4.5f, 0.4f, 7);


        /*for (int i = 0; i < balls.length; i++) {
            balls[i] = new DynamicBodyCircle(world, 8+MathUtils.random(-0.1f, 0.1f), 5+i, 0.3f);
        }*/
        balls[0] = new DynamicBodyCircle(world, 2, 5, 0.3f);
        box = new DynamicBodyBox(world, 2, 4, 1, 1);
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new DynamicBodyBox(world, 12, 5+i, 0.3f, 0.8f);
        }
        for (int i = 0; i < triangles.length; i++) {
            triangles[i] = new DynamicBodyTriangle(world, 10, 5+i, 0.5f, 0.5f);
        }

        /*platform = new KinematicBody(world, 0, 1.5f, 4, 0.6f);
        cross = new KinematicBodyCross(world, 0, 5, 4, 0.5f);*/
    }

    @Override
    public void render() {
        // события
        /*platform.move();
        cross.move();*/

        // отрисовка
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        debugRenderer.render(world, camera.combined);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgColob, balls[0].getX(), balls[0].getY(),
            balls[0].getWidth()/2, balls[0].getHeight()/2,
            balls[0].getWidth(), balls[0].getHeight(),
            1, 1, balls[0].getAngle());
        batch.end();
        world.step(1/60f, 6, 2);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    class MyInputProcessor implements InputProcessor{
        Vector3 touchDownPos = new Vector3();
        Vector3 touchUpPos = new Vector3();
        Body bodyTouched;

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            touchDownPos.set(screenX, screenY, 0);
            camera.unproject(touchDownPos);
            bodyTouched = null;
            for (DynamicBodyCircle b: balls) {
                if(b.hit(touchDownPos)){
                    bodyTouched = b.body;
                }
            }
            for (DynamicBodyBox b: boxes) {
                if(b.hit(touchDownPos)){
                    bodyTouched = b.body;
                }
            }
            for (DynamicBodyTriangle b: triangles) {
                if(b.hit(touchDownPos)){
                    bodyTouched = b.body;
                }
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if(bodyTouched!=null) {
                touchUpPos.set(screenX, screenY, 0);
                camera.unproject(touchUpPos);
                Vector3 swapPos = new Vector3(touchUpPos).sub(touchDownPos);
                bodyTouched.applyLinearImpulse(new Vector2(-swapPos.x, -swapPos.y), bodyTouched.getPosition(), true);
            }
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
