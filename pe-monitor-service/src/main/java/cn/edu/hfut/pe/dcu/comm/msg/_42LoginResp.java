package cn.edu.hfut.pe.dcu.comm.msg;

import io.netty.buffer.ByteBuf;

public class _42LoginResp extends MsgResp {
	private byte result; // 0正常

	public _42LoginResp() {
		super.func = 0x42;
	}

	public _42LoginResp(Msg req) {
		super.seq = req.seq;
		super.func = req.func;
		super.unitId = req.unitId;

	}

	public _42LoginResp(byte result) {
		this();
		this.result = result;
	}

	@Override
	protected ByteBuf packBody(ByteBuf bf) {
		if(!super.isOk() || result != 0) {
			return super.packBody(bf);
		}
		return bf.writeByte(0);
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
		this.setExcepCode(result);
		if (result != 0) {
			super.func = (byte) (super.func | 0x80);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_42LoginResp [result=");
		builder.append(result);
		builder.append("]");
		return builder.toString();
	}

}
