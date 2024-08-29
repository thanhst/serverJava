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
import java.util.logging.Level;

/**
 *
 * @author Khánh Đẹp Zoai
 */
public class NinjaAoTim extends TrungUyTrang {

    protected int numPhanThan;

    public NinjaAoTim(int dame, int hp, Zone zone) throws Exception {
        super(dame, hp, zone, BossID.NINJA_AO_TIM, NINJA);
        numPhanThan = 0;
    }
    

    private static final BossData NINJA = new BossData(
            "Ninja Áo Tím", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{123, 124, 125, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            500, //dame
            new int[]{500}, //hp
            new int[]{1}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
//                {Skill.THAI_DUONG_HA_SAN, 4, 15000},
                {Skill.KAMEJOKO, 7, 3000}},
            new String[]{}, //text chat 1
            new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                "|-1|Ha ha ha! Mắt mày mù à? Nhìn máy đo chỉ số đi!!",
                "|-1|Định chạy trốn hả, hử",
                "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Hahaha mày đây rồi",
                "|-1|Tao đã có lệnh từ đại ca rồi"
            }, //text chat 2
            new String[]{"|-2|Đẹp trai nó phải thế"}, //text chat 3
            5 //second rest
    );

   @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal, 1015);
//            ChangeMapService.gI().changeMap(this, zoneFinal, 1015, 312);
            this.zone.isTrungUyTrangAlive = true;
        }
    }
    
    public void joinMapByZone(Zone zone, int x) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, x);
        }
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
            if (this.playerTarger.location.x < 300 || this.playerTarger.location.x > 1405) {
                this.playerTarger = null;
                this.lastTimeTargetPlayer = System.currentTimeMillis();
                this.timeTargetPlayer = Util.nextInt(1000, 2000);
            }
        }
        return this.playerTarger;
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }
    private long lastTimeBlame;

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(700, 1000)) {
                if (System.currentTimeMillis() - lastTimeBlame > 3000) {
                    this.chat("Xí hụt lêu lêu");
                }
                lastTimeBlame = System.currentTimeMillis();
                return 0;
            }
            if (this.numPhanThan > 0) {
                damage /= 2;
            }
            damage /= 5;
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
            try {
                if (System.currentTimeMillis() - lastTimePhanThan > 45000) {
                    if (this.numPhanThan <= 0 && this.nPoint.hp < this.nPoint.hpMax / 2) {
                        this.chat("Phân thân chi thuật bùm bùm bùm");
                        this.PhanThanChiThuat();
                        lastTimePhanThan = System.currentTimeMillis();
                    }
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(NinjaAoTim.class.getName()).log(Level.SEVERE, null, ex);
            }
            return damage;
        } else {
            return 0;
        }
    }

    private long lastTimePhanThan;

    void PhanThanChiThuat() throws Exception {
        this.numPhanThan = Util.nextInt(5, 10);
        for (Byte i = 0; i < this.numPhanThan; i++) {
            this.createPhanThan(BossID.NINJA_AO_TIM_CLONE + i);
        }
    }

    private Boss createPhanThan(int idBoss) throws Exception {
        return new NinjaClone(this, this.location.x, this.location.y, idBoss, this.nPoint.getDameAttack(false) / 4, this.nPoint.hpMax / 10, this.zone);
    }
}
