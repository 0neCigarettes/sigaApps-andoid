package com.example.omgimbot.mapsbanking.direction.model;

import java.util.List;

public class LegsObject {
    private List<StepsObject> steps;
    private Result duration;
    public LegsObject(List<StepsObject> steps) {
        this.steps = steps;
    }
    public List<StepsObject> getSteps() {
        return steps;
    }



}
