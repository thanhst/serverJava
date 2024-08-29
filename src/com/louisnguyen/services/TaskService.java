package com.louisnguyen.services;

import com.louisnguyen.consts.ConstMob;
import com.louisnguyen.consts.ConstNpc;
import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.consts.ConstTask;
import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.mob.Mob;
import com.louisnguyen.models.npc.Npc;
import com.louisnguyen.models.NhiemVu.SideTaskTemplate;
import com.louisnguyen.models.NhiemVu.SubTaskMain;
import com.louisnguyen.models.NhiemVu.TaskMain;
import com.louisnguyen.server.Manager;
import com.girlkun.network.io.Message;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.Util;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    /**
     * Làm cùng số người trong bang
     */
    private static final byte NMEMBER_DO_TASK_TOGETHER = 0;

    private static com.louisnguyen.services.TaskService i;

    public static com.louisnguyen.services.TaskService gI() {
        if (i == null) {
            i = new com.louisnguyen.services.TaskService();
        }
        return i;
    }

    public TaskMain getTaskMainById(Player player, int id) {
        for (TaskMain tm : Manager.TASKS) {
            if (tm.id == id) {
                TaskMain newTaskMain = new TaskMain(tm);
                newTaskMain.detail = transformName(player, newTaskMain.detail);
                for (SubTaskMain stm : newTaskMain.subTasks) {
                    stm.mapId = (short) transformMapId(player, stm.mapId);
                    stm.npcId = (byte) transformNpcId(player, stm.npcId);
                    stm.notify = transformName(player, stm.notify);
                    stm.name = transformName(player, stm.name);
                }
                return newTaskMain;
            }
        }
        return player.playerTask.taskMain;
    }

    //gửi thông tin nhiệm vụ chính
    public void sendTaskMain(Player player) {
        Message msg;
        try {
            msg = new Message(40);
            msg.writer().writeShort(player.playerTask.taskMain.id);
//            msg.writer().writeShort(12);
            msg.writer().writeByte(player.playerTask.taskMain.index);
//            msg.writer().writeUTF(player.playerTask.taskMain.name);
            msg.writer().writeUTF(player.playerTask.taskMain.name);
            msg.writer().writeUTF(player.playerTask.taskMain.detail);
            msg.writer().writeByte(player.playerTask.taskMain.subTasks.size());
            for (SubTaskMain stm : player.playerTask.taskMain.subTasks) {
                msg.writer().writeUTF(stm.name);
                msg.writer().writeByte(stm.npcId);
                msg.writer().writeShort(stm.mapId);
                msg.writer().writeUTF(stm.notify);
            }
            msg.writer().writeShort(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
            for (SubTaskMain stm : player.playerTask.taskMain.subTasks) {
                msg.writer().writeShort(stm.maxCount);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(TaskService.class, e);
        }
    }

    //chuyển sang task mới
    public void sendNextTaskMain(Player player) {
        rewardDoneTask(player);
        player.playerTask.taskMain = TaskService.gI().getTaskMainById(player, player.playerTask.taskMain.id + 1);
        sendTaskMain(player);
    }

    //số lượng đã hoàn thành
    public void sendUpdateCountSubTask(Player player) {
        Message msg;
        try {
            msg = new Message(43);
            msg.writer().writeShort(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //chuyển sub task tiếp theo
    public void sendNextSubTask(Player player) {
        Message msg;
        try {
            msg = new Message(41);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //gửi thông tin nhiệm vụ hiện tại
    public void sendInfoCurrentTask(Player player) {
        Service.gI().sendThongBao(player, "Nhiệm vụ hiện tại của bạn là "
                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
    }

    public boolean checkDoneTaskTalkNpc(Player player, Npc npc) {
        switch (npc.tempId) {
            case ConstNpc.QUY_LAO_KAME:
                if (player.clan != null  && player.clan.getMembers().size() >= 5 && player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 0) {
                    return player.gender == ConstPlayer.TRAI_DAT && doneTask(player, ConstTask.TASK_13_0);
                }
                return player.gender == ConstPlayer.TRAI_DAT && ((doneTask(player, ConstTask.TASK_10_0))
                        || (doneTask(player, ConstTask.TASK_10_1))
                        || (doneTask(player, ConstTask.TASK_13_1))
                        || (doneTask(player, ConstTask.TASK_14_3))
                        || (doneTask(player, ConstTask.TASK_15_2))
                        || (doneTask(player, ConstTask.TASK_16_4))
                        || (doneTask(player, ConstTask.TASK_18_4))
                        || (doneTask(player, ConstTask.TASK_19_2))
                        || (doneTask(player, ConstTask.TASK_20_6))
                        || (doneTask(player, ConstTask.TASK_21_3))
                        || (doneTask(player, ConstTask.TASK_22_4))
                        || (doneTask(player, ConstTask.TASK_22_4))
                        || (doneTask(player, ConstTask.TASK_23_3)));
            case ConstNpc.TRUONG_LAO_GURU:
                if (player.clan != null && player.clan.getMembers().size() >= 5 && player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 0) {
                    return player.gender == ConstPlayer.NAMEC && doneTask(player, ConstTask.TASK_13_0);
                }
                return player.gender == ConstPlayer.NAMEC && ((doneTask(player, ConstTask.TASK_10_0))
                        || (doneTask(player, ConstTask.TASK_10_1))
                        || (doneTask(player, ConstTask.TASK_13_1))
                        || (doneTask(player, ConstTask.TASK_14_3))
                        || (doneTask(player, ConstTask.TASK_15_2))
                        || (doneTask(player, ConstTask.TASK_16_4))
                        || (doneTask(player, ConstTask.TASK_18_4))
                        || (doneTask(player, ConstTask.TASK_19_2))
                        || (doneTask(player, ConstTask.TASK_20_6))
                        || (doneTask(player, ConstTask.TASK_21_3))
                        || (doneTask(player, ConstTask.TASK_22_4))
                        || (doneTask(player, ConstTask.TASK_22_4))
                        || (doneTask(player, ConstTask.TASK_23_3)));
            case ConstNpc.VUA_VEGETA:
                if (player.clan != null && player.clan.getMembers().size() >= 5 && player.playerTask.taskMain.id == 13 && player.playerTask.taskMain.index == 0) {
                    return player.gender == ConstPlayer.XAYDA && doneTask(player, ConstTask.TASK_13_0);
                }
                return player.gender == ConstPlayer.XAYDA
                        && ((doneTask(player, ConstTask.TASK_10_0))
                        || (doneTask(player, ConstTask.TASK_10_1))
                        || (doneTask(player, ConstTask.TASK_13_1))
                        || (doneTask(player, ConstTask.TASK_14_3))
                        || (doneTask(player, ConstTask.TASK_15_2))
                        || (doneTask(player, ConstTask.TASK_16_4))
                        || (doneTask(player, ConstTask.TASK_18_4))
                        || (doneTask(player, ConstTask.TASK_19_2))
                        || (doneTask(player, ConstTask.TASK_20_6))
                        || (doneTask(player, ConstTask.TASK_21_3))
                        || (doneTask(player, ConstTask.TASK_22_4))
                        || (doneTask(player, ConstTask.TASK_22_4))
                        || (doneTask(player, ConstTask.TASK_23_3)));
            case ConstNpc.ONG_GOHAN:
            case ConstNpc.ONG_MOORI:
            case ConstNpc.ONG_PARAGUS:
                return (doneTask(player, ConstTask.TASK_1_1)
                        || doneTask(player, ConstTask.TASK_2_1)
                        || doneTask(player, ConstTask.TASK_1_1)
                        || doneTask(player, ConstTask.TASK_3_2)
                        || doneTask(player, ConstTask.TASK_4_3)
                        || doneTask(player, ConstTask.TASK_8_3)
                        || doneTask(player, ConstTask.TASK_9_2));
            case ConstNpc.BUNMA:
            case ConstNpc.DENDE:
            case ConstNpc.APPULE:
                return doneTask(player, ConstTask.TASK_8_2);
            case ConstNpc.BUNMA_TL:
                return (doneTask(player, ConstTask.TASK_24_0)
                        || doneTask(player, ConstTask.TASK_24_2)
                        || doneTask(player, ConstTask.TASK_25_4)
                        || doneTask(player, ConstTask.TASK_25_4)
                        || doneTask(player, ConstTask.TASK_26_4)
                        || doneTask(player, ConstTask.TASK_27_5)
                        || doneTask(player, ConstTask.TASK_28_5)
                        || doneTask(player, ConstTask.TASK_29_5)
                        || doneTask(player, ConstTask.TASK_30_1));
            case ConstNpc.THAN_MEO_KARIN:
                if (player.nPoint.dameg >= 10000) {
                    return doneTask(player, ConstTask.TASK_29_0);
                }
        }
        return false;
    }

    //kiểm tra hoàn thành nhiệm vụ gia nhập bang hội
    public void checkDoneTaskJoinClan(Player player) {
        if (!player.isBoss && !player.isPet) {
            doneTask(player, ConstTask.TASK_13_0);
        }
    }

    //kiểm tra hoàn thành nhiệm vụ lấy item từ rương
    public void checkDoneTaskGetItemBox(Player player) {
        if (!player.isBoss && !player.isPet) {
            doneTask(player, ConstTask.TASK_0_3);
        }
    }

    //kiểm tra hoàn thành nhiệm vụ sức mạnh
    public void checkDoneTaskPower(Player player, long power) {
        if (!player.isBoss && !player.isPet) {
            if (power >= 16000) {
                doneTask(player, ConstTask.TASK_8_0);
            }
            if (power >= 40000) {
                doneTask(player, ConstTask.TASK_9_0);
            }
            if (power >= 200000) {
                doneTask(player, ConstTask.TASK_15_0);
            }
            if (power >= 500000) {
                doneTask(player, ConstTask.TASK_16_0);
            }
            if (power >= 1500000) {
                doneTask(player, ConstTask.TASK_18_0);
            }
            if (power >= 50000000) {
                doneTask(player, ConstTask.TASK_20_0);
            }
            if (power >= 5000000) {
                doneTask(player, ConstTask.TASK_19_0);
            }
        }
    }

    //kiểm tra hoàn thành nhiệm vụ khi player sử dụng tiềm năng
    public void checkDoneTaskUseTiemNang(Player player) {
        if (!player.isBoss && !player.isPet) {
            doneTask(player, ConstTask.TASK_3_0);
        }
    }

    //kiểm tra hoàn thành nhiệm vụ khi vào map nào đó
    public void checkDoneTaskGoToMap(Player player, Zone zoneJoin) {
        if (player.isPl()) {
            switch (zoneJoin.map.mapId) {
                case 39:
                case 40:
                case 41:
                    if (player.location.x >= 635) {
                        doneTask(player, ConstTask.TASK_0_0);
                    }
                    break;
                case 47:
                    doneTask(player, ConstTask.TASK_9_3);
                    break;
                case 93:
                    doneTask(player, ConstTask.TASK_25_0);
                    break;
                case 104:
                    doneTask(player, ConstTask.TASK_26_0);
                    break;
                case 97:
                    doneTask(player, ConstTask.TASK_27_0);
                    break;
                case 100:
                    doneTask(player, ConstTask.TASK_28_0);
                    break;
                case 103:
                    doneTask(player, ConstTask.TASK_29_2);
                case 114:
                    doneTask(player, ConstTask.TASK_30_0);
                    break;
            }
        }
    }

    //kiểm tra hoàn thành nhiệm vụ khi nhặt item
    public void checkDoneTaskPickItem(Player player, ItemMap item) {
        if (!player.isBoss && !player.isPet && item != null) {
            switch (item.itemTemplate.id) {
                case 73: //đùi gà
                    doneTask(player, ConstTask.TASK_2_0);
                    break;
                case 78: //em bé
                    doneTask(player, ConstTask.TASK_3_1);
                    Service.gI().sendFlagBag(player);
                    break;
                case 20:
                    doneTask(player, ConstTask.TASK_9_1);
                    break;
                case 85:
                    doneTask(player, ConstTask.TASK_15_1);
                    break;
                case 380:
                    doneTask(player, ConstTask.TASK_29_1);
                    break;

            }
        }
    }

    //kiểm tra hoàn thành nhiệm vụ kết bạn
//    public void checkDoneTaskMakeFriend(Player player, Player friend) {
//        if (!player.isBoss && !player.isPet) {
//            switch (friend.gender) {
//                case ConstPlayer.TRAI_DAT:
//                    doneTask(player, ConstTask.TASK_11_0);
//                    doneTask(player, ConstTask.TASK_27_0);
//                    break;
//                case ConstPlayer.NAMEC:
//                    doneTask(player, ConstTask.TASK_11_1);
//                    doneTask(player, ConstTask.TASK_27_1);
//                    break;
//                case ConstPlayer.XAYDA:
//                    doneTask(player, ConstTask.TASK_11_2);
//                    doneTask(player, ConstTask.TASK_27_2);
//                    break;
//            }
//        }
//    }
    //Kiểm tra hoàn thành nhiệm vụ thách đấu
//   public void checkDoneTaskPvP(Player player){
//            if (!player.isBoss && !player.isPet) {
//                doneTask(player, ConstTask.TASK_17_0);
//            }
//   }
    //kiểm tra hoàn thành nhiệm vụ khi xác nhận menu npc nào đó
    public void checkDoneTaskConfirmMenuNpc(Player player, Npc npc, byte select) {
        if (!player.isBoss && !player.isPet) {
            switch (npc.tempId) {
                case ConstNpc.DAU_THAN:
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                doneTask(player, ConstTask.TASK_0_4);
                            }
                    }
                    break;
            }
        }
    }

    //kiểm tra hoàn thành nhiệm vụ khi tiêu diệt được boss
    public void checkDoneTaskKillBoss(Player player, Boss boss) {
        if (player != null && !player.isBoss && !player.isPet) {
            switch ((int) boss.id) {
                case BossID.KUKU:
                    doneTask(player, ConstTask.TASK_21_0);
                    break;
                case BossID.MAP_DAU_DINH:
                    doneTask(player, ConstTask.TASK_21_1);
                    break;
                case BossID.RAMBO:
                    doneTask(player, ConstTask.TASK_21_2);
                    break;

//                case BossID.SO_4:
//                    doneTask(player, ConstTask.TASK_22_0);
//                    break;
//                case BossID.SO_3:
//                    doneTask(player, ConstTask.TASK_22_1);
//                    break;
//                case BossID.SO_1:
//                    doneTask(player, ConstTask.TASK_22_2);
//                    break;
//                case BossID.TIEU_DOI_TRUONG:
//                    doneTask(player, ConstTask.TASK_22_3);
//                    break;
                case BossID.FIDE:
                    switch (boss.currentLevel) {
                        case 0:
                            doneTask(player, ConstTask.TASK_23_0);
                            break;
                        case 1:
                            doneTask(player, ConstTask.TASK_23_1);
                            break;
                        case 2:
                            doneTask(player, ConstTask.TASK_23_2);
                            break;
                    }
                    break;
                case BossID.ANDROID_19:
                    doneTask(player, ConstTask.TASK_25_1);
                    break;
                case BossID.DR_KORE:
                    doneTask(player, ConstTask.TASK_25_2);
                    break;
                case BossID.ANDROID_15:
                    doneTask(player, ConstTask.TASK_26_1);
                    break;
                case BossID.ANDROID_14:
                    doneTask(player, ConstTask.TASK_26_2);
                    break;
                case BossID.ANDROID_13:
                    doneTask(player, ConstTask.TASK_26_3);
                    break;
                case BossID.POC:
                    doneTask(player, ConstTask.TASK_27_2);
                    break;
                case BossID.PIC:
                    doneTask(player, ConstTask.TASK_27_1);
                    break;
                case BossID.KING_KONG:
                    doneTask(player, ConstTask.TASK_27_3);
                    break;

                case BossID.XEN_BO_HUNG:
                    switch (boss.currentLevel) {
                        case 0:
                            doneTask(player, ConstTask.TASK_28_1);
                            break;
                        case 1:
                            doneTask(player, ConstTask.TASK_28_2);
                            break;
                        case 2:
                            doneTask(player, ConstTask.TASK_28_3);
                            break;
                    }
                    break;
                case BossID.TDST:
                    switch (boss.currentLevel) {
                        case 0:
                            doneTask(player, ConstTask.TASK_22_0);
                            break;
                        case 1:
                            doneTask(player, ConstTask.TASK_22_1);
                            break;
                        case 3:
                            doneTask(player, ConstTask.TASK_22_2);
                            break;
                        case 4:
                            doneTask(player, ConstTask.TASK_22_3);
                            break;
                    }
                    break;
                case BossID.XEN_CON_1:
                    doneTask(player, ConstTask.TASK_29_3);
                    break;
                case BossID.SIEU_BO_HUNG:
                    switch (boss.currentLevel) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            doneTask(player, ConstTask.TASK_29_4);
                            break;
                    }
                    break;
            }
        }
    }

    //kiểm tra hoàn thành nhiệm vụ khi giết được quái
    public void checkDoneTaskKillMob(Player player, Mob mob) {
        if (!player.isBoss && !player.isPet) {
            switch (mob.tempId) {
                case ConstMob.MOC_NHAN:
                    doneTask(player, ConstTask.TASK_1_0);
                    break;
                case ConstMob.KHUNG_LONG_ME:
                    doneTask(player, ConstTask.TASK_4_0);
                    break;
                case ConstMob.LON_LOI_ME:
                    doneTask(player, ConstTask.TASK_4_1);
                    break;
                case ConstMob.QUY_DAT_ME:
                    doneTask(player, ConstTask.TASK_4_2);
                    break;
                case ConstMob.THAN_LAN_BAY:
                case ConstMob.PHI_LONG:
                case ConstMob.QUY_BAY:
                    doneTask(player, ConstTask.TASK_8_1);
                    break;

                case ConstMob.HEO_RUNG:
                case ConstMob.HEO_DA_XANH:
                case ConstMob.HEO_XAYDA:
                    if (player.clan != null) {
                        List<Player> list = new ArrayList<>();
                        List<Player> playersMap = player.zone.getPlayers();
                        for (Player pl : playersMap) {
                            if (pl != null && pl.clan != null && pl.clan.equals(player.clan)) {
                                list.add(pl);
                            }
                        }
                        if (list.size() >= 3) {
                            for (Player pl : list) {
                                switch (mob.tempId) {
                                    case ConstMob.HEO_RUNG:
                                        doneTask(pl, ConstTask.TASK_14_0);
                                        break;
                                    case ConstMob.HEO_DA_XANH:
                                        doneTask(pl, ConstTask.TASK_14_1);
                                        break;
                                    case ConstMob.HEO_XAYDA:
                                        doneTask(pl, ConstTask.TASK_14_2);
                                        break;
                                }
                            }
                        }
                    }
                    break;
                case ConstMob.BULON:
                case ConstMob.UKULELE:
                case ConstMob.QUY_MAP:
                    if (player.clan != null) {
                        List<Player> list = new ArrayList<>();
                        List<Player> playersMap = player.zone.getPlayers();
                        for (Player pl : playersMap) {
                            if (pl != null && pl.clan != null && pl.clan.equals(player.clan)) {
                                list.add(pl);
                            }
                        }
                        if (list.size() >= 3) {
                            for (Player pl : list) {
                                switch (mob.tempId) {
                                    case ConstMob.BULON:
                                        doneTask(pl, ConstTask.TASK_16_1);
                                        break;
                                    case ConstMob.UKULELE:
                                        doneTask(pl, ConstTask.TASK_16_2);
                                        break;
                                    case ConstMob.QUY_MAP:
                                        doneTask(pl, ConstTask.TASK_16_3);
                                        break;
                                }
                            }
                        }
                    }
                    break;
                case ConstMob.TAMBOURINE:
                    doneTask(player, ConstTask.TASK_18_2);
                    break;
                case ConstMob.DRUM:
                    doneTask(player, ConstTask.TASK_18_3);
                    break;
                case ConstMob.AKKUMAN:
                    doneTask(player, ConstTask.TASK_18_1);
                    break;
                case ConstMob.NAPPA:
                    doneTask(player, ConstTask.TASK_20_1);
                    break;
                case ConstMob.SOLDIER:
                    doneTask(player, ConstTask.TASK_20_2);
                    break;
                case ConstMob.APPULE:
                    doneTask(player, ConstTask.TASK_20_3);
                    break;
                case ConstMob.RASPBERRY:
                    doneTask(player, ConstTask.TASK_20_4);
                    break;
                case ConstMob.THAN_LAN_XANH:
                    doneTask(player, ConstTask.TASK_20_5);
                    break;
                case ConstMob.XEN_CON_CAP_1:
                case ConstMob.XEN_CON_CAP_3:
                case ConstMob.XEN_CON_CAP_5:
                case ConstMob.XEN_CON_CAP_8:
                    if (player.clan != null) {
                        List<Player> list = new ArrayList<>();
                        List<Player> playersMap = player.zone.getPlayers();
                        for (Player pl : playersMap) {
                            if (pl != null && pl.clan != null && pl.clan.equals(player.clan)) {
                                list.add(pl);
                            }
                        }
                        if (list.size() >= 0) {
                            for (Player pl : list) {
                                switch (mob.tempId) {
                                    case ConstMob.XEN_CON_CAP_1:
                                        doneTask(player, ConstTask.TASK_24_1);
                                        break;
                                    case ConstMob.XEN_CON_CAP_3:
                                        doneTask(player, ConstTask.TASK_25_3);
                                        break;
                                    case ConstMob.XEN_CON_CAP_5:
                                        doneTask(player, ConstTask.TASK_27_4);
                                        break;
                                    case ConstMob.XEN_CON_CAP_8:
                                        doneTask(player, ConstTask.TASK_28_4);
                                        break;
                                }
                            }
                        }
                    }
            }
        }
    }

    //xong nhiệm vụ nào đó
    private boolean doneTask(Player player, int idTaskCustom) {
        if (TaskService.gI().isCurrentTask(player, idTaskCustom)) {
            if (player.playerTask.taskMain.id == 4 && player.playerTask.taskMain.index == 3) {
                player.playerTask.taskMain = TaskService.gI().getTaskMainById(player, player.playerTask.taskMain.id = 8);
                sendTaskMain(player);
            } else if (player.playerTask.taskMain.id == 10 && player.playerTask.taskMain.index == 1) {
                player.playerTask.taskMain = TaskService.gI().getTaskMainById(player, player.playerTask.taskMain.id = 13);
                sendTaskMain(player);
            } else if (player.playerTask.taskMain.id == 18 && player.playerTask.taskMain.index == 4) {
                player.playerTask.taskMain = TaskService.gI().getTaskMainById(player, player.playerTask.taskMain.id = 20);
                sendTaskMain(player);
            } else if (player.playerTask.taskMain.id == 16 && player.playerTask.taskMain.index == 4) {
                player.playerTask.taskMain = TaskService.gI().getTaskMainById(player, player.playerTask.taskMain.id = 18);
                sendTaskMain(player);
            }
            this.addDoneSubTask(player, 1);
            byte gender = player.gender;

            switch (idTaskCustom) {
                //--------------------------------------------------------------
                case ConstTask.TASK_1_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount + " mộc nhân");
                    } else {
                        Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                                + "hoàn thành nhiệm vụ.\b"
                                + "Nào, bây giờ bạn có thể\b"
                                + "gặp " + transformName(player, " %2") + " để báo\b"
                                + "cáo rồi!");
                    }
                    break;
                case ConstTask.TASK_1_1:
                    npcSay(player, ConstTask.NPC_NHA, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //--------------------------------------------------------------
                case ConstTask.TASK_2_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đã tìm thấy đùi gà\n"
                                + "rồi, hãy chạm nhanh 2\n"
                                + "lần vào đối tượng để\n"
                                + "lấy");
                        Service.gI().sendThongBao(player, "Bạn thu thập được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount + " đùi gà");
                    } else {
                        Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                                + "hoàn thành nhiệm vụ.\b"
                                + "Nào, bây giờ bạn có thể\b"
                                + "gặp " + transformName(player, " %2") + " để báo\b"
                                + "cáo rồi!");
                    }
                    break;
                case ConstTask.TASK_2_1:

                    InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItemBag(player, 73), 10);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().dropItemMapForMe(player, player.zone.getItemMapByTempId(74));

                    npcSay(player, ConstTask.NPC_NHA, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");

                    break;
                //--------------------------------------------------------------
                case ConstTask.TASK_3_0:
                    Service.gI().sendThongBao(player, "Bạn đã đạt sức mạnh\n"
                            + "yêu cầu, đi làm nhiệm\n"
                            + "vụ nào");
                    break;
                case ConstTask.TASK_3_1:

                    Service.gI().sendThongBao(player, "Aaa !!! Sao băng\n"
                            + "kìa... Đẹp thật đẹp");
                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                            + "hoàn thành nhiệm vụ.\b"
                            + "Nào, bây giờ bạn có thể\b"
                            + "gặp " + transformName(player, " %2") + " để báo\b"
                            + "cáo rồi!");
                    break;
                case ConstTask.TASK_3_2:
                    InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItemBag(player, 78), 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendFlagBag(player);
                    npcSay(player, ConstTask.NPC_NHA, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //--------------------------------------------------------------
                case ConstTask.TASK_4_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " khủng long mẹ"));
                    } else {
                        Service.gI().sendThongBao(player, "Tiếp theo là lợn lòi mẹ");
                    }
                    break;
                case ConstTask.TASK_4_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " lợn lòi mẹ"));
                    } else {
                        Service.gI().sendThongBao(player, "Tiếp theo là quỷ đất mẹ");
                    }
                    break;
                case ConstTask.TASK_4_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " quỷ đất mẹ"));
                    } else {
                        Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                                + "hoàn thành nhiệm vụ.\b"
                                + "Nào, bây giờ bạn có thể\b"
                                + "gặp " + transformName(player, " %2") + " để báo\b"
                                + "cáo rồi!");
                    }
                    break;
                case ConstTask.TASK_4_3:
                    npcSay(player, ConstTask.NPC_NHA, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //--------------------------------------------------------------
                case ConstTask.TASK_8_0:
                    Service.gI().sendThongBao(player, "Bạn đã đạt sức mạnh\n"
                            + "yêu cầu, đi làm nhiệm\n"
                            + "vụ nào");
                    break;
                case ConstTask.TASK_8_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " %9"));
                    } else {
                        Service.gI().sendThongBao(player, "Bạn vừa cứu được\n"
                                + transformName(player, ConstTask.TEN_NPC_SHOP_LANG) + " hãy về " + transformName(player, ConstTask.TEN_LANG) + "\n"
                                + "và nói chuyện với " + transformName(player, ConstTask.TEN_NPC_SHOP_LANG));
                    }
                    break;
                case ConstTask.TASK_8_2:
                    npcSay(player, ConstTask.NPC_SHOP_LANG,
                            "Cảm ơn ngươi đã cứu "
                            + "ta. Ta sẽ sẵn sàng phục\b"
                            + "vụ nếu ngươi cần mua "
                            + "vật dụng");
                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                            + "hoàn thành nhiệm vụ.\b"
                            + "Nào, bây giờ bạn có thể\b"
                            + "gặp " + transformName(player, " %2") + " để báo\b"
                            + "cáo rồi!");
                    break;
                case ConstTask.TASK_8_3:
                    npcSay(player, ConstTask.NPC_NHA, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //--------------------------------------------------------------------
                case ConstTask.TASK_9_0:
                    break;
                case ConstTask.TASK_9_1:
                    Service.gI().sendThongBao(player, "Bạn đã tìm thấy ngọc\n"
                            + "rồng rồi, hãy nhặt và\n"
                            + "mang về để hoàn thành\n"
                            + "nhiệm vụ");
                    break;
                case ConstTask.TASK_9_2:
                    npcSay(player, ConstTask.NPC_NHA, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                case ConstTask.TASK_9_3:
                    break;
                //-----------------------------------------------------------------------
                case ConstTask.TASK_10_0:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    Service.gI().sendThongBao(player, "Nhiệm vụ hoàn thành,\n"
                            + "báo cáo với " + transformName(player, ConstTask.TEN_NPC_QUY_LAO) + "  nào!");
                    break;
                case ConstTask.TASK_10_1:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //---------------------------------------------------------------------------
                case ConstTask.TASK_13_0:
                    Service.gI().sendThongBao(player, "Nhiệm vụ hoàn thành,\n"
                            + "báo cáo với " + transformName(player, ConstTask.TEN_NPC_QUY_LAO) + "  nào!");
                    break;
                case ConstTask.TASK_13_1:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;

                //=============================================================================
                case ConstTask.TASK_14_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " Heo rừng"));
                    } else {
                        Service.gI().sendThongBao(player, "Tiếp theo là Heo da xanh");
                    }
                    break;
                case ConstTask.TASK_14_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " Heo da xanh"));
                    } else {
                        Service.gI().sendThongBao(player, "Tiếp theo là Heo xayda");
                    }
                    break;
                case ConstTask.TASK_14_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " Heo xayda"));
                    } else {
                        Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                                + "hoàn thành nhiệm vụ.\b"
                                + "Nào, bây giờ bạn có thể\b"
                                + "gặp " + transformName(player, " %10") + " để báo\b"
                                + "cáo rồi!");
                    }
                    break;
                case ConstTask.TASK_14_3:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //===================================================================================
                case ConstTask.TASK_15_0:
                    Service.gI().sendThongBao(player, "Bạn đã đạt sức mạnh\n"
                            + "yêu cầu, đi làm nhiệm\n"
                            + "vụ nào");
                    break;
                case ConstTask.TASK_15_1:
                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                            + "hoàn thành nhiệm vụ.\b"
                            + "Nào, bây giờ bạn có thể\b"
                            + "gặp " + transformName(player, " %10") + " để báo\b"
                            + "cáo rồi!");
                    break;
                case ConstTask.TASK_15_2:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //=====================================================================================
                case ConstTask.TASK_16_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " Bulon"));
                    } else {
                        Service.gI().sendThongBao(player, "Tiếp theo là Ukulele");
                    }
                    break;
                case ConstTask.TASK_16_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " Ukulele"));
                    } else {
                        Service.gI().sendThongBao(player, "Tiếp theo là Quỷ mập");
                    }
                    break;
                case ConstTask.TASK_16_3:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " Quỷ mập"));
                    } else {
                        Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                                + "hoàn thành nhiệm vụ.\b"
                                + "Nào, bây giờ bạn có thể\b"
                                + "gặp " + transformName(player, " %10") + " để báo\b"
                                + "cáo rồi!");
                    }
                    break;
                case ConstTask.TASK_16_4:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //=====================================================================
                case ConstTask.TASK_18_0:
                    Service.gI().sendThongBao(player, "Mau đến XayDa\nđánh bại\nAkkuman tại\nThành phố Vegeta");
                    break;
                case ConstTask.TASK_18_1:
                    Service.gI().sendThongBao(player, "Mau đến Trái Đất\nđánh bại\nTambourine tại\nĐông Karin");
                    break;
                case ConstTask.TASK_18_2:
                    Service.gI().sendThongBao(player, "Mau đến Na mếc\nđánh bại\nDrum tại\nThung lũng Namếc");
                case ConstTask.TASK_18_3:
                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                            + "hoàn thành nhiệm vụ.\b"
                            + "Nào, bây giờ bạn có thể\b"
                            + "gặp " + transformName(player, " %10") + " để báo\b"
                            + "cáo rồi!");
                    break;
                case ConstTask.TASK_18_4:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //=========================================================================
                case ConstTask.TASK_19_1:
                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                            + "hoàn thành nhiệm vụ.\b"
                            + "Nào, bây giờ bạn có thể\b"
                            + "gặp " + transformName(player, " %10") + " để báo\b"
                            + "cáo rồi!");
                    break;
                case ConstTask.TASK_19_2:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                //==========================================================================
                case ConstTask.TASK_20_7:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //==========================================================================
                case ConstTask.TASK_21_0:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Kuku");
                    break;
                case ConstTask.TASK_21_1:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Mập đầu đinh");
                case ConstTask.TASK_21_2:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Rambo");
                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                            + "hoàn thành nhiệm vụ.\b"
                            + "Nào, bây giờ bạn có thể\b"
                            + "gặp " + transformName(player, " %10") + " để báo\b"
                            + "cáo rồi!");
                    break;
                case ConstTask.TASK_21_3:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;

                //==========================================================================
                case ConstTask.TASK_22_0:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Số 4");
                    break;
                case ConstTask.TASK_22_1:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Số 3");
                    break;
                case ConstTask.TASK_22_2:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Số 1");
                    break;
                case ConstTask.TASK_22_3:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Tiểu đội trưởng");
                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                            + "hoàn thành nhiệm vụ.\b"
                            + "Nào, bây giờ bạn có thể\b"
                            + "gặp " + transformName(player, " %10") + " để báo\b"
                            + "cáo rồi!");
                    break;
                case ConstTask.TASK_22_4:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;

                //==========================================================================
                case ConstTask.TASK_23_0:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Fide đại ca 01");
                    break;
                case ConstTask.TASK_23_1:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Fide đại ca 02");
                    break;
                case ConstTask.TASK_23_2:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Fide đại ca 03");
                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã\b"
                            + "hoàn thành nhiệm vụ.\b"
                            + "Nào, bây giờ bạn có thể\b"
                            + "gặp " + transformName(player, " %10") + " để báo\b"
                            + "cáo rồi!");
                    break;
                case ConstTask.TASK_23_3:
                    npcSay(player, ConstTask.NPC_QUY_LAO, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //==========================================================================
                case ConstTask.TASK_24_0:
                    npcSay(player, ConstNpc.BUNMA_TL, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                case ConstTask.TASK_24_1:
                    Service.gI().sendThongBao(player, "Xong việc rồi về báo\ncáo cho Bun ma nào");
                    break;
                case ConstTask.TASK_24_2:
                    npcSay(player, ConstNpc.BUNMA_TL, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //=============================================================================

                case ConstTask.TASK_25_0:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Android 19");
                    break;
                case ConstTask.TASK_25_1:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Android 20");
                    break;
                case ConstTask.TASK_25_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " Xên con cấp 3"));
                    } else {
                        Service.gI().sendThongBao(player, "Xong việc rồi về báo\ncáo cho Bun ma nào");
                    }
                    break;
                case ConstTask.TASK_25_3:
                    npcSay(player, ConstNpc.BUNMA_TL, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Đến sân sau siêu thị");
                    }
                    break;
                //=============================================================
                case ConstTask.TASK_26_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Tìm và tiêu diệt Android 15");
                    }
                    break;
                case ConstTask.TASK_26_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                                + "công Boss Android 15");
                    }
                    break;
                case ConstTask.TASK_26_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                                + "công Boss Android 14");
                    }
                    break;
                case ConstTask.TASK_26_3:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Android 13");
                    Service.gI().sendThongBao(player, "Xong việc rồi về báo\ncáo cho Bun ma nào");
                    break;
                case ConstTask.TASK_26_4:
                    npcSay(player, ConstNpc.BUNMA_TL, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
                //====================================================//
                case ConstTask.TASK_27_0:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Tiêu diệt Píc");
                    }
                    break;
                case ConstTask.TASK_27_1:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                                + "công Boss Píc");
                    }
                    break;
                case ConstTask.TASK_27_2:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                                + "công Boss Póc");
                    }
                    break;
                case ConstTask.TASK_27_3:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss King Kong");
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Tiêu diệt 800 xên con cấp 5");
                    }
                    break;
                case ConstTask.TASK_27_4:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " Xên con cấp 5"));
                    } else {
                        Service.gI().sendThongBao(player, "Xong việc rồi về báo\ncáo cho Bun ma nào");
                    }
                    break;
                case ConstTask.TASK_27_5:
                    npcSay(player, ConstNpc.BUNMA_TL, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");

                    Service.gI().sendThongBao(player, "Đến thị trận Ginder");
                    break;
                //============================================================

                case ConstTask.TASK_28_0:
                    Service.gI().sendThongBao(player, "Tiêu diệt Xên bọ hung cấp 1");
                    break;
                case ConstTask.TASK_28_1:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Xên bọ hung cấp 1");
                    break;
                case ConstTask.TASK_28_2:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Xên bọ hung cấp 2");
                    break;
                case ConstTask.TASK_28_3:
                    Service.gI().sendThongBao(player, "Bạn vừa tiêu diệt thành\n"
                            + "công Boss Xên bọ hung cấp 3");
                    Service.gI().sendThongBao(player, "Tiêu diệt 700 xên con cấp 8");
                    break;
                case ConstTask.TASK_28_4:
                    if (isCurrentTask(player, idTaskCustom)) {
                        Service.gI().sendThongBao(player, "Bạn đánh được "
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count + "/"
                                + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount
                                + transformName(player, " Xên con cấp 8"));
                    } else {
                        Service.gI().sendThongBao(player, "Xong việc rồi về báo\ncáo cho Bun ma nào");
                    }
                    break;
                case ConstTask.TASK_28_5:
                    npcSay(player, ConstNpc.BUNMA_TL, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");

                //=============================================================
                case ConstTask.TASK_29_0:
                    npcSay(player, ConstNpc.THAN_MEO_KARIN, "Hoàn thành nhiệm vụ nâng 10k sức đánh, mau\btiếp tục làm các mục tiếp theo nào!!!");
                    Service.gI().sendThongBao(player, "Đến tương lai thu\nthập Capsule kì bí\nnào, nhớ sử dụng\nmáy dò");
                    break;
                case ConstTask.TASK_29_1:
                    Service.gI().sendThongBao(player, "Hãy đến võ đài xên bọ hung");
                    break;
                case ConstTask.TASK_29_2:
                    Service.gI().sendThongBao(player, "Tiêu diệt lũ bọ hung con");
                    break;
                case ConstTask.TASK_29_3:
                    Service.gI().sendThongBao(player, "Hãy tiêu diệt Siêu Bọ Hung");
                    break;
                case ConstTask.TASK_29_4:
                    Service.gI().sendThongBao(player, "Xong việc rồi về báo\ncáo cho Bun ma nào");
                    break;
                case ConstTask.TASK_29_5:
                    npcSay(player, ConstNpc.BUNMA_TL, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;

                //=======================================================//
                case ConstTask.TASK_30_0:
                    Service.gI().sendThongBao(player, "Xong việc rồi về báo\ncáo cho Bun ma nào");
                    break;
                case ConstTask.TASK_30_1:
                    npcSay(player, ConstNpc.BUNMA_TL, "Con thật sự đã trưởng "
                            + "thành, hãy cố gắng tiếp "
                            + "tục phát huy nhé!!!");
                    break;
            }
            PlayerService.gI().sendInfoHpMpMoney(player);
            return true;
        }
        return false;
    }

    private void npcSay(Player player, int npcId, String text) {
        npcId = transformNpcId(player, npcId);
        text = transformName(player, text);
        int avatar = NpcService.gI().getAvatar(npcId);
        NpcService.gI().createTutorial(player, avatar, text);
    }

    //Thưởng nhiệm vụ
    private void rewardDoneTask(Player player) {
        switch (player.playerTask.taskMain.id) {
            case 1:
                Service.gI().addSMTN(player, (byte) 0, 500, false);
                Service.gI().addSMTN(player, (byte) 1, 500, false);
                player.inventory.gold += 100_000_000;
                player.inventory.ruby += 50;
                Service.gI().sendMoney(player);
                Service.gI().sendThongBao(player, "Bạn vừa được thưởng\n 500 sức mạnh");
                Service.gI().sendThongBao(player, "Bạn vừa được thưởng\n 500 tiềm năng");
                Service.gI().sendThongBao(player, "Bạn vừa được thưởng\n 100 Tr vàng");
                Service.gI().sendThongBao(player, "Bạn vừa được thưởng\n 50 hồng ngọc");
                break;
        }
    }

    // vd: pem đc 1 mộc nhân -> +1 mộc nhân vào nv hiện tại
    private void addDoneSubTask(Player player, int numDone) {
        player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count += numDone;
        if (player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count
                >= player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).maxCount) {
            player.playerTask.taskMain.index++;
            if (player.playerTask.taskMain.index >= player.playerTask.taskMain.subTasks.size()) {
                this.sendNextTaskMain(player);
            } else {
                this.sendNextSubTask(player);
            }
        } else {
            this.sendUpdateCountSubTask(player);
        }
    }

    private int transformMapId(Player player, int id) {
        if (id == ConstTask.MAP_NHA) {
            return (short) (player.gender + 21);
        } else if (id == ConstTask.MAP_200) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 1 : (player.gender == ConstPlayer.NAMEC
                            ? 8 : 15);
        } else if (id == ConstTask.MAP_VACH_NUI) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 39 : (player.gender == ConstPlayer.NAMEC
                            ? 40 : 41);
        } else if (id == ConstTask.MAP_200) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 2 : (player.gender == ConstPlayer.NAMEC
                            ? 9 : 16);
        } else if (id == ConstTask.MAP_TTVT) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 24 : (player.gender == ConstPlayer.NAMEC
                            ? 25 : 26);
        } else if (id == ConstTask.MAP_QUAI_BAY_600) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 3 : (player.gender == ConstPlayer.NAMEC
                            ? 11 : 17);
        } else if (id == ConstTask.MAP_LANG) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 0 : (player.gender == ConstPlayer.NAMEC
                            ? 7 : 14);
        } else if (id == ConstTask.MAP_QUY_LAO) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? 5 : (player.gender == ConstPlayer.NAMEC
                            ? 13 : 20);
        }
        return id;
    }

    private int transformNpcId(Player player, int id) {
        if (id == ConstTask.NPC_NHA) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? ConstNpc.ONG_GOHAN : (player.gender == ConstPlayer.NAMEC
                            ? ConstNpc.ONG_MOORI : ConstNpc.ONG_PARAGUS);
        } else if (id == ConstTask.NPC_TTVT) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? ConstNpc.DR_DRIEF : (player.gender == ConstPlayer.NAMEC
                            ? ConstNpc.CARGO : ConstNpc.CUI);
        } else if (id == ConstTask.NPC_SHOP_LANG) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? ConstNpc.BUNMA : (player.gender == ConstPlayer.NAMEC
                            ? ConstNpc.DENDE : ConstNpc.APPULE);
        } else if (id == ConstTask.NPC_QUY_LAO) {
            return player.gender == ConstPlayer.TRAI_DAT
                    ? ConstNpc.QUY_LAO_KAME : (player.gender == ConstPlayer.NAMEC
                            ? ConstNpc.TRUONG_LAO_GURU : ConstNpc.VUA_VEGETA);
        }
        return id;
    }

    //replate %1 %2 -> chữ
    public String transformName(Player player, String text) {
        byte gender = player.gender;

        text = text.replaceAll(ConstTask.TEN_NPC_QUY_LAO, player.gender == ConstPlayer.TRAI_DAT
                ? "Quy Lão Kame" : (player.gender == ConstPlayer.NAMEC
                        ? "Trưởng lão Guru" : "Vua Vegeta"));
        text = text.replaceAll(ConstTask.TEN_MAP_QUY_LAO, player.gender == ConstPlayer.TRAI_DAT
                ? "Đảo Kamê" : (player.gender == ConstPlayer.NAMEC
                        ? "Đảo Guru" : "Vách núi đen"));
        text = text.replaceAll(ConstTask.TEN_QUAI_3000, player.gender == ConstPlayer.TRAI_DAT
                ? "ốc mượn hồn" : (player.gender == ConstPlayer.NAMEC
                        ? "ốc sên" : "heo Xayda mẹ"));
        //----------------------------------------------------------------------
        text = text.replaceAll(ConstTask.TEN_LANG, player.gender == ConstPlayer.TRAI_DAT
                ? "Làng Aru" : (player.gender == ConstPlayer.NAMEC
                        ? "Làng Mori" : "Làng Kakarot"));
        text = text.replaceAll(ConstTask.TEN_NPC_NHA, player.gender == ConstPlayer.TRAI_DAT
                ? "ông Gôhan" : (player.gender == ConstPlayer.NAMEC
                        ? "ông Moori" : "ông Paragus"));
        text = text.replaceAll(ConstTask.TEN_QUAI_200, player.gender == ConstPlayer.TRAI_DAT
                ? "khủng long" : (player.gender == ConstPlayer.NAMEC
                        ? "lợn lòi" : "quỷ đất"));
        text = text.replaceAll(ConstTask.TEN_MAP_200, player.gender == ConstPlayer.TRAI_DAT
                ? "Đồi hoa cúc" : (player.gender == ConstPlayer.NAMEC
                        ? "Đồi nấm tím" : "Đồi hoang"));
        text = text.replaceAll(ConstTask.TEN_VACH_NUI, player.gender == ConstPlayer.TRAI_DAT
                ? "Vách núi Aru" : (player.gender == ConstPlayer.NAMEC
                        ? "Vách núi Moori" : "Vách núi Kakarot"));
        text = text.replaceAll(ConstTask.TEN_MAP_500, player.gender == ConstPlayer.TRAI_DAT
                ? "Thung lũng tre" : (player.gender == ConstPlayer.NAMEC
                        ? "Thị trấn Moori" : "Làng Plane"));
        text = text.replaceAll(ConstTask.TEN_NPC_TTVT, player.gender == ConstPlayer.TRAI_DAT
                ? "Dr. Brief" : (player.gender == ConstPlayer.NAMEC
                        ? "Cargo" : "Cui"));
        text = text.replaceAll(ConstTask.TEN_QUAI_BAY_600, player.gender == ConstPlayer.TRAI_DAT
                ? "thằn lằn bay" : (player.gender == ConstPlayer.NAMEC
                        ? "phi long" : "quỷ bay"));
        text = text.replaceAll(ConstTask.TEN_NPC_SHOP_LANG, player.gender == ConstPlayer.TRAI_DAT
                ? "Bunma" : (player.gender == ConstPlayer.NAMEC
                        ? "Dende" : "Appule"));
        return text;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j <= 10; j++) {
                System.out.println("case ConstTask.TASK_" + i + "_" + j + ":");
                System.out.println("return player.playerTask.taskMain.id == " + i + " && player.playerTask.taskMain.index == " + j + ";");
            }
        }
    }

    private boolean isCurrentTask(Player player, int idTaskCustom) {

        return idTaskCustom == (player.playerTask.taskMain.id << 10) + player.playerTask.taskMain.index << 1;
    }

    public int getIdTask(Player player) {
        if (player.isPet || player.isBoss || player.playerTask == null || player.playerTask.taskMain == null) {
            return -1;
        }
        return (player.playerTask.taskMain.id << 10) + player.playerTask.taskMain.index << 1;
    }

    //--------------------------------------------------------------------------
    public SideTaskTemplate getSideTaskTemplateById(int id) {
        if (id != -1) {
            return Manager.SIDE_TASKS_TEMPLATE.get(id);
        }
        return null;
    }

    public void changeSideTask(Player player, byte level) {
        if (player.playerTask.sideTask.leftTask > 0) {
            player.playerTask.sideTask.reset();
            SideTaskTemplate temp = Manager.SIDE_TASKS_TEMPLATE.get(Util.nextInt(0, Manager.SIDE_TASKS_TEMPLATE.size() - 1));
            player.playerTask.sideTask.template = temp;
            player.playerTask.sideTask.maxCount = Util.nextInt(temp.count[level][0], temp.count[level][1]);
            player.playerTask.sideTask.leftTask--;
            player.playerTask.sideTask.level = level;
            player.playerTask.sideTask.receivedTime = System.currentTimeMillis();
//            Service.gI().sendThongBao(player, "Bạn nhận được nhiệm vụ: " + player.playerTask.sideTask.getName());
        } else {
            Service.gI().sendThongBao(player,
                    "Gặp lại ta vào ngày mai\nnhé.");
        }
    }

    public void removeSideTask(Player player) {
        Service.gI().sendThongBao(player, "Bạn vừa hủy bỏ nhiệm vụ " + player.playerTask.sideTask.getName());
        player.playerTask.sideTask.reset();
    }

    public void paySideTask(Player player) {
        if (player.playerTask.sideTask.template != null) {
            if (player.playerTask.sideTask.isDone()) {
                int goldReward = 0;
                switch (player.playerTask.sideTask.level) {
                    case ConstTask.EASY:
                        goldReward = ConstTask.GOLD_EASY;
                        break;
                    case ConstTask.NORMAL:
                        goldReward = ConstTask.GOLD_NORMAL;
                        break;
                    case ConstTask.HARD:
                        goldReward = ConstTask.GOLD_HARD;
                        break;
                    case ConstTask.VERY_HARD:
                        goldReward = ConstTask.GOLD_VERY_HARD;
                        break;
                    case ConstTask.HELL:
                        goldReward = ConstTask.GOLD_HELL;
                        break;
                }
                player.inventory.addGold(goldReward);
                Service.gI().sendMoney(player);
                Service.gI().sendThongBao(player, "Bạn nhận được "
                        + Util.numberToMoney(goldReward) + " vàng");
                player.playerTask.sideTask.reset();
            } else {
                Service.gI().sendThongBao(player, "Bạn chưa hoàn thành nhiệm vụ");
            }
        }
    }

    public void checkDoneSideTaskKillMob(Player player, Mob mob) {
        if (player.playerTask.sideTask.template != null) {
            if ((player.playerTask.sideTask.template.id == 0 && mob.tempId == ConstMob.KHUNG_LONG)
                    || (player.playerTask.sideTask.template.id == 1 && mob.tempId == ConstMob.LON_LOI)
                    || (player.playerTask.sideTask.template.id == 2 && mob.tempId == ConstMob.QUY_DAT)
                    || (player.playerTask.sideTask.template.id == 3 && mob.tempId == ConstMob.KHUNG_LONG_ME)
                    || (player.playerTask.sideTask.template.id == 4 && mob.tempId == ConstMob.LON_LOI_ME)
                    || (player.playerTask.sideTask.template.id == 5 && mob.tempId == ConstMob.QUY_DAT_ME)
                    || (player.playerTask.sideTask.template.id == 6 && mob.tempId == ConstMob.THAN_LAN_BAY)
                    || (player.playerTask.sideTask.template.id == 7 && mob.tempId == ConstMob.PHI_LONG)
                    || (player.playerTask.sideTask.template.id == 8 && mob.tempId == ConstMob.QUY_BAY)
                    || (player.playerTask.sideTask.template.id == 9 && mob.tempId == ConstMob.THAN_LAN_ME)
                    || (player.playerTask.sideTask.template.id == 10 && mob.tempId == ConstMob.PHI_LONG_ME)
                    || (player.playerTask.sideTask.template.id == 11 && mob.tempId == ConstMob.QUY_BAY_ME)
                    || (player.playerTask.sideTask.template.id == 12 && mob.tempId == ConstMob.HEO_RUNG)
                    || (player.playerTask.sideTask.template.id == 13 && mob.tempId == ConstMob.HEO_DA_XANH)
                    || (player.playerTask.sideTask.template.id == 14 && mob.tempId == ConstMob.HEO_XAYDA)
                    || (player.playerTask.sideTask.template.id == 15 && mob.tempId == ConstMob.OC_MUON_HON)
                    || (player.playerTask.sideTask.template.id == 16 && mob.tempId == ConstMob.OC_SEN)
                    || (player.playerTask.sideTask.template.id == 17 && mob.tempId == ConstMob.HEO_XAYDA_ME)
                    || (player.playerTask.sideTask.template.id == 18 && mob.tempId == ConstMob.KHONG_TAC)
                    || (player.playerTask.sideTask.template.id == 19 && mob.tempId == ConstMob.QUY_DAU_TO)
                    || (player.playerTask.sideTask.template.id == 20 && mob.tempId == ConstMob.QUY_DIA_NGUC)
                    || (player.playerTask.sideTask.template.id == 21 && mob.tempId == ConstMob.HEO_RUNG_ME)
                    || (player.playerTask.sideTask.template.id == 22 && mob.tempId == ConstMob.HEO_XANH_ME)
                    || (player.playerTask.sideTask.template.id == 23 && mob.tempId == ConstMob.ALIEN)
                    || (player.playerTask.sideTask.template.id == 24 && mob.tempId == ConstMob.TAMBOURINE)
                    || (player.playerTask.sideTask.template.id == 25 && mob.tempId == ConstMob.DRUM)
                    || (player.playerTask.sideTask.template.id == 26 && mob.tempId == ConstMob.AKKUMAN)
                    || (player.playerTask.sideTask.template.id == 27 && mob.tempId == ConstMob.NAPPA)
                    || (player.playerTask.sideTask.template.id == 28 && mob.tempId == ConstMob.SOLDIER)
                    || (player.playerTask.sideTask.template.id == 29 && mob.tempId == ConstMob.APPULE)
                    || (player.playerTask.sideTask.template.id == 30 && mob.tempId == ConstMob.RASPBERRY)
                    || (player.playerTask.sideTask.template.id == 31 && mob.tempId == ConstMob.THAN_LAN_XANH)
                    || (player.playerTask.sideTask.template.id == 32 && mob.tempId == ConstMob.QUY_DAU_NHON)
                    || (player.playerTask.sideTask.template.id == 33 && mob.tempId == ConstMob.QUY_DAU_VANG)
                    || (player.playerTask.sideTask.template.id == 34 && mob.tempId == ConstMob.QUY_DA_TIM)
                    || (player.playerTask.sideTask.template.id == 35 && mob.tempId == ConstMob.QUY_GIA)
                    || (player.playerTask.sideTask.template.id == 36 && mob.tempId == ConstMob.CA_SAU)
                    || (player.playerTask.sideTask.template.id == 37 && mob.tempId == ConstMob.DOI_DA_XANH)
                    || (player.playerTask.sideTask.template.id == 38 && mob.tempId == ConstMob.QUY_CHIM)
                    || (player.playerTask.sideTask.template.id == 39 && mob.tempId == ConstMob.LINH_DAU_TROC)
                    || (player.playerTask.sideTask.template.id == 40 && mob.tempId == ConstMob.LINH_TAI_DAI)
                    || (player.playerTask.sideTask.template.id == 41 && mob.tempId == ConstMob.LINH_VU_TRU)
                    || (player.playerTask.sideTask.template.id == 42 && mob.tempId == ConstMob.KHI_LONG_DEN)
                    || (player.playerTask.sideTask.template.id == 43 && mob.tempId == ConstMob.KHI_GIAP_SAT)
                    || (player.playerTask.sideTask.template.id == 44 && mob.tempId == ConstMob.KHI_LONG_DO)
                    || (player.playerTask.sideTask.template.id == 45 && mob.tempId == ConstMob.KHI_LONG_VANG)
                    || (player.playerTask.sideTask.template.id == 46 && mob.tempId == ConstMob.XEN_CON_CAP_1)
                    || (player.playerTask.sideTask.template.id == 47 && mob.tempId == ConstMob.XEN_CON_CAP_2)
                    || (player.playerTask.sideTask.template.id == 48 && mob.tempId == ConstMob.XEN_CON_CAP_3)
                    || (player.playerTask.sideTask.template.id == 49 && mob.tempId == ConstMob.XEN_CON_CAP_4)
                    || (player.playerTask.sideTask.template.id == 50 && mob.tempId == ConstMob.XEN_CON_CAP_5)
                    || (player.playerTask.sideTask.template.id == 51 && mob.tempId == ConstMob.XEN_CON_CAP_6)
                    || (player.playerTask.sideTask.template.id == 52 && mob.tempId == ConstMob.XEN_CON_CAP_7)
                    || (player.playerTask.sideTask.template.id == 53 && mob.tempId == ConstMob.XEN_CON_CAP_8)
                    || (player.playerTask.sideTask.template.id == 54 && mob.tempId == ConstMob.TAI_TIM)
                    || (player.playerTask.sideTask.template.id == 55 && mob.tempId == ConstMob.ABO)
                    || (player.playerTask.sideTask.template.id == 56 && mob.tempId == ConstMob.KADO)
                    || (player.playerTask.sideTask.template.id == 57 && mob.tempId == ConstMob.DA_XANH)) {
                player.playerTask.sideTask.count++;
                notifyProcessSideTask(player);
            }
        }
    }

    public void checkDoneSideTaskPickItem(Player player, ItemMap item) {
        if (player.playerTask.sideTask.template != null) {
            if ((player.playerTask.sideTask.template.id == 58 && item.itemTemplate.type == 9)) {
                player.playerTask.sideTask.count += item.quantity;
                notifyProcessSideTask(player);
            }
        }
    }

    private void notifyProcessSideTask(Player player) {
        int percentDone = player.playerTask.sideTask.getPercentProcess();
        boolean notify = false;
        if (percentDone != 100) {
            if (!player.playerTask.sideTask.notify90 && percentDone >= 90) {
                player.playerTask.sideTask.notify90 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify80 && percentDone >= 80) {
                player.playerTask.sideTask.notify80 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify70 && percentDone >= 70) {
                player.playerTask.sideTask.notify70 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify60 && percentDone >= 60) {
                player.playerTask.sideTask.notify60 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify50 && percentDone >= 50) {
                player.playerTask.sideTask.notify50 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify40 && percentDone >= 40) {
                player.playerTask.sideTask.notify40 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify30 && percentDone >= 30) {
                player.playerTask.sideTask.notify30 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify20 && percentDone >= 20) {
                player.playerTask.sideTask.notify20 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify10 && percentDone >= 10) {
                player.playerTask.sideTask.notify10 = true;
                notify = true;
            } else if (!player.playerTask.sideTask.notify0 && percentDone >= 0) {
                player.playerTask.sideTask.notify0 = true;
                notify = true;
            }
            if (notify) {
                Service.gI().sendThongBao(player, "Nhiệm vụ: "
                        + player.playerTask.sideTask.getName() + " đã hoàn thành: "
                        + player.playerTask.sideTask.count + "/" + player.playerTask.sideTask.maxCount + " ("
                        + percentDone + "%)");
            }
        } else {
            Service.gI().sendThongBao(player, "Chúc mừng bạn đã hoàn thành nhiệm vụ, "
                    + "bây giờ hãy quay về Bò Mộng trả nhiệm vụ.");
        }
    }
}
