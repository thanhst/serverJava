package com.louisnguyen.models.reward;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;


@Data
public class MobReward {

    private int mobId;

    public int getMobId() {
        return mobId;
    }

    public List<ItemMobReward> getItemReward() {
        return itemReward;
    }

    public List<ItemMobReward> getGoldReward() {
        return goldReward;
    }

    public void setMobId(int mobId) {
        this.mobId = mobId;
    }

    public void setItemReward(List<ItemMobReward> itemReward) {
        this.itemReward = itemReward;
    }

    public void setGoldReward(List<ItemMobReward> goldReward) {
        this.goldReward = goldReward;
    }

    private List<ItemMobReward> itemReward;
    private List<ItemMobReward> goldReward;

    public MobReward(int mobId) {
        this.mobId = mobId;
        this.itemReward = new ArrayList<>();
        this.goldReward = new ArrayList<>();
    }
}






















