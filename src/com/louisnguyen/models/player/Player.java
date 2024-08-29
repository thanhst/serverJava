package com.louisnguyen.models.player;

import java.util.ArrayList;
import java.util.List;

import com.girlkun.network.io.Message;
import com.louisnguyen.card.Card;
import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.consts.ConstTask;
import com.louisnguyen.data.DataGame;
import com.louisnguyen.models.BangHoi.Clan;
import com.louisnguyen.models.BangHoi.ClanMember;
import com.louisnguyen.models.NhiemVu.TaskPlayer;
import com.louisnguyen.models.intrinsic.IntrinsicPlayer;
import com.louisnguyen.models.item.Item;
import com.louisnguyen.models.item.ItemTime;
import com.louisnguyen.models.map.TrapMap;
import com.louisnguyen.models.map.Zone;
import com.louisnguyen.models.map.DoanhTraiDocNhan.DoanhTraiService;
import com.louisnguyen.models.map.TranhNgocSaoDen.BlackBallWar;
import com.louisnguyen.models.map.bandokhobau.BanDoKhoBauService;
import com.louisnguyen.models.matches.IPVP;
import com.louisnguyen.models.matches.TYPE_LOSE_PVP;
import com.louisnguyen.models.mob.MobMe;
import com.louisnguyen.models.npc.specialnpc.BillEgg;
import com.louisnguyen.models.npc.specialnpc.MabuEgg;
import com.louisnguyen.models.npc.specialnpc.MagicTree;
import com.louisnguyen.models.skill.PlayerSkill;
import com.louisnguyen.models.skill.Skill;
import com.louisnguyen.server.Client;
import com.louisnguyen.server.io.MySession;
import com.louisnguyen.services.EffectSkillService;
import com.louisnguyen.services.FriendAndEnemyService;
import com.louisnguyen.services.PetService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.TaskService;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.services.func.ChonAiDay;
import com.louisnguyen.services.func.CombineNew;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.Util;

public class Player {

    public MySession session;

    public boolean beforeDispose;

    public boolean isPet;
    public boolean isNewPet;
    public boolean isBoss;
    public int NguHanhSonPoint = 0;

    public int HoiSinhDaiChienBangHoi = 5;
    
    public int LuotGoiRongThan = 3;

    public int ThamGiaDaiChienBangHoi = 0;

    public IPVP pvp;
    public int pointPvp;
    public byte maxTime = 30;
    public byte type = 0;

    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public List<Zone> mapMaBu;
    public long limitgold = 0;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Pet pet;
    public NewPet newpet;
    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public BillEgg billEgg;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public EffectFlagBag effectFlagBag;
    public FightMabu fightMabu;
    public SkillSpecial skillSpecial;
    public Clan clan;
    public ClanMember clanMember;
    
    //BDKB mới
    public int bdkb_countPerDay;
    public long bdkb_lastTimeJoin;
    public boolean bdkb_isJoinBdkb;

    public List<Friend> friends;
    public List<Enemy> enemies;

    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember;
    public short head;

    public byte typePk;

    public byte cFlag;

    public boolean haveTennisSpaceShip;

    public boolean justRevived;
    public long lastTimeRevived;

    public int violate;
    public byte totalPlayerViolate;
    public long timeChangeZone;
    public long lastTimeUseOption;

    public short idNRNM = -1;
    public short idGo = -1;
    public long lastTimePickNRNM;
    public int goldNormar;
    public int goldVIP;
    public long lastTimeWin;
    public boolean isWin;
    public List<Card> Cards = new ArrayList<>();
    public short idAura = -1;
    public int vnd;
    public int VND;
    public int levelWoodChest;
    public boolean receivedWoodChest;
    public int goldChallenge;
    public int gemChallenge;
//    public boolean bdkb_isJoinBdkb;
    public boolean kghd_isJoinKghd;

    public boolean titleitem;

    public boolean titlett;
    public boolean isTitleUse;
    public long lastTimeTitle1;
    public boolean isTitleUse2;
    public long lastTimeTitle2;
    public boolean isTitleUse3;
    public long lastTimeTitle3;

    public Player() {
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag();
        fightMabu = new FightMabu(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        charms = new Charms();
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
        skillSpecial = new SkillSpecial(this);
    }

    //--------------------------------------------------------------------------
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    public void setSession(MySession session) {
        this.session = session;
    }

    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    public MySession getSession() {
        return this.session;
    }

    public boolean isPl() {
        return !isPet && !isBoss && !isNewPet;
    }

    public void update() {
        if (!this.beforeDispose) {
            try {
                if (!iDMark.isBan()) {

                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkin != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (newpet != null) {
                        newpet.update();
                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if (this.lastTimeTitle1 != 0 && Util.canDoWithTime(this.lastTimeTitle1, 6000)) {
                        lastTimeTitle1 = 0;
                        isTitleUse = false;
                    }
                    if (this.lastTimeTitle2 != 0 && Util.canDoWithTime(this.lastTimeTitle2, 6000)) {
                        lastTimeTitle2 = 0;
                        isTitleUse2 = false;
                    }

                    if (this.lastTimeTitle3 != 0 && Util.canDoWithTime(this.lastTimeTitle3, 6000)) {
                        lastTimeTitle3 = 0;
                        isTitleUse3 = false;
                    }
                    BanDoKhoBauService.gI().updatePlayer(this);
                    DoanhTraiService.gI().updatePlayer(this);
                    BlackBallWar.gI().update(this);
                    if (!isBoss && this.iDMark.isGotoFuture() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.iDMark.setGotoFuture(false);
                    } if (this.iDMark.isGoToBDKB() && Util.canDoWithTime(this.iDMark.getLastTimeGoToBDKB(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                        this.iDMark.setGoToBDKB(false);
                    } else if (!isBoss && this.iDMark.isGoToKGHD() && Util.canDoWithTime(this.iDMark.getLastTimeGoToKGHD(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 149, -1, Util.nextInt(60, 200));
                        this.iDMark.setGoToKGHD(false);
                    } else if (!isBoss && this.iDMark.isGoToCDRD() && Util.canDoWithTime(this.iDMark.getLastTimeGoToKGHD(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 143, -1, 1090);
                        this.iDMark.setGoToCDRD(false);
                    }
                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                    if (this.isPl() && this.inventory.itemsBody.get(8) != null) {
                        Item it = this.inventory.itemsBody.get(8);
                        if (it != null && it.isNotNullItem() && this.newpet == null) {
                            switch (it.template.id) {
                                case 892:
                                    PetService.Pet2(this, 882, 883, 884);
                                    Service.gI().point(this);
                                    break;
                                case 893:
                                    PetService.Pet2(this, 885, 886, 887);
                                    Service.gI().point(this);
                                    break;
                                case 909:
                                    PetService.Pet2(this, 897, 898, 899);
                                    Service.gI().point(this);
                                    break;
                                case 942:
                                    PetService.Pet2(this, 966, 967, 968);
                                    Service.gI().point(this);
                                    break;
                                case 943:
                                    PetService.Pet2(this, 969, 970, 971);
                                    Service.gI().point(this);
                                    break;
                                case 944:
                                    PetService.Pet2(this, 972, 973, 974);
                                    Service.gI().point(this);
                                    break;
                                case 967:
                                    PetService.Pet2(this, 1050, 1051, 1052);
                                    Service.gI().point(this);
                                    break;
                                case 1407: // Con cún vàng
                                    PetService.Pet2(this, 663, 664, 665);
                                    Service.gI().point(this);
                                    break;
                                case 1408: // Cua đỏ
                                    PetService.Pet2(this, 1074, 1075, 1076);
                                    Service.gI().point(this);
                                    break;
                                case 1410: // Bí ma vương
                                    PetService.Pet2(this, 1155, 1156, 1157);
                                    Service.gI().point(this);
                                    break;
                                case 1411: //Mèo đuôi vàng đen
                                    PetService.Pet2(this, 1183, 1184, 1185);
                                    Service.gI().point(this);
                                    break;
                                case 1412: //Mèo đuôi vàng trắng
                                    PetService.Pet2(this, 1201, 1202, 1203);
                                    Service.gI().point(this);
                                    break;
                                case 1413: //Gà 9 cựa
                                    PetService.Pet2(this, 1239, 1240, 1241);
                                    Service.gI().point(this);
                                    break;
                                case 1414: //Ngựa 9 hồng mao
                                    PetService.Pet2(this, 1242, 1243, 1244);
                                    Service.gI().point(this);
                                    break;
                                case 1415: //Voi 9 ngà
                                    PetService.Pet2(this, 1245, 1246, 1247);
                                    Service.gI().point(this);
                                    break;
                                case 1416: //Minions
                                    PetService.Pet2(this, 1254, 1255, 1256);
                                    Service.gI().point(this);
                                    break;
                            }
                        }
                    } else if (this.isPl() && newpet != null && !this.inventory.itemsBody.get(8).isNotNullItem()) {
                        ChangeMapService.gI().exitMap(newpet);
                        newpet.dispose();
                        newpet = null;
                    }
                    if (this.isPl() && isWin && this.zone.map.mapId == 51 && Util.canDoWithTime(lastTimeWin, 2000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 52, 0, -1);
                        isWin = false;
                    }
                    if (location.lastTimeplayerMove < System.currentTimeMillis() - 30 * 60 * 1000) {
                        Client.gI().kickSession(getSession());
                    }
                } else {
                    if (Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
                Logger.logException(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    //--------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     */
    private static final short[][] idOutfitFusion = {
        {380, 381, 382}, //luong long
        {383, 384, 385},// porata 
        {391, 392, 393}, //hop the chung namec
        {870, 871, 872},//trai dat c2
        {873, 874, 875}, //namec c2
        {867, 868, 869}, //xayda c2
        {1151, 1152, 1153},//trai dat c2
        {1154, 1155, 1156},//Namec c2
        {1157, 1158, 1159},//Xayda c2
    };

    public byte getEffFront() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
            if (this.inventory.itemsBody.get(9).isNotNullItem()) {
               if (this.inventory.itemsBody.get(9).template.id == (1980+7)){
                    return (byte) 210;
               }
            }

        int levelAo = 0;
        Item.ItemOption optionLevelAo = null;
        int levelQuan = 0;
        Item.ItemOption optionLevelQuan = null;
        int levelGang = 0;
        Item.ItemOption optionLevelGang = null;
        int levelGiay = 0;
        Item.ItemOption optionLevelGiay = null;
        int levelNhan = 0;
        Item.ItemOption optionLevelNhan = null;
        Item itemAo = this.inventory.itemsBody.get(0);
        Item itemQuan = this.inventory.itemsBody.get(1);
        Item itemGang = this.inventory.itemsBody.get(2);
        Item itemGiay = this.inventory.itemsBody.get(3);
        Item itemNhan = this.inventory.itemsBody.get(4);
        for (Item.ItemOption io : itemAo.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelAo = io.param;
                optionLevelAo = io;
                break;
            }
        }
        for (Item.ItemOption io : itemQuan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelQuan = io.param;
                optionLevelQuan = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGang.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGang = io.param;
                optionLevelGang = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGiay.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGiay = io.param;
                optionLevelGiay = io;
                break;
            }
        }
        for (Item.ItemOption io : itemNhan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelNhan = io.param;
                optionLevelNhan = io;
                break;
            }
        }
        if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 8 && levelQuan >= 8 && levelGang >= 8 && levelGiay >= 8 && levelNhan >= 8) {
            return 8;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 7 && levelQuan >= 7 && levelGang >= 7 && levelGiay >= 7 && levelNhan >= 7) {
            return 7;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 6 && levelQuan >= 6 && levelGang >= 6 && levelGiay >= 6 && levelNhan >= 6) {
            return 6;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 5 && levelQuan >= 5 && levelGang >= 5 && levelGiay >= 5 && levelNhan >= 5) {
            return 5;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 4 && levelQuan >= 4 && levelGang >= 4 && levelGiay >= 4 && levelNhan >= 4) {
            return 4;
        } else {
            return -1;
        }
    }

    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (effectSkill.isBang) {
            return 1251;
        } else if (effectSkill.isDa) {
            return 454;
        } else if (effectSkill.isCarot) {
            return 406;
        } else if (effectSkill.isBinh) {
            return 2027;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                if (inventory != null && inventory.itemsBody.get(5).isNotNullItem() && inventory.itemsBody.get(5).isThanZeno()) {
                    return idOutfitFusion[6 + this.gender][0];
                }
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][0];
                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                   if (this.pet.typePet == 1) {
//                       return idOutfitFusion[3 + this.gender][0];
//                    }
                return idOutfitFusion[3 + this.gender][0];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int head = inventory.itemsBody.get(5).template.head;
            if (head != -1) {
                return (short) head;
            }
        }
        return this.head;
    }

    public short getBody() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (effectSkill.isBang) {
            return 1252;
        } else if (effectSkill.isDa) {
            return 455;
        } else if (effectSkill.isCarot) {
            return 407;
        } else if (effectSkill.isBinh) {
            return 2028;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                if (inventory != null && inventory.itemsBody.get(5).isNotNullItem() && inventory.itemsBody.get(5).isThanZeno()) {
                    return idOutfitFusion[6 + this.gender][1];
                }
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][1];
                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                ///  if (this.pet.typePet == 1) {
                // //      return idOutfitFusion[3 + this.gender][1];
                //    }
                return idOutfitFusion[3 + this.gender][1];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else if (effectSkill.isBang) {
            return 1253;
        } else if (effectSkill.isDa) {
            return 456;
        } else if (effectSkill.isCarot) {
            return 408;
        } else if (effectSkill.isBinh) {
            return 2029;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                if (inventory != null && inventory.itemsBody.get(5).isNotNullItem() && inventory.itemsBody.get(5).isThanZeno()) {
                    return idOutfitFusion[6 + this.gender][2];
                }
                if (this.pet.typePet == 1) {
                    return idOutfitFusion[3 + this.gender][2];
                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                //  if (this.pet.typePet == 1) {
                //      return idOutfitFusion[3 + this.gender][2];
                //  }
                return idOutfitFusion[3 + this.gender][2];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public byte getAura() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        if (this.nPoint.power >= 60999999999L && this.nPoint.power < 80999999999L) {
            return 10;
        }
        if (this.nPoint.power >= 80999999999L && this.nPoint.power < 179999999999L) {
            return 11;
        }
        if (this.nPoint.power >= 179999999999L && this.nPoint.power < 210999999999L) {
            return 14;
        }
        if (this.nPoint.power >= 210999999999L) {
            return 54;
        } else {
            return -1;
        }

    }

    public short getFlagBag() {
        if (this.iDMark.isHoldBlackBall()) {
            return 31;
        } else if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        if (this.inventory.itemsBody.size() == 11) {
            if (this.inventory.itemsBody.get(7).isNotNullItem()) {
                return this.inventory.itemsBody.get(7).template.part;

            }
        }
            if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
                return 28;
            }
            if (this.clan != null) {
                return (short) this.clan.imgId;
            }
            return -1;
        }

    

    public short getMount() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(10);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.type == 23) {
            if (item.template.gender == 3 || item.template.gender == this.gender) {
                return item.template.id;
            } else {
                return -1;
            }
        } else {
            if (item.template.id < 500) {
                return item.template.id;
            } else {
                return (short) DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
            }
        }

    }

    //--------------------------------------------------------------------------
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            com.louisnguyen.services.PlayerService.gI().hoiPhuc(this, 0, damage * this.nPoint.voHieuChuong / 100);
                            return 0;
                        }
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }

            this.nPoint.subHP(damage);
            if (isDie()) {
                if (this.zone.map.mapId == 112) {
//                    plAtt.pointPvp++;
                }
                setDie(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    protected void setDie(Player plAtt) {
        //xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.gI().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.gI().charDie(this);
        //add kẻ thù
        if (!this.isPet && !this.isNewPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isNewPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        //kết thúc pk
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }
//        PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
        BlackBallWar.gI().dropBlackBall(this);
    }

    //--------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public boolean isAdmin() {
        return this.session.isAdmin;
    }

    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    public void preparedToDispose() {
    }

    public void dispose() {
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (newpet != null) {
            newpet.dispose();
            newpet = null;
        }
        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }
        if (billEgg != null) {
            billEgg.dispose();
            billEgg = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }
        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (skillSpecial != null) {
            skillSpecial.dispose();
            skillSpecial = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (combineNew != null) {
            combineNew.dispose();
            combineNew = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (gift != null) {
            gift.dispose();
            gift = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (effectFlagBag != null) {
            effectFlagBag.dispose();
            effectFlagBag = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }
        effectFlagBag = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        name = null;
    }

    public String percentGold(int type) {
        try {
            if (type == 0) {
                double percent = ((double) this.goldNormar / ChonAiDay.gI().goldNormar) * 100;
                return String.valueOf(Math.ceil(percent));
            } else if (type == 1) {
                double percent = ((double) this.goldVIP / ChonAiDay.gI().goldVip) * 100;
                return String.valueOf(Math.ceil(percent));
            }
        } catch (ArithmeticException e) {
            return "0";
        }
        return "0";
    }

    public List<String> textRuongGo = new ArrayList<>();

}
