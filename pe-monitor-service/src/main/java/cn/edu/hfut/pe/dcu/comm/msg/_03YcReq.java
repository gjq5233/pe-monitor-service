package cn.edu.hfut.pe.dcu.comm.msg;

import base.util.BytesUtil;
import base.util.SeqProducer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class _03YcReq extends Msg {
	private short offset;

	private short regCount;

	public _03YcReq() {
		super.func = 0x03;
	}

	public _03YcReq(short offset, short regCount) {
		this();
		this.offset = offset;
		this.regCount = regCount;
	}

	public static _03YcReq create_03YcReq(byte unitId, short offset, short regCount) {
		_03YcReq req = new _03YcReq(offset, regCount);
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
		bf.writeShort(regCount);
		return bf;
	}

	public short getOffset() {
		return offset;
	}

	public void setOffset(short offset) {
		this.offset = offset;
	}

	public short getRegCount() {
		return regCount;
	}

	public void setRegCount(short regCount) {
		this.regCount = regCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_03Req [offset=");
		builder.append(offset);
		builder.append(", regCount=");
		builder.append(regCount);
		builder.append("]");
		return builder.toString();
	}

	public static void main(String[] args) {
		_03YcReq req = create_03YcReq((byte) 1, (short) 0, (short) 110);
		ByteBuf bf = Unpooled.buffer(20);
		req.pack(bf);
		System.out.println(BytesUtil.hexDump(bf));
	}
}
