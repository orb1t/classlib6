/*
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.HeaderList;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.message.EmptyMessageImpl;
import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
import javax.xml.transform.Source;

import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalMessageContext;

/**
 * Implementation of LogicalMessageContext. This class is used at runtime
 * to pass to the handlers for processing logical messages.
 *
 * <p>This Class delegates most of the fuctionality to Packet
 *
 * @see Packet
 *
 * @author WS Development Team
 */
class LogicalMessageContextImpl extends MessageUpdatableContext implements LogicalMessageContext {
    private LogicalMessageImpl lm;
    private WSBinding binding;

    public LogicalMessageContextImpl(WSBinding binding, Packet packet) {
        super(packet);
        this.binding = binding;
    }

    public LogicalMessage getMessage() {
        if(lm == null)
            lm = new LogicalMessageImpl(packet);
        return lm;
    }

    void setPacketMessage(Message newMessage){
        if(newMessage != null) {
            packet.setMessage(newMessage);
            lm = null;
    }
    }


    protected void updateMessage() {
        //If LogicalMessage is not acccessed, its not modified.
        if(lm != null) {
            //Check if LogicalMessageImpl has changed, if so construct new one
            //TODO: Attachments are not used
            // Packet are handled through MessageContext
            if(lm.isPayloadModifed()){
                Message msg = packet.getMessage();
                HeaderList headers = msg.getHeaders();
                AttachmentSet attachments = msg.getAttachments();
                Source modifiedPayload = lm.getModifiedPayload();
                if(modifiedPayload == null){
                    packet.setMessage(new EmptyMessageImpl(headers,attachments,binding.getSOAPVersion()));
                } else {
                    packet.setMessage(new PayloadSourceMessage(headers,modifiedPayload, attachments, binding.getSOAPVersion()));
    }
    }
            lm = null;
    }

    }

}
