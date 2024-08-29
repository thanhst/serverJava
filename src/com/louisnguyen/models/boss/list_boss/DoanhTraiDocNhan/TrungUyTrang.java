/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.louisnguyen.models.boss.list_boss.DoanhTraiDocNhan;

import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossData;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossManager;
import com.louisnguyen.models.boss.BossStatus;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.models.skill.Skill;
import com.louisnguyen.services.EffectSkillService;
import com.louisnguyen.services.PlayerService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.SkillService;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.SkillUtil;
import com.louisnguyen.utils.Util;

/**
 *
 * @author Khánh Đẹp Zoai
 */
public class TrungUyTrang extends Boss {

    private int dameClan;
    private int hpClan;

    public TrungUyTrang(int dame, int hp, Zone zone) throws Exception {
        super(BossID.TRUNG_UY_TRANG, TrungUyTrang.TRUNG_UY_TRANG);
        this.dameClan = dame;
        this.hpClan = hp;
        this.zoneFinal = zone;
    }

    public TrungUyTrang(int dame, int hp, Zone zone, int id, BossData... data) throws Exception {
        super(id, data);
        this.dameClan = dame;
        this.hpClan = hp;
        this.zoneFinal = zone;
    }

    private static final BossData TRUNG_UY_TRANG = new BossData(
            "Trung úy trắng", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{141, 142, 143, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            500, //dame
            new int[]{500}, //hp
            new int[]{1}, //map join
            new int[][]{
                {Skill.MASENKO, 3, 1000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{""
            }, //text chat 1
            new String[]{""
            }, //text chat 2
            new String[]{""}, //text chat 3
            5 //second rest
    );

    public void initBase() {
        BossData data = this.data[this.currentLevel];
        this.name = data.getName();
        this.gender = data.getGender();
        this.nPoint.mpg = 23_07_2003;
        this.nPoint.dameg = this.dameClan;
        this.nPoint.hpg = this.hpClan;
        this.nPoint.hp = nPoint.hpg;
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

//    @Override
//    public void joinMap() {
//        if (zoneFinal != null) {
////            int x = Util.nextInt(755, 1069);
////            ChangeMapService.gI().changeMap(this, this.zone, x, this.zone.map.yPhysicInTop(x, 0));
//joinMapByZone(zoneFinal, 897);
//             System.out.println("boss jion map");
//            this.zone.isTrungUyTrangAlive = true;
//        }
//    }
    public void joinMapByZone(Zone zone, int x) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMap(this, this.zone, x, -1);
        }
    }
    
    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal, 897);
//            ChangeMapService.gI().changeMap(this, zoneFinal, 897, 384);
            this.zone.isTrungUyTrangAlive = true;
        }
    }

    @Override
    public void active() {
        this.attack();
    }

    private long lastTimeFindPlayerToChangeFlag;

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeFindPlayerToChangeFlag, 500) && this.typePk == ConstPlayer.NON_PK) {
            if (getPlayerAttack() == null) {
                this.lastTimeFindPlayerToChangeFlag = System.currentTimeMillis();
            } else {
                this.changeToTypePK();
            }
        } else if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }

                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= 100) {
                    if (Util.isTrue(5, 20)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                        }
                    }
                    SkillService.gI().useSkill(this, pl, null,null);
                    checkPlayerDie(pl);
                } else {
                    this.moveToPlayer(pl);
                }
            } catch (Exception ex) {
                Logger.logException(Boss.class, ex);
            }
        }
    }

    @Override
    public Player getPlayerAttack() {
        if (this.playerTarger != null && (this.playerTarger.isDie() || !this.zone.equals(this.playerTarger.zone))) {
            this.playerTarger = null;
        }
        if (this.playerTarger == null || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
            this.playerTarger = this.zone.getRandomPlayerInMap();
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(5000, 7000);
        }
        if (this.playerTarger != null && this.playerTarger.effectSkin != null && this.playerTarger.effectSkin.isVoHinh) {
            this.playerTarger = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(1000, 2000);
        }
        if (this.playerTarger == this.pet) {
            this.playerTarger = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(1000, 2000);
        }
        if (this.playerTarger != null) {
            if (this.playerTarger.location.x < 755 || this.playerTarger.location.x > 1035) {
                this.playerTarger = null;
                this.lastTimeTargetPlayer = System.currentTimeMillis();
                this.timeTargetPlayer = Util.nextInt(1000, 2000);
            }
        }
        return this.playerTarger;
    }

    @Override
    /**
     * Không senđ thông báo bên dưới
     */
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
        }
        this.changeStatus(BossStatus.DIE);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 17, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
    }

    @Override
    public void leaveMap() {
        this.zone.isTrungUyTrangAlive = false;
        super.leaveMap();
        synchronized (this) {
            BossManager.gI().removeBoss(this);
        }
        this.bossStatus = null;
        this.lastZone = null;
        this.playerTarger = null;
        this.bossAppearTogether = null;
        this.parentBoss = null;
        this.zoneFinal = null;
        this.dispose();
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.location.x >= 800 || this.location.x <= 995) {
            byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
            byte move = (byte) Util.nextInt(40, 60);
            PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
        }
    }

    private long lastTimeBlame;

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (this.zone.isbulon14Alive) {
                if (System.currentTimeMillis() - lastTimeBlame > 3000) {
                    this.chat("Ngu lắm đánh bulon trước đi con zai");
                }
                lastTimeBlame = System.currentTimeMillis();
                return 0;
            }
            if (this.zone.isbulon13Alive) {
                if (System.currentTimeMillis() - lastTimeBlame > 3000) {
                    this.chat("Ngu lắm đánh bulon trước đi con zai");
                }
                lastTimeBlame = System.currentTimeMillis();
                return 0;
            }
            if (!piercing && Util.isTrue(300, 1000)) {
                this.chat("Xí hụt lêu lêu");
                return 0;
            }
            damage /= 3;
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

}
