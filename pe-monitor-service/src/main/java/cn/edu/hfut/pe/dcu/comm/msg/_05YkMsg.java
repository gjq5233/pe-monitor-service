package cn.edu.hfut.pe.dcu.comm.msg;

import base.util.BytesUtil;
import base.util.SeqProducer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class _05YkMsg extends MsgResp {
	private short offset;// 线圈地址

	private short coilsValue; // 0xFF00 = on, 0x0000=off

	private boolean on;

	public _05YkMsg() {
		super.func = 0x05;
	}

	public _05YkMsg(short offset, boolean on) {
		this();
		this.offset = offset;
		this.on = on;
	}
	
	public _05YkMsg(short offset, short coilsValue) {
		this();
		this.offset = offset;
		this.on = (coilsValue&0xFFFF)==0xFF00;;
	}

	public static _05YkMsg create_05YkMsg(byte unitId, short coilsValue, boolean on) {
		_05YkMsg req = new _05YkMsg(coilsValue, on);
		req.setUnitId(unitId);
		req.setSeq(SeqProducer.genLocalSeq());
		return req;
	}
	
	@Override
	protected void parseBody(ByteBuf bf) {
		if (!super.isOk()) {
			super.parseBody(bf);
			return;
		} 
		offset = bf.readShort();
		coilsValue = bf.readShort();
		on = (coilsValue&0xFFFF)==0xFF00;
	}

	@Override
	protected ByteBuf packBody(ByteBuf bf) {
		bf.writeShort(offset);
		if (on) {
			coilsValue = (short) 0xFF00;
		} else {
			coilsValue = 0;
		}
		bf.writeShort(coilsValue);
		return bf;
	}

	public short getOffset() {
		return offset;
	}

	public void setOffset(short offset) {
		this.offset = offset;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_05YkMsg [offset=");
		builder.append(offset);
		builder.append(", on=");
		builder.append(on);
		builder.append("]");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		_05YkMsg req = new _05YkMsg((short) 10, true);
		req.setUnitId((byte) 1);
		req.setSeq(SeqProducer.genLocalSeq());
		ByteBuf bf = Unpooled.buffer(20);
		req.pack(bf);
		System.out.println(BytesUtil.hexDump(bf));
	}

}
