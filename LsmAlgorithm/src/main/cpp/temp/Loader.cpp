#include "stdafx.h"
#include "Loader.h"

Loader::Loader(void)
{
}

Loader::~Loader(void)
{
}

short* Loader::convertByteToShortArrayForMotion(char* array) {
			
	short* result = new short[9];
	for(int i = 0; i < 9; i++ ) {
		short twoBytes = (array[(i*2)+1] & 0xFF) << 8 | (array[i*2] & 0xFF);
		short val = this->twosComplement(twoBytes, 16);
		result[i] = val;
	}
	return result;
}

/* 將 n byte 的 array 計算成 length 個 short array */
short* Loader::convertByteToShortArrayForECG(char* array, int length) {
			
	short* result = new short[length];
	for(int i = 0; i < length; i++ ) {
		short twoBytes = (array[(i*2)+1] & 0xFF) << 8 | (array[i*2] & 0xFF);
		short val = this->twosComplement(twoBytes, 16);
		result[i] = val;
	}
	return result;
}

short Loader::twosComplement(short value, short bit) {	
	if((value & (1 << (bit-1))) != 0) {
		value = value - (1 << bit);
	}
	return value;
}