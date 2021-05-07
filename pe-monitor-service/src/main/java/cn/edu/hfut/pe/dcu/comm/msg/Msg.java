package cn.edu.hfut.pe.dcu.comm.msg;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.util.BytesUtil;
import cn.edu.hfut.pe.dcu.cons.FuncCode;
import cn.edu.hfut.pe.model.ModbusValueExps;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 
 * int slaveId, int range, int offset, int len
 */
public class Msg {
	private final static Logger LOG = LoggerFactory.getLogger(Msg.class);
	protected int seq;
	protected short length; //

	protected byte unitId;// slaveId, 串口地址 1-255

	protected byte func;// range功能码

	public final static byte getCmd(ByteBuf fullMsg) {
		return fullMsg.getByte(fullMsg.readerIndex() + 7);
	}

	public final static boolean isHeartbeat(ByteBuf fullMsg) {
		return Msg.getCmd(fullMsg) == 0x43;
	}

	public final static boolean isNeedLog(ByteBuf fullMsg) {
		byte cmd = Msg.getCmd(fullMsg);
		return isNeedLog(cmd);
	}

	public final static boolean isNeedLog(byte cmd) {
		return isDown(cmd);
	}

	public final static boolean isDown(byte cmd) {
		return cmd == FuncCode._05YK || cmd == FuncCode._06YT || cmd == FuncCode._44LOG;
	}

	public final static boolean isNeedAck(byte cmd) {
		return cmd == FuncCode._42LOGIN || cmd == FuncCode._45TIME;
	}

	public final static boolean isNeedLog(Msg msg) {
		byte cmd = msg.getFunc();
		return isNeedLog(cmd);
	}

	public final static Msg parseMsg(ByteBuf fullMsg) {
		byte func = getCmd(fullMsg);
		Msg mm = null;
		byte excepCode = 0;
		if (func < 0) {
			excepCode = -1;
			func = (byte) (func & 0x7F);
			LOG.warn("返回异常码{}", BytesUtil.hexDump(fullMsg));
		}

		switch (func) {
		case FuncCode._02YX:
			mm = new _02YxResp();
			break;
		case FuncCode._03YC:
			mm = new _03YcResp();
			break;
		case FuncCode._05YK:
			mm = new _05YkMsg();
			break;
		case FuncCode._06YT:
			mm = new _06YtMsg();
			break;
		case FuncCode._42LOGIN:
			mm = new _42LoginReq();
			break;
		case FuncCode._43HEARTBEAT:
			mm = new _43Heartbeat();
			break;
		case FuncCode._44LOG:
			mm = new _44LogResp();
			break;
		case FuncCode._45TIME:
			mm = new _45TimeReq();
			break;

		default:
			break;
		}
		if (mm == null) {
			return null;
		}
		if (!MsgResp.isOk(excepCode)) {
			((MsgResp) mm).setExcepCode(excepCode);
		}
		try {
			fullMsg.markReaderIndex();
			mm.parse(fullMsg);
		} catch (Exception e) {
			fullMsg.resetReaderIndex();
			LOG.warn("e:{}, bf:{}", e, BytesUtil.hexDump(fullMsg));
			return null;
		}

		return mm;
	}

	public final static boolean isDown(Msg msg) {
		return isDown(msg.func);
	}

	public final boolean isDown() {
		return isDown(func);
	}

	public Msg parse(ByteBuf fullMsg) {
		Objects.requireNonNull(fullMsg);

		ByteBuf bf = Unpooled.wrappedBuffer(fullMsg);
		seq = bf.readInt();
		length = bf.readShort();
		unitId = bf.readByte();
		func = bf.readByte();
		parseBody(bf);

		return this;
	}

	protected void parseBody(ByteBuf fullMsg) {
		// TODO Auto-generated method stub

	}

	protected ByteBuf packBody(ByteBuf bf) {
		return bf;
	}

	public void pack(ByteBuf bf) {
		bf.writeInt(seq);
		int writeIndex = bf.writerIndex();
		bf.writeShort(length);
		bf.writeByte(unitId);
		bf.writeByte(func);
		packBody(bf);
		int len = bf.readableBytes();
		length = (short) (len - 6);
//		bf.resetWriterIndex();
		bf.setShort(writeIndex, length);

		System.out.println(BytesUtil.hexDump(bf));
	}

	public final static Msg gentReqByGraph(MON_METRIC_CONFIG graph) {
		ModbusValueExps mve = graph.getMve();
		return gentReqByMve(mve);
	}
	
	public final static Msg gentReqByMve(ModbusValueExps mve ) {
		if (mve == null) {
			return null;
		}
		int cmd = mve.getFunc();
		int offset = mve.getOffset();
		int lenOrP = mve.getLenOrP();
		switch (cmd) {
		case 0x02:
			return new _02YxReq((short)offset, (short)lenOrP);
		case 0x03:
			return new _03YcReq((short)offset, (short)lenOrP);
		case 0x05:
			return new _05YkMsg((short)offset, (short)lenOrP);
		case 0x06:
			return new _06YtMsg((short)offset, (short)lenOrP);
		case 0x44:
			return new _44LogReq((short)offset, (short)lenOrP);

		default:
			break;
		}

		return null;
	}
	
	public final static Msg gentReqByVs(String vs) {
		ModbusValueExps mve = ModbusValueExps.getInstance(vs);
		return gentReqByMve(mve);
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public byte getUnitId() {
		return unitId;
	}

	public void setUnitId(byte unitId) {
		this.unitId = unitId;
	}

	public byte getFunc() {
		return func;
	}

	public void setFunc(byte func) {
		this.func = func;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Msg [seq=");
		builder.append(seq);
		builder.append(", length=");
		builder.append(length);
		builder.append(", unitId=");
		builder.append(unitId);
		builder.append(", func=");
		builder.append(func);
		builder.append("]");
		return builder.toString();
	}

	public static void main(String[] args) {
		System.out.println(2);
		byte func = 3;
		func |= 0x80;
		func |= 0x80;
		func |= 0x80;
		System.out.println(func);
		System.out.println(BytesUtil.bytes2Str(func));
		func = (byte) (func & 0x7F);
		System.out.println(func);
	}

}
