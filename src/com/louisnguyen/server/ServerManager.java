package com.louisnguyen.server;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

import com.girlkun.database.GirlkunDB;
import com.girlkun.network.example.MessageSendCollect;
import com.girlkun.network.server.GirlkunServer;
import com.girlkun.network.server.IServerClose;
import com.girlkun.network.server.ISessionAcceptHandler;
import com.girlkun.network.session.ISession;
import com.louisnguyen.MaQuaTang.MaQuaTangManager;
import com.louisnguyen.jdbc.daos.HistoryTransactionDAO;
import com.louisnguyen.jdbc.daos.PlayerDAO;
import com.louisnguyen.kygui.ShopKyGuiManager;
import com.louisnguyen.models.boss.BossManager;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.matches.pvp.DaiHoiVoThuat;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.server.io.MyKeyHandler;
import com.louisnguyen.server.io.MySession;
import com.louisnguyen.services.ClanService;
import com.louisnguyen.services.InventoryServiceNew;
import com.louisnguyen.services.NgocRongNamecService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.func.ChonAiDay;
import com.louisnguyen.services.func.TopService;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.TimeUtil;
import com.louisnguyen.utils.Util;

public class ServerManager {

    public static String timeStart;

    public static final Map CLIENTS = new HashMap();

    public static String NAME = "ngocrong";
    public static int PORT = 14445;

    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;

    public void init() {
        Manager.gI();
        try {
            if (Manager.LOCAL)
                return;
            GirlkunDB.executeUpdate("update account set last_time_login = '2000-01-01', "
                    + "last_time_logout = '2001-01-01'");
        } catch (Exception e) {
        }
        HistoryTransactionDAO.deleteHistory();
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager.gI().run();
    }

    public void run() {
        activeCommandLine();
        activeGame();
        activeServerSocket();
        MaQuaTangManager.gI().init();
        ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
        NgocRongNamecService.gI().initNgocRongNamec((byte) 0);
        new Thread(DaiHoiVoThuat.gI(), "Thread DHVT").start();// Khởi tạo các thread
        new Thread(ChonAiDay.gI(), "Thread CAD").start();
        new Thread(NgocRongNamecService.gI(), "Thread NRNM").start();
        new Thread(TopService.gI(), "Thread TOP").start();
        new Thread(AutoMaintenance.gI(), "Bảo trì tự động").start();
        long delay = 500; // Cập nhật thông tin game và player
        isRunning = true;
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    Player player = null;
                    for (int i = 0; i < Client.gI().getPlayers().size(); ++i) {
                        if (Client.gI().getPlayers().get(i) != null) {
                            player = (Client.gI().getPlayers().get(i));
                            PlayerDAO.updatePlayer(player);
                        }
                    }
                    ShopKyGuiManager.gI().save();
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update dai hoi vo thuat").start();
        try {
            Thread.sleep(1000);
            BossManager.gI().loadBoss();
            Manager.MAPS.forEach(com.louisnguyen.models.map.Map::initBoss);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(BossManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void act() throws Exception {
        GirlkunServer.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
            @Override
            public void sessionInit(ISession is) {
                if (!canConnectWithIp(is.getIP())) {
                    is.disconnect();
                    return;
                }
                is = is.setMessageHandler(Controller.getInstance())
                        .setSendCollect(new MessageSendCollect())
                        .setKeyHandler(new MyKeyHandler())
                        .startCollect();
            }

            @Override
            public void sessionDisconnect(ISession session) {
                Client.gI().kickSession((MySession) session);
            }
        }).setTypeSessioClone(MySession.class)
                .setDoSomeThingWhenClose(new IServerClose() {
                    @Override
                    public void serverClose() {
                        System.out.println("server close");
                        System.exit(0);
                    }
                })
                .start(PORT);
    }

    private void activeServerSocket() {
        if (true) {
            try {
                this.act();
            } catch (Exception e) {
            }
            return;
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    public void disconnect(MySession session) {
        Object o = CLIENTS.get(session.getIP());
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.getIP(), n);
        }
    }

    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("baotri")) {
                    Maintenance.gI().start(60);
                } else if (line.equals("thread")) {
                    ServerNotify.gI().notify("DragonBlue debug server: " + Thread.activeCount());
                } else if (line.equals("nplayer")) {
                    Logger.error("Player in game: " + Client.gI().getPlayers().size() + "\n");
                } else if (line.equals("admin")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }).start();
                } else if (line.startsWith("bang")) {
                    new Thread(() -> {
                        try {
                            ClanService.gI().close();
                            Logger.error("Save " + Manager.CLANS.size() + " bang");
                        } catch (Exception e) {
                            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");
                        }
                    }).start();
                } else if (line.startsWith("a")) {
                    String a = line.replace("a ", "");
                    Service.gI().sendThongBaoAllPlayer(a);
                } else if (line.startsWith("qua")) {
                    try {
                        List<Item.ItemOption> ios = new ArrayList<>();
                        String[] pagram1 = line.split("=")[1].split("-");
                        String[] pagram2 = line.split("=")[2].split("-");
                        if (pagram1.length == 4 && pagram2.length % 2 == 0) {
                            Player p = Client.gI().getPlayer(Integer.parseInt(pagram1[0]));
                            if (p != null) {
                                for (int i = 0; i < pagram2.length; i += 2) {
                                    ios.add(new Item.ItemOption(Integer.parseInt(pagram2[i]),
                                            Integer.parseInt(pagram2[i + 1])));
                                }
                                Item i = Util.sendDo(Integer.parseInt(pagram1[2]), Integer.parseInt(pagram1[3]), ios);
                                i.quantity = Integer.parseInt(pagram1[1]);
                                InventoryServiceNew.gI().addItemBag(p, i);
                                InventoryServiceNew.gI().sendItemBags(p);
                                Service.gI().sendThongBao(p, "Admin trả đồ. anh em thông cảm nhé...");
                            } else {
                                System.out.println("Người chơi không online");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Lỗi quà");
                    }
                }
            }
        }, "Active line").start();
    }

    private void activeGame() {
    }

    public void close(long delay) {
        GirlkunServer.gI().stopConnect();

        isRunning = false;
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");
        }
        Client.gI().close();
        ShopKyGuiManager.gI().save();

        if (AutoMaintenance.isRunning) {
            AutoMaintenance.isRunning = false;
            try {
                String OS = System.getProperty("os.name").toLowerCase();
                String batFile = "";
                if (OS.contains("win"))
                    batFile = "E:\\Test\\8-9-2023\\SourceBlue\\SourceBlue\\run.bat";
                else
                    batFile = "nro\\serverJava\\serverJava\\run.sh";
                AutoMaintenance.runFile(batFile);
            } catch (Exception e) {

            }
        }
        Logger.log(Logger.RED, "Bảo trì đóng server thành công.\n");
        System.exit(0);

        // try {
        // Runtime.getRuntime().exec("cmd /c exit");
        // } catch (IOException e) {
        // Logger.log("Lỗi khi đóng cmd: " + e.getMessage());
        // e.printStackTrace();
        // }
    }
}
