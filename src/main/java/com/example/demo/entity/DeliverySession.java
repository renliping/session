package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DeliverySession implements Serializable {

    private int deliverySessionId;
    private ActionType Action;
    private String TMGIPool;
    private String TMGI;
    private int startTime;
    private int stopTime;
    private String version;
    DeliverySession(int deliverySessionId,ActionType actionType,String TMGIPool,String TMGI,
                    int startTime,int stopTime,String version){
        this.deliverySessionId = deliverySessionId;
        this.Action = actionType;
        this.TMGIPool = TMGIPool;
        this.TMGI = TMGI;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.version = version;
    }

}
