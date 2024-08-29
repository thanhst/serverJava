package com.louisnguyen.models.player;

import com.louisnguyen.models.item.Item;


public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku;
    public byte thienXinHang;
    public byte kirin;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;

    public byte kakarot;
    public byte cadic;
    public byte nappa;

    public byte worldcup;
    public byte setDHD;
    public byte setspl;
   public byte setthucan;
 public byte setts;
    public boolean godClothes;
    public int ctHaiTac = -1;
       public int upthucan = -1;

    public void setup() {
        setDefault();
        setupSKT();
        this.godClothes = true;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;

            }
        }
        
         this.player.setClothes.setthucan = 0;
           for (int i = 0; i < 5; i++) {
           Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
              if (item.template.id >= 555 && item.template.id <= 567) {
                 
                        player.setClothes.setthucan++;
                }
            }  
           }
            this.player.setClothes.setts = 0;
           for (int i = 0; i < 5; i++) {
           Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
              if (item.template.id > 662 || item.template.id < 650) {
                 
                        player.setClothes.setts++;
                }
            }  
           }
               this.player.setClothes.setspl = 0;
           for (int i = 0; i < 6; i++) {
           Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                  boolean isActSet = false;
              if ( item.template.id == 455|| item.template.id == 458|| item.template.id == 461) {
                  isActSet = true;
                        player.setClothes.setspl++;
                           break;
                }
            }  
           }
        //    Item item = this.player.inventory.itemsBody.get(i);
       //     if (item.isNotNullItem()) {
      //          if (item.template.id > 567 || item.template.id < 555) {
     //               this.setthucan = false;
         //           break;
      //          }
      //      }
   //     }
  //
    }
        
    
    
    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kirin++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                            break;
                        case 21:
                            if (io.param == 80) {
                                setDHD++;
                            }
                            
                            break;
                           case 110:                           
                              isActSet = true;
                                setspl++;
                            
                            
                            break;
                    }

                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
  //private void setupthucan() {
    //    for (int i = 0; i < 5; i++) {
      //      boolean isActSet = false;
     //       Item item = this.player.inventory.itemsBody.get(i);
       //     if (item.isNotNullItem()) {
        //          if (item.template.id > 567 || item.template.id < 555)
              
                      
        //               isActSet = true;
        //                    setthucan++;
         //                   break;
                      
            //                }
  //            
      //  }
    //               
           //     }
            
    
    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.setDHD = 0;
        this.worldcup = 0;
         this.setthucan = 0;
         this.setspl = 0;
         this.setts = 0;
        this.godClothes = false;
        this.ctHaiTac = -1;
         this.upthucan = -1;
    }

    public void dispose() {
        this.player = null;
    }
}
