package cn.edu.hfut.pe.model;

import com.serotonin.modbus4j.code.DataType;

/**
 * //功能号:位置:modbus数据类型:[bit类型的位置/长度]
 * 
 * @author 86189
 *
 */
public class ModbusValueExps {
	private String dataTypeStr;
	private int dataType;

	private byte func;

	private int offset;

	private int lenOrP = -1;// 字节长度或bit位置

	public ModbusValueExps() {
	}

	public ModbusValueExps(String dataTypeStr, int dataType, byte func, int offset, int lenOrP) {
		super();
		this.dataTypeStr = dataTypeStr;
		this.dataType = dataType;
		this.func = func;
		this.offset = offset;
		this.lenOrP = lenOrP;
	}

	public String getDataTypeStr() {
		return dataTypeStr;
	}

	public void setDataTypeStr(String dataTypeStr) {
		this.dataTypeStr = dataTypeStr;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	
	public byte getFunc() {
		return func;
	}

	public void setFunc(byte func) {
		this.func = func;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLenOrP() {
		return lenOrP;
	}

	public void setLenOrP(int lenOrP) {
		this.lenOrP = lenOrP;
	}

	public static final ModbusValueExps getInstance(String oid) {
		try {
			String[] oidA = oid.split(":");// 功能号:位置:modbus数据类型:[bit类型的位置/长度]
			byte func= Byte.parseByte(oidA[0]);
			int offset = Integer.parseInt(oidA[1]);
			String dataTypeStr = oidA[2];
			int dataType = DataType.getDataType(dataTypeStr);
			int lenOrP = -1;
			if (oidA.length > 3) {
				lenOrP = Integer.parseInt(oidA[3]);
			}
			ModbusValueExps modbusOid = new ModbusValueExps(dataTypeStr, dataType, func, offset, lenOrP);
			return modbusOid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModbusOid [dataTypeStr=");
		builder.append(dataTypeStr);
		builder.append(", dataType=");
		builder.append(dataType);
		builder.append(", func=");
		builder.append(func);
		builder.append(", offset=");
		builder.append(offset);
		builder.append(", lenOrP=");
		builder.append(lenOrP);
		builder.append("]");
		return builder.toString();
	}

}
