package com.louisnguyen.services.func;

import com.girlkun.database.GirlkunDB;
import com.louisnguyen.consts.ConstNpc;
import com.louisnguyen.jdbc.daos.PlayerDAO;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.bandokhobau.BanDoKhoBauService;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.npc.Npc;
import com.louisnguyen.models.npc.NpcManager;
import com.louisnguyen.models.player.Inventory;
import com.louisnguyen.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.louisnguyen.server.Client;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.GiftService;
import com.louisnguyen.services.InventoryServiceNew;
import com.louisnguyen.services.ItemService;
//import com.louisnguyen.services.NapThe;
//import com.louisnguyen.services.NapThe;
import com.louisnguyen.services.NpcService;
import com.louisnguyen.utils.Util;

import java.util.HashMap;
import java.util.Map;

public class Input {

    public static String LOAI_THE;
    public static String MENH_GIA;
    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<Integer, Object>();

    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 504;
    public static final int NAP_THE = 505;
    public static final int CHANGE_NAME_BY_ITEM = 506;
    public static final int GIVE_IT = 507;
    public static final int SEND_ITEM_OP = 512;

    public static final int QUY_DOI_COIN = 508;
    public static final int QUY_DOI_NGOC_XANH = 509;

    public static final int TAI = 510;
    public static final int XIU = 511;

    public static final int QUY_DOI_HRZ = 513;
    public static final int CHOOSE_LEVEL_KGHD = 514;

    public static final int CHOOSE_LEVEL_CDRD = 515;
    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;

    private static Input intance;

    private Input() {

    }

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case GIVE_IT:
                    String name = text[0];
                    int id = Integer.valueOf(text[1]);
                    int q = Integer.valueOf(text[2]);
                    if (Client.gI().getPlayer(name) != null) {
                        Item item = ItemService.gI().createNewItem(((short) id));
                        item.quantity = q;
                        InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), item);
                        InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                        Service.gI().sendThongBao(Client.gI().getPlayer(name), "Nhận " + item.template.name + " từ " + player.name);

                    } else {
                        Service.gI().sendThongBao(player, "Không online");
                    }
                    break;
                case SEND_ITEM_OP:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        String[] options = text[2].split(" ");
                        int slItemBuff = Integer.parseInt(text[3]);
                        Player admin = player;
                        Player target = Client.gI().getPlayer(text[0]);
                        if (target != null) {
                            String txtBuff = "Bạn vừa nhận được: " + target.name + ", hãy kiểm tra hành trang\n";
                            Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                            for (String option : options) {
                                String[] optionParts = option.split("-");
                                int idOptionBuff = Integer.parseInt(optionParts[0]);
                                int slOptionBuff = Integer.parseInt(optionParts[1]);
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuff, slOptionBuff));
                            }
                            itemBuffTemplate.quantity = slItemBuff;
                            InventoryServiceNew.gI().addItemBag(target, itemBuffTemplate);
                            InventoryServiceNew.gI().sendItemBags(target);
                            txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\n";
                            Service.gI().sendThongBao(target, txtBuff);
                        } else {
                            Service.gI().sendThongBao(admin, "Không tìm thấy cư dân hoặc cư dân.");
                        }
                    }
                    break;
                    
                    case GIFT_CODE:
                    GiftService.gI().giftCode(player, text[0]);
                    break;
                    
                case CHANGE_PASSWORD:
                    Service.gI().changePassword(player, text[0], text[1], text[2]);
                    break;
                case FIND_PLAYER:
                    Player pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, 1139, "Quyền Năng Thiên Sứ?",
                                new String[]{"Dịch chuyển\nđến\n" + pl.name, "Triệu hồi\n" + pl.name + "\nđến đây", "Đổi tên\n" + pl.name + "", "Khóa\n" + pl.name + "", "Đăng xuất\n" + pl.name + ""},
                                pl);
                    } else {
                        Service.gI().sendThongBao(player, "Cư dân " + pl.name + " hiện tại không online!!");
                    }
                    break;
                case CHANGE_NAME: {
                    Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
                    if (plChanged != null) {
                        if (GirlkunDB.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên cư dân đã tồn tại!");
                        } else {
                            plChanged.name = text[0];
                            GirlkunDB.executeUpdate("update player set name = ? where id = ?", plChanged.name, plChanged.id);
                            Service.gI().player(plChanged);
                            Service.gI().Send_Caitrang(plChanged);
                            Service.gI().sendFlagBag(plChanged);
                            Zone zone = plChanged.zone;
                            ChangeMapService.gI().changeMap(plChanged, zone, plChanged.location.x, plChanged.location.y);
                            Service.gI().sendThongBao(plChanged, "Đại thiên sứ đã đổi tên của bạn thành: " + player.name + "");
                            Service.gI().sendThongBao(player, "Quyền năng của ngài đã áp dụng lên cư dân  ");
                        }
                    }
                }
                break;
                case CHANGE_NAME_BY_ITEM: {
                    if (player != null) {
                        if (GirlkunDB.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
                            createFormChangeNameByItem(player);
                        } else {
                            Item theDoiTen = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2006);
                            if (theDoiTen == null) {
                                Service.gI().sendThongBao(player, "Không tìm thấy thẻ đổi tên");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, theDoiTen, 1);
                                player.name = text[0];
                                GirlkunDB.executeUpdate("update player set name = ? where id = ?", player.name, player.id);
                                Service.gI().player(player);
                                Service.gI().Send_Caitrang(player);
                                Service.gI().sendFlagBag(player);
                                Zone zone = player.zone;
                                ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
                                Service.gI().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            }
                        }
                    }
                }
                break;

                case TAI:
                    int sotvxiu = Integer.valueOf(text[0]);
                    Item tv2 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 457) {
                            tv2 = item;
                            break;
                        }
                    }
                    try {
                        if (tv2 != null && tv2.quantity >= sotvxiu) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv2, sotvxiu);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
                                    tvthang.quantity = (int) Math.round(sotvxiu * 1.8);
                                    InventoryServiceNew.gI().addItemBag(player, tvthang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " "
                                            + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvxiu
                                            + " thỏi vàng vào Xỉu 1" + "\nKết quả : Xỉu" + "\n\nVề bờ");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "Số hệ thống quay ra : " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvxiu + " thỏi vàng vào Xỉu" + "\nKết quả : Tam hoa" + "\nCòn cái nịt.");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra là :"
                                            + " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                            + sotvxiu + " thỏi vàng vào Xỉu 2" + "\nKết quả : Tài" + "\nCòn cái nịt.");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case XIU:
                    int sotvtai = Integer.valueOf(text[0]);
                    Item tv1 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.isNotNullItem() && item.template.id == 457) {
                            tv1 = item;
                            break;
                        }
                    }
                    try {
                        if (tv1 != null && tv1.quantity >= sotvtai) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, sotvtai);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra là :"
                                            + " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                            + sotvtai + " thỏi vàng vào Tài 1" + "\nKết quả : Xỉu " + "\nCòn cái nịt.");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "Số hệ thống quay ra : " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvtai + " thỏi vàng vào Xỉu" + "\nKết quả : Tam hoa" + "\nCòn cái nịt.");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
                                    tvthang.quantity = (int) Math.round(sotvtai * 1.8);
                                    InventoryServiceNew.gI().addItemBag(player, tvthang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " "
                                            + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvtai
                                            + " thỏi vàng vào Tài 2 " + "\nKết quả : Tài" + "\n\nVề bờ");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;
                case CHOOSE_LEVEL_BDKB:
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                case CHOOSE_LEVEL_KGHD:
                    int level2 = Integer.parseInt(text[0]);
                    if (level2 >= 1 && level2 <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.MR_POPO, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_KGHD,
                                    "Con có chắc chắn muốn tới khí gas hủy diệt cấp độ " + level2 + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level2);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                case CHOOSE_LEVEL_CDRD:
                    int level3 = Integer.parseInt(text[0]);
                    if (level3 >= 1 && level3 <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.THAN_VU_TRU, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_CDRD,
                                    "Con có chắc chắn muốn tới con đường rắn độc cấp độ " + level3 + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level3);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                case NAP_THE:
//                    NapThe.SendCard(player, LOAI_THE, MENH_GIA, text[0], text[1]);
                    break;
                case QUY_DOI_COIN:
                    int ratioGold = 1;
                    int coinGold = 10;
                    int goldTrade = Integer.parseInt(text[0]);
                    if (goldTrade <= 0 || goldTrade >= 500) {
                        Service.gI().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được 500");
                    } else if (player.session.vnd >= goldTrade * coinGold) {
                        PlayerDAO.subvndBar(player, goldTrade * coinGold);
                        Item thoiVang = ItemService.gI().createNewItem((short) 457, goldTrade * 1);// x3
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + goldTrade * ratioGold
                                + " " + thoiVang.template.name);
                    } else {
                        Service.gI().sendThongBao(player, "Số Xu của bạn là " + player.session.vnd + " không đủ để quy "
                                + " đổi " + goldTrade + " thỏi vàng " + " " + "bạn cần thêm" + (player.session.vnd - goldTrade));
                    }
                    break;

                case QUY_DOI_NGOC_XANH:
                    int ratioGem = 1;
                    int coinGem = 1;
                    int GemTrade = Integer.parseInt(text[0]);
                    if (GemTrade <= 0 || GemTrade >= 100000000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được 100.000.000 Ngọc xanh");
                    } else if (player.session.vnd >= GemTrade * coinGem) {
                        PlayerDAO.subvndBar(player, GemTrade * coinGem);
                        Item thoiVang = ItemService.gI().createNewItem((short) 77, GemTrade * 10);// x3
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + GemTrade * ratioGem
                                + " " + thoiVang.template.name);
                    } else {
                        Service.gI().sendThongBao(player, "Số Xu của bạn là " + player.session.vnd + " không đủ để quy "
                                + " đổi " + GemTrade + " thỏi vàng " + " " + "bạn cần thêm" + (player.session.vnd - GemTrade));
                    }
                    break;

                case QUY_DOI_HRZ:
                    int ratioHRZ = 1;
                    int coinHRZ = 1;
                    int HRZTrade = Integer.parseInt(text[0]);
                    if (HRZTrade <= 0 || HRZTrade >= 100000000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được 100.000.000 Ngọc Hồng");
                    } else if (player.session.vnd >= HRZTrade * coinHRZ) {
                        PlayerDAO.subvndBar(player, HRZTrade * coinHRZ);
                        Item thoiVang = ItemService.gI().createNewItem((short) 861, HRZTrade * 10);//
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + HRZTrade * ratioHRZ
                                + " " + thoiVang.template.name);
                    } else {
                        Service.gI().sendThongBao(player, "Số Xu của bạn là " + player.session.vnd + " không đủ để quy "
                                + " đổi " + HRZTrade + " thỏi vàng " + " " + "bạn cần thêm" + (player.session.vnd - HRZTrade));
                    }
                    break;
            }
        } catch (Exception e) {
        }

    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createForm(ISession session, int typeInput, String title, SubInput... subInputs) {
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Quên Mật Khẩu", new SubInput("Nhập mật khẩu đã quên", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormGiveItem(Player pl) {
        createForm(pl, GIVE_IT, "Tặng vật phẩm", new SubInput("Tên", ANY), new SubInput("Id Item", ANY), new SubInput("Số lượng", ANY));
    }

    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "GiftCode", new SubInput("Mã tân thủ", ANY));
    }

    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }

    public void TAI(Player pl) {
        createForm(pl, TAI, "Chọn số thỏi vàng đặt Xỉu", new SubInput("Số thỏi vàng", ANY));//????
    }

    public void XIU(Player pl) {
        createForm(pl, XIU, "Chọn số thỏi vàng đặt Tài", new SubInput("Số thỏi vàng", ANY));
    }

    public void createFormNapThe(Player pl, String loaiThe, String menhGia) {
        LOAI_THE = loaiThe;
        MENH_GIA = menhGia;
        createForm(pl, NAP_THE, "Nạp thẻ", new SubInput("Số Seri", ANY), new SubInput("Mã thẻ", ANY));
    }

    public void createFormSenditem1(Player pl) {
        createForm(pl, SEND_ITEM_OP, "Buff item",
                new SubInput("Name", ANY),
                new SubInput("ID Item", NUMERIC),
                new SubInput("ID Option", ANY),
                new SubInput("Quantity", NUMERIC));
    }

    public void createFormQDTV(Player pl) {

        createForm(pl, QUY_DOI_COIN, "Quy đổi thỏi vàng, giới hạn đổi không quá 500 Thỏi vàng."
                + "\n10 Xu = 1 Thỏi vàng", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormQDNX(Player pl) {

        createForm(pl, QUY_DOI_NGOC_XANH, "Quy đổi Xu sang Ngọc Xanh."
                + "\n1 Xu = 10 Ngọc Xanh", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormQDHRZ(Player pl) {

        createForm(pl, QUY_DOI_HRZ, "Quy đổi Xu sang Ngọc Hồng."
                + "\n1 Xu = 10 Ngọc Hồng", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChangeNameByItem(Player pl) {
        createForm(pl, CHANGE_NAME_BY_ITEM, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormChooseLevelKGHD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_KGHD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormChooseLevelCDRD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_CDRD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public static class SubInput {

        private String name;
        private byte typeInput;

        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }

}
