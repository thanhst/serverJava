package com.louisnguyen.server;

import com.louisnguyen.services.Service;
import com.louisnguyen.utils.Logger;


public class Maintenance extends Thread {

    public static boolean isRuning = false;
    public boolean canUseCode;
    private static Maintenance i;

    private int min;

    private Maintenance() {

    }

    public static Maintenance gI() {
        if (i == null) {
            i = new Maintenance();
        }
        return i;
    }

    public void start(int min) {
        if (!isRuning) {
            isRuning = true;
            this.min = min;
            this.start();
        }
    }

    @Override
    public void run() {
        while (this.min > 0) {
            this.min--;
            if(this.min < 60){
            Service.gI().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau " + min
                    + " giây nữa, vui lòng thoát game để tránh mất vật phẩm");
            }else{
                Service.gI().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau " + min/60
                    + " phút nữa, vui lòng thoát game để tránh mất vật phẩm");
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        Logger.log(Logger.RED,"Hệ thống bắt đầu tiến hành bảo trì.");
        ServerManager.gI().close(100);
    }

}
