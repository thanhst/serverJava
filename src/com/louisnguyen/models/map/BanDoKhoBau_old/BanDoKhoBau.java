package com.louisnguyen.models.map.BanDoKhoBau_old;

import java.util.ArrayList;
import java.util.List;

import com.louisnguyen.models.BangHoi.Clan;
import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossManager;
import com.louisnguyen.models.map.TrapMap;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.mob.Mob;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.ItemTimeService;
import com.louisnguyen.services.MapService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.Util;

/**
 *
 * @author BTH
 */
public class BanDoKhoBau implements Runnable {

    public static final long POWER_CAN_GO_TO_DBKB = 2000000000;

    public static final List<BanDoKhoBau> BAN_DO_KHO_BAUS;
    public static final int MAX_AVAILABLE = 50;
    public static int TIME_BAN_DO_KHO_BAU = 1800000;

    // public static Player player;

    static {
        BAN_DO_KHO_BAUS = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            BAN_DO_KHO_BAUS.add(new BanDoKhoBau(i));
        }
    }

    public int id;
    public byte level;
    public final List<Zone> zones;

    public Clan clan;

    public boolean isOpened;
    private long lastTimeOpen;
    private boolean running;
    private long lastTimeUpdate;

    public BanDoKhoBau(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        running = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                // for(Zone zone : zones) {
                // List<Player> players = zone.getPlayers();
                // for (Player pl : players) {
                Thread.sleep(10000);
                if (Util.canDoWithTime(lastTimeUpdate, 10000)) {
                    update();
                    // checktrunguy(pl);
                    lastTimeUpdate = System.currentTimeMillis();
                }
                // }
                // }
            } catch (Exception ignored) {
            }

        }
    }

    public void update() {
        for (BanDoKhoBau bando : BAN_DO_KHO_BAUS) {
            if (Util.canDoWithTime(lastTimeOpen, TIME_BAN_DO_KHO_BAU)) {
                this.finish();
                return;
            }
        }
    }

    public void checktrunguy(Player player) {
        Boss trunguytrang = BossManager.gI().getBossById(-555);
        if ((trunguytrang == null || trunguytrang.isDie()) && running == true) {
            CloseBanDoSau30s(player);
            // Service.gI().sendThongBaoOK(player, "OK");
        } else {
            System.out.println("nhu lon");
        }
    }

    public void openBanDoKhoBau(Player plOpen, Clan clan, byte level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.banDoKhoBau_lastTimeOpen = this.lastTimeOpen;
        this.clan.banDoKhoBau_playerOpen = plOpen;
        // this.clan.BanDoKhoBau = this;

        resetBanDo();
        ChangeMapService.gI().goToDBKB(plOpen);
        sendTextBanDoKhoBau();
    }

    private void resetBanDo() {
        for (Zone zone : zones) {
            for (TrapMap trap : zone.trapMaps) {
                trap.dame = this.level * 10000;
            }
        }
        for (Zone zone : zones) {
            for (Mob m : zone.mobs) {
                m.initMobBanDoKhoBau(m, this.level);
                m.hoiSinhMob(m);
                m.hoiSinh();
                m.sendMobHoiSinh();
            }
        }
    }

    // public void Finish30s() {
    // Boss boss = BossManager.gI().getBossById(-555);
    // if(boss == null && boss.isDie()){
    // CloseBanDoSau30s();
    // }
    // }

    public void CloseBanDoSau30s(Player player) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000); // Đợi 30 giây
                    // Hoạt động bạn muốn thực hiện sau 30 giây
                    finish();
                    ItemTimeService.gI().sendTextBanDoKhoBau(player);
                    System.out.println("Hoạt động được thực hiện sau 30 giây!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(task);
        thread.start(); // Bắt đầu chạy luồng

        System.out.println("Tiếp tục thực hiện các hoạt động khác...");

    }

    // kết thúc bản đồ kho báu
    public void finish() {
        List<Player> plOutBD = new ArrayList();
        for (Zone zone : zones) {
            List<Player> players = zone.getPlayers();
            for (Player pl : players) {
                plOutBD.add(pl);
                kickOutOfBDKB(pl);
            }

        }
        for (Player pl : plOutBD) {
            ChangeMapService.gI().changeMapBySpaceShip(pl, 5, -1, 64);
        }
        this.clan.banDoKhoBau = null;
        this.clan = null;
        this.isOpened = false;
    }

    private void kickOutOfBDKB(Player player) {
        if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
            Service.getInstance().sendThongBao(player, "Hang Kho Báu Đã Sập Bạn Đang Được Đưa Ra Ngoài");
            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1038);
            running = false;
            this.clan.banDoKhoBau = null;
        }
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public static void addZone(int idBanDo, Zone zone) {
        BAN_DO_KHO_BAUS.get(idBanDo).zones.add(zone);
    }

    private void sendTextBanDoKhoBau() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextBanDoKhoBau(pl);
        }
    }
}
