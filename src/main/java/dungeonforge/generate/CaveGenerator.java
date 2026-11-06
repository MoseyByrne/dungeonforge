package dungeonforge.generate;

import dungeonforge.core.DungeonLevel;
import dungeonforge.core.Room;

import java.util.List;

public class CaveGenerator extends LevelGenerator {

    @Override
    protected void connectRooms(DungeonLevel level) {
        List<Room> rooms = level.getRooms();

        for (int r = 1; r < rooms.size(); r++) {
            attachToSomeEarlierRoom(rooms, r);
        }
    }

    private void attachToSomeEarlierRoom(List<Room> rooms, int r) {
        Room child = rooms.get(r);

        for (int attempt = 0;
             attempt < r * DIRECTIONS.length * 2;
             attempt++) {

            Room parent = rooms.get(rng().nextInt(r));
            String direction = DIRECTIONS[rng().nextInt(DIRECTIONS.length)];

            if (tryConnect(parent, direction, child)) {
                return;
            }
        }

        // Check every remaining possibility if random attempts were unlucky.
        for (int i = 0; i < r; i++) {
            for (String direction : DIRECTIONS) {
                if (tryConnect(rooms.get(i), direction, child)) {
                    return;
                }
            }
        }

        throw new IllegalStateException("Could not attach room " + r);
    }

    @Override
    public String shapeName() {
        return "cave";
    }
}