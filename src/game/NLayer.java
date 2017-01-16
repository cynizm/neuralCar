package game;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by TeTorro on 16.01.2017.
 */


public class NLayer {
    private static final Float BIAS=-1.0f;
    private int totalNeurons;
    private int totalInputs;
    private List<Neuron> neurons;


    public void Evaluate(List<Double> input, List<Double>output) {
        int inputIndex=0;
        for(int i=0; i< totalNeurons; i++){
            double activation = 0.0d;
            for (int j = 0; j < neurons.get(i).numInputs - 1; j++)
            {
                activation += input.get(inputIndex) * neurons.get(i).weights.get(j);
                inputIndex++;
            }
            activation += neurons.get(i).weights.get(neurons.get(i).numInputs) * BIAS;

            output.add(Sigmoid(activation, 1.0f));
            inputIndex = 0;
        }
    }
    public void PopulateLayer(int numOfNeurons, int numOfInputs)
    {
        totalInputs = numOfInputs;
        totalNeurons = numOfNeurons;
        this.neurons = new ArrayList<>();
        for(int n = 0; n<numOfNeurons; n++){
            neurons.add(new Neuron());
        }
        for (int i = 0; i < numOfNeurons; i++)
        {
            neurons.get(i).Populate(numOfInputs);
        }
    }

    public void setWeights(List<Double> weights, int numOfNeurons, int numOfInputs) {
        int index = 0;
        totalInputs = numOfInputs;
        totalNeurons = numOfNeurons;
        this.neurons = new ArrayList<>();
        for(int n = 0; n<numOfNeurons; n++){
            neurons.add(new Neuron());
        }
        // Copy the weights into the neurons.
        for (int i = 0; i < numOfNeurons; i++) {
            neurons.get(i).weights = new ArrayList<>();

            for (int j = 0; j < numOfInputs; j++) {
                neurons.get(i).weights.add(weights.get(index));
                index++;
            }
        }
    }

    public void getWeights(List<Double> out) {
        int size = 0;
        for (int i = 0; i < this.totalNeurons; i++)
        {
            size += neurons.get(i).weights.size();
        }
        out = new ArrayList<>();
        for(int n=0; n<size; n++){
            out.add(0d);
        }

        // Iterate over each neuron and each connection weight and retrieve the weights
        for (int i = 0; i < this.totalNeurons; i++)
        {
            for (int j = 0; j < neurons.get(i).weights.size(); j++)
            {
                out.set(this.totalNeurons*i+j,neurons.get(i).weights.get(j));
            }
        }
    }

    public void setNeurons(List<Neuron> neurons, int numOfNeurons, int numOfInputs) {
        totalInputs = numOfInputs;
        totalNeurons = numOfNeurons;
        this.neurons = neurons;
    }

    private double Sigmoid(double a, double p)
    {
        double ap = (-a)/p;

        return (1 / (1 + Math.exp(ap)));
    }
}
