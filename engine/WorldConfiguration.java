package engine;

import engine.lighting.*;

import java.util.ArrayList;

// Class to hold common information about the world, which is often
// passed around to functions together.
// Pretty much a struct...
public class WorldConfiguration {

    private Camera camera;
    private DirectionalLight directionalLight;
    private ArrayList<PointLight> pointLights = new ArrayList<>();
    private Spotlight spotlight;

    public WorldConfiguration(Camera camera) {
        this.camera = camera;
    }

    public void addPointLight(PointLight light) {
        pointLights.add(light);
    }

    public Camera getCamera() {
        return camera;
    }

    public PointLight getPointLight(int index) {
        return pointLights.get(index);
    }

    public ArrayList<PointLight> getPointLights() {
        return pointLights;
    }

    public void setDirectionalLight(DirectionalLight light) {
        directionalLight = light;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setSpotlight(Spotlight light) {
        spotlight = light;
    }

    public Spotlight getSpotlight() {
        return spotlight;
    }

}
