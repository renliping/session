package com.example.demo.entity;


public enum ActionType  {
    START(1),
    STOP(0);

    public Integer getState() {
        return state;
    }
    private Integer state;
    ActionType(Integer state) {
        this.state = state;
    }

}