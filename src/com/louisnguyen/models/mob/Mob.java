package com.louisnguyen.models.mob;

import com.louisnguyen.services.TaskService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.ItemMapService;
import com.louisnguyen.consts.ConstMap;
import com.louisnguyen.consts.ConstMob;
import com.louisnguyen.consts.ConstTask;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.ItemMap;

import java.util.List;

import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.player.Location;
import com.louisnguyen.models.player.Pet;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.models.reward.ItemMobReward;
import com.louisnguyen.models.reward.MobReward;
import com.girlkun.network.io.Message;
import com.louisnguyen.server.Maintenance;
import com.louisnguyen.server.Manager;
import com.louisnguyen.server.ServerManager;
import com.louisnguyen.services.MapService;
import com.louisnguyen.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;
    private int action = 0;
    public short cx;
    public short cy;
    public int Gio;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    public static void initMobBanDoKhoBau(Mob mob, byte level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public static void initMobKhiGaHuyDiet(Mob mob, byte level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public static void initMobConDuongRanDoc(Mob mob, byte level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public static void hoiSinhMob(Mob mob) {
        mob.point.hp = mob.point.maxHp;
        mob.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(mob.id);
            msg.writer().writeByte(mob.tempId);
            msg.writer().writeByte(0); //level mob
            msg.writer().writeInt((mob.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }

    public synchronized void injured(Player plAtt, int damage, boolean dieWhenHpFull) {
        try {
            if (!this.isDie()) {
                if (damage >= this.point.hp) {
                    damage = this.point.hp;
                }
                if (!dieWhenHpFull) {
                    if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                        damage = this.point.hp - 1;
                    }
                    if (this.tempId == 0 && damage > 10) {
                        damage = 10;
                    }
                    if (this.tempId == 77 && this.isDie()) {
                        this.hoiSinh();
                    }
                    if (MapService.gI().isVungDat01(plAtt.zone.map.mapId) && plAtt.isPl() && !plAtt.isPet) {
                        damage = 0;
                    }
                }
                this.point.hp -= damage;
                if (this.isDie()) {
                    this.status = 0;
                    this.sendMobDieAffterAttacked(plAtt, damage);
                    TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                    TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                    this.lastTimeDie = System.currentTimeMillis();
                } else {
                    this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? plAtt.nPoint.isCrit : false);
                }
                if (plAtt != null && plAtt.nPoint.power < 210999999999L) {
                    Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
                }else {
                    Service.gI().addSMTN(plAtt, (byte) 2, 0, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BigbossAttack() {
        if (!isDie() && !this.effectSkill.isHaveEffectSkill() && Util.canDoWithTime(lastTimeAttackPlayer, 300)) {
            Message msg = null;
            try {
                switch (this.tempId) {
                    case 71: // Vua Bạch Tuộc
                        int[] idAction2 = new int[]{3, 4, 5};
                        action = action == 7 ? 0 : idAction2[Util.nextInt(0, idAction2.length - 1)];
                        int index2 = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player2 = zone.getPlayers().get(index2);
                        if (player2 == null || player2.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 5) {
                            if (action != 5) {
                                msg.writer().writeByte(1);
                                int dame = player2.clan.banDoKhoBau.level * 3250 * 18 * 4;
                                msg.writer().writeInt((int) player2.id);
                                msg.writer().writeInt(dame);
                            }
                            if (action == 5) {
                                cx = (short) player2.location.x;
                                msg.writer().writeShort(cx);
                            }
                        } else {

                        }
                        Service.gI().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                    case 72: // Rôbốt bảo vệ
                        int[] idAction3 = new int[]{0, 1, 2};
                        action = action == 7 ? 0 : idAction3[Util.nextInt(0, idAction3.length - 1)];
                        int index3 = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player3 = zone.getPlayers().get(index3);
                        if (player3 == null || player3.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 2) {
                            msg.writer().writeByte(1);
                            int dame = player3.clan.banDoKhoBau.level * 3250 * 18 * 4;
                            msg.writer().writeInt((int) player3.id);
                            msg.writer().writeInt(dame);
                        }
                        Service.gI().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                }
            } catch (Exception e) {

            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }
            }
        }
    }

    public long getTiemNangForPlayer(Player pl, long dame) {
        int levelPlayer = Service.gI().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = dame * 100 / point.getHpFull();
        long tiemNang = pDameHit * maxTiemNang / 100;
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang = (int) pl.nPoint.calSucManhTiemNang(tiemNang);
        if (pl.nPoint.power >= 210999999999L) {
            tiemNang = 0;
        }
        if (pl.zone.map.mapId == 122 || pl.zone.map.mapId == 123 || pl.zone.map.mapId == 124) {
            tiemNang *= 2;
        }if (pl.zone.map.mapId == 189 || pl.zone.map.mapId == 190 || pl.zone.map.mapId == 191) {
            tiemNang = 0;
        }
        return tiemNang;
    }

    public void update() {
        if (isDie() && (this.tempId == 71 || this.tempId == 72)) {
            Service.gI().sendBigBoss(zone, this.id == 71 ? 7 : 6, 0, -1, -1);
            return;
        }
        if (this.tempId == 71) {
            try {
                Message msg = new Message(102);
                msg.writer().writeByte(5);
                msg.writer().writeShort(this.zone.getPlayers().get(0).location.x);
                Service.gI().sendMessAllPlayerInMap(zone, msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
        if (this.isDie() && !Maintenance.isRuning) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    break;
                case ConstMap.MAP_KHI_GA_HUY_DIET:
                    break;
                case ConstMap.MAP_CON_DUONG_RAN_DOC:
                    break;
                case ConstMap.MAP_GIAI_CUU_MI_NUONG:
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 5000)) {
                        if (this.tempId == 77) {
                            long currentTime = System.currentTimeMillis();
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(currentTime);
                            cal.set(Calendar.HOUR_OF_DAY, 20); // Đặt giờ hồi sinh là 20:00
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            long respawnTime = cal.getTimeInMillis();

                            // Kiểm tra nếu đã đến thời gian hồi sinh
                            if (currentTime >= respawnTime) {
                                this.sendMobHoiSinh();
                            }
                        } else {
                            this.hoiSinh();
                            this.sendMobHoiSinh();
                        }
                    }
            }
        }
        if (this.tempId >= 70 && this.tempId <= 72) {
            BigbossAttack();
        }
        effectSkill.update();
        attackPlayer();
    }

    private void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && !(tempId == 82) && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
            Player pl = getPlayerCanAttack();
            if (pl != null) {
                this.mobAttackPlayer(pl);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    private Player getPlayerCanAttack() {
        int distance = 100;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isNewPet && !pl.name.equals("Jajirô") && !pl.isBoss && !pl.effectSkin.isVoHinh) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    //**************************************************************************
    private void mobAttackPlayer(Player player) {
        int dameMob = this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        int dame = player.injured(null, dameMob, false, true);
        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
    }

    private void sendMobAttackMe(Player player, int dame) {
        if (!player.isPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt(dame); //dame
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeInt(player.nPoint.hp);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    public void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeInt(this.point.hp);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //**************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, int dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (Exception e) {
        }
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (!player.isPet && !player.isNewPet) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    }
                }
            }
        } else if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
            for (ItemMap item : items) {
                if (item.itemTemplate.id != 590) {
                    ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                }
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {

        List<ItemMap> itemReward = new ArrayList<>();
        try {
            //if ((!player.isPet && player.getSession().actived && player.setClothes.setDHD == 5) || (player.isPet && ((Pet) player).master.getSession().actived && ((Pet) player).setClothes.setDHD == 5)) {
            //byte random = 1;
            //if (Util.isTrue(5, 100)) {
            //random = 2;
            //}
            //Item i = Manager.RUBY_REWARDS.get(Util.nextInt(0, Manager.RUBY_REWARDS.size() - 1));
            //i.quantity = random;
            //InventoryServiceNew.gI().addItemBag(player, i);
            //InventoryServiceNew.gI().sendItemBags(player);
            //Service.gI().sendThongBao(player, "Bạn vừa nhận được " + random + " hồng ngọc");
            //}

            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            msg.writer().writeByte(itemReward.size()); //sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemReward;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();

        MobReward mobReward = Manager.MOB_REWARDS.get(this.tempId);
        if (mobReward == null) {
            return list;
        }
        List<ItemMobReward> items = mobReward.getItemReward();
        List<ItemMobReward> golds = mobReward.getGoldReward();
        if (!items.isEmpty()) {
            ItemMobReward item = items.get(Util.nextInt(0, items.size() - 1));
            ItemMap itemMap = item.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        if (!golds.isEmpty()) {
            ItemMobReward gold = golds.get(Util.nextInt(0, golds.size() - 1));
            ItemMap itemMap = gold.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Gio = calendar.get(Calendar.HOUR_OF_DAY);
        if ( ( zone.map.mapId == 0 || zone.map.mapId == 7 || zone.map.mapId == 14 ) && Util.isTrue(100, 100) && this.tempId == 0 && (Gio == 2 || Gio == 4 || Gio == 6 || Gio == 8 || Gio == 10 || Gio == 12 || Gio == 14 || Gio == 16 || Gio == 18 || Gio == 20 || Gio == 22 || Gio == 24) && player.cFlag == 8) {
            list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
            list.add(new ItemMap(zone, 590, 1, x - 15, player.location.y, player.id));
            list.add(new ItemMap(zone, 590, 1, x + 15, player.location.y, player.id));
        }
        
        if ((zone.map.mapId >= 135 && zone.map.mapId <= 138) && Util.isTrue(100, 100)) {
            if (player.clan.banDoKhoBau.level <= 10) {
                int min = 1000;
                int max = 1700;
                Random random = new Random();
                int randomvang = random.nextInt(max - min + 1) + min;
                 int randomvang2 = random.nextInt(max - min + 1) + min;
                        
                for (int i = 0; i < player.clan.banDoKhoBau.level/2; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang,this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.banDoKhoBau.level/3; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level/4; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.banDoKhoBau.level/4; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it2);
                }
            }
            if (player.clan.banDoKhoBau.level > 10 && player.clan.banDoKhoBau.level <= 50) {
                int min = 1200;
                int max = 2000;
                Random random = new Random();
                int randomvang = random.nextInt(max - min + 1) + min;
                int randomvang2 = random.nextInt(max - min + 1) + min;
                        
                for (int i = 0; i < player.clan.banDoKhoBau.level*(3/5); i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang,this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.banDoKhoBau.level/2; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level/3; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.banDoKhoBau.level/3; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it2);
                }
            } else if(player.clan.banDoKhoBau.level > 50 && player.clan.banDoKhoBau.level <= 80) {
                int min = 3000;
                int max = 3500;
                int minx = 42;
                int maxx = 1165;
                Random random = new Random();
                int randomvang2 = random.nextInt(max - min + 1) + min;
//                int randomtoado = ;
                for (int i = 0; i < player.clan.banDoKhoBau.level/4; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                    
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level/4; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level/6; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.banDoKhoBau.level/6; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it2);
                }
            }else {
                int min = 3500;
                int max = 5500;
                int minx = 42;
                int maxx = 1165;
                Random random = new Random();
                int randomvang2 = random.nextInt(max - min + 1) + min;
//                int randomtoado = ;
                for (int i = 0; i < player.clan.banDoKhoBau.level/3; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                    
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level/3; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.banDoKhoBau.level/6; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.banDoKhoBau.level/6; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it2);
                }
            }
        }
        
        
        if (player.itemTime.isUseMayDo && Util.isTrue(21, 100) && this.tempId > 57 && this.tempId < 66) {
            list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
        }// vat phẩm rơi khi user maaáy dò adu hoa r o day ti code choa
        if (player.itemTime.isUseMayDo2 && Util.isTrue(7, 100) && this.tempId > 1 && this.tempId < 81) {
//            list.add(new ItemMap(zone, 2036, 1, x, player.location.y, player.id));// cai nay sua sau nha
        }
        if (player.setClothes.setthucan == 5 && Util.isTrue(100, 100) && MapService.gI().isMapCold(this.zone.map)) {
            list.add(new ItemMap(zone, Util.nextInt(663, 667), 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setspl == 1 && Util.isTrue(25, 100) && this.tempId > 0 && this.tempId < 81) {
            list.add(new ItemMap(zone, 441, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setspl == 1 && Util.isTrue(24, 100) && this.tempId > 0 && this.tempId < 81) {
            list.add(new ItemMap(zone, 442, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setspl == 1 && Util.isTrue(26, 100) && this.tempId > 0 && this.tempId < 81) {
            list.add(new ItemMap(zone, 443, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setspl == 1 && Util.isTrue(23, 100) && this.tempId > 0 && this.tempId < 81) {
            list.add(new ItemMap(zone, 444, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setspl == 1 && Util.isTrue(25, 100) && this.tempId > 0 && this.tempId < 81) {
            list.add(new ItemMap(zone, 445, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setspl == 1 && Util.isTrue(24, 100) && this.tempId > 0 && this.tempId < 81) {
            list.add(new ItemMap(zone, 446, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setspl == 1 && Util.isTrue(26, 100) && this.tempId > 0 && this.tempId < 81) {
            list.add(new ItemMap(zone, 447, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setts == 5 && Util.isTrue(20, 100) && this.tempId > 79 && this.tempId < 81) {
            list.add(new ItemMap(zone, 1066, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setts == 5 && Util.isTrue(21, 100) && this.tempId > 79 && this.tempId < 81) {
            list.add(new ItemMap(zone, 1067, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setts == 5 && Util.isTrue(22, 100) && this.tempId > 79 && this.tempId < 81) {
            list.add(new ItemMap(zone, 1068, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setts == 5 && Util.isTrue(19, 100) && this.tempId > 79 && this.tempId < 81) {
            list.add(new ItemMap(zone, 1069, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
        if (player.setClothes.setts == 5 && Util.isTrue(18, 100) && this.tempId > 79 && this.tempId < 81) {
            list.add(new ItemMap(zone, 1070, 1, x, player.location.y, player.id));// cai nay sua sau nha

        }
//        if (player.isPet && player.getSession().actived && Util.isTrue(15, 100)) {
//            list.add(new ItemMap(zone, 610, 1, x, player.location.y, player.id));
//        }
        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        switch (this.tempId) {
            case ConstMob.THAN_LAN_ME:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_9_1) {
                    itemMap = new ItemMap(this.zone, 20, 1, this.location.x, this.location.y, player.id);
                }
                break;
            case ConstMob.HEO_XAYDA_ME:
            case ConstMob.OC_MUON_HON:
            case ConstMob.OC_SEN:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_15_1) {
                    itemMap = new ItemMap(this.zone, 85, 1, this.location.x, this.location.y, player.id);
                }
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

    private void sendMobStillAliveAffterAttacked(int dameHit, boolean crit) {
        Message msg;
        try {
            msg = new Message(-9);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(this.point.gethp());
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(crit); // chí mạng
            msg.writer().writeInt(-1);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

}
