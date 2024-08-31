package com.louisnguyen.models.boss.list_boss.BrolyTraiDat;

import java.util.Random;

import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.Util;

public class SuperGodTd extends Boss {
    public SuperGodTd() throws Exception {
        super(2055, BossesData.godTd, BossesData.SuperGodTD);
    }

    @Override
    public void reward(Player plKill) {
        if (this.name.equals("Thần Trái Đất nguyên thủy")) {
            if (Util.isTrue(100, 100)) {
                ItemMap caitrangBrolyNamec = new ItemMap(this.zone, 2056, 1, this.location.x,
                        this.zone.map.yPhysicInTop(this.location.x,
                                this.location.y - 24),
                        plKill.id);
                caitrangBrolyNamec.options.add(new Item.ItemOption(50, 23));
                caitrangBrolyNamec.options.add(new Item.ItemOption(77, 21));
                caitrangBrolyNamec.options.add(new Item.ItemOption(103, 21));
                caitrangBrolyNamec.options.add(new Item.ItemOption(159, 5));
                caitrangBrolyNamec.options.add(new Item.ItemOption(160, 50));
                caitrangBrolyNamec.options.add(new Item.ItemOption(93, new Random().nextInt(3) + 4));
                Service.getInstance().dropItemMap(this.zone, caitrangBrolyNamec);
            }
            if (Util.isTrue(5, 100)) {
                ItemMap caitrangBrolyNamec2 = new ItemMap(this.zone, 2056, 1, this.location.x,
                        this.zone.map.yPhysicInTop(this.location.x,
                                this.location.y - 24),
                        plKill.id);
                caitrangBrolyNamec2.options.add(new Item.ItemOption(50, 23));
                caitrangBrolyNamec2.options.add(new Item.ItemOption(77, 21));
                caitrangBrolyNamec2.options.add(new Item.ItemOption(103, 21));
                caitrangBrolyNamec2.options.add(new Item.ItemOption(159, 5));
                caitrangBrolyNamec2.options.add(new Item.ItemOption(160, 50));
                Service.getInstance().dropItemMap(this.zone, caitrangBrolyNamec2);
            }
        } else {
            if (Util.isTrue(100, 100)) {
                ItemMap caitrangBrolyNamec = new ItemMap(this.zone, 2057, 1, this.location.x,
                        this.zone.map.yPhysicInTop(this.location.x,
                                this.location.y - 24),
                        plKill.id);
                caitrangBrolyNamec.options.add(new Item.ItemOption(50, Util.nextInt(10, 30)));
                caitrangBrolyNamec.options.add(new Item.ItemOption(77, Util.nextInt(10, 30)));
                caitrangBrolyNamec.options.add(new Item.ItemOption(103, Util.nextInt(10, 30)));
                caitrangBrolyNamec.options.add(new Item.ItemOption(5, Util.nextInt(10, 100)));
                caitrangBrolyNamec.options.add(new Item.ItemOption(159, 4));
                caitrangBrolyNamec.options.add(new Item.ItemOption(230, 4));
                caitrangBrolyNamec.options.add(new Item.ItemOption(231, 4));
                caitrangBrolyNamec.options.add(new Item.ItemOption(106));
                caitrangBrolyNamec.options.add(new Item.ItemOption(116));
                caitrangBrolyNamec.options.add(new Item.ItemOption(98, 100));
                Service.getInstance().dropItemMap(this.zone, caitrangBrolyNamec);
            }
            // if (Util.isTrue(5, 100)) {
            // ItemMap caitrangBrolyNamec2 = new ItemMap(this.zone, 2057, 1,
            // this.location.x,
            // this.zone.map.yPhysicInTop(this.location.x,
            // this.location.y - 24),
            // plKill.id);
            // caitrangBrolyNamec2.options.add(new Item.ItemOption(50, 23));
            // caitrangBrolyNamec2.options.add(new Item.ItemOption(77, 21));
            // caitrangBrolyNamec2.options.add(new Item.ItemOption(103, 21));
            // caitrangBrolyNamec2.options.add(new Item.ItemOption(227, 4));
            // caitrangBrolyNamec2.options.add(new Item.ItemOption(160, 50));
            // Service.getInstance().dropItemMap(this.zone, caitrangBrolyNamec2);
            // }
        }
        if (Util.isTrue(100, 100)) {
            ItemMap ngocrong3s = new ItemMap(this.zone, 16, 10, this.location.x - 20,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            ngocrong3s.options.add(new Item.ItemOption(30, 1));
            ngocrong3s.options.add(new Item.ItemOption(86, 1));
            Service.getInstance().dropItemMap(this.zone, ngocrong3s);
        }
        if (Util.isTrue(70, 100)) {
            ItemMap ngocrong2s = new ItemMap(this.zone, 15, 1, this.location.x - 40,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            ngocrong2s.options.add(new Item.ItemOption(30, 1));
            ngocrong2s.options.add(new Item.ItemOption(86, 1));
            Service.getInstance().dropItemMap(this.zone, ngocrong2s);
        }
        if (Util.isTrue(25, 100)) {
            ItemMap ngocrong1s = new ItemMap(this.zone, 14, 1, this.location.x - 80,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            ngocrong1s.options.add(new Item.ItemOption(30, 1));
            ngocrong1s.options.add(new Item.ItemOption(86, 1));
            Service.getInstance().dropItemMap(this.zone, ngocrong1s);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap vang = new ItemMap(this.zone, 457, 10, this.location.x + 20,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, vang);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap vang2 = new ItemMap(this.zone, 457, 10, this.location.x + 40,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, vang2);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap hn = new ItemMap(this.zone, 722, 10, this.location.x + 60,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, hn);
        }
        if (Util.isTrue(100, 100)) {
            ItemMap hn1 = new ItemMap(this.zone, 722, 10, this.location.x + 80,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.getInstance().dropItemMap(this.zone, hn1);
        }
    }

    @Override
    public void active() {
        super.active(); // To change body of generated methods, choose Tools | Templates.
        // if (Util.canDoWithTime(st, 1500)) {
        // this.changeStatus(BossStatus.LEAVE_MAP);
        // }
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
}
