package game;

import java.util.List;
import java.util.Random;

/**
 * Created by TeTorro on 16.01.2017.
 */
public class Neuron {
    public int numInputs;
    public List<Double> weights;

    public void Populate(int numOfInputs) {
        this.numInputs = numOfInputs;
        for (int i = 0; i < numOfInputs; i++) {
            weights.add(RandomClamped());
        }

        // Add an extra weight as the bias (the value that acts as a threshold in a step activation).
        weights.add(RandomClamped());

    }

    public void Initialise(List<Double> weightsIn,int numOfInputs){
        this.numInputs=numOfInputs;
        weights=weightsIn;
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
