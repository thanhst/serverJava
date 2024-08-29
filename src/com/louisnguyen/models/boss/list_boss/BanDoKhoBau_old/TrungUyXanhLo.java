package com.louisnguyen.models.boss.list_boss.BanDoKhoBau_old;

import com.louisnguyen.models.boss.BossData;
import com.louisnguyen.models.boss.BossManager;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.consts.ConstPlayer;
//import com.louisnguyen.models.map.BanDoKhoBau_old.BanDoKhoBau;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.models.skill.Skill;
import com.louisnguyen.services.Service;
import com.louisnguyen.utils.Util;


public class TrungUyXanhLo extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public TrungUyXanhLo(Zone zone , int level, int dame, int hp) throws Exception {
        super(-555, new BossData(
                "Trung úy xanh lơ",
                ConstPlayer.TRAI_DAT,
                new short[]{135, 136, 137, -1, -1, -1},
                ((10000 + dame) * level),
                new int[]{((500000 + hp) * level)},
                new int[]{103},
                (int[][]) Util.addArray(FULL_DEMON),
                new String[]{},
                new String[]{"|-1|Nhóc con"},
                new String[]{},
                600000000
        ));
        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 19, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }
    @Override
    public void active() {
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}