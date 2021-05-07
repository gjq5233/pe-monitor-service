package cn.edu.hfut.pe.dcu.comm.msg;

public interface ExcepCode {
	byte OK = 0;
	byte ERROR_FUNC = 1;
	byte ERROR_DATA = 3;
	byte ERROR_CHECK = 5;
	byte ERROR_ID = 0x1A;
	byte ERROR_INTERNAL = 0x1F;
}
