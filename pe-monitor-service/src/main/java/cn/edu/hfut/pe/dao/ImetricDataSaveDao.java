package cn.edu.hfut.pe.dao;

import java.io.IOException;
import java.util.List;


public interface ImetricDataSaveDao {

	void saveMetricList(List<cn.edu.hfut.pe.model.MetricData> list) throws IOException;

	boolean isAccessible();

}
