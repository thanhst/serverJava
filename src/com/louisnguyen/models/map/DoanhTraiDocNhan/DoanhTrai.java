package com.louisnguyen.models.map.DoanhTraiDocNhan;

import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.BangHoi.Clan;
import com.louisnguyen.models.mob.Mob;
import com.louisnguyen.services.ItemTimeService;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossManager;
import com.louisnguyen.models.boss.BossStatus;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.Util;
import java.util.Arrays;

/**
 *
 * @author Khánh Nguu
 */
@Data
public class DoanhTrai {

    //bang hội đủ số người mới đc mở
    public static final int N_PLAYER_CLAN = 2;
    //số người đứng cùng khu
    public static final int N_PLAYER_MAP = 2;
    public static final int AVAILABLE = 20; // số lượng doanh trại trong server
    public static final int TIME_DOANH_TRAI = 1_800_000;

    public int getId() {
        return id;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public Clan getClan() {
        return clan;
    }

    public long getLastTimeOpen() {
        return lastTimeOpen;
    }

    public boolean isTimePickDragonBall() {
        return timePickDragonBall;
    }

    public List<Integer> getListMap() {
        return listMap;
    }

    public int getCurrentIndexMap() {
        return currentIndexMap;
    }

    public List<Boss> getBossDoanhTrai() {
        return bossDoanhTrai;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public void setLastTimeOpen(long lastTimeOpen) {
        this.lastTimeOpen = lastTimeOpen;
    }

    public void setTimePickDragonBall(boolean timePickDragonBall) {
        this.timePickDragonBall = timePickDragonBall;
    }

    public void setListMap(List<Integer> listMap) {
        this.listMap = listMap;
    }

    public void setCurrentIndexMap(int currentIndexMap) {
        this.currentIndexMap = currentIndexMap;
    }

    public void setBossDoanhTrai(List<Boss> bossDoanhTrai) {
        this.bossDoanhTrai = bossDoanhTrai;
    }

    private int id;
    private List<Zone> zones;
    private Clan clan;

    private long lastTimeOpen;

    public boolean timePickDragonBall;

    List<Integer> listMap = Arrays.asList(53, 58, 59, 60, 61, 62, 55, 56, 54, 57);
    private int currentIndexMap = -1;
    private List<Boss> bossDoanhTrai;

    public DoanhTrai(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        this.bossDoanhTrai = new ArrayList<>();
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : this.zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public void openDoanhTrai(Player player) {
        this.lastTimeOpen = System.currentTimeMillis();
        this.clan = player.clan;
        player.clan.doanhTrai = this;
        player.clan.doanhTrai_playerOpen = player.name;
        player.clan.doanhTrai_lastTimeOpen = this.lastTimeOpen;
        //Khởi tạo quái, boss
//        this.init();
        //Đưa thành viên vào doanh trại
        for (Player pl : player.clan.membersInGame) {
            if (pl == null || pl.zone == null) {
                continue;
            }

            ItemTimeService.gI().sendTextDoanhTrai(pl);
            if (player.zone.equals(pl.zone)) {
                ChangeMapService.gI().changeMapInYard(pl, 53, -1, 60);
            }

        }
    }

    public void init() {
        long totalDame = 0;
        long totalHp = 0;
        for (Player pl : this.clan.membersInGame) {
            totalDame += pl.nPoint.dame;
            totalHp += pl.nPoint.hpMax;
        }

        //Hồi sinh quái và thả boss
        for (Zone zone : this.zones) {
            if (zone.map.mapId == this.listMap.get(this.currentIndexMap)) {
                long newDameMob = (totalHp /15);
                long newHpMob = totalDame * 20;//(totalDame * 20);
                for (Mob mob : zone.mobs) {
                    mob.point.dame = (int) (newDameMob);
                    mob.point.maxHp = (int) (newHpMob);
                    mob.hoiSinh();
                }
                int idBoss = this.getIdBoss(zone.map.mapId);
                if (idBoss != -1) {
                    if (idBoss != 1111111111) {
                        bossDoanhTrai.add(BossManager.gI().createBossDoanhTrai(idBoss, (int) (newDameMob * 3 > 2_000_000_000 ? 2_000_000_000 : newDameMob * 3), (int) (newHpMob * 3 > 2_000_000_000 ? 2_000_000_000 : newHpMob * 3), zone));
                    } else { // là vệ sĩ
                        for (Byte i = 0; i < 4; i++) {
                            bossDoanhTrai.add(BossManager.gI().createBossDoanhTrai(BossID.ROBOT_VE_SI1 + i, (int) (newDameMob * 4 > 2_000_000_000 ? 2_000_000_000 : newDameMob * 4), (int) (newHpMob * 4 > 2_000_000_000 ? 2_000_000_000 : newHpMob * 4), zone));
                        }
                    }
                }
            }
        }

    }

    private int getIdBoss(int mapId) {
        switch (mapId) {
            case 59:
                return BossID.TRUNG_UY_TRANG;
            case 62:
                return BossID.TRUNG_UY_XANH_LO;
            case 55:
                return BossID.TRUNG_UY_THEP;
            case 54:
                return BossID.NINJA_AO_TIM;
            case 57:
                return 1111111111; // check vệ sĩ
            default:
                return -1;
        }
    }

    public void DropNgocRong() {
        for (Zone zone : zones) {
            ItemMap itemMap = null;

            switch (zone.map.mapId) {
                case 53:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 917, 384, -1);
                    itemMap.isDoanhTraiBall = true;
                    break;
                case 58:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 658, 336, -1);
                    itemMap.isDoanhTraiBall = true;
                    break;
                case 59:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 675, 240, -1);
                    itemMap.isDoanhTraiBall = true;
                    break;
                case 60:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, Util.nextInt(725, 1241), 384, -1);
                    itemMap.isDoanhTraiBall = true;
                    break;
                case 61:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 789, 264, -1);
                    itemMap.isDoanhTraiBall = true;
                    break;
                case 62:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, Util.nextInt(197, 1294), 384, -1);
                    itemMap.isDoanhTraiBall = true;
                    break;
                case 55:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 422, 288, -1);
                    itemMap.isDoanhTraiBall = true;
                    break;
                case 56:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 789, 312, -1);
                    itemMap.isDoanhTraiBall = true;
                    break;
                case 54:
                    itemMap = new ItemMap(zone, Util.nextInt(16, 20), 1, 211, 1228, -1);
                    itemMap.isDoanhTraiBall = true;
                    break;
            }
        }

    }

    public void dispose() {
        for (Boss b : bossDoanhTrai) {
            if (b != null) {
                b.changeStatus(BossStatus.LEAVE_MAP);
                b = null;
            }

        }
        this.clan = null;
        this.bossDoanhTrai.clear();
        timePickDragonBall = false;
        currentIndexMap = -1;
        bossDoanhTrai.clear();
    }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Khánh đê zéc
 */
