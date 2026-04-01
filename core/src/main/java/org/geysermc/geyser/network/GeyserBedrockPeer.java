/*
 * Copyright (c) 2019-2023 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.geyser.network;

import io.netty.channel.Channel;
import org.cloudburstmc.protocol.bedrock.BedrockPeer;
import org.cloudburstmc.protocol.bedrock.BedrockSession;
import org.cloudburstmc.protocol.bedrock.BedrockSessionFactory;

import java.net.SocketAddress;

public class GeyserBedrockPeer extends BedrockPeer {
    private SocketAddress proxiedAddress;

    /**
     * The subclient ID currently being created. Set before super.onSessionCreated()
     * triggers initSession(), so GeyserServerInitializer can read it.
     * Safe because session creation runs on the Netty event loop (single-threaded).
     * @see GeyserServerInitializer#initSession
     */
    private int pendingSubClientId = -1;

    public GeyserBedrockPeer(Channel channel, BedrockSessionFactory sessionFactory) {
        super(channel, sessionFactory);
    }

    @Override
    protected BedrockSession onSessionCreated(int subClientId) {
        this.pendingSubClientId = subClientId;
        BedrockSession session = super.onSessionCreated(subClientId);
        this.pendingSubClientId = -1;
        return session;
    }

    /**
     * Returns the subclient ID of the session currently being created.
     * Only valid during initSession() -- returns -1 otherwise.
     */
    int getPendingSubClientId() {
        return pendingSubClientId;
    }

    public SocketAddress getRealAddress() {
        SocketAddress proxied = this.proxiedAddress;
        return proxied == null ? this.getSocketAddress() : proxied;
    }

    public void setProxiedAddress(SocketAddress proxiedAddress) {
        this.proxiedAddress = proxiedAddress;
    }

    /**
     * Returns the session for the given subclient ID, or null if not found.
     * Used for split screen guest support to look up the main client's session (ID 0).
     */
    BedrockSession getSession(int subClientId) {
        return this.sessions.get(subClientId);
    }

    /**
     * Removes a subclient session from the peer's session map immediately.
     * Used when a split screen guest disconnects, so the subclient ID can be reused on rejoin.
     */
    void removeSubClientSession(BedrockSession session) {
        removeSession(session);
    }
}
