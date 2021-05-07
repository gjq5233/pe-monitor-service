package cn.edu.hfut.pe.dcu.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.serotonin.modbus4j.code.DataType;

import cn.edu.hfut.pe.dcu.cons.FuncCode;
import cn.edu.hfut.pe.model.ModbusValueExps;
import cn.edu.hfut.pe.model.entity.MON_DATA;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;

/**
 * modbus通讯工具类,采用modbus4j实现
 * 
 * @author lxq
 * @dependencies modbus4j-3.0.3.jar
 * @website https://github.com/infiniteautomation/modbus4j
 */
public class Modbus4jUtils {

	private final static Logger LOG = LoggerFactory.getLogger(Modbus4jUtils.class);

	public static List<MON_DATA> parseGraph(Long moid, MON_METRIC_CONFIG graph, byte[] resultBytes) {
		long time = System.currentTimeMillis();
		List<MON_DATA> result = new ArrayList<>();
		MON_DATA data = parseGraph(moid, 0, graph, resultBytes);
		if(data != null) {
			data.setCOLL_TIME(time);
			result.add(data);
		}
		int offset = graph.getMve().getOffset();
	

		List<MON_METRIC_CONFIG> subGarphList = graph.getSubConfList();
		if (!CollectionUtils.isEmpty(subGarphList)) {
			for (MON_METRIC_CONFIG subGraph : subGarphList) {
				int subOffset = subGraph.getMve().getOffset();
				MON_DATA subPe2Value = parseGraph(moid, subOffset- offset, subGraph, resultBytes);
				if (subPe2Value != null) {
					subPe2Value.setCOLL_TIME(time);
					result.add(subPe2Value);
				}
			}
		}
		return result;
	}

	/**
	 * 根据字遥测,遥信返回的字节数组，解析成指标数据
	 * 
	 * @param moid        设备id
	 * @param offset      指标modbus协议的相对便宜量，功能号3表示偏移的寄存器，02表示偏移的线圈数量
	 * @param graph
	 * @param resultBytes
	 * @return
	 */
	public static MON_DATA parseGraph(Long moid, int offset, MON_METRIC_CONFIG graph, byte[] resultBytes) {
		MON_DATA pe2Value = new MON_DATA();
		pe2Value.setDEVICE_ID(moid);
		pe2Value.setMETRIC_CONFIG_ID(graph.getMETRIC_CONFIG_ID());

		ModbusValueExps mve = graph.getMve();
		int lenOrP = mve.getLenOrP();
		int func = mve.getFunc();
		int bytePosition = 0;// 相对于字节数组resultBytes的位置
		if (FuncCode._03YC == func) {// 03, 寄存器，字节位置等于 寄存器偏移量乘以2
			bytePosition = offset * 2;
		} else if (FuncCode._02YX == func) {// 03, 线圈，字节位置等于 线圈配置除以8
			bytePosition = offset / 8;
		}
		int dataTyep = mve.getDataType();
		switch (dataTyep) {
		case DataType.BYTES:// 批量数据只能是BYTES
			break;
		case DataType.UINT8:
		case DataType.INT16:
		case DataType.UINT16:
		case DataType.INT32:
		case DataType.UINT32:
		case DataType.INT32_CF:
		case DataType.INT32_SWAPPED:
		case DataType.UINT32_SWAPPED:
		case DataType.FLOAT32:
		case DataType.FLOAT32_SWAPPED:
			Number value = Bytes2ValueUtil.toNumber(resultBytes, bytePosition, mve.getDataType());
			value = graph.getMULTIPLIERvalue(value);
			pe2Value.setCOLL_VALUE(value);
			return pe2Value;
		case DataType.BIT:
			if (lenOrP > -1) {// 整形后面跟数字，表示取整形后再取bit，暂不用，有需求再重新实现，mve例子  3:111:BIT:3(表示遥测寄存器111位置后的第3位)
				bytePosition += lenOrP/8;
				byte d = resultBytes[bytePosition];
				int byteP = lenOrP % 8;
				int v = (d >> byteP) & 0x1;
				pe2Value.setCOLL_VALUE(v);
				return pe2Value;
			} else {
				byte d = resultBytes[bytePosition];
				int byteP = offset % 8;
				int v = (d >> byteP) & 0x1;
				pe2Value.setCOLL_VALUE(v);
				return pe2Value;
			}
		default:
			LOG.warn("no deal modbus, moid:{}, graph:{}", moid, graph);
			break;
		}
		return null;
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
//			System.out.println(((byte) 33) >> 6);
//			// 01测试
//			Boolean v011 = readCoilStatus(1, 0);
//			Boolean v012 = readCoilStatus(1, 1);
//			Boolean v013 = readCoilStatus(1, 6);
//			System.out.println("v011:" + v011);
//			System.out.println("v012:" + v012);
//			System.out.println("v013:" + v013);
//			// 02测试
//			Boolean v021 = readInputStatus(1, 0);
//			Boolean v022 = readInputStatus(1, 1);
//			Boolean v023 = readInputStatus(1, 2);
//			System.out.println("v021:" + v021);
//			System.out.println("v022:" + v022);
//			System.out.println("v023:" + v023);

			// 03测试
//			byte[] v0311 = readHoldingRegister1(1, 1, DataType.BYTES);// 注意,float
//						byte[] v0322 = readHoldingRegister1(33, 3, DataType.BYTES);// 同上
//			System.out.println("v031:" + BytesUtil.bytes2Str_(v0311));
//						System.out.println("v032:" + BytesUtil.bytes2Str_(v0322));

			// 03测试
//			Number v031 = readHoldingRegister(33, 1, DataType.TWO_BYTE_INT_UNSIGNED);// 注意,float
//			Number v032 = readHoldingRegister(33, 3, DataType.TWO_BYTE_INT_UNSIGNED);// 同上
//			System.out.println("v031:" + v031);
//			System.out.println("v032:" + v032);

//			// 04测试
//			Number v041 = readInputRegisters(1, 1, DataType.FOUR_BYTE_FLOAT);//
//			Number v042 = readInputRegisters(1, 3, DataType.FOUR_BYTE_FLOAT);//
//			System.out.println("v041:" + v041);
//			System.out.println("v042:" + v042);
//			// 批量读取
//			batchRead();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}