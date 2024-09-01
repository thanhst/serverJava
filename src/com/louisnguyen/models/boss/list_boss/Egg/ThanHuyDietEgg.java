package com.louisnguyen.models.boss.list_boss.Egg;

import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.Util;

public class ThanHuyDietEgg extends Boss {

    public ThanHuyDietEgg() throws Exception {
        super(9999, BossesData.THAN_HUY_DIET_Egg);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(60, 100)) {
            ItemMap it = new ItemMap(this.zone, 2028, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
    }

    @Override
    public void active() {
        super.active(); // To change body of generated methods, choose Tools | Templates.
        // if (Util.canDoWithTime(st, 1500)) {
        // this.changeStatus(BossStatus.LEAVE_MAP);
        // }
        this.SendLaiThongBao(13);
    }

    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);
            this.notifyJoinMap();
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            if (this.currentLevel == 0) {
                if (this.parentBoss == null) {
                    ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, 394);
                } else {
                    ChangeMapService.gI().changeMapBySpaceShip(this, this.zone,
                            394);
                }
                this.wakeupAnotherBossWhenAppear();
            } else {
                ChangeMapService.gI().changeMap(this, this.zone, 394, 336);
            }
            Service.gI().sendFlagBag(this);
            this.notifyJoinMap();
        }
        st = System.currentTimeMillis();
    }

    private long st;

    // @Override
    // public void doneChatS() {
    // if (this.bossAppearTogether == null ||
    // this.bossAppearTogether[this.currentLevel] == null) {
    // return;
    // }
    // for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
    // if(boss.id == BossID.POC && !boss.isDie()){
    // boss.changeToTypePK();
    // break;
    // }
    // }
    // }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
