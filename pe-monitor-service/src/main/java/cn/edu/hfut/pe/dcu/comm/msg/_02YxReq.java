package cn.edu.hfut.pe.dcu.comm.msg;

import base.util.BytesUtil;
import base.util.SeqProducer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class _02YxReq extends Msg {
	private short offset;

	private short bitCount;

	public _02YxReq() {
		super.func = 0x02;
	}

	public _02YxReq(short offset, short bitCount) {
		this();
		this.offset = offset;
		this.bitCount = bitCount;
	}
	
	public static _02YxReq create_02YxReq(byte unitId, short offset, short bitCount) {
		_02YxReq req = new _02YxReq(offset, bitCount);
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
		bf.writeShort(bitCount);
		return bf;
	}

	public short getOffset() {
		return offset;
	}

	public void setOffset(short offset) {
		this.offset = offset;
	}

	public short getBitCount() {
		return bitCount;
	}

	public void setBitCount(short bitCount) {
		this.bitCount = bitCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_02YxReq [offset=");
		builder.append(offset);
		builder.append(", bitCount=");
		builder.append(bitCount);
		builder.append("]");
		return builder.toString();
	}

	public static void main(String[] args) {
		_02YxReq req = new _02YxReq((short) 0, (short) 32);
		req.setUnitId((byte) 1);
		req.setSeq(SeqProducer.genLocalSeq());
		ByteBuf bf = Unpooled.buffer(20);
		req.pack(bf);
		System.out.println(BytesUtil.hexDump(bf));
	}

}
