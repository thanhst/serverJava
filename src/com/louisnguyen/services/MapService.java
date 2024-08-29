package com.louisnguyen.services;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.girlkun.network.io.Message;
import com.louisnguyen.consts.ConstMap;
import com.louisnguyen.models.map.Map;
import com.louisnguyen.models.map.WayPoint;
import com.louisnguyen.models.map.Zone;
//import com.louisnguyen.models.map.DoanhTraiDocNhan.DoanhTraiService;
import com.louisnguyen.models.mob.Mob;
import com.louisnguyen.models.player.Player;
import com.louisnguyen.server.Manager;
import com.louisnguyen.services.func.ChangeMapService;
import com.louisnguyen.utils.Logger;
import com.louisnguyen.utils.Util;

/**
 *
 * @author 💖 Louis Nguyễn 💖
 * @copyright 💖 Louis Nguyễn 💖
 *
 */
public class MapService {

    private static MapService i;

    public static MapService gI() {
        if (i == null) {
            i = new MapService();
        }
        return i;
    }

    public WayPoint getWaypointPlayerIn(Player player) {
        for (WayPoint wp : player.zone.map.wayPoints) {
            if (player.location.x >= wp.minX && player.location.x <= wp.maxX && player.location.y >= wp.minY && player.location.y <= wp.maxY) {
                return wp;
            }
        }
        return null;
    }

    /**
     * @param tileTypeFocus tile type: top, bot, left, right...
     * @return [tileMapId][tileType]
     */
    public int[][] readTileIndexTileType(int tileTypeFocus) {
        int[][] tileIndexTileType = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_set_info"));
            int numTileMap = dis.readByte();
            tileIndexTileType = new int[numTileMap][];
            for (int i = 0; i < numTileMap; i++) {
                int numTileOfMap = dis.readByte();
                for (int j = 0; j < numTileOfMap; j++) {
                    int tileType = dis.readInt();
                    int numIndex = dis.readByte();
                    if (tileType == tileTypeFocus) {
                        tileIndexTileType[i] = new int[numIndex];
                    }
                    for (int k = 0; k < numIndex; k++) {
                        int typeIndex = dis.readByte();
                        if (tileType == tileTypeFocus) {
                            tileIndexTileType[i][k] = typeIndex;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
        return tileIndexTileType;
    }

    //tilemap for paint
    public int[][] readTileMap(int mapId) {
        int[][] tileMap = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_map_data/" + mapId));
            dis.readByte();
            int w = dis.readByte();
            int h = dis.readByte();
            tileMap = new int[h][w];
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    tileMap[i][j] = dis.readByte();
                }
            }
            dis.close();
        } catch (Exception e) {
        }
        return tileMap;
    }

    public Zone getMapCanJoin(Player player, int mapId, int zoneId) {
        if (isMapOffline(mapId)) {
            return getMapById(mapId).zones.get(0);
        }
        if (this.isMapDoanhTrai(mapId)) {
            if (player.clan == null || player.clan.doanhTrai == null) {
                return null;
            }
            if (this.isMapDoanhTrai(player.zone.map.mapId)) {
                for (Mob mob : player.zone.mobs) {
                    if (!mob.isDie()) {
                        return null;
                    }
                }
                for (Player boss : player.zone.getBosses()) {
                    if (!boss.isDie()) {
                        return null;
                    }
                }
            }
            /**
             * Qua map mới thì làm mới lại mob
             */
            if (player.clan.doanhTrai.getListMap().indexOf(mapId) > player.clan.doanhTrai.getCurrentIndexMap()) {

                player.clan.doanhTrai.setCurrentIndexMap(player.clan.doanhTrai.getListMap().indexOf(mapId));
                player.clan.doanhTrai.init();

            }
            return player.clan.doanhTrai.getMapById(mapId);
        }
        if (this.isMapGiaiCuuMiNuong(mapId)) {
            if (player.clan == null || player.clan.giaiCuuMiNuong == null) {
                return null;
            }
            if (this.isMapGiaiCuuMiNuong(player.zone.map.mapId)) {
                for (Mob mob : player.zone.mobs) {
                    if (!mob.isDie()) {
                        return null;
                    }
                }
                for (Player boss : player.zone.getBosses()) {
                    if (!boss.isDie()) {
                        return null;
                    }
                }
            }
            return player.clan.giaiCuuMiNuong.getMapById(mapId);
        }

        if (this.isMapBanDoKhoBau(mapId)) {
            if (player.clan == null || player.clan.banDoKhoBau == null) {
                return null;
            }
            if (this.isMapBanDoKhoBau(player.zone.map.mapId)) {
                for (Mob mob : player.zone.mobs) {
                    if (!mob.isDie()) {
                        return null;
                    }
                }
                for (Player boss : player.zone.getBosses()) {
                    if (!boss.isDie()) {
                        return null;
                    }
                }
            }
            /**
             * Qua map mới thì làm mới lại mob
             */
            if (player.clan.banDoKhoBau.getListMap().indexOf(mapId) > player.clan.banDoKhoBau.getCurrentIndexMap()) {
                player.clan.banDoKhoBau.setCurrentIndexMap(player.clan.banDoKhoBau.getListMap().indexOf(mapId));
                player.clan.banDoKhoBau.init();

            }
            return player.clan.banDoKhoBau.getMapById(mapId);
        }
        if (this.isMapKhiGaHuyDiet(mapId)) {
            if (player.clan == null) {
                return null;
            }
        }
        //**********************************************************************
        if (zoneId == -1) { //vào khu bất kỳ
            return getZone(mapId);
        } else {
            return getZoneByMapIDAndZoneID(mapId, zoneId);
        }
    }

    public Zone getZone(int mapId) {
        Map map = getMapById(mapId);
        if (map == null) {
            return null;
        }
        // int z = Util.nextInt(0, map.zones.size() - 1);
        int z= 0;
        while (map.zones.get(z).getNumOfPlayers() >= map.zones.get(z).maxPlayer) {
            z++;
        }
        return map.zones.get(z);
    }

    private Zone getZoneByMapIDAndZoneID(int mapId, int zoneId) {
        Zone zoneJoin = null;
        try {
            Map map = getMapById(mapId);
            if (map != null) {
                zoneJoin = map.zones.get(Util.nextInt(0, map.zones.size() - 1));
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
        return zoneJoin;
    }

    public Map getMapById(int mapId) {
        for (Map map : Manager.MAPS) {
            if (map.mapId == mapId) {
                return map;
            }
        }
        return null;
    }

//    public Map getMapForCalich() {
//        int mapId = Util.nextInt(27, 29);
//        return MapService.gI().getMapById(mapId);
//    }
    /**
     * Trả về 1 map random cho boss
     */
    public Zone getMapWithRandZone(int mapId) {
        Map map = MapService.gI().getMapById(mapId);
        Zone zone = null;
        try {
            if (map != null) {
                zone = map.zones.get(Util.nextInt(0, map.zones.size() - 1));
            }
        } catch (Exception e) {
        }
        return zone;
    }

    public String getPlanetName(byte planetId) {
        switch (planetId) {
            case 0:
                return "Trái đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    /**
     * lấy danh sách map cho capsule
     */
    public List<Zone> getMapCapsule(Player pl) {
        List<Zone> list = new ArrayList<>();
        if (pl.mapBeforeCapsule != null
                && pl.mapBeforeCapsule.map.mapId != 21
                && pl.mapBeforeCapsule.map.mapId != 22
                && pl.mapBeforeCapsule.map.mapId != 23
                && !isMapTuongLai(pl.mapBeforeCapsule.map.mapId)) {
            addListMapCapsule(pl, list, pl.mapBeforeCapsule);
        }
        addListMapCapsule(pl, list, getMapCanJoin(pl, 21 + pl.gender, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 47, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 45, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 0, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 7, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 14, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 5, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 20, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 13, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 24 + pl.gender, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 27, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 19, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 79, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 84, 0));
        return list;
    }

    public List<Zone> getMapBlackBall() {
        List<Zone> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(getMapById(85 + i).zones.get(0));
        }
        return list;
    }

    public List<Zone> getMapMaBu() {
        List<Zone> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(getMapById(114 + i).zones.get(0));
        }
        return list;
    }

    public List<Zone> getMapBamBo() {
        List<Zone> list = new ArrayList<>();
        list.add(getMapById(27).zones.get(0));
        return list;
    }

    private void addListMapCapsule(Player pl, List<Zone> list, Zone zone) {
        for (Zone z : list) {
            if (z != null && zone != null && z.map.mapId == zone.map.mapId) {
                return;
            }
        }
        if (zone != null && pl.zone.map.mapId != zone.map.mapId) {
            list.add(zone);
        }
    }

    public void sendPlayerMove(Player player) {
        Message msg;
        try {
            msg = new Message(-7);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }

    public boolean isMapOffline(int mapId) {
        for (Map map : Manager.MAPS) {
            if (map.mapId == mapId) {
                return map.type == ConstMap.MAP_OFFLINE;
            }
        }
        return false;
    }

    public boolean isMapBlackBallWar(int mapId) {
        return mapId >= 85 && mapId <= 91;
    }

    public boolean isMapMaBu(int mapId) {
        return mapId >= 114 && mapId <= 120;
    }

    public boolean isMapBamBoo(int mapId) {
        return mapId == 27;
    }

    public boolean isMapTapLuyen(int mapId) {
        return mapId == 189 && mapId == 190 && mapId == 191;
    }

    public boolean isMapPVP(int mapId) {
        return mapId == 112;
    }

    public boolean isMapCold(Map map) {
        int mapId = map.mapId;
        return mapId >= 105 && mapId <= 110;
    }

    public boolean isVungDatThamHiem(Map map) {
        int mapId = map.mapId;
        return mapId == 184;
    }

    public boolean isMapTL(Map map) {
        int mapId = map.mapId;
        return (mapId >= 92 && mapId <= 94)
                || (mapId >= 96 && mapId <= 100)
                || mapId == 102 || mapId == 103;
    }

    public boolean isMapDoanhTrai(int mapId) {
        return mapId >= 53 && mapId <= 62;
    }

    public boolean isMapChienTruong(int mapId) {
        return mapId == 196;
    }

    public boolean isVungDat01(int mapId) {
        return mapId == 184;
    }

    public boolean isMapGiaiCuuMiNuong(int mapId) {
        return mapId >= 185 && mapId <= 188;
    }

    public boolean isNguHanhSon(int mapId) {
        return mapId >= 122 && mapId <= 124;
    }

    public boolean isMapEventt(int mapId) {
        return mapId >= 170 && mapId <= 173;
    }

    public boolean isMapKhiGaHuyDiet(int mapId) {
        return mapId == 149 || mapId == 150 || mapId == 151 || mapId == 152 || mapId == 153;
    }

    public boolean isMapConDuongRanDoc(int mapId) {
        return mapId == 141 || mapId == 142 || mapId == 143 || mapId == 144;
    }

    public boolean isMapHuyDiet(int mapId) {
        return mapId >= 200 && mapId <= 201;
    }

    public boolean isMapBanDoKhoBau(int mapId) {
        return mapId >= 135 && mapId <= 138;
    }

    public boolean isMapTuongLai(int mapId) {
        return (mapId >= 92 && mapId <= 94)
                || (mapId >= 96 && mapId <= 100)
                || mapId == 102 || mapId == 103;
    }

    public void goToMap(Player player, Zone zoneJoin) {
        Zone oldZone = player.zone;
        if (oldZone != null) {
            ChangeMapService.gI().exitMap(player);
            if (player.mobMe != null) {
                player.mobMe.goToMap(zoneJoin);
            }
        }
        player.zone = zoneJoin;
        player.zone.addPlayer(player);
        // System.out.println("Mapservice");
    }
}
