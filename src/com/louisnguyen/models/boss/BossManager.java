package com.louisnguyen.models.boss;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.girlkun.network.io.Message;
import com.louisnguyen.models.boss.ConDuongRanDoc.Saibamen;
import com.louisnguyen.models.boss.list_boss.BlackGoku.BlackGoku;
import com.louisnguyen.models.boss.list_boss.Broly.Broly;
import com.louisnguyen.models.boss.list_boss.BrolyNamec.BrolyNamec;
import com.louisnguyen.models.boss.list_boss.BrolyNamec.SuperNamecNail;
import com.louisnguyen.models.boss.list_boss.BrolyTraiDat.BrolyTraidat;
import com.louisnguyen.models.boss.list_boss.BrolyTraiDat.SuperGodTd;
import com.louisnguyen.models.boss.list_boss.BrolyXayda.SieuXaydaHuyenThoai;
import com.louisnguyen.models.boss.list_boss.BrolyXayda.XaydaCalick;
import com.louisnguyen.models.boss.list_boss.Cooler.Cooler;
import com.louisnguyen.models.boss.list_boss.DoanhTraiDocNhan.NinjaAoTim;
import com.louisnguyen.models.boss.list_boss.DoanhTraiDocNhan.RobotVeSi;
import com.louisnguyen.models.boss.list_boss.DoanhTraiDocNhan.TrungUyThep;
import com.louisnguyen.models.boss.list_boss.DoanhTraiDocNhan.TrungUyTrang;
import com.louisnguyen.models.boss.list_boss.DoanhTraiDocNhan.TrungUyXanhLo;
import com.louisnguyen.models.boss.list_boss.Egg.ThanHuyDietEgg;
import com.louisnguyen.models.boss.list_boss.HangDongSoi.BaConSoi;
import com.louisnguyen.models.boss.list_boss.JacyChunVaQuyLao.QuyLao;
import com.louisnguyen.models.boss.list_boss.MrEvilXayda.MrEvilXayda;
import com.louisnguyen.models.boss.list_boss.TDST.TDST;
import com.louisnguyen.models.boss.list_boss.XenBoHung.SieuBoHung;
import com.louisnguyen.models.boss.list_boss.XenBoHung.XenBoHung;
import com.louisnguyen.models.boss.list_boss.XenBoHung.Xencon;
import com.louisnguyen.models.boss.list_boss.android.Android13;
import com.louisnguyen.models.boss.list_boss.android.Android14;
import com.louisnguyen.models.boss.list_boss.android.Android15;
import com.louisnguyen.models.boss.list_boss.android.Android19;
import com.louisnguyen.models.boss.list_boss.android.DrKore;
import com.louisnguyen.models.boss.list_boss.android.KingKong;
import com.louisnguyen.models.boss.list_boss.android.Pic;
import com.louisnguyen.models.boss.list_boss.android.Poc;
import com.louisnguyen.models.boss.list_boss.android.SUPER_ANDROID_17;
import com.louisnguyen.models.boss.list_boss.bandokhobau.TrungUyXanhLoBdkb;
import com.louisnguyen.models.boss.list_boss.bosstauhuydiet.ThanHuyDiet;
import com.louisnguyen.models.boss.list_boss.bosstauhuydiet.ThanThienSu;
import com.louisnguyen.models.boss.list_boss.fide.Fide;
import com.louisnguyen.models.boss.list_boss.nappa.Kuku;
import com.louisnguyen.models.boss.list_boss.nappa.MapDauDinh;
import com.louisnguyen.models.boss.list_boss.nappa.Rambo;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.server.ServerManager;
import com.louisnguyen.services.ItemMapService;
import com.louisnguyen.services.MapService;

public class BossManager implements Runnable {

    private static BossManager I;
    public static final byte ratioReward = 2;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }

    private BossManager() {
        this.bosses = new ArrayList<>();
    }

    private boolean loadedBoss;
    private final List<Boss> bosses;

    public List<Boss> getBosses() {
        return this.bosses;
    }

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        if (this.loadedBoss) {
            return;
        }
        try {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            if (calendar.get(Calendar.HOUR_OF_DAY) == 2) {
                this.createBoss(-103);
            }
            this.createBoss(BossID.TDST);
            this.createBoss(BossID.BROLY_CLONE);
            this.createBoss(BossID.BROLY_CLONE);
            this.createBoss(BossID.BROLY_CLONE);
            this.createBoss(BossID.BROLY_CLONE);
            this.createBoss(BossID.BROLY_CLONE);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.SUPER_ANDROID_17);
            // this.createBoss(BossID.AN_TROM);
            // this.createBoss(BossID.AN_TROM);
            // this.createBoss(BossID.AN_TROM);
            // this.createBoss(BossID.AN_TROM);
            this.createBoss(BossID.SUPER_BROLY);
            this.createBoss(BossID.PIC);
            this.createBoss(BossID.POC);
            this.createBoss(BossID.KING_KONG);
            this.createBoss(BossID.CUMBER);
            this.createBoss(BossID.COOLER_GOLD);
            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.SIEU_BO_HUNG);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.BLACK_GOKU);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.BLACK2);
            this.createBoss(BossID.BLACK_GOKU);
            this.createBoss(BossID.BLACK3);
            this.createBoss(BossID.KUKU);

            this.createBoss(BossID.MAP_DAU_DINH);
            this.createBoss(BossID.RAMBO);
            this.createBoss(BossID.FIDE);
            this.createBoss(BossID.DR_KORE);
            this.createBoss(BossID.ANDROID_14);
            this.createBoss(BossID.COOLER);
            this.createBoss(BossID.MABU);
            this.createBoss(BossID.TAU_PAY_PAY_M);
            this.createBoss(BossID.SU);
            this.createBoss(BossID.PI_LAP);
            this.createBoss(BossID.COOLER);
            this.createBoss(BossID.THAN_HUY_DIET);
            this.createBoss(BossID.THAN_THIEN_SU);
            this.createBoss(BossID.THAN_HUY_DIET);
            this.createBoss(BossID.THAN_THIEN_SU);
            this.createBoss(BossID.THAN_HUY_DIET);
            this.createBoss(BossID.THAN_THIEN_SU);
            // this.createBoss(BossID.QUY_LAO);
            // this.createBoss(BossID.JACKY_CHUN2);
            this.createBoss(BossID.BACONSOI);
            this.createBoss(1019);
            this.createBoss(2049);
            // this.createBoss(2050);
            this.createBoss(2051);
            this.createBoss(2052);
            this.createBoss(2055);
            this.createBoss(2057);
            this.createBoss(9999);
            this.createBoss(-104);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.loadedBoss = true;
        new Thread(BossManager.I, "Update boss").start();
    }

    public Boss createBoss(int bossID) {
        try {
            switch (bossID) {
                case BossID.KUKU:
                    return new Kuku();
                case -104:
                    return new MrEvilXayda();
                case -103:
                    return new BlackGoku();
                case BossID.COOLER:
                    return new Cooler();
                case BossID.SUPER_ANDROID_17:
                    return new SUPER_ANDROID_17();
                case BossID.QUY_LAO:
                    return new QuyLao();
                case BossID.SUPER_BROLY:
                    return new Broly();
                case BossID.BROLY_CLONE:
                    return new Broly();
                case BossID.MAP_DAU_DINH:
                    return new MapDauDinh();
                case BossID.RAMBO:
                    return new Rambo();
                case BossID.FIDE:
                    return new Fide();
                case BossID.DR_KORE:
                    return new DrKore();
                case BossID.ANDROID_19:
                    return new Android19();
                case BossID.ANDROID_13:
                    return new Android13();
                case BossID.ANDROID_14:
                    return new Android14();
                case BossID.ANDROID_15:
                    return new Android15();
                case BossID.PIC:
                    return new Pic();
                case BossID.POC:
                    return new Poc();
                case BossID.KING_KONG:
                    return new KingKong();
                case BossID.XEN_BO_HUNG:
                    return new XenBoHung();
                case BossID.SIEU_BO_HUNG:
                    return new SieuBoHung();
                case BossID.BLACK_GOKU:
                    return new BlackGoku();
                case BossID.XEN_CON_1:
                    return new Xencon();
                case BossID.TDST:
                    return new TDST();
                case BossID.BROLY:
                    return new Broly();
                case BossID.SAIBAMEN:
                    return new Saibamen();
                case BossID.THAN_HUY_DIET:
                    return new ThanHuyDiet();
                case BossID.THAN_THIEN_SU:
                    return new ThanThienSu();
                case BossID.BACONSOI:
                    return new BaConSoi();
                case 1019:
                    return new BrolyNamec();
                case 2049:
                    return new SuperNamecNail();
                case 2051:
                    return new XaydaCalick();
                case 2052:
                    return new SieuXaydaHuyenThoai();
                case 2055:
                    return new BrolyTraidat();
                case 2057:
                    return new SuperGodTd();
                case 9999:
                    return new ThanHuyDietEgg();
                // case BossID.COOLER_GOLD:
                //     return new CoolerGold();
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Boss createBossDoanhTrai(int bossID, int dame, int hp, Zone zone) {
        // System.err.println("create boss donh trai");
        try {
            switch (bossID) {
                case BossID.TRUNG_UY_TRANG:
                    return new TrungUyTrang(dame, hp, zone);
                case BossID.TRUNG_UY_XANH_LO:
                    return new TrungUyXanhLo(dame, hp, zone);
                case BossID.TRUNG_UY_THEP:
                    return new TrungUyThep(dame, hp, zone);
                case BossID.NINJA_AO_TIM:
                    return new NinjaAoTim(dame, hp, zone);
                case BossID.ROBOT_VE_SI1:
                case BossID.ROBOT_VE_SI2:
                case BossID.ROBOT_VE_SI3:
                case BossID.ROBOT_VE_SI4:
                    return new RobotVeSi(bossID, dame, hp, zone);
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Boss createBossBdkb(int bossID, int dame, int hp, Zone zone) {
        try {
            switch (bossID) {
                case BossID.TRUNG_UY_XANH_LO:
                    return new TrungUyXanhLoBdkb(dame, hp, zone);
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existBossOnPlayer(Player player) {
        return player.zone.getBosses().size() > 0;
    }

    public void showListBoss(Player player) {
        if (!player.isAdmin()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Danh Sách Boss");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))
                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                // if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) ||
                // MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])) {
                // continue;
                // }
                msg.writer().writeInt(i);
                msg.writer().writeInt(0);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("On");
                    msg.writer().writeUTF(
                            boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                } else {
                    msg.writer().writeUTF("Off");
                    msg.writer().writeUTF("Chưa có thông tin !");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void callBoss(Player player, int mapId) {
        try {
            if (BossManager.gI().existBossOnPlayer(player)
                    || player.zone.items.stream()
                            .anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                    || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            if (k != null) {
                k.currentLevel = 0;
                k.joinMapByZone(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boss getBossById(int bossId) {
        return BossManager.gI().bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst()
                .orElse(null);
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (Boss boss : this.bosses) {
                    boss.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception ignored) {
            }

        }
    }
}
