/**
 * <p>
 *     Contains classes necessary for communication with clients and for maintaining the tile bag.
 * </p>
 * <p>
 *     <b>Overview</b>
 *     <br>
 *     The primary class {@link scrabble.network.host.PartyHost} should be used and run as the host object.
 *     PartyHost uses the remaining classes as implementers. PartyHost spawns
 *     {@link scrabble.network.host.HostReceiver} threads for client listening and sending.
 *     PartyHost uses {@link scrabble.network.host.TileBag} to randomly select tiles from the
 *     set number of tiles created at the game's beginning.
 * </p>
 * <p>
 *     <b>Using PartyHost</b>
 *     <br>
 *     PartyHost should be used as follows:
 *     	<ol>
 *     	    <li>Create a PartyHost object.</li>
 *     	    <li>Get the PartyHost IP address and port for display ({@link scrabble.network.host.PartyHost#getIPAdress},
 *     	    		{@link scrabble.network.host.PartyHost#getPort}). </li>
 *     	    <li>Call {@link scrabble.network.host.PartyHost#run}.</li>
 *     	    <li>Call {@link scrabble.network.host.PartyHost#startGame}.</li>
 *     	</ol>
 *     	Item 3 accepts clients to the server. Item 4 disables acceptance and informs players that the game has started.
 * </p>
 */
package scrabble.network.host;