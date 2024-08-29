package com.louisnguyen.models.reward;

import com.louisnguyen.models.Template;
import com.louisnguyen.server.Manager;
import lombok.Data;


@Data
public class ItemOptionMobReward {

    private Template.ItemOptionTemplate temp;

    public Template.ItemOptionTemplate getTemp() {
        return temp;
    }

    public int[] getParam() {
        return param;
    }

    public int[] getRatio() {
        return ratio;
    }

    public void setTemp(Template.ItemOptionTemplate temp) {
        this.temp = temp;
    }

    public void setParam(int[] param) {
        this.param = param;
    }

    public void setRatio(int[] ratio) {
        this.ratio = ratio;
    }
    private int[] param;
    private int[] ratio;

    public ItemOptionMobReward(int tempId, int[] param, int[] ratio) {
        this.temp = Manager.ITEM_OPTION_TEMPLATES.get(tempId);
        this.param = param;
        if (this.param[0] < 0) {
            this.param[0] = -this.param[0];
        } else if (this.param[0] == 0) {
            this.param[0] = 1;
        }
        if (this.param[1] < 0) {
            this.param[1] = -this.param[1];
        } else if (this.param[1] == 0) {
            this.param[1] = 1;
        }
        if (this.param[0] > this.param[1]) {
            int tempSwap = this.param[0];
            this.param[0] = this.param[1];
            this.param[1] = tempSwap;
        }
        this.ratio = ratio;
    }
    
}





















