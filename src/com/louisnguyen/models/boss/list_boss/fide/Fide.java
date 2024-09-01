package com.louisnguyen.models.boss.list_boss.fide;

import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.TaskService;
import com.louisnguyen.utils.Util;


public class Fide extends Boss {

    public Fide() throws Exception {
        super(BossID.FIDE, BossesData.FIDE_DAI_CA_1, BossesData.FIDE_DAI_CA_2, BossesData.FIDE_DAI_CA_3,BossesData.Fide_den);
    }
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, 17, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);

        if (Util.isTrue(100, 100)) {
            ItemMap ngocrong3s = new ItemMap(this.zone, 16, Util.nextInt(1,5), this.location.x - 20,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            // ngocrong3s.options.add(new Item.ItemOption(30, 1));
            // ngocrong3s.options.add(new Item.ItemOption(86, 1));
            Service.getInstance().dropItemMap(this.zone, ngocrong3s);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap vang = new ItemMap(this.zone, 457, Util.nextInt(1,10), this.location.x + 20,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, vang);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap hn = new ItemMap(this.zone, 722, Util.nextInt(1,3), this.location.x + 60,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, hn);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap manhvbtt = new ItemMap(this.zone, 933, Util.nextInt(1,20), this.location.x + 60,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, manhvbtt);
        }
        if(this.name.equals("Golden Fide")){
            if (Util.isTrue(70, 100)) {
                ItemMap trungUbb = new ItemMap(this.zone, 2027, 1, this.location.x + 60,
                        this.zone.map.yPhysicInTop(this.location.x,
                                this.location.y - 24),
                        plKill.id);
                Service.getInstance().dropItemMap(this.zone, trungUbb);
            }
        }
    }
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        this.SendLaiThongBao(13);
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;

}






















