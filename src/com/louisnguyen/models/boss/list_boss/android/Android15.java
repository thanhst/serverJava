package com.louisnguyen.models.boss.list_boss.android;

import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossStatus;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.PlayerService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.TaskService;
import com.louisnguyen.utils.Util;


public class Android15 extends Boss {

    public boolean callApk13;

    public Android15() throws Exception {
        super(BossID.ANDROID_15, BossesData.ANDROID_15);
    }
  @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{1142, 382, 383, 384, 1142};
        int itemId = itemRan[2];
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 17, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    protected void resetBase() {
        super.resetBase();
        this.callApk13 = false;
    }

    @Override
    public void active() {
        this.attack();
        this.SendLaiThongBao(12);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.callApk13 && damage >= this.nPoint.hp) {
            if (this.parentBoss != null) {
                ((Android14) this.parentBoss).callApk13();
            }
            return 0;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }
}
