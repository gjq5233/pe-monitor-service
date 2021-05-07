package cn.edu.hfut.pe.dcu.comm.msg;

import base.util.BytesUtil;
import base.util.SeqProducer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class _44LogReq extends Msg {
	private short offset;

	private short LogCount;

	public _44LogReq() {
		super.func = 0x44;
	}

	public _44LogReq(short offset, short LogCount) {
		this();
		this.offset = offset;
		this.LogCount = LogCount;
	}
	
	public static _44LogReq create__44LogReq(byte unitId, short offset, short LogCount) {
		_44LogReq req = new _44LogReq(offset, LogCount);
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
		bf.writeShort(LogCount);
		return bf;
	}

	public short getOffset() {
		return offset;
	}

	public void setOffset(short offset) {
		this.offset = offset;
	}

	public short getLogCount() {
		return LogCount;
	}

	public void setLogCount(short logCount) {
		LogCount = logCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_44LogReq [offset=");
		builder.append(offset);
		builder.append(", LogCount=");
		builder.append(LogCount);
		builder.append("]");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		_44LogReq req = new _44LogReq((short) 00, (short) 10);
		req.setUnitId((byte) 1);
		req.setSeq(SeqProducer.genLocalSeq());
		ByteBuf bf = Unpooled.buffer(20);
		req.pack(bf);
		System.out.println(BytesUtil.hexDump(bf));
	}

}
