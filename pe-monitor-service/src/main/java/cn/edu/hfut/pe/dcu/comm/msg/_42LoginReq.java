package cn.edu.hfut.pe.dcu.comm.msg;

import base.util.BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class _42LoginReq extends Msg {
	private long dcuid;

	private long imei;

	private int lac;

	private long ci;

	private byte check;

	private boolean isCheckOk = true;

	public _42LoginReq() {
		super.func = 0x42;
	}

	public _42LoginReq(long dcuid, long imei) {
		this();
		this.dcuid = dcuid;
		this.imei = imei;
	}

	@Override
	protected void parseBody(ByteBuf bf) {
		int readIndex = bf.readerIndex();
		dcuid = bf.readUnsignedInt();
		imei = bf.readLong();
		lac = bf.readUnsignedShort();
		ci = bf.readUnsignedInt();
		check = bf.readByte();
		isCheckOk = check(bf, readIndex-8, 26, check);
		System.out.println(isCheckOk);
	}

	public static boolean check(ByteBuf bf, int readIndex, int count, byte check) {
		int c = 0;
 		for (int i = readIndex; i < count + readIndex; i++) {
			c += bf.getUnsignedByte(i);
		}
		System.out.println(256 - c & 0xFF);
		return (256 - c & 0xFF) == (check & 0xFF);
//		return true;
	}

	public long getDcuid() {
		return dcuid;
	}

	public void setDcuid(long dcuid) {
		this.dcuid = dcuid;
	}

	public long getImei() {
		return imei;
	}

	public void setImei(long imei) {
		this.imei = imei;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}

	public long getCi() {
		return ci;
	}

	public void setCi(long ci) {
		this.ci = ci;
	}

	public byte getCheck() {
		return check;
	}

	public void setCheck(byte check) {
		this.check = check;
	}

	public boolean isCheckOk() {
		return isCheckOk;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_42LoginReq [dcuid=");
		builder.append(dcuid);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", lac=");
		builder.append(lac);
		builder.append(", ci=");
		builder.append(ci);
		builder.append(", check=");
		builder.append(check);
		builder.append("]");
		return builder.toString();
	}

	public static void main(String[] args) {
		System.out.println(Long.MAX_VALUE);

		String ss = "00 BC 61 4E 00 1E DC 1B 9E EA 3E 40 56 1B 04 69 49 EC";

		byte[] bb = BytesUtil.str2Bytes(ss.replace(" ", ""));

		ByteBuf bf = Unpooled.wrappedBuffer(bb);
		boolean r = check(bf, 0, bf.readableBytes(), (byte) 103);
		System.out.println(r);
		System.out.println(r);

		System.out.println(BytesUtil.bytes2Str((byte) 225));
		System.out.println(0xA3);
	}

}
