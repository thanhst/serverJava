package com.louisnguyen.models.npc;

import com.louisnguyen.consts.ConstNpc;
import com.louisnguyen.consts.ConstTask;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.server.Manager;
import com.louisnguyen.services.TaskService;
import java.util.ArrayList;
import java.util.List;

public class NpcManager {

    public static Npc getByIdAndMap(int id, int mapId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == id && npc.mapId == mapId) {
                return npc;
            }
        }
        return null;
    }

    public static Npc getNpc(byte tempId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == tempId) {
                return npc;
            }
        }
        return null;
    }
    
//    public static Npc Autochatnpc() {
//        for (Npc npc : Manager.NPCS) {
//            switch (npc.tempId) {  //id npc
//                case 13:
//                    npc.npcChat("Là lá la, là lá la");
//                    break;
//                case 75:
//                    npc.npcChat("Từ 21h-21h30 các ngươi có 30p để chọn điều ước.");
//                    npc.npcChat("Từ 21h Rồng Vô Cực sẽ xuất hiện tại làng trước nhà");
//                    npc.npcChat("Ta sẽ gọi Rồng Vô Cực Vào lúc 21h thứ 7 hằng tuần.");
//                    break;
//            }
//               
//        }
//        return null;
//    }

    public static List<Npc> getNpcsByMapPlayer(Player player) {
        List<Npc> list = new ArrayList<>();
        if (player.zone != null) {
            for (Npc npc : player.zone.map.npcs) {
                if (npc.tempId == ConstNpc.QUA_TRUNG && player.mabuEgg == null && player.zone.map.mapId == (21 + player.gender)) {
                    continue;
                } else if (npc.tempId == ConstNpc.CALICK && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    continue;
                }
                list.add(npc);
            }
        }
        return list;
    }
}
