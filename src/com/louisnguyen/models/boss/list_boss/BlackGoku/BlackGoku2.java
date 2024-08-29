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
import java.util.Calendar;

import java.util.Random;
import java.util.TimeZone;


public class BlackGoku2 extends Boss {


    public BlackGoku2() throws Exception {
        super( -103, BossesData.SUPER_BLACK_GOKU_2_2H3H, BossesData.ZAMAS);
    }

    @Override
    public void reward(Player plKill) {
        if(Util.isTrue(50,100)){
        ItemMap it = new ItemMap(this.zone, 1142, 1, this.location.x, this.location.y, plKill.id);
        Service.gI().dropItemMap(this.zone, it);
    }else  if(Util.isTrue(5,100)){
         ItemMap it = new ItemMap(this.zone, 16, 1, this.location.x, this.location.y, plKill.id);
        Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
    private long lastTimeGK;
    @Override
    public void active() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
        if(calendar.get(Calendar.HOUR_OF_DAY) > 15){
            leaveMap();
        }
        
    }
  
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
