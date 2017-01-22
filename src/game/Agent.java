package game;

import game.obj.Sensor;

import java.util.ArrayList;
import java.util.List;

import static game.CarGame.HIDDEN_LAYER_NEURONS;

/**
 * Created by TeTorro on 21.01.2017.
 */
public class Agent {
    private static final int FEELER_COUNT = 5;
    private float FEELER_LENGTH = 120.0f;
    boolean hasFailed;
    double distanceDelta;
    List<Double> intersectionDepths;

    int collidedCorner;
    Sensor sensor;

    NeuralNet neuralNet;

    public Agent() {
        this.hasFailed = false;
        this.intersectionDepths = new ArrayList<>(FEELER_COUNT);
        this.distanceDelta = 90.0f;

    }

    public List<Boolean> Update(List<Sensor> inputs) {
        List<Boolean> result = new ArrayList<>();

        if (this.hasFailed == false) {
            // Our NN inputs are the intersection depths normalised and then fliped.
            // Eg if the intersection depth is the feeler length, then we normalise it
            // and subtract it from one. This way we get a gauge of how far the feeler is
            // into the wall.
//            for (int i = 0; i < FEELER_COUNT; i++)
//            {
//                // Normalise the depth value.
//                Double depth = intersectionDepths.get(i) / FEELER_LENGTH;
//
//                // Subtract the normalised depth from 1 to end up with how close a wall is.
//                // If a feeler has sensed no wall it will be FEELER_LENGTH for intersection depth.
//                // So dividing depth / FEELER_LENGTH will result in 1.
//                // If we then subtract the normalised depth from 1 -> 1 - 1 == 0.
//                // Using this as our input for our NNet, it will guarentee that input to not fire.
//                inputs.add(1 - depth);
//            }
            List<Double> puts = new ArrayList<>();
            puts.add(inputs.get(0).isColision() ? 1d : 0d);
            puts.add(0d);
            puts.add(0d);
            puts.add(0d);
            puts.add(1d);
            neuralNet.setInput(puts);
            neuralNet.update();

            // Retrieve outputs. These will be normalised 0 - 1 values.
            Double leftForce = neuralNet.getOutput(0);

            Double rightForce = neuralNet.getOutput(1);

            result.add(leftForce > 0);
            result.add(rightForce > 0);
            return result;
        }
        result.add(false);
        result.add(false);

        return result;
    }

    public Double GetDistanceDelta()
    {
        return distanceDelta;
    }

    public void ClearFailure()
    {
        hasFailed = false;
        collidedCorner = -1;
    }

    public boolean HasFailed()
    {
        return hasFailed;
    }

    void Attach(NeuralNet net)
    {
        neuralNet = net;
    }

     public NeuralNet GetNeuralNet()
    {
        return neuralNet;
    }

    public void CreateNewNet()
    {
        neuralNet.releaseNet();
        neuralNet.createNet(1, FEELER_COUNT, HIDDEN_LAYER_NEURONS, 2);
    }
}
