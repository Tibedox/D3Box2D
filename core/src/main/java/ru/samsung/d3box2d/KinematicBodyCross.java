package ru.samsung.d3box2d;

import static ru.samsung.d3box2d.Main.WORLD_WIDTH;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class KinematicBodyCross {
    public float x, y;
    public float width, height;
    private float vx = 2f;
    private float va = 4f;
    private Body body;

    public KinematicBodyCross(World world, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(height/2, width/2);

        body.createFixture(shape, 0);
        body.createFixture(shape2, 0);

        shape.dispose();
        shape2.dispose();

        body.setLinearVelocity(vx, 0);
        body.setAngularVelocity(va);
    }

    public void move(){
        if(body.getPosition().x < 0 || body.getPosition().x > WORLD_WIDTH){
            vx = -vx;
            body.setLinearVelocity(vx, 0);
        }
    }
}
