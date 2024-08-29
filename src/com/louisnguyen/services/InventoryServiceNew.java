package com.louisnguyen.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.girlkun.network.io.Message;
import com.louisnguyen.MaQuaTang.MaQuaTang;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.TranhNgocSaoDen.BlackBallWar;
import com.louisnguyen.models.player.Inventory;
import com.louisnguyen.models.player.Pet;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.Util;

public class InventoryServiceNew {

    private static InventoryServiceNew I;

    public static InventoryServiceNew gI() {
        if (InventoryServiceNew.I == null) {
            InventoryServiceNew.I = new InventoryServiceNew();
        }
        return InventoryServiceNew.I;
    }

    // GIFT CODE
    public void addItemGiftCodeToPlayer(Player p, MaQuaTang giftcode) {
        Set<Integer> keySet = giftcode.detail.keySet();
        Service.gI().sendThongBao(p, "Bạn vừa nhập GiftCode\nthành công.");
        for (Integer key : keySet) {
            int idItem = key;
            int quantity = giftcode.detail.get(key);

            if (idItem == -1) {
                p.inventory.gold = Math.min(p.inventory.gold + (long) quantity, 2000000000L);
                // textGift += quantity + " vàng\b";
                // Service.gI().sendThongBao(p, "Bạn nhận được X" + quantity + " vàng");
            } else if (idItem == -2) {
                p.inventory.gem = Math.min(p.inventory.gem + quantity, 200000000);
                // textGift += quantity + " ngọc\b";
            } else if (idItem == -3) {
                p.inventory.ruby = Math.min(p.inventory.ruby + quantity, 200000000);
                // textGift += quantity + " ngọc khóa\b";
            } else if (idItem == 251003) {
                if (p.lastTimeTitle2 == 0) {
                    p.lastTimeTitle2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);
                } else {
                    p.lastTimeTitle2 += (1000 * 60 * 60 * 24 * 3);
                }
                p.isTitleUse2 = true;
                Service.gI().point(p);
                Service.gI().sendTitle(p, 889);
                // InventoryServiceNew.gI().subQuantityItemsBag(p, item, 1);
                Service.gI().sendThongBao(p, "Bạn nhận được 3 ngày danh hiệu Fan cứng!");
                break;
            } else {

                Item itemGiftTemplate = ItemService.gI().createNewItem((short) idItem);
                if (itemGiftTemplate != null) {
                    Item itemGift = new Item((short) idItem);

                    itemGift.itemOptions = giftcode.option;
                    itemGift.quantity = quantity;
                    addItemBag(p, itemGift);

                    Service.gI().sendThongBao(p, "Bạn nhận được X" + quantity + " " + itemGift.template.name);

                    // textGift += "x" + quantity + " " + itemGift.template.name + "\b";
                }
            }
        }

        sendItemBags(p);

    }

    private void __________________Tìm_kiếm_item_____________________________() {
        // **********************************************************************
    }

    public Item findItem(List<Item> list, int tempId) {
        try {
            for (Item item : list) {
                if (item.isNotNullItem() && item.template.id == tempId) {
                    return item;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Item findItemBody(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBody, tempId);
    }

    public Item findItemBag(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBag, tempId);
    }

    public Item findItemBox(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBox, tempId);
    }

    public boolean isExistItem(List<Item> list, int tempId) {
        try {
            this.findItem(list, tempId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExistItemBody(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBody, tempId);
    }

    public boolean isExistItemBag(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBag, tempId);
    }

    public boolean isExistItemBox(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBox, tempId);
    }

    private void __________________Sao_chép_danh_sách_item__________________() {
        // **********************************************************************
    }

    public List<Item> copyList(List<Item> items) {
        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            list.add(ItemService.gI().copyItem(item));
        }
        return list;
    }

    public List<Item> copyItemsBody(Player player) {
        return copyList(player.inventory.itemsBody);
    }

    public List<Item> copyItemsBag(Player player) {
        return copyList(player.inventory.itemsBag);
    }

    public List<Item> copyItemsBox(Player player) {
        return copyList(player.inventory.itemsBox);
    }

    private void __________________Vứt_bỏ_item______________________________() {
        // **********************************************************************
    }

    public void throwItem(Player player, int where, int index) {
        Item itemThrow = null;
        if (where == 0) {
            itemThrow = player.inventory.itemsBody.get(index);
            removeItemBody(player, index);
            sendItemBody(player);
            Service.gI().Send_Caitrang(player);
        } else if (where == 1) {
            itemThrow = player.inventory.itemsBag.get(index);
            if (itemThrow.template.id != 530 && itemThrow.template.id != 531 && itemThrow.template.id != 534
                    && itemThrow.template.id != 535 && itemThrow.template.id != 536) {
                removeItemBag(player, index);
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Không thể vứt bỏ vật phẩm này");
            }
        }
        if (itemThrow == null) {
            return;
        }
    }

    private void __________________Xoá_bỏ_item______________________________() {
        // **********************************************************************
    }

    public void removeItem(List<Item> items, int index) {
        Item item = ItemService.gI().createItemNull();
        items.set(index, item);
    }

    public void removeItem(List<Item> items, Item item) {
        if (item == null) {
            return;
        }
        Item it = ItemService.gI().createItemNull();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                items.set(i, it);
                item.dispose();
                break;
            }
        }
    }

    public void removeItemBag(Player player, int index) {
        this.removeItem(player.inventory.itemsBag, index);
    }

    public void removeItemBag(Player player, Item item) {
        this.removeItem(player.inventory.itemsBag, item);
    }

    public void removeItemBody(Player player, int index) {
        this.removeItem(player.inventory.itemsBody, index);
    }

    public void removeItemPetBody(Player player, int index) {
        this.removeItemBody(player.pet, index);
    }

    public void removeItemBox(Player player, int index) {
        this.removeItem(player.inventory.itemsBox, index);
    }

    private void __________________Giảm_số_lượng_item_______________________() {
        // **********************************************************************
    }

    public void subQuantityItemsBag(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBag, item, quantity);
    }

    public void subQuantityItemsBody(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBody, item, quantity);
    }

    public void subQuantityItemsBox(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBox, item, quantity);
    }

    public void subQuantityItem(List<Item> items, Item item, int quantity) {
        if (item != null) {
            for (Item it : items) {
                if (item.equals(it)) {
                    it.quantity -= quantity;
                    if (it.quantity <= 0) {
                        this.removeItem(items, item);
                    }
                    break;
                }
            }
        }
    }

    private void __________________Sắp_xếp_danh_sách_item___________________() {
        // **********************************************************************
    }

    public void sortItems(List<Item> list) {
        int first = -1;
        int last = -1;
        Item tempFirst = null;
        Item tempLast = null;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isNotNullItem()) {
                first = i;
                tempFirst = list.get(i);
                break;
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).isNotNullItem()) {
                last = i;
                tempLast = list.get(i);
                break;
            }
        }
        if (first != -1 && last != -1 && first < last) {
            list.set(first, tempLast);
            list.set(last, tempFirst);
            sortItems(list);
        }
    }

    private void __________________Thao_tác_tháo_mặc_item___________________() {
        // **********************************************************************
    }

    private Item putItemBag(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
            if (!player.inventory.itemsBag.get(i).isNotNullItem()) {
                player.inventory.itemsBag.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBox(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBox.size(); i++) {
            if (!player.inventory.itemsBox.get(i).isNotNullItem()) {
                player.inventory.itemsBox.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBody(Player player, Item item) {
        Item sItem = item;
        if (!item.isNotNullItem()) {
            return sItem;
        }
        if (player.isPl() && !player.isPet) {
            switch (item.template.type) {
                case 0: // áo
                case 1: // quần
                case 2: // găng
                case 3: // giầy
                case 4: // rada
                case 5: // Cải trang
                case 32: // giap luyen tap
                case 11: // vật phẩm đeo lưng
                case 27: // Pet
                case 72: // linh thú
                case 23: // Ván bay
                    break;
                default:
                    Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                            "Trang bị không phù hợp!");
                    return sItem;
            }
        } else if (!player.isPl() && player.isPet) {
            switch (item.template.type) {
                case 0: // áo
                case 1: // quần
                case 2: // găng
                case 3: // giầy
                case 4: // rada
                case 5: // Cải trang
                case 32: // giap luyen tap
                case 11: // Vật phẩm đeo lưng
                case 23:// van bay
                    break;
                default:
                    Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                            "Trang bị không phù hợp!");
                    return sItem;
            }
        }
        if (item.template.gender < 3 && item.template.gender != player.gender) {
            Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
            return sItem;
        }
        long powerRequire = item.template.strRequire;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 21) {
                powerRequire = io.param * 1000000000L;
                break;
            }
        }
        if (player.nPoint.power < powerRequire) {
            Service.gI().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Sức mạnh không đủ yêu cầu!");
            return sItem;
        }
        int index = -1;
        if (player.isPl() && !player.isPet) {
            switch (item.template.type) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    index = item.template.type;
                    break;
                case 32:
                    index = 6;
                    break;
                case 11:
                    index = 7;
                    break;
                case 27:
                    index = 8;
                    break;
                case 72:
                    index = 9;
                    break;
                case 23:
                    index = 10;
                    break;
            }
        } else if (!player.isPl() && player.isPet) {
            switch (item.template.type) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    index = item.template.type;
                    break;
                case 32:
                    index = 6;
                    break;
                case 11:
                    index = 7;
                    break;
                case 23:
                    index = 8;
                    break;
            }
        }
        sItem = player.inventory.itemsBody.get(index);
        player.inventory.itemsBody.set(index, item);
        return sItem;
    }

    public void itemBagToBody(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBag.set(index, putItemBody(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.gI().sendFlagBag(player);
            Service.gI().Send_Caitrang(player);
            Service.gI().point(player);
        } else {
            Service.gI().sendThongBao(player, "Vật phẩm không thể mặc");
            return;
        }
    }

    public void itemBodyToBag(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            if (index == 8) { // pet nếu là pet thì thêm pet
                // Service.getInstance().sendPetFollow(player, 1,
                //         (short) (player.inventory.itemsBody.get(8).template.iconID + 1), (byte) 1, (short) 32,
                //         (short) 30, player.session, new byte[] { 0, 1, 2 });
            }
            if (index == 8) {
                if (player.newpet != null) {
                    ChangeMapService.gI().exitMap(player.newpet);
                    player.newpet.dispose();
                    player.newpet = null;
                    player.isNewPet = false; // Add this line to reset the isNewPet flag
                }
            }

            // if (index == 9) {
                // Service.getInstance().sendPetFollow(player, 2,
                //         (short) (player.inventory.itemsBody.get(8).template.iconID ), (byte) 1, (short) 32,
                //         (short) 30, player.session, new byte[] { 0, 1, 2 });
            // }
            if (index == 9) {
                // if (player.newpet != null) {
                //     ChangeMapService.gI().exitMap(player.newpet);
                //     player.newpet.dispose();
                //     player.newpet = null;
                //     player.isNewPet = false; // Add this line to reset the isNewPet flag
                // }
            }
            player.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().player(player);
            player.zone.load_Me_To_Another(player);
            player.zone.load_Another_To_Me(player);
            Service.getInstance().sendFlagBag(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
        }
    }

    public void itemBagToPetBody(Player player, int index) {
        if (player.pet != null && player.pet.nPoint.power >= 1500000) {
            Item item = player.inventory.itemsBag.get(index);
            if (item.isNotNullItem()) {
                Item itemSwap = putItemBody(player.pet, item);
                player.inventory.itemsBag.set(index, itemSwap);
                sendItemBags(player);
                sendItemBody(player);
                Service.gI().Send_Caitrang(player.pet);
                Service.gI().Send_Caitrang(player);
                if (!itemSwap.equals(item)) {
                    Service.gI().point(player);
                    Service.gI().showInfoPet(player);
                }
            } else {
                return;
            }
        } else {
            Service.gI().sendThongBaoOK(player, "Đệ tử phải đạt 1tr5 sức mạnh mới có thể mặc");
        }
    }

    public void itemPetBodyToBag(Player player, int index) {
        Item item = player.pet.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.pet.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.gI().Send_Caitrang(player.pet);
            Service.gI().Send_Caitrang(player);
            Service.gI().point(player);
            Service.gI().showInfoPet(player);
        }
    }

    public void itemBoxToBodyOrBag(Player player, int index) {
        Item item = player.inventory.itemsBox.get(index);
        if (item.isNotNullItem()) {
            boolean done = false;
            if (item.template.type >= 0 && item.template.type <= 5 || item.template.type == 32) {
                Item itemBody = player.inventory.itemsBody.get(item.template.type == 32 ? 6 : item.template.type);
                if (!itemBody.isNotNullItem()) {
                    if (item.template.gender == player.gender || item.template.gender == 3) {
                        long powerRequire = item.template.strRequire;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 21) {
                                powerRequire = io.param * 1000000000L;
                                break;
                            }
                        }
                        if (powerRequire <= player.nPoint.power) {
                            player.inventory.itemsBody.set(item.template.type == 32 ? 6 : item.template.type, item);
                            player.inventory.itemsBox.set(index, itemBody);
                            done = true;

                            sendItemBody(player);
                            Service.gI().Send_Caitrang(player);
                            Service.gI().point(player);
                        }
                    }
                }
            }
            if (!done) {
                if (addItemBag(player, item)) {
                    if (item.quantity == 0) {
                        Item sItem = ItemService.gI().createItemNull();
                        player.inventory.itemsBox.set(index, sItem);
                    }
                    sendItemBags(player);
                }
            }
            sendItemBox(player);
        }
    }

    public void itemBagToBox(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (item != null) {
            if (item.template.id == 1998 || item.template.id == 674) {
                Service.gI().sendThongBao(player, "Không thể cất vàng vào rương");
                return;
            }
            if (addItemBox(player, item)) {
                if (item.quantity == 0) {
                    Item sItem = ItemService.gI().createItemNull();
                    player.inventory.itemsBag.set(index, sItem);
                }
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
                sendItemBox(player);
            }
        }
    }

    public void itemBodyToBox(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBody.set(index, putItemBox(player, item));
            sortItems(player.inventory.itemsBag);
            sendItemBody(player);
            sendItemBox(player);
            Service.gI().Send_Caitrang(player);
            sendItemBody(player);
            Service.gI().point(player);
        }
    }

    private void __________________Gửi_danh_sách_item_cho_người_chơi________() {
        // **********************************************************************
    }

    public void sendItemBags(Player player) {
        sortItems(player.inventory.itemsBag);
        Message msg;
        try {
            msg = new Message(-36);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBag.size());
            for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                Item item = player.inventory.itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    continue;
                }
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                msg.writer().writeByte(item.itemOptions.size()); // options
                for (int j = 0; j < item.itemOptions.size(); j++) {
                    msg.writer().writeByte(item.itemOptions.get(j).optionTemplate.id);
                    msg.writer().writeShort(item.itemOptions.get(j).param);
                }
            }

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemBody(Player player) {
        Message msg;
        try {
            msg = new Message(-37);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeByte(player.inventory.itemsBody.size());
            for (Item item : player.inventory.itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<Item.ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (Item.ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        Service.gI().Send_Caitrang(player);
    }

    public void sendItemBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBox.size());
            for (Item it : player.inventory.itemsBox) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    msg.writer().writeByte(it.itemOptions.size());
                    for (Item.ItemOption io : it.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        this.openBox(player);
    }

    public void openBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void __________________Thêm_vật_phẩm_vào_danh_sách______________() {
        // **********************************************************************
    }

    private boolean addItemSpecial(Player player, Item item) {
        // bùa
        if (item.template.type == 13) {
            int min = 0;
            try {
                String tagShopBua = player.iDMark.getShopOpen().tagName;
                if (tagShopBua.equals("BUA_1H")) {
                    min = 60;
                } else if (tagShopBua.equals("BUA_8H")) {
                    min = 60 * 8;
                } else if (tagShopBua.equals("BUA_1M")) {
                    min = 60 * 24 * 30;
                } else if (tagShopBua.equals("BUA_MABU")) {
                    min = 60;
                }
            } catch (Exception e) {
            }
            player.charms.addTimeCharms(item.template.id, min);
            return true;
        }

        switch (item.template.id) {
            // case 568: // quả trứng
            //     if (player.mabuEgg == null) {
            //         MabuEgg.createMabuEgg(player);
            //     }
            //     return true;
            case 1132: // cs bang
                player.clan.capsuleClan += 1;
                player.clan.update();
                return true;
            case 861: // hồng ngọc
                player.inventory.ruby += 1;
                Service.gI().sendMoney(player);
                return true;
            // case 2027: // 
            //     if (player.billEgg == null) {
            //         BillEgg.createBillEgg(player);
            //     }
            //     return true;
            case 453: // tàu tennis
                player.haveTennisSpaceShip = true;
                return true;
            case 74: // đùi gà nướng
                player.nPoint.setFullHpMp();
                PlayerService.gI().sendInfoHpMp(player);
                return true;
            case 191: // cà chua
                player.nPoint.setFullHp();
                PlayerService.gI().sendInfoHp(player);
                return true;
            case 192: // cà rốt
                player.nPoint.setFullMp();
                PlayerService.gI().sendInfoMp(player);
                return true;
        }
        return false;
    }

    public boolean addItemBag(Player player, Item item) {
        if (item.template.id >= 650 && item.template.id <= 662 && player.zone.map.mapId == 48) { // ITEM HUY DIET
            Item ThucAn = player.inventory.itemsBag.stream()
                    .filter(itemm -> itemm.isNotNullItem() && itemm.isThucAn() && itemm.quantity >= 99).findFirst()
                    .get();
            if (ThucAn.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                    Service.gI().sendThongBao(player, "Hành trang của bạn không đủ chỗ trống");
                    return false;
                } else {
                    int bonusHuyDiet = Util.nextInt(0, 16);
                    if (item.template.type == (byte) 0) { // AO HUY DIET
                        Item _item = new Item(item);
                        _item.quantity += (item.quantity - 1);
                        if (bonusHuyDiet > -1) {
                            for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                if (_item.itemOptions.get(ix).optionTemplate.id == 47) {
                                    item.itemOptions.get(
                                            ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    item.itemOptions.add(new Item.ItemOption(21, 40));
                                    item.itemOptions.add(new Item.ItemOption(30, 0));
                                }
                            }
                            InventoryServiceNew.gI().subQuantityItemsBag(player, ThucAn, 99);
                        }
                    } else if (item.template.type == (byte) 1) { // QUAN HUY DIET
                        Item _item = new Item(item);
                        _item.quantity += (item.quantity - 1);
                        if (bonusHuyDiet > -1) {
                            for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                if (_item.itemOptions.get(ix).optionTemplate.id == 22) {
                                    item.itemOptions.get(
                                            ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    item.itemOptions.add(new Item.ItemOption(21, 40));
                                    item.itemOptions.add(new Item.ItemOption(30, 0));
                                }
                                if (_item.itemOptions.get(ix).optionTemplate.id == 27) {
                                    item.itemOptions.get(
                                            ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                }
                            }
                            InventoryServiceNew.gI().subQuantityItemsBag(player, ThucAn, 99);
                        }
                    } else if (item.template.type == (byte) 2) { // GANG HUY DIET
                        Item _item = new Item(item);
                        _item.quantity += (item.quantity - 1);
                        if (bonusHuyDiet > -1) {
                            for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                if (_item.itemOptions.get(ix).optionTemplate.id == 0) {
                                    item.itemOptions.get(
                                            ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    item.itemOptions.add(new Item.ItemOption(21, 40));
                                    item.itemOptions.add(new Item.ItemOption(30, 0));
                                }
                            }
                        }
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ThucAn, 99);
                    } else if (item.template.type == (byte) 3) { // GIAY HUY DIET
                        Item _item = new Item(item);
                        _item.quantity += (item.quantity - 1);
                        if (bonusHuyDiet > -1) {
                            for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                if (_item.itemOptions.get(ix).optionTemplate.id == 23) {
                                    item.itemOptions.get(
                                            ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                }
                                if (_item.itemOptions.get(ix).optionTemplate.id == 28) {
                                    item.itemOptions.get(
                                            ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    item.itemOptions.add(new Item.ItemOption(21, 40));
                                    item.itemOptions.add(new Item.ItemOption(30, 0));
                                }
                            }
                        }
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ThucAn, 99);
                    } else if (item.template.type == (byte) 4) { // NHAN HUY DIET
                        Item _item = new Item(item);
                        _item.quantity += (item.quantity - 1);
                        if (bonusHuyDiet > -1) {
                            for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                if (_item.itemOptions.get(ix).optionTemplate.id == 14) {
                                    item.itemOptions.get(
                                            ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    item.itemOptions.add(new Item.ItemOption(21, 40));
                                    item.itemOptions.add(new Item.ItemOption(30, 0));
                                }
                            }
                            InventoryServiceNew.gI().subQuantityItemsBag(player, ThucAn, 99);
                        }
                    }
                }
            } else {
                Service.gI().sendThongBao(player, "Không đủ thức ăn để đổi đồ!");
                return false;
            }
        }
        // ngọc rồng đen
        if (ItemMapService.gI().isBlackBall(item.template.id)) {
            return BlackBallWar.gI().pickBlackBall(player, item);
        }
        if (addItemSpecial(player, item)) {
            return true;
        }

        // gold, gem, ruby
        switch (item.template.type) {
            case 9:
                if (player.inventory.gold + item.quantity > Inventory.LIMIT_GOLD) {
                    player.inventory.gold += item.quantity;
                    Service.gI().sendMoney(player);
                }
            case 10:
                player.inventory.gem += item.quantity;
                Service.gI().sendMoney(player);
                return true;
            case 34:
                player.inventory.ruby += item.quantity;
                Service.gI().sendMoney(player);
                return true;
        }

        // mở rộng hành trang - rương đồ
        if (item.template.id == 517) {
            if (player.inventory.itemsBag.size() < Inventory.MAX_ITEMS_BAG) {
                player.inventory.itemsBag.add(ItemService.gI().createItemNull());
                Service.gI().sendThongBaoOK(player, "Hành trang của bạn đã được mở rộng thêm 1 ô");
                return true;
            } else {
                Service.gI().sendThongBaoOK(player, "Hành trang của bạn đã đạt tối đa");
                return false;
            }
        } else if (item.template.id == 518) {
            if (player.inventory.itemsBox.size() < Inventory.MAX_ITEMS_BOX) {
                player.inventory.itemsBox.add(ItemService.gI().createItemNull());
                Service.gI().sendThongBaoOK(player, "Rương đồ của bạn đã được mở rộng thêm 1 ô");
                return true;
            } else {
                Service.gI().sendThongBaoOK(player, "Rương đồ của bạn đã đạt tối đa");
                return false;
            }
        }
        return addItemList(player.inventory.itemsBag, item);
    }

    public boolean addItemBox(Player player, Item item) {
        return addItemList(player.inventory.itemsBox, item);
    }

    public boolean addItemList(List<Item> items, Item itemAdd) {
        // nếu item ko có option, add option rỗng vào
        if (itemAdd.itemOptions.isEmpty()) {
            itemAdd.itemOptions.add(new Item.ItemOption(73, 0));
        }

        // item cộng thêm chỉ số param: tự động luyện tập
        int[] idParam = isItemIncrementalOption(itemAdd);
        if (idParam[0] != -1) {
            for (Item it : items) {
                if (it.isNotNullItem() && it.template.id == itemAdd.template.id) {
                    for (Item.ItemOption io : it.itemOptions) {
                        if (io.optionTemplate.id == idParam[0]) {
                            io.param += idParam[1];
                        }
                    }
                    return true;
                }
            }
        }

        // item tăng số lượng
        if (itemAdd.template.isUpToUp) {
            for (Item it : items) {
                if (!it.isNotNullItem() || it.template.id != itemAdd.template.id) {
                    continue;
                }
                if (itemAdd.template.id == 457
                        || itemAdd.template.type == 14
                        || itemAdd.template.type == 15
                        || itemAdd.template.type == 16
                        || itemAdd.template.type == 17
                        || itemAdd.template.type == 18
                        || itemAdd.template.type == 19
                        || itemAdd.template.type == 20
                        || itemAdd.template.type == 193
                        || itemAdd.template.type == 211
                        || itemAdd.template.type == 212
                        || itemAdd.template.type == 220
                        || itemAdd.template.type == 221
                        || itemAdd.template.type == 222
                        || itemAdd.template.type == 223
                        || itemAdd.template.type == 224
                        || itemAdd.template.type == 225
                        || itemAdd.template.type == 441
                        || itemAdd.template.type == 442
                        || itemAdd.template.type == 443
                        || itemAdd.template.type == 444
                        || itemAdd.template.type == 445
                        || itemAdd.template.type == 446
                        || itemAdd.template.type == 447
                        || itemAdd.template.id == 590
                        // Thức ăn
                        || itemAdd.template.type == 663
                        || itemAdd.template.type == 664
                        || itemAdd.template.type == 665
                        || itemAdd.template.type == 666
                        || itemAdd.template.type == 667
                        // Mảnh bông tai
                        || itemAdd.template.id == 933
                        || itemAdd.template.id == 934
                        // Thiên Sứ
                        || itemAdd.template.type == 1066
                        || itemAdd.template.type == 1067
                        || itemAdd.template.type == 1068
                        || itemAdd.template.type == 1069
                        || itemAdd.template.type == 1070
                        || itemAdd.template.type == 1071
                        || itemAdd.template.type == 1072
                        || itemAdd.template.type == 1073
                        || itemAdd.template.type == 1074
                        || itemAdd.template.type == 1075
                        || itemAdd.template.type == 1076
                        || itemAdd.template.type == 1077
                        || itemAdd.template.type == 1078
                        || itemAdd.template.type == 1079
                        || itemAdd.template.type == 1080
                        || itemAdd.template.type == 1081
                        || itemAdd.template.type == 1082
                        || itemAdd.template.type == 1083
                        || itemAdd.template.type == 1084
                        || itemAdd.template.type == 1085
                        || itemAdd.template.type == 1086) {
                    it.quantity += itemAdd.quantity;
                    itemAdd.quantity = 0;
                    return true;
                }

                if (it.quantity < 99) {
                    int add = 99 - it.quantity;
                    if (itemAdd.quantity <= add) {
                        it.quantity += itemAdd.quantity;
                        itemAdd.quantity = 0;
                        return true;
                    } else {
                        it.quantity = 99;
                        itemAdd.quantity -= add;
                    }
                }
            }
        }

        // add item vào ô mới
        if (itemAdd.quantity > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (!items.get(i).isNotNullItem()) {
                    items.set(i, ItemService.gI().copyItem(itemAdd));
                    itemAdd.quantity = 0;
                    return true;
                }
            }
        }
        return false;
    }

    private void __________________Kiểm_tra_điều_kiện_vật_phẩm______________() {
        // **********************************************************************
    }

    /**
     * Kiểm tra vật phẩm có phải là vật phẩm tăng chỉ số option hay không
     *
     * @param item
     * @return id option tăng chỉ số - param
     */
    private int[] isItemIncrementalOption(Item item) {
        for (Item.ItemOption io : item.itemOptions) {
            switch (io.optionTemplate.id) {
                case 1:
                    return new int[] { io.optionTemplate.id, io.param };
            }
        }
        return new int[] { -1, -1 };
    }

    private void __________________Kiểm_tra_danh_sách_còn_chỗ_trống_________() {
        // **********************************************************************
    }

    public byte getCountEmptyBag(Player player) {
        return getCountEmptyListItem(player.inventory.itemsBag);
    }

    public byte getCountEmptyListItem(List<Item> list) {
        byte count = 0;
        for (Item item : list) {
            if (!item.isNotNullItem()) {
                count++;
            }
        }
        return count;
    }

    public byte getIndexBag(Player pl, Item it) {
        for (byte i = 0; i < pl.inventory.itemsBag.size(); ++i) {
            Item item = pl.inventory.itemsBag.get(i);
            if (item != null && it.equals(item)) {
                return i;
            }
        }
        return -1;
    }

    public boolean finditemWoodChest(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        for (Item item : player.inventory.itemsBox) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        return true;
    }
}
