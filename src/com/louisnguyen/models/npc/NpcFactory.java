package com.louisnguyen.models.npc;

import static com.louisnguyen.services.func.SummonDragon.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.louisnguyen.MaQuaTang.MaQuaTangManager;
import com.louisnguyen.consts.ConstMap;
import com.louisnguyen.consts.ConstNpc;
import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.consts.ConstTask;
import com.louisnguyen.jdbc.daos.PlayerDAO;
import com.louisnguyen.kygui.ShopKyGuiService;
import com.louisnguyen.models.BangHoi.Clan;
import com.louisnguyen.models.BangHoi.ClanMember;
import com.louisnguyen.models.boss.Boss;
import com.louisnguyen.models.boss.BossID;
import com.louisnguyen.models.boss.BossManager;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.map.ConDuongRanDoc.ConDuongRanDoc;
import com.louisnguyen.models.map.DoanhTraiDocNhan.DoanhTrai;
import com.louisnguyen.models.map.DoanhTraiDocNhan.DoanhTraiService;
import com.louisnguyen.models.map.GiaiCuuMiNuong.GiaiCuuMiNuongService;
import com.louisnguyen.models.map.TranhNgocSaoDen.BlackBallWar;
import com.louisnguyen.models.map.bandokhobau.BanDoKhoBau;
import com.louisnguyen.models.map.bandokhobau.BanDoKhoBauService;
import com.louisnguyen.models.matches.PVPService;
import com.louisnguyen.models.mob.Mob;
import com.louisnguyen.models.npc.specialnpc.Egg;
import com.louisnguyen.models.npc.specialnpc.MabuEgg;
import com.louisnguyen.models.player.NPoint;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.models.shop.ShopServiceNew;
import com.louisnguyen.server.Client;
import com.louisnguyen.server.Maintenance;
import com.louisnguyen.server.Manager;
import com.louisnguyen.services.ClanService;
import com.louisnguyen.services.FriendAndEnemyService;
import com.louisnguyen.services.IntrinsicService;
import com.louisnguyen.services.InventoryServiceNew;
import com.louisnguyen.services.ItemMapService;
import com.louisnguyen.services.ItemService;
import com.louisnguyen.services.ItemTimeService;
import com.louisnguyen.services.MapService;
import com.louisnguyen.services.NapVangService;
import com.louisnguyen.services.NgocRongNamecService;
import com.louisnguyen.services.NpcService;
import com.louisnguyen.services.OpenPowerService;
import com.louisnguyen.services.PetService;
import com.louisnguyen.services.PlayerService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.TaskService;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.services.func.CombineServiceNew;
import com.louisnguyen.services.func.Input;
import com.louisnguyen.services.func.LuckyRound;
import com.louisnguyen.services.func.SummonDragon;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.TimeUtil;
import com.louisnguyen.utils.Util;

public class NpcFactory {

    private static final int COST_HD = 50000000;
    int rubyCost = 50;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    public int Gio;

    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    ///////////////////////////////// =Trọng
    ///////////////////////////////// Tài=////////////////////////////////////////////////////////
    private static Npc TrongTai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.zone.map.mapId == 13) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Đại chiến bang hội toàn vũ trụ đang được diễn ra\n"
                                        + "Ngươi muốn ta giúp gì?",
                                "Tham gia", "Cửa hàng\nHồng ngọc", "Bảng xếp\nhạng", "Hướng\ndẫn");
                        return;
                    }
                    this.createOtherMenu(player, 99, "Ngươi vừa vào chiến trường lại muốn chạy trốn rồi cơ à?",
                            "Về\nĐảo guru");
                    return;
                }
            }

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            int Gio = calendar.get(Calendar.HOUR_OF_DAY);

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && player.zone.map.mapId == 13) {
                        switch (select) {

                            case 0:
                                if (Gio >= 7 && Gio <= 8) {
                                    if (player.ThamGiaDaiChienBangHoi == 0) {
                                        this.createOtherMenu(player, 251,
                                                "Mỗi ngày ngươi có một lượt tham gia miễn phí\n"
                                                        + "Ngươi sẽ có 15 phút để tham gia, ngươi đã sẵn sàng?",
                                                "Tham gia\n(Miễn phí)", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, 252,
                                                "Ngươi có muốn trả phí để tiếp tục tham gia không?",
                                                "Tham gia\n(" + player.ThamGiaDaiChienBangHoi * 20 + "Tr vàng)",
                                                "Từ chối");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Đại chiến bang hội mở vào lúc 19h59 - 23h59 hằng ngày");
                                }
                                return;
                            case 1:
                                ShopServiceNew.gI().opendShop(player, "TRONGTAIGURU", true);
                                break;
                            case 2:
                                Service.gI().sendThongBaoOK(player, "Chức năng đang bảo trì!!!");
                                break;
                            case 3:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.DAI_CHIEN_BANG_HOI);
                            case 4:
                                if (Gio >= 7 && Gio < 8) {
                                    System.out.println(
                                            "Thời gian hiện tại nằm trong khoảng từ 8 giờ sáng đến 6 giờ tối.");
                                } else {
                                    System.out.println(
                                            "Thời gian hiện tại không nằm trong khoảng từ 8 giờ sáng đến 6 giờ tối.");
                                }
                        }
                    }
                    if (player.iDMark.getIndexMenu() == 99) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 13, -1, 312);
                        }
                    }
                    if (player.iDMark.getIndexMenu() == 251) {
                        int vangconthieu;
                        vangconthieu = (int) (player.inventory.gold - (player.ThamGiaDaiChienBangHoi * 20_000_000));
                        switch (select) {
                            case 0:
                                if (player.inventory.gold >= (player.ThamGiaDaiChienBangHoi * 20_000_000)) {
                                    player.inventory.gold -= player.ThamGiaDaiChienBangHoi * 20_000_000;
                                    Service.gI().changeFlag(player, 8);
                                    player.HoiSinhDaiChienBangHoi = 5;
                                    Service.gI().sendMoney(player);
                                    player.ThamGiaDaiChienBangHoi++;
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 196, -1, 312);
                                    return;
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn còn thiếu " + vangconthieu + " vàng");
                                }
                        }
                    }
                    if (player.iDMark.getIndexMenu() == 252) {
                        switch (select) {
                            case 0:
                                Service.gI().changeFlag(player, 8);
                                player.HoiSinhDaiChienBangHoi = 5;
                                player.inventory.gold -= player.ThamGiaDaiChienBangHoi * 20_000_000;
                                player.ThamGiaDaiChienBangHoi++;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 196, -1, 312);
                                return;
                        }
                    }
                }
            }
        };
    }

    private static Npc Mai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                        "Mọi người ơi, mọi\n"
                                + "người ơi",
                        "Xin hãy đi với em, Ca\n"
                                + "lích đột nhiên mất tích\n"
                                + "rồi",
                        "Em đã tìm khắp nơi mà\n"
                                + "không gặp cậu ấy, Các\n"
                                + "anh hãy tìm Ca lích\n"
                                + "giúp em."
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 10000, 10000);
            }

            @Override
            public void openBaseMenu(Player player) {
                chatWithNpc(player);
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hiện tại Ca lích đã mất tích\n"
                            + "Em sẽ đưa các anh tới Hành Tinh Ngục Tù\n"
                            + "Để giải cứu Ca lích cùng với em nhé!!", "Đồng ý!!", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.power >= 80000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 155, 265, 792);
                                } else {
                                    Service.gI().sendThongBao(player, "Yêu cầu đạt 80 tr sức mạnh!!!");
                                    break;
                                }
                                return;
                        }
                    }
                }
            }
        };
    }

    private static Npc Fu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Khi tiêu diệt Mr. Evil Xayda có cơ hội thu thập được đá ngục tù, khi đủ 99 viên đá ngục tù hãy gặp ta để quy đổi phần thưởng nhé!!!",
                            "Đổi bùa\nNâng cấp", "Đổi thẻ\nGia hạn", "Đổi bùa\nGiải P.Sư");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                Item DaNgucTu = InventoryServiceNew.gI().findItemBag(player, 1196);
                Item BuaNangCap = ItemService.gI().createNewItem((short) 1159);
                Item TheGiaHan = ItemService.gI().createNewItem((short) 1160);
                Item BuaGiaiPhapSu = ItemService.gI().createNewItem((short) 1161);
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (DaNgucTu != null && DaNgucTu.quantity >= 99) {
                                    Service.gI().sendThongBaoOK(player,
                                            "Hiện bùa nâng cấp đang được phát triển, vui lòng đổi vật phẩm khác!!!");
                                    break;
                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                            "Hiện bùa nâng cấp đang được phát triển, vui lòng đổi vật phẩm khác!!!");
                                    return;
                                }
                            case 1:
                                if (DaNgucTu != null && DaNgucTu.quantity >= 99) {
                                    TheGiaHan.quantity = 1;
                                    TheGiaHan.itemOptions.add(new Item.ItemOption(86, 1));
                                    TheGiaHan.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, TheGiaHan);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, DaNgucTu, 99);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được " + TheGiaHan.Name());
                                    break;
                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bạn không đủ đá ngục tù để quy đổi vật phẩm!!!");
                                    return;
                                }
                            case 2:
                                if (DaNgucTu != null && DaNgucTu.quantity >= 99) {
                                    BuaGiaiPhapSu.quantity = 1;
                                    BuaGiaiPhapSu.itemOptions.add(new Item.ItemOption(86, 1));
                                    BuaGiaiPhapSu.itemOptions.add(new Item.ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, BuaGiaiPhapSu);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, DaNgucTu, 99);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được " + BuaGiaiPhapSu.Name());
                                    break;
                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bạn không đủ đá ngục tù để quy đổi vật phẩm!!!");
                                    return;
                                }
                        }
                    }
                }
            }
        };
    }

    /////////////////////////////////////////// NPC Quy Lão
    /////////////////////////////////////////// Kame///////////////////////////////////////////
    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                        "Là lá la",
                        "La lá là",
                        "Lá là la"
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 10000, 10000);
            }

            @Override
            public void openBaseMenu(Player player) {
                chatWithNpc(player);
                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                    if (canOpenNpc(player)) {
                        this.createOtherMenu(player, 13, "Ngươi tìm ta có việc gì?", "Chức năng\nBang hội",
                                "Kho báu\ndưới biển", "Hang sói", "Đảo trên trời");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == 13) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, ConstNpc.CHUC_NANG_BANG_HOI,
                                        "Ta có hỗ trợ những chức năng Bang hội, nhà ngươi cần gì?",
                                        "Giải tán\nBang hội", "Nâng cấp\nBang hội", "Từ chối");
                                break;
                            case 1:
                                // if (player.clan != null && player.clan.BanDoKhoBau == null || player.clan ==
                                // null) {
                                // this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                // "Đây là bản đồ kho báu hải tặc tí hon\n"
                                // + "Các con cứ yên tâm lên đường\n"
                                // + "Ở đây có ta lo\n"
                                // + "Nhớ chọn cấp độ vừa sức mình nhé", "Chọn\ncấp độ", "Từ chối");
                                // break;
                                // } else if (player.clan != null && player.clan.BanDoKhoBau != null) {
                                // this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                // "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                // + player.clan.BanDoKhoBau.level + "\nCon có muốn đi theo không?",
                                // "Đồng ý", "Từ chối");
                                // break;
                                // } else if (player.clan == null) {
                                // this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                // "Đây là bản đồ kho báu hải tặc tí hon\n"
                                // + "Các con cứ yên tâm lên đường\n"
                                // + "Ở đây có ta lo\n"
                                // + "Nhớ chọn cấp độ vừa sức mình nhé", "Chọn\ncấp độ", "Từ chối");
                                // break;
                                // }
                                // Service.gI().sendThongBaoOK(player, "Chức năng bảo trì để cho anh em tập
                                // trung đua top!!!");

                                if (player.clan == null) {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Vào bang hội trước", "Đóng");
                                    break;
                                }
                                // if (player.clan.getMembers().size() < BanDoKhoBau.N_PLAYER_CLAN) {
                                // this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                // "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                                // break;
                                // }
                                // if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                                // createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                // "Bản đồ kho báu chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi
                                // quay lại vào lúc khác",
                                // "OK");
                                // break;
                                // }

                                // if (player.bdkb_countPerDay >= 3) {
                                // createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                // "Con đã đạt giới hạn lượt đi trong ngày",
                                // "OK");
                                // break;
                                // }
                                //
                                // player.clan.banDoKhoBau_haveGone = !(System.currentTimeMillis() -
                                // player.clan.banDoKhoBau_lastTimeOpen > 10000);
                                // if (player.clan.banDoKhoBau_haveGone) {
                                // createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                // "Bang hội của con đã đi Bản Đồ lúc " +
                                // TimeUtil.formatTime(player.clan.banDoKhoBau_lastTimeOpen, "HH:mm:ss") + " hôm
                                // nay. Người mở\n"
                                // + "(" + player.clan.banDoKhoBau_playerOpen + "). Hẹn con sau 5 phút nữa",
                                // "OK");
                                // break;
                                // }
                                if (player.clan.banDoKhoBau != null) {
                                    createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                            "Bang hội của con đang tham gia Bản đồ kho báu cấp độ "
                                                    + player.clan.banDoKhoBau.level + "\n"
                                                    + "Thời gian còn lại là "
                                                    + TimeUtil.getSecondLeft(player.clan.banDoKhoBau.getLastTimeOpen(),
                                                            BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000)
                                                    + " giây. Con có muốn tham gia không?",
                                            "Tham gia", "Không");
                                    break;
                                }
                                Input.gI().createFormChooseLevelBDKB(player);
                                break;

                            case 2:
                                this.createOtherMenu(player, ConstNpc.HANG_SOI,
                                        "Hang sói là nơi xuất hiện của 3 loài Sói đến từ Vũ trụ khác\n"
                                                + "Mang tới Trái đất nhiều vật phẩm giá trị\n"
                                                + "Con có muốn vào tham chiến không?",
                                        "Đồng ý", "Từ chối");
                                break;
                            case 3:
                                this.createOtherMenu(player, 999, "Đảo trên trời hơi nguy hiểm á con, cẩn thận...",
                                        "Đồng ý", "Từ chối");
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == 999) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 170, 0, 500);
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                Object level = PLAYERID_OBJECT.get(player.id);
                                BanDoKhoBauService.gI().openBDKB(player, (int) level);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        if (select == 0) {
                            BanDoKhoBauService.gI().joinBDKB(player);
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.HANG_SOI) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.power >= 80000000000L) {
                                    Item bandosoi = InventoryServiceNew.gI().findItemBag(player, 1133);
                                    if (bandosoi != null && bandosoi.quantity > 0) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 192, -1, 144);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, bandosoi, 1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        break;
                                    } else {
                                        Service.gI().sendThongBao(player, "Cần có 1 Bản đồ Sói");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Yêu cầu sức mạnh đạt 80 tỷ");
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_BANG_HOI) {
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (!clan.isLeader(player)) {
                                            Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ!");
                                            break;
                                        }
                                        if (clan.members.size() > 1) {
                                            Service.gI().sendThongBao(player,
                                                    "Yêu cầu bang hội chỉ còn một thành viên!");
                                            break;
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.XAC_NHAN_XOA_BANG_HOI, -1,
                                                "Bạn có chắc chắn muốn giải tán bang hội?\n( Yêu cầu sẽ không thể hoàn tác )",
                                                "Đồng ý", "Từ chối!");
                                        break;
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                break;
                            case 1:
                                Clan clan2 = player.clan;
                                if (player.clan != null) {
                                    if (!clan2.isLeader(player)) {
                                        Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ!");
                                        break;
                                    }
                                    if (player.clan.level >= 0 && player.clan.level <= 9) {
                                        this.createOtherMenu(player, ConstNpc.CHUC_NANG_BANG_HOI2,
                                                "Bạn có muốn Nâng cấp lên " + (player.clan.level + 11)
                                                        + " thành viên không?\n"
                                                        + "Cần 1000 Capsule Bang\n"
                                                        + "(Thu thập Capsule Bang bằng cách tiêu diệt quái tại Map Rừng Bamboo\n"
                                                        + "cùng các thành viên khác)",
                                                "Nâng cấp\n(1 tỷ vàng)", "Từ chối");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bang của bạn đã đạt cấp tối đa!");
                                        return;
                                    }
                                    break;
                                } else if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    return;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_BANG_HOI2) {
                        Clan clan = player.clan;
                        switch (select) {
                            case 0:
                                if (player.clan.capsuleClan >= 1000 && clan.isLeader(player)
                                        && player.inventory.gold >= 1000000000L) {
                                    player.clan.level += 1;
                                    player.clan.maxMember += 1;
                                    player.clan.capsuleClan -= 1000;
                                    player.inventory.gold -= 1000000000L;
                                    player.clan.update();
                                    Service.gI().sendThongBao(player, "Yêu cầu nâng cấp bang hội thành công");
                                    break;
                                } // else if (!clan.isDeputy(player)) {
                                  // Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ");
                                  // return;
                                  // }
                                else if (player.inventory.gold < 1000000000L) {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bạn còn thiều " + (1000000000L - player.inventory.gold) + " vàng");
                                    return;
                                } else if (player.clan.capsuleClan < 1000) {
                                    Service.gI().sendThongBaoOK(player, "Bang của bạn còn thiều "
                                            + (1000 - player.clan.capsuleClan) + " Capsule bang");
                                    return;
                                }
                        }
                    }
                }

            };
        }

        ;
    }

    /////////////////////////////////////////// NPC Trưởng Lão Guru
    /////////////////////////////////////////// Namec///////////////////////////////////////////
    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override

            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    // if (TaskService.gI().getIdTask(player) == ConstTask.TASK_10_1) {
                    //     TaskService.gI().sendNextTaskMain(player);
                    //     Service.gI().sendThongBao(player, "Nhiệm vụ hoàn thành,\n"
                    //             + "báo cáo với " + TaskService.gI().transformName(player,
                    //                     ConstTask.TEN_NPC_QUY_LAO)
                    //             + " nào!");
                    //     return;
                    // }
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                        return;
                    }
                    if (TaskService.gI().getIdTask(player) == ConstTask.TASK_18_4) {
                        TaskService.gI().sendNextTaskMain(player);
                    } else if (TaskService.gI().getIdTask(player) == ConstTask.TASK_16_4) {
                        TaskService.gI().sendNextTaskMain(player);

                    }
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    ///////////////////////////////// NPC ĐẠI THIÊN
    ///////////////////////////////// SỨ/////////////////////////////////////////////////
    public static Npc DaiThienSu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                        "Từ 21h-21h30 các ngươi có 30p để chọn điều ước.",
                        "Từ 21h Rồng Vô Cực sẽ xuất hiện tại làng trước nhà",
                        "Ta sẽ gọi Rồng Vô Cực Vào lúc 21h thứ 7 hằng tuần."
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 10000, 10000);
            }

            @Override
            public void openBaseMenu(Player player) {
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    return;
                }

            }
        };
    }

    /////////////////////////////////////////// NPC Vua Vegeta
    // Xayda///////////////////////////////////////////
    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    // if (TaskService.gI().getIdTask(player) == ConstTask.TASK_10_1) {
                    //     TaskService.gI().sendNextTaskMain(player);
                    //     Service.gI().sendThongBao(player, "Nhiệm vụ hoàn thành,\n"
                    //             + "báo cáo với " + TaskService.gI().transformName(player,
                    //                     ConstTask.TEN_NPC_QUY_LAO)
                    //             + " nào!");
                    //     return;
                    // }
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                        return;
                    }
                    if (TaskService.gI().getIdTask(player) == ConstTask.TASK_18_4) {
                        TaskService.gI().sendNextTaskMain(player);
                        return;
                    } else if (TaskService.gI().getIdTask(player) == ConstTask.TASK_16_4) {
                        TaskService.gI().sendNextTaskMain(player);
                        return;
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    /////////////////////////////////////////// NPC Ký
    /////////////////////////////////////////// Gửi///////////////////////////////////////////
    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    // if (player.getSession().actived) {
                    ShopKyGuiService.gI().openShopKyGui(player);
                    return;

                    // }
                } else {
                    Service.gI().sendThongBaoOK(player,
                            "Yêu cầu phải kích hoạt tài khoản mới có thể sử dụng chức năng này!");
                    return;
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.getInstance().sendPopUpMultiLine(pl, tempId, avartar,
                                    "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 1 Thỏi vàng\bGiá trị ký gửi 1-500k Thỏi vàng");
                            break;
                        case 1:

                            // Service.gI().sendThongBaoOK(pl, "Chức năng đang được bảo trì để tối ưu!");
                    }
                }
            }
        };
    }

    /////////////////////////////////////////// NPC MR
    /////////////////////////////////////////// Popo///////////////////////////////////////////
    // public static Npc mrpopo(int mapId, int status, int cx, int cy, int tempId,
    // int avartar) {
    // return new Npc(mapId, status, cx, cy, tempId, avartar) {
    // @Override
    // public void openBaseMenu(Player player) {
    // if (canOpenNpc(player)) {
    // if (this.mapId == 0) {
    // this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Thượng đế vừa phát hiện
    // 1 loại khí đang âm thầm hủy diệt mọi mầm sông trên Trái Đất.\nGọi là Destron
    // Gas.\nTa sẽ đưa các cậu đến đó.",
    // "Đến\nKhí Gas", "Từ chối");
    // }
    // if (this.mapId == 147) {
    // this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Cậu muốn quay về làng
    // Aru ?",
    // "Đi thôi","Tạm biệt");
    // }
    // }
    // }
    //
    // @Override
    // public void confirmMenu(Player player, int select) {
    // if (canOpenNpc(player)) {
    // if (this.mapId == 0) {
    // if (player.iDMark.isBaseMenu()) {
    // switch (select) {
    // case 0:
    // break;
    // }
    // }
    // }
    // if (this.mapId == 147) {
    // if (player.iDMark.isBaseMenu()) {
    // switch (select) {
    // case 0:
    // break;
    // }
    // }
    // }
    // }
    // }
    // };
    // }
    /////////////////////////////////////////// NPC
    // Bumma///////////////////////////////////////////
    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 84) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            if (player.gender == ConstPlayer.TRAI_DAT) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                            } else {
                                NpcService.gI().createTutorial(player, this.avartar,
                                        "Ta chỉ bán trang bị cho hành tinh Trái đất");
                            }
                        }
                    } else if (this.mapId == 164) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu muốn quay lại làng Aru?", "Đồng ý", "Tạm Biệt");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // Shop

                                case 1:
                                    this.createOtherMenu(player, 1,
                                            "Cưng muốn đến khu sau làng à,",
                                            "Sau làng\nAru",
                                            "Hướng dẫn");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) {
                            switch (select) {
                                case 0:
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        if (player.getSession().actived) {
                                            if (player.nPoint.power >= 500000 && player.nPoint.power < 5000000L) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 164, -1, 144);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Xin lỗi cưng, chị chỉ cho cư dân đã đạt sức mạnh từ 500k đến 5 triệu để đi đến Sau làng Aru.",
                                                        "Tạm biệt");
                                            }
                                        } else {
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Xin lỗi cưng, chị chỉ cho cư dân đã mở thẻ Heroes Z đi đến Sau làng Aru.",
                                                    "Tạm biệt");
                                        }
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Xin lỗi cưng, chị chỉ cho cư dân Trái Đất đi đến Sau làng Aru.",
                                                "Tạm biệt");
                                    }
                                    break;
                                case 1:
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 165, -1, 144);
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Xin lỗi cưng, chị chỉ cho cư dân Trái Đất đi thôi nha.", "Tạm biệt");
                                    }
                                    break;
                                case 2:
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_BUMMA);
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Xin lỗi cưng, chị chỉ cho cư dân Trái Đất đi thôi nha.", "Tạm biệt");
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 164) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 192);
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Xin lỗi cưng, chị chỉ cho cư dân Trái Đất đi thôi nha.", "Đóng");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1,
                                        "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước",
                                        "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else if (player.gender == ConstPlayer.NAMEC) {
                            ShopServiceNew.gI().opendShop(player, "DENDE", true);
                        } else {
                            NpcService.gI().createTutorial(player, this.avartar,
                                    "Ta chỉ bán trang bị cho hành tinh Namek");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            // case 0://Shop
                            // if (player.gender == ConstPlayer.NAMEC) {
                            // ShopServiceNew.gI().opendShop(player, "DENDE", true);
                            // mít } else {
                            // this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán
                            // đồ cho dân tộc Namếc", "Đóng");
                            // }
                            // break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() +
                                        86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                                // Service.gI().sendThongBaoOK(player, "Chức năng đang bảo trì");
                                return;
                            } else {
                                Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.gender == ConstPlayer.XAYDA) {
                            ShopServiceNew.gI().opendShop(player, "APPULE", true);
                        } else {
                            NpcService.gI().createTutorial(player, this.avartar,
                                    "Ta chỉ bán trang bị cho hành tinh Xayda");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop

                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất"
                                        : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (pl.getSession().player.nPoint.power >= 5000000000L) {
                        this.createOtherMenu(pl, 2,
                                "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                    } else {
                        this.createOtherMenu(pl, 3,
                                "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                "Đến\nNamếc", "Đến\nXayda");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.getIndexMenu() == 2) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 3) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(player, this.avartar,
                                    "Hãy lên con đường cầu vồng đưa bé nhà tôi\n"
                                            + "Chắc bây giờ nó sẽ không còn sợ hãi nữa");
                        } else if (player.getSession().player.nPoint.power >= 1500000000L) {
                            this.createOtherMenu(player, 2,
                                    "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                    "Đến\nTrái đất", "Đến\nXayda", "Siêu thị");
                        } else {
                            this.createOtherMenu(player, 3,
                                    "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                    "Đến\nTrái đất", "Đến\nXayda");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == 2) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 3) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(player, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else if (this.mapId == 19) {

                            int taskId = TaskService.gI().getIdTask(player);
                            switch (taskId) {
                                case ConstTask.TASK_21_0:
                                    this.createOtherMenu(player, ConstNpc.MENU_FIND_KUKU,
                                            "Vùng đất thám hiểm nơi X2 tnsm và chứa nhiều vật phẩm quý hiếm\nKiếm bình nước tại Giải cứu mị nương hoặc mua tại Santa",
                                            "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                            "Đến Cold", "Đến\nNappa", "Từ chối");
                                    break;
                                case ConstTask.TASK_21_1:
                                    this.createOtherMenu(player, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                            "Vùng đất thám hiểm nơi X2 tnsm và chứa nhiều vật phẩm quý hiếm\nKiếm bình nước tại Giải cứu mị nương hoặc mua tại Santa",
                                            "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                            "Đến Cold", "Đến\nNappa", "Từ chối");
                                    break;
                                case ConstTask.TASK_21_2:
                                    this.createOtherMenu(player, ConstNpc.MENU_FIND_RAMBO,
                                            "Vùng đất thám hiểm nơi X2 tnsm và chứa nhiều vật phẩm quý hiếm\nKiếm bình nước tại Giải cứu mị nương hoặc mua tại Santa",
                                            "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                            "Đến Cold", "Đến\nNappa", "Từ chối");
                                    break;
                                default:
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Vùng đất thám hiểm nơi X2 tnsm và chứa nhiều vật phẩm quý hiếm\nKiếm bình nước tại Giải cứu mị nương hoặc mua tại Santa",
                                            "Đến Cold", "Đến\nNappa", "Vùng đất\nThám Hiểm", "Từ chối");

                                    break;
                            }
                        } else if (this.mapId == 192) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ngươi cần hỗ trợ gì?", "Về nhà", "Từ chối");
                        } else if (this.mapId == 68) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                        } else if (this.mapId == 184) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Đây là Vùng đất Thám Hiểm\n"
                                            + "Nơi tăng x2 tiềm năng và chứa nhiều vật phẩm quý báu\n"
                                            + "Chỉ Đệ tử mới tiêu diệt được Quái\n"
                                            + "Ngươi muốn ta giúp gì nữa không?",
                                    "Về nhà", "Đóng");
                        } else if (player.getSession().player.nPoint.power >= 1500000000L) {
                            this.createOtherMenu(player, 2,
                                    "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                        } else {
                            this.createOtherMenu(player, 3,
                                    "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nNamếc");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 184) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, -1);
                                    break;
                            }
                        }

                    }
                    if (this.mapId == 192) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, -1);
                                    break;
                            }
                        }

                    }
                    if (this.mapId == 26) {
                        if (player.iDMark.getIndexMenu() == 2) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 3) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                            }
                        }
                    }
                }
                if (this.mapId == 19) {

                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_29_1) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn cần làm nhiệm vụ\nđể mạnh mẽ hơn để có\nthể qua khu vực này");
                                    break;
                                }
                                break;
                            case 1:
                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_19_0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn cần làm nhiệm vụ\nđể mạnh mẽ hơn để có\nthể qua khu vực này");
                                    break;
                                }
                            case 2:
                                // Item binhnuoc = InventoryServiceNew.gI().findItemBag(player, 456);
                                if (player.nPoint.power >= 800000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 184, -1, 168);
                                    // InventoryServiceNew.gI().subQuantityItemsBag(player, binhnuoc, 1);
                                    // InventoryServiceNew.gI().sendItemBags(player);
                                    break;
                                }
                                if (player.nPoint.power < 800000000L) {
                                    Service.gI().sendThongBao(player, "Yêu cầu sức mạnh trên\n80 tỷ");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Yêu cầu cần 1 Bình nước");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                    boss.location.y);
                                            Service.gI().sendMoney(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_29_1) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn cần làm nhiệm vụ\nđể mạnh mẽ hơn để có\nthể qua khu vực này");
                                }
                                break;
                            case 2:
                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_19_0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn cần làm nhiệm vụ\nđể mạnh mẽ hơn để có\nthể qua khu vực này");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                    boss.location.y);
                                            Service.gI().sendMoney(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_29_1) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn cần làm nhiệm vụ\nđể mạnh mẽ hơn để có\nthể qua khu vực này");
                                }
                                break;
                            case 2:
                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_19_0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn cần làm nhiệm vụ\nđể mạnh mẽ hơn để có\nthể qua khu vực này");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                    boss.location.y);
                                            Service.gI().sendMoney(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_29_1) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn cần làm nhiệm vụ\nđể mạnh mẽ hơn để có\nthể qua khu vực này");
                                }
                                break;
                            case 2:
                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_19_0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn cần làm nhiệm vụ\nđể mạnh mẽ hơn để có\nthể qua khu vực này");
                                    break;
                                }
                        }
                    }
                }
                if (this.mapId == 68) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cửa hàng ta có bán các vật phẩm, cải trang đặc biệt cũng như có hỗ trợ\n"
                                    + "mua nhanh vật phẩm bằng Thỏi vàng\n"
                                    + "Ngươi muốn ta giúp gì?",
                            "Cửa hàng\nSanta", "Mua bằng\nThỏi vàng", "Mua bằng\nHồng ngọc");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                    int itemId = 722;
                    Item capsulehong = ItemService.gI().createNewItem(((short) itemId));
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                                // case 1:
                                // this.npcChat(player, "Ôi tôi xin lỗi, cửa hàng của tôi chưa kịp bày bán sản
                                // phẩm.");
                                // break;
                                // case 2:
                                // this.npcChat(player, "Ôi tôi xin lỗi, cửa hàng của tôi chưa kịp bày bán sản
                                // phẩm.");
                                // break;
                                case 1:
                                    createOtherMenu(player, ConstNpc.MENU_MUA_CAPSULE,
                                            "Hiện tại ta có hỗ trợ mua vật phẩm trong cửa hàng Santa bằng Thỏi vàng nhanh.\n"
                                                    + "Ngươi muốn mua vật phẩm nào?",
                                            "Capsule\nhồng ngọc");
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "SANTA_HN", true);
                                    break;
                            }
                            return;
                        }
                        if (player.iDMark.getIndexMenu() == ConstNpc.MENU_MUA_CAPSULE) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.MENU_MUA_CAPSULE1,
                                            "Giá Capsule hồng ngọc tại Cửa hàng Santa: 500Tr vàng\n"
                                                    + "Ta hỗ trợ mua với số lượng lớn quy định như sau:\n"
                                                    + "10 Capsule hồng ngọc = 10 thỏi vàng\n"
                                                    + "105 Capsule hồng ngọc = 100 thỏi vàng\n"
                                                    + "1100 Capsule hồng ngọc = 1000 thỏi vàng\n"
                                                    + "Ngươi muốn mua số lượng bao nhiêu?",
                                            "Mua 10\nCapsule\nhồng ngọc", "Mua 105\nCapsule\nhồng ngọc",
                                            "Mua 1100\nCapsule\nhồng ngọc");
                                    break;
                            }
                            return;
                        }
                    }
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_MUA_CAPSULE1) {
                        switch (select) {
                            case 0:
                                if (thoivang != null && thoivang.quantity >= 10) {
                                    capsulehong.quantity = 10;
                                    InventoryServiceNew.gI().addItemBag(player, capsulehong);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 10);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được 10 capsule hồng ngọc!");
                                    break;
                                } else {

                                    Service.getInstance().sendThongBaoOK(player, "Bạn không đủ thỏi vàng");
                                    return;
                                }
                            case 1:
                                if (thoivang != null && thoivang.quantity >= 100) {
                                    capsulehong.quantity = 105;
                                    InventoryServiceNew.gI().addItemBag(player, capsulehong);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 100);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được 105 capsule hồng ngọc!");
                                    break;
                                } else {
                                    Service.getInstance().sendThongBaoOK(player, "Bạn không đủ thỏi vàng");
                                    return;
                                }
                            case 2:
                                if (thoivang != null && thoivang.quantity >= 1000) {
                                    capsulehong.quantity = 1100;
                                    InventoryServiceNew.gI().addItemBag(player, capsulehong);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 1000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được 1100 capsule hồng ngọc!");
                                    break;
                                } else {
                                    Service.getInstance().sendThongBaoOK(player, "Bạn không đủ thỏi vàng");
                                    return;
                                }
                        }
                    }
                }
            }
        };
    }

    public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đưa cho ta thỏi vàng và ngươi sẽ mua đc oto\nĐây không phải chẵn lẻ tài xỉu đâu=)))",
                            "Xỉu", "Tài");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Input.gI().TAI(player);
                                    break;
                                case 1:
                                    Input.gI().XIU(player);
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị", "Pha lê\nhóa\ntrang bị", "Pháp sư\ntrang bị", "Nâng cấp\ntrang bị",
                                "Gia hạn\nvật phẩm", "Pha lê hóa 5 lần", "Pha lê hóa 10 lần", "Pha lê hóa 100 lần",
                                "Rèn kiếm Z");
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nhập\nNgọc Rồng", "Nâng cấp bông tai cấp 2", "Mở chỉ số bông tai cấp 2");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:
                                    this.createOtherMenu(player, ConstNpc.PHAP_SU_TRANG_BI,
                                            "Pháp sư hóa trang bị sẽ được thêm chỉ số Pháp sư\n"
                                                    + "Giải pháp sư sẽ bị xóa tất cả chỉ số Pháp sư\n"
                                                    + "Ngươi muốn làm gì?",
                                            "Pháp sư\nhóa\nTrang bị", "Giải\nPháp sư\nTrang bị");
                                    break;
                                case 3:
                                    this.createOtherMenu(player, ConstNpc.NANG_CAP_VAT_PHAM,
                                            "Ngươi muốn nâng cấp loại Trang bị nào?", "Nâng cấp\nđồ\nThần linh",
                                            "Nâng cấp\nđồ\nHủy diệt");
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.GIA_HAN_VAT_PHAM);
                                    break;
                                case 5:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHA_LE_HOA_TRANG_BI_5_LAN);
                                    break;

                                case 6:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHA_LE_HOA_TRANG_BI_10_LAN);
                                    break;
                                case 7:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHA_LE_HOA_TRANG_BI_100_LAN);
                                    break;
                                case 8:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.REN_KIEM_Z);
                                    break;
                            }
                        }
                        if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_VAT_PHAM) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_TL);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.NANG_CAP_VAT_PHAM2,
                                            "Nâng thường cần 1 trang bị Hủy diệt\n"
                                                    + "Nâng VIP cần 3 trang bị Hủy diệt bất kì. Vì thế\n"
                                                    + "Nâng VIP có tỷ lệ ra các trang bị Thần linh kích hoạt\n"
                                                    + "Ngươi muốn chọn loại nào?",
                                            "Nâng\nThường", "Nâng\nVIP");
                                    break;
                            }
                            return;
                        }
                        if (player.iDMark.getIndexMenu() == ConstNpc.PHAP_SU_TRANG_BI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PS_HOA_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.TAY_PS_HOA_TRANG_BI);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_VAT_PHAM2) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_HD);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_3_DO_HD);
                                    break;
                            }
                            return;
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_HD) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 1);
                            }
                            return;
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_3_DO_HD) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 1);
                            }
                            return;
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TL) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 1);
                            }
                            return;
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_DO_TL:
                                case CombineServiceNew.NANG_CAP_DO_HD:
                                case CombineServiceNew.NANG_CAP_3_DO_HD:
                                case CombineServiceNew.PS_HOA_TRANG_BI:
                                case CombineServiceNew.TAY_PS_HOA_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.GIA_HAN_VAT_PHAM:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI_5_LAN:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI_10_LAN:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI_100_LAN:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player, 1);
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại. Mua xong có tác dụng ngay nhé, nhớ tranh thủ sử\n"
                                                    + "dụng, thoát game phí lắm. Mua càng nhiều thời gian giá càng rẻ!",
                                            "Bùa\n Dùng \n1 tháng", "Bùa\nĐệ tử Mabư\n1 giờ");
                                    break;
                                case 1:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                                case 1:
                                    Service.gI().sendThongBaoOK(player, "Đang bảo trì");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NHAP_NGOC_RONG:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player, 1);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        this.createOtherMenu(player, 0, "Ngũ hành sơn ( sự kiện tết 2013 )", "Vào", "Từ chối");
                    }
                    // if (mapId == 122) {
                    // this.createOtherMenu(player, 0, "Thí chủ muốn quay về làng Aru?", "Đồng ý",
                    // "Từ chối");
                    //
                    // }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0,
                                "A mi khò khò, ở Ngũ hành sơn có lũ khỉ đã ăn trộm Hồng Đào\b Thí chủ có thể giúp ta lấy lại Hồng Đào từ chúng\bTa sẽ đổi 1 ít đồ để đổi lấy Hồng Đào.",
                                "Cửa hàng\nHồng Đào", "Về\nLàng Aru", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 0) {
                                if (player.nPoint.power < 5000L) {
                                    this.npcChat(player, "Sức mạnh thí chủ không phù hợp để qua Ngũ Hành Sơn!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapBySpaceShip(player, 123, -1, 96);
                            }
                            // if (mapId == 122) {
                            // ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 936);
                            // }
                            if (mapId == 122) {
                                if (select == 0) {
                                    ShopServiceNew.gI().opendShop(player, "TAYDUKY", true);
                                    break;
                                }
                                if (select == 1) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 936);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            // private void changeMap() {
            // if (this.mapId != 102) {
            // count++;
            // if (this.count >= COUNT_CHANGE) {
            // count = 0;
            // this.map.npcs.remove(this);
            // Map map = MapService.gI().getMapForCalich();
            // if (map != null) {
            // this.mapId = map.mapId;
            // this.cx = Util.nextInt(100, map.mapWidth - 100);
            // this.cy = map.yPhysicInTop(this.cx, 0);
            // this.map = map;
            // this.map.npcs.add(this);
            // }
            // }
            // }
            // }
            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);

                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                    if (this.mapId == 102) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào chú, cháu có thể giúp gì?",
                                "Quay về\nQuá khứ", "Từ chối");
                    } else if (this.mapId != 102) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào chú, cháu có thể giúp gì?", "Đi đến\nTương lai", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            // về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 99) {
                        // kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 0) {
                        // đến tương lai
                        // changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_23_0) {
                            ChangeMapService.gI().goToTuongLai(player);
                        } else {
                            Service.gI().sendThongBao(player,
                                    "Bạn cần làm nhiệm vụ\nđể mạnh mẽ hơn để có\nthể qua khu vực này");
                            return;
                        }
                    } else if (select == 1) {
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào?", "Quay số\nmay mắn", "Lên hành tinh Kaio");
                    }
                    if (this.mapId == 141) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Hãy nắm lấy tay ta mau!", "Về\nthần điện");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                                    + (player.inventory.itemsBoxCrackBall.size()
                                                            - InventoryServiceNew.gI().getCountEmptyListItem(
                                                                    player.inventory.itemsBoxCrackBall))
                                                    + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                                    + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 141) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapInYard(player, 45, 328, 408);
                                    Service.gI().sendThongBao(player, "Hãy xuống dưới gặp thần\nmèo Karin");
                                    break;
                            }
                        }

                    }

                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào?", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio",
                                            "Con\nđường\nrắn độc", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    if (player.clan != null) {
                                        if (player.clan.conDuongRanDoc != null) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_CDRD,
                                                    "Bang hội của con đang đi con đường rắn độc cấp độ "
                                                            + player.clan.conDuongRanDoc.level
                                                            + "\nCon có muốn đi theo không?",
                                                    "Đồng ý", "Từ chối");
                                        } else {

                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_CDRD,
                                                    "Đây là Con đường rắn độc \nCác con cứ yên tâm lên đường\n"
                                                            + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                    "Chọn\ncấp độ", "Từ chối");
                                        }
                                    } else {
                                        this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.isAdmin()
                                            || player.nPoint.power >= ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        ChangeMapService.gI().goToCDRD(player);
                                    } else {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                    }
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.isAdmin()
                                            || player.nPoint.power >= ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        Input.gI().createFormChooseLevelCDRD(player);
                                    } else {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                    }
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                            switch (select) {
                                case 0:
                                    // ConDuongRanDocService.gI().openConDuongRanDoc(player,
                                    // Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId,
            int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn đi đến Vùng Đất của các Vị Thần ?",
                                "Đến \nHành Tinh Kaio", "Đến \nHành Tinh Bill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi muốn đi về Trái Đất hay đi đến Thung Lũng Hủy Diệt?",
                                "Về \nSiêu Thị", "Đến\n Thung Lũng Hủy Diệt", "Đến\n Hành tinh\nNgục tù",
                                "Hướng dẫn");
                    } else if (this.mapId == 176) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ta có thể giúp ngươi trở về Vùng Đất của các Vị Thần ?",
                                "Quay về\n Vùng Đất của Thần", "Tạm biệt");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ta có thể giúp ngươi trở về Vùng Đất của các Vị Thần ?",
                                "Quay về\n Vùng Đất của Thần", "Tạm biệt");
                    } else if (this.mapId == 52) {
                        try {
                            // MapMaBu.gI().setTimeJoinMapMaBu();
                            // if (this.mapId == 52) {
                            // long now = System.currentTimeMillis();
                            // if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                            // this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB,
                            // "Đại chiến Ma Bư đã mở, " + "ngươi có muốn tham gia không?",
                            // "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            // } else {
                            // this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                            // "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                            // }

                            // }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 624, 432);
                                    break;

                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 354);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, 1416);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 176, -1, 96);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 155, -1, 144);
                                    break;
                            }
                        }
                    } else if (this.mapId == 176) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 624, 432);
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 624, 432);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar,
                                            ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player,
                                                "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else
                                        ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar,
                                            ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1,
                                        this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0,
                                        -1);
                            }
                        } else if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0,
                                    -1);
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0,
                                        -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        player.clan.doanhTrai_haveGone = true;
                        player.clan.doanhTrai.setLastTimeOpen(System.currentTimeMillis() + 350000);
                        player.clan.doanhTrai.DropNgocRong();
                        for (Player pl : player.clan.membersInGame) {
                            ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 300);
                        }
                        player.clan.doanhTrai.timePickDragonBall = true;
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    // public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId,
    // int avartar) {
    // return new Npc(mapId, status, cx, cy, tempId, avartar) {
    // @Override
    // public void openBaseMenu(Player player) {
    // if (canOpenNpc(player)) {
    // createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
    // "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
    // + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
    // "Vào\n(miễn phí)", "Hướng dẫn", "Từ chối");
    // }
    // }
    //
    // @Override
    // public void confirmMenu(Player player, int select) {
    // int nPlSameClan = 0;
    // for (Player pl : player.zone.getPlayers()) {
    // if (!pl.equals(player) && pl.clan != null
    // && pl.clan.equals(player.clan) && pl.location.x >= 1285
    // && pl.location.x <= 1645) {
    // nPlSameClan++;
    // }
    // }
    // if (canOpenNpc(player)) {
    // switch (player.iDMark.getIndexMenu()) {
    // case ConstNpc.MENU_JOIN_DOANH_TRAI:
    // if (select == 0) {
    // if (player.clan == null) {
    // Service.gI().sendThongBao(player, "Yêu cầu gia nhập bang hội");
    // break;
    // }
    //// if (player.clan.doanhTrai != null) {
    //// ChangeMapService.gI().changeMapInYard(player, 53, player.clan.doanhTrai.id,
    // 60);
    //// break;
    //// }
    // else if (nPlSameClan < 0) {
    // Service.gI().sendThongBao(player, "Yêu cầu tham gia cùng 2 đồng đội");
    // break;
    // } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 0) {
    // Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 1 ngày");
    // break;
    // }
    //// else if (player.clan.haveGoneDoanhTrai) {
    //// Service.gI().sendThongBaoOK(player, "Bang hội của ngươi đã tham gia vào lúc
    // " + TimeUtil.formatTime(player.clan.lastTimeOpenDoanhTrai, "HH:mm:ss") +
    // "\nVui lòng tham gia vào ngày mai");
    //// break;
    //// }
    //// else {
    //// DoanhTraiService.gI().opendoanhtrai(player);
    //// }
    // } else if (select == 1) {
    // NpcService.gI().createTutorial(player, this.avartar,
    // ConstNpc.HUONG_DAN_DOANH_TRAI);
    // }
    // break;
    // }
    // }
    // }
    // };
    // }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    // if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                    // this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    // "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                    // return;
                    // }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                        + "Thời gian còn lại là "
                                        + TimeUtil.getTimeLeft(player.clan.doanhTrai.getLastTimeOpen(),
                                                DoanhTrai.TIME_DOANH_TRAI / 1000)
                                        + ". Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    // if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                    // createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    // "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng
                    // gần mới có thể\nvào\n"
                    // + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                    // + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                    // return;
                    // }
                    // if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                    // createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    // "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay
                    // lại vào lúc khác",
                    // "OK", "Hướng\ndẫn\nthêm");
                    // return;
                    // }

                    if (!player.clan.doanhTrai_haveGone) {
                        player.clan.doanhTrai_haveGone = (new java.sql.Date(player.clan.doanhTrai_lastTimeOpen))
                                .getDay() == (new java.sql.Date(System.currentTimeMillis())).getDay();
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi trại lúc "
                                        + TimeUtil.formatTime(player.clan.doanhTrai_lastTimeOpen, "HH:mm:ss")
                                        + " hôm nay. Người mở\n"
                                        + "(" + player.clan.doanhTrai_playerOpen + "). Hẹn ngươi quay lại vào ngày mai",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                                    + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc miNuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.MENU_JOIN_GIAI_CUU_MI_NUONG,
                            "Ta đang bị kẻ xấu lợi dụng kiểm soát bản thân\n"
                                    + "Các chàng trai hãy cùng nhau nhanh chóng tập hợp lên đường giải cứu ta",
                            "Tham gia", "Hướng dẫn", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                int nPlSameClan = 0;
                for (Player pl : player.zone.getPlayers()) {
                    if (!pl.equals(player) && pl.clan != null
                            && pl.clan.equals(player.clan) && pl.location.x >= 1285
                            && pl.location.x <= 1645) {
                        nPlSameClan++;
                    }
                }
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_GIAI_CUU_MI_NUONG:
                            if (select == 0) {
                                if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu cầu gia nhập bang hội");
                                    break;
                                }
                                if (player.clan.giaiCuuMiNuong != null) {
                                    ChangeMapService.gI().changeMapInYard(player, 185, player.clan.giaiCuuMiNuong.id,
                                            60);
                                    break;
                                } else if (nPlSameClan < 0) {
                                    Service.gI().sendThongBao(player, "Yêu cầu tham gia cùng 2 đồng đội");
                                    break;
                                } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 0) {
                                    Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 1 ngày");
                                    break;
                                } else if (player.clan.haveGoneGiaiCuuMiNuong) {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bang hội của ngươi đã tham gia vào lúc " + TimeUtil
                                                    .formatTime(player.clan.lastTimeOpenGiaiCuuMiNuong, "HH:mm:ss")
                                                    + "\nVui lòng tham gia vào ngày mai");
                                    break;
                                } else {
                                    GiaiCuuMiNuongService.gI().openGiaiCuuMiNuong(player);
                                }
                            } else if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar,
                                        ConstNpc.HUONG_DAN_GIAI_CUU_MI_NUONG);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc BuMap(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            // @Override
            // public void openBaseMenu(Player player) {
            // if (canOpenNpc(player)) {
            // if (player.mabuEgg == null) {
            // createOtherMenu(player, ConstNpc.BASE_MENU,
            // "Ta được admin Ngọc Rồng Blue đến đây để hỗ trợ các "
            // + "chiến binh đệ tử Ma Bư với giá 66 thỏi vàng", "Đổi\nđệ Mabư", "Từ chối");
            // }
            // createOtherMenu(player, ConstNpc.BASE_MENU,
            // "Ta được admin Ngọc Rồng Sky đến đây để hỗ trợ các "
            // + "chiến binh đệ tử Ma Bư với giá 66 thỏi vàng\nHiện chiến binh đang có 1 quả
            // trứng tại nhà!! Cân nhắc trước khi đổi", "Đổi\nđệ Mabư", "Từ chối");
            // }
            // }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);

                                    if (thoivang != null && thoivang.quantity >= 66) {
                                        MabuEgg.createMabuEgg(player);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 66);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        break;
                                    } else {
                                        Service.getInstance().sendThongBaoOK(player, "Bạn không đủ thỏi vàng");
                                        return;
                                    }
                            }
                        }
                    }
                    ;
                }
                ;
            }

        };
    }

    public static Npc thoNgoc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta là Thợ Ngọc được đến từ Hành tinh mới\n"
                                    + "Ta có hỗ trợ liên quan tới vật phẩm mới Ngọc Bội\n"
                                    + "Ngươi muốn ta giúp gì?",
                            "Cửa hàng\nThợ ngọc", "Thăng hoa\nNgọc bội", "Thăng cấp\nNgọc bội");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Service.gI().sendThongBaoOK(player, "Chức năng đang được bảo trì");
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.THANG_HOA_NGOC_BOI);
                                    break;
                                // Service.gI().sendThongBaoOK(player, "Chức năng đang được bảo trì");
                                // break;
                                case 2:
                                    Service.gI().sendThongBaoOK(player, "Chức năng đang được bảo trì");
                                    break;
                            }
                        }
                        if (player.iDMark.getIndexMenu() == ConstNpc.MENU_THANG_HOA_NGOC_BOI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().startCombine(player, 1);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().startCombine(player, 1);
                                    break;
                                // Service.gI().sendThongBaoOK(player, "Chức năng đang bảo trì để phát
                                // triển!!");
                            }
                            return;
                        }
                        ;
                    }
                }
                ;
            };
        }

        ;
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        player.mabuEgg.sendMabuEgg();
                        this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG,
                                "Bạn có chắc chắn muốn thay thế đệ tử hiện tại thành đệ tử Mabư?",
                                "Chờ\n" + "(23h59')", "Nở ngay\n1 tỷ vàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_EGG:
                                if (select == 0) {
                                    return;
                                }
                                if (select == 1) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                            "Ta sẽ giúp ngươi chọn hành tinh cho đệ tử, ngươi hãy chọn đi?",
                                            "Trái đất", "Namek", "Xayda");
                                    break;
                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                            "Bạn còn thiếu " + (COST_AP_TRUNG_NHANH - player.inventory.gold) + " vàng");
                                }
                            case ConstNpc.CONFIRM_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        // if (player.pet.nPoint.power >= 25000000000L && player.pet.typePet != 1) {
                                        // player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        // break;
                                        // } else if (player.pet.nPoint.power < 25000000000L && player.pet.typePet != 1)
                                        // {
                                        // Service.gI().sendThongBao(player, "Yêu cầu đệ tử đạt 25 tỷ sức mạnh");
                                        // }
                                        player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        if (player.pet.typePet == 1) {
                                            break;
                                        }
                                    case 1:
                                        // if (player.pet.nPoint.power >= 25000000000L && player.pet.typePet != 1) {
                                        // player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                        // break;
                                        // } else if (player.pet.nPoint.power < 25000000000L && player.pet.typePet != 1)
                                        // {
                                        // Service.gI().sendThongBao(player, "Yêu cầu đệ tử đạt 25 tỷ sức mạnh");
                                        // }
                                        player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                        if (player.pet.typePet == 1) {
                                        }
                                        break;
                                    case 2:
                                        // if (player.pet.nPoint.power >= 25000000000L && player.pet.typePet != 1) {
                                        // player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                        // break;
                                        // } else if (player.pet.nPoint.power < 25000000000L && player.pet.typePet != 1)
                                        // {
                                        // Service.gI().sendThongBao(player, "Yêu cầu đệ tử đạt 25 tỷ sức mạnh");
                                        // }
                                        player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                        // if (player.pet.typePet == 1) {
                                        // }
                                        break;
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < 12) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                                    + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n"
                                                    + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
                                                    + " vàng",
                                            "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                        + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(
                                                        OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng",
                                                "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                // giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                                    + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                            - player.inventory.gold))
                                                    + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                                + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                        - player.inventory.gold))
                                                + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {

                    if (TaskService.gI().getIdTask(player) == ConstTask.TASK_30_1) {
                        TaskService.gI().sendNextTaskMain(player);
                        NpcService.gI().createTutorial(player, this.avartar,
                                "Bạn đã hoàn thành tất cả nhiệm vụ của game\bBạn hãy chờ đợi để ADMIN Update tiếp nhiệm vụ nhé");

                        int itemId = 591;
                        Item SieuThan = ItemService.gI().createNewItem(((short) itemId));
                        SieuThan.itemOptions.add(new Item.ItemOption(47, 450));
                        SieuThan.itemOptions.add(new Item.ItemOption(108, 30));
                        SieuThan.itemOptions.add(new Item.ItemOption(33, 1));

                        Service.gI().sendThongBao(player, "Bạn nhận được " + SieuThan.Name());
                        InventoryServiceNew.gI().addItemBag(player, SieuThan);
                        InventoryServiceNew.gI().sendItemBags(player);
                        return;

                    }

                    if (canOpenNpc(player)) {
                        ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {

                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW,
                                        "Đường đến với ngọc rồng sao đen đã mở, "
                                                + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                        "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Nhận thưởng", "Từ chối");
                            }
                            // Service.gI().sendThongBaoOK(player, "Chức năng đang bảo trì để phát
                            // triển!!");
                            return;
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            if (select == 1) {
                                Service.gI().sendThongBaoOK(player, "Chức năng hiện đang bảo trì!!");
                                // String[] optionRewards = new String[7];
                                // int index = 0;
                                // for (int i = 0; i < 7; i++) {
                                // if (player.rewardBlackBall.timeOutOfDateReward[i] >
                                // System.currentTimeMillis()) {
                                // String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? " " : "";
                                // optionRewards[index] = quantily + (i + 1) + " sao";
                                // index++;
                                // }
                                // }
                                // if (index != 0) {
                                // String[] options = new String[index + 1];
                                // for (int i = 0; i < index; i++) {
                                // options[i] = optionRewards[i];
                                // }
                                // options[options.length - 1] = "Từ chối";
                                // this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần
                                // thưởng ngọc "
                                // + "rồng sao đen đây!",
                                // options);
                                // }
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ",
                                "Từ chối");
                    } else if (BossManager.gI().existBossOnPlayer(player)
                            || player.zone.items.stream()
                                    .anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                            || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?",
                                "Về nhà", "Từ chối");
                    } else {
                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?",
                                "Về nhà", "Từ chối", "Gọi BOSS");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đói bụng!!!\nquá Không muốn hành tinh này bị hủy diệt thì mang 99 phần đồ ăn tới đây,\nta sẽ cho một món đồ Hủy Diệt.\nPhục vụ tốt ta có thể cho trang bị mạnh mẽ hơn đến 15%!",
                            "Đồng ý", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 48:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        Item pudding = InventoryServiceNew.gI().findItemBag(player, 663);
                                        Item xucxich = InventoryServiceNew.gI().findItemBag(player, 664);
                                        Item kemdau = InventoryServiceNew.gI().findItemBag(player, 665);
                                        Item mily = InventoryServiceNew.gI().findItemBag(player, 666);
                                        Item sushi = InventoryServiceNew.gI().findItemBag(player, 667);
                                        if (pudding != null && pudding.quantity >= 99
                                                || xucxich != null && xucxich.quantity >= 99
                                                || kemdau != null && kemdau.quantity >= 99
                                                || mily != null && mily.quantity >= 99
                                                || sushi != null && sushi.quantity >= 99) {
                                            ShopServiceNew.gI().opendShop(player, "HUY_DIET", true);
                                            break;
                                        } else {
                                            this.npcChat(player, "Còn không mau đem x99 thức ăn đến cho ta !!");
                                            break;
                                        }
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "Học tuyệt kỹ", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ", "Chế tạo",
                                        "Từ chối");
                                break;
                            case 1:
                                Item BiKiepTuyetKy = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1125);
                                if (BiKiepTuyetKy != null) {
                                    if (player.gender == 0) {
                                        this.createOtherMenu(player, 6,
                                                "|1|Ta sẽ dạy ngươi tuyệt kỹ Super kamejoko\n" + "|7|Bí kiếp tuyệt kỹ: "
                                                        + BiKiepTuyetKy.quantity + "/999\n"
                                                        + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 1) {
                                        this.createOtherMenu(player, 6,
                                                "|1|Ta sẽ dạy ngươi tuyệt kỹ Ma phông ba\n" + "|7|Bí kiếp tuyệt kỹ: "
                                                        + BiKiepTuyetKy.quantity + "/999\n"
                                                        + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 2) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ "
                                                + "đíc chưởng liên hoàn\n" + "|7|Bí kiếp tuyệt kỹ: "
                                                + BiKiepTuyetKy.quantity + "/999\n" + "|2|Giá vàng: 10.000.000\n"
                                                + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Hãy tìm bí kíp rồi quay lại gặp ta!");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            // case 0:
                            // ShopServiceNew.gI().opendShop(player, "THIEN_SU", false);
                            // break;
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                                break;
                        }
                        // } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        // if (select == 0) {
                        // CombineServiceNew.gI().startCombine(player);
                    } else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                Item sach = InventoryServiceNew.gI().findItemBag(player, 1125);
                                if (sach != null && sach.quantity >= 9999 && player.inventory.gold >= 10000000
                                        && player.inventory.gem > 99 && player.nPoint.power >= 60000000000L) {

                                    // if (player.gender == 2) {
                                    // SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                                    // }
                                    // if (player.gender == 0) {
                                    // SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                                    // }
                                    // if (player.gender == 1) {
                                    // SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                                    // }
                                    InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, sach, 9999);
                                    player.inventory.gold -= 10000000;
                                    player.inventory.gem -= 99;
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else if (player.nPoint.power < 60000000000L) {
                                    Service.getInstance().sendThongBao(player,
                                            "Ngươi không đủ sức mạnh để học tuyệt kỹ");
                                    return;
                                } else if (sach.quantity <= 999) {
                                    int sosach = 999 - sach.quantity;
                                    Service.getInstance().sendThongBao(player,
                                            "Ngươi còn thiếu " + sosach + " bí kíp nữa.\nHãy tìm đủ rồi đến gặp ta.");
                                    return;
                                } else if (player.inventory.gold <= 10000000) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ vàng thì quay lại gặp ta.");
                                    return;
                                } else if (player.inventory.gem <= 99) {
                                    Service.getInstance().sendThongBao(player,
                                            "Hãy có đủ ngọc xanh thì quay lại gặp ta.");
                                    return;
                                }

                                break;
                        }
                    }
                }
            }

        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 47 || this.mapId == 84) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ngươi cần giúp gì?", "Nhập\nGift Code", "Danh hiệu", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Input.gI().createFormGiftCode(player);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 888,
                                            "Đây là danh hiệu mà ngươi có"
                                                    + (player.lastTimeTitle1 > 0
                                                            ? "\n Danh hiệu VIP 1: "
                                                                    + Util.msToTime(player.lastTimeTitle1)
                                                            : "")
                                                    + (player.lastTimeTitle2 > 0
                                                            ? "\n Danh hiệu Fan cứng: "
                                                                    + Util.msToTime(player.lastTimeTitle2)
                                                            : "")
                                                    + (player.lastTimeTitle3 > 0
                                                            ? "\n Danh hiệu Bất bại: "
                                                                    + Util.msToTime(player.lastTimeTitle3)
                                                            : ""),
                                            ("Danh hiệu \n VIP 1: " + (player.isTitleUse == true ? "On" : "Off")),
                                            ("Danh hiệu \n Fan cứng: " + (player.isTitleUse2 == true ? "On" : "Off")
                                                    + "\n"),
                                            ("Danh hiệu \n Bất bại: " + (player.isTitleUse3 == true ? "On" : "Off")
                                                    + "\n"),
                                            "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 888) {
                            switch (select) {
                                case 0:
                                    if (player.lastTimeTitle1 > 0) {
                                        Service.gI().removeTitle(player);
                                        player.isTitleUse = !player.isTitleUse;
                                        Service.gI().point(player);
                                        Service.gI().sendThongBao(player,
                                                "Đã " + (player.isTitleUse == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                        Service.gI().sendTitle(player, 890);
                                        Service.gI().sendTitle(player, 889);
                                        Service.gI().sendTitle(player, 888);
                                        break;
                                    }
                                    break;
                                case 1:
                                    if (player.lastTimeTitle2 > 0) {
                                        Service.gI().removeTitle(player);
                                        player.isTitleUse2 = !player.isTitleUse2;
                                        Service.gI().point(player);
                                        Service.gI().sendThongBao(player,
                                                "Đã " + (player.isTitleUse2 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                        Service.gI().sendTitle(player, 890);
                                        Service.gI().sendTitle(player, 889);
                                        Service.gI().sendTitle(player, 888);
                                        break;
                                    }
                                    break;
                                case 2:
                                    if (player.lastTimeTitle3 > 0) {
                                        Service.gI().removeTitle(player);
                                        player.isTitleUse3 = !player.isTitleUse3;
                                        Service.gI().point(player);
                                        Service.gI().sendThongBao(player,
                                                "Đã " + (player.isTitleUse3 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                        Service.gI().sendTitle(player, 890);
                                        Service.gI().sendTitle(player, 889);
                                        Service.gI().sendTitle(player, 888);
                                        break;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                    }
                    NpcService.gI().createTutorial(player, this.avartar,
                            "Chức năng đang được cập nhật !!! " + "[" + mapId + "]" + "[" + this.tempId + "]");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    /////////////////////////////////////////// NPC Ông Gohan, Ông Moori, Ông
    /////////////////////////////////////////// Paragus///////////////////////////////////////////
    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (!player.getSession().actived && player.pointPvp != 1) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Thành công đến từ sự cố gắng và chăm chỉ\nVà ngươi muốn ta hỗ trợ gì?"
                                            .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                    : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru"
                                                            : "Vua Vegeta"),
                                    "Nhiệm\nvụ", "Nhận\nngọc", "Nhận\nđệ tử");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Thành công đến từ sự cố gắng và chăm chỉ\nVà ngươi muốn ta hỗ trợ gì?"
                                            .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                    : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru"
                                                            : "Vua Vegeta"),
                                    "Nhiệm\nvụ", "Nhận\nngọc", "Nhận\nđệ tử", "Nhận quà\n thành viên");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.playerTask.sideTask.template == null) {
                                    TaskService.gI().changeSideTask(player, (byte) 4);
                                    String npcSay = "Nhiệm vụ: " + player.playerTask.sideTask.leftTask + "/"
                                            + ConstTask.MAX_SIDE_TASK
                                            + "\n"
                                            + player.playerTask.sideTask.getName() + "\n"
                                            + "Tiến độ: " + player.playerTask.sideTask.count + "/"
                                            + player.playerTask.sideTask.maxCount + " ("
                                            + player.playerTask.sideTask.getPercentProcess() + "%)";
                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                            npcSay, "Nhận thưởng", "Bỏ qua", "Đóng");
                                    break;
                                } else if (player.playerTask.sideTask.template != null) {
                                    String npcSay = "Nhiệm vụ: " + player.playerTask.sideTask.leftTask + "/"
                                            + ConstTask.MAX_SIDE_TASK
                                            + "\n"
                                            + player.playerTask.sideTask.getName() + "\n"
                                            + "Tiến độ: " + player.playerTask.sideTask.count + "/"
                                            + player.playerTask.sideTask.maxCount + " ("
                                            + player.playerTask.sideTask.getPercentProcess() + "%)";
                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                            npcSay, "Nhận thưởng", "Bỏ qua", "Đóng");
                                    break;
                                }
                            case 1:
                                if (player.inventory.gem >= 700000) {
                                    player.inventory.gem = 999999;
                                    Service.gI().sendThongBao(player, "Bạn nhận được 200.000\nngọc");
                                    Service.gI().sendMoney(player);
                                    break;
                                } else {
                                    player.inventory.gem += 200000;
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được 200.000\nngọc");
                                    Service.gI().sendMoney(player);
                                    break;
                                }
                            case 2:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn đã có đệ tử trước\nđó rồi");
                                }
                                break;
                            case 3:
                                if (player.getSession().actived && player.pointPvp < 1) {
                                    for (int i = 14; i <= 20; i++) {
                                        Item item = ItemService.gI().createNewItem((short) i);
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                    }
                                    Item thoivang = ItemService.gI().createNewItem((short) 457);
                                    thoivang.quantity = 20;
                                    InventoryServiceNew.gI().addItemBag(player, thoivang);
                                    player.pointPvp++;
                                    PlayerDAO.updatePlayer(player);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận thành công quà thành viên");
                                    break;
                                } else {
                                    Service.gI().sendThongBao(player, "Có cái nịt");
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                        switch (select) {
                            case 0:
                                TaskService.gI().paySideTask(player);
                                break;
                            case 1:
                                TaskService.gI().changeSideTask(player, (byte) 4);
                                String npcSay = "Nhiệm vụ: " + player.playerTask.sideTask.leftTask + "/"
                                        + ConstTask.MAX_SIDE_TASK
                                        + "\n"
                                        + player.playerTask.sideTask.getName() + "\n"
                                        + "Tiến độ: " + player.playerTask.sideTask.count + "/"
                                        + player.playerTask.sideTask.maxCount + " ("
                                        + player.playerTask.sideTask.getPercentProcess() + "%)";
                                this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                        npcSay, "Nhận thưởng", "Bỏ qua", "Đóng");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Ta Vừa Hắc Mắp Xêm Được Tóp Của Toàn Server\b|7|Người Muốn Xem Tóp Gì?",
                            "Tóp Sức Mạnh", "Top Nhiệm Vụ", "Top Pvp", "Tóp Ngũ Hành Sơn", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 5:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        Service.gI().showListTop(player, Manager.topSM);
                                        break;
                                    }
                                    break;
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?",
                                "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 131) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Tây thánh địa", "Từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                // đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                // về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Vào các khung giờ chẵn trong ngày\n"
                                + "Khi luyện tập với Mộc nhân với chế độ bật Cờ sẽ đánh rơi Bí kíp\n"
                                + "Hãy cố găng tập luyện thu thập 9999 bí kíp rồi quay lại gặp ta nhé", "Nhận\nthưởng",
                                "OK");

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (select == 0) {
                            if (biKiep != null) {
                                if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                    yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                    yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                    InventoryServiceNew.gI().addItemBag(player, yardart);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                                } else if (biKiep.quantity < 10000) {
                                    Service.gI().sendThongBao(player, "Vui lòng sưu tầm đủ\n9999 bí kíp");
                                }
                            } else {
                                Service.gI().sendThongBao(player, "Vui lòng sưu tầm đủ\n9999 bí kíp");
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        };
    }

    // public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId,
    // int avartar) {
    // return new Npc(mapId, status, cx, cy, tempId, avartar) {
    // String[] menuselect = new String[]{};
    //
    // @Override
    // public void openBaseMenu(Player pl) {
    // if (canOpenNpc(pl)) {
    // if (this.mapId == 52) {
    // createOtherMenu(pl, 0,
    // DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Giai(pl), "Thông
    // tin\nChi tiết",
    // DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(pl) ? "Đăng
    // ký" : "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23");
    // } else if (this.mapId == 129) {
    // int goldchallenge = pl.goldChallenge;
    // int goldchallenge1 = pl.gemChallenge;
    // if (pl.levelWoodChest == 0) {
    // menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" +
    // Util.numberToMoney(goldchallenge) + " vàng", "Thi đấu\n" +
    // Util.numberToMoney(goldchallenge1) + " Ngọc", "Về\nĐại Hội\nVõ Thuật"};
    // } else {
    // menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" +
    // Util.numberToMoney(goldchallenge) + " vàng", "Thi đấu\n" +
    // Util.numberToMoney(goldchallenge1) + " Ngọc", "Nhận thưởng\nRương cấp\n" +
    // pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
    // }
    // this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ
    // 23\nDiễn ra bất kể ngày đêm, ngày nghỉ, ngày lễ\nPhần thưởng vô cùng quý
    // giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");
    //
    // } else {
    // super.openBaseMenu(pl);
    // }
    // }
    // }
    //
    // @Override
    // public void confirmMenu(Player player, int select) {
    // if (canOpenNpc(player)) {
    // if (this.mapId == 52) {
    // switch (select) {
    // case 0:
    // Service.gI().sendPopUpMultiLine(player, tempId, avartar,
    // DaiHoiVoThuat.gI().Info());
    // break;
    // case 1:
    // if
    // (DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(player)) {
    // DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Reg(player);
    // }
    // break;
    // case 2:
    // ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x,
    // 360);
    // break;
    // }
    // } else if (this.mapId == 129) {
    // int goldchallenge = player.goldChallenge;
    // int goldchallenge1 = player.gemChallenge;
    // if (player.levelWoodChest == 0) {
    // switch (select) {
    // case 0:
    // NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
    // break;
    // case 1:
    // if (InventoryServiceNew.gI().finditemWoodChest(player)) {
    // if (player.inventory.gold >= goldchallenge) {
    // MartialCongressService.gI().startChallenge(player);
    // player.inventory.gold -= (goldchallenge);
    // PlayerService.gI().sendInfoHpMpMoney(player);
    // player.goldChallenge += 50000000;
    // } else {
    // Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " +
    // Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
    // }
    // } else {
    // Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
    // }
    // break;
    // case 2:
    // if (InventoryServiceNew.gI().finditemWoodChest(player)) {
    // if (player.inventory.gem >= goldchallenge1) {
    // MartialCongressService.gI().startChallenge(player);
    // player.inventory.gem -= (goldchallenge1);
    // PlayerService.gI().sendInfoHpMpMoney(player);
    // player.gemChallenge += 10000;
    // } else {
    // Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " +
    // Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
    // }
    // } else {
    // Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
    // }
    // break;
    // case 3:
    // ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x,
    // 336);
    // break;
    // }
    // } else {
    // switch (select) {
    // case 0:
    // NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
    // break;
    // case 1:
    // if (InventoryServiceNew.gI().finditemWoodChest(player)) {
    // if (player.inventory.gold >= goldchallenge) {
    // MartialCongressService.gI().startChallenge(player);
    // player.inventory.gold -= (goldchallenge);
    // PlayerService.gI().sendInfoHpMpMoney(player);
    // player.goldChallenge += 50000000;
    // } else {
    // Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " +
    // Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
    // }
    // } else {
    // Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
    // }
    // break;
    // case 2:
    // if (InventoryServiceNew.gI().finditemWoodChest(player)) {
    // if (player.inventory.gem >= goldchallenge1) {
    // MartialCongressService.gI().startChallenge(player);
    // player.inventory.gem -= (goldchallenge1);
    // PlayerService.gI().sendInfoHpMpMoney(player);
    // player.gemChallenge += 10000;
    // } else {
    // Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " +
    // Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
    // }
    // } else {
    // Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
    // }
    // break;
    // case 3:
    // if (!player.receivedWoodChest) {
    // if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
    // Item it = ItemService.gI().createNewItem((short) 570);
    // it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
    // it.itemOptions.add(new Item.ItemOption(30, 0));
    // it.createTime = System.currentTimeMillis();
    // InventoryServiceNew.gI().addItemBag(player, it);
    // InventoryServiceNew.gI().sendItemBags(player);
    //
    // player.receivedWoodChest = true;
    // player.levelWoodChest = 0;
    // Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
    // } else {
    // this.npcChat(player, "Hành trang đã đầy");
    // }
    // } else {
    // Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương
    // báu 1 lần");
    // }
    // break;
    // case 4:
    // ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x,
    // 336);
    // break;
    // }
    // }
    // }
    // }
    // }
    // };
    // }
    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI:
                    return TrongTai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_NGOC:
                    return thoNgoc(mapId, status, cx, cy, tempId, avatar);
                case 40:
                    return BuMap(mapId, status, cx, cy, tempId, avatar);
                case 112:
                    return Mai(mapId, status, cx, cy, tempId, avatar);
                case 113:
                    return Fu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.DOC_NHAN:
                    return docNhan(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.MI_NUONG:
                    return miNuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS:
                    return whis(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TO_SU_KAIO:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);

                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                // player.gender);
                            }
                        }
                    };
            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class, e, "Lỗi load npc");
            return null;
        }
    }

    // girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1
                                && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2
                                && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcSanTa() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.SANTA, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.BAN_NHIEU_THOI_VANG:
                        Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                        if (select == 0 && thoivang.quantity >= 1 && thoivang != null
                                && player.inventory.gold < 9_500_000_000L) {
                            thoivang.quantity -= 1;
                            player.inventory.gold += 500_000_000;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 0 && thoivang.quantity < 1 && thoivang == null
                                && player.inventory.gold < 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 1 Thỏi\nvàng để thực hiện");
                            return;
                        }
                        if (select == 1 && thoivang.quantity >= 5 && thoivang != null
                                && player.inventory.gold < 9_500_000_000L) {
                            thoivang.quantity -= 5;
                            player.inventory.gold += 2_500_000_000L;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 0 && thoivang.quantity < 5 && thoivang == null
                                && player.inventory.gold < 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 5 Thỏi\nvàng để thực hiện");
                            return;
                        }
                        if (select == 1 && thoivang.quantity >= 10 && thoivang != null
                                && player.inventory.gold < 9_500_000_000L) {
                            thoivang.quantity -= 10;
                            player.inventory.gold += 5_000_000_000L;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 0 && thoivang.quantity < 10 && thoivang == null
                                && player.inventory.gold < 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 10 Thỏi\nvàng để thực hiện");
                            return;
                        }
                        if (select == 1 && thoivang.quantity >= 19 && thoivang != null
                                && player.inventory.gold < 9_500_000_000L) {
                            thoivang.quantity -= 19;
                            player.inventory.gold += 9_500_000_000L;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 0 && thoivang.quantity < 19 && thoivang == null
                                && player.inventory.gold < 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 19 Thỏi\nvàng để thực hiện");
                            return;
                        } else if (player.inventory.gold > 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 10 tỷ");
                            return;
                        }
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.BAN_NHIEU_THOI_VANG:
                        Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                        if (select == 0 && thoivang.quantity >= 1 && thoivang != null
                                && player.inventory.gold <= 9_500_000_000L) {
                            player.inventory.gold += 500_000_000;
                            InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 1);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendMoney(player);

                            break;
                        } else if (select == 0 && thoivang.quantity < 1 && thoivang == null
                                && player.inventory.gold <= 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 1 Thỏi\nvàng để thực hiện");
                            return;
                        } else if (select == 0 && thoivang.quantity >= 1 && player.inventory.gold > 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 10 tỷ");
                            return;
                        }
                        if (select == 1 && thoivang.quantity >= 5 && thoivang != null
                                && player.inventory.gold <= 7_500_000_000L) {
                            player.inventory.gold += 2_500_000_000L;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 1 && thoivang.quantity < 5 && thoivang == null
                                && player.inventory.gold <= 7_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 5 Thỏi\nvàng để thực hiện");
                            return;
                        } else if (select == 1 && thoivang.quantity >= 5 && thoivang != null
                                && player.inventory.gold > 7_500_000_000L) {
                            Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 10 tỷ");
                            return;
                        }
                        if (select == 2 && thoivang.quantity >= 10 && thoivang != null
                                && player.inventory.gold <= 5_000_000_000L) {
                            player.inventory.gold += 5_000_000_000L;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 10);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 2 && thoivang.quantity < 10 && thoivang == null
                                && player.inventory.gold < 5_000_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 10 Thỏi\nvàng để thực hiện");
                            return;
                        } else if (select == 2 && thoivang.quantity >= 10 && thoivang != null
                                && player.inventory.gold > 5_000_000_000L) {
                            Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 10 tỷ");
                            return;
                        }
                        if (select == 3 && thoivang.quantity >= 19 && thoivang != null
                                && player.inventory.gold <= 500_000_000L) {
                            player.inventory.gold += 9_500_000_000L;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 19);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 3 && thoivang.quantity < 19 && thoivang == null
                                && player.inventory.gold <= 500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 19 Thỏi\nvàng để thực hiện");
                            return;
                        } else if (select == 3 && thoivang.quantity >= 19 && thoivang != null
                                && player.inventory.gold > 500_000_000L) {
                            Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 10 tỷ");
                            return;
                        }
                        break;
                    case ConstNpc.MAKE_MATCH_PVP:
                        if (player.getSession().actived) {
                            if (Maintenance.isRuning) {
                                break;
                            }
                            PVPService.gI().sendInvitePVP(player, (byte) select);
                            break;
                        } else {
                            // Service.gI().sendThongBao(player, "Vui lòng mở thẻ Heroes Z để sử dụng chức
                            // năng này.");
                            break;
                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                            return;
                        }
                        // else if (select == 1 && player.LuotGoiRongThan <= 0) {
                        // Service.gI().sendThongBaoOK(player,
                        // "Đã đạt giới hạn gọi rồng hôm nay\bVui lòng thử lại vào hôm sau");
                        // return;
                        // }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    // case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    // case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    // case ConstNpc.MENU_OPTION_USE_ITEM2002:
                    // try {
                    // ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                    // } catch (Exception e) {
                    // Logger.error("Lỗi mở hộp quà");
                    // }
                    // break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player,
                                    "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho "
                                        + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                }
                                InventoryServiceNew.gI().sendItemBags(player);
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    if (player.pet.typePet == 1) {
                                        PetService.gI().changeUubPet(player);
                                    } else if (player.pet.typePet == 2) {
                                        PetService.gI().changeMabuPet(player);
                                    }
                                    PetService.gI().changeBerusPet(player);
                                }
                                break;
                            case 2:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
                                    Maintenance.gI().start(15);
                                    System.out.println(player.name);
                                }
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                BossManager.gI().showListBoss(player);
                                break;
                            case 5:
                                MaQuaTangManager.gI().checkInfomationGiftCode(player);
                                break;
                        }
                        break;

                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().settaiyoken(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgenki(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setkamejoko(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodki(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgoddam(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setsummon(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;
                    case ConstNpc.XU_HRZ:
                        try {
                            if (select == 0) {
                                NapVangService.ChonGiaTien(20, player);
                            } else if (select == 1) {
                                NapVangService.ChonGiaTien(50, player);
                            } else if (select == 2) {
                                NapVangService.ChonGiaTien(100, player);
                            } else if (select == 3) {
                                NapVangService.ChonGiaTien(500, player);

                            } else {

                                break;
                            }
                            break;
                        } catch (Exception e) {
                            break;
                        }

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodgalick(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.XAC_NHAN_XOA_BANG_HOI:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Yêu cầu giải tán bang hội thành công!");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x,
                                                p.location.y);
                                        Service.gI().sendThongBao(player,
                                                "Đại thiên sứ đã được chuyển tức thời đến vị trí: " + p.name + " !");
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(p, player.zone, player.location.x,
                                                player.location.y);

                                        Service.gI().sendThongBao(player, "Cư dân " + p.name
                                                + " đã được Đại thiên sứ dịch chuyển tức thời đến đây!");
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[] { "Hủy diệt", "Tha" };
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Đại thiên sứ có muốn hủy diệt cư dân: " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.gI().sendThongBao(player,
                                            "Đại thiên sứ đã Logout cư dân " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                        break;
                    case 568:
                        int moDeMaBuTD = 5680;
                        switch (select) {
                            case 0:
                                // System.out.println("Đệ trái đất mabu trứng");
                                NpcService.gI().createMenuConMeo(player, moDeMaBuTD, -1,
                                        "Có đồng ý mở đệ trái đất không ?", "Có nhé", "Không nhé");
                                break;
                            case 1:
                                // System.out.println("Đệ namec mabu trứng");
                                moDeMaBuTD = 5681;
                                NpcService.gI().createMenuConMeo(player, moDeMaBuTD, -1,
                                        "Có đồng ý mở đệ namec không ?", "Có nhé", "Không nhé");
                                break;
                            case 2:
                                // System.out.println("Đệ xayda mabu trứng");
                                moDeMaBuTD = 5682;
                                NpcService.gI().createMenuConMeo(player, moDeMaBuTD, -1,
                                        "Có đồng ý mở đệ xayda không ?", "Có nhé", "Không nhé");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 5680:
                        if (select == 0) {
                            Egg eggTd = new Egg(player); // tạo trứng cho user
                            eggTd.openMabuEgg(0);
                        }
                        break;
                    case 5681:
                        if (select == 0) {
                            Egg eggNm = new Egg(player); // tạo trứng cho user
                            eggNm.openMabuEgg(1);
                        }
                        break;
                    case 5682:
                        if (select == 0) {
                            Egg eggXd = new Egg(player); // tạo trứng cho user
                            eggXd.openMabuEgg(2);
                        }
                        break;
                    case 2027:
                        int moDeUub = 20270;
                        switch (select) {
                            case 0:
                                // System.out.println("Đệ trái đất mabu trứng");
                                NpcService.gI().createMenuConMeo(player, moDeUub, -1,
                                        "Có đồng ý mở đệ trái đất không ?", "Có nhé", "Không nhé");
                                break;
                            case 1:
                                // System.out.println("Đệ namec mabu trứng");
                                moDeUub = 20271;
                                NpcService.gI().createMenuConMeo(player, moDeUub, -1,
                                        "Có đồng ý mở đệ namec không ?", "Có nhé", "Không nhé");
                                break;
                            case 2:
                                // System.out.println("Đệ xayda mabu trứng");
                                moDeUub = 20272;
                                NpcService.gI().createMenuConMeo(player, moDeUub, -1,
                                        "Có đồng ý mở đệ xayda không ?", "Có nhé", "Không nhé");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20270:
                        if (select == 0) {
                            Egg eggUubTd = new Egg(player); // tạo trứng cho user
                            eggUubTd.openUubEgg(0);
                        }
                        break;
                    case 20271:
                        if (select == 0) {
                            Egg eggUubNm = new Egg(player); // tạo trứng cho user
                            eggUubNm.openUubEgg(1);
                        }
                        break;
                    case 20272:
                        if (select == 0) {
                            Egg eggUubXd = new Egg(player); // tạo trứng cho user
                            eggUubXd.openUubEgg(2);
                        }
                        break;
                    case 2028:
                        int moDeBerus = 20280;
                        switch (select) {
                            case 0:
                                // System.out.println("Đệ trái đất mabu trứng");
                                NpcService.gI().createMenuConMeo(player, moDeBerus, -1,
                                        "Có đồng ý mở đệ trái đất không ?", "Có nhé", "Không nhé");
                                break;
                            case 1:
                                // System.out.println("Đệ namec mabu trứng");
                                moDeBerus = 20281;
                                NpcService.gI().createMenuConMeo(player, moDeBerus, -1,
                                        "Có đồng ý mở đệ namec không ?", "Có nhé", "Không nhé");
                                break;
                            case 2:
                                // System.out.println("Đệ xayda mabu trứng");
                                moDeBerus = 20282;
                                NpcService.gI().createMenuConMeo(player, moDeBerus, -1,
                                        "Có đồng ý mở đệ xayda không ?", "Có nhé", "Không nhé");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20280:
                        if (select == 0) {
                            Egg eggBeTd = new Egg(player); // tạo trứng cho user
                            eggBeTd.openBerusEgg(0);
                        }
                        break;
                    case 20281:
                        if (select == 0) {
                            Egg eggBeNm = new Egg(player); // tạo trứng cho user
                            eggBeNm.openBerusEgg(1);
                        }
                        break;
                    case 20282:
                        if (select == 0) {
                            Egg eggBeXd = new Egg(player); // tạo trứng cho user
                            eggBeXd.openBerusEgg(2);
                        }
                        break;
                    case 2000:
                        int moDo = 20000;
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, moDo, -1,
                                        "Có đồng ý nhận set Songoku không ?", "Có nhé", "Không nhé");
                                break;
                            case 1:
                                moDo = 20001;
                                NpcService.gI().createMenuConMeo(player, moDo, -1,
                                        "Có đồng ý nhận set Angry Goku không ?", "Có nhé", "Không nhé");
                                break;
                            case 2:
                                moDo = 20002;
                                NpcService.gI().createMenuConMeo(player, moDo, -1,
                                        "Có đồng ý nhận set Thên xin hăng không ?", "Có nhé", "Không nhé");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2001:
                        int moDo1 = 20010;
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, moDo1, -1,
                                        "Có đồng ý nhận set Kami không ?", "Có nhé", "Không nhé");
                                break;
                            case 1:
                                moDo1 = 20011;
                                NpcService.gI().createMenuConMeo(player, moDo1, -1,
                                        "Có đồng ý nhận set Nail không ?", "Có nhé", "Không nhé");
                                break;
                            case 2:
                                moDo1 = 20012;
                                NpcService.gI().createMenuConMeo(player, moDo1, -1,
                                        "Có đồng ý nhận set Picolo không ?", "Có nhé", "Không nhé");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2002:
                        int moDo2 = 20020;
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, moDo2, -1,
                                        "Có đồng ý nhận set Vegeta không ?", "Có nhé", "Không nhé");
                                break;
                            case 1:
                                moDo2 = 20021;
                                NpcService.gI().createMenuConMeo(player, moDo2, -1,
                                        "Có đồng ý nhận set Cumber không ?", "Có nhé", "Không nhé");
                                break;
                            case 2:
                                moDo2 = 20022;
                                NpcService.gI().createMenuConMeo(player, moDo2, -1,
                                        "Có đồng ý nhận set Kakarot không ?", "Có nhé", "Không nhé");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20000:// set songoku
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                    Item item = InventoryServiceNew.gI().findItemBag(player, 2000);
                                    if (item != null) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                        Item ao = ItemService.gI().createNewItem((short) 0);
                                        ao.itemOptions.add(new Item.ItemOption(129));
                                        ao.itemOptions.add(new Item.ItemOption(141));
                                        Item quan = ItemService.gI().createNewItem((short) 6);
                                        quan.itemOptions.add(new Item.ItemOption(129));
                                        quan.itemOptions.add(new Item.ItemOption(141));
                                        Item gang = ItemService.gI().createNewItem((short) 21);
                                        gang.itemOptions.add(new Item.ItemOption(129));
                                        gang.itemOptions.add(new Item.ItemOption(141));
                                        Item giay = ItemService.gI().createNewItem((short) 27);
                                        giay.itemOptions.add(new Item.ItemOption(129));
                                        giay.itemOptions.add(new Item.ItemOption(141));
                                        Item rada = ItemService.gI().createNewItem((short) 57);
                                        rada.itemOptions.add(new Item.ItemOption(129));
                                        rada.itemOptions.add(new Item.ItemOption(141));
                                        InventoryServiceNew.gI().addItemBag(player, ao);
                                        InventoryServiceNew.gI().addItemBag(player, quan);
                                        InventoryServiceNew.gI().addItemBag(player, gang);
                                        InventoryServiceNew.gI().addItemBag(player, giay);
                                        InventoryServiceNew.gI().addItemBag(player, rada);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được set kích hoạt");

                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ hành trang để sử dụng!");
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20001:// set angry goku 128
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                    Item item = InventoryServiceNew.gI().findItemBag(player, 2000);
                                    if (item != null) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                        Item ao = ItemService.gI().createNewItem((short) 0);
                                        ao.itemOptions.add(new Item.ItemOption(128));
                                        ao.itemOptions.add(new Item.ItemOption(140));
                                        Item quan = ItemService.gI().createNewItem((short) 6);
                                        quan.itemOptions.add(new Item.ItemOption(128));
                                        quan.itemOptions.add(new Item.ItemOption(140));
                                        Item gang = ItemService.gI().createNewItem((short) 21);
                                        gang.itemOptions.add(new Item.ItemOption(128));
                                        gang.itemOptions.add(new Item.ItemOption(140));
                                        Item giay = ItemService.gI().createNewItem((short) 27);
                                        giay.itemOptions.add(new Item.ItemOption(128));
                                        giay.itemOptions.add(new Item.ItemOption(140));
                                        Item rada = ItemService.gI().createNewItem((short) 57);
                                        rada.itemOptions.add(new Item.ItemOption(128));
                                        rada.itemOptions.add(new Item.ItemOption(140));

                                        InventoryServiceNew.gI().addItemBag(player, ao);
                                        InventoryServiceNew.gI().addItemBag(player, quan);
                                        InventoryServiceNew.gI().addItemBag(player, gang);
                                        InventoryServiceNew.gI().addItemBag(player, giay);
                                        InventoryServiceNew.gI().addItemBag(player, rada);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được set kích hoạt");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ hành trang để sử dụng!");
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20002: // set then xin hang 127
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                    Item item = InventoryServiceNew.gI().findItemBag(player, 2000);
                                    if (item != null) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                        Item ao = ItemService.gI().createNewItem((short) 0);
                                        ao.itemOptions.add(new Item.ItemOption(127));
                                        ao.itemOptions.add(new Item.ItemOption(139));
                                        Item quan = ItemService.gI().createNewItem((short) 6);
                                        quan.itemOptions.add(new Item.ItemOption(127));
                                        quan.itemOptions.add(new Item.ItemOption(139));
                                        Item gang = ItemService.gI().createNewItem((short) 21);
                                        gang.itemOptions.add(new Item.ItemOption(127));
                                        gang.itemOptions.add(new Item.ItemOption(139));
                                        Item giay = ItemService.gI().createNewItem((short) 27);
                                        giay.itemOptions.add(new Item.ItemOption(127));
                                        giay.itemOptions.add(new Item.ItemOption(139));
                                        Item rada = ItemService.gI().createNewItem((short) 57);
                                        rada.itemOptions.add(new Item.ItemOption(127));
                                        rada.itemOptions.add(new Item.ItemOption(139));
                                        InventoryServiceNew.gI().addItemBag(player, ao);
                                        InventoryServiceNew.gI().addItemBag(player, quan);
                                        InventoryServiceNew.gI().addItemBag(player, gang);
                                        InventoryServiceNew.gI().addItemBag(player, giay);
                                        InventoryServiceNew.gI().addItemBag(player, rada);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được set kích hoạt");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ hành trang để sử dụng!");
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20010:// set kami 132
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                    Item item = InventoryServiceNew.gI().findItemBag(player, 2001);
                                    if (item != null) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                        Item ao = ItemService.gI().createNewItem((short) 1);
                                        ao.itemOptions.add(new Item.ItemOption(132));
                                        ao.itemOptions.add(new Item.ItemOption(144));
                                        Item quan = ItemService.gI().createNewItem((short) 7);
                                        quan.itemOptions.add(new Item.ItemOption(132));
                                        quan.itemOptions.add(new Item.ItemOption(144));
                                        Item gang = ItemService.gI().createNewItem((short) 22);
                                        gang.itemOptions.add(new Item.ItemOption(132));
                                        gang.itemOptions.add(new Item.ItemOption(144));
                                        Item giay = ItemService.gI().createNewItem((short) 28);
                                        giay.itemOptions.add(new Item.ItemOption(132));
                                        giay.itemOptions.add(new Item.ItemOption(144));
                                        Item rada = ItemService.gI().createNewItem((short) 57);
                                        rada.itemOptions.add(new Item.ItemOption(132));
                                        rada.itemOptions.add(new Item.ItemOption(144));
                                        InventoryServiceNew.gI().addItemBag(player, ao);
                                        InventoryServiceNew.gI().addItemBag(player, quan);
                                        InventoryServiceNew.gI().addItemBag(player, gang);
                                        InventoryServiceNew.gI().addItemBag(player, giay);
                                        InventoryServiceNew.gI().addItemBag(player, rada);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được set kích hoạt");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ hành trang để sử dụng!");
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20011:// nail 131
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                    Item item = InventoryServiceNew.gI().findItemBag(player, 2001);
                                    if (item != null) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                        Item ao = ItemService.gI().createNewItem((short) 1);
                                        ao.itemOptions.add(new Item.ItemOption(131));
                                        ao.itemOptions.add(new Item.ItemOption(143));
                                        Item quan = ItemService.gI().createNewItem((short) 7);
                                        quan.itemOptions.add(new Item.ItemOption(131));
                                        quan.itemOptions.add(new Item.ItemOption(143));
                                        Item gang = ItemService.gI().createNewItem((short) 22);
                                        gang.itemOptions.add(new Item.ItemOption(131));
                                        gang.itemOptions.add(new Item.ItemOption(143));
                                        Item giay = ItemService.gI().createNewItem((short) 28);
                                        giay.itemOptions.add(new Item.ItemOption(131));
                                        giay.itemOptions.add(new Item.ItemOption(143));
                                        Item rada = ItemService.gI().createNewItem((short) 57);
                                        rada.itemOptions.add(new Item.ItemOption(131));
                                        rada.itemOptions.add(new Item.ItemOption(143));
                                        InventoryServiceNew.gI().addItemBag(player, ao);
                                        InventoryServiceNew.gI().addItemBag(player, quan);
                                        InventoryServiceNew.gI().addItemBag(player, gang);
                                        InventoryServiceNew.gI().addItemBag(player, giay);
                                        InventoryServiceNew.gI().addItemBag(player, rada);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được set kích hoạt");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ hành trang để sử dụng!");
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20012: // 130 set picolo
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                    Item item = InventoryServiceNew.gI().findItemBag(player, 2001);
                                    if (item != null) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                        Item ao = ItemService.gI().createNewItem((short) 1);
                                        ao.itemOptions.add(new Item.ItemOption(130));
                                        ao.itemOptions.add(new Item.ItemOption(142));
                                        Item quan = ItemService.gI().createNewItem((short) 7);
                                        quan.itemOptions.add(new Item.ItemOption(130));
                                        quan.itemOptions.add(new Item.ItemOption(142));
                                        Item gang = ItemService.gI().createNewItem((short) 22);
                                        gang.itemOptions.add(new Item.ItemOption(130));
                                        gang.itemOptions.add(new Item.ItemOption(142));
                                        Item giay = ItemService.gI().createNewItem((short) 28);
                                        giay.itemOptions.add(new Item.ItemOption(130));
                                        giay.itemOptions.add(new Item.ItemOption(142));
                                        Item rada = ItemService.gI().createNewItem((short) 57);
                                        rada.itemOptions.add(new Item.ItemOption(130));
                                        rada.itemOptions.add(new Item.ItemOption(142));
                                        InventoryServiceNew.gI().addItemBag(player, ao);
                                        InventoryServiceNew.gI().addItemBag(player, quan);
                                        InventoryServiceNew.gI().addItemBag(player, gang);
                                        InventoryServiceNew.gI().addItemBag(player, giay);
                                        InventoryServiceNew.gI().addItemBag(player, rada);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được set kích hoạt");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ hành trang để sử dụng!");
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20020: // set vegeta 135
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                    Item item = InventoryServiceNew.gI().findItemBag(player, 2002);
                                    if (item != null) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                        Item ao = ItemService.gI().createNewItem((short) 2);
                                        ao.itemOptions.add(new Item.ItemOption(135));
                                        ao.itemOptions.add(new Item.ItemOption(138));
                                        Item quan = ItemService.gI().createNewItem((short) 8);
                                        quan.itemOptions.add(new Item.ItemOption(135));
                                        quan.itemOptions.add(new Item.ItemOption(138));
                                        Item gang = ItemService.gI().createNewItem((short) 23);
                                        gang.itemOptions.add(new Item.ItemOption(135));
                                        gang.itemOptions.add(new Item.ItemOption(138));
                                        Item giay = ItemService.gI().createNewItem((short) 29);
                                        giay.itemOptions.add(new Item.ItemOption(135));
                                        giay.itemOptions.add(new Item.ItemOption(138));
                                        Item rada = ItemService.gI().createNewItem((short) 57);
                                        rada.itemOptions.add(new Item.ItemOption(135));
                                        rada.itemOptions.add(new Item.ItemOption(138));
                                        InventoryServiceNew.gI().addItemBag(player, ao);
                                        InventoryServiceNew.gI().addItemBag(player, quan);
                                        InventoryServiceNew.gI().addItemBag(player, gang);
                                        InventoryServiceNew.gI().addItemBag(player, giay);
                                        InventoryServiceNew.gI().addItemBag(player, rada);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được set kích hoạt");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ hành trang để sử dụng!");
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20021: // set cumber 134
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                    Item item = InventoryServiceNew.gI().findItemBag(player, 2002);
                                    if (item != null) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                        Item ao = ItemService.gI().createNewItem((short) 2);
                                        ao.itemOptions.add(new Item.ItemOption(134));
                                        ao.itemOptions.add(new Item.ItemOption(137));
                                        Item quan = ItemService.gI().createNewItem((short) 8);
                                        quan.itemOptions.add(new Item.ItemOption(134));
                                        quan.itemOptions.add(new Item.ItemOption(137));
                                        Item gang = ItemService.gI().createNewItem((short) 23);
                                        gang.itemOptions.add(new Item.ItemOption(134));
                                        gang.itemOptions.add(new Item.ItemOption(137));
                                        Item giay = ItemService.gI().createNewItem((short) 29);
                                        giay.itemOptions.add(new Item.ItemOption(134));
                                        giay.itemOptions.add(new Item.ItemOption(137));
                                        Item rada = ItemService.gI().createNewItem((short) 57);
                                        rada.itemOptions.add(new Item.ItemOption(134));
                                        rada.itemOptions.add(new Item.ItemOption(137));
                                        InventoryServiceNew.gI().addItemBag(player, ao);
                                        InventoryServiceNew.gI().addItemBag(player, quan);
                                        InventoryServiceNew.gI().addItemBag(player, gang);
                                        InventoryServiceNew.gI().addItemBag(player, giay);
                                        InventoryServiceNew.gI().addItemBag(player, rada);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được set kích hoạt");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ hành trang để sử dụng!");
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                        break;
                    case 20022:// set kakakrot 133
                        switch (select) {
                            case 0:
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                    Item item = InventoryServiceNew.gI().findItemBag(player, 2002);
                                    if (item != null) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                                        Item ao = ItemService.gI().createNewItem((short) 2);
                                        ao.itemOptions.add(new Item.ItemOption(133));
                                        ao.itemOptions.add(new Item.ItemOption(136));
                                        Item quan = ItemService.gI().createNewItem((short) 8);
                                        quan.itemOptions.add(new Item.ItemOption(133));
                                        quan.itemOptions.add(new Item.ItemOption(136));
                                        Item gang = ItemService.gI().createNewItem((short) 23);
                                        gang.itemOptions.add(new Item.ItemOption(133));
                                        gang.itemOptions.add(new Item.ItemOption(136));
                                        Item giay = ItemService.gI().createNewItem((short) 29);
                                        giay.itemOptions.add(new Item.ItemOption(133));
                                        giay.itemOptions.add(new Item.ItemOption(136));
                                        Item rada = ItemService.gI().createNewItem((short) 57);
                                        rada.itemOptions.add(new Item.ItemOption(133));
                                        rada.itemOptions.add(new Item.ItemOption(136));
                                        InventoryServiceNew.gI().addItemBag(player, ao);
                                        InventoryServiceNew.gI().addItemBag(player, quan);
                                        InventoryServiceNew.gI().addItemBag(player, gang);
                                        InventoryServiceNew.gI().addItemBag(player, giay);
                                        InventoryServiceNew.gI().addItemBag(player, rada);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được set kích hoạt");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ hành trang để sử dụng!");
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                        break;
                }
            }
        };
    }
}
