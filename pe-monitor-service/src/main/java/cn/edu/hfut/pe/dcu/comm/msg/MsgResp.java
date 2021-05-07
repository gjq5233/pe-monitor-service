package cn.edu.hfut.pe.dcu.comm.msg;

import io.netty.buffer.ByteBuf;

public class MsgResp extends Msg {
	protected byte excepCode = ExcepCode.OK; // 0正常

	public MsgResp() {
	}

	public MsgResp(Msg req) {
		super.seq = req.seq;
		super.func = req.func;
		super.unitId = req.unitId;
	}

	@Override
	public void pack(ByteBuf bf) {
		if(!isOk()) {
			super.func |= 0x80;
		}
		super.pack(bf);
	}

	public static boolean isOk(byte excepCode) {
		return excepCode == 0;
	}

	public boolean isOk() {
		return excepCode == 0;
	}

	@Override
	protected void parseBody(ByteBuf bf) {
		excepCode = bf.readByte();
	}

	@Override
	protected ByteBuf packBody(ByteBuf bf) {
		bf.writeByte(excepCode);
		return bf;
	}

	public byte getExcepCode() {
		return excepCode;
	}

	public void setExcepCode(byte excepCode) {
		this.excepCode = excepCode;
		if (!isOk()) {
			super.func = (byte) (super.func | 0x80);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MsgResp [excepCode=");
		builder.append(excepCode);
		builder.append(", seq=");
		builder.append(seq);
		builder.append(", unitId=");
		builder.append(unitId);
		builder.append(", func=");
		builder.append(func);
		builder.append("]");
		return builder.toString();
	}

}
