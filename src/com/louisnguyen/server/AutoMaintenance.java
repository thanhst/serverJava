package com.louisnguyen.server;

import java.time.LocalTime;

import com.louisnguyen.utils.Logger;

public class AutoMaintenance extends Thread {
    public static boolean AutoMaintenance = true;
    public static final int hour = 00;
    public static final int mins= 0;
    public static boolean isRunning;
    private static AutoMaintenance instance;


    public static AutoMaintenance gI(){
        if(instance == null){
            instance = new AutoMaintenance();
        }
        return instance;
    }
    @Override
    public void run(){
        while(!Maintenance.isRuning && !isRunning){
            try{
                if(AutoMaintenance){
                    LocalTime currentTime = LocalTime.now();
                    if(currentTime.getHour() == hour && currentTime.getMinute() == mins){
                        Maintenance.gI().start(60);
                        isRunning = true;
                        AutoMaintenance=false;
                        Logger.log("Đang set up bảo trì!!");
                    }
                }
            }
            catch(Exception e){
                Logger.log("Có lỗi xảy ra vui lòng chờ chút!!");
            }
        }
    }
}
