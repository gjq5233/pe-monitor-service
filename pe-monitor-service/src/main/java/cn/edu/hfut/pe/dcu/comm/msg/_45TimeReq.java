package cn.edu.hfut.pe.dcu.comm.msg;

public class _45TimeReq extends Msg {

	public _45TimeReq() {
		super.func = 0x45;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("_43Heartbeat []");
		return builder.toString();
	}

}
