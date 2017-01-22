package game.obj;

import game.Agent;
import game.GeneticAlgorithm;
import game.NeuralNet;

import java.util.List;

/**
 * Created by TeTorro on 21.01.2017.
 */
public class EntityManager {
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

    private NeuralNet neuralNet;

    GeneticAlgorithm geneticAlgorithm;


    public EntityManager(Agent testAgent, List<Agent> agents, float currentTimer, NeuralNet neuralNet) {
        this.testAgent = testAgent;
        this.agents = agents;
        this.currentAgentFitness = 0.0f;
        this.bestFitness = 0.0f;
        this.currentTimer = 0.0f;
        this.neuralNet = neuralNet;
        int totalweights = 5 * HIDDEN_LAYER_NEURONS + HIDDEN_LAYER_NEURONS * 2 + HIDDEN_LAYER_NEURONS + 2;
        this.geneticAlgorithm = new GeneticAlgorithm();
    }
}
