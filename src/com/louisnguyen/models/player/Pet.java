package com.louisnguyen.models.player;

import com.girlkun.network.io.Message;
import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.models.mob.Mob;
import com.louisnguyen.models.skill.Skill;
import com.louisnguyen.server.Manager;
import com.louisnguyen.services.ItemTimeService;
import com.louisnguyen.services.MapService;
import com.louisnguyen.services.PlayerService;
import com.louisnguyen.services.Service;
import com.louisnguyen.services.SkillService;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.SkillUtil;
import com.louisnguyen.utils.TimeUtil;
import com.louisnguyen.utils.Util;

public class Pet extends Player {

    private static final short ARANGE_CAN_ATTACK = 300;
    private static final short ARANGE_ATT_SKILL1 = 70;

    private static final short[][] PET_ID = { { 285, 286, 287 }, { 288, 289, 290 }, { 282, 283, 284 },
            { 304, 305, 303 } };

    public static final byte FOLLOW = 0;
    public static final byte PROTECT = 1;
    public static final byte ATTACK = 2;
    public static final byte GOHOME = 3;
    public static final byte FUSION = 4;

    public Player master;
    public byte status = 0;

    public byte typePet;
    public boolean isTransform;

    public long lastTimeDie;

    private boolean goingHome;

    private Mob mobAttack;
    private Player playerAttack;

    private static final int TIME_WAIT_AFTER_UNFUSION = 5000;
    private long lastTimeUnfusion;

    public byte getStatus() {
        return this.status;
    }

    public Pet(Player master) {
        this.master = master;
        this.isPet = true;
    }

    public void changeStatus(byte status) {
        if (goingHome || master.fusion.typeFusion != 0 || (this.isDie() && status == FUSION)) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        Service.getInstance().chatJustForMe(master, this, getTextStatus(status));
        if (status == GOHOME) {
            goHome();
        } else if (status == FUSION) {
            fusion(false);
        }
        this.status = status;
    }

    public void joinMapMaster() {
        if (status != GOHOME && status != FUSION && !isDie()) {
            this.location.x = master.location.x + Util.nextInt(-10, 10);
            this.location.y = master.location.y;
            ChangeMapService.gI().goToMap(this, master.zone);
            this.zone.load_Me_To_Another(this);
        }
    }

    public void goHome() {
        if (this.status == GOHOME) {
            return;
        }
        goingHome = true;
        new Thread(() -> {
            try {
                Pet.this.status = Pet.ATTACK;
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            ChangeMapService.gI().goToMap(this, MapService.gI().getMapCanJoin(this, master.gender + 21, -1));
            this.zone.load_Me_To_Another(this);
            Pet.this.status = Pet.GOHOME;
            goingHome = false;
        }).start();
    }

    private String getTextStatus(byte status) {
        switch (status) {
            case FOLLOW:
                return "Ok con theo sư phụ";
            case PROTECT:
                return "Ok con sẽ bảo vệ sư phụ";
            case ATTACK:
                return "Ok con sẽ đi đám bọn nó";
            case GOHOME:
                return "Ok con về nhà đây";
            default:
                return "";
        }
    }

    public void fusion(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                        Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion2(boolean porata2) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata2) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA2;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                        Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void unFusion() {
        master.fusion.typeFusion = 0;
        this.status = PROTECT;
        Service.gI().point(master);
        joinMapMaster();
        fusionEffect(master.fusion.typeFusion);
        Service.gI().Send_Caitrang(master);
        Service.gI().point(master);
        this.lastTimeUnfusion = System.currentTimeMillis();
    }

    private void fusionEffect(int type) {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) master.id);
            Service.gI().sendMessAllPlayerInMap(master, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public long lastTimeMoveIdle;
    private int timeMoveIdle;
    public boolean idle;

    private void moveIdle() {
        if (status == GOHOME || status == FUSION) {
            return;
        }
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - master.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, master.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), master.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    private long lastTimeMoveAtHome;
    private byte directAtHome = -1;

    @Override
    public void update() {
        try {
            super.update();
            increasePoint(); // cộng chỉ số
            updatePower(); // check mở skill...
            if (isDie()) {
                if (System.currentTimeMillis() - lastTimeDie > 50000) {
                    Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
                } else {
                    return;
                }
            }

            if (justRevived && this.zone == master.zone) {
                Service.gI().chatJustForMe(master, this, "Sư phụ ơi, con đây nè!");
                justRevived = false;
            }

            if (this.zone == null || this.zone != master.zone) {
                joinMapMaster();
            }
            if (master.isDie() || this.isDie() || effectSkill.isHaveEffectSkill()) {
                return;
            }

            moveIdle();
            switch (status) {
                case FOLLOW:
                    followMaster(60);
                    break;
                case PROTECT:
                    if (useSkill3() || useSkill4() || useSkill5() || useSkill6() || useSkill7()) {
                        // System.out.println("Dùng chiêu 3-4");
                        break;
                    }
                    mobAttack = findMobAttack();
                    if (mobAttack != null) {
                        int disToMob = Util.getDistance(this, mobAttack);
                        if (disToMob <= ARANGE_ATT_SKILL1) {
                            // đấm
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-60, 60),
                                            mobAttack.location.y);
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            // chưởng
                            this.playerSkill.skillSelect = getSkill(2);
                            if (this.playerSkill.skillSelect.skillId != -1) {
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            }
                        }

                    } else {
                        idle = true;
                    }

                    break;
                case ATTACK:
                    if (useSkill3() || useSkill4() || useSkill5()) {
                        break;
                    }
                    mobAttack = findMobAttack();
                    if (mobAttack != null) {
                        int disToMob = Util.getDistance(this, mobAttack);
                        if (disToMob <= ARANGE_ATT_SKILL1) {
                            this.playerSkill.skillSelect = getSkill(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20),
                                            mobAttack.location.y);
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                } else {
                                    askPea();
                                }
                            }
                        } else {
                            this.playerSkill.skillSelect = getSkill(2);
                            if (this.playerSkill.skillSelect.skillId != -1) {
                                if (SkillService.gI().canUseSkillWithMana(this)) {
                                    SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                }
                            } else {
                                this.playerSkill.skillSelect = getSkill(1);
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        PlayerService.gI().playerMove(this,
                                                mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            }
                        }

                    } else {
                        idle = true;
                    }
                    break;

                case GOHOME:
                    if (this.zone != null
                            && (this.zone.map.mapId == 21 || this.zone.map.mapId == 22 || this.zone.map.mapId == 23)) {
                        if (System.currentTimeMillis() - lastTimeMoveAtHome <= 5000) {
                            return;
                        } else {
                            if (this.zone.map.mapId == 21) {
                                if (directAtHome == -1) {

                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 500, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 452, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            }
                            Service.gI().chatJustForMe(master, this, "Bibi sư phụ !");
                            lastTimeMoveAtHome = System.currentTimeMillis();
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            // Logger.logException(Pet.class, e);
        }
    }

    private long lastTimeAskPea;

    public void askPea() {
        if (Util.canDoWithTime(lastTimeAskPea, 10000)) {
            Service.gI().chatJustForMe(master, this, "Sư phụ ơi cho con đậu thần đi, con đói sắp chết rồi !!");
            lastTimeAskPea = System.currentTimeMillis();
        }
    }

    private int countTTNL;

    private boolean useSkill3() {
        try {
            // System.out.println("Đệ dùng chiêu 3");
            playerSkill.skillSelect = getSkill(3);
            if (playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.THAI_DUONG_HA_SAN:
                    if (SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        Service.gI().chat(this, "Thái dương hạ san");
                        return true;
                    }
                    return false;
                case Skill.TAI_TAO_NANG_LUONG:
                    if (this.effectSkill.isCharging && this.countTTNL < Util.nextInt(3, 5)) {
                        this.countTTNL++;
                        return true;
                    }
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)
                            && (this.nPoint.getCurrPercentHP() <= 20 || this.nPoint.getCurrPercentMP() <= 20)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        Service.gI().chat(this, "Tái tạo năng lượng");
                        this.countTTNL = 0;
                        return true;
                    }
                    return false;

                case Skill.TRI_THUONG:
                    if (pet.nPoint.getCurrPercentHP() < 30 &&
                            SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, master, null, null);
                        Service.gI().chat(this, "Cứu người , cứu người");
                        return true;
                    }
                    return false;

                default:
                    return false;
            }
        } catch (

        Exception e) {
            return false;
        }
    }

    private boolean useSkill4() {
        try {
            // System.out.println("Đệ dùng chiêu 4");
            this.playerSkill.skillSelect = getSkill(4);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.BIEN_KHI:
                    if (!this.effectSkill.isMonkey && SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        Service.gI().chat(this, "Biến khỉ , tránh ra không pem giờ!!!");
                        return true;
                    }
                    return false;
                case Skill.DE_TRUNG:
                    if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        Service.gI().chat(this, "Á đờ , con trai đâu , pem nó!!!");
                        return true;
                    }
                    return false;
                case Skill.KAIOKEN:
                    if (SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        mobAttack = this.findMobAttack();
                        if (mobAttack == null) {
                            return false;
                        }
                        int dis = Util.getDistance(this, mobAttack);
                        if (dis > ARANGE_ATT_SKILL1) {
                            PlayerService.gI().playerMove(this, mobAttack.location.x, mobAttack.location.y);
                        } else {
                            if (SkillService.gI().canUseSkillWithCooldown(this)
                                    && SkillService.gI().canUseSkillWithMana(this)) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20),
                                        mobAttack.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        Service.gI().chat(this, "Songoku xuất thế!!!");
                        getSkill(4).lastTimeUseThisSkill = System.currentTimeMillis();
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // ========================BETA SKILL5=====================
    private boolean useSkill5() {
        try {
            // System.out.println("Đệ dùng chiêu 5");
            this.playerSkill.skillSelect = getSkill(5);
            if (this.playerSkill.skillSelect.skillId == -1) {
                // System.out.println("Không tìm thấy chiêu 5");
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.QUA_CAU_KENH_KHI:
                    if (SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        mobAttack = this.findMobAttack();
                        if (mobAttack == null) {
                            return false;
                        }
                        int dis = Util.getDistance(this, mobAttack);
                        if (dis > ARANGE_ATT_SKILL1) {
                            if (SkillService.gI().canUseSkillWithCooldown(this)
                                    && SkillService.gI().canUseSkillWithMana(this)) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20),
                                        mobAttack.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        Service.gI().chat(this, "Biến khỉ , kênh khi!!!");
                        PlayerService.gI().playerMove(this, master.location.x + Util.nextInt(-20, 20),
                                master.location.y);
                        getSkill(5).lastTimeUseThisSkill = System.currentTimeMillis();

                        return true;
                    }
                    return false;
                // if (!this.effectSkill.isThoiMien &&
                // SkillService.gI().canUseSkillWithCooldown(this) &&
                // SkillService.gI().canUseSkillWithMana(this)) {
                // SkillService.gI().useSkill(this, null, null, null);
                // return true;
                // }
                // return false;
                case Skill.TU_SAT:
                    if (this.nPoint.getCurrPercentHP() <= 10) {
                        if (SkillService.gI().canUseSkillWithCooldown(this)
                                && SkillService.gI().canUseSkillWithMana(this)) {
                            mobAttack = this.findMobAttack();
                            if (mobAttack == null) {
                                return false;
                            }
                            int dis = Util.getDistance(this, mobAttack);
                            if (dis > ARANGE_ATT_SKILL1) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x, mobAttack.location.y);
                            } else {
                                if (SkillService.gI().canUseSkillWithCooldown(this)
                                        && SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20),
                                            mobAttack.location.y);
                                }
                            }
                            Service.gI().chat(this, "Kích kích kích kích kích .... Boom!!!");
                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                            getSkill(5).lastTimeUseThisSkill = System.currentTimeMillis();
                            return true;
                        }
                    }
                    return false;
                // if (!this.effectSkill.isBlindDCTT &&
                // SkillService.gI().canUseSkillWithCooldown(this) &&
                // SkillService.gI().canUseSkillWithMana(this)) {
                // SkillService.gI().useSkill(this, null, null, null);
                // return true;
                // }
                // return false;
                case Skill.MAKANKOSAPPO:
                    if (SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().canUseSkillWithMana(this)) {
                        mobAttack = this.findMobAttack();
                        if (mobAttack == null) {
                            return false;
                        }
                        int dis = Util.getDistance(this, mobAttack);
                        if (dis > ARANGE_ATT_SKILL1) {
                            if (SkillService.gI().canUseSkillWithCooldown(this)
                                    && SkillService.gI().canUseSkillWithMana(this)) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-100, 100),
                                        mobAttack.location.y);
                            }
                        }
                        Service.gI().chat(this, "Laze doẹt doẹt doẹt doẹt ....!!!");
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        PlayerService.gI().playerMove(this, master.location.x + Util.nextInt(-20, 20),
                                master.location.y);
                        getSkill(2).lastTimeUseThisSkill = System.currentTimeMillis();
                        return true;
                    }
                    return false;
                // if (this.effectSkill.isSocola &&
                // SkillService.gI().canUseSkillWithCooldown(this) &&
                // SkillService.gI().canUseSkillWithMana(this)) {
                // SkillService.gI().useSkill(this, null, null, null);
                // return true;
                // }
                // return false;
                // case Skill.DE_TRUNG:
                // if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this)
                // && SkillService.gI().canUseSkillWithMana(this)) {
                // SkillService.gI().useSkill(this, null, null, null);
                // return true;
                // }
                // return false;
                // case Skill.QUA_CAU_KENH_KHI:
                // if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this)
                // && SkillService.gI().canUseSkillWithMana(this)) {
                // SkillService.gI().useSkill(this, null, null, null);
                // return true;
                // }
                // return false;
                default:
                    // System.out.println("Có chiêu nhưng không hiểu sao không dùng!");
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }//

    private boolean useSkill6() {
        try {
            this.playerSkill.skillSelect = getSkill(6);
            if (this.playerSkill.skillSelect.skillId != -1) {
                switch (this.playerSkill.skillSelect.template.id) {
                    case Skill.LIEN_HOAN:
                        if (SkillService.gI().canUseSkillWithCooldown(this)
                                && SkillService.gI().canUseSkillWithMana(this)) {
                            mobAttack = this.findMobAttack();
                            if (mobAttack == null) {
                                return false;
                            }
                            int dis = Util.getDistance(this, mobAttack);
                            if (dis > ARANGE_ATT_SKILL1) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x, mobAttack.location.y);
                            } else {
                                if (SkillService.gI().canUseSkillWithCooldown(this)
                                        && SkillService.gI().canUseSkillWithMana(this)) {
                                    PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20),
                                            mobAttack.location.y);
                                }
                            }
                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                            Service.gI().chat(this, "Siêu cấp liên hoàn chưởng!!!");
                            getSkill(6).lastTimeUseThisSkill = System.currentTimeMillis();
                            return true;
                        }
                        return false;
                    case Skill.DICH_CHUYEN_TUC_THOI:
                        if (!this.effectSkill.isBlindDCTT && SkillService.gI().canUseSkillWithCooldown(this)
                                && SkillService.gI().canUseSkillWithMana(this)) {
                            mobAttack = this.findMobAttack();
                            if (mobAttack == null) {
                                return false;
                            }
                            // int dis = Util.getDistance(this, mobAttack);
                            // if (dis > ARANGE_ATT_SKILL1) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x,
                            // mobAttack.location.y);
                            // } else {
                            // if (SkillService.gI().canUseSkillWithCooldown(this)
                            // && SkillService.gI().canUseSkillWithMana(this)) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20,
                            // 20),
                            // mobAttack.location.y);
                            // }
                            // }
                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                            Service.gI().chat(this, "Dịch chuyển tức thời!!!");
                            PlayerService.gI().playerMove(this, master.location.x + Util.nextInt(-20, 20),master.location.y);
                            getSkill(6).lastTimeUseThisSkill = System.currentTimeMillis();
                            return true;
                        }
                        return false;
                    case Skill.TROI:
                        if (!this.effectSkill.isBlindDCTT && SkillService.gI().canUseSkillWithCooldown(this)
                                && SkillService.gI().canUseSkillWithMana(this)) {
                            mobAttack = this.findMobAttack();
                            if (mobAttack == null) {
                                return false;
                            }
                            // int dis = Util.getDistance(this, mobAttack);
                            // if (dis > ARANGE_ATT_SKILL1) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x,
                            // mobAttack.location.y);
                            // } else {
                            // if (SkillService.gI().canUseSkillWithCooldown(this)
                            // && SkillService.gI().canUseSkillWithMana(this)) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20,
                            // 20),
                            // mobAttack.location.y);
                            // }
                            // }
                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                            Service.gI().chat(this, "Trói!!! Con trói rồi , sư phụ pem nó đi!!");
                            getSkill(6).lastTimeUseThisSkill = System.currentTimeMillis();
                            return true;
                        }
                        return false;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean useSkill7() {
        // case Skill.KHIEN_NANG_LUONG:
        // if (!this.effectSkill.isShielding &&
        // SkillService.gI().canUseSkillWithCooldown(this)
        // && SkillService.gI().canUseSkillWithMana(this)) {
        // SkillService.gI().useSkill(this, null, null, null);
        // return true;
        // }
        // return false;
        try {
            this.playerSkill.skillSelect = getSkill(7);
            if (this.playerSkill.skillSelect.skillId != -1) {
                switch (this.playerSkill.skillSelect.template.id) {
                    case Skill.HUYT_SAO:
                        if (SkillService.gI().canUseSkillWithCooldown(this)
                                && SkillService.gI().canUseSkillWithMana(this)) {
                            // mobAttack = this.findMobAttack();
                            // if (mobAttack == null) {
                            // return false;
                            // }
                            // int dis = Util.getDistance(this, mobAttack);
                            // if (dis > ARANGE_ATT_SKILL1) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x,
                            // mobAttack.location.y);
                            // } else {
                            // if (SkillService.gI().canUseSkillWithCooldown(this)
                            // && SkillService.gI().canUseSkillWithMana(this)) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20,
                            // 20),
                            // mobAttack.location.y);
                            // }
                            // }
                            SkillService.gI().useSkill(this, null, null, null);
                            Service.gI().chat(this, "Huýt sáo : huýt huýt huýt hú hú hú!!!");
                            getSkill(7).lastTimeUseThisSkill = System.currentTimeMillis();
                            return true;
                        }
                        return false;
                    case Skill.THOI_MIEN:
                        if (!this.effectSkill.isThoiMien && SkillService.gI().canUseSkillWithCooldown(this)
                                && SkillService.gI().canUseSkillWithMana(this)) {
                            mobAttack = this.findMobAttack();
                            if (mobAttack == null) {
                                return false;
                            }
                            // int dis = Util.getDistance(this, mobAttack);
                            // if (dis > ARANGE_ATT_SKILL1) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x,
                            // mobAttack.location.y);
                            // } else {
                            // if (SkillService.gI().canUseSkillWithCooldown(this)
                            // && SkillService.gI().canUseSkillWithMana(this)) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20,
                            // 20),
                            // mobAttack.location.y);
                            // }
                            // }
                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                            Service.gI().chat(this, "Thôi miên!!!");
                            getSkill(7).lastTimeUseThisSkill = System.currentTimeMillis();
                            return true;
                        }
                        return false;
                    case Skill.SOCOLA:
                        if (!this.effectSkill.isSocola && SkillService.gI().canUseSkillWithCooldown(this)
                                && SkillService.gI().canUseSkillWithMana(this)) {
                            mobAttack = this.findMobAttack();
                            if (mobAttack == null) {
                                return false;
                            }
                            // int dis = Util.getDistance(this, mobAttack);
                            // if (dis > ARANGE_ATT_SKILL1) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x,
                            // mobAttack.location.y);
                            // } else {
                            // if (SkillService.gI().canUseSkillWithCooldown(this)
                            // && SkillService.gI().canUseSkillWithMana(this)) {
                            // PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20,
                            // 20),
                            // mobAttack.location.y);
                            // }
                            // }
                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                            Service.gI().chat(this, "Úm ba la hô biến sôcôla!!");
                            getSkill(7).lastTimeUseThisSkill = System.currentTimeMillis();
                            return true;
                        }
                        return false;
                    case Skill.KHIEN_NANG_LUONG:
                        if (!this.effectSkill.isShielding &&
                                SkillService.gI().canUseSkillWithCooldown(this)
                                && SkillService.gI().canUseSkillWithMana(this)) {
                            SkillService.gI().useSkill(this, null, null, null);
                            return true;
                        }
                        return false;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    // ====================================================
    private long lastTimeIncreasePoint;

    private void increasePoint() {
        if (this.nPoint != null && Util.canDoWithTime(lastTimeIncreasePoint, 1)) {
            if (Util.isTrue(1, 100)) {
                this.nPoint.increasePoint((byte) 2, (short) 1);
            } else {
                byte type = (byte) Util.nextInt(0, 2);
                short point = (short) Util.nextInt(Manager.RATE_EXP_SERVER);
                this.nPoint.increasePoint(type, point);
            }
            lastTimeIncreasePoint = System.currentTimeMillis();
        }
    }

    public void followMaster() {
        if (this.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.status) {
            case ATTACK:
                if ((mobAttack != null && Util.getDistance(this, master) <= 1500)) {
                    break;
                }
            case FOLLOW:
            case PROTECT:
                followMaster(60);
                break;
        }
    }

    private void followMaster(int dis) {
        int mX = master.location.x;
        int mY = master.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(0, dis);
            } else {
                this.location.x = mX + Util.nextInt(0, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }

    public short getAvatar() {
        if (this.typePet == 1) {
            return 297;
        } else if (this.typePet == 2) {
            return 508;
        } else if (this.typePet == 3) {
            return 946;
        } else {
            return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getHead() {
        if (effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill.isSocola) {
            return 412;
        } else if (this.typePet == 1) {
            return 297;
        } else if (this.typePet == 2) {
            return 508;
        } else if (this.typePet == 3) {
            return 946;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            int part = inventory.itemsBody.get(5).template.head;
            if (part != -1) {
                return (short) part;
            }
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][0];
        } else {
            return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getBody() {
        if (effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill.isSocola) {
            return 413;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 298;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 509;
        } else if (this.typePet == 3 && !this.isTransform) {
            return 947;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][1];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
        }
    }

    @Override
    public short getLeg() {
        if (effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill.isSocola) {
            return 414;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 299;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 510;
        } else if (this.typePet == 3 && !this.isTransform) {
            return 948;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }

        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][2];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 60 : 58);
        }
    }

    private Mob findMobAttack() {
        int dis = ARANGE_CAN_ATTACK;
        Mob mobAtt = null;
        for (Mob mob : zone.mobs) {
            if (mob.isDie()) {
                continue;
            }
            int d = Util.getDistance(this, mob);
            if (d <= dis) {
                dis = d;
                mobAtt = mob;
            }
        }
        return mobAtt;
    }

    // Sức mạnh mở skill đệ
    private void updatePower() {
        if (this.playerSkill != null) {
            switch (this.playerSkill.getSizeSkill()) {
                case 1:
                    if (this.nPoint.power >= 150000000) {
                        openSkill2();
                    }
                    break;
                case 2:
                    if (this.nPoint.power >= 1500000000) {
                        openSkill3();
                    }
                    break;
                case 3:
                    if (this.nPoint.power >= 20000000000L) {
                        openSkill4();
                    }
                    break;

                case 4:
                    if (this.nPoint.power >= 30000000000L) {
                        openSkill5();
                    }
                    break;
                case 5:
                    if (this.nPoint.power >= 50000000000L) {
                        openSkill6();
                    }
                    break;
                case 6:
                    if (this.nPoint.power >= 80000000000L) {
                        openSkill7();
                    }
                    break;
            }
        }
    }

    public void openSkill2() {
        Skill skill = null;
        int tiLeKame = 40;
        int tiLeMasenko = 30;
        int tiLeAntomic = 30;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeKame) {
            skill = SkillUtil.createSkill(Skill.KAMEJOKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko) {
            skill = SkillUtil.createSkill(Skill.MASENKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko + tiLeAntomic) {
            skill = SkillUtil.createSkill(Skill.ANTOMIC, 1);
        }
        skill.coolDown = 1000;
        this.playerSkill.skills.set(1, skill);
    }

    public void openSkill3() {
        Skill skill = null;
        int tiLeTDHS = 30;
        int tiLeTTNL = 30;
        int tiLeTriThuong = 40;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeTDHS) {
            skill = SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL) {
            skill = SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL + tiLeTriThuong) {
            skill = SkillUtil.createSkill(Skill.TRI_THUONG, 1);
        }
        this.playerSkill.skills.set(2, skill);
    }

    private void openSkill4() {
        Skill skill = null;
        int tiLeBienKhi = 30;
        int tiLeDeTrung = 30;
        int tiLeKok = 40;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeBienKhi) {
            skill = SkillUtil.createSkill(Skill.BIEN_KHI, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung) {
            skill = SkillUtil.createSkill(Skill.DE_TRUNG, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung + tiLeKok) {
            skill = SkillUtil.createSkill(Skill.KAIOKEN, 1);
        }
        this.playerSkill.skills.set(3, skill);
    }

    private void openSkill5() {
        Skill skill = null;
        int tiLeQckk = 10; // khi
        int tiLeBoomHs = 70; // detrung
        int tiLeLaze = 20; // khienNl
        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeQckk) {
            skill = SkillUtil.createSkill(Skill.QUA_CAU_KENH_KHI, 1);
        } else if (rd <= tiLeQckk + tiLeBoomHs) {
            skill = SkillUtil.createSkill(Skill.MAKANKOSAPPO, 1);
        } else if (rd <= tiLeBoomHs + tiLeLaze + tiLeQckk) {
            skill = SkillUtil.createSkill(Skill.TU_SAT, 1);
        }
        this.playerSkill.skills.set(4, skill);
    }

    private void openSkill6() {
        Skill skill = null;
        int tiLeLienHoan = 40; // khi
        int tiLeTroi = 30; // detrung
        int tiLeDctt = 30; // khienNl
        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeDctt) {
            skill = SkillUtil.createSkill(Skill.DICH_CHUYEN_TUC_THOI, 1);
        } else if (rd <= tiLeTroi + tiLeDctt) {
            skill = SkillUtil.createSkill(Skill.TROI, 1);
        } else if (rd <= tiLeTroi + tiLeDctt + tiLeLienHoan) {
            skill = SkillUtil.createSkill(Skill.LIEN_HOAN, 1);
        }
        this.playerSkill.skills.set(5, skill);
    }

    private void openSkill7() {
        Skill skill = null;
        int tiLeThoiMien = 10; // khi
        int tiLeHuytSao = 20; // detrung
        int tiLeSocola = 30; // khienNl
        int tiLeKhien = 40;
        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeThoiMien) {
            skill = SkillUtil.createSkill(Skill.THOI_MIEN, 1);
        } else if (rd <= tiLeThoiMien + tiLeHuytSao) {
            skill = SkillUtil.createSkill(Skill.HUYT_SAO, 1);
        } else if (rd <= tiLeThoiMien + tiLeHuytSao + tiLeSocola) {
            skill = SkillUtil.createSkill(Skill.SOCOLA, 1);
        } else {
            skill = SkillUtil.createSkill(Skill.KHIEN_NANG_LUONG, 1);
        }
        this.playerSkill.skills.set(6, skill);
    }

    // ========================================================

    private Skill getSkill(int indexSkill) {
        return this.playerSkill.skills.get(indexSkill - 1);
    }

    public void transform() {
        if (this.typePet == 1) {
            this.isTransform = !this.isTransform;
            Service.gI().Send_Caitrang(this);
            Service.gI().chat(this, "Bư..Bư..Ma..Nhân..Bư....");
        }
        if (this.typePet == 2) {
            this.isTransform = !this.isTransform;
            Service.gI().Send_Caitrang(this);
            Service.gI().chat(this, "72 phép thần thông , biến !!!!");
        }
        if (this.typePet == 3) {
            this.isTransform = !this.isTransform;
            Service.gI().Send_Caitrang(this);
            Service.gI().chat(this, "Henshin");
        }
    }

    @Override
    public void dispose() {
        this.mobAttack = null;
        this.master = null;
        super.dispose();
    }
}
