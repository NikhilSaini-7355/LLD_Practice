
interface TalkingStrategy{
    void talk();
}

class Talkable implements TalkingStrategy {
    public void talk(){
        System.out.println("This Robot Can talk");
    }
}

class NonTalkable implements TalkingStrategy {
    public void talk(){
        System.out.println("This Robot can not talk");
    }
}

interface WalkingStrategy {
    void walk();
}

class Walkable implements WalkingStrategy {
    public void walk(){
        System.out.println("This Robot can walk");
    }
}

class NonWalkable implements WalkingStrategy{
    public void walk(){
        System.out.println("This Robot can not walk");
    }
}

interface FlyingStrategy {
    void fly();
}

class Flyable implements FlyingStrategy {
    public void fly(){
        System.out.println("This Robot can fly");
    }
}

class NonFlyable implements FlyingStrategy{
    public void fly(){
        System.out.println("This robot can not fly");
    }
}

interface ProjectionStrategy {
    void project();
}

class Projectable implements ProjectionStrategy {
    public void project(){
        System.out.println("This Robot can project itself");
    }
}

class NonProjectable implements ProjectionStrategy {
    public void project(){
        System.out.println("This robot cannot project itself");
    }
}

abstract class Robot{
    TalkingStrategy talkingstrategy;
    WalkingStrategy walkingStrategy;
    FlyingStrategy flyingstrategy;
    ProjectionStrategy projectionstrategy;
    public Robot(TalkingStrategy t,WalkingStrategy w,FlyingStrategy f,ProjectionStrategy p){
        this.talkingstrategy = t;
        this.walkingStrategy = w;
        this.flyingstrategy = f;
        this.projectionstrategy = p;
    }
    void fly(){
        flyingstrategy.fly();
    }
    void walk(){
        walkingStrategy.walk();
    }
    void talk(){
        talkingstrategy.talk();
    }
    void project(){
        projectionstrategy.project();
    }
}

class CompanionRobot extends Robot{
    public CompanionRobot(TalkingStrategy t,WalkingStrategy w,FlyingStrategy f,ProjectionStrategy p)
    {
        super(t,w,f,p);
    }
}

class WorkerRobot  extends Robot{
    public WorkerRobot(TalkingStrategy t,WalkingStrategy w,FlyingStrategy f,ProjectionStrategy p)
    {
        super(t,w,f,p);
    }
}
class Main{
    public static void main(String[] args)
    {
        Robot obj = new CompanionRobot(new Talkable(), new Walkable(), new NonFlyable(), new Projectable());
        obj.fly();
        obj.talk();
        obj.project();
        obj.walk();
        
        System.out.println("----------------------------------------------------------------------");
        Robot obj2 = new WorkerRobot(new Talkable(), new Walkable(), new Flyable(), new Projectable());
        obj2.fly();
        obj2.talk();
        obj2.project();
        obj2.walk();
    }
}
