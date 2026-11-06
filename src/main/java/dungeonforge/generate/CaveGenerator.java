package dungeonforge.generate;

import dungeonforge.config.RandomSource;
import dungeonforge.core.DungeonLevel;
import dungeonforge.core.Room;

import java.util.List;

public class CaveGenerator extends LevelGenerator {

    @Override
    protected void connectRooms(DungeonLevel level) {
        List<Room> rooms = level.getRooms();
        String[] directions = {"north", "south"};

        for (int r = 1; r < rooms.size(); r++) {
            int back = RandomSource.getInstance().nextInt(r);
            String direction = directions[
                    RandomSource.getInstance().nextInt(directions.length)];
            String reverse = direction.equals("north") ? "south" : "north";

            rooms.get(back).link(direction, rooms.get(r));
            rooms.get(r).link(reverse, rooms.get(back));
        }
    }

    @Override
    public String shapeName() {
        return "cave";
    }
}