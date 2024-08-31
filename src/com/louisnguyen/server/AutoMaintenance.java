package com.louisnguyen.server;

import java.io.IOException;
import java.time.LocalTime;

import com.louisnguyen.utils.Logger;

public class AutoMaintenance extends Thread {
    public static boolean AutoMaintenance = true;
    public static final int hour = 15;
    public static final int mins = 00;
    public static boolean isRunning;
    private static AutoMaintenance instance;

    public static AutoMaintenance gI() {
        if (instance == null) {
            instance = new AutoMaintenance();
        }
        return instance;
    }

    @Override
    public void run() {
        while (!Maintenance.isRuning && !isRunning) {
            try {
                if (AutoMaintenance) {
                    LocalTime currentTime = LocalTime.now();
                    if (currentTime.getHour() == hour && currentTime.getMinute() == mins) {
                        Maintenance.gI().start(60);
                        isRunning = true;
                        AutoMaintenance = false;
                        Logger.log("Đang set up bảo trì!!");
                    }
                }
            } catch (Exception e) {
                Logger.log("Có lỗi xảy ra vui lòng chờ chút!!");
            }
        }
    }

    public static void runFile(String batFile) throws IOException, InterruptedException {
        String OS = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder = null;
        // ProcessBuilder processBuilder = new ProcessBuilder("gnome-terminal", "--",
        // "bash", "-c", batFile);
        if (OS.contains("win")) {
            processBuilder = new ProcessBuilder("cmd", "/c", "start", batFile);
        } else {
            String checkSessionCommand = "screen -list | grep mysession";
            Process checkSessionProcess = new ProcessBuilder("bash", "-c", checkSessionCommand).start();
            checkSessionProcess.waitFor();

            // Nếu session đã tồn tại, thay đổi tên session
            String screenSessionName = "mysession";
            if (checkSessionProcess.exitValue() == 0) {
                // Session đã tồn tại, thay đổi tên session để tạo một session mới
                screenSessionName = "mysession_" + System.currentTimeMillis(); // Sử dụng thời gian hiện tại để tạo tên
                                                                               // duy nhất
            }

            String screenCommand = "screen -dmS " + screenSessionName + " bash -c \"" + batFile + "\"";
            processBuilder = new ProcessBuilder("bash", "-c", screenCommand);
        }
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
