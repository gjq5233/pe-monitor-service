package cn.edu.hfut.pe.dcu.comm.msg;

import base.util.BytesUtil;
import base.util.SeqProducer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class _06YtMsg extends MsgResp {
	private short offset;

	private short regValue;

	public _06YtMsg() {
		super.func = 0x06;
	}

	public _06YtMsg(short offset, short regCount) {
		this();
		this.offset = offset;
		this.regValue = regCount;
	}

	public static _06YtMsg create_06YtMsg(byte unitId, short offset, short regCount) {
		_06YtMsg req = new _06YtMsg(offset, regCount);
		req.setUnitId(unitId);
		req.setSeq(SeqProducer.genLocalSeq());
		return req;
	}

	@Override
	protected void parseBody(ByteBuf fullMsg) {
		// TODO Auto-generated method stub
		super.parseBody(fullMsg);
	}

	@Override
	protected ByteBuf packBody(ByteBuf bf) {
		bf.writeShort(offset);
		bf.writeShort(regValue);
		return bf;
	}

	public short getOffset() {
		return offset;
	}

	public void setOffset(short offset) {
		this.offset = offset;
	}

	public short getRegCount() {
		return regValue;
	}

	public void setRegCount(short regCount) {
		this.regValue = regCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_03Req [offset=");
		builder.append(offset);
		builder.append(", regCount=");
		builder.append(regValue);
		builder.append("]");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		_06YtMsg req = new _06YtMsg((short) 0x4C, (short) 2000);//电压稳压下限
		req.setUnitId((byte) 1);
		req.setSeq(SeqProducer.genLocalSeq());
		ByteBuf bf = Unpooled.buffer(20);
		req.pack(bf);
		System.out.println(BytesUtil.hexDump(bf));
	}


}
