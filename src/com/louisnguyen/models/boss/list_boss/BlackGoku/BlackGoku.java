package com.louisnguyen.models.boss.list_boss.BlackGoku;

import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossesData;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.server.ServerNotify;
import com.louisnguyen.services.EffectSkillService;
import com.louisnguyen.services.PlayerService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.TaskService;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.Util;

import java.util.Random;


public class BlackGoku extends Boss {


    public BlackGoku() throws Exception {
        super(BossID.BLACK_GOKU, BossesData.BLACK_GOKU, BossesData.SUPER_BLACK_GOKU_2);
    }

    @Override
    public void reward(Player plKill) {
        if(Util.isTrue(50,100)){
        ItemMap it = new ItemMap(this.zone, 992, 1, this.location.x, this.location.y, plKill.id);
        Service.gI().dropItemMap(this.zone, it);
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
