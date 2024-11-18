/**
 * <p>
 *     Manages {@link scrabble.view}, {@link scrabble.model}, and {@link scrabble.network}
 *     components to make scrabble runnable.
 * </p>
 * <p>
 *     {@link scrabble.controller.Controller} makes the application runnable. It uses
 *     the classes of other packages as implementers and dons the role of client.
 *     The controller adds action listeners to components of view objects. Each action
 *     listener is responsible for updating the model as necessary and sending
 *     {@link scrabble.network.messages} objects to the host, who then relays the messages to the
 *     other players. If the application running on this computer is designated as the
 *     {@link scrabble.network.PartyHost}, then controller maintains a reference to
 *     the party host for convenience.
 * </p>
 * <p>
 * @see scrabble.network.messages messages
 * </p>
 */
package scrabble.controller;