package com.louisnguyen.models.boss.list_boss.MrEvilXayda;

//import com.louisnguyen.models.boss.list_boss.Boss2Hden3H.*;
import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.server.ServerNotify;
import com.louisnguyen.services.EffectSkillService;
import com.louisnguyen.services.PlayerService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.TaskService;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.Util;
import java.util.Calendar;

import java.util.Random;
import java.util.TimeZone;

public class MrEvilXayda extends Boss {

    public MrEvilXayda() throws Exception {
        super(-104, BossesData.CUMBER, BossesData.SUPER_CUMBER);
    }

    @Override
    public void reward(Player plKill) {
            ItemMap it = new ItemMap(this.zone, 1196, 1, this.location.x, this.location.y, plKill.id);
            it.options.add(new Item.ItemOption(86, 1));
            it.options.add(new Item.ItemOption(30, 1));
            Service.gI().dropItemMap(this.zone, it);
        }

    private long lastTimeGK;

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
