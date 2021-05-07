package cn.edu.hfut.pe.dcu.comm.msg;

public class _43Heartbeat extends Msg {

	public _43Heartbeat() {
		super.func = 0x43;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_43Heartbeat []");
		return builder.toString();
	}

}
