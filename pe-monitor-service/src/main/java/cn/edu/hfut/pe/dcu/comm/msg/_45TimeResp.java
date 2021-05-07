package cn.edu.hfut.pe.dcu.comm.msg;

import base.util.TimeTool;
import io.netty.buffer.ByteBuf;

public class _45TimeResp extends MsgResp {

	private long time = System.currentTimeMillis();

	public _45TimeResp() {
		super.func = 0x45;
	}
	
	public _45TimeResp(Msg msg) {
		super(msg);
	}

	@Override
	protected ByteBuf packBody(ByteBuf bf) {
		bf.writeLong(time);
		return bf;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_45TimeResp [time=");
		builder.append(TimeTool.format(time));
		builder.append("]");
		return builder.toString();
	}

}
