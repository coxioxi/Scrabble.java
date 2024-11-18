/**
 * <p>
 *     Provides the classes necessary for communication between a host and its clients.
 * </p>
 * <p>
 *     All communication between host and client is performed by reading and writing
 *     {@link scrabble.network.messages message} objects. A client must establish a <code>Socket</code>
 *     to a listening host before a game starts. The host will then listen for Message objects
 *     to be sent over the network.
 * </p>
 * <p>
 *     Use {@link scrabble.controller.Controller.ClientMessenger ClientMessenger} as a client and
 *     {@link scrabble.network.PartyHost PartyHost} as the host.
 *     A host may network with up to 4 clients. Note that the application which
 *     spawns the host is considered to be a client as well.
 * </p>
 */
package scrabble.network;