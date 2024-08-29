package com.louisnguyen.models.boss.list_boss.JacyChunVaQuyLao;

import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossStatus;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.TaskService;
import com.louisnguyen.utils.Util;
import java.util.Random;


public class QuyLao extends Boss {

    public QuyLao() throws Exception {
        super(BossID.QUY_LAO, BossesData.QUY_LAO);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap caitrangquylao = new ItemMap(this.zone, 710, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            caitrangquylao.options.add(new Item.ItemOption(50, 22));
            caitrangquylao.options.add(new Item.ItemOption(77, 20));
            caitrangquylao.options.add(new Item.ItemOption(103, 20));
            caitrangquylao.options.add(new Item.ItemOption(159, 3));
            caitrangquylao.options.add(new Item.ItemOption(160, 35));
            caitrangquylao.options.add(new Item.ItemOption(93,  new Random().nextInt(3) + 4));
            Service.getInstance().dropItemMap(this.zone, caitrangquylao);
        }if (Util.isTrue(5, 100)) {
            ItemMap caitrangquylao2 = new ItemMap(this.zone, 710, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            caitrangquylao2.options.add(new Item.ItemOption(50, 22));
            caitrangquylao2.options.add(new Item.ItemOption(77, 20));
            caitrangquylao2.options.add(new Item.ItemOption(103, 20));
            caitrangquylao2.options.add(new Item.ItemOption(159, 3));
            caitrangquylao2.options.add(new Item.ItemOption(160, 35));
            Service.getInstance().dropItemMap(this.zone, caitrangquylao2);
        }if (Util.isTrue(100, 100)) {
            ItemMap ngocrong3s = new ItemMap(this.zone, 16, 1, this.location.x - 20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            ngocrong3s.options.add(new Item.ItemOption(30, 1));
            ngocrong3s.options.add(new Item.ItemOption(86, 1));
            Service.getInstance().dropItemMap(this.zone, ngocrong3s);
        }if (Util.isTrue(25, 100)) {
            ItemMap ngocrong2s = new ItemMap(this.zone, 15, 1, this.location.x - 40, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            ngocrong2s.options.add(new Item.ItemOption(30, 1));
            ngocrong2s.options.add(new Item.ItemOption(86, 1));
            Service.getInstance().dropItemMap(this.zone, ngocrong2s);
        }if (Util.isTrue(25, 100)) {
            ItemMap ngocrong1s = new ItemMap(this.zone, 14, 1, this.location.x - 80, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            ngocrong1s.options.add(new Item.ItemOption(30, 1));
            ngocrong1s.options.add(new Item.ItemOption(86, 1));
            Service.getInstance().dropItemMap(this.zone, ngocrong1s);
        }if (Util.isTrue(100, 100)) {
            ItemMap vang = new ItemMap(this.zone, 190, 30000, this.location.x + 20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, vang);
        }if (Util.isTrue(100, 100)) {
            ItemMap vang2 = new ItemMap(this.zone, 190, 30000, this.location.x + 40, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, vang2);
        }if (Util.isTrue(100, 100)) {
            ItemMap vang3 = new ItemMap(this.zone, 190, 30000, this.location.x + 60, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, vang3);
        }if (Util.isTrue(100, 100)) {
            ItemMap vang4 = new ItemMap(this.zone, 190, 30000, this.location.x + 80, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, vang4);
        }
    }
    
    @Override
    public void doneChatE() {
        if (this.parentBoss == null || this.parentBoss.bossAppearTogether == null
                || this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] == null) {
        }
//        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
//            if (boss.id == BossID.THAN_HUY_DIET && !boss.isDie()) {
//                super.active();
//                break;
//            }
//        }
    }
    
    
    
    
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
//        if(Util.canDoWithTime(st,1500)){
//            super.active();
//        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
