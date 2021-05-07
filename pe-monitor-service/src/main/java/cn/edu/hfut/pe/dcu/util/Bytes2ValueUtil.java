package cn.edu.hfut.pe.dcu.util;

import java.io.UnsupportedEncodingException;

import com.serotonin.modbus4j.code.DataType;

//import base.util.CharsetUtil;

/**
 * 
 * 从modbus返回的字节数组中 读取数据
 * 
 * @author 86189
 *
 */
public class Bytes2ValueUtil {

	/**
	 * 从modbus 返回的字节数组中读取状态值
	 * 
	 * @param data   modbus中返回的字节数组
	 * @param offset 从哪个寄存器开始读, 1个寄存器为两字节
	 * @param bit    寄存器中哪一位的数据
	 * @return
	 */
	public static Boolean toBool(byte[] data, int offset, int bit) {

		// For the rest of the types, we double the normalized offset to account for
		// short to byte.
		offset *= 2;

		// We could still be asking for a binary if it's a bit in a register.
		return new Boolean((((data[offset + 1 - bit / 8] & 0xff) >> (bit % 8)) & 0x1) == 1);
	}

	public static int toBoolInt(byte[] data, int offset, int bit) {
		if (toBool(data, offset, bit)) {
			return 1;
		}
		return 0;
	}

	/**
	 * 从modbus 返回的字节数组中读取字符串
	 * 
	 * @param data    modbus中返回的字节数组
	 * @param offset  从哪个寄存器开始读, 1个寄存器为两字节
	 * @param length  字节长度
	 * @param charset 字符编码 默认为uft8
	 * @return
	 */
	public static String toStr(byte[] data, int offset, int length, String charset) {
		offset *= 2;
		if (charset == null) {
//			charset = CharsetUtil.UTF_8_CHARSET;
		}
		try {
			return new String(data, offset, length, charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String toStr(byte[] data, int offset, int length) {
		return toStr(data, offset, length, null);
	}

	public static Number toNumber(byte[] data, int offset, String dataType) {
		return toNumber(data, offset, DataType.getDataType(dataType));
	}

	/**
	 * 从modbus 返回的字节数组中读取数据
	 * 
	 * @param data     modbus中返回的字节数组
	 * @param offset   从哪个寄存器开始读, 1个寄存器为两字节
	 * @param dataType 读取什么样的数据类型
	 * @return
	 */
	public static Number toNumber(byte[] data, int offset, int dataType) {
		offset *= 2;

		// 1 byte
		if (dataType == DataType.ONE_BYTE_INT_UNSIGNED_LOWER) {
			return new Integer(data[offset + 1] & 0xff);
		}
		if (dataType == DataType.ONE_BYTE_INT_UNSIGNED_UPPER) {
			return new Integer(data[offset] & 0xff);
		}

		// 2 bytes
		if (dataType == DataType.TWO_BYTE_INT_UNSIGNED) {
			return new Integer(((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff));
		}

		if (dataType == DataType.TWO_BYTE_INT_SIGNED) {
			return new Short((short) (((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff)));
		}

		// 1 byte
		if (dataType == DataType.ONE_BYTE_INT_UNSIGNED_LOWER) {
			return new Integer(data[offset + 1] & 0xff);
		}
		if (dataType == DataType.ONE_BYTE_INT_UNSIGNED_UPPER) {
			return new Integer(data[offset] & 0xff);
		}

		// 4 bytes

		if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED)
            return new Long(((long) ((data[offset] & 0xff)) << 24) | ((long) ((data[offset + 1] & 0xff)) << 16)
                    | ((long) ((data[offset + 2] & 0xff)) << 8) | ((data[offset + 3] & 0xff)));
		
		if (dataType == DataType.FOUR_BYTE_INT_CF)
            return new Long(((long) ((data[offset+1] & 0xff)) << 24) | ((long) ((data[offset] & 0xff)) << 16)
                    | ((long) ((data[offset + 3] & 0xff)) << 8) | ((data[offset + 2] & 0xff)));

        if (dataType == DataType.FOUR_BYTE_INT_SIGNED)
            return new Integer(((data[offset] & 0xff) << 24) | ((data[offset + 1] & 0xff) << 16)
                    | ((data[offset + 2] & 0xff) << 8) | (data[offset + 3] & 0xff));

        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED)
            return new Long(((long) ((data[offset + 2] & 0xff)) << 24) | ((long) ((data[offset + 3] & 0xff)) << 16)
                    | ((long) ((data[offset] & 0xff)) << 8) | ((data[offset + 1] & 0xff)));

        if (dataType == DataType.FOUR_BYTE_INT_SIGNED_SWAPPED)
            return new Integer(((data[offset + 2] & 0xff) << 24) | ((data[offset + 3] & 0xff) << 16)
                    | ((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff));
        
        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED)
            return new Long(((long) ((data[offset + 3] & 0xff)) << 24) | (((data[offset + 2] & 0xff) << 16))
                    | ((long) ((data[offset + 1] & 0xff)) << 8) | (data[offset] & 0xff));

        if (dataType == DataType.FOUR_BYTE_INT_SIGNED_SWAPPED_SWAPPED)
            return new Integer(((data[offset + 3] & 0xff) << 24) | ((data[offset + 2] & 0xff) << 16)
                    | ((data[offset + 1] & 0xff) << 8) | ((data[offset] & 0xff)));
        

		if (dataType == DataType.FOUR_BYTE_FLOAT) {
			return Float.intBitsToFloat(((data[offset] & 0xff) << 24) | ((data[offset + 1] & 0xff) << 16)
					| ((data[offset + 2] & 0xff) << 8) | (data[offset + 3] & 0xff));
		}

		if (dataType == DataType.FOUR_BYTE_FLOAT_SWAPPED) {
			return Float.intBitsToFloat(((data[offset + 2] & 0xff) << 24) | ((data[offset + 3] & 0xff) << 16)
					| ((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff));
		}
		throw new RuntimeException("Unsupported data type: " + dataType);
	}

	public static void test(int lenOrP) {
		byte[] resultBytes = new byte[] { 0, 3, 00, 1 };
		int subOffset = 0;
		if (lenOrP > -1) {
				int vbit16Int = Bytes2ValueUtil.toBoolInt(resultBytes, subOffset + lenOrP / 15, lenOrP % 15);
				System.out.println(vbit16Int);
		}
	}

	public static void main(String[] args) {
		
//		System.out.println(DataType.getDataType("INT32"));
		
		byte[] bba = new byte[] { 76, 38, 00, 01 };
		bba = new byte[] { 0x42, (byte)0xa8, (byte)0x18, (byte) 0xe4 };
//		bba = new byte[] { 0x00, (byte)0x00, 0x33, (byte) 0x00 };
		System.out.println(Bytes2ValueUtil.toNumber(bba, 0, DataType.FLOAT32));
//		bba = new byte[] { 0x40, (byte) 0xA0, 0, 0 };
		System.out.println(Bytes2ValueUtil.toNumber(bba, 0, DataType.FLOAT32_SWAPPED));
//		test(17);
//		test(16);
//		test(15);
//		test(2);
//		test(1);
//		test(0);

//		Multimap mm = Multimaps.synchronizedSetMultimap(HashMultimap.create());
//		mm.put(1, 2);
//		mm.put(1, 3);
//		mm.put(2, 2);
//		mm.put(2, 3);
//		System.out.println(mm.containsEntry(1, 2));
//		System.out.println(mm.containsEntry(1, 3));
//		System.out.println(mm.containsEntry3(1, 4));
//		System.out.println(mm);
//		System.out.println("====");
//		mm.remove(1, 4);
//		System.out.println(mm.containsEntry(1, 2));
//		System.out.println(mm.containsEntry(1, 3));
//		System.out.println(mm.containsEntry(1, 4));
//		System.out.println(mm);
//		System.out.println("====");
//		mm.remove(1, 3);
//		System.out.println(mm.containsEntry(1, 2));
//		System.out.println(mm.containsEntry(1, 3));
//		System.out.println(mm.containsEntry(1, 4));
//		System.out.println(mm);
//		System.out.println("====");
//		mm.removeAll(1);
//		System.out.println(mm.containsEntry(1, 2));
//		System.out.println(mm.containsEntry(1, 3));
//		System.out.println(mm.containsEntry(1, 4));
//		System.out.println(mm);
//		System.out.println("====");
	}
}
