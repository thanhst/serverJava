package com.louisnguyen.models.map.BanDoKhoBau_old;

import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.boss.list_boss.BanDoKhoBau_old.TrungUyXanhLo;
import com.louisnguyen.models.map.BanDoKhoBau_old.BanDoKhoBau;
import static com.louisnguyen.models.map.BanDoKhoBau_old.BanDoKhoBau.TIME_BAN_DO_KHO_BAU;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.InventoryServiceNew;
import com.louisnguyen.services.MapService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.Util;
import java.util.List;

/**
 *
 * @author BTH
 *
 */
public class BanDoKhoBauService {

    private static BanDoKhoBauService i;

    private BanDoKhoBauService() {

    }

    public static BanDoKhoBauService gI() {
        if (i == null) {
            i = new BanDoKhoBauService();
        }
        return i;
    }

    public void openBanDoKhoBau(Player player, byte level) {
        if (level >= 1 && level <= 110) {
            if (player.clan != null && player.clan.banDoKhoBau == null) {
                Item item = InventoryServiceNew.gI().findItemBag(player, 611);
                if (item != null && item.quantity > 0) {
                    BanDoKhoBau banDoKhoBau = null;
                    for (BanDoKhoBau bdkb : BanDoKhoBau.BAN_DO_KHO_BAUS) {
                        if (!bdkb.isOpened) {
                            banDoKhoBau = bdkb;
                            break;
                        }
                    }
                    if (banDoKhoBau != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                        InventoryServiceNew.gI().sendItemBags(player);
                        banDoKhoBau.openBanDoKhoBau(player, player.clan, level);
                        try {
                            long bossDamage = (20 * level);
                            long bossMaxHealth = (2 * level);
                            bossDamage = Math.min(bossDamage, 200000000L);
                            bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);
                            TrungUyXanhLo boss = new TrungUyXanhLo(
                                    player.clan.banDoKhoBau.getMapById(137),
                                    player.clan.banDoKhoBau.level,

                                    (int) bossDamage,
                                    (int) bossMaxHealth);
                        } catch (Exception exception) {
                            Logger.logException(BanDoKhoBauService.class, exception, "Error initializing boss");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Bản đồ kho báu đã đầy, vui lòng quay lại sau");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Yêu cầu có bản đồ kho báu");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
}
