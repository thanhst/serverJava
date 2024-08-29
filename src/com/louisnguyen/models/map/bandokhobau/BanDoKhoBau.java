package com.louisnguyen.models.map.bandokhobau;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.louisnguyen.models.boss.list_boss.phoban.TrungUyXanhLoBdkb;
import com.louisnguyen.models.BangHoi.Clan;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossManager;
import com.louisnguyen.models.boss.BossStatus;
import com.louisnguyen.models.boss.list_boss.bandokhobau.TrungUyXanhLoBdkb;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.mob.Mob;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.ItemTimeService;
import com.louisnguyen.services.func.ChangeMapService;

import lombok.Data;

/**
 *
 * @author Khánh Đẹp Zoai
 */
@Data
public class BanDoKhoBau {
    public static final long POWER_CAN_GO_TO_DBKB = 200000000L;

    public static final int N_PLAYER_CLAN = 2;
    public static final int AVAILABLE = 20; // số lượng bdkb trong server
    public static final int TIME_BAN_DO_KHO_BAU = 1_800_000;

    public static final int MAX_HP_MOB = 32_600_000;
    public static final int MIN_HP_MOB = 16_300_000;

    public static final int MAX_DAME_MOB = 2_000_000;
    public static final int MIN_DAME_MOB = 300000;

    public static long getPOWER_CAN_GO_TO_DBKB() {
        return POWER_CAN_GO_TO_DBKB;
    }

    public static int getN_PLAYER_CLAN() {
        return N_PLAYER_CLAN;
    }

    public static int getAVAILABLE() {
        return AVAILABLE;
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

    public void setLevel(int level) {
        this.level = level;
    }

    public void setListMap(List<Integer> listMap) {
        this.listMap = listMap;
    }

    public void setCurrentIndexMap(int currentIndexMap) {
        this.currentIndexMap = currentIndexMap;
    }

    public void setBoss(TrungUyXanhLoBdkb boss) {
        this.boss = boss;
    }

    public void setTimePickReward(boolean timePickReward) {
        this.timePickReward = timePickReward;
    }

    public static int getTIME_BAN_DO_KHO_BAU() {
        return TIME_BAN_DO_KHO_BAU;
    }

    public static int getMAX_HP_MOB() {
        return MAX_HP_MOB;
    }

    public static int getMIN_HP_MOB() {
        return MIN_HP_MOB;
    }

    public static int getMAX_DAME_MOB() {
        return MAX_DAME_MOB;
    }

    public static int getMIN_DAME_MOB() {
        return MIN_DAME_MOB;
    }

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

    public int getLevel() {
        return level;
    }

    public List<Integer> getListMap() {
        return listMap;
    }

    public int getCurrentIndexMap() {
        return currentIndexMap;
    }

    public TrungUyXanhLoBdkb getBoss() {
        return boss;
    }

    public boolean isTimePickReward() {
        return timePickReward;
    }

    private int id;
    private List<Zone> zones;
    private Clan clan;

    private long lastTimeOpen;

    public int level;

    List<Integer> listMap = Arrays.asList(135, 138, 136, 137);
    private int currentIndexMap = -1;

    TrungUyXanhLoBdkb boss;

    public boolean timePickReward;

    public BanDoKhoBau(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : this.zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public void openBDKB(Player player, int level) {
        this.lastTimeOpen = System.currentTimeMillis();
        this.clan = player.clan;
        this.level = level;
        player.clan.banDoKhoBau = this;
        player.clan.banDoKhoBau_playerOpen = player;
        player.clan.banDoKhoBau_lastTimeOpen = this.lastTimeOpen;

        player.bdkb_isJoinBdkb = true;
        player.bdkb_countPerDay++;
        player.bdkb_lastTimeJoin = System.currentTimeMillis();
        ChangeMapService.gI().goToDBKB(player);
        for (Player pl : player.clan.membersInGame) {
            if (pl == null || pl.zone == null) {
                continue;
            }
            ItemTimeService.gI().sendTextBanDoKhoBau(pl);
        }

    }

    public void init() {
        // Hồi sinh quái và thả boss
        for (Zone zone : this.zones) {
            if (zone.map.mapId == this.listMap.get(this.currentIndexMap)) {
                long newHpMob = ((this.level - 1) / (2 - 1)) * (MAX_HP_MOB - MIN_HP_MOB) + MIN_HP_MOB;
                long newDameMob = ((this.level - 1) / (2 - 1)) * (MAX_DAME_MOB - MIN_DAME_MOB) + MIN_DAME_MOB;
                for (Mob mob : zone.mobs) {
                    mob.point.dame = (int) (newDameMob);
                    mob.point.maxHp = (int) (newHpMob);
                    mob.hoiSinh();
                }
                int idBoss = (zone.map.mapId == 137 ? BossID.TRUNG_UY_XANH_LO : -1);
                if (idBoss != -1) {
                    boss = (TrungUyXanhLoBdkb) BossManager.gI().createBossBdkb(idBoss,
                            (int) (newDameMob * 4 > 2_000_000_000 ? 2_000_000_000 : newDameMob * 4),
                            (int) (newHpMob * 5 > 2_000_000_000 ? 2_000_000_000 : newHpMob * 5), zone);
                }
            }
        }
    }

    public void dispose() {
        boss.changeStatus(BossStatus.LEAVE_MAP);
        this.clan = null;
        this.boss = null;
        timePickReward = false;
        currentIndexMap = -1;
    }

}
