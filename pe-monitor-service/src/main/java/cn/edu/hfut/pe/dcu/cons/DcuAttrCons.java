package cn.edu.hfut.pe.dcu.cons;

import cn.edu.hfut.pe.model.entity.config.MON_DCU;
import io.netty.util.AttributeKey;

public interface DcuAttrCons {
	AttributeKey<MON_DCU> DCU = AttributeKey.valueOf("DCU_ID");// ip
}
