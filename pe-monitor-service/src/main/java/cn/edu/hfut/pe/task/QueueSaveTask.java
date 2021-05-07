package cn.edu.hfut.pe.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.pe.service.IdataSaveService;

public class QueueSaveTask<E> implements Runnable {

	private final static Logger LOG = LoggerFactory.getLogger(QueueSaveTask.class);

	private IdataSaveService service;

	private BlockingQueue<E> queue;

	private int size = 1000; //

	@Override
	public void run() {
		deal();
	}

	private int sleepTime = 200;// ms

	protected void deal() {
		Objects.requireNonNull(queue, "queue cannot be null");
		int count = Math.min(size, queue.size());
		if (count == 0) {
			return;
		}
		if (count < size / 10) {
			try {
				Thread.sleep(sleepTime);
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
				service.saveQueueList(list);
				long time1 = System.currentTimeMillis();
				if (time1 - time0 > 1000) {
					LOG.warn("type:{}, saveQueueList save size:{}, time-consuming:{}ms", list.get(0).getClass(),
							list.size(), time1 - time0);
				}
			}
		} catch (Throwable e) {
			LOG.warn(e.getMessage(), e);
		}
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setService(IdataSaveService service) {
		this.service = service;
	}

	public void setQueue(BlockingQueue<E> queue) {
		this.queue = queue;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

}
