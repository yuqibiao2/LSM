#ifndef SWM_FILTER
#define SWM_FILTER

//Signal Processing API
long MY_GetDiffSquare(short i16Value, short i16DiffValue);
long MY_PerformOFProtect(long* pi32Dest, long* pi32Src, long i32SampleCount);
long MY_LocalMean(long* pi32Buffer, short i16bufferSize);
long MY_PerformECGMedian(long* pi32Dest, long* pi32Src, long i32InputSampleCount,long i32OutputSampleCount, long i32MeanFilter);
long MY_PerformECGMean(long* pi32Dest, long* pi32Src, long i32InputSampleCount, long i32OutputSampleCount, long i32MeanFilter);
void MY_FIRFilterInFixPoint(short* pi16Coeffs, long* pi32Input,long* pi32Output, long i32FilterLength);
long MY_PerformECGFIR(long* pi32Dest, long* pi32Src, long i32InputSampleCount, long i32OutputSampleCount, long i32FirFilter, short* pi16FirFilterCoeff);
void MY_PerformSpecialFiter(long* pi32Dest, long* pi32Src, long i32DataLength, long i32WindowStart);
long MY_Perform1StepDiff(long* pi32Dest, long* pi32Src, long i32DiffPoints, long i32InputSampleCount, long i32OutputSampleCount);

#endif