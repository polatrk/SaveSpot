package polatrk.saveSpot;

import javax.naming.spi.DirStateFactory;
import java.io.Serializable;
import java.util.UUID;

public class CoordInfo implements Serializable {

    public UUID playerUUID;
    public boolean isPublic;
    public String spotName;
    public Vector3 coords;

    public static class Vector3 implements Serializable {
        public int x;
        public int y;
        public int z;

        public Vector3(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static double Distance(CoordInfo.Vector3 pos1, CoordInfo.Vector3 pos2) {
            return Math.sqrt(
                    Math.pow(pos2.x - pos1.x, 2) +
                            Math.pow(pos2.y - pos1.y, 2) +
                            Math.pow(pos2.z - pos1.z, 2)
            );
        }
    }

    public CoordInfo(UUID playerUUID, boolean isPublic, String spotName, Vector3 coords) {
        this.playerUUID = playerUUID;
        this.isPublic = isPublic;
        this.spotName = spotName;
        this.coords = coords;
    }
}
