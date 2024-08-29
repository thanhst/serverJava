package com.louisnguyen.services;

//import static com.louisnguyen.models.map.BanDoKhoBau_old.BanDoKhoBau.TIME_BAN_DO_KHO_BAU;
import static com.louisnguyen.services.EffectSkillService.*;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.girlkun.database.GirlkunDB;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.girlkun.network.session.Session;
import com.girlkun.result.GirlkunResultSet;
import com.louisnguyen.consts.ConstInfor;
import com.louisnguyen.consts.ConstNpc;
import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.data.DataGame;
import com.louisnguyen.jdbc.daos.GodGK;
import com.louisnguyen.jdbc.daos.PlayerDAO;
import com.louisnguyen.models.boss.BossManager;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.item.Item.ItemOption;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.matches.TOP;
import com.louisnguyen.models.mob.Mob;
import com.louisnguyen.models.player.Pet;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.models.shop.ItemShop;
import com.louisnguyen.models.shop.Shop;
import com.louisnguyen.models.skill.Skill;
//import com.louisnguyen.models.map.BanDoKhoBau_old.BanDoKhoBau;
import com.louisnguyen.server.Client;
import com.louisnguyen.server.Maintenance;
import com.louisnguyen.server.Manager;
import com.louisnguyen.server.ServerManager;
import com.louisnguyen.server.io.MySession;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.services.func.Input;
import com.louisnguyen.services.func.SummonDragon;
import com.louisnguyen.utils.FileIO;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.TimeUtil;
import com.louisnguyen.utils.Util;

public class Service {

    private static Service instance;
    LocalTime currentTime = LocalTime.now();

    public static Service gI() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void sendTitleRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 888) {
                if (player.lastTimeTitle1 > 0 && player.isTitleUse) {
                    me.writer().writeShort(60);
                }
            } else if (id == 889) {
                if (player.lastTimeTitle2 > 0 && player.isTitleUse2) {
                    if (!player.isTitleUse) {
                        me.writer().writeShort(85);
                    }
                    me.writer().writeShort(66);
                }
            } else if (id == 890) {
                if (player.lastTimeTitle3 > 0 && player.isTitleUse3) {
                    if (player.isTitleUse && player.isTitleUse2) {
                        me.writer().writeShort(64);
                    } else if (player.isTitleUse2 && !player.isTitleUse) {
                        me.writer().writeShort(87);
                    } else if (player.isTitleUse && !player.isTitleUse2) {
                        me.writer().writeShort(87);
                    } else if (!player.isTitleUse && !player.isTitleUse2) {
                        me.writer().writeShort(88);
                    }
                }
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 888) {
                if (player.lastTimeTitle1 > 0 && player.isTitleUse) {
                    me.writer().writeShort(60);
                    // System.out.println("60 duoi");
                }
            } else if (id == 889) {
                if (player.lastTimeTitle2 > 0 && player.isTitleUse2) {
                    if (!player.isTitleUse || !player.isTitleUse && player.lastTimeTitle1 == 0) {
                        me.writer().writeShort(85);
                        // System.out.println("85 duoi");
                    } else if (player.isTitleUse || (player.isTitleUse && player.lastTimeTitle1 > 0)) {
                        me.writer().writeShort(66);
                        // System.out.println("66 giua");
                    }
                }
            } else if (id == 890) {
                if (player.lastTimeTitle3 > 0 && player.isTitleUse3) {
                    if ((!player.isTitleUse || !player.isTitleUse && player.lastTimeTitle1 == 0)
                            && (!player.isTitleUse2 || !player.isTitleUse2 && player.lastTimeTitle2 == 0)) {
                        me.writer().writeShort(88);
                        // System.out.println("88 duoi");
                    }
                    if ((player.isTitleUse || player.isTitleUse && player.lastTimeTitle1 > 0)
                            && (!player.isTitleUse2 || !player.isTitleUse2 && player.lastTimeTitle2 == 0)) {
                        me.writer().writeShort(87);
                        // System.out.println("87 giua");
                    }
                    if ((!player.isTitleUse || !player.isTitleUse && player.lastTimeTitle1 == 0)
                            && (player.isTitleUse2 || player.isTitleUse2 && player.lastTimeTitle2 == 0)) {
                        me.writer().writeShort(87);
                        // System.out.println("87 giua");
                    }
                    if ((player.isTitleUse || player.isTitleUse && player.lastTimeTitle1 == 0)
                            && (player.isTitleUse2 || player.isTitleUse2 && player.lastTimeTitle2 == 0)) {
                        me.writer().writeShort(73);
                        // System.out.println("73 tren");
                    }
                }
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListTop(Player player, List<TOP> tops) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
                msg.writer().writeUTF(top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBigBoss(Zone map, int action, int size, int id, int dame) {
        Message msg = null;
        try {
            msg = new Message(102);
            msg.writer().writeByte(action);
            if (action != 6 && action != 7) {
                msg.writer().writeByte(size); // SIZE PLAYER ATTACK
                msg.writer().writeInt(id); // PLAYER ID
                msg.writer().writeInt(dame); // DAME
            }
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendPetFollow(Player pl, int type, int idImage, byte frame, short wImg, short hImg, Session session,
            byte... nFr) {
        Message msg = null;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(type);
            msg.writer().writeShort(idImage);
            msg.writer().writeByte(frame);
            if (frame > 0) {
                msg.writer().writeByte(nFr.length);
                for (byte i = 0; i < nFr.length; i++) {
                    msg.writer().writeByte(nFr[i]);
                }
                msg.writer().writeShort(wImg);
                msg.writer().writeShort(hImg);
            }
            sendMessAllPlayerInMap(pl.zone, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void sendPetFollowToMe(Player me, Player pl) {
    // Item linhThu = pl.inventory.itemsBody.get(8);
    // if (!linhThu.isNotNullItem()) {
    // return;
    // }
    // short smallId = (short) (linhThu.template.iconID - 1);
    // Message msg;
    // try {
    // msg = new Message(31);
    // msg.writer().writeInt((int) pl.id);
    // msg.writer().writeByte(1);
    // msg.writer().writeShort(smallId);
    // msg.writer().writeByte(1);
    // int[] fr = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    // msg.writer().writeByte(fr.length);
    // for (int i = 0; i < fr.length; i++) {
    // msg.writer().writeByte(fr[i]);
    // }
    // msg.writer().writeShort(smallId == 15067 ? 65 : 75);
    // msg.writer().writeShort(smallId == 15067 ? 65 : 75);
    // me.sendMessage(msg);
    // msg.cleanup();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    public void sendMessAllPlayer(Message msg) {
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        if (zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = zone.getPlayers();
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        for (Player pl : players) {
            if (pl != null) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    public void sendMessAllPlayerInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        if (MapService.gI().isMapOffline(player.zone.map.mapId)) {
            if (player.isPet) {
                ((Pet) player).master.sendMessage(msg);
            } else {
                player.sendMessage(msg);
            }
        } else {
            List<Player> players = player.zone.getPlayers();
            if (players.isEmpty()) {
                msg.dispose();
                return;
            }
            for (int i = 0; i < players.size(); i++) {
                Player pl = players.get(i);
                if (pl != null) {
                    pl.sendMessage(msg);
                }
            }
        }
        msg.cleanup();
    }

    public void regisAccount(Session session, Message _msg) {
        try {
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            String user = _msg.readUTF();
            String pass = _msg.readUTF();
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 6 && pass.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Mật khẩu phải có độ dài 6-18 ký tự");
                return;
            }
            GirlkunResultSet rs = GirlkunDB.executeQuery("select * from account where username = ?", user);
            if (rs.first()) {
                sendThongBaoOK((MySession) session, "Tài khoản đã tồn tại");
            } else {
                pass = pass;
                GirlkunDB.executeUpdate("insert into account (username, password) values()", user, pass);
                sendThongBaoOK((MySession) session, "Đăng ký tài khoản thành công !!");
            }
            rs.dispose();
        } catch (Exception e) {

        }
    }

    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = player.zone.getPlayers();
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        for (Player pl : players) {
            if (pl != null && !pl.equals(player)) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();

    }

    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);// Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(pl.nPoint.hp);
            msg.writer().writeByte(0);// Hiệu ứng Ăn Đậu
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(pl.nPoint.hp);
            msg.writer().writeByte(1);
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void loginDe(MySession session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void switchToRegisterScr(ISession session) {
        try {
            Message message;
            try {
                message = new Message(42);
                message.writeByte(0);
                session.sendMessage(message);
                message.cleanup();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    public void chat(Player player, String text) {
        if (player.getSession() != null && player.isAdmin()) {
            if (text.equals("loadsv")) {
                Manager.loadPart();
                DataGame.updateData(player.getSession());
                DataGame.updateSkill(player.getSession());
                DataGame.updateMap(player.getSession());

                return;
            }
            if (text.equals("hskill")) {
                Service.getInstance().releaseCooldownSkill(player);
                return;
            }
            if (text.equals("skillxd")) {
                SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                return;
            }
            if (text.equals("skilltd")) {
                SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                return;
            }
            if (text.equals("skillnm")) {
                SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                return;
            }
            if (text.equals("client")) {
                Client.gI().show(player);
            } else if (text.equals("map")) {
                sendThongBao(player, "Thông tin map: " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                return;
            } else if (text.equals("vt")) {
                sendThongBao(player, player.location.x + " - " + player.location.y + "\n"
                        + player.zone.map.yPhysicInTop(player.location.x, player.location.y));
            } else if (text.equals("hs")) {
                player.nPoint.setFullHpMp();
                PlayerService.gI().sendInfoHpMp(player);
                sendThongBao(player, "Quyền năng trị liệu\n");
                return;
            } else if (text.equals("b")) {
                Message msg;
                try {
                    msg = new Message(52);
                    msg.writer().writeByte(0);
                    msg.writer().writeInt((int) player.id);
                    sendMessAllPlayerInMap(player, msg);
                    msg.cleanup();
                } catch (Exception e) {
                }
            } else if (text.equals("c")) {
                Message msg;
                try {
                    msg = new Message(52);
                    msg.writer().writeByte(2);
                    msg.writer().writeInt((int) player.id);
                    msg.writer().writeInt((int) player.zone.getHumanoids().get(1).id);
                    sendMessAllPlayerInMap(player, msg);
                    msg.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (text.equals("nrnm")) {
                Service.gI().activeNamecShenron(player);
            }
            if (text.equals("ts")) {
                sendThongBao(player, "Time start server: " + ServerManager.timeStart + "\n");
                return;
            }
            if (text.equals("boss")) {
                BossManager.gI().showListBoss(player);
                return;
            }
            if (text.equals("cmd")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, 1139,
                        "Quyền năng Admin: " + Client.gI().getPlayers().size() + "\n",
                        "Ngọc Rồng", "Đệ tử", "Bảo trì", "Tìm kiếm\nngười chơi", "Boss", "Giftcode", "Đóng");
                return;
            }
            if (text.equals("addtn")) {
                player.nPoint.powerUp(200000000);
                player.nPoint.tiemNangUp(200000000);
                PlayerDAO.updatePlayer(player);
                return;
            } else if (text.startsWith("fly ")) {
                try {
                    int mapId = Integer.parseInt(text.replace("fly ", ""));
                    ChangeMapService.gI().changeMapBySpaceShip(player, mapId, -1, -1);
                    sendThongBao(player, "" + player.name + " đã bay đến: " + player.zone.map.mapName + " ("
                            + player.zone.map.mapId + ")");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("m ")) {
                try {
                    int mapId = Integer.parseInt(text.replace("m ", ""));
                    ChangeMapService.gI().changeMapBySpaceShip(player, mapId, -1, -1);
                    // sendThongBao(player, "" + player.name + " đã dịch chuyển tức thời đến: " +
                    // player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("baotri_giay ")) {
                int min = Integer.parseInt(text.replace("baotri_giay ", ""));
                Maintenance.gI().start(min);
                return;
            } else if (text.startsWith("baotri_phut ")) {
                int min = Integer.parseInt(text.replace("baotri_phut ", ""));
                Maintenance.gI().start(min * 60);
                return;
            } else if (text.startsWith("danhhieu1")) {
                if (player.lastTimeTitle3 == 0) {
                    player.lastTimeTitle3 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);
                } else {
                    player.lastTimeTitle3 += (1000 * 60 * 60 * 24 * 3);
                }
                player.isTitleUse3 = true;
                Service.gI().point(player);
                Service.gI().sendTitle(player, 890);
                Service.gI().sendThongBao(player, "Bạn nhận được 3 ngày danh hiệu !");
                return;
            } else if (text.startsWith("danhhieu2")) {
                if (player.lastTimeTitle2 == 0) {
                    player.lastTimeTitle2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);
                } else {
                    player.lastTimeTitle2 += (1000 * 60 * 60 * 24 * 3);
                }
                player.isTitleUse2 = true;
                Service.gI().point(player);
                Service.gI().sendTitle(player, 889);
                Service.gI().sendThongBao(player, "Bạn nhận được 3 ngày danh hiệu !");
                return;
            } else if (text.startsWith("danhhieu3")) {
                if (player.lastTimeTitle1 == 0) {
                    player.lastTimeTitle1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);
                } else {
                    player.lastTimeTitle1 += (1000 * 60 * 60 * 24 * 3);
                }
                player.isTitleUse = true;
                Service.gI().point(player);
                Service.gI().sendTitle(player, 888);
                Service.gI().sendThongBao(player, "Bạn nhận được 3 ngày danh hiệu !");
                return;
            } else if (text.startsWith("get ")) {
                int itemId = Integer.parseInt(text.replace("get ", ""));
                Item item = ItemService.gI().createNewItem(((short) itemId));
                ItemShop it = new Shop().getItemShop(itemId);
                if (it != null && !it.options.isEmpty()) {
                    item.itemOptions.addAll(it.options);
                }
                InventoryServiceNew.gI().addItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player,
                        "Đã lấy " + item.template.name + " [" + item.template.id + "]  ra từ kho đồ!");

            } else if (text.startsWith("sieunhangao")) {
                EffectSkillService.gI().sendEffectPlayer(player, player, TURN_ON_EFFECT, HAOQUANG_EFFECT);
            } else if (text.startsWith("getsl ")) {
                String[] parts = text.split(" ");
                if (parts.length < 3) {
                    Service.gI().sendThongBao(player, "Vui lòng nhập số lượng!");
                    return;
                }
                int itemId = Integer.parseInt(parts[1]);
                long quantity = Long.parseLong(parts[2]);
                if (quantity > 2000000000) {
                    Service.gI().sendThongBao(player, "Không thể lấy số lượng vượt quá 2 tỷ!");
                    return;
                }
                Item item = ItemService.gI().createNewItem((short) itemId);
                item.quantity = (int) quantity;
                ItemShop itemShop = new Shop().getItemShop(itemId);
                if (itemShop != null && !itemShop.options.isEmpty()) {
                    item.itemOptions.addAll(itemShop.options);
                }
                InventoryServiceNew.gI().addItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player,
                        "Đã lấy vật phẩm: " + item.template.name + " Số lượng: " + quantity + " ra từ kho đồ!");
            } else if (text.equals("itembuff")) {// ???
                Input.gI().createFormGiveItem(player);
            } else if (text.equals("enddoanhtrai")) {// ???
                player.clan.doanhTrai_haveGone = true;
                player.clan.doanhTrai.setLastTimeOpen(System.currentTimeMillis() + 350000);
                player.clan.doanhTrai.DropNgocRong();
                for (Player pl : player.clan.membersInGame) {
                    ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 30);
                }
                player.clan.doanhTrai.timePickDragonBall = true;
            } else if (text.equals("getz")) {
                Input.gI().createFormSenditem1(player);
            } else if (text.equals("thread")) {
                sendThongBao(player, "Current thread: " + Thread.activeCount());
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                return;
            } else if (text.startsWith("s")) {
                try {
                    player.nPoint.speed = (byte) Integer.parseInt(text.substring(1));
                    point(player);
                    return;
                } catch (Exception e) {
                }
            }
        }

        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
        }

        if (player.pet != null) {
            if (text.equals("di theo") || text.equals("follow")) {
                player.pet.changeStatus(Pet.FOLLOW);
            } else if (text.equals("bao ve") || text.equals("protect")) {
                player.pet.changeStatus(Pet.PROTECT);
            } else if (text.equals("tan cong") || text.equals("attack")) {
                player.pet.changeStatus(Pet.ATTACK);
            } else if (text.equals("ve nha") || text.equals("go home")) {
                player.pet.changeStatus(Pet.GOHOME);
            } else if (text.equals("bien hinh")) {
                player.pet.transform();
            }
        }

        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void Transport(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-105);
            msg.writer().writeShort(pl.maxTime);
            msg.writer().writeByte(pl.type);
            pl.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public long exp_level1(long sucmanh) {
        if (sucmanh < 3000) {
            return 3000;
        } else if (sucmanh < 15000) {
            return 15000;
        } else if (sucmanh < 40000) {
            return 40000;
        } else if (sucmanh < 90000) {
            return 90000;
        } else if (sucmanh < 170000) {
            return 170000;
        } else if (sucmanh < 340000) {
            return 340000;
        } else if (sucmanh < 700000) {
            return 700000;
        } else if (sucmanh < 1500000) {
            return 1500000;
        } else if (sucmanh < 15000000) {
            return 15000000;
        } else if (sucmanh < 150000000) {
            return 150000000;
        } else if (sucmanh < 1500000000) {
            return 1500000000;
        } else if (sucmanh < 5000000000L) {
            return 5000000000L;
        } else if (sucmanh < 10000000000L) {
            return 10000000000L;
        } else if (sucmanh < 40000000000L) {
            return 40000000000L;
        } else if (sucmanh < 50010000000L) {
            return 50010000000L;
        } else if (sucmanh < 60010000000L) {
            return 60010000000L;
        } else if (sucmanh < 70010000000L) {
            return 70010000000L;
        } else if (sucmanh < 80010000000L) {
            return 80010000000L;
        } else if (sucmanh < 200010000000L) {
            return 200010000000L;
        }
        return 1000;
    }

    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writer().writeInt(player.nPoint.hpg);
                msg.writer().writeInt(player.nPoint.mpg);
                msg.writer().writeInt(player.nPoint.dameg);
                msg.writer().writeInt(player.nPoint.hpMax);// hp full
                msg.writer().writeInt(player.nPoint.mpMax);// mp full
                msg.writer().writeInt(player.nPoint.hp);// hp
                msg.writer().writeInt(player.nPoint.mp);// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writer().writeInt(player.nPoint.dame);// dam base
                msg.writer().writeInt(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writer().writeShort(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    private void activeNamecShenron(Player pl) {
        Message msg;
        try {

            SummonDragon.gI().summonNamec(pl);
            msg = new Message(-83);
            msg.writer().writeByte(0);

            msg.writer().writeShort(pl.zone.map.mapId);
            msg.writer().writeShort(pl.zone.map.bgId);
            msg.writer().writeByte(pl.zone.zoneId);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeUTF("");
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            msg.writer().writeByte(1);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
        } catch (Exception e) {
        }
    }

    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0); // cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            // --------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            // ---vang---luong--luongKhoa
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            // --------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            // --------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            // --------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            // -----------------
            DataGame.sendHeadAvatar(msg);
            // -----------------
            msg.writer().writeShort(514); // char info id - con chim thông báo
            msg.writer().writeShort(515); // char info id
            msg.writer().writeShort(537); // char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); // nhập thể
            msg.writer().writeInt(333); // deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); // is new member
            msg.writer().writeShort(pl.getAura()); // idauraeff
            msg.writer().writeByte(pl.getEffFront());
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;

            param = master.nPoint.calSubTNSM(param);
            master.nPoint.powerUp(param);
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 1";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 149999999999L) {
            return "Thần Hủy Diệt";
        } else if (sucmanh <= 210999999999L) {
            return "Thần Thiên Sứ";
        }
        return "Đại Thiên Sứ";
    }

    public int getCurrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return 1;
        } else if (sucmanh < 15000) {
            return 2;
        } else if (sucmanh < 40000) {
            return 3;
        } else if (sucmanh < 90000) {
            return 4;
        } else if (sucmanh < 170000) {
            return 5;
        } else if (sucmanh < 340000) {
            return 6;
        } else if (sucmanh < 700000) {
            return 7;
        } else if (sucmanh < 1500000) {
            return 8;
        } else if (sucmanh < 15000000) {
            return 9;
        } else if (sucmanh < 150000000) {
            return 10;
        } else if (sucmanh < 1500000000) {
            return 11;
        } else if (sucmanh < 5000000000L) {
            return 12;
        } else if (sucmanh < 10000000000L) {
            return 13;
        } else if (sucmanh < 40000000000L) {
            return 14;
        } else if (sucmanh < 50010000000L) {
            return 15;
        } else if (sucmanh < 60010000000L) {
            return 16;
        } else if (sucmanh < 70010000000L) {
            return 17;
        } else if (sucmanh < 80010000000L) {
            return 18;
        } else if (sucmanh < 100010000000L) {
            return 19;
        } else if (sucmanh < 150010000000L) {
            return 20;
        } else if (sucmanh < 210999999999L) {
            return 21;
        }
        return 21;
    }

    public void hsChar(Player pl, int hp, int mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet && !pl.isNewPet) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }

            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(hp);
            msg.writer().writeInt(mp);
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet && !pl.isNewPet) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else if (pl.isPet) {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }
            if (!pl.isPet && !pl.isBoss && pl.idNRNM != -1) {
                ItemMap itemMap = new ItemMap(pl.zone, pl.idNRNM, 1, pl.location.x, pl.location.y, -1);
                Service.gI().dropItemMap(pl.zone, itemMap);
                NgocRongNamecService.gI().pNrNamec[pl.idNRNM - 353] = "";
                NgocRongNamecService.gI().idpNrNamec[pl.idNRNM - 353] = -1;
                pl.idNRNM = -1;
                PlayerService.gI().changeAndSendTypePK(pl, ConstPlayer.NON_PK);
                Service.gI().sendFlagBag(pl);
            }
            if (pl.zone.map.mapId == 51) {
                ChangeMapService.gI().changeMapBySpaceShip(pl, 21 + pl.gender, 0, -1);
            }
            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); // cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob, null);
                    break;
                }
            }
        }
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();

                msg.writer().writeShort(head);// set head
                msg.writer().writeShort(body);// setbody
                msg.writer().writeShort(leg);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendFlagBag(Player pl) {
        Message msg;
        try {
            int FlagBag = pl.getFlagBag();
            if (pl.isPl() && pl.getSession().version >= 225) {
                switch (FlagBag) {
                    case 104:
                        FlagBag = 206;
                        break;
                }
            }
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(FlagBag);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet || pl.isNewPet) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendThongBaoOK(MySession session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {

        }
    }

    public void sendEffPlayer(Player pl, Player plReceive, int idEff, int layer, int loop, int loopCount) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(idEff);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopCount);
            msg.writer().writeByte(0);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 7710, text);
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendThongBao(List<Player> pl, String thongBao) {
        for (int i = 0; i < pl.size(); i++) {
            Player ply = pl.get(i);
            if (ply != null) {
                this.sendThongBao(ply, thongBao);
            }
        }
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            sendMessAllPlayerIgnoreMe(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public static final int[] flagTempId = { 363, 364, 365, 366, 367, 368, 369, 370, 371 };
    public static final int[] flagIconId = { 2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331 };

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changeFlag(Player pl, int index) {
        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                Service.gI().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.gI().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            pl.iDMark.setLastTimeChangeFlag(System.currentTimeMillis());
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chooseFlag(Player pl, int index) {
        if (MapService.gI().isMapBlackBallWar(pl.zone.map.mapId) || pl.zone.map.mapId == 196
                || MapService.gI().isMapMaBu(pl.zone.map.mapId) || MapService.gI().isMapPVP(pl.zone.map.mapId)) {
            sendThongBao(pl, "Không thể đổi cờ lúc này!");
            return;
        }
        if (Util.canDoWithTime(pl.iDMark.getLastTimeChangeFlag(), 60000)) {
            changeFlag(pl, index);
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi "
                    + TimeUtil.getTimeLeft(pl.iDMark.getLastTimeChangeFlag(), 20) + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null, null);
    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);
                int timeLeft = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        int countOption = item.itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : item.itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeShort(iop.param);
                        }
                    }
                }

                msg.writer().writeInt(pl.pet.nPoint.hp); // hp
                msg.writer().writeInt(pl.pet.nPoint.hpMax); // hpfull
                msg.writer().writeInt(pl.pet.nPoint.mp); // mp
                msg.writer().writeInt(pl.pet.nPoint.mpMax); // mpfull
                msg.writer().writeInt(pl.pet.nPoint.dame); // damefull
                msg.writer().writeUTF(pl.pet.name); // name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); // curr level
                msg.writer().writeLong(pl.pet.nPoint.power); // power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); // tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); // status
                msg.writer().writeShort(pl.pet.nPoint.stamina); // stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); // stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); // crit
                msg.writer().writeShort(pl.pet.nPoint.def); // def
                PetService.updatePet(pl,ConstInfor.maxSkillPet);
                int sizeSkill = pl.pet.playerSkill.skills.size();
                msg.writer().writeByte(sizeSkill); // counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        switch (i) {
                            case 1:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 150 Tr sức mạnh để mở");
                                break;
                            case 2:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 1,5 Tỉ sức mạnh để mở");
                                break;
                            case 3:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 20 Tỉ sức mạnh để mở");
                                break;
                            case 4:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 30 Tỉ sức mạnh để mở");
                                break;
                            case 5:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 50 Tỉ sức mạnh để mở");
                                break;
                            default:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 80 tỉ sức mạnh để mở");
                                break;
                        }
                    }
                }

                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.gI().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);
            }
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|7|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); // bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 6) {
                if (newPass.equals(rePass)) {
                    String hashedPassword = newPass;
                    player.getSession().pp = hashedPassword;
                    try {
                        GirlkunDB.executeUpdate("update account set password = ? where id = ? and username = ?",
                                hashedPassword, player.getSession().userId, player.getSession().uu);
                        Service.getInstance().sendThongBao(player, "Đổi mật khẩu thành công!");
                    } catch (Exception ex) {
                        Service.getInstance().sendThongBao(player, "Đổi mật khẩu thất bại!");
                        Logger.logException(Service.class, ex);
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Mật khẩu ít nhất 6 ký tự!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(MySession session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendCaption(MySession session, byte gender) {
        Message msg;
        try {
            msg = new Message(-41);
            msg.writer().writeByte(Manager.CAPTIONS.size());
            for (String caption : Manager.CAPTIONS) {
                msg.writer().writeUTF(caption.replaceAll("%1", gender == ConstPlayer.TRAI_DAT ? "Trái đất"
                        : (gender == ConstPlayer.NAMEC ? "Namếc" : "Xayda")));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendWaitToLogin(MySession session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendMessage(MySession session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void setClientType(MySession session, Message msg) {
        try {
            session.typeClient = (msg.reader().readByte());// client_type
            session.zoomLevel = msg.reader().readByte();// zoom_level
            msg.reader().readBoolean();// is_gprs
            msg.reader().readInt();// width
            msg.reader().readInt();// height
            msg.reader().readBoolean();// is_qwerty
            msg.reader().readBoolean();// is_touch
            String platform = msg.reader().readUTF();
            String[] arrPlatform = platform.split("\\|");
            session.version = Integer.parseInt(arrPlatform[1].replaceAll("\\.", ""));

            // System.out.println(platform);
        } catch (Exception e) {
        } finally {
            msg.cleanup();
        }
        // DataGame.sendLinkIP(session);
    }

    public void DropVeTinh(Player pl, Item item, Zone map, int x, int y) {
        ItemMap itemMap = new ItemMap(map, item.template, item.quantity, x, y, pl.id);
        itemMap.options = item.itemOptions;
        map.addItem(itemMap);
        Message msg = null;
        try {
            msg = new Message(68);
            msg.writer().writeShort(itemMap.itemMapId);
            msg.writer().writeShort(itemMap.itemTemplate.id);
            msg.writer().writeShort(itemMap.x);
            msg.writer().writeShort(itemMap.y);
            msg.writer().writeInt(-2);
            msg.writer().writeShort(200);
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception exception) {

        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }
}
