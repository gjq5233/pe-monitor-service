package cn.edu.hfut.pe.dcu.comm.msg;

import java.util.ArrayList;
import java.util.List;

import base.util.BytesUtil;
import base.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class _44LogResp extends MsgResp {

	public _44LogResp() {
		super.func = 0x44;
	}

	private short logCount;

	private List<String> logList = new ArrayList<>();

	@Override
	protected void parseBody(ByteBuf bf) {
		if (!super.isOk()) {
			super.parseBody(bf);
			return;
		}

		logCount = bf.readShort();
		if (logCount < 1 || logCount > 200) {
			System.err.println("logCount error");
			return;
		}
		for (int i = 0; i < logCount; i++) {
			if (bf.readableBytes() >= 2) {
				short byteCount = bf.readShort();
				if (byteCount < 1 || byteCount > 1024) {
					System.err.println("logCount error");
					return;
				}
				if (bf.readableBytes() < byteCount) {
					System.err.println("logCount error");
					return;
				}
				logList.add(bf.readCharSequence(byteCount, CharsetUtil.GBK).toString());
			}
		}
	}

	public short getLogCount() {
		return logCount;
	}

	public void setLogCount(short logCount) {
		this.logCount = logCount;
	}

	public List<String> getLogList() {
		return logList;
	}

	public void setLogList(List<String> logList) {
		this.logList = logList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_44LogResp [logCount=");
		builder.append(logCount);
		builder.append(", logList=");
		builder.append(logList);
		builder.append("]");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		
		System.out.println(BytesUtil.hexDump("[2021-03-30 17:05:30.337 遥控操作KA1断路器->合]".getBytes(CharsetUtil.GBK)));
		
		String s = "00 00 00 00 01 CA 01 44 00 0A 00 29 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 36 2E 34 38 36 20 4B 41 31 CE BB D6 C3 D7 B4 CC AC 2D 3E BA CF D5 A2 00 2F 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 36 2E 34 38 39 20 D7 D4 B6 AF B2 D9 D7 F7 4B 41 31 B6 CF C2 B7 C6 F7 2D 3E B7 D6 D5 A2 00 29 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 36 2E 36 30 38 20 4B 41 31 CE BB D6 C3 D7 B4 CC AC 2D 3E B7 D6 D5 A2 00 29 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 36 2E 36 31 36 20 4B 41 31 CE BB D6 C3 D7 B4 CC AC 2D 3E BA CF D5 A2 00 2F 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 36 2E 36 31 39 20 D7 D4 B6 AF B2 D9 D7 F7 4B 41 31 B6 CF C2 B7 C6 F7 2D 3E B7 D6 D5 A2 00 29 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 36 2E 38 31 39 20 4B 41 31 B6 CF C2 B7 C6 F7 B7 D6 D5 A2 BE DC B6 AF 00 2F 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 36 2E 38 31 39 20 D7 D4 B6 AF B2 D9 D7 F7 4B 41 31 B6 CF C2 B7 C6 F7 2D 3E B7 D6 D5 A2 00 29 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 37 2E 30 31 39 20 4B 41 31 B6 CF C2 B7 C6 F7 B7 D6 D5 A2 BE DC B6 AF 00 2F 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 37 2E 30 31 39 20 D7 D4 B6 AF B2 D9 D7 F7 4B 41 31 B6 CF C2 B7 C6 F7 2D 3E B7 D6 D5 A2 00 29 32 30 32 31 2D 30 34 2D 31 39 20 31 32 3A 34 30 3A 35 37 2E 30 36 37 20 4B 41 31 CE BB D6 C3 D7 B4 CC AC 2D 3E B7 D6 D5 A2 "
				;
		s = s.replaceAll(" ", "");
		byte[] bb = BytesUtil.str2Bytes(s);
		Object o = Msg.parseMsg(Unpooled.wrappedBuffer(bb));
		System.out.println(o);
		
		System.out.println(Short.MIN_VALUE);
		System.out.println(Short.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE);
	}


}
