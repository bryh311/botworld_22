package brain;

import actor.BotBrain;

/**
 * A bot that doesn't move
 */
public class DumbBot extends BotBrain {
    public int chooseAction() {
        return 1;
    }
}
