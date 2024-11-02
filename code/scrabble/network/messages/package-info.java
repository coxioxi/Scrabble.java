/**
 * <p>
 *     Provides serializable objects which can be relayed between clients and the host (and in the opposite order).
 * </p>
 * <p>
 *     All message objects extend the abstract class {@link scrabble.network.messages.Message}.
 *     The GUI generates Message objects in response to events. Each message object implements
 *     the abstract {@link scrabble.network.messages.Message#execute(scrabble.controller.Controller)}
 *     method, which operates on the controller.
 *     This execute method updates the game model dynamically; if the player who creates the message
 *     is the same as the application's Local player object, then the message is relayed to the host.
 * </p>
 * <p>
 *     Similarly, the abstract {@link scrabble.network.messages.Message#execute(scrabble.network.host.PartyHost)}
 *     operates on the <code>PartyHost</code> object. Calling <code>execute</code> makes any
 *     necessary changes to the host (such as returning tiles, ending the listening thread, etc.),
 *     then sends to the other clients.
 * </p>
 * <p>
 * @see scrabble.model.LocalPlayer LocalPlayer
 * </p>
 */
package scrabble.network.messages;