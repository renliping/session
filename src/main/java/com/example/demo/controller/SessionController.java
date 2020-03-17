package com.example.demo.controller;

import com.example.demo.entity.ActionType;
import com.example.demo.entity.DeliverySession;
import com.example.demo.util.HttpClient;
import com.example.demo.util.HttpClientResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SessionController implements Serializable {

    //服务器地址
    final String URL_PREFIX = "http://127.0.0.1:8081/nbi/deliverysession?id=";

    //线程池
    ExecutorService fixedThreadPool = Executors.newCachedThreadPool();

    /**
     * 发送基本请求
     * @param deliverySession
     */
    public void doBaseSeesion(DeliverySession deliverySession) throws Exception {
              log.info("session request send,{}",deliverySession);
              HashMap<String,String> map = new HashMap<>(8);
              map.put("DeliverySessionId",String.valueOf(deliverySession.getDeliverySessionId()));
              map.put("Action",deliverySession.getAction().getState().toString());

              //二选一
              if(!StringUtils.isEmpty(deliverySession.getTMGIPool())){
                  map.put("TMGIPool",deliverySession.getTMGIPool());
              }else{
                  map.put("TMGI",deliverySession.getTMGI());
              }

              //startTime stopTime参数不能小于0
              if(deliverySession.getAction().getState()== ActionType.START.getState()){
                  if(deliverySession.getStartTime() < 0){
                      throw new Exception("startTime not allowed little thanner 0");
                  }
                  map.put("StartTime",String.valueOf(deliverySession.getStartTime()));
              }else{
                  if(deliverySession.getStopTime() < 0){
                      throw new Exception("stopTime not allowed little thanner 0");
                  }
                  map.put("StopTime",String.valueOf(deliverySession.getStopTime()));
              }

              HttpClientResult httpClientResult =HttpClient.doPost(URL_PREFIX+deliverySession.getDeliverySessionId(),null,
                      map);

              if(httpClientResult.getCode()== HttpStatus.SC_OK){
                 //请求成功
              }else{
                  throw new Exception("request fail");
              }

    }

    /**
     * 并发发送多个请求
     * @param deliverySessions
     */
    public void doMultiSession(List<DeliverySession> deliverySessions){
        if(!CollectionUtils.isEmpty(deliverySessions)){
          for(int i=0;i<deliverySessions.size();i++){
              int copyOfI = i;
              fixedThreadPool.execute(new Runnable() {
                                          @Override
                                          public void run() {
                                              try {
                                                  doBaseSeesion(deliverySessions.get(copyOfI));
                                              } catch (Exception e) {
                                                  e.printStackTrace();
                                              }
                                          }
                                      }

              );
          }
        }
    }

    /**
     * 根据session时长发送stop请求
     * @param deliverySession
     * @param millsec session时长 ，毫秒
     * @throws InterruptedException
     */
    public void sendStopSessionByMillesc(DeliverySession deliverySession,int millsec) throws InterruptedException {
        try {
            //发送start请求
            doBaseSeesion(deliverySession);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sleep
        Thread.sleep(millsec);
        deliverySession.setAction(ActionType.STOP);
        Calendar canlendar = Calendar.getInstance();
        canlendar.setTime(new Date(deliverySession.getStartTime()));
        canlendar.add(Calendar.MILLISECOND, millsec);
        deliverySession.setStopTime(canlendar.getTimeInMillis());
        try {
            //发送stop请求
            doBaseSeesion(deliverySession);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






}
