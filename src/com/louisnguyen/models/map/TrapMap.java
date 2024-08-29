package com.louisnguyen.models.map;

import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.PlayerService;
import com.louisnguyen.services.func.EffectMapService;
import com.louisnguyen.utils.Util;


public class TrapMap {

    public int x;
    public int y;
    public int w;
    public int h;
    public int effectId;
    public int dame;

    public void doPlayer(Player player) {
        switch (this.effectId) {
            case 49:
    if (!player.isDie() && Util.canDoWithTime(player.iDMark.getLastTimeAnXienTrapBDKB(), 1000) && !player.isBoss) {
        int increasedDame = dame * 2; // Tăng lượng dame lên 100%
        player.injured(null, increasedDame + (Util.nextInt(-10, 10) * increasedDame / 100), false, false);
        PlayerService.gI().sendInfoHp(player);
        EffectMapService.gI().sendEffectMapToAllInMap(player.zone, effectId, 2, 1, player.location.x - 32, 1040, 1);
        player.iDMark.setLastTimeAnXienTrapBDKB(System.currentTimeMillis());
    }
    break;
        }
    }

}
