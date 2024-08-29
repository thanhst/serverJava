package com.louisnguyen.models.npc.specialnpc;

import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.InventoryServiceNew;
import com.louisnguyen.services.PetService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.Util;

public class Egg {
    private Player player;

    public Egg(Player player) {
        this.player = player;
    };

    public void openMabuEgg(int gender) {
        if (this.player.pet != null) {
            try {
                Item trungTd = InventoryServiceNew.gI().findItemBag(player, 568);
                InventoryServiceNew.gI().subQuantityItemsBag(player, trungTd, 1);
                InventoryServiceNew.gI().sendItemBags(player);
                Thread.sleep(4000);
                if (this.player.pet == null) {
                    PetService.gI().createMabuPet(this.player, gender);
                } else {
                    PetService.gI().changeMabuPet(this.player, gender);
                }
                ChangeMapService.gI().changeMapInYard(this.player, this.player.gender * 7, -1, Util.nextInt(300, 500));
            } catch (Exception e) {

            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu phải có đệ tử");
        }
    }

    public void openBerusEgg(int gender) {
        if (this.player.pet != null) {
            try {
                Thread.sleep(4000);
                Item trungTd = InventoryServiceNew.gI().findItemBag(player, 2028);
                InventoryServiceNew.gI().subQuantityItemsBag(player, trungTd, 1);
                Thread.sleep(2000);
                if (this.player.pet == null) {
                    PetService.gI().createBerusPet(this.player, gender);
                } else {
                    PetService.gI().changeBerusPet(this.player, gender);
                }
                ChangeMapService.gI().changeMapInYard(this.player, this.player.gender * 7, -1, Util.nextInt(300, 500));
            } catch (Exception e) {

            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu phải có đệ tử");
        }
    }

    public void openUubEgg(int gender) {
        if (this.player.pet != null) {
            try {
                Item trungTd = InventoryServiceNew.gI().findItemBag(player, 2027);
                InventoryServiceNew.gI().subQuantityItemsBag(player, trungTd, 1);
                Thread.sleep(4000);
                if (this.player.pet == null) {
                    PetService.gI().createUubPet(this.player, gender);
                } else {
                    PetService.gI().changeUubPet(this.player, gender);
                }
                ChangeMapService.gI().changeMapInYard(this.player, this.player.gender * 7, -1, Util.nextInt(300, 500));
            } catch (Exception e) {

            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu phải có đệ tử");
        }
    }
}
