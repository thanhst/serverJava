package com.louisnguyen.models.boss;

import com.louisnguyen.consts.ConstPlayer;
import com.louisnguyen.models.skill.Skill;
import com.louisnguyen.utils.Util;

/**
 * @Stole By BTH❤
 */
public class BossesData {

    /**
     * Prefix text chat |-1| Boss chat |-2| Player in map chat |-3| Parent chat
     * |0|,|1|,|n| Index boss in list chat
     */
    private static final int[][] FULL_DRAGON = new int[][]{{Skill.DRAGON, 1}, {Skill.DRAGON, 2}, {Skill.DRAGON, 3}, {Skill.DRAGON, 4}, {Skill.DRAGON, 5}, {Skill.DRAGON, 6}, {Skill.DRAGON, 7}};
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};
    private static final int[][] FULL_GALICK = new int[][]{{Skill.GALICK, 1}, {Skill.GALICK, 2}, {Skill.GALICK, 3}, {Skill.GALICK, 4}, {Skill.GALICK, 5}, {Skill.GALICK, 6}, {Skill.GALICK, 7}};
    private static final int[][] FULL_KAMEJOKO = new int[][]{{Skill.KAMEJOKO, 1}, {Skill.KAMEJOKO, 2}, {Skill.KAMEJOKO, 3}, {Skill.KAMEJOKO, 4}, {Skill.KAMEJOKO, 5}, {Skill.KAMEJOKO, 6}, {Skill.KAMEJOKO, 7}};
    private static final int[][] FULL_MASENKO = new int[][]{{Skill.MASENKO, 1}, {Skill.MASENKO, 2}, {Skill.MASENKO, 3}, {Skill.MASENKO, 4}, {Skill.MASENKO, 5}, {Skill.MASENKO, 6}, {Skill.MASENKO, 7}};
    private static final int[][] FULL_ANTOMIC = new int[][]{{Skill.ANTOMIC, 1}, {Skill.ANTOMIC, 2}, {Skill.ANTOMIC, 3}, {Skill.ANTOMIC, 4}, {Skill.ANTOMIC, 5}, {Skill.ANTOMIC, 6}, {Skill.ANTOMIC, 7}};
    private static final int[][] FULL_LIENHOAN = new int[][]{{Skill.LIEN_HOAN, 1}, {Skill.LIEN_HOAN, 2}, {Skill.LIEN_HOAN, 3}, {Skill.LIEN_HOAN, 4}, {Skill.LIEN_HOAN, 5}, {Skill.LIEN_HOAN, 6}, {Skill.LIEN_HOAN, 7}};
    private static final int[][] FULL_TDHS = new int[][]{{Skill.THAI_DUONG_HA_SAN, 1}, {Skill.THAI_DUONG_HA_SAN, 2}, {Skill.THAI_DUONG_HA_SAN, 3}, {Skill.THAI_DUONG_HA_SAN, 4}, {Skill.THAI_DUONG_HA_SAN, 5}, {Skill.THAI_DUONG_HA_SAN, 6}, {Skill.THAI_DUONG_HA_SAN, 7}};
    private static final int[][] FULL_TAI_TAO_NANG_LUONG = new int[][]{{Skill.TAI_TAO_NANG_LUONG, 1}, {Skill.TAI_TAO_NANG_LUONG, 2}, {Skill.TAI_TAO_NANG_LUONG, 3}, {Skill.TAI_TAO_NANG_LUONG, 4}, {Skill.TAI_TAO_NANG_LUONG, 5}, {Skill.TAI_TAO_NANG_LUONG, 6}, {Skill.TAI_TAO_NANG_LUONG, 7}};

    private static final int REST_1_S = 1;
    private static final int REST_2_S = 2;
    private static final int REST_5_S = 5;
    private static final int REST_10_S = 10;
    private static final int REST_20_S = 20;
    private static final int REST_30_S = 30;
    private static final int REST_1_M = 60;
    private static final int REST_2_M = 120;
    private static final int REST_5_M = 300;
    private static final int REST_10_M = 600;
    private static final int REST_15_M = 900;
    private static final int REST_30_M = 1800;
    private static final int REST_1_H = 3600;
    private static final int REST_2_H = 7200;
    private static final int REST_3_H = 10800;
    private static final int REST_4_H = 14400;
    private static final int REST_5_H = 18000;
    private static final int REST_6_H = 21600;
    private static final int REST_7_H = 25200;
    private static final int REST_8_H = 28800;
    private static final int REST_9_H = 32400;
    private static final int REST_10_H = 36000;
    private static final int REST_11_H = 39600;
    private static final int REST_12_H = 43200;
    private static final int REST_24_H = 86400;

    //************************************************************************** Boss nappa
    public static final BossData KUKU = new BossData(
            "Kuku", //name
            ConstPlayer.XAYDA, //gender
            new short[]{159, 160, 161, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            35000, //dame
            new int[]{Util.nextInt(7300000, 9000000)}, //hp
            new int[]{68, 69, 70, 71, 72}, //map join
            new int[][]{
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 5000}},
            new String[]{"|-1|Hế lô em,anh đứng đây từ chiều",
                "|-1|Mày hiểu thế là sao chứ? Cuối cùng tao đã có thể giết mày!",
                "|-2|Tao lại sợ mày quá cơ,cho bố cái địa chỉ!",
                "|-1|Mày làm tao phấn khích rồi đấy hahaha.."
            }, //text chat 1
            new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                "|-1|Ha ha ha! Mắt mày mù à? Nhìn máy đo chỉ số đi!!",
                "|-1|Định chạy trốn hả, hử",
                "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Hahaha mày đây rồi",
                "|-1|Tao đã có lệnh từ đại ca Fide rồi"
            }, //text chat 2
            new String[]{"|-2|Đẹp trai nó phải thế"}, //text chat 3
            REST_1_M //second rest
    );

    public static final BossData SUPER_BROLY = new BossData(
            "Super Broly", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{294, 295, 296, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            75000, //dame
            new int[]{250000000}, //hp
            new int[]{4, 5, 6, 11, 12, 13, 17, 18, 19}, //map join
            new int[][]{
                {Skill.KAMEJOKO, 7, 30000},
                {Skill.MASENKO, 7, 10000},
                {Skill.ANTOMIC, 7, 15000},
                {Skill.GALICK, 7, 15000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 5000},},
            new String[]{
                "|-1|Gaaaaaa",
                "|-2|Tới đây đi!"
            }, //text chat 1
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                "|-1|Gaaaaaa",
                "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
            }, //text chat 2
            new String[]{"|-1|Gaaaaaaaa!!!Hình như Lê Quang Thanh quá mạnh , hahahaha"}, //text chat 3
            REST_1_M //second rest
    );

    public static final BossData MAP_DAU_DINH = new BossData(
            "Mập Đầu Đinh", //name
            ConstPlayer.XAYDA, //gender
            new short[]{165, 166, 167, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            40000, //dame
            new int[]{Util.nextInt(15000000, 20000000)}, //hp
            new int[]{63, 64, 65, 66, 67}, //map join
            new int[][]{
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 5000}},
            new String[]{"|-1|Tao là Mập Đầu Đinh",
                "|-2|Em chào đại ca!",
                "|-1|Ai thèm làm đại ca của mày?"
            }, //text chat 1
            new String[]{"|-1|Chết hết đi cho tao",
                "|-1|Tao sẽ giết hết bọn mày",
                "|-1|Hahaha",
                "|-1|Tao chỉ cần 10 phút để giết hết bọn mày",
                "|-1|Được rồi tao sẽ thổi bay hết bọn mày",
                "|-1|Muốn đùa thì thêm tí muối đi!",
                "|-2|Thằng này,tao nhịn mày lâu lắm rồi ấy nhá",
                "|-2|Coi thường nhau quá đấy",}, //text chat 2
            new String[]{"|-1|Ôi bạn ơi ....ơi!!!"}, //text chat 3
            REST_1_M //second rest
    );

    public static final BossData RAMBO = new BossData(
            "Rambo", //name
            ConstPlayer.XAYDA, //gender
            new short[]{162, 163, 164, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            45000, //dame
            new int[]{Util.nextInt(20000000, 30000000)}, //hp
            new int[]{73, 74, 75, 76, 77}, //map join
            new int[][]{
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 5000}},
            new String[]{"|-2|Xin chào, lâu lắm không gặp!",
                "|-2|Tao đã kết liễu Mập đầu đinh rồi, lần này đến lượt mày...",
                "|-1|Mày đã giết Mập đầu đinh sao..!? Đừng.. đừng hòng lừa tao!",
                "|-2|Vậy sao mày không thử sức mạnh của tao luôn đi?"
            }, //text chat 1
            new String[]{"|-1|Hahaha",
                "|-1|Ngạc nhiên thật, đúng là mày đã tiến bộ rất nhanh..",
                "|-1|Tao sẽ cho mày biết lý do tại sao tao lại không dùng đến năng lực thực sự..",
                "|-1|Đến tao còn không thắng nổi thì đừng mộng tưởng đối đầu với đại ca Fide!",
                "|-1|Ha ha ha! Ngươi tưởng chạy trốn được sao?",
                "|-2|Oái..!",
                "|-2|Đừng tưởng thế này là xong..! Tao sẽ còn mạnh hơn nữa!",}, //text chat 2
            new String[]{"|-1|Ôi bạn ơi..."}, //text chat 3
            REST_1_M //second rest
    );

    //************************************************************************** Boss tiểu đội sát thủ
    public static final BossData SO_4 = new BossData(
            "Số 4", //name
            ConstPlayer.XAYDA, //gender
            new short[]{168, 169, 170, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            50000, //dame
            new int[]{50000000}, //hp
            new int[]{81, 82, 83}, //map join
            new int[][]{
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 5000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Các ngươi không nhúc nhích được sao?",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            REST_1_M
    );

    public static final BossData SO_3 = new BossData(
            "Số 3", //name
            ConstPlayer.XAYDA, //gender
            new short[]{174, 175, 176, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            50000, //dame
            new int[]{50000000}, //hp
            new int[]{81, 82, 83}, //map join
            new int[][]{
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 5000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData SO_2 = new BossData(
            "Số 2", //name
            ConstPlayer.XAYDA, //gender
            new short[]{171, 172, 173, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            50000, //dame
            new int[]{50000000}, //hp
            new int[]{81, 82, 83}, //map join
            new int[][]{
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 5000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData SO_1 = new BossData(
            "Số 1", //name
            ConstPlayer.XAYDA, //gender
            new short[]{177, 178, 179, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            50000, //dame
            new int[]{50000000}, //hp
            new int[]{81, 82, 83}, //map join
            new int[][]{
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 5000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData TIEU_DOI_TRUONG = new BossData(
            "Tiểu đội trưởng", //name
            ConstPlayer.XAYDA, //gender
            new short[]{180, 181, 182, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            55000, //dame
            new int[]{50000000}, //hp
            new int[]{81, 82, 83}, //map join
            new int[][]{
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 5000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!"
            }, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );

    //************************************************************************** Boss Fide đại ca
    public static final BossData FIDE_DAI_CA_1 = new BossData(
            "Fide đại ca 1", //name
            ConstPlayer.XAYDA, //gender
            new short[]{183, 184, 185, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            60000, //dame
            new int[]{50000000}, //hp
            new int[]{80}, //map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_GALICK), //skill
            new String[]{"|-1|Khá lắm con trai, dám phá tan giấc mộng của ta...",
                "|-1|Ta không thấy đám Ginyu đâu,... các ngươi đã giết chúng rồi à?",
                "|-1|Tuy không biết các ngươi dùng quỷ kế gì, nhưng ta rất ấn tượng đấy!",
                "|-1|Không thể tha thứ, ta không thể tha cho lũ sâu bọ các ngươi được!!"
            }, //text chat 1
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                "|-1|Toàn bọn tốt thí",
                "|-2|Không..thể..nào!!",
                "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!",
                "|-1|Chúng mày nghĩ kiến lại thắng nổi khủng long sao?",
                "|-1|Hô hô hô",
                "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Biến hình, hây aaaa..."}, //text chat 3
            REST_1_M //second rest
    );

    public static final BossData FIDE_DAI_CA_2 = new BossData(
            "Fide đại ca 2", //name
            ConstPlayer.XAYDA, //gender
            new short[]{186, 187, 188, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            60000, //dame
            new int[]{60000000}, //hp
            new int[]{80}, //map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_GALICK, FULL_DEMON, FULL_DRAGON), //skill
            new String[]{"|-1|Hê hê, cẩn thận đi",
                "|-1|Nếu đã biến thành thế này thì ta sẽ không nhùn nhặn như trước đâu"
            }, //text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Ôi, xin lỗi nhé. Sức mạnh lớn quá nên ta cũng chẳng điều khiển nổi nữa!",
                "|-1|Hahaha! Ấn tượng đấy! Tên nào cũng lủi rất nhanh!",
                "|-2|A...Tốc độ nhanh quá!",
                "|-1|Hình như... mày không phải là một thằng nhóc bình thường!",
                "|-1|Mấy đòn vừa rồi, nói thật là cũng đau đấy!",
                "|-1|Nhưng tiếc rằng đối thủ của mày lại là Fide này...",
                "|-2|Chết tiệt.. chúng ta đã đánh giá quá thấp sức mạnh của hắn!!",
                "|-2|Đồ..Đồ quái vật..!",
                "|-2|Tốc độ kinh hoàng quá! Ai mà né nổi chứ!",}, //text chat 2
            new String[]{"|-1|Ác quỷ biến hình, Graaaaa...."}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );

    public static final BossData FIDE_DAI_CA_3 = new BossData(
            "Fide đại ca 3", //name
            ConstPlayer.XAYDA, //gender
            new short[]{189, 190, 191, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            65000, //dame
            new int[]{70000000}, //hp
            new int[]{80}, //map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_GALICK, FULL_DEMON, FULL_DRAGON), //skill
            new String[]{"|-1|Ta sẽ cho các ngươi thấy đâu mới là sức mạnh của ta!!"}, //text chat 1
            new String[]{"|-1|Ta nói các ngươi rồi! Sức mạnh này của ta còn đáng sợ hơn địa ngục!!",
                "|-1|Ta chơi thêm chút nữa chắc ngươi chóng mặt buồn nôn mất!!",
                "|-2|Ăn gì mà khỏe thế!",
                "|-2|Chết đi Fide!!!!",
                "|-1|Hô hô hô hô",
                "|-1|Chán thật! Khí của ngươi sắp hết rồi. Để ta tiễn ngươi về địa ngục!",
                "|-1|Ngươi quá tự cao rồi đấy,xem ta đây!",}, //text chat 2
            new String[]{"|-1|Lũ khốn..",
                "|-1|..Một ngày nào đó ta sẽ quay lại và trả thù các ngươi",
                "|-1|Nhớ mặt tao đấy !",}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );

    public static final BossData SOI_01 = new BossData(
            "Sói Basil", //name
            ConstPlayer.XAYDA, //gender
            new short[]{745, 746, 747, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            70000, //dame
            new int[]{500000000}, //hp
            new int[]{192}, //map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_GALICK), //skill
            new String[]{"|-1|"
            }, //text chat 1
            new String[]{"|-1|"
            }, //text chat 2
            new String[]{"|-1|Biến hình, hây aaaa..."}, //text chat 3
            REST_1_M //second rest
    );

    public static final BossData SOI_02 = new BossData(
            "Sói Lavender", //name
            ConstPlayer.XAYDA, //gender
            new short[]{748, 749, 750, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            70000, //dame
            new int[]{600000000}, //hp
            new int[]{192}, //map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_GALICK, FULL_DEMON, FULL_DRAGON), //skill
            new String[]{"|-1|"
            }, //text chat 1
            new String[]{"|-1|",}, //text chat 2
            new String[]{"|-1|"}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );

    public static final BossData SOI_03 = new BossData(
            "Sói Bergamo", //name
            ConstPlayer.XAYDA, //gender
            new short[]{751, 752, 753, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            75000, //dame
            new int[]{700000000}, //hp
            new int[]{192}, //map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_GALICK, FULL_DEMON, FULL_DRAGON), //skill
            new String[]{"|-1|"}, //text chat 1
            new String[]{"|-1|",}, //text chat 2
            new String[]{"|-1|",}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );

    public static final BossData SIEU_BO_HUNG_1 = new BossData(
            "Siêu bọ hung 1",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            90000,
            new int[]{1500000000},
            new int[]{103},
            new int[][]{
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                {Skill.TAI_TAO_NANG_LUONG, 2, 30000},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},},//skill}, //skill
            //skill
            new String[]{"|-1|Cuối cùng thì ngươi cũng đến Goku,... ta đang đợi ngươi đây...",
                "|-1|Chào mừng đến với giải đấu!!!",
                "|-2|Vậy thì... Chúng ta bắt đầu cuộc chiến thôi chứ! Tớ lên trước nhé!",
                "|-1|Đến giờ rồi..",
                "|-1|Ai muốn đấu trước cũng được,.. kẻ nào muốn đầu trước thì bước lên đi!",
                "|-1|Hãy bắt đầu trò chơi của cell thôi!!"
            }, //text chat 1
            new String[]{"|-1|Đến đây",
                "|-1|Làm tốt lắm! Thế mới là chiến đấu chứ",
                "|-1|Chẳng hay ho gì nếu không dồn hết sức vào trận đấu!",
                "|-1|Ngươi được lắm, thực sự rất khá đấy...",
                "|-1|Cảnh báo với ngươi là ta sẽ không thất bại như lúc nãy nữa đâu!!",
                "|-1|Giờ mà ngươi phí sức vào những đòn tấn công vô ích thì trận đấu sẽ vô vị lắm đấy!!",
                "|-1|Ta muốn ngươi giải trí cho ta thêm chút nữa!!",
                "|-1|Nếu không còn ai tham dự trò chơi của Cell,.. thì toàn bộ cư dân trái đất sẽ bị tiêu diệt!"
            }, //text chat 2
            new String[]{"|-1|Hô hô hô, đây sẽ là kết thúc của lũ ngu ngốc các ngươi!! Ta sẽ chết nhưng sẽ kéo theo cái hành tinh này luôn"}, //text chat 3
            REST_1_M //second rest
    );

    public static final BossData SIEU_BO_HUNG_2 = new BossData(
            "Siêu bọ hung 2",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            100000,
            new int[]{1500000000},
            new int[]{103},
            new int[][]{
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                {Skill.TAI_TAO_NANG_LUONG, 2, 30000},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},},//skill}, //skill
            //skill
            new String[]{"|-1|Cuối cùng thì ngươi cũng đến Goku,... ta đang đợi ngươi đây...",
                "|-1|Chào mừng đến với giải đấu!!!",
                "|-2|Vậy thì... Chúng ta bắt đầu cuộc chiến thôi chứ! Tớ lên trước nhé!",
                "|-1|Đến giờ rồi..",
                "|-1|Ai muốn đấu trước cũng được,.. kẻ nào muốn đầu trước thì bước lên đi!",
                "|-1|Hãy bắt đầu trò chơi của cell thôi!!"
            }, //text chat 1
            new String[]{"|-1|Đến đây",
                "|-1|Làm tốt lắm! Thế mới là chiến đấu chứ",
                "|-1|Chẳng hay ho gì nếu không dồn hết sức vào trận đấu!",
                "|-1|Ngươi được lắm, thực sự rất khá đấy...",
                "|-1|Cảnh báo với ngươi là ta sẽ không thất bại như lúc nãy nữa đâu!!",
                "|-1|Giờ mà ngươi phí sức vào những đòn tấn công vô ích thì trận đấu sẽ vô vị lắm đấy!!",
                "|-1|Ta muốn ngươi giải trí cho ta thêm chút nữa!!",
                "|-1|Nếu không còn ai tham dự trò chơi của Cell,.. thì toàn bộ cư dân trái đất sẽ bị tiêu diệt!"
            }, //text chat 2
            new String[]{"|-1|Hô hô hô, đây sẽ là kết thúc của lũ ngu ngốc các ngươi!! Ta sẽ chết nhưng sẽ kéo theo cái hành tinh này luôn"}, //text chat 3
            //            
            TypeAppear.ANOTHER_LEVEL //type appear
    );

    public static final BossData SIEU_BO_HUNG_3 = new BossData(
            "Siêu Bọ Hung",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            110000,
            new int[]{1500000000},
            new int[]{103},
            new int[][]{
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                {Skill.TAI_TAO_NANG_LUONG, 2, 30000},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},},//skill}, //skill
            //skill
            new String[]{"|-1|Cuối cùng thì ngươi cũng đến Goku,... ta đang đợi ngươi đây...",
                "|-1|Chào mừng đến với giải đấu!!!",
                "|-2|Vậy thì... Chúng ta bắt đầu cuộc chiến thôi chứ! Tớ lên trước nhé!",
                "|-1|Đến giờ rồi..",
                "|-1|Ai muốn đấu trước cũng được,.. kẻ nào muốn đầu trước thì bước lên đi!",
                "|-1|Hãy bắt đầu trò chơi của cell thôi!!"
            }, //text chat 1
            new String[]{"|-1|Đến đây",
                "|-1|Làm tốt lắm! Thế mới là chiến đấu chứ",
                "|-1|Chẳng hay ho gì nếu không dồn hết sức vào trận đấu!",
                "|-1|Ngươi được lắm, thực sự rất khá đấy...",
                "|-1|Cảnh báo với ngươi là ta sẽ không thất bại như lúc nãy nữa đâu!!",
                "|-1|Giờ mà ngươi phí sức vào những đòn tấn công vô ích thì trận đấu sẽ vô vị lắm đấy!!",
                "|-1|Ta muốn ngươi giải trí cho ta thêm chút nữa!!",
                "|-1|Nếu không còn ai tham dự trò chơi của Cell,.. thì toàn bộ cư dân trái đất sẽ bị tiêu diệt!"
            }, //text chat 2
            new String[]{"|-1|Hô hô hô, đây sẽ là kết thúc của lũ ngu ngốc các ngươi!! Ta sẽ chết nhưng sẽ kéo theo cái hành tinh này luôn"}, //text chat 3

            TypeAppear.ANOTHER_LEVEL //type appear
    );

    //************************************************************************** Boss Android
    public static final BossData DR_KORE = new BossData(
            "Dr.Kôrê", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{255, 256, 257, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            80000, //dame
            new int[]{50000000}, //hp
            new int[]{92, 93, 94}, //map join
            new int[][]{
                {Skill.ANTOMIC, 3, 10000},
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000}, {Skill.GALICK, 7, 10000}},//skill
            //skill
            new String[]{"|-2|Chào anh! em đứng đây từ chiều",
                "|-1|Quái lạ! Sao chúng biết rõ tung tích của bọn ta thế nhỉ?",
                "|-1|Chúng còn biết chính xác ta sẽ xuất hiện ở đây để đón đánh nữa!",
                "|-1|Chúng mày là ai từ đâu tới?Cho tao xin cái địa chỉ",
                "|-2|Điều ấy biết hay không cũng không còn quan trọng nữa",
                "|-1|Ừ bọn bây chỉ là hạng tôm tép ta chẳng cần biết tên làm gì!",
                "|-1|Số 19! Xuất chiêu đi nào",
                "|0|Okê đại ca, em sẽ xử lý bọn này trong vòng 2 tiếng."
            }, //text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Chán",
                "|-1|Mi khá đấy, nhưng so với ta cũng chỉ là hạng tôm tép",
                "|-1|Lôi Công Trảo",
                "|-1|Cho dù ngươi có mạnh đến đâu.. thì cũng không đánh bại được rôbốt bọn ta",
                "|-2|Lão già khôn thật!!",
                "|-2|Hừ! Lão già khốn kiếp!",}, //text chat 2
            new String[]{}, //text chat 3
            REST_1_M, //second rest
            new int[]{BossID.ANDROID_19}
    );

    public static final BossData ANDROID_19 = new BossData(
            "Android 19", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{249, 250, 251, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            80000, //dame
            new int[]{40000000}, //hp
            new int[]{92, 93, 94}, //map join
            new int[][]{
               {Skill.ANTOMIC, 3, 10000},
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000}, {Skill.GALICK, 7, 10000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Chán",
                "|-1|Ngươi sẽ không bao giờ thắng được đâu!!",
                "|-2|Ngươi vừa hút được nhiều rồi đấy, nhưng giờ thì đừng hòng!!"}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );

    //**************************************************************************
    public static final BossData ANDROID_13 = new BossData(
            "Android 13", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{252, 253, 254, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            90000, //dame
            new int[]{30000000}, //hp
            new int[]{104}, //map join
            new int[][]{
                {Skill.ANTOMIC, 3, 10000},
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000}, {Skill.GALICK, 7, 10000},},//skill
            new String[]{"|-1|Sôn..gôku",
                "|-2|Lại là tiến sĩ Kôrê à.. rốt cuộc ông ta đã tạo ra bao nhiêu rôbốt nhân tạo thế nhỉ?",
                "|-1|Bọn ta là rôbốt sát thủ, sinh ra từ máy tính ngài Kôrê,..",
                "|-1|..cho một mục tiêu duy nhất là giết Sôngôku!",
                "|-2|Máy tính? Để giết Gôku sao?",
                "|-1|Mong muốn trả thù Gôku của ngài Kôrê đã được lưu hết vào máy tính..",
                "|-1|.., Bọn ta sinh ra từ lòng căm thù ngày một tăng bên trong chiếc máy tính có chứa mong muốn trả thù",
                "|-1|Mục tiêu của bọn ta chỉ là Gôku, nhưng mà.. nếu ngươi mà cản đường thì là chuyện khác!",}, //text chat 1
            new String[]{"|-1|Sao thế hả? Ta mới chỉ khởi động thôi mà!",
                "|-2|Ngươi đánh giá thấp bọn ta quá đấy!",
                "|-2|Đừng có tưởng bở, lũ sâu bọ!",
                "|-1|Nếu có ý định gây trở ngại cho cuộc chiến giữa ta và Sôngôku, thì ta cũng sẽ giết ngươi ngay lập tức",
                "|-2|Ngươi tưởng ta để cho ngươi giết được ta ngay à?",
                "|-2|Đúng là mạnh mồm thật đấy!",
                "|-2|Đỡ này",}, //text chat 2
            new String[]{"|-1|Sô..Sông...gôku....."}, //text chat 3
            TypeAppear.CALL_BY_ANOTHER
    );

    public static final BossData ANDROID_14 = new BossData(
            "Android 14", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{246, 247, 248, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            90000, //dame
            new int[]{40000000}, //hp
            new int[]{104}, //map join
            new int[][]{
                {Skill.ANTOMIC, 3, 10000},
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000}, {Skill.GALICK, 7, 10000},},//skill
            new String[]{"|-2|Các ngươi là ai?",
                "|-2|Ta không thể cảm nhận được khí của các ngươi, các ngươi không phải là con người đúng chứ?",
                "|-2|Ta hiểu rồi, các ngươi là rôbốt sát thủ do tiến sĩ Kôrê tạo ra chứ gì?"
            }, //text chat 1
            new String[]{}, //text chat 2
            new String[]{"|0|Số 14 và số 15 tiêu tùng cả rồi à?"}, //text chat 3
            REST_1_M,
            new int[]{BossID.ANDROID_13, BossID.ANDROID_15}
    );

    public static final BossData ANDROID_15 = new BossData(
            "Android 15", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{261, 262, 263, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            90000, //dame
            new int[]{50000000}, //hp
            new int[]{104}, //map join
            new int[][]{
                {Skill.ANTOMIC, 3, 10000},
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000}, {Skill.GALICK, 7, 10000},},//skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{"|-2|Thì ra vẫn chỉ là một đống sắt vụn!"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
//**************************************************************************

    public static final BossData PIC = new BossData(
            "Píc", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{237, 238, 239, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            90000, //dame
            new int[]{60000000}, //hp
            new int[]{96, 97, 98}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO, FULL_ANTOMIC, FULL_MASENKO), //skill
            new String[]{"|-1|Chào! Có Gôku ở đây không?",
                "|-3|Tôi không nghĩ Gôku ở đây đâu!",
                "|-2|Biến khỏi đây đi, Gôku không có ở đây đâu!",
                "|-1|Bọn ta cũng nghĩ vậy, ngươi nói cho bọn ta biết hắn ở đâu được không!?",
                "|-2|Ngươi nghĩ bọn ta sẽ nói sao??",
                "|-1|Nếu ngươi không chịu nói bọn ta sẽ phải ra tay.."
            }, //text chat 1
            new String[]{"|-1|Ngươi thực sự rất mạnh dù không phải là một rôbốt. Ngươi không phải là Piccôlô",
                "|-1|Nhưng ta không quan tâm ngươi là ai, ta chỉ cần biết Gôku đang ở đâu!",
                "|-1|Sao ngươi không chịu nói cho ta biết chứ!?",
                "|-2|Mục đích của ngươi không phải là giết Gôku sao? Vì vậy ta không nói cho ngươi biết cậu ấy đang ở đâu",
                "|-1|Vậy thì ta bắt buộc phải tiếp tục đánh buộc ngươi phải nói ra!",
                "|-1|Lần này ta không nương tay đâu!",
                "|-2|Ngươi thực sự rất nhanh. Nhưng chưa đủ thực lực đâu!!",
                "|-1|Cái gì!? Đó là điều ngu ngốc nhất ta từng nghe.. ta là chiến binh mạnh nhất từ trước đến giờ.!",
                "|1|Hắn thực sự rất mạnh, đúng là cuộc chiến cân sức",
                "|-3|Pic, trả nhẽ cậu lại để thua mấy tên nhóc vặt này sao?"
            }, //text chat 2
            new String[]{"|1|Pic tiêu rồi, tớ lên trước nhé!",
                "|-3|Okê, xin cứ tự nhiên"
            }, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );

    public static final BossData POC = new BossData(
            "Póc", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{240, 241, 242, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            90000, //dame
            new int[]{80000000}, //hp
            new int[]{96, 97, 98}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO, FULL_ANTOMIC, FULL_MASENKO), //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Đừng tưởng ta đây là con gái mà dễ bắt nạt nhé",
                "|-1|Khôn hồn thì chỉ chỗ Gôku cho bọn ta nhanh đi",
                "|-3|Coi kìa, một lũ xúm lại bắt nạt một cô gái kìa..",
                "|-1|Đừng có mà trọng nam khinh nữ",
                "|-2|Tại sao cô gái xinh đẹp thế này mà lại là rôbốt nhỉ?"
            }, //text chat 2
            new String[]{"|-2|Cô gái xinh đẹp vậy mà lại bị tên tiến sĩ Kôrê biến thành người máy.."}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );

    public static final BossData KING_KONG = new BossData(
            "King Kong", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{243, 244, 245, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            95000, //dame
            new int[]{100000000}, //hp
            new int[]{96, 97, 98}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO, FULL_ANTOMIC, FULL_MASENKO), //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Mau đền mạng cho những người bạn của ta",
                "|-1|Sức mạnh của ta chênh nhau với các ngươi một trời một vực đấy!",
                "|-1|Thằng kia đừng để bọn nó trói tao !"
            }, //text chat 2
            new String[]{}, //text chat 3
            REST_1_M,
            new int[]{BossID.PIC, BossID.POC}
    );

    //=============================BOSS TRẠM TÀU HỦY DIỆT=========================//
    public static final BossData THAN_THIEN_SU = new BossData(
            "Thần Thiên Sứ 1", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{838, 839, 840, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            150000, //dame
            new int[]{1000000000}, //hp
            new int[]{193, 194, 195}, //map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_TAI_TAO_NANG_LUONG, FULL_ANTOMIC, FULL_MASENKO), //skill
            new String[]{"|-2|Kia là ai thế",
                "|-1|Xin chào! Chúng tôi tới đây để tìm người!",
                "|-1|Ngài Berrus! Có vẻ hắn ta không có ở đây",
                "|-1|Ta có thể hỏi các ngươi",
                "|-2|Chuyện gì vậy?",
                "|-1|Các ngươi có biết tên Super Sayan God ở đâu không?",
                "|-2|Super Sayan God? đó là gì vậy?",
                "|-1|Có vẻ giấc mơ của ngài Berrus là bịp rồi! Hô Hô",
                "|0|Nếu các ngươi không biết hắn ở đâu",
                "|0|Chắc ta phải phá hủy hành tinh này vậy"
            }, //text chat 1
            new String[]{"|-1|Ta có thể ngồi ăn một chút được rồi!",
                "|-1|Các ngươi vẫn yếu vẫn như mọi khi",
                "|0|Thật là bực mình!",
                "|-2|Đây là sức mạnh của một thiên sứ sao?",
                "|-1|Hô Hô",
                "|-1|Các ngươi không đánh bại được ta đâu!",
                "|-2|Không thể nào",
                "|-2|Tại sao lại vậy chứ !",}, //text chat 2
            new String[]{"|-1|Ta đi về đây!Cảm ơn vì món ăn"}, //text chat 3
            REST_1_M,
            new int[]{BossID.THAN_HUY_DIET}
    );

    public static final BossData THAN_HUY_DIET = new BossData(
            "Thần Hủy Diệt 2", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{508, 509, 510, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            160000, //dame
            new int[]{1000000000}, //hp
            new int[]{193, 194, 195}, //map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_TAI_TAO_NANG_LUONG, FULL_ANTOMIC, FULL_GALICK, FULL_MASENKO), //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Các ngươi thật là yếu ớt",
                "|-1|Ta sẽ phá hủy hành tinh này",
                "|-1|Chán quá!",
                "|-1|Ta vẫn chưa dùng hết sức đâu!",
                "|-2|Hắn ta không cần phòng thủ luôn!",}, //text chat 2
            new String[]{"|-1|Ta buồn ngủ quá!"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );

    ////===========================BOSS JACKY CHUN VÀ QUY LÃO KAME==============//
    public static final BossData JACKY_CHUN2 = new BossData(
            "Jacky Chun", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{356, 357, 358, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            90000, //dame
            new int[]{1000000000}, //hp
            new int[]{47}, //map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK, FULL_TAI_TAO_NANG_LUONG), //skill
            new String[]{"|-2|Kia là ai thế",
                "|-1|Xin chào! Chúng tôi tới đây để tìm người!",}, //text chat 1
            new String[]{"|-1|Các ngươi thật yếu đuối",}, //text chat 2
            new String[]{"|-1|Ta sẽ quay trở lại"}, //text chat 3
            REST_1_M,
            new int[]{BossID.QUY_LAO}
    );

    public static final BossData QUY_LAO = new BossData(
            "Quy Lão", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{554, 555, 556, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            90000, //dame
            new int[]{1000000000}, //hp
            new int[]{47}, //map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_ANTOMIC, FULL_GALICK, FULL_TDHS), //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Các ngươi thật là yếu ớt",
                "|-1|Chán quá!",
                "|-1|Ta vẫn chưa dùng hết sức đâu!",
                "|-2|Hắn ta không cần phòng thủ luôn!",}, //text chat 2
            new String[]{"|-1|Ta buồn ngủ quá!"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    //*****************************BOSS 4H Sáng******************************

    public static final BossData MAI = new BossData(
            "Mai", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{615, 616, 617, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            50000, //dame
            new int[]{100}, //hp
            new int[]{0}, //map join
            (int[][]) Util.addArray(FULL_DRAGON), //skill
            new String[]{"|-1|Dragon Goku Comeback"}, //text chat 1
            new String[]{"|-1|Dragon Goku Comeback"}, //text chat 2
            new String[]{"|-1|Dragon Goku Comeback"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );

    public static final BossData SU = new BossData(
            "Su", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{618, 619, 620, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            35000, //dame
            new int[]{100}, //hp
            new int[]{0}, //map join
            (int[][]) Util.addArray(FULL_DEMON), //skill
            new String[]{"|-1|Dragon Goku Comeback"}, //text chat 1
            new String[]{"|-1|Dragon Goku Comeback"}, //text chat 2
            new String[]{"|-1|Dragon Goku Comeback"}, //text chat
            TypeAppear.APPEAR_WITH_ANOTHER
    );

    public static final BossData PI_LAP = new BossData(
            "PiLap", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{612, 613, 614, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            30000, //dame
            new int[]{100}, //hp
            new int[]{0}, //map join
            (int[][]) Util.addArray(FULL_GALICK), //skill
            new String[]{"|-1|Dragon Goku Comeback"}, //text chat 1
            new String[]{"|-1|Dragon Goku Comeback"}, //text chat 2
            new String[]{"|-1|Dragon Goku Comeback"}, //text chat 3
            REST_1_M,
            new int[]{BossID.MAI, BossID.SU}
    );
    //************************************************************************** Boss cell

    public static final BossData XEN_BO_HUNG_1 = new BossData(
            "Xên bọ hung 1",
            ConstPlayer.XAYDA,
            new short[]{228, 229, 230, -1, -1, -1},
            90000,
            new int[]{200000000},
            new int[]{100},
            new int[][]{
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                {Skill.TAI_TAO_NANG_LUONG, 2, 30000},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},},//skill}, //skill
            new String[]{"|-2|Cái gì kia vậy!? Đó là loài gì thế!!?",
                "|-1|Hôm nay sẽ là ngày đáng nhớ đây!",
                "|-1|Ta sẽ hấp thụ số 17 và 18 để đạt được dạng hoàn hảo!",
                "|-1|Sao vậy, Picôlô? Nếu ngươi muốn ngăn ta lại thì xong lên đi chứ!?"
            }, //text chat 1
            new String[]{"|-2|Hắn làm ta bất ngờ đấy! Khốn kiếp!",
                "|-2|Tên đó muốn hấp thụ số 17 và 18 sao?",
                "|-1|Đến đây nào! Khi kết hợp với ta, ngươi sẽ trở nên bất bại, trở thành một thể sống hoàn mỹ!",
                "|-2|Mơ đi, sao ta phải để ngươi hấp thu hả!?",
                "|-2|Dù muốn hay không, ngươi cũng sẽ bị ta hấp thu thôi..",
                "|-2|Chúng ta không thể để hắn đạt đến dạng hoàn hảo được!",
                "|-2|Mục tiêu của hắn không phải là Sôngôku.., mà là phá hủy cả vũ trụ này!",
                "|-1|Làm đứt đuôi ta ư? Đừng quên ta có tế bào của Picôlô!!",
                "|-1|Ta có thể tái tạo.. mọi bộ phận cơ thể!",
                "|-2|Vậy thì để ngăn cản ngươi đạt đến dạng hoàn hảo, ta phải tiêu diệt ngươi!",
                "|-2|Hắn quá mạnh! Mình có thể làm được gì đây!?",
                "|-1|Có vẻ như ta đã trở nên quá mạnh, chắc là ta đã giết nhiều người hơn dự tính!!",
                "|-1|Ngươi không thể thắng nổi ta! Từ bỏ đi!!",
                "|-1|Đến lúc ta hấp thu ngươi rồi",
                "|-2|Đồ quái vật chết tiệt...",
                "|-1|Hê hê hê, rồi ngươi sẽ trở thành một phần của con quái vật này thôi!",
                "|-1|Lại thêm một tên ngốc nữa chán sống!"
            }, //text chat 2
            new String[]{"|-2|Khốn kiếp, Pic.. hắn bị Cell hấp thu rồi!!"}, //text chat 3
            REST_1_M
    );

    public static final BossData XEN_BO_HUNG_2 = new BossData(
            "Xên bọ hung 2",
            ConstPlayer.XAYDA,
            new short[]{231, 232, 233, -1, -1, -1},
            950000,
            new int[]{250000000},
            new int[]{100},
            new int[][]{
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                {Skill.TAI_TAO_NANG_LUONG, 2, 30000},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},},//skill}, //skill
            new String[]{}, //text chat 1
            new String[]{"|-2|Nguy rồi... thực sự nguy to rồi!",
                "|-1|Các ngươi nghĩ có thể chạy được sao!?",
                "|-1|Muốn chạy khỏi ta thì đừng hòng!!",
                "|-1|Ta cũng ngạc nhiên với tốc độ của mình! Đó tất nhiên là do ta hấp thụ được số 17!",
                "|-2|Hắn nhanh quá!!",
                "|-1|Ta muốn thử xem sức mạnh này đến đâu...",
                "|-1|Hmm.. có vẻ như sức mạnh này đã tăng lên gấp bội!",
                "|-1|Đã đến lúc ta đạt đến trạng thái hoàn hảo.!",
                "|-1|Có vẻ như ngươi muốn bị ép hơn là tự nguyện!!",
                "|-2|Bây giờ ta chưa thể thắng được ngươi!! Nhưng ngươi đừng hòng huyênh hoang!!!",
                "|-1|Muốn chạy à!!? Ta sẽ không để ngươi thoát đâu!!",}, //text chat 2
            new String[]{"|-1|Đến lúc rồi!"}, //text chat 3
            TypeAppear.ANOTHER_LEVEL
    );

    public static final BossData XEN_BO_HUNG_3 = new BossData(
            "Xên hoàn thiện",
            ConstPlayer.XAYDA,
            new short[]{231, 232, 233, -1, -1, -1},
            100000,
            new int[]{250000000},
            new int[]{100},
            new int[][]{
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                {Skill.TAI_TAO_NANG_LUONG, 2, 30000},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},},//skill}, //skill
            new String[]{}, //text chat 1
            new String[]{"|-2|Nguy rồi... thực sự nguy to rồi!",
                "|-1|Các ngươi nghĩ có thể chạy được sao!?",
                "|-1|Muốn chạy khỏi ta thì đừng hòng!!",
                "|-1|Ta cũng ngạc nhiên với tốc độ của mình! Đó tất nhiên là do ta hấp thụ được số 17!",
                "|-2|Hắn nhanh quá!!",
                "|-1|Ta muốn thử xem sức mạnh này đến đâu...",
                "|-1|Hmm.. có vẻ như sức mạnh này đã tăng lên gấp bội!",
                "|-1|Đã đến lúc ta đạt đến trạng thái hoàn hảo.!",
                "|-1|Có vẻ như ngươi muốn bị ép hơn là tự nguyện!!",
                "|-2|Bây giờ ta chưa thể thắng được ngươi!! Nhưng ngươi đừng hòng huyênh hoang!!!",
                "|-1|Muốn chạy à!!? Ta sẽ không để ngươi thoát đâu!!",}, //text chat 2
            new String[]{"|-1|Đến lúc rồi!"}, //text chat 3
            TypeAppear.ANOTHER_LEVEL
    );

    //**************************************************************************
    public static final BossData COOLER_GOLD = new BossData(
            "Cooler Vàng", //name
            ConstPlayer.XAYDA, //gender
            new short[]{709, 710, 711, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            2000000, //dame
            new int[]{1000000000}, //hp
            new int[]{155}, //map join
            new int[][]{
                {Skill.THOI_MIEN, 7, 100000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 3000},
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.LIEN_HOAN, 4, 1000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 50000},
                {Skill.MASENKO, 7, 10000}}, //skill
            new String[]{"|-1|Hello cục cưng",
                "|-1|Mày có biết tao là ai không?",
                "|-2|Tao không cần biết mày là ai, mày nghĩ mày dọa được tao à?",
                "|-1|Thôi không nói nhiều nữa,giờ tao cho mày biết tao là ai."
            }, //text chat 1
            new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                "|-1|Ghê chưa ghê chưa!",
                "|-1|Tao có rất nhiều vật phẩm quý giá,nhưng với mày thì có cái..nịt",
                "|-1|Đánh tao à,lo mà luyện tập thêm đi",
                "|-1|Nói cho mày biết,tao là anh trai của Fide",
                "|-1|trạng thái Goldend Meta Cooler sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Đêm qua em đẹp lắm!"}, //text chat 3
            REST_1_M //second rest
    );

    //**************************************************************************
    public static final BossData XEN_CON = new BossData(
            "Xên con", //name
            ConstPlayer.XAYDA, //gender
            new short[]{264, 265, 266, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            150000, //dame
            new int[]{2000000000}, //hp
            new int[]{103}, //map join
            (int[][]) Util.addArray(FULL_DEMON, FULL_MASENKO), //skill
            new String[]{"|-1|Hello cục cưng",
                "|-1|Mày có biết tao là ai không?",
                "|-2|Tao không cần biết mày là ai, mày nghĩ mày dọa được tao à?",
                "|-1|Thôi không nói nhiều nữa,giờ tao cho mày biết tao là ai."
            }, //text chat 1
            new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                "|-1|Ghê chưa ghê chưa!",
                "|-1|Tao có rất nhiều vật phẩm quý giá,nhưng với mày thì có cái..nịt",
                "|-1|Đánh tao à,lo mà luyện tập thêm đi",
                "|-1|Nói cho mày biết,tao là con của Xên ",
                "|-1|Tao sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Đêm qua em đẹp lắm!"}, //text chat 3
            REST_1_M //second rest
    );
    //**************************************************************************

    //**************************************************************************cha con fide
    public static final BossData VUA_COLD = new BossData(
            "King Cold",
            ConstPlayer.XAYDA,
            new short[]{712, 713, 714, -1, -1, -1},
            3000000,
            new int[]{2000000000},
            new int[]{131, 132, 133},
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_LIENHOAN), //skill
            new String[]{"|-2|Hắn ta là ai vậy?"}, //text chat 1
            new String[]{"|-1|Thì ra đây là trái đất",
                "|-1|Hành tinh này bán đi chắc cũng kha khá đó!",
                "|-2|Ngươi làm ta khó chịu rồi đấy",
                "|-1|Ngươi sẽ không bao giờ thắng được đâu!!",
                "|-2|Tên này mạnh quá",}, //text chat 2
            new String[]{"|-1|Xin hãy tha cho ta !",
                "|-1|Ta sẽ cho ngươi nửa số hành tinh ta đang giữ!",
                "|-1|Đừng màaa!"}, //text chat 3
            REST_1_M, //second rest
            new int[]{BossID.FIDE_ROBOT}
    );

    public static final BossData FIDE_ROBOT = new BossData(
            "Fide người máy", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{715, 716, 717, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            12200, //dame
            new int[]{150000000}, //hp
            new int[]{131, 132, 133}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_ANTOMIC), //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Haaaahaa",
                "|-1|Chúng ta sẽ hủy diệt hành tinh này",
                "|-1|Tên Sôn gô ku mãi vẫn chưa tới",
                "|-1|Ngươi sẽ không bao giờ thắng được đâu!!",
                "|-2|Để xem ai mới là người chiến thắng!!",}, //text chat 2
            new String[]{"|-1|Ta thua rồi sao? Khôngggggggg!"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );

    //**************************************************************************
    //**************************************************************************
    public static final BossData ANTROM = new BossData(
            "Ăn Trộm", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{201, 202, 203, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1, //dame
            new int[]{100}, //hp
            new int[]{5, 84, 0, 7, 14}, //map join
            new int[][]{
                {Skill.THAI_DUONG_HA_SAN, 3, 30000},},//skill
            new String[]{}, //text chat 1
            new String[]{},
            new String[]{"|-1|Ta trả ngươi vàng này, hãy tha cho ta ..."}, //text chat 3
            REST_1_H //type appear
    );

    public static final BossData TAU_PAY_PAY_BASE = new BossData(
            "Tàu Pảy Pảy", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{92, 93, 94, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000, //dame
            new int[]{1000000}, //hp
            new int[]{1}, //map join
            new int[][]{
                {Skill.DRAGON, 7, 3000},
                {Skill.DEMON, 7, 3000},
                {Skill.GALICK, 7, 3000},},//skill
            new String[]{"|-1|"}, //text chat 1
            new String[]{"|-1|"},
            new String[]{"|-1|"}, //text chat 3
            REST_1_H //type appear
    );

    //**************************************************************************
    public static final BossData BLACK_GOKU_BASE = new BossData(
            "Black Goku", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{550, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            800000, //dame
            new int[]{777000000}, //hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 27, 28, 29}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO, FULL_LIENHOAN), //skill
            new String[]{"|-1|Ta là Sôn Gô Ku",
                "|-1|Cơ thể này,sức mạnh này",
                "|-1|Ta khá thích việc loại bỏ các ngươi",
                "|-1|Mau chấp nhận số phận đi lũ sâu bọ"
            }, //text chat 1
            new String[]{"|-1|Các ngươi chỉ có vậy thôi sao?",
                "|-1|Đúng là loài người thấp kém",
                "|-2|Ngươi nói như thể ngươi không phải con người vậy?",
                "|-2|Chiếc nhẫn kia lẽ nào ngươi là một Kaioshin?!",
                "|-1|Các ngươi không nên biết quá nhiều",
                "|-2|Xem đòn đánh của ta đây !",
                "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Biến hình! Super Sayan Rose"}, //text chat 3
            REST_1_M //second rest
    );
    //**************************************************************************
    public static final BossData BLACK_GOKU = new BossData(
            "Black Goku", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{550, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            800000, //dame
            new int[]{977000000}, //hp
            new int[]{102, 92, 93, 94, 96, 97, 98, 99, 100}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO, FULL_TAI_TAO_NANG_LUONG), //skill
            new String[]{"|-1|Ta là Sôn Gô Ku",
                "|-1|Cơ thể này,sức mạnh này",
                "|-1|Ta khá thích việc loại bỏ các ngươi",
                "|-1|Mau chấp nhận số phận đi lũ sâu bọ"
            }, //text chat 1
            new String[]{"|-1|Các ngươi chỉ có vậy thôi sao?",
                "|-1|Đúng là loài người thấp kém",
                "|-2|Ngươi nói như thể ngươi không phải con người vậy?",
                "|-2|Chiếc nhẫn kia lẽ nào ngươi là một Kaioshin?!",
                "|-1|Các ngươi không nên biết quá nhiều",
                "|-2|Xem đòn đánh của ta đây !",
                "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Biến hình! Super Sayan Rose"}, //text chat 3
            REST_1_M //second rest
    );

    public static final BossData SUPER_BLACK_GOKU_2 = new BossData(
            "Super Black Goku", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{553, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            680000, //dame
            new int[]{1000000000}, //hp
            new int[]{102, 92, 93, 94, 96, 97, 98, 99, 100}, //map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 7, 30000},
                {Skill.GALICK, 7, 10000},
                {Skill.ANTOMIC, 7, 10000},
                {Skill.KAMEJOKO, 7, 10000},},
            //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Ta chính là người mang thân thể của Songoku",
                "|-1|Sức mạnh của ta là không có giới hạn",
                "|-1|Ta sẽ thống trị vũ trụ",
                "|-1|Để ta nói cho nghe,người Sayan sau khi hồi phục sức mạnh sẽ tăng lên rất nhiều",
                "|-2|Tại sao ngươi lại lấy thân thể của songoku chứ?"
            }, //text chat 2

            new String[]{"|-1|Chúng ta sẽ gặp lại nhau sớm thôi",
                "|-2|Ngươi nói gì chứ?"}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );

    public static final BossData SUPER_BLACK_GOKU_2_2H3H = new BossData(
            "Black Goku Rose", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{553, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            680000, //dame
            new int[]{1000000000}, //hp
            new int[]{197}, //map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 7, 30000},
                {Skill.GALICK, 7, 10000},
                {Skill.ANTOMIC, 7, 10000},
                {Skill.KAMEJOKO, 7, 10000},},
            //skill
            new String[]{""
            }, //text chat 1
            new String[]{"|-1|Ta chính là người mang thân thể của Songoku",
                "|-1|Sức mạnh của ta là không có giới hạn",
                "|-1|Ta sẽ thống trị vũ trụ",
                "|-1|Để ta nói cho nghe,người Sayan sau khi hồi phục sức mạnh sẽ tăng lên rất nhiều",
                "|-2|Tại sao ngươi lại lấy thân thể của songoku chứ?"
            }, //text chat 2

            new String[]{"|-1|Chúng ta sẽ gặp lại nhau sớm thôi",
                "|-2|Ngươi nói gì chứ?"}, //text chat 3
            REST_1_M //second rest
    );

    public static final BossData ZAMAS = new BossData(
            "Zamasu", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{903, 904, 905, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            4550500, //dame
            new int[]{450000000}, //hp
            new int[]{197}, //map join
            new int[][]{
                {Skill.GALICK, 7, 1000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 4, 1000},
                {Skill.KAMEJOKO, 7, 30000}}, //skill
            new String[]{"|-1|Kia là một con người sao?",
                "|-3|Ủa tên kia là ai vậy?",
                "|-2|Lẽ nào đúng như chúng ta đã nghĩ",
                "|-1|Lũ con người không đủ tư cách để nói chuyện với ta",
                "|-2|Zamas! Tại sao chứ !",
                "|-1|Ta sẽ cho người biết sức mạnh của một vị thần là như thế nào !"
            }, //text chat 1
            new String[]{"|-1|Ta là kaioshin của vũ trụ thứ 10 ",
                "|-1|Tên của ta là Zamas, ta sẽ thay đổi thế giới này",
                "|-1|Lũ con người các ngươi là những thứ ta cần loại bỏ đầu tiên",
                "|-2|Tại sao các ngươi lại nhắm tới con người bọn ta chứ?",
                "|-1|Bởi vì ta muốn thực hiện kế hoạch đưa con người về số 0 !",
                "|-1|Lần này ta không nương tay đâu!",
                "|-2|Ngươi thực sự rất mạnh. Nhưng chưa đủ thực lực đâu!!",
                "|-1|Cái gì!? Đó là điều ngu ngốc nhất ta từng nghe! Mau biến đi",
                "|-1|Hắn thực sự rất mạnh, đúng là cuộc chiến hay",
                "|-3|Không lí nào ta lại run sợ bọn con người sao"
            }, //text chat 2

            new String[]{"|-1|Chỉ còn một cách duy nhất mà thôi",
                "|-1|Bông tai Porata!"}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );

    public static final BossData CUMBER = new BossData(
            "Cumber", //name
            ConstPlayer.XAYDA, //gender
            new short[]{1181, 1182, 1183, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            100000, //dame
            new int[]{500000000}, //hp
            new int[]{155}, //map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 3, 30000},
                {Skill.GALICK, 7, 10000},
                {Skill.MASENKO, 7, 6000},
                {Skill.MASENKO, 1, 1000},},
            //skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2

            new String[]{}, //text chat 3
            REST_1_M //second rest
    );

    public static final BossData SUPER_CUMBER = new BossData(
            "Super Cumber", //name
            ConstPlayer.XAYDA, //gender
            new short[]{1187, 1182, 1183, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            100000, //dame
            new int[]{1000000000}, //hp
            new int[]{155}, //map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 3, 30000},
                {Skill.GALICK, 7, 10000},
                {Skill.MASENKO, 7, 10000},
                {Skill.MASENKO, 1, 10000},},//skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );

    //**************************************************************************
    public static final BossData BROLY_CLONE = new BossData(
            "Broly ",
            ConstPlayer.XAYDA, //gender
            new short[]{291, 292, 293, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000,
            new int[]{500000},
            new int[]{5, 6, 10, 13, 19, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38},
            new int[][]{
                {Skill.KAMEJOKO, 1, 30000},
                {Skill.MASENKO, 1, 10000},
                {Skill.ANTOMIC, 1, 15000},
                {Skill.TAI_TAO_NANG_LUONG, 1, 20000},},
            new String[]{
                "|-1|Tuy không biết các ngươi là ai, nhưng ta rất ấn tượng đấy!",
                "|-2|Tới đây đi!"
            },
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                "|-1|Gaaaaaa",
                "|-2|Ngươi làm ta bực rồi đó",},
            new String[]{"|-1|Gaaaaaaaa!!!"},
            REST_1_M
    );

    public static final BossData BROLY_1 = new BossData(
            "Broly",
            ConstPlayer.XAYDA, //gender
            new short[]{291, 292, 293, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000,
            new int[]{30000000},
            new int[]{49},
            new int[][]{
                {Skill.KAMEJOKO, 1, 7000},
                {Skill.MASENKO, 1, 6000},
                {Skill.ANTOMIC, 1, 5000},
                {Skill.TAI_TAO_NANG_LUONG, 5, 30000}},
            new String[]{
                "|-1|Tuy không biết các ngươi là ai, nhưng ta rất ấn tượng đấy!",},
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                "|-1|Gaaaaaa",
                "|-1|Ngươi làm ta bực rồi đó",
                "|-2|",
                "|-2|"
            },
            new String[]{"|-1|Gaaaaaaaa!!!"},
            REST_1_M
    );

    //=========================================================================
    public static final BossData SAIBAMEN = new BossData(
            "Số 1",
            ConstPlayer.XAYDA, //gender
            new short[]{642, 643, 644, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000,
            new int[]{30000000},
            new int[]{103},
            new int[][]{},
            new String[]{
                "|-1|Tuy không biết các ngươi là ai, nhưng ta rất ấn tượng đấy!",},
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                "|-1|Gaaaaaa",
                "|-1|Ngươi làm ta bực rồi đó",
                "|-2|",
                "|-2|"
            },
            new String[]{"|-1|Gaaaaaaaa!!!"},
            REST_1_M
    );

    //**************************************************************************
    public static final BossData Rong_1Sao = new BossData(
            "Rồng Syn 1 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{204, 205, 206, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{2000000000}, //hp
            new int[]{85}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 10000},
                {Skill.KAMEJOKO, 5, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                "|-2|Tên kia là ai vậy",
                "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                "|-1|Đi chết đi!",
                "|-1|Các ngươi không phải đối thủ của ta đâu",
                "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_1_M //second rest
    );
    public static final BossData Rong_2Sao = new BossData(
            "Rồng Haze 2 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{219, 220, 221, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{200000000}, //hp
            new int[]{86}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.KAMEJOKO, 5, 1000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                "|-2|Tên kia là ai vậy",
                "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                "|-1|Đi chết đi!",
                "|-1|Các ngươi không phải đối thủ của ta đâu",
                "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_1_M //second rest

    );
    public static final BossData Rong_3Sao = new BossData(
            "Rồng Eis 3 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{207, 208, 209, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1200000, //dame
            new int[]{400000000}, //hp
            new int[]{87}, //map join
            new int[][]{
                {Skill.THOI_MIEN, 4, 125000},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                "|-2|Tên kia là ai vậy",
                "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                "|-1|Đi chết đi!",
                "|-1|Các ngươi không phải đối thủ của ta đâu",
                "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_1_M //second rest

    );
    public static final BossData Rong_4Sao = new BossData(
            "Rồng Nuova 4 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{210, 211, 212, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{1800000000}, //hp
            new int[]{88}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 10000},
                {Skill.TROI, 7, 100000},
                {Skill.KAMEJOKO, 5, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                "|-2|Tên kia là ai vậy",
                "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                "|-1|Đi chết đi!",
                "|-1|Các ngươi không phải đối thủ của ta đâu",
                "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_1_M //second rest
    );
    public static final BossData Rong_5Sao = new BossData(
            "Rồng Rage 5 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{213, 214, 215, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{2000000000}, //hp
            new int[]{89}, //map join
            new int[][]{
                {Skill.KAMEJOKO, 5, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                "|-2|Tên kia là ai vậy",
                "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                "|-1|Đi chết đi!",
                "|-1|Các ngươi không phải đối thủ của ta đâu",
                "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_1_M //second rest
    );
    public static final BossData Rong_6Sao = new BossData(
            "Rồng Oceanus 6 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{222, 223, 224, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            3000000, //dame
            new int[]{2000000000}, //hp
            new int[]{90}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 4000},
                {Skill.KAMEJOKO, 5, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                "|-2|Tên kia là ai vậy",
                "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                "|-1|Đi chết đi!",
                "|-1|Các ngươi không phải đối thủ của ta đâu",
                "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_1_M //second rest
    );
    public static final BossData Rong_7Sao = new BossData(
            "Rồng Naturon 7 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{216, 217, 218, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{2000000000}, //hp
            new int[]{91}, //map join
            new int[][]{
                {Skill.KAMEJOKO, 5, 1000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                "|-2|Tên kia là ai vậy",
                "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                "|-1|Đi chết đi!",
                "|-1|Các ngươi không phải đối thủ của ta đâu",
                "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_1_M //second rest
    );
    //**************************************************************************Team Mabu 12h
    public static final BossData MABU_12H = new BossData(
            "Mabư",
            ConstPlayer.XAYDA,
            new short[]{297, 298, 299, -1, -1, -1},
            1000000,
            new int[]{2000000000},
            new int[]{120},
            new int[][]{
                {Skill.KHIEN_NANG_LUONG, 7, 50000},
                {Skill.THAI_DUONG_HA_SAN, 7, 70000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 1000},
                {Skill.DE_TRUNG, 7, 1000},
                {Skill.KAMEJOKO, 7, 100}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );
    public static final BossData DRABURA = new BossData(
            "Drabura",
            ConstPlayer.XAYDA,
            new short[]{418, 419, 420, -1, -1, -1},
            100000,
            new int[]{50000000},
            new int[]{114},
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.DEMON, 7, 10000}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );
    public static final BossData DRABURA_2 = new BossData(
            "Drabura",
            ConstPlayer.XAYDA,
            new short[]{418, 419, 420, -1, -1, -1},
            200000,
            new int[]{50000000},
            new int[]{115},
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.DEMON, 7, 10000}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );
    public static final BossData BUI_BUI = new BossData(
            "Pui Pui",
            ConstPlayer.XAYDA,
            new short[]{451, 452, 453, -1, -1, -1},
            200000,
            new int[]{300000000},
            new int[]{117},
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.DEMON, 7, 10000}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );

    public static final BossData BUI_BUI_2 = new BossData(
            "Pui Pui",
            ConstPlayer.XAYDA,
            new short[]{451, 452, 453, -1, -1, -1},
            200000,
            new int[]{500000000},
            new int[]{118},
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.DEMON, 7, 10000}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );
    public static final BossData YACON = new BossData(
            "Yacôn",
            ConstPlayer.XAYDA,
            new short[]{415, 416, 417, -1, -1, -1},
            200000,
            new int[]{500000000},
            new int[]{119},
            new int[][]{
                {Skill.DEMON, 7, 100}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );

    public static final BossData COOLER_1 = new BossData(
            "Cooler", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{317, 318, 319, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1, //dame
            new int[]{30000000}, //hp
            new int[]{110}, //map join
            new int[][]{
                {Skill.GALICK, 7, 400},
                {Skill.LIEN_HOAN, 1, 400},
                {Skill.ANTOMIC, 7, 300},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Các ngươi thật là yếu ớt",
                "|-1|Chán quá!",
                "|-1|Không có ai đủ mạnh để đấu với ta sao?",
                "|-1|Ta sẽ cho ngươi cái nịt!",}, //text chat 2
            new String[]{"|-1|Biến hình !!!!!!!!!!!!"}, //text chat 3
            REST_1_M
    );

    public static final BossData COOLER_2 = new BossData(
            "Cooler 2", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{320, 321, 322, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1, //dame
            new int[]{50000000}, //hp
            new int[]{110}, //map join
            new int[][]{
                {Skill.GALICK, 7, 400},
                {Skill.LIEN_HOAN, 1, 400},
                {Skill.ANTOMIC, 7, 300},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Các ngươi thật là yếu ớt",
                "|-1|Ta sẽ phá hủy hành tinh này",
                "|-1|Chán quá!",
                "|-1|Không có ai đủ mạnh để đấu với ta sao?",
                "|-1|Ta nghèo lắm!Đừng săn ta nữa",}, //text chat 2
            new String[]{"|-1|Đen lắm em trai !"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData SUPER_ANDROID_17 = new BossData(
            "Super Android 17", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{636, 637, 638, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            5500000, //dame
            new int[]{2000000000}, //hp
            new int[]{98, 99, 100, 96, 92, 93}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 1, 300}, {Skill.LIEN_HOAN, 2, 400}, {Skill.LIEN_HOAN, 3, 500}, {Skill.LIEN_HOAN, 4, 600}, {Skill.LIEN_HOAN, 5, 700}, {Skill.LIEN_HOAN, 6, 800}, {Skill.LIEN_HOAN, 7, 900},
                {Skill.KAMEJOKO, 7, 600}, {Skill.KAMEJOKO, 6, 700}, {Skill.KAMEJOKO, 5, 800}, {Skill.KAMEJOKO, 4, 900}, {Skill.KAMEJOKO, 3, 1000}, {Skill.KAMEJOKO, 2, 1100}, {Skill.KAMEJOKO, 1, 1002},
                {Skill.ANTOMIC, 1, 130}, {Skill.ANTOMIC, 2, 140}, {Skill.ANTOMIC, 3, 150}, {Skill.ANTOMIC, 4, 160}, {Skill.ANTOMIC, 5, 170}, {Skill.ANTOMIC, 6, 190}, {Skill.ANTOMIC, 7, 200},
                {Skill.MASENKO, 1, 210}, {Skill.MASENKO, 5, 220}, {Skill.MASENKO, 6, 230},
                {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 7, 15000},
                {Skill.KHIEN_NANG_LUONG, 7, 50000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_1_M
    );
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final BossData SOI_HEC_QUYN = BossData.builder()
            .name("Sói Hẹc Quyn")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(1000)
            .hp(new int[]{10000})
            .outfit(new short[]{394, 395, 396, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData O_DO = BossData.builder()
            .name("Ở Dơ")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(3000)
            .hp(new int[]{25000})
            .outfit(new short[]{400, 401, 402, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData XINBATO = BossData.builder()
            .name("Xinbatô")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(6000)
            .hp(new int[]{50000})
            .outfit(new short[]{359, 360, 361, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData CHA_PA = BossData.builder()
            .name("Cham pa")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(9000)
            .hp(new int[]{100000})
            .outfit(new short[]{362, 363, 364, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData THAN_MEO = BossData.builder()
            .name("Thần mèo Karin")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(9000)
            .hp(new int[]{100000})
            .outfit(new short[]{89, 90, 91, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData PON_PUT = BossData.builder()
            .name("Pon pút")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(15000)
            .hp(new int[]{250000})
            .outfit(new short[]{365, 366, 367, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData CHAN_XU = BossData.builder()
            .name("Chan xư")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(20000)
            .hp(new int[]{500000})
            .outfit(new short[]{371, 372, 373, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData TAU_PAY_PAY = BossData.builder()
            .name("Tàu Pảy Pảy")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(30000)
            .hp(new int[]{2000000})
            .outfit(new short[]{92, 93, 94, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData YAMCHA = BossData.builder()
            .name("Yamcha")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(40000)
            .hp(new int[]{5000000})
            .outfit(new short[]{374, 375, 376, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData JACKY_CHUN = BossData.builder()
            .name("Jacky Chun")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(50000)
            .hp(new int[]{25000000})
            .outfit(new short[]{356, 357, 358, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData THIEN_XIN_HANG = BossData.builder()
            .name("Thiên Xin Hăng")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(100000)
            .hp(new int[]{75000000})
            .outfit(new short[]{368, 369, 370, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.THAI_DUONG_HA_SAN, 1, 30000}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData THIEN_XIN_HANG_CLONE = BossData.builder()
            .name("Thiên Xin Hăng")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(10000)
            .hp(new int[]{100000})
            .outfit(new short[]{368, 369, 370, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700}, //                    {Skill.THAI_DUONG_HA_SAN, 1, 15000}
    })
            .secondsRest(REST_5_S)
            .build();

    public static final BossData LIU_LIU = BossData.builder()
            .name("Liu Liu")
            .gender(ConstPlayer.TRAI_DAT)
            .dame(250000)
            .hp(new int[]{1000000000})
            .outfit(new short[]{397, 398, 399, -1, -1, -1})
            .mapJoin(new int[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(REST_5_S)
            .build();
}
