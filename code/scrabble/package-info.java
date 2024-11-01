/**
 * Provides the classes and packages necessary to run a multiplayer, network-dependent,
 * point-and-click game of Scrabble on a computer.
 * <p>
 *     The application is runnable through {@link scrabble.controller.Controller#main(java.lang.String[])}.
 *     At least two players must use the same network to play a game of Scrabble. One player must
 *     select the option to "Host", while any other player(s) should join the game by entering
 *     the IP address of the host (displayed on selection). Players should enter their name to
 *     join a party. The host must select game options or use the defaults before starting the game.
 *     Upon starting, the software will enforce the options selected. Players may choose to play tiles,
 *     pass their turn, or exchange tiles at any point on their turn.
 * </p>
 * <p>
 * 		See <a href = "https://github.com/bwittman/comp3100-fall2024-2/blob/main/Docs/Team2-FinalDesign.docx">
 *     Design Documentation</a>
 *     for a more detailed description of components and their interactions.
 * </p>
 */
package scrabble;

