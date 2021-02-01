package com.example.omgimbot.mapsbanking.direction.model;

import java.util.List;

public class DirectionObject {
    private List<RouteObject> routes;
    private String status;
    private Result result ;
    public DirectionObject(List<RouteObject> routes, String status) {
        this.routes = routes;
        this.status = status;
    }
    public List<RouteObject> getRoutes() {
        return routes;
    }
    public String getStatus() {
        return status;
    }

    public Result getResult() {
        return result;
    }
}
