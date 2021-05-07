package cn.edu.hfut.pe.dcu.comm.msg;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;

public class _02YxResp extends MsgResp {

	public _02YxResp() {
		super.func = 0x02;
	}

	private byte byteCount;

	private byte[] data;

	@Override
	protected void parseBody(ByteBuf bf) {
		if (!super.isOk()) {
			super.parseBody(bf);
			return;
		} 

		byteCount = bf.readByte();
		data = new byte[byteCount];
		bf.readBytes(data);
	}

	public byte getByteCount() {
		return byteCount;
	}

	public void setByteCount(byte byteCount) {
		this.byteCount = byteCount;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_02YxResp [byteCount=");
		builder.append(byteCount);
		builder.append(", data=");
		builder.append(Arrays.toString(data));
		builder.append("]");
		return builder.toString();
	}

}
