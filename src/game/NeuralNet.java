package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TeTorro on 18.01.2017.
 */
public class NeuralNet {

    private int inputAmount;
    private int outputAmount;
    private List<Double> inputs;
    private NLayer inputLayer;
    private List<NLayer> hiddenLayers;
    private NLayer outputLayer;
    private List<Double> outputs;

    public NeuralNet() {
        this.inputs = new ArrayList<>();
        this.hiddenLayers = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.outputLayer = new NLayer();
        this.inputLayer = new NLayer();
    }


    public void update() {
        outputs.clear();

        for (int i = 0; i < hiddenLayers.size(); i++)
        {
            if (i > 0)
            {
                inputs = outputs;
            }

            hiddenLayers.get(i).Evaluate(inputs, outputs);
        }

        inputs = outputs;
        // Process the layeroutputs through the output llayer to
        outputLayer.Evaluate(inputs, outputs);

    }

    public void setInput(List<Double> in) {
        inputs = in;
    }

    public Double getOutput(int ID) {
        if (ID >= outputAmount)
            return 0.0d;
        return outputs.get(ID);
    }

    public int getTotalOutputs() {
        return outputAmount;
    }

    public void createNet(int numOfHiddenLayers, int numOfInputs, int neuronsPerHidden, int numOfOutputs)
    {
        inputAmount = numOfInputs;
        outputAmount = numOfOutputs;

        for (int i = 0; i < numOfHiddenLayers; i++)
        {
            NLayer layer = new NLayer();
            layer.PopulateLayer(neuronsPerHidden, numOfInputs);
            hiddenLayers.add(layer);
        }

        outputLayer = new NLayer();
        outputLayer.PopulateLayer(numOfOutputs, neuronsPerHidden);
    }

    public void releaseNet()
    {
        if (inputLayer != null)
        {
            inputLayer = new NLayer();
        }
        if (outputLayer != null)
        {
            outputLayer = new NLayer();
        }
        for (int i = 0; i < hiddenLayers.size(); i++)
        {
            if (hiddenLayers.get(i) != null)
            {
                hiddenLayers.set(i, new NLayer());
            }
        }
        hiddenLayers.clear();
    }

    public int getNumOfHiddenLayers()
    {
        return hiddenLayers.size();
    }

    public Genome toGenome(){
        Genome genome = new Genome();
        for ( int i = 0; i < this.hiddenLayers.size(); i++)
        {
            List<Double> weights = new ArrayList<>();
            hiddenLayers.get(i).getWeights(weights);
            for (int j = 0; j < weights.size(); j++)
            {
                genome.weights.add(weights.get(j));
            }
        }

        List<Double> weights = new ArrayList<>();
        outputLayer.getWeights(weights);
        for (int i = 0; i < weights.size(); i++)
        {
            genome.weights.add(weights.get(i));
        }
        return genome;
    }

    public void formGenom(Genome genome, int numOfInputs, int neuronsPerHidden, int numOfOutputs)
    {
        releaseNet();

        outputAmount = numOfOutputs;
        inputAmount = numOfInputs;
        int weightsForHidden = numOfInputs * neuronsPerHidden;
        NLayer hidden = new NLayer();
        List<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < neuronsPerHidden; i++)
        {
            List<Double> weights = new ArrayList<>();
            for (int j = 0; j < numOfInputs + 1; j++)
            {
                weights.add(genome.weights.get(i * neuronsPerHidden + j));
            }
            neurons.get(i).Initialise(weights, numOfInputs);
        }
        hidden.LoadLayer(neurons);
        this.hiddenLayers.add(hidden);

        // Clear weights and reasign the weights to the output.
        int weightsForOutput = neuronsPerHidden * numOfOutputs;
        neurons = new ArrayList<>();
        for (int i = 0; i < numOfOutputs; i++)
        {
            List<Double> weights = new ArrayList<>();
            for (int j = 0; j < neuronsPerHidden + 1; j++)
            {
                weights.add(genome.weights.get(i * neuronsPerHidden + j));
            }
            neurons.get(i).Initialise(weights, neuronsPerHidden);
        }
        outputLayer = new NLayer();
        this.outputLayer.LoadLayer(neurons);
    }
}
