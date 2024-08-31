package com.louisnguyen.models.boss.list_boss.nappa;

import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.Service;
import com.louisnguyen.utils.Util;

/**
 *
 * @Stole By Louis Nguyễn
 */
public class MapDauDinh extends Boss {

    public MapDauDinh() throws Exception {
        super(BossID.MAP_DAU_DINH, BossesData.MAP_DAU_DINH);
    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        this.SendLaiThongBao(2);
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    @Override
    public void reward(Player plKill){
        super.reward(plKill);
        if (Util.isTrue(50, 100)) {
            ItemMap ngocrong3s = new ItemMap(this.zone, 16, Util.nextInt(1,2), this.location.x - 20,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            // ngocrong3s.options.add(new Item.ItemOption(30, 1));
            // ngocrong3s.options.add(new Item.ItemOption(86, 1));
            Service.getInstance().dropItemMap(this.zone, ngocrong3s);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap vang = new ItemMap(this.zone, 457, Util.nextInt(1,5), this.location.x + 20,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, vang);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap hn = new ItemMap(this.zone, 722, Util.nextInt(1,2), this.location.x + 60,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, hn);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap manhvbtt = new ItemMap(this.zone, 933, Util.nextInt(1,10), this.location.x + 60,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, manhvbtt);
        }
    }
    private long st;
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
