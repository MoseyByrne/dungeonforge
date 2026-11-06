package dungeonforge.generate;

import dungeonforge.core.DungeonLevel;
import dungeonforge.core.Room;

import java.util.List;

public class RingGenerator extends LevelGenerator {

    @Override
    protected void connectRooms(DungeonLevel level) {
        List<Room> rooms = level.getRooms();
        int count = rooms.size();

        for (int r = 0; r < count; r++) {
            Room here = rooms.get(r);
            Room next = rooms.get((r + 1) % count);
            here.link("north", next);
            next.link("south", here);
        }
    }

    @Override
    public String shapeName() {
        return "ring";
    }
}