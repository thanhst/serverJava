package com.louisnguyen.models.boss.ConDuongRanDoc;


import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossStatus;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.boss.list_boss.XenBoHung.SieuBoHung;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.EffectSkillService;
import com.louisnguyen.services.PlayerService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.SkillService;
import com.louisnguyen.utils.Util;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Saibamen extends Boss {
    
    private long lastTimeHP;
    private int timeHP;
    private boolean calledNinja;

    public Saibamen() throws Exception {
        super(BossID.SAIBAMEN, BossesData.BROLY_1);
    }

//    public Saibamen(Zone mapById, byte level, int i, int i0) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
    
    @Override
public void active() {
    super.active();
    if(Util.canDoWithTime(st,300000)){
        this.changeStatus(BossStatus.LEAVE_MAP);
    }
    try {
            this.TuSat();
        } catch (Exception ex) {
            Logger.getLogger(SieuBoHung.class.getName()).log(Level.SEVERE, null, ex);
        }
}

private void TuSat() throws Exception {
        if (this.nPoint.hp == 0) {
            
         SkillService.gI().selectSkill(playerTarger, 14);
            
        }
    }
    
    @Override
    public void joinMap() {
        super.joinMap();
        st= System.currentTimeMillis();
    }
    private long st;
    
    @Override
    public void moveToPlayer(Player player) {
        this.moveTo(player.location.x, player.location.y);
    }
    
     @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/2;
            }
            this.nPoint.subHP(damage);
            if (this.nPoint.hp >= 49999999 && !this.calledNinja) {
                try {
//                    new BrolySuper(this.zone, 2, Util.nextInt(1000, 10000), BossID.BROLY_SUPER);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                this.calledNinja = true;
            }
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
