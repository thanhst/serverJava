package com.louisnguyen.models.reward;

import com.louisnguyen.models.Template;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.map.ItemMap;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.server.Manager;
import com.louisnguyen.utils.Util;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;


@Data
public class ItemMobReward {

    public Template.ItemTemplate getTemp() {
        return temp;
    }

    public int[] getMapDrop() {
        return mapDrop;
    }

    public int[] getQuantity() {
        return quantity;
    }

    public int[] getRatio() {
        return ratio;
    }

    public int getGender() {
        return gender;
    }

    public List<ItemOptionMobReward> getOption() {
        return option;
    }

    public void setTemp(Template.ItemTemplate temp) {
        this.temp = temp;
    }

    public void setMapDrop(int[] mapDrop) {
        this.mapDrop = mapDrop;
    }

    public void setQuantity(int[] quantity) {
        this.quantity = quantity;
    }

    public void setRatio(int[] ratio) {
        this.ratio = ratio;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setOption(List<ItemOptionMobReward> option) {
        this.option = option;
    }

    private Template.ItemTemplate temp;
    private int[] mapDrop;
    private int[] quantity;
    private int[] ratio;
    private int gender;

    private List<ItemOptionMobReward> option;

    public ItemMobReward(int tempId, int[] mapDrop, int[] quantity, int[] ratio, int gender) {
        this.temp = Manager.ITEM_TEMPLATES.get(tempId);
        this.mapDrop = mapDrop;
        this.quantity = quantity;
        if (this.quantity[0] < 0) {
            this.quantity[0] = -this.quantity[0];
        } else if (this.quantity[0] == 0) {
            this.quantity[0] = 1;
        }
        if (this.quantity[1] < 0) {
            this.quantity[1] = -this.quantity[1];
        } else if (this.quantity[1] == 0) {
            this.quantity[1] = 1;
        }
        if (this.quantity[0] > this.quantity[1]) {
            int tempSwap = this.quantity[0];
            this.quantity[0] = this.quantity[1];
            this.quantity[1] = tempSwap;
        }
        this.ratio = ratio;
        this.gender = gender;
        this.option = new ArrayList<>();
    }

    public ItemMap getItemMap(Zone zone, Player player, int x, int y) {
        for (int mapId : this.mapDrop) {
            if (mapId != -1 && mapId != zone.map.mapId) {
                continue;
            }
            if(this.gender != -1 && this.gender != player.gender){
                break;
            }
            if (Util.isTrue(this.ratio[0], this.ratio[1])) {
                ItemMap itemMap = new ItemMap(zone, this.temp, Util.nextInt(this.quantity[0], this.quantity[1]), 
                        x, y, player.id);
                for(ItemOptionMobReward opt : this.option){
                    if(!Util.isTrue(opt.getRatio()[0], opt.getRatio()[1])){
                        continue;
                    }
                    itemMap.options.add(new Item.ItemOption(opt.getTemp(), Util.nextInt(opt.getParam()[0], opt.getParam()[1])));
                }
                return itemMap;
            }
        }
        return null;
    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - TiMi :)))
 */
