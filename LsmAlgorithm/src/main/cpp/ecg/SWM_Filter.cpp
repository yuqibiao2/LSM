#include "SWM_ECG_ALGO.h"
#include "SWM_Filter.h"
//#if defined(USE_PC_SIMULATE)
#include <string.h>
#include <math.h>
//#endif

long 
MY_GetDiffSquare(
	short	i16Value, 
	short	i16DiffValue) 
{
	return (long) ((i16Value - i16DiffValue) * (i16Value - i16DiffValue));
}

long
MY_PerformOFProtect (
    long* 	pi32Dest,
    long* 	pi32Src,
	long 	i32SampleCount)
{
	long	i = 0;
	long long i64Temp = 0;

	
	for(i = 0; i < i32SampleCount; i++)
	{
		if(i == 0)
		{
			pi32Dest[i] = pi32Src[i];
		}
		else
		{
			i64Temp = pi32Src[i] - pi32Dest[i-1];
			
			if( i64Temp >= OVER_FLOW_MAX)//avoid the overflow for 16bits
			{
				pi32Dest[i]= pi32Src[i] - (ABS(i64Temp) / OVER_FLOW_MAX)* SHORT_MAX;
			}
			else if(i64Temp <=  OVER_FLOW_MIN)
			{
				pi32Dest[i]= pi32Src[i] + (ABS(i64Temp)/ OVER_FLOW_MAX)* SHORT_MAX;
			}
			else
			{
				pi32Dest[i] = pi32Src[i];
			}
		}
		
	}
	
	return 0;
}

long 
MY_LocalMean(
	long* pi32Buffer, 
	short i16bufferSize)
{

	short i;
	long long i64Sum = 0;

	for (i = 0; i < i16bufferSize; i++)
	{
		i64Sum += pi32Buffer[i];
	}

	i64Sum /= (long) i16bufferSize;

	return ((long) i64Sum);
}

long
MY_PerformECGMedian(
	long* pi32Dest, 
	long* pi32Src, 
	long i32InputSampleCount,
	long i32OutputSampleCount, 
	long i32MeanFilter)
{
	long i = 0;
	long i32Offset = (i32InputSampleCount - i32OutputSampleCount) >> 1;
	long i32Offset_S = 0;
	long i32Offset_E = 0;
	long i32Sum = 0;
	long i32LocalMean = 0;
	long i32TotalMean = 0;

	if (pi32Dest == 0 || pi32Src == 0) 
	{
		return -1;
	}

	for (i = 0; i < i32OutputSampleCount; i++) 
	{
		i32Sum += pi32Src[i];

		if (i == i32OutputSampleCount - 1) 
		{
			i32TotalMean = i32Sum / i32OutputSampleCount;
		}
	}

	i32Offset = (i32InputSampleCount - i32OutputSampleCount) >> 1;
	i32Offset_S = i32MeanFilter / 2 + i32Offset;
	i32Offset_E = i32OutputSampleCount - i32MeanFilter / 2 - 1 - i32Offset;

	for (i = 0; i < i32OutputSampleCount; i++) 
	{
		if ((i >= i32Offset_S) && (i <= i32Offset_E)) 
		{
			i32LocalMean = MY_LocalMean(&pi32Src[i - i32Offset_S], (short) i32MeanFilter);
			//pi32Dest[i] = i32LocalMean - i32TotalMean;
			pi32Dest[i] = pi32Src[i] - i32LocalMean;
		} 
		else 
		{
			pi32Dest[i] = pi32Src[i]; //(short)i32TotalMean;
		}
	}

	for (i = 0; i < i32Offset_S; i++) 
	{
		pi32Dest[i] = pi32Dest[i32Offset_S];
		pi32Dest[i + i32Offset_E + 1] = pi32Dest[i32Offset_E];
	}

	return 0;
}

long 
MY_PerformECGMean(
	long* pi32Dest, 
	long* pi32Src, 
	long i32InputSampleCount,
	long i32OutputSampleCount, 
	long i32MeanFilter)
{
	long i = 0;
	long i32Offset = (i32InputSampleCount - i32OutputSampleCount) >> 1;
	long i32Offset_S = 0;
	long i32Offset_E = 0;
	long i32Sum = 0;
	long i32LocalMean = 0;
	long i32TotalMean = 0;

	if (pi32Dest == 0 || pi32Src == 0) 
	{
		return -1;
	}

	for (i = 0; i < i32OutputSampleCount; i++) 
	{
		i32Sum += pi32Src[i];

		if (i == i32OutputSampleCount - 1) 
		{
			i32TotalMean = i32Sum / i32OutputSampleCount;
		}
	}

	i32Offset = (i32InputSampleCount - i32OutputSampleCount) >> 1;
	i32Offset_S = i32MeanFilter / 2 + i32Offset;
	i32Offset_E = i32OutputSampleCount - i32MeanFilter / 2 - 1 - i32Offset;

	for (i = 0; i < i32OutputSampleCount; i++) 
	{
		if ((i >= i32Offset_S) && (i <= i32Offset_E)) 
		{
			i32LocalMean = MY_LocalMean(&pi32Src[i - i32Offset_S], (short) i32MeanFilter);
			pi32Dest[i] = i32LocalMean;
		} 
		else 
		{
			pi32Dest[i] = pi32Src[i]; //(short)i32TotalMean;
		}
	}

	for (i = 0; i < i32Offset_S; i++) 
	{
		pi32Dest[i] = pi32Dest[i32Offset_S];
		pi32Dest[i + i32Offset_E + 1] = pi32Dest[i32Offset_E];
	}

	return 0;
}

void
MY_FIRFilterInFixPoint (
    short*  pi16Coeffs,
    long*  pi32Input,
    long*  pi32Output,
    long    i32FilterLength )
{
    long	i;
	long 	i32FilterOffset = i32FilterLength/2;
    long long 	i64Acc = 0;    // accumulator for MACs
    

    // perform the multiply-accumulate
    for ( i = 0; i < i32FilterOffset; i++ )
    {
        i64Acc += ((long long)(pi16Coeffs[i32FilterOffset - i - 1]) * (long)pi32Input[i32FilterOffset - i - 1]) + 
				((long long)(pi16Coeffs[i32FilterLength - i - 1]) * (long)pi32Input[i32FilterLength - i - 1]);
    }
	
	i64Acc += ((long)(pi16Coeffs[i32FilterOffset]) * (long)pi32Input[i32FilterOffset]);

    // saturate the result
    if ( i64Acc > 0x7FFFFFFFFFFFFFFF)
    {
        i64Acc = 0x7FFFFFFFFFFFFFFF;
    }
    else if ( i64Acc < -0x7FFFFFFFFFFFFFFF)
    {
        i64Acc = -0x7FFFFFFFFFFFFFFF;
    }

		i64Acc = i64Acc >> 16;
    // convert from Q30 to Q15
    *pi32Output = (long)i64Acc;
}


long
MY_PerformECGFIR (
	long* 	pi32Dest,
	long* 	pi32Src,
	long 	i32InputSampleCount,
	long 	i32OutputSampleCount,
	long 	i32FirFilter,
	short* 	pi16FirFilterCoeff)
{
	long	i = 0;
	long	i32Offset		= (i32InputSampleCount - i32OutputSampleCount) >> 1;
	long	i32Offset_S		= 0;
	long	i32Offset_E		= 0;

	i32Offset = (i32InputSampleCount - i32OutputSampleCount) >> 1;
	i32Offset_S = i32FirFilter/2 + i32Offset;
	i32Offset_E = i32OutputSampleCount - i32FirFilter/2 - 1 - i32Offset;
	
	for(i = 0; i < i32OutputSampleCount; i++)
	{
		if((i >= i32Offset_S) && (i <= i32Offset_E))
		{
			MY_FIRFilterInFixPoint (pi16FirFilterCoeff, 
									&pi32Src[i - i32Offset_S], 
									&pi32Dest[i], 
									i32FirFilter);
		}
		else
		{
			pi32Dest[i] = pi32Src[i];
		}	
	}

	for(i = 0; i<i32Offset_S; i++)
	{
		pi32Dest[i] = pi32Dest[i32Offset_S];
		pi32Dest[i + i32Offset_E + 1] = pi32Dest[i32Offset_E];
	}
	
	return 0;
}

void 
MY_PerformSpecialFiter(
	long*	pi32Dest, 
	long*	pi32Src, 
	long	i32DataLength,
	long	i32WindowStart) 
{
	long	i = 0;

        for (i = i32WindowStart; i < (i32DataLength + i32WindowStart); i++)
    {
       	if(pi32Src[i] > 1500)
                pi32Src[i] = 0;

        if(pi32Src[i] < -1500)
                pi32Src[i] = 0;

        pi32Dest[i] = pi32Src[i];//max(0, pi16Src[i]);
    }

}

long
MY_Perform1StepDiff(
	long*	pi32Dest,
	long*	pi32Src,
	long	i32DiffPoints,
	long	i32InputSampleCount,
	long	i32OutputSampleCount)
{
	long	i = 0;
	long	i32Offset		= (i32InputSampleCount - i32OutputSampleCount) >> 1;
	long	i32Offset_S		= 0;
	long	i32Offset_E		= 0;

	i32Offset = (i32InputSampleCount - i32OutputSampleCount) >> 1;
	i32Offset_S = i32DiffPoints/2 + i32Offset;
	i32Offset_E = i32OutputSampleCount - i32DiffPoints - 1 - i32Offset;
	
	if(pi32Dest == NULL || pi32Src == NULL)
	{	
        return -1;
    }

	for(i = 0; i < i32OutputSampleCount; i++)
	{
		if((i >= i32Offset_S) && (i < i32Offset_E))
		{
			pi32Dest[i] = pi32Src[i + i32DiffPoints] - pi32Src[i];
		}
		else
		{
			pi32Dest[i] = 0;
		}	
	}

	for(i = 0; i<i32Offset_S; i++)
	{
		pi32Dest[i] = pi32Dest[i32Offset_S];
		pi32Dest[i + i32Offset_E + 1] = pi32Dest[i32Offset_E];
	}

	return 0;
}
