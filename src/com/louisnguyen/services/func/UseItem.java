package com.louisnguyen.services.func;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.girlkun.network.io.Message;
import com.louisnguyen.card.Card;
import com.louisnguyen.card.RadarCard;
import com.louisnguyen.card.RadarService;
import com.louisnguyen.consts.ConstMap;
import com.louisnguyen.consts.ConstNpc;
import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.item.Item.ItemOption;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.player.Inventory;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.models.skill.Skill;
import com.louisnguyen.server.io.MySession;
import com.louisnguyen.services.InventoryServiceNew;
import com.louisnguyen.services.ItemService;
import com.louisnguyen.services.ItemTimeService;
import com.louisnguyen.services.MapService;
import com.louisnguyen.services.NgocRongNamecService;
import com.louisnguyen.services.NpcService;
import com.louisnguyen.services.PetService;
import com.louisnguyen.services.PlayerService;
import com.louisnguyen.services.RewardService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.TaskService;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.SkillUtil;
import com.louisnguyen.utils.TimeUtil;
import com.louisnguyen.utils.Util;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    public int Gio;

    private static UseItem instance;

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;

        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            // Item ThoNgoc = InventoryServiceNew.gI().findItemBag(player, 1165);
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryServiceNew.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryServiceNew.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryServiceNew.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryServiceNew.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryServiceNew.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryServiceNew.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryServiceNew.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.gI().point(player);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);

        }
    }

    public void testItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            System.out.println("type: " + type);
            System.out.println("where: " + where);
            System.out.println("index: " + index);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            // System.out.println(type + " " + where + " " + index);
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học "
                                            + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            this.eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }
                        msg = new Message(-43);
                        msg.writer().writeByte(type);
                        msg.writer().writeByte(where);
                        msg.writer().writeByte(index);
                        msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                        player.sendMessage(msg);
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryServiceNew.gI().throwItem(player, where, index);
                    Service.gI().point(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                case ACCEPT_USE_ITEM:
                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {
            // Logger.logException(UseItem.class, e);
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        if (item.template.strRequire <= pl.nPoint.power) {
            switch (item.template.type) {
                case 7: // sách học, nâng skill
                    learnSkill(pl, item);
                    break;
                case 33:
                    UseCard(pl, item);
                    break;
                case 6: // đậu thần
                    this.eatPea(pl);
                    break;
                case 12: // ngọc rồng các loại
                    controllerCallRongThan(pl, item);
                    break;
                case 11: // item bag
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFlagBag(pl);
                    break;
                case 39: // ngọc bội
                    Service.gI().sendThongBao(pl, "Vui lòng tới Thợ Ngọc\ntại Đảo Kame để Thăng\nhoa sử dụng");
                    return;
                case 72: {
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    // Service.getInstance().sendPetFollow(pl, 1,
                    // (short) (pl.inventory.itemsBody.get(9).template.iconID-1), (byte) 1, (short)
                    // 35,
                    // (short) 30, pl.session, new byte[] { 0,1,2,3,4,5,6,7 });
                    break;
                }
                case 76:
                    if (pl.newpet != null) {
                        ChangeMapService.gI().exitMap(pl.newpet);
                        pl.newpet.dispose();
                        pl.newpet = null;
                        pl.isNewPet = false; // Add this line to reset the isNewPet flag
                    }
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    PetService.Pet2(pl, item.template.head, item.template.body, item.template.leg);
                    Service.gI().point(pl);
                    break;
                default:
                    switch (item.template.id) {
                        case 992:
                            if (calendar.get(Calendar.HOUR_OF_DAY) >= 14 && calendar.get(Calendar.HOUR_OF_DAY) <= 15) {
                                pl.type = 1;
                                pl.maxTime = 5;
                                Service.gI().Transport(pl);
                                break;
                            } else {
                                Service.gI().sendThongBao(pl,
                                        "Hiện không thể tìm thấy tung tích của Kẻ phá hủy thời gian!!!");
                                break;
                            }
                        case 361:
                            if (pl.idNRNM != -1) {
                                Service.gI().sendThongBao(pl, "Không thể thực hiện");
                                return;
                            }
                            pl.idGo = (short) Util.nextInt(0, 6);
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.CONFIRM_TELE_NAMEC, -1,
                                    "1 Sao (" + NgocRongNamecService.gI().getDis(pl, 0, (short) 353) + " m)\n2 Sao ("
                                            + NgocRongNamecService.gI().getDis(pl, 1, (short) 354) + " m)\n3 Sao ("
                                            + NgocRongNamecService.gI().getDis(pl, 2, (short) 355) + " m)\n4 Sao ("
                                            + NgocRongNamecService.gI().getDis(pl, 3, (short) 356) + " m)\n5 Sao ("
                                            + NgocRongNamecService.gI().getDis(pl, 4, (short) 357) + " m)\n6 Sao ("
                                            + NgocRongNamecService.gI().getDis(pl, 5, (short) 358) + " m)\n7 Sao ("
                                            + NgocRongNamecService.gI().getDis(pl, 6, (short) 359) + " m)",
                                    "Đến ngay\nViên " + (pl.idGo + 1) + " Sao\n50 ngọc", "Kết thức");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;
                        // case 1165:
                        // Service.gI().sendThongBao(pl, "Vui lòng tới Thợ Ngọc\ntại Đảo Kame để
                        // Thăng\nhoa sử dụng");
                        // break;
                        case 1499:
                            if (pl.lastTimeTitle3 == 0) {
                                pl.lastTimeTitle3 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);
                            } else {
                                pl.lastTimeTitle3 += (1000 * 60 * 60 * 24 * 3);
                            }
                            pl.isTitleUse3 = true;
                            Service.gI().point(pl);
                            Service.gI().sendTitle(pl, 890);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            Service.gI().sendThongBao(pl, "Bạn nhận được 3 ngày danh hiệu !");
                            break;
                        case 1153: // Qua tan thu day nay
                            int itemId = 1021;
                            switch (Util.nextInt(1, 3)) {
                                case 1:
                                    itemId = 733;
                                    break;
                                case 2:
                                    itemId = 1106;
                                    break;
                                case 3:
                                    itemId = 746;
                                    break;
                                default:
                                    break;
                            }
                            Item PhanQua = ItemService.gI().createNewItem(((short) itemId));
                            PhanQua.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 20)));
                            PhanQua.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 20)));
                            PhanQua.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 20)));
                            PhanQua.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 20)));
                            // switch (Util.nextInt(1,3)) { // tính thêm cái hạn sử dụng.
                            // case 1:
                            // case 2:
                            // PhanQua.itemOptions.add(new Item.ItemOption(93, Util.nextInt(5, 10)));
                            // break;
                            // case 3:
                            // break;
                            // }

                            int itemId2 = 457;
                            Item PhanQua2 = ItemService.gI().createNewItem(((short) itemId2));
                            PhanQua2.quantity = 999;
                            PhanQua2.itemOptions.add(new Item.ItemOption(30, 1));

                            int itemId3 = 454;
                            Item PhanQua3 = ItemService.gI().createNewItem(((short) itemId3));

                            int itemId4 = 194;
                            Item PhanQua4 = ItemService.gI().createNewItem(((short) itemId4));

                            int itemId6 = 380;
                            Item PhanQua6 = ItemService.gI().createNewItem(((short) itemId6));
                            PhanQua6.quantity = 99;

                            int itemId11 = 441;
                            Item PhanQua11 = ItemService.gI().createNewItem(((short) itemId11));
                            PhanQua11.itemOptions.add(new Item.ItemOption(95, 5));
                            PhanQua11.quantity = 99;

                            int itemId12 = 442;
                            Item PhanQua12 = ItemService.gI().createNewItem(((short) itemId12));
                            PhanQua12.itemOptions.add(new Item.ItemOption(96, 5));
                            PhanQua12.quantity = 99;

                            int itemId13 = 443;
                            Item PhanQua13 = ItemService.gI().createNewItem(((short) itemId13));
                            PhanQua13.itemOptions.add(new Item.ItemOption(97, 5));
                            PhanQua13.quantity = 99;

                            // int itemId14 = 444;
                            // Item PhanQua14 = ItemService.gI().createNewItem(((short) itemId14));
                            // PhanQua14.itemOptions.add(new Item.ItemOption(98, 3));
                            // PhanQua14.quantity = 10;

                            // int itemId15 = 445;
                            // Item PhanQua15 = ItemService.gI().createNewItem(((short) itemId15));
                            // PhanQua15.itemOptions.add(new Item.ItemOption(99, 3));
                            // PhanQua15.quantity = 10;

                            // int itemId16 = 446;
                            // Item PhanQua16 = ItemService.gI().createNewItem(((short) itemId16));
                            // PhanQua16.itemOptions.add(new Item.ItemOption(100, 5));
                            // PhanQua16.quantity = 10;

                            int itemId17 = 447;
                            Item PhanQua17 = ItemService.gI().createNewItem(((short) itemId17));
                            PhanQua17.itemOptions.add(new Item.ItemOption(101, 5));
                            PhanQua17.quantity = 99;

                            pl.inventory.gold = 2000000000L;
                            pl.inventory.gem += 1000000;

                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua.Name());
                            InventoryServiceNew.gI().addItemBag(pl, PhanQua2);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua2.Name());
                            InventoryServiceNew.gI().addItemBag(pl, PhanQua3);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua3.Name());
                            InventoryServiceNew.gI().addItemBag(pl, PhanQua4);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua4.Name());

                            Item nr1s = ItemService.gI().createNewItem(((short) 14));
                            Item nr2s = ItemService.gI().createNewItem(((short) 15));
                            Item nr3s = ItemService.gI().createNewItem(((short) 16));
                            InventoryServiceNew.gI().addItemBag(pl, nr1s);
                            InventoryServiceNew.gI().addItemBag(pl, nr2s);
                            InventoryServiceNew.gI().addItemBag(pl, nr3s);

                            if (pl.gender == 0) {
                                Item PhanQua5 = ItemService.gI().createNewItem(((short) 0));
                                PhanQua5.itemOptions.add(new Item.ItemOption(47, 3));
                                PhanQua5.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua7 = ItemService.gI().createNewItem(((short) 6));
                                PhanQua7.itemOptions.add(new Item.ItemOption(6, 20));
                                PhanQua7.itemOptions.add(new Item.ItemOption(27, 3));
                                PhanQua7.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua8 = ItemService.gI().createNewItem(((short) 21));
                                PhanQua8.itemOptions.add(new Item.ItemOption(0, 5));
                                PhanQua8.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua9 = ItemService.gI().createNewItem(((short) 27));
                                PhanQua9.itemOptions.add(new Item.ItemOption(7, 5));
                                PhanQua9.itemOptions.add(new Item.ItemOption(28, 3));
                                PhanQua9.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua10 = ItemService.gI().createNewItem(((short) 12));
                                PhanQua10.itemOptions.add(new Item.ItemOption(14, 1));
                                PhanQua10.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua20 = ItemService.gI().createNewItem(((short) 2000));
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua5.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua7.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua8.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua9.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua10.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua20.Name());
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua5);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua7);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua8);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua9);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua10);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua20);
                                InventoryServiceNew.gI().sendItemBags(pl);
                            }
                            if (pl.gender == 1) {
                                Item PhanQua5 = ItemService.gI().createNewItem(((short) 1));
                                PhanQua5.itemOptions.add(new Item.ItemOption(47, 3));
                                PhanQua5.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua7 = ItemService.gI().createNewItem(((short) 7));
                                PhanQua7.itemOptions.add(new Item.ItemOption(6, 20));
                                PhanQua7.itemOptions.add(new Item.ItemOption(27, 3));
                                PhanQua7.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua8 = ItemService.gI().createNewItem(((short) 22));
                                PhanQua8.itemOptions.add(new Item.ItemOption(0, 5));
                                PhanQua8.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua9 = ItemService.gI().createNewItem(((short) 28));
                                PhanQua9.itemOptions.add(new Item.ItemOption(7, 5));
                                PhanQua9.itemOptions.add(new Item.ItemOption(28, 3));
                                PhanQua9.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua10 = ItemService.gI().createNewItem(((short) 12));
                                PhanQua10.itemOptions.add(new Item.ItemOption(14, 1));
                                PhanQua10.itemOptions.add(new Item.ItemOption(107, 3));
                                Item PhanQua21 = ItemService.gI().createNewItem(((short) 2001));

                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua5.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua7.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua8.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua9.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua10.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua21.Name());
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua5);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua7);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua8);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua9);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua10);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua21);
                                InventoryServiceNew.gI().sendItemBags(pl);
                            }
                            if (pl.gender == 2) {
                                Item PhanQua5 = ItemService.gI().createNewItem(((short) 2));
                                PhanQua5.itemOptions.add(new Item.ItemOption(47, 3));
                                PhanQua5.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua7 = ItemService.gI().createNewItem(((short) 8));
                                PhanQua7.itemOptions.add(new Item.ItemOption(6, 20));
                                PhanQua7.itemOptions.add(new Item.ItemOption(27, 3));
                                PhanQua7.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua8 = ItemService.gI().createNewItem(((short) 23));
                                PhanQua8.itemOptions.add(new Item.ItemOption(0, 5));
                                PhanQua8.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua9 = ItemService.gI().createNewItem(((short) 29));
                                PhanQua9.itemOptions.add(new Item.ItemOption(7, 5));
                                PhanQua9.itemOptions.add(new Item.ItemOption(28, 3));
                                PhanQua9.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua10 = ItemService.gI().createNewItem(((short) 12));
                                PhanQua10.itemOptions.add(new Item.ItemOption(14, 1));
                                PhanQua10.itemOptions.add(new Item.ItemOption(107, 3));

                                Item PhanQua22 = ItemService.gI().createNewItem(((short) 2002));
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua5.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua7.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua8.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua9.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua10.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua22.Name());
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua5);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua7);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua8);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua9);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua10);
                                InventoryServiceNew.gI().addItemBag(pl, PhanQua22);
                                InventoryServiceNew.gI().sendItemBags(pl);
                            }

                            InventoryServiceNew.gI().addItemBag(pl, PhanQua6);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua6.Name());
                            InventoryServiceNew.gI().addItemBag(pl, PhanQua11);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua11.Name());
                            InventoryServiceNew.gI().addItemBag(pl, PhanQua12);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua12.Name());
                            InventoryServiceNew.gI().addItemBag(pl, PhanQua13);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua13.Name());
                            // InventoryServiceNew.gI().addItemBag(pl, PhanQua14);
                            // Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua14.Name());
                            // InventoryServiceNew.gI().addItemBag(pl, PhanQua15);
                            // Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua15.Name());
                            // InventoryServiceNew.gI().addItemBag(pl, PhanQua16);
                            // Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua16.Name());
                            InventoryServiceNew.gI().addItemBag(pl, PhanQua17);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua17.Name());
                            Service.gI().sendThongBao(pl, "Bạn nhận được 2 Tỷ\nvàng");
                            Service.gI().sendThongBao(pl, "Bạn nhận được 1\nTriệu ngọc xanh");
                            InventoryServiceNew.gI().addItemBag(pl, PhanQua);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            Service.gI().sendMoney(pl);
                            break;
                        case 457: // thỏi vàng
                            Service.gI().sendThongBaoOK(pl, "Thỏi vàng chỉ có thể bán vào Shop!!");
                            break;
                        // pet này
                        case 892: // Thần chết cute
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 882, 883, 884);
                            Service.gI().point(pl);
                            break;
                        case 893: // Thần chết cute
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 885, 886, 887);
                            Service.gI().point(pl);
                            break;
                        case 909: // Thần chết cute
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 897, 898, 899);
                            Service.gI().point(pl);
                            break;
                        case 942:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 966, 967, 968);
                            Service.gI().point(pl);
                            break;
                        case 943:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 969, 970, 971);
                            Service.gI().point(pl);
                            break;
                        case 944:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 972, 973, 974);
                            Service.gI().point(pl);
                            break;
                        case 967:
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1050, 1051, 1052);
                            Service.gI().point(pl);
                            break;
                        case 1407: // Con cún vàng
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 663, 664, 665);
                            Service.gI().point(pl);
                            break;
                        case 1408: // Cua đỏ
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1074, 1075, 1076);
                            Service.gI().point(pl);
                            break;
                        case 1410: // Bí ma vương
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1155, 1156, 1157);
                            Service.gI().point(pl);
                            break;
                        case 1411: // Mèo đuôi vàng đen
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1183, 1184, 1185);
                            Service.gI().point(pl);
                            break;
                        case 1412: // Mèo đuôi vàng trắng
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1201, 1202, 1203);
                            Service.gI().point(pl);
                            break;
                        case 1413: // Gà 9 cựa
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1239, 1240, 1241);
                            Service.gI().point(pl);
                            break;
                        case 1414: // Ngựa 9 hồng mao
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1242, 1243, 1244);
                            Service.gI().point(pl);
                            break;
                        case 1415: // Voi 9 ngà
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1245, 1246, 1247);
                            Service.gI().point(pl);
                            break;
                        case 1416: // Pet Minions
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1254, 1255, 1256);
                            Service.gI().point(pl);
                            break;

                        // đậu
                        case 293:
                            openGoiDau1(pl, item);
                            break;
                        case 294:
                            openGoiDau2(pl, item);
                            break;
                        case 295:
                            openGoiDau3(pl, item);
                            break;
                        case 296:
                            openGoiDau4(pl, item);
                            break;
                        case 297:
                            openGoiDau5(pl, item);
                            break;
                        case 298:
                            openGoiDau6(pl, item);
                            break;
                        case 299:
                            openGoiDau7(pl, item);
                            break;
                        case 596:
                            openGoiDau8(pl, item);
                            break;
                        case 597:
                            openGoiDau9(pl, item);
                            break;
                        case 211: // nho tím
                        case 212: // nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 401: // đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 1105:// hop qua skh, item 2002 xd
                            UseItem.gI().Hopts(pl, item);
                            break;
                        case 342:
                        case 343:
                        case 344:
                        case 345:
                            if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22)
                                    .count() < 5) {
                                Service.gI().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Đặt ít vệ tinh thôi");
                            }
                            break;
                        case 380: // cskb
                            openCSKB(pl, item);
                            break;
                        case 628:
                            openPhieuCaiTrangHaiTac(pl, item);
                        case 381: // cuồng nộ
                        case 382: // bổ huyết
                        case 383: // bổ khí
                        case 384: // giáp xên
                        case 385: // ẩn danh
                        case 379: // máy dò capsule
                        case 2037: // máy dò cosmos
                        case 663: // bánh pudding
                        case 664: // xúc xíc
                        case 665: // kem dâu
                        case 666: // mì ly
                        case 667: // sushi
                        case 1099:
                        case 1100:
                        case 1101:
                        case 1102:
                        case 1172:
                        case 1103:
                        case 1154:
                        case 1155:
                        case 1156:
                        case 1171:
                            useItemTime(pl, item);
                            break;
                        case 1132:
                            if (pl.setClothes.setthucan == 5) {
                                useItemTime(pl, item);
                                break;
                            } else if (pl.setClothes.setthucan != 5) {
                                Service.gI().sendThongBao(pl, "Yêu cầu đủ set thần!!");
                                return;
                            }
                        case 570:
                            openWoodChest(pl, item);
                        case 521: // tdlt
                            useTDLT(pl, item);
                            break;
                        case 454: // bông tai
                            UseItem.gI().usePorata(pl);
                            break;
                        case 193: // gói 10 viên capsule
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 194: // capsule đặc biệt
                            openCapsuleUI(pl);
                            break;
                        case 722: // Capsule hồng ngọc
                            int radomsoluonghongngoc = Util.nextInt(50, 135);
                            pl.inventory.ruby += radomsoluonghongngoc;
                            Service.gI().sendMoney(pl);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + radomsoluonghongngoc + " hồng ngọc");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 402: // sách nâng chiêu 1 đệ tử
                        case 403: // sách nâng chiêu 2 đệ tử
                        case 404: // sách nâng chiêu 3 đệ tử
                        case 759:
                        case 2058:
                        case 2059:
                        case 2060: // sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
                        case 921: // bông tai c2
                            UseItem.gI().usePorata2(pl);
                            break;
                        // case 736:
                        // ItemService.gI().OpenItem736(pl, item);
                        // break;
                        case 987:
                            Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); // đá bảo vệ
                            break;
                        case 1098:
                            useItemHopQuaTanThu(pl, item);
                            break;
                        case 1128:
                            openDaBaoVe(pl, item);
                            break;
                        case 1129:
                            openSPL(pl, item);
                            break;
                        case 1130:
                            openDaNangCap(pl, item);
                            break;
                        case 1131:
                            openManhTS(pl, item);
                            break;
                        // case 1133:
                        // SkillService.gI().learSkillSpecial(pl, Skill.MA_PHONG_BA);
                        // break;
                        // case 1134:
                        //// InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        //// InventoryServiceNew.gI().sendItemBags(pl);
                        // SkillService.gI().learSkillSpecial(pl, Skill.LIEN_HOAN_CHUONG);
                        // break;
                        case 2006:
                            Input.gI().createFormChangeNameByItem(pl);
                            break;
                        case 999:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(1).skillId != -1
                                    && pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                pl.pet.openSkill2();
                                pl.pet.openSkill3();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 2 3 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                            }
                            break;

                        case 648: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = null;
                                if (Util.isTrue(50, 100)) {
                                    linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026));
                                } else {
                                    int[] setID = { 1139, 1145, 1146, 1176, 1177, 1178, 1179, 1187, 1188, 1189, 1190,
                                            1191, 1192, 1193, 1194, 2033, 2034, 2035, 2038, 2039, 2040, 2041, 2042 };
                                    int randomIndex = Util.nextInt(0, setID.length - 1);
                                    short randomID = (short) setID[randomIndex];

                                    // Tạo item với ID ngẫu nhiên từ mảng setID[]
                                    linhThu = ItemService.gI().createNewItem((short) randomID);
                                }
                                linhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(95, Util.nextInt(5, 30)));
                                linhThu.itemOptions.add(new Item.ItemOption(96, Util.nextInt(5, 30)));
                                if (Util.isTrue(80, 100)) {
                                    linhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
                                }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl,
                                        "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                            }
                            break;
                        }
                        case 1150:
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                int[] setID = { 892, 893, 909, 916, 917, 918, 919, 936, 942, 943, 944, 1039, 1040,
                                        1047 };
                                int randomIndex = Util.nextInt(0, setID.length - 1);
                                short randomID = (short) setID[randomIndex];

                                // Tạo item với ID ngẫu nhiên từ mảng setID[]
                                Item linhThu = ItemService.gI().createNewItem((short) randomID);
                                linhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                                linhThu.itemOptions.add(new Item.ItemOption(5, Util.nextInt(5, 25)));
                                if (Util.isTrue(80, 100)) {
                                    linhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
                                }
                                // else{
                                // // linhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(2, 7)));
                                // }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl,
                                        "Chúc mừng bạn nhận được " + linhThu.template.name);
                            }
                            break;
                        case 736:// van bay
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                int[] setID = { 733, 734, 735, 743, 744, 746, 795, 849, 897, 920, 1092, 1106, 1107 };
                                int randomIndex = Util.nextInt(0, setID.length - 1);
                                short randomID = (short) setID[randomIndex];

                                // Tạo item với ID ngẫu nhiên từ mảng setID[]
                                Item linhThu = ItemService.gI().createNewItem((short) randomID);
                                linhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(18, Util.nextInt(5, 50)));
                                linhThu.itemOptions.add(new Item.ItemOption(5, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(16, Util.nextInt(20, 100)));
                                if (Util.isTrue(90, 100)) {
                                    linhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
                                }
                                // else{
                                // // linhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(2, 7)));
                                // }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl,
                                        "Chúc mừng bạn nhận được " + linhThu.template.name);
                            }
                            break;
                        case 668:// deo lung
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) 0);
                                if (Util.isTrue(5.263f, 100)) {
                                    linhThu = ItemService.gI().createNewItem((short) Util.nextInt(467, 471));
                                } else if (Util.isTrue(30.53f, 100)) {
                                    linhThu = ItemService.gI().createNewItem((short) Util.nextInt(766, 794));
                                } else if (Util.isTrue(6.31f, 100)) {
                                    linhThu = ItemService.gI().createNewItem((short) Util.nextInt(800, 805));
                                } else if (Util.isTrue(4.21f, 100)) {
                                    linhThu = ItemService.gI().createNewItem((short) Util.nextInt(814, 817));
                                } else if (Util.isTrue(8.42f, 100)) {
                                    linhThu = ItemService.gI().createNewItem((short) Util.nextInt(994, 1001));
                                } else if (Util.isTrue(8.42f, 100)) {
                                    linhThu = ItemService.gI().createNewItem((short) Util.nextInt(1021, 1028));
                                } else if (Util.isTrue(8.42f, 100)) {
                                    linhThu = ItemService.gI().createNewItem((short) Util.nextInt(1500, 1507));
                                } else {
                                    int[] setID = { 740, 741, 745, 822, 823, 852, 865, 954, 955, 966, 982, 983, 1007,
                                            1013, 1030, 1031, 1047, 1138, 1148, 1157, 1158, 1159, 1195, 1197, 1200,
                                            1202 };
                                    int randomIndex = Util.nextInt(0, setID.length - 1);
                                    short randomID = (short) setID[randomIndex];
                                    linhThu = ItemService.gI().createNewItem((short) randomID);
                                }
                                // Tạo item với ID ngẫu nhiên từ mảng setID[]
                                linhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                                linhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                                linhThu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 20)));
                                linhThu.itemOptions.add(new Item.ItemOption(17, Util.nextInt(5, 50)));
                                linhThu.itemOptions.add(new Item.ItemOption(117, Util.nextInt(5, 20)));
                                linhThu.itemOptions.add(new Item.ItemOption(78, Util.nextInt(5, 100)));
                                if (Util.isTrue(90, 100)) {
                                    linhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
                                }
                                // else{
                                // // linhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(2, 7)));
                                // }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl,
                                        "Chúc mừng bạn nhận được " + linhThu.template.name);
                            }
                            break;

                        case 568:
                            NpcService.gI().createMenuConMeo(pl, 568, -1, "Nở ra đệ mabu vip rồi !! Nhớ tháo đồ nhé!",
                                    "Đệ trái đất", "Đệ namec", "Đệ xayda");
                            break;
                        case 2027:
                            NpcService.gI().createMenuConMeo(pl, 2027, -1, "Nở ra đệ uub vip rồi !! Nhớ tháo đồ nhé!",
                                    "Đệ trái đất", "Đệ namec", "Đệ xayda");
                            break;
                        case 2028:
                            NpcService.gI().createMenuConMeo(pl, 2028, -1, "Nở ra đệ berus vip rồi !! Nhớ tháo đồ nhé!",
                                    "Đệ trái đất", "Đệ namec", "Đệ xayda");
                            break; // case 733:
                        // UseItem.gI().useItem(pl, item, indexBag);;
                        case 2000:
                        case 2001:
                        case 2002:
                            ItemSKH(pl, item);

                    }
                    // Service.gI().sendThongBao(pl, "Trang bị không phù hợp!");
                    break;
            }
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.gI().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    public void UseCard(Player pl, Item item) {
        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(c -> c.Id == item.template.id)
                .findFirst().orElse(null);
        if (radarTemplate == null) {
            return;
        }
        if (radarTemplate.Require != -1) {
            RadarCard radarRequireTemplate = RadarService.gI().RADAR_TEMPLATE.stream()
                    .filter(r -> r.Id == radarTemplate.Require).findFirst().orElse(null);
            if (radarRequireTemplate == null) {
                return;
            }
            Card cardRequire = pl.Cards.stream().filter(r -> r.Id == radarRequireTemplate.Id).findFirst().orElse(null);
            if (cardRequire == null || cardRequire.Level < radarTemplate.RequireLevel) {
                Service.gI().sendThongBao(pl, "Bạn cần sưu tầm " + radarRequireTemplate.Name + " ở cấp độ "
                        + radarTemplate.RequireLevel + " mới có thể sử dụng thẻ này");
                return;
            }
        }
        Card card = pl.Cards.stream().filter(r -> r.Id == item.template.id).findFirst().orElse(null);
        if (card == null) {
            Card newCard = new Card(item.template.id, (byte) 1, radarTemplate.Max, (byte) -1, radarTemplate.Options);
            if (pl.Cards.add(newCard)) {
                RadarService.gI().RadarSetAmount(pl, newCard.Id, newCard.Amount, newCard.MaxAmount);
                RadarService.gI().RadarSetLevel(pl, newCard.Id, newCard.Level);
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {
            if (card.Level >= 2) {
                Service.gI().sendThongBao(pl, "Thẻ này đã đạt cấp tối đa");
                return;
            }
            card.Amount++;
            if (card.Amount >= card.MaxAmount) {
                card.Amount = 0;
                if (card.Level == -1) {
                    card.Level = 1;
                } else {
                    card.Level++;
                }
                Service.gI().point(pl);
            }
            RadarService.gI().RadarSetAmount(pl, card.Id, card.Amount, card.MaxAmount);
            RadarService.gI().RadarSetLevel(pl, card.Id, card.Level);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 994: // vỏ ốc
                break;
            case 995: // cây kem
                break;
            case 996: // cá heo
                break;
            case 997: // con diều
                break;
            case 998: // diều rồng
                break;
            case 999: // mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: // xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: // phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1202: // Hào quang
                if (!player.effectFlagBag.useHaoQuang) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHaoQuang = !player.effectFlagBag.useHaoQuang;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.gI().point(player);
        Service.gI().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeUubPet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeUubPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changePetPic(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeUubPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void openDaBaoVe(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 987, 987 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openSPL(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 441, 442, 443, 444, 445, 446, 447 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openDaNangCap(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 220, 221, 222, 223, 224 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openManhTS(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 1066, 1067, 1068, 1069, 1070 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau1(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 13, 13 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau2(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 60, 60 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau3(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 61, 61 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau4(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 62, 62 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau5(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 63, 63 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau6(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 64, 64 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau7(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 65, 65 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau8(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 352, 352 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau9(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = { 523, 523 };
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = { 190, 381, 382, 383, 384, 385, 381 };
            int[][] gold = { { 100000, 200000 } };
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 1) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "bạn nhận được " + it.Name());
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            // CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemHopQuaTanThu(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = { 19, 20, 21, 22 };
            int[][] gold = { { 10000, 50000 } };
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 382: // bổ huyết
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: // bổ khí
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: // giáp xên
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: // cuồng nộ
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.gI().point(pl);
                break;
            case 385: // ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: // máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1099:// cn
                if (!pl.itemTime.isUseCuongNo) {
                    pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                    pl.itemTime.isUseCuongNo2 = true;
                    Service.gI().point(pl);
                } else {
                    Service.gI().sendThongBao(pl, "Chỉ dùng được 1 loại cuồng nộ");
                }

                break;
            case 1100:// bo huyet
                if (!pl.itemTime.isUseBoHuyet) {
                    pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                    pl.itemTime.isUseBoHuyet2 = true;
                } else {
                    Service.gI().sendThongBao(pl, "Chỉ dùng được 1 loại bổ huyết");
                }
                break;
            case 1101:// bo khi
                if (!pl.itemTime.isUseBoKhi) {
                    pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                    pl.itemTime.isUseBoKhi2 = true;
                } else {
                    Service.gI().sendThongBao(pl, "Chỉ dùng được 1 loại bổ khí");
                }
                break;
            case 1102:// xbh
                if (!pl.itemTime.isUseGiapXen) {
                    pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                    pl.itemTime.isUseGiapXen2 = true;
                }
                else{
                    Service.gI().sendThongBao(pl, "Chỉ dùng được 1 loại giáp xên");
                }
                break;
            case 1103:// an danh
                pl.itemTime.lastTimeAnDanh2 = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh2 = true;
                break;
            case 2037: // máy dò đồ
                pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo2 = true;
                break;
        }
        Service.gI().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                    level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil
                                    .createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.gI().sendThongBao(pl,
                                    "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else if (curSkill.point + 1 == level) {
                        curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                        // System.out.println(curSkill.template.name + " - " + curSkill.point);
                        SkillUtil.setSkill(pl, curSkill);
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        msg = Service.gI().messageSubCommand((byte) 62);
                        msg.writer().writeShort(curSkill.skillId);
                        pl.sendMessage(msg);
                        msg.cleanup();
                    } else {
                        Service.gI().sendThongBao(pl,
                                "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                    }
                    InventoryServiceNew.gI().sendItemBags(pl);
                }
            } else {
                Service.gI().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        } else if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
            pl.pet.fusion(true);
        } else {
            pl.pet.unFusion();
        }
    }

    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 10
                || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
            pl.pet.fusion2(true);
        } else {
            pl.pet.unFusion();
        }
    }

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        Zone zoneChose = pl.mapCapsule.get(index);
        // Kiểm tra số lượng người trong khu

        if (zoneChose.getNumOfPlayers() > 25
                || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapGiaiCuuMiNuong(zoneChose.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)
                || MapService.gI().isMapKhiGaHuyDiet(zoneChose.map.mapId)
                || MapService.gI().isMapConDuongRanDoc(zoneChose.map.mapId)
                || MapService.gI().isMapMaBu(zoneChose.map.mapId)
                || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
            Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
            return;
        }
        if (zoneChose.map.mapId == 196 || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapGiaiCuuMiNuong(zoneChose.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)) {
            Service.gI().sendThongBaoOK(pl, "Map này không thể khứ hồi!!");
            return;
        } else {
            if (index != 0 || zoneChose.map.mapId == 21
                    || zoneChose.map.mapId == 22
                    || zoneChose.map.mapId == 23) {
                pl.mapBeforeCapsule = pl.zone;
            } else {
                zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
                pl.mapBeforeCapsule = null;
            }
            ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
        }
    }

    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 10000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
                player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
                Service.gI().sendInfoPlayerEatPea(player.pet);
                Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }
            // if (player.pet.askPea() && player.pet.typePet == 1 && ((Pet)
            // player).charms.tdMaBu2 > System.currentTimeMillis()) {
            // int statima = 100 * lvPea;
            // player.pet.nPoint.stamina += statima;
            // if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
            // player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
            // }
            // player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
            // player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
            // Service.gI().sendInfoPlayerEatPea(player.pet);
            // Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu
            // thần");
            // }

            InventoryServiceNew.gI().subQuantityItemsBag(player, pea, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: // skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: // skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: // skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: // skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 2058: // skill 5
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 4)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 2059: // skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 5)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 2060: // skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 6)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;

            }

        } catch (Exception e) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void ItemSKH(Player pl, Item item) {// hop qua skh
        if (item.template.id == 2000) {
            NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Set Songoku",
                    "Set Angry Goku", "Set Thên Xin Hăng");
        } else if (item.template.id == 2001) {
            NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Set Kami", "Set Nail",
                    "Set Picolo");
        } else {
            NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Set Vegeta",
                    "Set Cumber", "Set Kakarot");
        }
    }

    private void ItemDHD(Player pl, Item item) {// hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày",
                "Rada", "Từ Chối");
    }

    private void Hopts(Player pl, Item item) {// hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất",
                "Set namec", "Set xayda", "Từ chổi");
    }

    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            int param = item.itemOptions.size();
            int gold = 0;
            int[] listItem = { 441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225 };
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param < 8) {
                gold = 100000 * param;
                listClothesReward = new int[] { randClothes(param) };
                listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param < 10) {
                gold = 250000 * param;
                listClothesReward = new int[] { randClothes(param), randClothes(param) };
                listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                gold = 500000 * param;
                listClothesReward = new int[] { randClothes(param), randClothes(param), randClothes(param) };
                listItemReward = Util.pickNRandInArr(listItem, 5);
                int ruby = Util.nextInt(1, 5);
                pl.inventory.ruby += ruby;
                pl.textRuongGo.add(text + "|1| " + ruby + " Hồng Ngọc");
            }
            for (int i : listClothesReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type,
                        itemReward.itemOptions);
                RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[] {
                        new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3),
                        new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5), });
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            for (int i : listItemReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
                itemReward.quantity = Util.nextInt(1, 5);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            if (param == 11) {
                itemReward = ItemService.gI().createNewItem((short) 0);
                itemReward.quantity = Util.nextInt(1, 3);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                    "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + pl.textRuongGo.size() + "]");
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.addGold(gold);
            InventoryServiceNew.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vui lòng đợi 24h");
        }
    }

    private int randClothes(int level) {
        return LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    public static final int[][][] LIST_ITEM_CLOTHES = {
            // áo , quần , găng ,giày,rada
            // td -> nm -> xd
            { { 0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555 },
                    { 6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556 },
                    { 21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562 },
                    { 27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563 },
                    { 12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561 } },
            { { 1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557 },
                    { 7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558 },
                    { 22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261, 564 },
                    { 28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565 },
                    { 12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561 } },
            { { 2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559 },
                    { 8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560 },
                    { 23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566 },
                    { 29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567 },
                    { 12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561 } }
    };

}
