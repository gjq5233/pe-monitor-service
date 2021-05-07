package cn.edu.hfut.pe.model;

import io.netty.channel.Channel;

public class ChannelReqId {

	private int originalRequestId = -1;

	private Channel ch;
	
	private final long reqTime = System.currentTimeMillis();

	public ChannelReqId(int originalRequestId, Channel ch) {
		this.originalRequestId = originalRequestId;
		this.ch = ch;
	}

	public int getOriginalRequestId() {
		return originalRequestId;
	}

	public void setOriginalRequestId(int originalRequestId) {
		this.originalRequestId = originalRequestId;
	}

	public Channel getCh() {
		return ch;
	}

	public void setCh(Channel ch) {
		this.ch = ch;
	}

	public long getReqTime() {
		return reqTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChannelReqId [originalRequestId=");
		builder.append(originalRequestId);
		builder.append(", ch=");
		builder.append(ch);
		builder.append(", reqTime=");
		builder.append(reqTime);
		builder.append("]");
		return builder.toString();
	}
	
	

}
