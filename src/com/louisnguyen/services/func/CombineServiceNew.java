package com.louisnguyen.services.func;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import com.girlkun.network.io.Message;
import com.louisnguyen.consts.ConstNpc;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.item.Item.ItemOption;
import com.louisnguyen.models.npc.Npc;
import com.louisnguyen.models.npc.NpcManager;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.server.Manager;
import com.louisnguyen.server.ServerNotify;
import com.louisnguyen.services.InventoryServiceNew;
import com.louisnguyen.services.ItemService;
import com.louisnguyen.services.Service;
import com.louisnguyen.utils.Util;

public class CombineServiceNew {

    private static final byte MAX_STAR_ITEM = 8;
    private static final byte MAX_LEVEL_ITEM = 8;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int PHA_LE_HOA_TRANG_BI_5_LAN = 5011;
    public static final int PHA_LE_HOA_TRANG_BI_10_LAN = 5012;
    public static final int PHA_LE_HOA_TRANG_BI_100_LAN = 5013;

    public static final int PHAP_SU_HOA_TRANG_BI = 503;
    public static final int CHUYEN_HOA_TRANG_BI = 502;

    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int MO_CHI_SO_BONG_TAI = 519;
    public static final int CHE_TAO_TRANG_BI_TS = 520;
    public static final int PS_HOA_TRANG_BI = 2150;
    public static final int TAY_PS_HOA_TRANG_BI = 2151;
    public static final int GIA_HAN_VAT_PHAM = 2152;

    public static final int NANG_CAP_DO_TL = 521;
    public static final int NANG_CAP_DO_HD = 522;
    public static final int NANG_CAP_3_DO_HD = 523;

    public static final int THANG_CAP_NGOC_BOI = 524;

    public static final int THANG_HOA_NGOC_BOI = 525;

    public static final int NHAP_NGOC_RONG = 513;

    public static final int REN_KIEM_Z = 517;

    private static final int GOLD_BONG_TAI = 2_000_000;
    private static final int GOLD_KIEM_Z = 200_000_000;
    private static final int GEM_BONG_TAI = 10;
    private static final int GEM_KIEM_Z = 1_000;
    private static final int RATIO_BONG_TAI = 50;
    private static final int RATIO_NANG_CAP = 50;
    private static final int RATIO_KIEM_Z2 = 40;

    private final Npc baHatMit;
    private final Npc thoNgoc;
    private final Npc tosukaio;
    private final Npc whis;

    private static CombineServiceNew i;

    public CombineServiceNew(com.louisnguyen.models.npc.Npc whis) {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.tosukaio = NpcManager.getNpc(ConstNpc.TO_SU_KAIO);
        this.whis = NpcManager.getNpc(ConstNpc.WHIS);
        this.thoNgoc = NpcManager.getNpc(ConstNpc.THO_NGOC);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew(null);
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type   kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 454) {
                            bongTai = item;
                        } else if (item.template.id == 933) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "|2|Bông tai Porata[+2]" + "\n";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|2|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        npcSay +="|2|Cần 99 Mảnh vỡ bông tai \n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng\n"
                                    + "|2|Cần" + player.combineNew.gemCombine + "ngọc\n"
                                    + "|7|Thất bại -99 mảnh vỡ bông tai \n";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp " + Util.numberToMoney(player.combineNew.goldCombine) + "vàng\n"
                                            + player.combineNew.gemCombine + " ngọc\n");
                        } else {
                            npcSay += "Còn thiếu "
                                    + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                    + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần Bông tai Porata và X99 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần Bông tai Porata và X99 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case TAY_PS_HOA_TRANG_BI:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item daHacHoa = null;
                        Item itemHacHoa = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            if (item_.template.id == 1161) {
                                daHacHoa = item_;
                            } else if (item_.isTrangBiHacHoa()) {
                                itemHacHoa = item_;
                            }
                        }
                        if (daHacHoa == null) {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Cần 1 trang bị đã Pháp Sư và 1 Bùa giải Pháp sư");
                            return;
                        }
                        if (itemHacHoa == null) {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Cần 1 trang bị đã Pháp Sư và 1 Bùa giải Pháp sư");
                            return;
                        }

                        String npcSay = "Trang bị được Giải pháp sư \"" + itemHacHoa.Name() + "\"\n"
                                + "|0|Sau khi Giải pháp sư: Về trang bị thường"
                                + "\nTỉ lệ thành công: 100%\n"
                                + "|2|Cần 0 vàng";

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Nâng cấp", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần có trang bị Pháp sư hóa");
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Không đủ hành trang");
                }

                break;
            case PS_HOA_TRANG_BI:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item daHacHoa = null;
                        Item itemHacHoa = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            if (item_.template.id == 1151) {
                                daHacHoa = item_;
                            } else if (item_.isTrangBiHacHoa()) {
                                itemHacHoa = item_;
                            }
                        }
                        if (daHacHoa == null) {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Cần 1 ngọc Pháp sư và 1 trang bị có thể Pháp sư");
                            return;
                        }
                        if (itemHacHoa == null) {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Cần 1 ngọc Pháp sư và 1 trang bị có thể Pháp sư");
                            return;
                        }
                        if (itemHacHoa != null) {
                            for (ItemOption itopt : itemHacHoa.itemOptions) {
                                if (itopt.optionTemplate.id == 223) {
                                    if (itopt.param >= 8) {
                                        Service.getInstance().sendThongBaoOK(player,
                                                "Trang bị đã Pháp Sư Hóa tới mức tối đa");
                                        return;
                                    }
                                }
                            }
                        }
                        String npcSay = "Trang bị được pháp sư hóa \"" + itemHacHoa.template.name + "\"";
                        //
                        npcSay += "\n|0|Sau khi Nâng cấp: +3% chỉ số pháp sư\n";

                        npcSay += "|2|Tỉ lệ thành công: 100%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "Cần " + player.combineNew.goldCombine + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp", "Từ chối");
                        } else {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Không đủ vàng để thực hiện");
                        }
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần 1 đá Pháp sư và 1 trang bị có thể Pháp sư");
                    }
                }
                break;

            case GIA_HAN_VAT_PHAM:

                if (player.combineNew.itemsCombine.size() == 2) {
                    Item daHacHoa = null;
                    Item itemHacHoa = null;
                    for (Item item_ : player.combineNew.itemsCombine) {
                        if (item_.template.id == 1160) {
                            daHacHoa = item_;
                        } else if (item_.isTrangBiHSD()) {
                            itemHacHoa = item_;
                        }
                    }
                    if (daHacHoa == null) {
                        Service.getInstance().sendThongBaoOK(player,
                                "Cần 1 trang bị có hạn sử dụng và 1 phiếu Gia hạn");
                        return;
                    }
                    if (itemHacHoa == null) {
                        Service.getInstance().sendThongBaoOK(player,
                                "Cần 1 trang bị có hạn sử dụng và 1 phiếu Gia hạn");
                        return;
                    }
                    for (ItemOption itopt : itemHacHoa.itemOptions) {
                        if (itopt.optionTemplate.id == 93) {
                            if (itopt.param < 0 && itopt == null) {
                                Service.getInstance().sendThongBaoOK(player,
                                        "Trang bị này không phải trang bị có Hạn Sử Dụng");
                                return;
                            }
                        }
                    }

                    String npcSay = "Trang bị được gia hạn \"" + itemHacHoa.template.name + "\"";

                    npcSay += "\n|0|Sau khi gia hạn +1 ngày\n";

                    npcSay += "|0|Tỉ lệ thành công: 100%" + "\n";
                    if (player.inventory.gold > 200000000) {
                        npcSay += "|2|Cần 200Tr vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp", "Từ chối");

                    } else if (player.inventory.gold < 200000000) {
                        int SoVangThieu2 = (int) (200000000 - player.inventory.gold);
                        Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu " + SoVangThieu2 + " vàng");
                    } else {
                        Service.getInstance().sendThongBaoOK(player,
                                "Cần 1 trang bị có hạn sử dụng và 1 phiếu Gia hạn");
                    }
                } else {

                    Service.getInstance().sendThongBaoOK(player, "Hành trang cần ít nhất 1 chỗ trống");
                }
                break;

            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhHon = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } 
                    }
                    if (bongTai != null && manhHon != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "|2|Bông tai Porata cấp[+2]" + "\n";

                        npcSay += "|2|Tỉ lệ thành công: 50%\n"
                                + "|2|Cần 99 Mảnh hồn bông tai\n"
                                + "|2|Cần 1 Đá xanh lam\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng\n"
                                    + "|2|Cần " + player.combineNew.gemCombine + " ngọc\n"
                                    + "|1|+1 Chỉ số ngẫu nhiên\n";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng\n"
                                            + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu "
                                    + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                    + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần Bông tai Porata cấp 2 và X99 Mảnh hồn bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần Bông tai Porata cấp 2 và X99 Mảnh hồn bông tai", "Đóng");
                }
                break;
            case REN_KIEM_Z:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item manhKiemZ = null;
                    Item quangKiemZ = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 865 || item.template.id == 1200) {
                            manhKiemZ = item;
                        } else if (item.template.id == 1201) {
                            quangKiemZ = item;
                        }
                    }
                    if (manhKiemZ != null && quangKiemZ != null && quangKiemZ.quantity >= 99) {
                        player.combineNew.goldCombine = GOLD_KIEM_Z;
                        player.combineNew.gemCombine = GEM_KIEM_Z;
                        player.combineNew.ratioCombine = RATIO_KIEM_Z2;
                        String npcSay = "Kiếm Z cấp 1" + "\n|2|";
                        for (Item.ItemOption io : manhKiemZ.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Rèn Kiếm Z " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Rèn Kiếm Z\ncần " + player.combineNew.gemCombine + " Ngọc xanh");
                        } else {
                            npcSay += "Còn thiếu "
                                    + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                    + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else if (manhKiemZ == null || quangKiemZ == null) {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Kiếm Z và X99 Quặng Kiếm Z", "Đóng");
                    } else {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Số lượng quặng Kiếm Z không đủ", "Đóng");
                    }
                } else {
                    this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Kiếm Z và X99 Quặng Kiếm Z", "Đóng");
                }
                break;

            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; // sao pha lê đã ép
                    int starEmpty = 0; // lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|0|";
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102 && io.optionTemplate.id != 107) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (Item.ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|0|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|0|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name
                                        .replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\n" + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|0|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102 && io.optionTemplate.id != 107) {
                                    npcSay += io.getOptionString() + "\n";
                                } else if (io.optionTemplate.id == 107) {
                                    npcSay += (io.param + 1) + " Sao Pha Lê" + "\n";
                                } else if (io.optionTemplate.id != 107) {
                                    npcSay += " 1 Sao Pha Lê" + "\n";
                                }
                            }
                            npcSay += "|2|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\n" + player.combineNew.gemCombine + " ngọc");
                            } else {
                                npcSay += "Còn thiếu "
                                        + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                        + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                            "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI_5_LAN:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|0|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102 && io.optionTemplate.id != 107) {
                                    npcSay += io.getOptionString() + "\n";
                                } else if (io.optionTemplate.id == 107) {
                                    npcSay += (io.param + 1) + " Sao Pha Lê" + "\n";
                                } else if (io.optionTemplate.id != 107) {
                                    npcSay += " 1 Sao Pha Lê" + "\n";
                                }
                            }
                            npcSay += "|2|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\n" + player.combineNew.gemCombine + " ngọc");
                            } else {
                                npcSay += "Còn thiếu "
                                        + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                        + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                            "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI_10_LAN:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|0|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102 && io.optionTemplate.id != 107) {
                                    npcSay += io.getOptionString() + "\n";
                                } else if (io.optionTemplate.id == 107) {
                                    npcSay += (io.param + 1) + " Sao Pha Lê" + "\n";
                                } else if (io.optionTemplate.id != 107) {
                                    npcSay += " 1 Sao Pha Lê" + "\n";
                                }
                            }
                            npcSay += "|2|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\n" + player.combineNew.gemCombine + " ngọc");
                            } else {
                                npcSay += "Còn thiếu "
                                        + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                        + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                            "Đóng");
                }
                break;

            case PHA_LE_HOA_TRANG_BI_100_LAN:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|0|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102 && io.optionTemplate.id != 107) {
                                    npcSay += io.getOptionString() + "\n";
                                } else if (io.optionTemplate.id == 107) {
                                    npcSay += (io.param + 1) + " Sao Pha Lê" + "\n";
                                } else if (io.optionTemplate.id != 107) {
                                    npcSay += " 1 Sao Pha Lê" + "\n";
                                }
                            }
                            npcSay += "|2|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\n" + player.combineNew.gemCombine + " ngọc");
                            } else {
                                npcSay += "Còn thiếu "
                                        + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                        + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                            "Đóng");
                }
                break;

            // case PHAP_SU_HOA_TRANG_BI:
            // if (player.combineNew.itemsCombine.size() == 2) {
            // Item item2 = player.combineNew.itemsCombine.get(0);
            //
            // Item ngocphapsu = null;
            // for (Item item : player.combineNew.itemsCombine) {
            // if (item.template.id == 1151) {
            // ngocphapsu = item;
            // }
            // }
            // if (isTrangBiPhapSuHoa(item2) && ngocphapsu != null) {
            // int star = 0;
            // for (Item.ItemOption io : item2.itemOptions) {
            // if (io.optionTemplate.id == 218) {
            // star = io.param;
            // break;
            // }
            // }
            // if (star < MAX_STAR_ITEM) {
            //
            // String npcSay = "Trang bị được pháp sư hóa \"" + item2.template.name + "\"";
            //
            // npcSay += "\n|0|Sau khi Nâng cấp: +3% chỉ số pháp sư\n";
            //
            // npcSay += "|2|Tỉ lệ thành công: 100%" + "\n";
            // if (player.combineNew.goldCombine <= player.inventory.gold) {
            // npcSay += "Cần 0 vàng";
            // baHatMit.createOtherMenu(player, ConstNpc.MENU_PHAP_SU_HOA_TRANG_BI, npcSay,
            // "Nâng cấp", "Từ chối");
            // } else {
            // npcSay += "|2|Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine
            // - player.inventory.gold) + " vàng";
            // baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
            // }
            //
            // } else {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt
            // tối đa sao pha lê", "Đóng");
            // }
            // } else if (ngocphapsu == null) {
            // Service.gI().sendThongBaoOK(player, "Thiếu ngọc pháp sư");
            // } else {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này
            // không thể đục lỗ", "Đóng");
            // }
            // } else {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1
            // vật phẩm để pha lê hóa", "Đóng");
            // }
            // break;
            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20)
                                && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name
                                    + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                    "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống",
                            "Đóng");
                }
                break;

            case NANG_CAP_DO_TL:

                if (player.combineNew.itemsCombine.size() == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL())
                            .count() < 1) {
                        Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị Thần linh");
                        return;
                    }
                    Item item = player.combineNew.itemsCombine.get(0);
                    // if(item.template.type == 0){
                    String npcSay = "|-1|"
                            + player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get().typeName()
                            + " Hủy Diệt\n";
                    if (item.template.type == 3) {
                        npcSay += "|0|KI+?K\n"
                                + "+? KI/30s\n";
                    }
                    if (item.template.type == 1) {
                        npcSay += "|0|HP+?K\n"
                                + "+? HP/30s\n";
                    }
                    if (item.template.type == 0) {
                        npcSay += "|0|Giáp+?\n";
                    }
                    if (item.template.type == 4) {
                        npcSay += "|0|Chí mạng+?\n";
                    }
                    npcSay += "|0|Yêu cầu sức mạnh 80 tỉ\n"
                            + "|2|Tỷ lệ thành công: 100%\n"
                            + "|2|Cần 2 Tỷ vàng";
                    // }

                    int SoVangThieu = (int) (2000000000 - player.inventory.gold);
                    if (player.inventory.gold < 2000000000) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu " + SoVangThieu + " vàng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_TL,
                            npcSay, "Nâng cấp", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        Service.getInstance().sendThongBaoOK(player, "Nguyên liệu không đủ");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu", "Đóng");
                }
                break;

            case NANG_CAP_DO_HD:

                if (player.combineNew.itemsCombine.size() == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD())
                            .count() < 1) {
                        Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị Hủy Diệt");
                        return;
                    }
                    Item item = player.combineNew.itemsCombine.get(0);
                    // if(item.template.type == 0){

                    String npcSay = "";

                    if (item.template.gender == 0 && item.template.type == 0) {
                        npcSay += "Áo vải 3 lỗ";
                    }
                    if (item.template.gender == 0 && item.template.type == 1) {
                        npcSay += "Quần vải đen";
                    }
                    if (item.template.gender == 0 && item.template.type == 2) {
                        npcSay += "Găng thun đen";
                    }
                    if (item.template.gender == 0 && item.template.type == 3) {
                        npcSay += "Giầy nhựa";
                    }
                    if (item.template.gender == 0 && item.template.type == 4) {
                        npcSay += "Rada cấp 1";
                    } else if (item.template.gender == 1 && item.template.type == 0) {
                        npcSay += "Áo sợi len";
                    } else if (item.template.gender == 1 && item.template.type == 1) {
                        npcSay += "Quần sợi len";
                    } else if (item.template.gender == 1 && item.template.type == 2) {
                        npcSay += "Găng sợi len";
                    } else if (item.template.gender == 1 && item.template.type == 3) {
                        npcSay += "Giầy sợi len";
                    } else if (item.template.gender == 1 && item.template.type == 4) {
                        npcSay += "Rada cấp 1";
                    } else if (item.template.gender == 2 && item.template.type == 0) {
                        npcSay += "Áo vải thô";
                    } else if (item.template.gender == 2 && item.template.type == 1) {
                        npcSay += "Quần vải thô";
                    } else if (item.template.gender == 2 && item.template.type == 2) {
                        npcSay += "Găng vải thô";
                    } else if (item.template.gender == 2 && item.template.type == 3) {
                        npcSay += "Giầy vải thô";
                    } else if (item.template.gender == 3 && item.template.type == 4) {
                        npcSay += "Rada cấp 1";
                    }
                    npcSay += "\n|0|Ngẫu nhiên 1 trong 3 set kích hoạt\n"
                            + "|2|Tỷ lệ thành công: 100%\n"
                            + "|2|Cần 500Tr vàng";

                    int SoVangThieu = (int) (500000000 - player.inventory.gold);
                    if (player.inventory.gold < 500000000) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu " + SoVangThieu + " vàng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_HD,
                            npcSay, "Nâng cấp", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        Service.getInstance().sendThongBaoOK(player, "Nguyên liệu không đủ");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu", "Đóng");
                }
                break;

            case NANG_CAP_3_DO_HD:
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD())
                            .count() < 3) {
                        Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị Hủy Diệt");
                        return;
                    }
                    Item item = player.combineNew.itemsCombine.get(0);
                    // if(item.template.type == 0){

                    String npcSay = "Trang bị được nâng cấp \"" + item.Name() + "\"";

                    npcSay += "\n|0|Ngẫu nhiên 1 trong 3 set kích hoạt\n"
                            + "Có tỷ lệ được đồ Thần linh kích hoạt\n"
                            + "|2|Tỷ lệ thành công: 100%\n"
                            + "|2|Cần 500Tr vàng";

                    int SoVangThieu = (int) (500000000 - player.inventory.gold);
                    if (player.inventory.gold < 500000000) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu " + SoVangThieu + " vàng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_HD,
                            npcSay, "Nâng cấp", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        Service.getInstance().sendThongBaoOK(player, "Nguyên liệu không đủ");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu", "Đóng");
                }
                break;

            case THANG_HOA_NGOC_BOI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isNgocBoi())
                            .count() < 1) {
                        Service.getInstance().sendThongBaoOK(player, "Yêu cầu trang bị là\" Ngọc Bội\"");
                        return;
                    }
                    Item NgocBoi = player.combineNew.itemsCombine.stream().filter(Item::isNgocBoi).findFirst().get();
                    if (NgocBoi != null) {
                        for (ItemOption itopt : NgocBoi.itemOptions) {
                            if (itopt.optionTemplate.id == 225 && itopt.optionTemplate.id != 226) {
                                if (itopt.param >= 1) {
                                    Service.getInstance().sendThongBao(player, "Trang bị đã thăng hoa cho bản thân");
                                    return;
                                }
                            }
                            if (itopt.optionTemplate.id != 225 && itopt.optionTemplate.id == 226) {
                                if (itopt.param >= 1) {
                                    Service.getInstance().sendThongBao(player, "Trang bị đã thăng hoa cho đệ tử");
                                    return;
                                }
                            }
                        }
                    }
                    Item item = player.combineNew.itemsCombine.get(0);
                    // if(item.template.type == 0){
                    String npcSay = "Trang bị được nâng cấp \"" + item.Name() + "\"";

                    npcSay += "\n|0|Thăng hoa giúp sử dụng ngọc bội\n"
                            + "Tỷ lệ thành công: 100%\n"
                            + "|2|Cần 10.000 ngọc xanh";

                    int SoVangThieu = (int) (10000 - player.inventory.gem);
                    if (player.inventory.gem < 10000) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu " + SoVangThieu + " vàng");
                        return;
                    }
                    this.thoNgoc.createOtherMenu(player, ConstNpc.MENU_THANG_HOA_NGOC_BOI,
                            npcSay, "Thăng hoa\nBản thân", "Thăng hoa\nĐệ tử");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        Service.getInstance().sendThongBaoOK(player, "Yêu cầu trang bị là\" Ngọc Bội\"");
                        return;
                    }
                    this.thoNgoc.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Yêu cầu trang bị là\" Ngọc Bội\"",
                            "Đóng");
                }
                break;

            case CHE_TAO_TRANG_BI_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nâng cấp", "Yes");
                    return;
                }
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() < 1) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999)
                            .count() < 1) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 &&
                            player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem()
                                    && item.isDaNangCap()).count() < 1
                            || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream()
                                    .filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 &&
                            player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem()
                                    && item.isDaMayMan()).count() < 1
                            || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream()
                                    .filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
                        return;
                    }
                    Item mTS = null, daNC = null, daMM = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.isManhTS()) {
                                mTS = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;
                    int tilenew = tilemacdinh;
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                    }

                    String npcSay = "|1|Chế tạo "
                            + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get()
                                    .typeNameManh()
                            + " Thiên sứ "
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get()
                                    .typeHanhTinh()
                            + "\n"
                            + "|1|Mạnh hơn trang bị Hủy Diệt từ 20% đến 35% \n"
                            + "|2|Mảnh ghép " + mTS.quantity + "/999(Thất bại -99 mảnh ghép)";
                    if (daNC != null) {
                        npcSay += "|2|Đá nâng cấp "
                                + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get()
                                        .typeDanangcap()
                                + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn "
                                + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get()
                                        .typeDaMayman()
                                + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|2|Phí nâng cấp: 2 tỉ vàng";
                    if (player.inventory.gold < 2000000000) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Đồng ý", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu", "Đóng");
                }
                break;

            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3
                                    && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null
                                    ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe)
                                    : "";
                            if ((level == 2 || level == 4 || level == 6)
                                    && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity)
                                                + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay,
                                        "Còn thiếu\n"
                                                + Util.numberToMoney(
                                                        (player.combineNew.goldCombine - player.inventory.gold))
                                                + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV)
                                    && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity)
                                                + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp" + daNPC, "Nâng cấp bằng bùa\nNâng cấp", "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player, int select) {
        switch (player.combineNew.typeCombine) {
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case PHAP_SU_HOA_TRANG_BI:
                phapSuHoaTrangBi(player);
                break;
            case GIA_HAN_VAT_PHAM:
                GiaHanTrangBi(player);
                break;
            case THANG_HOA_NGOC_BOI:
                ThangHoaBanThan(player);
                break;
            case CHUYEN_HOA_TRANG_BI:
                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case NANG_CAP_VAT_PHAM:
                if (select == 1) {
                    nangCapVatPham(player);
                }
                if (select == 2) {
                    nangCapVatPham100PT(player);
                }
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
                break;
            case REN_KIEM_Z:
                renKiemZ(player);
                break;
            case CHE_TAO_TRANG_BI_TS:
                openCreateItemAngel(player);
                break;
            case NANG_CAP_DO_TL:
                openUpDoTL(player);
                break;
            case PS_HOA_TRANG_BI:
                psHoaTrangBi(player);
                break;
            case TAY_PS_HOA_TRANG_BI:
                tayHacHoaTrangBi(player);
                break;
            case NANG_CAP_DO_HD:
                openUpDoHD(player);
                break;
            case NANG_CAP_3_DO_HD:
                openUp3DoHD(player);
                break;
            case PHA_LE_HOA_TRANG_BI_5_LAN:
                phaLeHoaTrangBi5lan(player);
                break;
            case PHA_LE_HOA_TRANG_BI_10_LAN:
                phaLeHoaTrangBi10lan(player);
                break;
            case PHA_LE_HOA_TRANG_BI_100_LAN:
                phaLeHoaTrangBi100lan(player);
                break;
        }

        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }

    private void doiKiemz(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item keo = null, luoiKiem = null, chuoiKiem = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 2015) {
                    keo = it;
                } else if (it.template.id == 2016) {
                    chuoiKiem = it;
                } else if (it.template.id == 2017) {
                    luoiKiem = it;
                }
            }
            if (keo != null && keo.quantity >= 99 && luoiKiem != null && chuoiKiem != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2018);
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 15)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 15)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 15)));
                    if (Util.isTrue(80, 100)) {
                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                    }
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 99);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, luoiKiem, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, chuoiKiem, 1);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiChuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhNhua = player.combineNew.itemsCombine.get(0);
            if (manhNhua.template.id == 2014 && manhNhua.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2016);
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhNhua, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiLuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhSat = player.combineNew.itemsCombine.get(0);
            if (manhSat.template.id == 2013 && manhSat.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2017);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhSat, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void psHoaTrangBi(Player player) {

        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHacHoa())
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị pháp sư");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1151)
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đá pháp sư");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            Item daHacHoa = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1151).findFirst()
                    .get();
            Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHacHoa).findFirst()
                    .get();
            if (daHacHoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu đá pháp sư");
                return;
            }
            if (trangBiHacHoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị pháp sư");
                return;
            }

            if (trangBiHacHoa != null) {
                for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                    if (itopt.optionTemplate.id == 102) {
                        if (itopt.param >= 8) {
                            Service.getInstance().sendThongBao(player, "Trang bị đã đạt tới giới hạn pháp sư");
                            return;
                        }
                    }
                }
            }

            if (Util.isTrue(100, 100)) {
                sendEffectSuccessCombine(player);
                List<Integer> idOptionHacHoa = Arrays.asList(219, 220, 221, 222);
                int randomOption = idOptionHacHoa.get(Util.nextInt(0, 3));
                if (!trangBiHacHoa.haveOption(107)) {
                    trangBiHacHoa.itemOptions.add(new ItemOption(107, 1));
                    trangBiHacHoa.itemOptions.add(new ItemOption(223, 1));
                } else {
                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                        if (itopt.optionTemplate.id == 107) {
                            itopt.param += 1;
                            break;
                        }
                    }
                }
                if (!trangBiHacHoa.haveOption(102)) {
                    trangBiHacHoa.itemOptions.add(new ItemOption(102, 1));
                } else {
                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                        if (itopt.optionTemplate.id == 102) {
                            itopt.param += 1;
                            break;
                        }
                    }
                }
                if (!trangBiHacHoa.haveOption(randomOption)) {
                    trangBiHacHoa.itemOptions.add(new ItemOption(randomOption, 3));
                } else {
                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                        if (itopt.optionTemplate.id == randomOption) {
                            itopt.param += 3;
                            break;
                        }
                    }
                }
                Service.getInstance().sendThongBao(player, "Bạn đã nâng cấp thành công");
            } else {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, daHacHoa, 1);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void GiaHanTrangBi(Player player) {

        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHSD())
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị HSD");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1160)
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Bùa Gia Hạn");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            Item daHacHoa = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1160).findFirst()
                    .get();
            Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHSD).findFirst().get();
            if (daHacHoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu Bùa Gia Hạn");
                return;
            }
            if (trangBiHacHoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị HSD");
                return;
            }

            if (trangBiHacHoa != null) {
                for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                    if (itopt.optionTemplate.id == 93) {
                        if (itopt.param < 0 && itopt == null) {
                            Service.getInstance().sendThongBao(player, "Không Phải Trang Bị Có HSD");
                            return;
                        }
                    }
                }
            }
            if (Util.isTrue(100, 100)) {
                sendEffectSuccessCombine(player);
                List<Integer> idOptionHacHoa = Arrays.asList(219, 220, 221, 222);
                int randomOption = idOptionHacHoa.get(Util.nextInt(0, 3));

                for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                    if (itopt.optionTemplate.id == 93) {
                        itopt.param += 1;
                        break;
                    }
                }
            } else {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, daHacHoa, 1);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void ThangHoaBanThan(Player player) {
        if (player.combineNew.itemsCombine == null || player.combineNew.itemsCombine.size() != 1) {
            Service.getInstance().sendThongBaoOK(player, "Yêu cầu là trang bị ngọc bội");
            return;
        }

        Item NgocBoi = player.combineNew.itemsCombine.stream()
                .filter(item -> item.isNotNullItem() && item.isNgocBoi())
                .findFirst()
                .orElse(null);

        if (NgocBoi == null) {
            Service.getInstance().sendThongBaoOK(player, "Yêu cầu là trang bị ngọc bội");
            return;
        }

        for (ItemOption itopt : NgocBoi.itemOptions) {
            if (itopt.optionTemplate.id == 225 && itopt.optionTemplate.id != 226) {
                if (itopt.param > 0) {
                    Service.getInstance().sendThongBao(player, "Ngọc Bội Đã Thăng Hoa");
                    return;
                }
            }
        }

        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }

        sendEffectSuccessCombine(player);

        for (ItemOption itopt : NgocBoi.itemOptions) {
            if (itopt.optionTemplate.id != 225) {
                NgocBoi.itemOptions.add(new ItemOption(225, 1));
                break;
            }
        }
        InventoryServiceNew.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
    }

    private void tayHacHoaTrangBi(Player player) {

        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHacHoa())
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị hắc hóa");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1161)
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đá pháp sư");
            return;
        }

        Item buagiaihachoa = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1161)
                .findFirst().get();
        Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHacHoa).findFirst().get();
        if (buagiaihachoa == null) {
            Service.getInstance().sendThongBao(player, "Thiếu bùa giải pháp sư");
            return;
        }
        if (trangBiHacHoa == null) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị pháp sư");
            return;
        }

        if (Util.isTrue(100, 100)) {
            sendEffectSuccessCombine(player);
            List<Integer> idOptionHacHoa = Arrays.asList(219, 220, 221, 222);

            ItemOption option_223 = new ItemOption();
            ItemOption option_219 = new ItemOption();
            ItemOption option_220 = new ItemOption();
            ItemOption option_221 = new ItemOption();
            ItemOption option_222 = new ItemOption();
            ItemOption option_102 = new ItemOption();
            ItemOption option_107 = new ItemOption();

            for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                if (itopt.optionTemplate.id == 223) {
                    // System.out.println("223");
                    option_223 = itopt;
                }
                if (itopt.optionTemplate.id == 219) {
                    // System.out.println("219");
                    option_219 = itopt;
                }
                if (itopt.optionTemplate.id == 220) {
                    // System.out.println("220");
                    option_220 = itopt;
                }
                if (itopt.optionTemplate.id == 221) {
                    // System.out.println("221");
                    option_221 = itopt;
                }
                if (itopt.optionTemplate.id == 222) {
                    // System.out.println("222");
                    option_222 = itopt;
                }
                if (itopt.optionTemplate.id == 102) {
                    // System.out.println("102");
                    option_102 = itopt;
                }
                if (itopt.optionTemplate.id == 107) {
                    // System.out.println("107");
                    option_107 = itopt;
                }
            }
            if (option_223 != null) {
                trangBiHacHoa.itemOptions.remove(option_223);
            }
            if (option_219 != null) {
                trangBiHacHoa.itemOptions.remove(option_219);
            }
            if (option_220 != null) {
                trangBiHacHoa.itemOptions.remove(option_220);
            }
            if (option_221 != null) {
                trangBiHacHoa.itemOptions.remove(option_221);
            }
            if (option_222 != null) {
                trangBiHacHoa.itemOptions.remove(option_222);
            }
            if (option_102 != null) {
                trangBiHacHoa.itemOptions.remove(option_102);
            }
            if (option_107 != null) {
                trangBiHacHoa.itemOptions.remove(option_107);
            }
            Service.getInstance().sendThongBao(player, "Bạn đã tẩy thành công");
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            sendEffectFailCombine(player);
        }
        InventoryServiceNew.gI().subQuantityItemsBag(player, buagiaihachoa, 1);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        player.combineNew.itemsCombine.clear();
        reOpenItemCombine(player);
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }

            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }

            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 454) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                }
            }

            if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {
                Item findItemBag = InventoryServiceNew.gI().findItemBag(player, 921); // Khóa btc2
                if (findItemBag != null) {
                    Service.gI().sendThongBao(player, "Bạn đang sở hữu bông tai Porata cấp 2");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(921);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 2));
                    sendEffectSuccessCombine(player);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 9999);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã đập được bông tai cấp 2!");
                } else {
                    sendEffectFailCombine(player);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 99);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                    Service.gI().sendThongBao(player, "Xịt rồi , cày mảnh bông tai tiếp đi...");
                }

            }
        }
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item manhhon = null;
            Item bongtaiC2 = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 934) {
                    manhhon = item;
                }
                else if(item.template.id==921){
                    bongtaiC2=item;
                }
            }
            if (manhhon != null && manhhon.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhhon, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 1)) {
                    bongtaiC2.itemOptions.clear();
                    bongtaiC2.itemOptions.add(new Item.ItemOption(72, 2));
                    bongtaiC2.itemOptions.add(new Item.ItemOption(38, 1));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtaiC2.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                    } else if (rdUp == 1) {
                        bongtaiC2.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                    } else if (rdUp == 2) {
                        bongtaiC2.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                    } else if (rdUp == 3) {
                        bongtaiC2.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 25)));
                    } else if (rdUp == 4) {
                        bongtaiC2.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        bongtaiC2.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        bongtaiC2.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 25)));
                    } else if (rdUp == 7) {
                        bongtaiC2.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void PhapSuHoaTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            Item linhthu = null;
            Item thangtinhthach = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id >= 1157 && item.template.id <= 1159
                        || item.template.id >= 1135 && item.template.id <= 1137) {
                    linhthu = item;
                } else if (item.template.id == 934) {
                    thangtinhthach = item;
                }
            }
            if (linhthu != null && thangtinhthach != null) {
                player.inventory.gold -= gold;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thangtinhthach, 1);
                if (Util.isTrue(100, 100)) {
                    linhthu.itemOptions.add(new Item.ItemOption(218, 1));
                    int rdUp = Util.nextInt(0, 3);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(214, 3)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(215, 3)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(216, 3)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(217, 3)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void renKiemZ(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }

            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }

            Item manhKiemZ = null;
            Item quangKiemZ = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 865 || item.template.id == 1200) {
                    manhKiemZ = item;
                } else if (item.template.id == 1201) {
                    quangKiemZ = item;
                }
            }

            if (manhKiemZ != null && quangKiemZ != null && quangKiemZ.quantity >= 99) {
                Item findItemBag = InventoryServiceNew.gI().findItemBag(player, 1200);
                // Nguyên liệu
                if (findItemBag != null) {
                    Service.gI().sendThongBao(player, "Con đã có Kiếm Z trong hành trang rồi, không thể rèn nữa.");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, quangKiemZ, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    manhKiemZ.template = ItemService.gI().getTemplate(1200);
                    manhKiemZ.itemOptions.clear();
                    Random rand = new Random();
                    int ratioCombine = rand.nextInt(60) + 1;
                    int level = 0;
                    if (ratioCombine <= 40) {
                        level = 1 + rand.nextInt(4);
                    } else if (ratioCombine <= 70) {
                        level = 5 + rand.nextInt(4);
                    } else if (ratioCombine <= 90) {
                        level = 9 + rand.nextInt(4);
                    } else if (ratioCombine <= 95) {
                        level = 13 + rand.nextInt(3);
                    } else {
                        level = 16;
                    }
                    manhKiemZ.itemOptions.add(new Item.ItemOption(0, level * 200 + 10000));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(49, level * 1 + 20));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(14, level));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(97, level));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(30, 0));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(72, level));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    public void openUpDoTL(Player player) {

        if (player.combineNew.itemsCombine.size() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (player.inventory.gold < 2000000000) {
            Service.getInstance().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL())
                .findFirst().get();

        player.inventory.gold -= 2000000000;
        sendEffectSuccessCombine(player);
        short[][] itemIds = { { 650, 651, 657, 658, 656 }, { 652, 653, 659, 660, 656 }, { 654, 655, 661, 662, 656 } }; // thứ
                                                                                                                       // tự
                                                                                                                       // td
                                                                                                                       // -
                                                                                                                       // 0,nm
                                                                                                                       // -
                                                                                                                       // 1,
                                                                                                                       // xd
                                                                                                                       // -
                                                                                                                       // 2

        if (itemTL.template.gender < 3) {
            Item itemTS = ItemService.gI().randomCS_DHD(itemIds[itemTL.template.gender < 3 ? itemTL.template.gender
                    : itemTL.template.gender][itemTL.template.type], itemTL.template.gender);
            InventoryServiceNew.gI().addItemBag(player, itemTS);
        } else {
            Item itemTS = ItemService.gI().randomCS_DHD(
                    itemIds[itemTL.template.gender == 3 ? player.gender : itemTL.template.gender][itemTL.template.type],
                    itemTL.template.gender);
            InventoryServiceNew.gI().addItemBag(player, itemTS);
        }

        InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        player.combineNew.itemsCombine.clear();
        reOpenItemCombine(player);
    }

    // public void openUpDoHD(Player player) {
    // // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
    // if (player.combineNew.itemsCombine.size() != 1) {
    // Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
    // return;
    // }
    // if (player.combineNew.itemsCombine.stream().filter(item ->
    // item.isNotNullItem() && item.isDHD()).count() != 1) {
    // Service.gI().sendThongBao(player, "Thiếu đồ Hủy diệt");
    // return;
    // }
    // if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
    // if (player.inventory.gold < 500000000) {
    // Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
    // return;
    // }
    // player.inventory.gold -= 500000000;
    // Item itemHD =
    // player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get();
    // CombineServiceNew.gI().sendEffectOpenItem(player, itemHD.template.iconID,
    // itemHD.template.iconID);
    // short itemId;
    // if (itemHD.template.gender == 3 || itemHD.template.type == 4) {
    // itemId = Manager.radaSKHVip[player.gender];
    // if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 /
    // player.getSession().bdPlayer))) {
    // itemId = Manager.radaSKHVip[6];
    // }
    // }
    // if (itemHD.template.gender == 0) {
    // if (itemHD.template.type == 0) {
    // itemId = Manager.aotd[player.gender];
    // if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 /
    // player.getSession().bdPlayer))) {
    // itemId = Manager.aotd[6];
    // }
    // }
    // }
    // if (itemHD.template.gender == 0) {
    // if (itemHD.template.id == 4) {
    // itemId = Manager.radaSKHVip[player.gender];
    // if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 /
    // player.getSession().bdPlayer))) {
    // itemId = Manager.radaSKHVip[6];
    // }
    // }
    // }
    // byte gender012 = 0;
    // if (itemHD.template.gender < 3) {
    // gender012 = itemHD.template.gender;
    // } else {
    // gender012 = player.gender;
    // }
    // int skhId = ItemService.gI().randomSKHId(gender012);
    // Item item;
    // if (new Item(itemId).isDTL()) {
    // item = Util.ratiItemTL(itemId);
    // item.itemOptions.add(new Item.ItemOption(skhId, 1));
    // item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId),
    // 1));
    // item.itemOptions.remove(item.itemOptions.stream()
    // .filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
    // item.itemOptions.add(new Item.ItemOption(21, 15));
    // item.itemOptions.add(new Item.ItemOption(30, 1));
    // } else {
    // item = ItemService.gI().itemSKH(itemId, skhId);
    // }
    // InventoryServiceNew.gI().addItemBag(player, item);
    // InventoryServiceNew.gI().subQuantityItemsBag(player, itemHD, 1);
    // InventoryServiceNew.gI().sendItemBags(player);
    // Service.gI().sendMoney(player);
    // player.combineNew.itemsCombine.clear();
    // reOpenItemCombine(player);
    // } else {
    // Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành
    // trang");
    // }
    // }

    // }
    // ============================LỖI==============================//
    // public void openUpDoHD(Player player) {
    // // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
    // if (player.combineNew.itemsCombine.size() != 1) {
    // Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
    // return;
    // }
    // if (player.combineNew.itemsCombine.stream().filter(item ->
    // item.isNotNullItem() && item.isDHD()).count() != 1) {
    // Service.gI().sendThongBao(player, "Thiếu đồ Hủy diệt");
    // return;
    // }
    // if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
    // if (player.inventory.gold < 500000000) {
    // Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
    // return;
    // }
    // player.inventory.gold -= 500000000;
    // Item itemHD =
    // player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get();
    // CombineServiceNew.gI().sendEffectOpenItem(player, itemHD.template.iconID,
    // itemHD.template.iconID);
    // short itemId;
    // if (itemHD.template.gender != 3 || itemHD.template.type != 4) {
    // itemId =
    // Manager.doSKHVip[itemHD.template.gender][itemHD.template.type][itemHD.template.gender];
    // if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 /
    // player.getSession().bdPlayer))) {
    // itemId = Manager.doSKHVip[itemHD.template.gender][itemHD.template.type][6];
    // //[] ??? [
    // }
    // return;
    // } else {
    // itemId = Manager.radaSKHVip[player.gender];
    // if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 /
    // player.getSession().bdPlayer))) {
    // itemId = Manager.radaSKHVip2[6];
    // }
    // }
    // byte gender012 = 0;
    // if (itemHD.template.gender < 3) {
    // gender012 = itemHD.template.gender;
    // } else {
    // gender012 = player.gender;
    // }
    // int skhId = ItemService.gI().randomSKHId(gender012);
    // Item item;
    // if (new Item(itemId).isDTL()) {
    // item = Util.ratiItemTL(itemId);
    // item.itemOptions.add(new Item.ItemOption(skhId, 1));
    // item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId),
    // 1));
    // item.itemOptions.remove(item.itemOptions.stream().filter(itemOption ->
    // itemOption.optionTemplate.id == 21).findFirst().get());
    // item.itemOptions.add(new Item.ItemOption(21, 15));
    // item.itemOptions.add(new Item.ItemOption(30, 1));
    // } else {
    // item = ItemService.gI().itemSKH(itemId, skhId);
    // }
    // InventoryServiceNew.gI().addItemBag(player, item);
    // InventoryServiceNew.gI().sendItemBags(player);
    // Service.gI().sendMoney(player);
    // player.combineNew.itemsCombine.clear();
    // reOpenItemCombine(player);
    // } else {
    // Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành
    // trang");
    // }
    // }
    public void openUpDoHD(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 1) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ Hủy diệt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 500000000) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= 500000000;
            Item itemHD = player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream()
                    .filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemHD.template.iconID, itemHD.template.iconID);
            short itemId;
            if (itemHD.template.gender == 3 || itemHD.template.type == 4) {
                itemId = Manager.radaSKHVip2[player.gender];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.radaSKHVip2[6];
                }
            } else {
                itemId = Manager.doSKHVip2[itemHD.template.gender][itemHD.template.type][itemHD.template.gender];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.doSKHVip2[itemHD.template.gender][itemHD.template.type][6];
                }
            }
            byte gender012 = 0;
            if (itemHD.template.gender < 3) {
                gender012 = itemHD.template.gender;
            } else {
                gender012 = player.gender;
            }
            int skhId = ItemService.gI().randomSKHId(gender012);
            Item item;
            if (new Item(itemId).isDTL()) {
                item = Util.ratiItemTL(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream()
                        .filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void openUp3DoHD(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 3) {
            Service.gI().sendThongBao(player, "Thiếu đồ Hủy diệt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 500000000) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= 500000000;
            Item itemHD = player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream()
                    .filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemHD.template.iconID, itemHD.template.iconID);
            short itemId;
            if (itemHD.template.gender == 3 || itemHD.template.type == 4) {
                itemId = Manager.radaSKHVip3[player.gender];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.radaSKHVip2[6];
                }
            } else {
                itemId = Manager.doSKHVip3[itemHD.template.gender][itemHD.template.type][itemHD.template.gender];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.doSKHVip3[itemHD.template.gender][itemHD.template.type][6];
                }
            }
            byte gender012 = 0;
            if (itemHD.template.gender < 3) {
                gender012 = itemHD.template.gender;
            } else {
                gender012 = player.gender;
            }
            int skhId = ItemService.gI().randomSKHId(gender012);
            Item item;
            if (new Item(itemId).isDTL()) {
                item = Util.ratiItemTL(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream()
                        .filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);

            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void openCreateItemAngel(Player player) {
        // Công thức vip + x999 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
            Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip())
                .count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream()
                .filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh thiên sứ");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 &&
                player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem()
                        && item.isDaNangCap()).count() != 1
                || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream()
                        .filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 &&
                player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem()
                        && item.isDaMayMan()).count() != 1
                || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream()
                        .filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
            return;
        }
        Item mTS = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.isManhTS()) {
                    mTS = item;
                } else if (item.isDaNangCap()) {
                    daNC = item;
                } else if (item.isDaMayMan()) {
                    daMM = item;
                } else if (item.isCongThucVip()) {
                    CtVip = item;
                }
            }
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {// check chỗ trống hành trang
            if (player.inventory.gold < 2000000000) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            player.inventory.gold -= 2000000000;

            int tilemacdinh = 35;
            int tileLucky = 20;
            if (daNC != null) {
                tilemacdinh += (daNC.template.id - 1073) * 10;
            } else {
                tilemacdinh = tilemacdinh;
            }
            if (daMM != null) {
                tileLucky += tileLucky * (daMM.template.id - 1078) * 10 / 100;
            } else {
                tileLucky = tileLucky;
            }
            if (Util.nextInt(0, 100) < tilemacdinh) {
                Item itemCtVip = player.combineNew.itemsCombine.stream()
                        .filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                if (daNC != null) {
                    Item itemDaNangC = player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                }
                if (daMM != null) {
                    Item itemDaMayM = player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                }
                Item itemManh = player.combineNew.itemsCombine.stream()
                        .filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).findFirst()
                        .get();

                tilemacdinh = Util.nextInt(0, 50);
                if (tilemacdinh == 49) {
                    tilemacdinh = 20;
                } else if (tilemacdinh == 48 || tilemacdinh == 47) {
                    tilemacdinh = 19;
                } else if (tilemacdinh == 46 || tilemacdinh == 45) {
                    tilemacdinh = 18;
                } else if (tilemacdinh == 44 || tilemacdinh == 43) {
                    tilemacdinh = 17;
                } else if (tilemacdinh == 42 || tilemacdinh == 41) {
                    tilemacdinh = 16;
                } else if (tilemacdinh == 40 || tilemacdinh == 39) {
                    tilemacdinh = 15;
                } else if (tilemacdinh == 38 || tilemacdinh == 37) {
                    tilemacdinh = 14;
                } else if (tilemacdinh == 36 || tilemacdinh == 35) {
                    tilemacdinh = 13;
                } else if (tilemacdinh == 34 || tilemacdinh == 33) {
                    tilemacdinh = 12;
                } else if (tilemacdinh == 32 || tilemacdinh == 31) {
                    tilemacdinh = 11;
                } else if (tilemacdinh == 30 || tilemacdinh == 29) {
                    tilemacdinh = 10;
                } else if (tilemacdinh <= 28 || tilemacdinh >= 26) {
                    tilemacdinh = 9;
                } else if (tilemacdinh <= 25 || tilemacdinh >= 23) {
                    tilemacdinh = 8;
                } else if (tilemacdinh <= 22 || tilemacdinh >= 20) {
                    tilemacdinh = 7;
                } else if (tilemacdinh <= 19 || tilemacdinh >= 17) {
                    tilemacdinh = 6;
                } else if (tilemacdinh <= 16 || tilemacdinh >= 14) {
                    tilemacdinh = 5;
                } else if (tilemacdinh <= 13 || tilemacdinh >= 11) {
                    tilemacdinh = 4;
                } else if (tilemacdinh <= 10 || tilemacdinh >= 8) {
                    tilemacdinh = 3;
                } else if (tilemacdinh <= 7 || tilemacdinh >= 5) {
                    tilemacdinh = 2;
                } else if (tilemacdinh <= 4 || tilemacdinh >= 2) {
                    tilemacdinh = 1;
                } else if (tilemacdinh <= 1) {
                    tilemacdinh = 0;
                }
                short[][] itemIds = { { 1048, 1051, 1054, 1057, 1060 }, { 1049, 1052, 1055, 1058, 1061 },
                        { 1050, 1053, 1056, 1059, 1062 } }; // thứ tự td - 0,nm - 1, xd - 2

                Item itemTS = ItemService.gI().DoThienSu(
                        itemIds[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh
                                .typeIdManh()],
                        itemCtVip.template.gender);

                tilemacdinh += 10;

                if (tilemacdinh > 0) {
                    for (byte i = 0; i < itemTS.itemOptions.size(); i++) {
                        if (itemTS.itemOptions.get(i).optionTemplate.id != 21
                                && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                            itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param * tilemacdinh / 100);
                        }
                    }
                }
                tilemacdinh = Util.nextInt(0, 100);

                if (tilemacdinh <= tileLucky) {
                    if (tilemacdinh >= (tileLucky - 3)) {
                        tileLucky = 3;
                    } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                        tileLucky = 2;
                    } else {
                        tileLucky = 1;
                    }
                    itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                    ArrayList<Integer> listOptionBonus = new ArrayList<>();
                    listOptionBonus.add(50);
                    listOptionBonus.add(77);
                    listOptionBonus.add(103);
                    listOptionBonus.add(98);
                    listOptionBonus.add(99);
                    for (int i = 0; i < tileLucky; i++) {
                        tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                        itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                        listOptionBonus.remove(tilemacdinh);
                    }
                }

                InventoryServiceNew.gI().addItemBag(player, itemTS);
                sendEffectSuccessCombine(player);
                if (mTS != null && daMM != null && daNC != null && CtVip != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                } else if (CtVip != null && mTS != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                } else if (CtVip != null && mTS != null && daNC != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                } else if (CtVip != null && mTS != null && daMM != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                }

                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);

            } else {
                sendEffectFailCombine(player);
                if (mTS != null && daMM != null && daNC != null && CtVip != null) {

                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);

                } else if (CtVip != null && mTS != null) {

                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                } else if (CtVip != null && mTS != null && daNC != null) {

                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);

                } else if (CtVip != null && mTS != null && daMM != null) {

                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);

                }

                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }

        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; // sao pha lê đã ép
            int starEmpty = 0; // lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.gem -= gem;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                    }

                    InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 200)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi5lan(Player player) {
        int success = 0;
        int star = 0;
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }

            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;

                    for (int i = 0; i < 5; i++) {
                        if (Util.isTrue(player.combineNew.ratioCombine, 200)) {
                            if (optionStar == null) {
                                item.itemOptions.add(new Item.ItemOption(107, 1));
                            } else {
                                optionStar.param++;
                                star++;
                            }
                            success++;
                            Service.gI().sendThongBao(player,
                                    "Bạn đã đập thành công " + star + " sao sau " + success + " lần đập");
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendMoney(player);
                            reOpenItemCombine(player);
                            if (optionStar != null && optionStar.param >= 7) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                        + "thành công " + item.template.name + " lên " + optionStar.param
                                        + " sao pha lê");
                            }
                            break;
                        } else {
                            sendEffectFailCombine(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendMoney(player);
                            reOpenItemCombine(player);
                            break;
                        }
                    }
                    if (success != 4 && success != 0) {

                    } else {
                        Service.gI().sendThongBao(player, "Hơi đen, anh Thanh chia buồn ...");
                    }
                }
            }
        }
    }

    private void phaLeHoaTrangBi10lan(Player player) {
        int success = 0;
        int star = 0;
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }

            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    for (int i = 0; i < 10; i++) {
                        if (Util.isTrue(player.combineNew.ratioCombine, 200)) {
                            if (optionStar == null) {
                                item.itemOptions.add(new Item.ItemOption(107, 1));
                                star = 1;
                            } else {
                                optionStar.param++;
                                star++;
                            }
                            success++;
                            Service.gI().sendThongBao(player,
                                    "Bạn đã đập thành công " + star + " sao sau " + success + " lần đập");
                            if (optionStar != null && optionStar.param >= 7) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                        + "thành công " + item.template.name + " lên " + optionStar.param
                                        + " sao pha lê");
                            }
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendMoney(player);
                            reOpenItemCombine(player);
                            break;
                        } else {
                            sendEffectFailCombine(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendMoney(player);
                            reOpenItemCombine(player);
                        }

                    }
                    if (success != 9 && success != 0) {

                    } else {
                        Service.gI().sendThongBao(player, "Hơi đen ,cố xíu nữa!");
                    }
                }
            }
        }
    }

    private void phaLeHoaTrangBi100lan(Player player) {
        int success = 0;
        int star = 0;
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    for (int i = 0; i < 100; i++) {
                        if (Util.isTrue(player.combineNew.ratioCombine, 200)) {
                            if (optionStar == null) {
                                item.itemOptions.add(new Item.ItemOption(107, 1));
                                star = 1;
                            } else {
                                optionStar.param++;
                                star++;
                            }
                            success++;
                            Service.gI().sendThongBao(player,
                                    "Bạn đã đập thành công " + star + " sao sau " + success + " lần đập");
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendMoney(player);
                            reOpenItemCombine(player);
                            if (optionStar != null && optionStar.param >= 7) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                        + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                            }
                            break;
                        } else {

                            sendEffectFailCombine(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendMoney(player);
                            reOpenItemCombine(player);
                        }
                    }
                    if (success != 4 && success != 0) {

                    } else {
                        Service.gI().sendThongBao(player,
                                "Còn thở là còn gỡ , không phải sợ , hết vàng anh Thanh buff cho");
                    }
                }
            }
        }
    }

    private void phapSuHoaTrangBi(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            if (player.combineNew.itemsCombine.size() == 2) {
                Item item2 = player.combineNew.itemsCombine.get(0);
                int ChiSoMacDinh = 0;
                Item.ItemOption optionChiSoMacDinh = null;
                Item ngocphapsu = null;
                for (Item item : player.combineNew.itemsCombine) {
                    if (item.template.id == 1151) {
                        ngocphapsu = item;
                    }
                }
                if (isTrangBiPhapSuHoa(item2) && ngocphapsu != null) {

                    for (Item.ItemOption io : item2.itemOptions) {
                        if (io.optionTemplate.id == 218) {
                            ChiSoMacDinh = io.param;
                            optionChiSoMacDinh = io;
                            break;
                        }
                    }
                    int ChiSoPhapSu = 0;
                    for (Item.ItemOption io : item2.itemOptions) {
                        int chisophapsu = Util.nextInt(214, 217);
                        if (io.optionTemplate.id == chisophapsu) {
                            ChiSoPhapSu = io.param;
                            optionChiSoMacDinh = io;
                            break;
                        }
                    }
                    if (ChiSoMacDinh < MAX_STAR_ITEM) {
                        int rdUp = Util.nextInt(0, 3);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocphapsu, 1);
                        if (optionChiSoMacDinh == null) {
                            item2.itemOptions.add(new Item.ItemOption(218, 1));
                            if (rdUp == 0) {
                                item2.itemOptions.add(new Item.ItemOption(214, 3));
                            } else if (rdUp == 1) {
                                item2.itemOptions.add(new Item.ItemOption(215, 3));
                            } else if (rdUp == 2) {
                                item2.itemOptions.add(new Item.ItemOption(216, 3));
                            } else if (rdUp == 3) {
                                item2.itemOptions.add(new Item.ItemOption(217, 3));
                            }
                        } else if (optionChiSoMacDinh != null) {
                            optionChiSoMacDinh.param++;
                            if (rdUp == 0) {
                                item2.itemOptions.add(new Item.ItemOption(214, 3));
                            } else if (rdUp == 1) {
                                item2.itemOptions.add(new Item.ItemOption(215, 3));
                            } else if (rdUp == 2) {
                                item2.itemOptions.add(new Item.ItemOption(216, 3));
                            } else if (rdUp == 3) {
                                item2.itemOptions.add(new Item.ItemOption(217, 3));
                            }

                            sendEffectSuccessCombine(player);

                        }
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendMoney(player);
                        reOpenItemCombine(player);
                    }
                }
            }
        }
    }

    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20)
                        && item.quantity >= 7) {
                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                    sendEffectCombineDB(player, item.template.iconID);
                } else {
                    Service.gI().sendThongBaoOK(player, "Vật phẩm không phải là ngọc rồng");
                }
            }
        } else {
            Service.gI().sendThongBaoOK(player, "Không đủ ô trống trong hành trang");
        }
    }

    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5)
                    .count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14)
                    .count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream()
                    .filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;// admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3
                            && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
                        if (optionLevel != null && optionLevel.param >= 5) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
                                    + "thành công trang bị lên +" + optionLevel.param);

                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 10 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapVatPham100PT(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5)
                    .count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream()
                    .filter(buanangcap -> buanangcap.isNotNullItem() && buanangcap.template.id == 9999).count() <= 0) {
                Service.gI().sendThongBao(player, "Thiếu bùa nâng cấp!!");
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14)
                    .count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream()
                    .filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;// admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3
                            && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }

                    option.param += (option.param * 10 / 100);
                    if (option2 != null) {
                        option2.param += (option2.param * 10 / 100);
                    }
                    if (optionLevel == null) {
                        itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                    } else {
                        optionLevel.param++;
                    }
                    if (optionLevel != null && optionLevel.param >= 5) {
                        ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
                                + "thành công trang bị lên +" + optionLevel.param);

                    }
                    sendEffectSuccessCombine(player);

                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    // --------------------------------------------------------------------------
    /**
     * r
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    // --------------------------------------------------------------------------Ratio,
    // cost combine
    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 5000000;
            case 1:
                return 10000000;
            case 2:
                return 20000000;
            case 3:
                return 40000000;
            case 4:
                return 60000000;
            case 5:
                return 90000000;
            case 6:
                return 120000000;
            case 7:
                return 180000000;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) { // tile dap do chi hat mit
        switch (star) {
            case 0:
                return 100f;
            case 1:
                return 50f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 15f;
            case 5:
                return 8f;
            case 6:
                return 3f;
            case 7:
                return 1f;
        }
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 80;
            case 8:
                return 100;
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 100;
        }
        return 0;
    }

    private double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 7;
            case 5:
                return 5;
            case 6:
                return 1;
            case 7:
                return 1;
            case 8:
                return 5;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 70;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 70000;
            case 2:
                return 300000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 100000000;
            case 7:
                return 250000000;
        }
        return 0;
    }

    // --------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20)
                || (item.template.id >= 441 && item.template.id <= 447)
                || (item.template.id >= 964 && item.template.id <= 965));
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if ((item.template.type <= 5 || item.template.type == 3 || item.template.type == 32)
                    && item.template.id != 1135) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiPhapSuHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 1157 && item.template.id <= 1159 && item.template.id == 1135) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5;
            case 19:
                return 5;
            case 18:
                return 5;
            case 17:
                return 5;
            case 16:
                return 3;
            case 15:
                return 2;
            case 14:
                return 5;
            case 441:
                return 5;
            case 442:
                return 5;
            case 443:
                return 5;
            case 444:
                return 5;
            case 445:
                return 5;
            case 446:
                return 5;
            case 447:
                return 5;
            case 964:
                return 10;
            case 965:
                return 10;
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77; // hp
            case 19:
                return 103; // ki
            case 18:
                return 80; // hp 30s
            case 17:
                return 81; // mp 30s
            case 16:
                return 50; // sức đánh
            case 15:
                return 94; // giáp %
            case 14:
                return 108; // né đòn
            case 441:
                return 95; // hút hp
            case 442:
                return 96; // hút ki
            case 443:
                return 97; // phả sát thương
            case 444:
                return 98; // xuyên giáp chưởng
            case 445:
                return 99; // xuyên giáp đấm
            case 446:
                return 100; // vàng rơi từ quái
            case 447:
                return 101; // tấn công % khi đánh quái
            case 964:
                return 14; // chí mạng
            case 965:
                return 50; // sức đánh
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    // Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    // --------------------------------------------------------------------------Text
    // tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PS_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pháp sư";
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case PHA_LE_HOA_TRANG_BI_5_LAN:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case PHA_LE_HOA_TRANG_BI_10_LAN:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case PHA_LE_HOA_TRANG_BI_100_LAN:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case REN_KIEM_Z:
                return "Ta sẽ rèn\ncho con thanh\nKiếm Z này";
            case CHE_TAO_TRANG_BI_TS:
                return "Chế tạo\ntrang bị thiên sứ";
            case NANG_CAP_DO_TL:
                return "Ta sẽ phù phép\n"
                        + "cho trang bị Thần linh\n"
                        + "trở thành trang bị Hủy diệt";
            case NANG_CAP_DO_HD:
                return "Ta sẽ phù phép\n"
                        + "cho trang bị Hủy diệt\n"
                        + "trở thành trang bị Kích hoạt";
            case NANG_CAP_3_DO_HD:
                return "Trang bị chọn đầu tiên\n"
                        + "Là trang bị được nâng cấp";
            case TAY_PS_HOA_TRANG_BI:
                return "Ta sẽ Giải pháp sư\nncho trang bị của ngươi\nntrở thành trang bị thường";
            case GIA_HAN_VAT_PHAM:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\nthêm hạn sử dụng";
            case THANG_HOA_NGOC_BOI:
                return "Ta sẽ phù phép\ncho ngọc bội của ngươi\nthăng hoa";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case PS_HOA_TRANG_BI:
                return "Chọn trang bị\n(Cải trang Thần Zeno, Pet, Phụ kiện bang)\nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "Vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case CHE_TAO_TRANG_BI_TS:
                return "Cần 1 công thức vip\n"
                        + "Mảnh trang bị tương ứng\n"
                        + "1 đá nâng cấp (tùy chọn)\n"
                        + "1 đá may mắn (tùy chọn)\n";
            case NANG_CAP_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\n99 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata cấp 2\nChọn mảnh hồn bông tai số lượng 99 cái\nSau đó chọn 'Nâng cấp chỉ số'";
            case REN_KIEM_Z:
                return "Chọn Kiếm Z\nChọn Quặng Z, số lượng\n99 cái\nSau đó chọn 'Rèn Kiếm'\n Ngẫu nhiên Kiếm Z cấp 1 đến cấp 16";
            case NANG_CAP_DO_TL:
                return "Vào hành trang\n"
                        + "Chọn trang bị\n"
                        + "(Áo, quần, găng, giầy hoặc rada)\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_DO_HD:
                return "Vào hành trang\n"
                        + "Chọn trang bị\n"
                        + "(Áo, quần, găng, giầy hoặc rada)\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_3_DO_HD:
                return "Vào hành trang\n"
                        + "Chọn 3 trang bị Hủy diệt\n"
                        + "(Áo, quần, găng, giầy hoặc rada)\n"
                        + "Sau đó chọn 'Nâng cấp'";

            case TAY_PS_HOA_TRANG_BI:
                return "Chọn trang bị\n(Cải trang Thần Zeno, Pet, Phụ kiện bang)\nSau đó chọn 'Nâng cấp'";
            case GIA_HAN_VAT_PHAM:
                return "vào hành trang\n"
                        + "Chọn 1 trang bị có hạn sử dụng\n"
                        + "Chọn thẻ gia hạn\n"
                        + "Sau đó chọn 'Gia hạn'";
            case THANG_HOA_NGOC_BOI:
                return "vào hành trang\n"
                        + "Chọn 1 vật phẩm ngọc bội\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI_5_LAN:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI_10_LAN:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI_100_LAN:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            default:
                return "";
        }
    }
}
