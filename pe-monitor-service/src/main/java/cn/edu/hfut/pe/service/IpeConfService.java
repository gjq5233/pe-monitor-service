package cn.edu.hfut.pe.service;

import java.util.List;

import cn.edu.hfut.pe.model.entity.config.MON_DCU;
import cn.edu.hfut.pe.model.entity.config.MON_DEVICE;
import cn.edu.hfut.pe.model.entity.config.MON_METRIC_CONFIG;

public interface IpeConfService {

	List<MON_DEVICE> getMON_DEVICElist();

	List<MON_DCU> getMON_DCUlist();

	List<MON_METRIC_CONFIG> getMON_METRIC_CONFIGlist();

}
