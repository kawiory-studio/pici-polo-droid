package pl.dom3k.picipolodelux;

/**
 * Created by domek on 10.05.16.
 */
public interface PiciServerable {
    /**
     * Returns name of game, when player wants to have one, allowing others to connect to it.
     * It is recommended that games are named by some common words + number, if necessary (lack of free names),
     * like "cat", "dog", "tank1", "inu324"
     * @return name of the game
     */
    String newGameName();

    /**
     * Returns, which user/team has "right to move" now, which means that method is usually used
     * to tell whether is our turn to move or to wait, and let application react.
     * @return name of player/team, like "dom3k", "Red", "Blue"
     */
    String whoseTurn();
}
