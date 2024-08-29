package com.louisnguyen.models.boss;

import lombok.Builder;
import lombok.Data;


@Data
public class BossData {

    public static final int DEFAULT_APPEAR = 0;
    public static final int APPEAR_WITH_ANOTHER = 1;
    public static final int ANOTHER_LEVEL = 2;

    public static int getDEFAULT_APPEAR() {
        return DEFAULT_APPEAR;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public void setOutfit(short[] outfit) {
        this.outfit = outfit;
    }

    public void setDame(int dame) {
        this.dame = dame;
    }

    public void setHp(int[] hp) {
        this.hp = hp;
    }

    public void setMapJoin(int[] mapJoin) {
        this.mapJoin = mapJoin;
    }

    public void setSkillTemp(int[][] skillTemp) {
        this.skillTemp = skillTemp;
    }

    public void setTextS(String[] textS) {
        this.textS = textS;
    }

    public void setTextM(String[] textM) {
        this.textM = textM;
    }

    public void setTextE(String[] textE) {
        this.textE = textE;
    }

    public void setSecondsRest(int secondsRest) {
        this.secondsRest = secondsRest;
    }

    public void setTypeAppear(TypeAppear typeAppear) {
        this.typeAppear = typeAppear;
    }

    public void setBossesAppearTogether(int[] bossesAppearTogether) {
        this.bossesAppearTogether = bossesAppearTogether;
    }

    public static int getAPPEAR_WITH_ANOTHER() {
        return APPEAR_WITH_ANOTHER;
    }

    public static int getANOTHER_LEVEL() {
        return ANOTHER_LEVEL;
    }

    public String getName() {
        return name;
    }

    public byte getGender() {
        return gender;
    }

    public short[] getOutfit() {
        return outfit;
    }

    public int getDame() {
        return dame;
    }

    public int[] getHp() {
        return hp;
    }

    public int[] getMapJoin() {
        return mapJoin;
    }

    public int[][] getSkillTemp() {
        return skillTemp;
    }

    public String[] getTextS() {
        return textS;
    }

    public String[] getTextM() {
        return textM;
    }

    public String[] getTextE() {
        return textE;
    }

    public int getSecondsRest() {
        return secondsRest;
    }

    public TypeAppear getTypeAppear() {
        return typeAppear;
    }

    public int[] getBossesAppearTogether() {
        return bossesAppearTogether;
    }

    private String name;

    private byte gender;

    private short[] outfit;

    private int dame;

    private int[] hp;

    private int[] mapJoin;

    private int[][] skillTemp;

    private String[] textS;

    private String[] textM;

    private String[] textE;

    private int secondsRest;

    private TypeAppear typeAppear;

    private int[] bossesAppearTogether;

     private BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE) {
        this.name = name;
        this.gender = gender;
        this.outfit = outfit;
        this.dame = dame;
        this.hp = hp;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.textS = textS;
        this.textM = textM;
        this.textE = textE;
        this.secondsRest = 0;
        this.typeAppear = TypeAppear.DEFAULT_APPEAR;
    }
    
    @Builder
    public BossData(String name, byte gender, int dame, int[] hp,
                    short[] outfit, int[] mapJoin, int[][] skillTemp,
                    int secondsRest, String[] textS, String[] textM,
                    String[] textE) {
        this.name = name;
        this.gender = gender;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.textS = new String[]{};
        this.textM = new String[]{};
        this.textE = new String[]{};
    }
    
    public BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, int secondsRest) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE);
        this.secondsRest = secondsRest;
    }

    public BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, int secondsRest, int[] bossesAppearTogether) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE, secondsRest);
        this.bossesAppearTogether = bossesAppearTogether;
    }

    public BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, TypeAppear typeAppear) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE);
        this.typeAppear = typeAppear;
    }

    public BossData(String name, byte gender, short[] outfit, int dame, int[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, int secondsRest, TypeAppear typeAppear) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE, secondsRest);
        this.typeAppear = typeAppear;
    }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
