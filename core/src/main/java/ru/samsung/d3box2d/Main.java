package ru.samsung.d3box2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
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
    DynamicBodyCircle bird;
    DynamicBodyCircle pig;
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


        bird = new DynamicBodyCircle(world, 2, 5, 0.3f, "bird");
        pig = new DynamicBodyCircle(world, 12.5f, 2, 0.3f, "pig");
        box = new DynamicBodyBox(world, 2, 4, 1, 1, "postament");
        boxes[0] = new DynamicBodyBox(world, 12, 2, 0.2f, 1.2f, "brick1");
        boxes[1] = new DynamicBodyBox(world, 13, 2, 0.2f, 1.2f, "brick2");
        boxes[2] = new DynamicBodyBox(world, 12.5f, 3, 1.2f, 0.2f, "brick3");
        boxes[3] = new DynamicBodyBox(world, 12.5f, 4, 0.2f, 1.2f, "brick4");

        for (int i = 0; i < triangles.length; i++) {
            triangles[i] = new DynamicBodyTriangle(world, 10, 5+i, 0.5f, 0.5f);
        }

        /*platform = new KinematicBody(world, 0, 1.5f, 4, 0.6f);
        cross = new KinematicBodyCross(world, 0, 5, 4, 0.5f);*/
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                // Этот метод вызывается при начале столкновения
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                Body bodyA = fixtureA.getBody();
                Body bodyB = fixtureB.getBody();

                // Проверяем, что оба тела динамические
                if (bodyA.getType() == BodyDef.BodyType.DynamicBody &&
                    bodyB.getType() == BodyDef.BodyType.DynamicBody) {

                    // Получаем пользовательские данные тел (если вы их задавали)
                    Object userDataA = bodyA.getUserData();
                    Object userDataB = bodyB.getUserData();

                    // Здесь можно обработать столкновение
                    System.out.println("Dynamic bodies collided: " + userDataA + " and " + userDataB);
                    /*if(userDataA.equals("pig")){

                    }*/
                }
            }

            @Override
            public void endContact(Contact contact) {
                // Вызывается при окончании столкновения
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                // Вызывается перед расчетом столкновения
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                // Вызывается после расчета столкновения
            }
        });
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
        batch.draw(imgColob, bird.getX(), bird.getY(),
            bird.getWidth()/2, bird.getHeight()/2,
            bird.getWidth(), bird.getHeight(),
            1, 1, bird.getAngle());
        batch.draw(imgColob, pig.getX(), pig.getY(),
            pig.getWidth()/2, pig.getHeight()/2,
            pig.getWidth(), pig.getHeight(),
            1, 1, pig.getAngle());
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
            if(bird.hit(touchDownPos)){
                bodyTouched = bird.body;
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
