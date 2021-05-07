/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.edu.hfut.pe.dcu.comm;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.util.BytesUtil;
import cn.edu.hfut.pe.dcu.comm.msg.Msg;
import cn.edu.hfut.pe.dcu.comm.msg.MsgResp;
import cn.edu.hfut.pe.dcu.cons.DcuAttrCons;
import cn.edu.hfut.pe.dcu.util.LogDcuidUtil;
import cn.edu.hfut.pe.model.entity.config.MON_DCU;

/**
 */
@Sharable
public class DcuServerEncoder extends MessageToByteEncoder<Msg> {
	private final static Logger LOG = LoggerFactory.getLogger(DcuServerEncoder.class);


	/**
	 * Creates a new instance.
	 *
	 */
	public DcuServerEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
		int writeIndexStart = out.writerIndex();
		msg.pack(out);
		int writeIndexEnd = out.writerIndex();
		char dli = ' ';
		/**
		 * 打印日志
		 */
		Channel ch = ctx.channel();
		MON_DCU dcu = ch.attr(DcuAttrCons.DCU).get();
		Long dcuid = null;
		if(dcu != null) {
			dcuid = dcu.getDCU_ID();
		}
		if(msg.isDown()) {
			LOG.warn("send, ch:{}, dcuid:{}, msg:{}", ch, dcuid, BytesUtil.hexDump(out, writeIndexStart, writeIndexEnd, dli));
		} else {
			boolean isOk = false;
			if(msg instanceof MsgResp) {
				MsgResp resp = (MsgResp) msg;
				isOk = resp.isOk();
			}
			if(!isOk) {
				LOG.warn("send, ch:{}, dcuid:{}, msg:{}", ch, dcuid, BytesUtil.hexDump(out, writeIndexStart, writeIndexEnd, dli));
			} else {
				LogDcuidUtil.log(ch, LOG, "send, ch:{}, dcuid:{}, msg:{}", ch, dcuid, BytesUtil.hexDump(out, writeIndexStart, writeIndexEnd, dli));
			}
		} 
	}
	
	
}
