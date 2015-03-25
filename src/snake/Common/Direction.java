package snake.Common;

/**
 * Enum for directions
 * */
public enum Direction{
    UP,DOWN,LEFT,RIGHT {
        @Override
        public Direction next() {
            return UP; 
        };
    };

    public Direction next() {
        // No bounds checking required here, because the last instance overrides
        return values()[ordinal() + 1];
    }
}
