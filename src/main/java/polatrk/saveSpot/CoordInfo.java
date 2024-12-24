package polatrk.saveSpot;

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
    }

    public CoordInfo(UUID playerUUID, boolean isPublic, String spotName, Vector3 coords) {
        this.playerUUID = playerUUID;
        this.isPublic = isPublic;
        this.spotName = spotName;
        this.coords = coords;
    }
}
