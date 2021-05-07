package cn.edu.hfut.pe.service;

import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;
import io.netty.channel.Channel;

//import com.kdgz.monitor.pe2.model.entity.MON_PE2_TEMPLATE_GRAPH;
//import com.kdgz.monitor.pe2.model.entity.MON_PE_MANAGEDOBJECT_TMP;

public interface IpeService {

	/**
	 * 构造消息，发送到dcu
	 * 
	 * @param ch
	 * @param device
	 * @param graph
	 * @return
	 */
	Object dealMoGraph(Channel ch, MON_DEVICE device, MON_METRIC_CONFIG graph);

//	void dealMoGraph(MON_PE_MANAGEDOBJECT_TMP mo, MON_PE2_TEMPLATE_GRAPH graph);
//
//	void dealVertivSnmpAlarm(MON_PE_MANAGEDOBJECT_TMP mo);
//
//	boolean isWritable2C();

}
