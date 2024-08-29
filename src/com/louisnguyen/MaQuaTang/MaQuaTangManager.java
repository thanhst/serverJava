/*
 * Louis Nguyễn
*/
package com.louisnguyen.MaQuaTang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.girlkun.database.GirlkunDB;
import com.louisnguyen.models.item.Item.ItemOption;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.services.InventoryServiceNew;
import com.louisnguyen.services.NpcService;
import com.louisnguyen.services.Service;
import com.louisnguyen.utils.Logger;

/**
 *
 * @author Administrator
 */
public class MaQuaTangManager {
    public String name;
    public final ArrayList<MaQuaTang> listGiftCode = new ArrayList<>();

    private static MaQuaTangManager instance;

    public static MaQuaTangManager gI() {
        if (instance == null) {
            instance = new MaQuaTangManager();
        }
        return instance;
    }

    public void init() {
        try (Connection con = GirlkunDB.getConnection();) {

            PreparedStatement ps = con.prepareStatement("SELECT * FROM giftcode");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MaQuaTang giftcode = new MaQuaTang();
                ArrayList<Integer> tempListIdPlayer = new ArrayList<>();
                String tempDBListIdPlayers = null;
                giftcode.code = rs.getString("code")/* +Util.nextInt(111, 999) */;
                giftcode.countLeft = rs.getInt("count_left");
                giftcode.datecreate = rs.getTimestamp("datecreate");
                giftcode.dateexpired = rs.getTimestamp("expired");
                String dbListIdPlayer = rs.getString("listIdPlayers");
                JSONArray jar = (JSONArray) JSONValue.parse(rs.getString("detail"));
                if (jar != null) {
                    for (int i = 0; i < jar.size(); ++i) {
                        JSONObject jsonObj = (JSONObject) jar.get(i);
                        giftcode.detail.put(Integer.parseInt(jsonObj.get("id").toString()),
                                Integer.parseInt(jsonObj.get("quantity").toString()));
                        jsonObj.clear();
                    }
                }

                JSONArray option = (JSONArray) JSONValue.parse(rs.getString("itemoption"));
                Logger.log("Done-------------------" + option.toString());
                if (option != null) {
                    for (int u = 0; u < option.size(); u++) {
                        JSONObject jsonobject = (JSONObject) option.get(u);
                        giftcode.option.add(new ItemOption(Integer.parseInt(jsonobject.get("id").toString()),
                                Integer.parseInt(jsonobject.get("param").toString())));
                        jsonobject.clear();

                    }

                }

                if (!dbListIdPlayer.isEmpty()) {
                    tempDBListIdPlayers = dbListIdPlayer = removeCharAt(dbListIdPlayer, 0);
                    tempDBListIdPlayers = dbListIdPlayer = removeCharAt(dbListIdPlayer, dbListIdPlayer.length() - 1);
                    String[] resultTempDBListPlayer = tempDBListIdPlayers.split(",");
                    for (String item : resultTempDBListPlayer) {
                        if (!item.isEmpty())
                            tempListIdPlayer.add(Integer.parseInt(item));
                    }
                    giftcode.listIdPlayer = tempListIdPlayer;
                }
                listGiftCode.add(giftcode);
            }
            con.close();
        } catch (Exception erorlog) {
            erorlog.printStackTrace();
        }
    }

    public void updateGiftCodeListIdPlayer(ArrayList<Integer> listIdPlayers, String code) {
        try {
            String sql = "UPDATE giftcode set listIdPlayers=? where code=?";
            ArrayList<Integer> deDupStriList = new ArrayList<>(new HashSet<>(listIdPlayers));
            GirlkunDB.executeUpdate(sql, JSONValue.toJSONString(deDupStriList), code);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void sizeList(Player pl) {
        Service.gI().sendThongBao(pl, "" + MaQuaTang.class);
    }

    public MaQuaTang checkUseGiftCode(Player player, String code) {
        for (MaQuaTang giftCode : listGiftCode) {
            if (giftCode.code.equals(code) && giftCode.countLeft > 0 && !giftCode.isUsedGiftCode((int) player.id)) {
                Set<Integer> keySet = giftCode.detail.keySet();
                if (InventoryServiceNew.gI().getCountEmptyBag(player) >= keySet.size()) {
                    giftCode.countLeft -= 1;
                    giftCode.addPlayerUsed((int) player.id);
                    updateGiftCodeListIdPlayer(giftCode.listIdPlayer, code);
                    return giftCode;
                } else {
                    Service.gI().sendThongBao(player, "Hành trang không đủ chỗ trống! Vui lòng dọn " +keySet.size()+" ô để để đồ nhé!!");
                    return null;
                }
            }
        }
        return null;
    }

    public void checkInfomationGiftCode(Player p) {
        StringBuilder sb = new StringBuilder();
        for (MaQuaTang giftCode : listGiftCode) {
            sb.append("Code: ").append(giftCode.code).append(", Số lượng: ").append(giftCode.countLeft).append("\b")
                    .append("Ngày tạo: ")
                    .append(giftCode.datecreate).append("\b").append("Ngày hết hạn: ").append(giftCode.dateexpired)
                    .append("\b");
        }

        NpcService.gI().createTutorial(p, 5073, sb.toString());
    }

    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }
}
