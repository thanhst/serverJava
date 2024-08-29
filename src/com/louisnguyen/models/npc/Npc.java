package com.louisnguyen.models.npc;

import com.louisnguyen.consts.ConstNpc;
import com.louisnguyen.models.map.Map;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.server.Manager;
import com.girlkun.network.io.Message;
import com.louisnguyen.services.MapService;
import com.louisnguyen.services.NpcService;
import com.louisnguyen.services.Service;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.Util;


public abstract class Npc implements IAtionNpc {

    public int mapId;
    public Map map;

    public int status;

    public int cx;

    public int cy;

    public int tempId;

    public int avartar;

    public BaseMenu baseMenu;

    protected Npc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        this.map = MapService.gI().getMapById(mapId);
        this.mapId = mapId;
        this.status = status;
        this.cx = cx;
        this.cy = cy;
        this.tempId = tempId;
        this.avartar = avartar;
        Manager.NPCS.add(this);
    }

    public void initBaseMenu(String text) {
        text = text.substring(1);
        String[] data = text.split("\\|");
        baseMenu = new BaseMenu();
        baseMenu.npcId = tempId;
        baseMenu.npcSay = data[0].replaceAll("<>", "\n");
        baseMenu.menuSelect = new String[data.length - 1];
        for (int i = 0; i < baseMenu.menuSelect.length; i++) {
            baseMenu.menuSelect[i] = data[i + 1].replaceAll("<>", "\n");
        }
    }

    public void createOtherMenu(Player player, int indexMenu, String npcSay, String... menuSelect) {
        Message msg;
        try {
            player.iDMark.setIndexMenu(indexMenu);
            msg = new Message(32);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(npcSay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createOtherMenu(Player player, int indexMenu, String npcSay, String[] menuSelect, Object object) {
        NpcFactory.PLAYERID_OBJECT.put(player.id, object);
        Message msg;
        try {
            player.iDMark.setIndexMenu(indexMenu);
            msg = new Message(32);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(npcSay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
            try {
                if (baseMenu != null) {
                    baseMenu.openMenu(player);
                } else {
                    Message msg;
                    msg = new Message(32);
                    msg.writer().writeShort(tempId);
                    NpcService.gI().createTutorial(player, this.avartar, "Chức năng đang được cập nhật !!! " + "[" + mapId + "]" +"[" + this.tempId + "]");
                    msg.writer().writeByte(1);
                    player.sendMessage(msg);
                    msg.cleanup();
                }
            } catch (Exception e) {
                Logger.logException(Npc.class, e);
            }
        }
    }
    
    public void createTutorial(Player player, int avatar, String npcSay) {
        Message msg;
        try {
            msg = new Message(38);
            msg.writer().writeShort(ConstNpc.CON_MEO);
            msg.writer().writeUTF(npcSay);
            if (this.avartar != -1) {
                msg.writer().writeShort(this.avartar);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void npcChat(Player player, String text) {
        Message msg;
        try {
            msg = new Message(124);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(text);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

//    public void npcChat(String text) {
//        Message msg;
//        try {
//            msg = new Message(124);
//            msg.writer().writeShort(tempId);
//            msg.writer().writeUTF(text);
//            for (Zone zone : map.zones) {
//                Service.gI().sendMessAllPlayerInMap(zone, msg);
//            }
//            msg.cleanup();
//        } catch (Exception e) {
//            Logger.logException(Service.class, e);
//        }
//    }
    
    

    public boolean canOpenNpc(Player player) {
        if (this.tempId == ConstNpc.DAU_THAN) {
            if (player.zone.map.mapId == 21
                    || player.zone.map.mapId == 22
                    || player.zone.map.mapId == 23) {
                return true;
            } else {
                Service.gI().hideWaitDialog(player);
                Service.gI().sendThongBao(player, "Không thể thực hiện");
                return false;
            }
        }
        if (player.zone.map.mapId == this.mapId
                && Util.getDistance(this.cx, this.cy, player.location.x, player.location.y) <= 60) {
            player.iDMark.setNpcChose(this);
            return true;
        } else {
            Service.gI().hideWaitDialog(player);
            Service.gI().sendThongBao(player, "Không thể thực hiện khi đứng quá xa");
            return false;
        }
    }

}
