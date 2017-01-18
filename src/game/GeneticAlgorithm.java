package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by TeTorro on 18.01.2017.
 */
public class GeneticAlgorithm {

    private final static double MUTATION_RATE = 0.15f;
    private final static double MAX_PERBETUATION = 0.3f;


    private int currentGenome;
    private int totalPopulation;
    private int genomeID;
    private int generation;
    private int totalGenomeWeights;
    private List<Genome> population;
    private List<Integer> crossoverSplits;

    public GeneticAlgorithm() {
        this.currentGenome=-1;
        this.totalPopulation=0;
        genomeID=0;
        generation=1;
    }

    public Genome getNextGenome(){
        currentGenome++;
        if(currentGenome>=population.size())
            return null;
        return population.get(this.currentGenome);
    }

    public Genome getBestGenome()
    {
        int bestGenome = -1;
        Double fitness = 0d;
        for (int i = 0; i < population.size(); i++)
        {
            if (population.get(i).fitness > fitness)
            {
                fitness = population.get(i).fitness;
                bestGenome = i;
            }
        }

        return population.get(bestGenome);
    }

    public Genome getWorstGenome()
    {
        int worstGenome = -1;
        double fitness = 1000000d;
        for (int i = 0; i < population.size(); i++)
        {
            if (population.get(i).fitness < fitness)
            {
                fitness = population.get(i).fitness;
                worstGenome = i;
            }
        }

        return population.get(worstGenome);
    }

    public void getBestCases(int totalGenomes, List<Genome> out)
    {
        int genomeCount = 0;
        int runCount = 0;

        while (genomeCount < totalGenomes)
        {
            if (runCount > 10)
                return;

            runCount++;

            // Find the best cases for cross breeding based on fitness score.
            Double bestFitness = 0d;
            int bestIndex = -1;
            for ( int i = 0; i < this.totalPopulation; i++)
            {
                if (population.get(i).fitness > bestFitness)
                {
                    boolean isUsed = false;

                    for (int j = 0; j < out.size(); j++)
                    {
                        if (out.get(j).ID == population.get(i).ID)
                        {
                            isUsed = true;
                        }
                    }

                    if (isUsed == false)
                    {
                        bestIndex = i;
                        bestFitness = population.get(bestIndex).fitness;
                    }
                }
            }

            if (bestIndex != -1)
            {
                genomeCount++;
                out.add(population.get(bestIndex));
            }

        }
    }

    public void crossBreed(Genome g1, Genome g2, Genome baby1, Genome baby2)
    {
        // Select a random cross over point.
        int totalWeights = g1.weights.size();
        int crossover = (int) Math.random() % totalWeights;

        baby1 = new Genome();
        baby1.ID = genomeID;
        baby1.weights = new ArrayList<>();
        genomeID++;

        baby2 = new Genome();
        baby2.ID = genomeID;
        baby2.weights = new ArrayList<>();
        genomeID++;

        // Go from start to crossover point, copying the weights from g1.
        for (int i = 0; i < crossover; i++)
        {
            baby1.weights.add(g1.weights.get(i));
            baby2.weights.add(g2.weights.get(i));
        }
        // Go from start to crossover point, copying the weights from g2 to child.
        for (int i = crossover; i < totalWeights; i++)
        {
            baby1.weights.add(g2.weights.get(i));
            baby2.weights.add(g1.weights.get(i));
        }
    }

    public Genome createNewGenome(int totalWeights)
    {
        Genome genome = new Genome();
        genome.ID = genomeID;
        genome.fitness = 0.0d;
        genome.weights = new ArrayList<>();
        for ( int j = 0; j < totalWeights; j++)
        {
            genome.weights.add(RandomClamped());
        }
        genomeID++;
        return genome;
    }

    void generateNewPopulation(int totalPop, int totalWeights)
    {
        generation = 1;
        ClearPopulation();
        currentGenome = -1;
        totalPopulation = totalPop;
        population = new ArrayList<>();
        for (int i = 0; i < population.size(); i++)
        {
            Genome genome = new Genome();
            genome.ID = genomeID;
            genome.fitness = 0.0d;
            genome.weights= new ArrayList<>();
            for (int j = 0; j < totalWeights; j++)
            {
                genome.weights.set(j,RandomClamped());
            }
            genomeID++;
            population.set(i, genome);
        }
    }

    public void BreedPopulation()
    {
        List<Genome> bestGenomes = new ArrayList<>();

        // Find the 4 best genomes.
        this.getBestCases(4, bestGenomes);

        // Breed them with each other twice to form 3*2 + 2*2 + 1*2 = 12 children
        List<Genome> children = new ArrayList<>();

        // Carry on the best dude.
        Genome bestDude = new Genome();
        bestDude.fitness = 0.0f;
        bestDude.ID = bestGenomes.get(0).ID;
        bestDude.weights = bestGenomes.get(0).weights;
        Mutate(bestDude);
        children.add(bestDude);

        // Child genomes.
        Genome baby1 = null;
        Genome baby2 = null;

        // Breed with genome 0.
        crossBreed(bestGenomes.get(0), bestGenomes.get(1), baby1, baby2);
        Mutate(baby1);
        Mutate(baby2);
        children.add(baby1);
        children.add(baby2);
        crossBreed(bestGenomes.get(0), bestGenomes.get(2), baby1, baby2);
        Mutate(baby1);
        Mutate(baby2);
        children.add(baby1);
        children.add(baby2);
        crossBreed(bestGenomes.get(0), bestGenomes.get(3), baby1, baby2);
        Mutate(baby1);
        Mutate(baby2);
        children.add(baby1);
        children.add(baby2);

        // Breed with genome 1.
        crossBreed(bestGenomes.get(1), bestGenomes.get(2), baby1, baby2);
        Mutate(baby1);
        Mutate(baby2);
        children.add(baby1);
        children.add(baby2);
        crossBreed(bestGenomes.get(1), bestGenomes.get(3), baby1, baby2);
        Mutate(baby1);
        Mutate(baby2);
        children.add(baby1);
        children.add(baby2);

        // For the remainding n population, add some random kiddies.
        int remainingChildren = (totalPopulation - children.size());
        for (int i = 0; i < remainingChildren; i++)
        {

            children.add(this.createNewGenome(bestGenomes.get(0).weights.size()));
        }

        ClearPopulation();
        population = children;

        currentGenome = -1;
        generation++;
    }

    public void ClearPopulation()
    {
        population.clear();
    }


    public void Mutate(Genome genome)
    {
        for (int i = 0; i < genome.weights.size(); ++i)
        {
            // Generate a random chance of mutating the weight in the genome.
            if (RandomClamped() < MUTATION_RATE)
            {
                genome.weights.set(i, genome.weights.get(i)+(RandomClamped() * MAX_PERBETUATION));
            }
        }
    }

    public void SetGenomeFitness(float fitness, int index)
    {
        if (index >= population.size())
            return;
        population.get(index).fitness = fitness;
    }

    public Genome GetGenome(int index)
    {
        if (index >= totalPopulation)
            return null;

        return population.get(index);
    }

    public int getTotalPopulation() {
        return totalPopulation;
    }

    public int getGeneration() {
        return generation;
    }

    public int getCurrentGenome() {
        return currentGenome;
    }

    public int getGenomeID() {
        return genomeID;
    }

    private double RandomFloat()
    {
        Random r = new Random();

        return r.nextDouble()/*0 to 32767*/ / Double.MAX_VALUE/*32767*/ + 1.0d;
    }

    private double RandomClamped()
    {
        return RandomFloat() - RandomFloat();
    }

}
