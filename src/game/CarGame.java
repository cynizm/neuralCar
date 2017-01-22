package game;

import game.obj.*;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TeTorro on 08.01.2017.
 */
public class CarGame {

    public static final String TITLE = "neural Car";
    public static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;

    private List<Obj> objs = new ArrayList<>();
    private List<Obj> objsAdd = new ArrayList<>();
    private List<Obj> objsRemove = new ArrayList<>();

    private Car car = new Car(this);
    private List<Sensor> sensor = new ArrayList<>();

    public enum State { INITIALIZING, TITLE, PLAYING, HITTED, GAME_OVER }
    private State state = State.INITIALIZING;

    static float CHECK_POINT_BONUS = 15.0f;
    static float DEFAULT_ROTATION = 90.0f;
    static int MAX_GENOME_POPULATION = 15;
    static int HIDDEN_LAYER_NEURONS = 8;
    static int MAX_POPULATION = 15;
    static float MUTATION_RATE = 0.15f;


    private Agent testAgent;
    private List<Agent> agents;
    private float currentAgentFitness;
    private float bestFitness;
    private float currentTimer;
    private List<Boolean> decision;

    private NeuralNet neuralNet;

    GeneticAlgorithm geneticAlgorithm;



    public CarGame() {
        init();
        this.testAgent = new Agent();
        this.agents = agents;
        this.currentAgentFitness = 0.0f;
        this.bestFitness = 0.0f;
        this.currentTimer = 0.0f;
        this.neuralNet = new NeuralNet();
        int totalweights = 5 * HIDDEN_LAYER_NEURONS + HIDDEN_LAYER_NEURONS * 2 + HIDDEN_LAYER_NEURONS + 2;
        this.geneticAlgorithm = new GeneticAlgorithm();
        this.testAgent.Attach(neuralNet);
        this.decision= new ArrayList<>();
    }

    public int getWidth() {
        return SCREEN_WIDTH;
    }

    public int getHeight() {
        return SCREEN_HEIGHT;
    }

    public State getState() {
        return state;
    }

    private void newSensors(){
        sensor.add(new Sensor(this, (int) car.x+20, (int) car.y,(int) car.x+50, (int) car.y, 1));
//        sensor.add(new Sensor(this, 48  , 40, 2));
//        sensor.add(new Sensor(this, 60, 0, 3));
//        sensor.add(new Sensor(this, 48,  -40, 4));
//        sensor.add(new Sensor(this, 10  , -50, 5));


    }

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            for (Obj obj : objs) {
                obj.StateChanged(state);
            }
        }
    }

    public void add(Obj obj) {
        objsAdd.add(obj);
    }

    private void init() {
        add(new Initializer(this));
        add(new HUD(this));
        add(car = new Car(this));
        newSensors();
        sensor.stream().forEach(s -> add(s));
        //add(sensor = new Sensor(this));
        createObstacles();
    }

    public void createObstacles() {
        for (int i = 0; i < 30; i++) {
            createOneObstacle();
        }
    }

    public void createOneObstacle() {
        int p = (int) (4 * Math.random());
        int x = 0;
        int y = 0;
        if (p == 0) {
            x = 0;
            y = (int) (SCREEN_HEIGHT * Math.random());
        }
        else if (p == 1) {
            x = SCREEN_WIDTH;
            y = (int) (SCREEN_HEIGHT * Math.random());
        }
        else if (p == 2) {
            x = (int) (SCREEN_WIDTH * Math.random());
            y = 0;
        }
        else if (p == 3) {
            x = (int) (SCREEN_WIDTH * Math.random());
            y = SCREEN_HEIGHT;
        }
        Obstacle obstacle = new Obstacle(this, x, y, 3);
        add(obstacle);
    }

    private void removeAllObstacles() {
        for (Obj obj : objs) {
            if (obj instanceof Obstacle) {
                obj.destroyed = true;
            }
        }


    }

    public void update() {
        for (Obj obj : objs) {
            obj.update();
            if (obj.destroyed) {
                objsRemove.add(obj);
            }
        }
        if (testAgent.HasFailed())
        {
            if (geneticAlgorithm.getCurrentGenomeIndex() == MAX_GENOME_POPULATION - 1)
            {
                EvolveGenomes();
                return;
            }
            nextTestSubject();
        }
        // Update the agent.
        decision = testAgent.Update(sensor);
        objs.addAll(objsAdd);
        objsAdd.clear();
        objs.removeAll(objsRemove);
        objsRemove.clear();
    }

    public void draw(Graphics2D g) {
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        for (Obj obj : objs) {
            obj.draw(g);
        }
    }


    public Obj checkCollision(Obj o1, Class collidedObjType) {
        for (Obj o2 : objs) {
            if (o1 == o2 || !collidedObjType.isInstance(o2)
                    || o1.shape == null || o2.shape == null) {
                continue;
            }
            Area a1 = new Area(o1.shape);
            Area a2 = new Area(o2.shape);
            a1.transform(o1.getTranform());
            a2.transform(o2.getTranform());
            a1.intersect(a2);

            if(o1 instanceof Sensor) {
                Area a = new Area(o1.shape);
                Area b = new Area(o2.shape);
                b.intersect(a);
                //System.out.println("bounds :"+ b.getBounds()+ " next: Bouds2D: "+b.getBounds2D());
                if (!b.isEmpty()) {
                    ((Sensor) o1).setColision(true);
                    //System.out.println("bounds :"+ b.getBounds()+ " next: Bouds2D: "+b.getBounds2D());
                }

            }
            if (!a1.isEmpty()) {

                return o2;
            }
        }

        return null;
    }

    public Obj getIntersection (Obj o1, Class collidedObjType){


//        if(o1 instanceof Sensor && collidedObjType instanceof Obstacle)
//        Area a = new Area(o1);
//        Area b = new Area(collidedObjType);
//        b.intersect(a);
//        if (!b.isEmpty()) {
//            //Shapes have non-empty intersection, so do you actions.
//            //In case of need, actual intersection is in Area b. (its destructive operation)
//        }
        return null;
    }

    // ---

    public void nextTestSubject(){
        geneticAlgorithm.setGenomeFitness(currentAgentFitness, geneticAlgorithm.getCurrentGenomeIndex());
        currentAgentFitness = 0.0f;
        Genome genome = geneticAlgorithm.getNextGenome();
        neuralNet.formGenom(genome, 5 , HIDDEN_LAYER_NEURONS, 2);
        testAgent.Attach(neuralNet);
        testAgent.ClearFailure();
    }

    public void BreedNewPopulation()
    {
        geneticAlgorithm.ClearPopulation();
        int totalWeights = 5 * HIDDEN_LAYER_NEURONS + HIDDEN_LAYER_NEURONS * 2 + HIDDEN_LAYER_NEURONS + 2;
        geneticAlgorithm.generateNewPopulation(MAX_GENOME_POPULATION, totalWeights);
    }

    public void EvolveGenomes()
    {
        geneticAlgorithm.BreedPopulation();
        nextTestSubject();
    }

    public int GetCurrentMemberOfPopulation()
    {
        return geneticAlgorithm.getCurrentGenomeIndex();
    }

    public void forceToNextAgent(){
        if (geneticAlgorithm.getCurrentGenomeIndex() == MAX_GENOME_POPULATION - 1)
        {
            EvolveGenomes();
            return;
        }
        nextTestSubject();
    }

    public void start() {
        removeAllObstacles();
        createObstacles();
        setState(State.PLAYING);
    }

    public void hit() {
        removeAllObstacles();
        setState(State.GAME_OVER);
    }

    public void backToTitle() {
        removeAllObstacles();
        createObstacles();
        setState(State.TITLE);
    }

    public Car getCar(){
        return car;
    }

    public List<Boolean> getDecision() {
        return decision;
    }

}

