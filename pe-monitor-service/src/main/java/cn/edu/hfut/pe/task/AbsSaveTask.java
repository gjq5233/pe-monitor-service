package cn.edu.hfut.pe.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbsSaveTask<E> implements Runnable {

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	protected int size = 2000; //

	protected BlockingQueue<E> queue;

	{
		initQueue();
	}

	@Override
	public void run() {
		LOG.debug("{} is exe", this.getClass().getSimpleName());
		deal();
	}

	protected abstract void initQueue();
	
	private void deal() {
		int count = Math.min(size, queue.size());
		if (count == 0) {
			return;
		}
		if (count < size / 10) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			count = Math.min(size, queue.size());
			if (count == 0) {
				return;
			}
		}
		List<E> list = new ArrayList<>(count);
		queue.drainTo(list, count);
		try {
			if (!list.isEmpty()) {
				LOG.warn("deal list, size:{}", list.size());
				long time0 = System.currentTimeMillis();
				save2(list);
				long time1 = System.currentTimeMillis();
				if (time1 - time0 > 1000) {
					LOG.warn(this.getClass().getSimpleName() + " save size:{}, time-consuming:{}ms", list.size(),
							time1 - time0);
				}
			}
		} catch (Throwable e) {
			LOG.warn(e.getMessage(), e);
		}
	}

	protected void save2(List<E> list) {
		try {
			save(list);
		} catch (Throwable e) {
			LOG.warn("{}", e);
		}

	};

	protected void saveHc(List<E> list) {
	
	}

	protected void save(List<E> list) {
	};

	public void setSize(int size) {
		this.size = size;
	}


}
