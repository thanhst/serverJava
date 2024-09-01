package com.louisnguyen.models.boss.list_boss.BlackGoku;

import java.util.Random;

import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.TaskService;
import com.louisnguyen.utils.Util;

public class BlackGoku extends Boss {

    public BlackGoku() throws Exception {
        super(BossID.BLACK_GOKU, BossesData.BLACK_GOKU, BossesData.SUPER_BLACK_GOKU_2);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(50, 100)) {
            ItemMap it = new ItemMap(this.zone, 992, 1, this.location.x, this.location.y, plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        if (Util.isTrue(50, 100)) {
            ItemMap it = new ItemMap(this.zone, 555, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it.options.add(new Item.ItemOption(47, new Random().nextInt(1001) + 900)); // áo từ 1800-2800 giáp
            it.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it2 = new ItemMap(this.zone, 557, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it2.options.add(new Item.ItemOption(47, new Random().nextInt(1001) + 900)); // áo từ 1800-2800 giáp
            it2.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it2.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it2.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it3 = new ItemMap(this.zone, 559, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it3.options.add(new Item.ItemOption(47, new Random().nextInt(1001) + 900)); // áo từ 1800-2800 giáp
            it3.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it3.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it3.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it4 = new ItemMap(this.zone, 556, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it4.options.add(new Item.ItemOption(22, new Random().nextInt(16) + 50)); // hp 85-100k
            it4.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it4.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it4.options.add(new Item.ItemOption(30, 1));// ko the gd

            it.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            ItemMap it5 = new ItemMap(this.zone, 558, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it5.options.add(new Item.ItemOption(22, new Random().nextInt(16) + 50)); // hp 85-100k
            it5.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it5.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it5.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it6 = new ItemMap(this.zone, 560, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it6.options.add(new Item.ItemOption(22, new Random().nextInt(16) + 50)); // hp 85-100k
            it6.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it6.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it6.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it7 = new ItemMap(this.zone, 562, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it7.options.add(new Item.ItemOption(0, new Random().nextInt(150) + 3000)); // 8500-10000
            it7.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it7.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it7.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it8 = new ItemMap(this.zone, 564, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it8.options.add(new Item.ItemOption(0, new Random().nextInt(150) + 3000)); // 8500-10000
            it8.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it8.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it8.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it9 = new ItemMap(this.zone, 566, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it9.options.add(new Item.ItemOption(0, new Random().nextInt(150) + 3000)); // 8500-10000
            it9.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it9.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it9.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it10 = new ItemMap(this.zone, 563, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it10.options.add(new Item.ItemOption(23, new Random().nextInt(11) + 40000)); // ki 80-90k
            it10.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it10.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it10.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it11 = new ItemMap(this.zone, 565, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it11.options.add(new Item.ItemOption(23, new Random().nextInt(11) + 40000)); // ki 80-90k
            it11.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it11.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it11.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it12 = new ItemMap(this.zone, 567, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it12.options.add(new Item.ItemOption(23, new Random().nextInt(11) + 40000)); // ki 80-90k
            it12.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it12.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it12.options.add(new Item.ItemOption(30, 1));// ko the gd

            ItemMap it13 = new ItemMap(this.zone, 561, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it13.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 14)); // áo từ 1800-2800 giáp
            it13.options.add(new Item.ItemOption(86, 1));// Ký gửi vàng
            it13.options.add(new Item.ItemOption(21, 60));// yêu cầu sm 80 tỉ
            // it13.options.add(new Item.ItemOption(30, 1));// ko the gd

            int randomdo = Util.nextInt(1, 14);
            if (randomdo == 1) {
                Service.gI().dropItemMap(this.zone, it);
            }
            if (randomdo == 2) {
                Service.gI().dropItemMap(this.zone, it2);
            }
            if (randomdo == 3) {
                Service.gI().dropItemMap(this.zone, it3);
            }
            if (randomdo == 4) {
                Service.gI().dropItemMap(this.zone, it4);
            }
            if (randomdo == 5) {
                Service.gI().dropItemMap(this.zone, it5);
            }
            if (randomdo == 6) {
                Service.gI().dropItemMap(this.zone, it6);
            }
            if (randomdo == 7) {
                Service.gI().dropItemMap(this.zone, it7);
            }
            if (randomdo == 8) {
                Service.gI().dropItemMap(this.zone, it8);
            }
            if (randomdo == 9) {
                Service.gI().dropItemMap(this.zone, it9);
            }
            if (randomdo == 10) {
                Service.gI().dropItemMap(this.zone, it10);
            }
            if (randomdo == 11) {
                Service.gI().dropItemMap(this.zone, it11);
            }
            if (randomdo == 12) {
                Service.gI().dropItemMap(this.zone, it12);
            }
            if (randomdo == 13) {
                Service.gI().dropItemMap(this.zone, it13);
            }
        }
        if (Util.isTrue(100, 100)) {
            ItemMap vang = new ItemMap(this.zone, 457, Util.nextInt(1, 10), this.location.x + 20,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, vang);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap hn = new ItemMap(this.zone, 722, Util.nextInt(1, 3), this.location.x + 60,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, hn);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    private long lastTimeGK;

    @Override
    public void active() {

        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
        this.SendLaiThongBao(5);
    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
