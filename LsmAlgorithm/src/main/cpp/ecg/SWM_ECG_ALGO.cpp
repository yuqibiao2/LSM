#include "SWM_ECG_ALGO.h"
#include "SWM_Filter.h"
#include <string.h>
#include <math.h>

#if defined(USE_PC_SIMULATE)
extern void 
BCB_ShowHRResult(
short i16RRIRealCount,
short i16RRITotalCount,
short i16HRSigama,
short i16HROrignial,
short i16HRFinal);


extern void MY_SaveECGFeatrue(
QRS_PARAM*	pQRSArray,
long i32ECGPairCnt);

extern void MY_CalECAverageSNR(
long* pInBuf, 
QRS_PARAM* pQRSArray,
long currentQRSpair);

#endif

//Using C code for develpment on Apps platform as following:
short g_i16OriginalAvgHR = 0;
short g_i16FinalAvgHR = 0;
short g_i16HRV_RMSSD = 0;							///< 2016.05.13 Clark Add	
short g_i16HRV_SDNN = 0;							///< 2016.05.08 Clark Add
long  g_i32CalcultedLength = CAL_TIME_BUF * ECG_SAMPLE_RATE;
long* g_i32ECGInBuffer = NULL;
long* g_i32ECGOutBuffer = NULL;
long* g_i32ECGWorkBuffer = NULL;
QRS_PARAM* g_enQRSPairArray = NULL;
short   g_i16HistoryHRIdx = 0;						///< 2016.04.02 Clark Add
short   g_i16UpdateHRIdx = 0;						///< 2016.05.08 Clark Add
short 	g_i16HistoryHRBuf[KEEP_TIME_BUF] = {0};		///< 2016.04.02 Clark Add
short 	g_i16GroupMark[MAX_RRI_BUF] = {0};			///< 2016.04.02 Clark Add
short 	g_i16UpdateHRBuf[CAL_TIME_BUF] = {0};		///< 2016.05.08 Clark Add
long 	g_i32detaHRArray[MAX_RRI_BUF] = {0};		///< 2016.04.02 Clark Add
long	g_i32finalHRArray[MAX_RRI_BUF] = {0};		///< 2016.04.02 Clark Add
long	g_i32HRArray[MAX_RRI_BUF] = {0};			///< 2016.04.02 Clark Add
long  	g_i32GroupHR[MAX_RRI_BUF] = {0};			///< 2016.04.02 Clark Add
long  	g_i32GroupCnt[MAX_RRI_BUF] = {0};			///< 2016.04.02 Clark Add

short 	g_i16RealTimeMonitorHRVFlag = 0;				///< 2016.05.08 Clark Add
short	g_i16HRVFirstFlag = 0;							///< 2016.05.08 Clark Add
short	g_i16HRVTimer = 0;								///< 2016.05.08 Clark Add
short   g_i16HistoryRRIIdx = 0;							///< 2016.05.08 Clark Add
short   g_i16RecordRRIIdx = 0;							///< 2016.06.22 Clark Add
short 	g_i16HistoryRRIBuf[HRV_RRI_MAX_BUF] = {0};		///< 2016.05.08 Clark Add
short 	g_i16CurrentRRIBuf[MAX_RRI_BUF] = {0};			///< 2016.05.08 Clark Add
short 	g_i16RecordHRVCntBuf[HRV_RRI_LIMIT_BUF] = {0};	///< 2016.06.22 Clark Add
short 	g_i16RecordHRVRRIBuf[HRV_RRI_LIMIT_BUF] = {0};	///< 2016.06.22 Clark Add
short 	g_i16TempHRVRPeakBuf[MAX_RRI_BUF] = {0};		///< 2016.06.22 Clark Add
long 	g_i32RecordHRVTimeBuf[HRV_RRI_LIMIT_BUF] = {0};	///< 2016.06.22 Clark Add
long 	g_i32RecordHRVFrameIdx = 0;						///< 2016.06.22 Clark Add

//LPF 30Hz
static short LPF_FIR_COEFF[31] = 
{
     -536,   -566,   -258,    277,    768,    909,    524,   -302,  -1203,
    -1656,  -1204,    317,   2655,   5187,   7138,   7869,   7138,   5187,
     2655,    317,  -1204,  -1656,  -1203,   -302,    524,    909,    768,
      277,   -258,   -566,   -536
};

void APPS_ECG_InitialForModeChange(int mode)
{
	short i = 0;
	g_i16OriginalAvgHR = 0;
	g_i16FinalAvgHR = 0;
	g_i16RealTimeMonitorHRVFlag = mode;
	///< 2016.05.08 Clark Add
	for(i = 0; i < CAL_TIME_BUF; i++)
	{
		if (i < KEEP_TIME_BUF)
		{
			g_i16HistoryHRBuf[i] = 0;
		}
		g_i16UpdateHRBuf[i] = 0;	
	}

	g_i16HistoryHRIdx = 0;
		
	///< 2016.05.08 Clark Add as following:
	g_i16UpdateHRIdx = 0;
	g_i16HistoryRRIIdx = 0;
	g_i16HRVFirstFlag = 1;
	g_i16HRV_SDNN = 0;
	g_i16HRVTimer = 0;

	///< 2016.05.13 Clark Add as following:
	g_i16HRV_RMSSD = 0;
	
	for(i=0; i<HRV_RRI_MAX_BUF; i++)
	{
		g_i16HistoryRRIBuf[i] = 0;
	}
	
	///< 2016.06.22 Clark Add as following:
	g_i32RecordHRVFrameIdx = 0;
	g_i16RecordRRIIdx = 0;
	
	for(i=0; i<HRV_RRI_LIMIT_BUF; i++)
	{
		g_i16RecordHRVCntBuf[i] = 0;
		g_i32RecordHRVTimeBuf[i] = 0;
		g_i16RecordHRVRRIBuf[i] = 0;
	}

}

void APPS_ECG_Initial(void)
{
	short i = 0;

	memset(g_i32ECGInBuffer, 0, sizeof(g_i32ECGInBuffer));
	memset(g_i32ECGOutBuffer, 0, sizeof(g_i32ECGOutBuffer));
	memset(g_i32ECGWorkBuffer, 0, sizeof(g_i32ECGWorkBuffer));
	
	for(i=0; i<LOCAL_MAX_POINTS_NUM; i++)
	{	
		g_enQRSPairArray[i].u8RRI_ValidFlagIdx = 0;
		g_enQRSPairArray[i].i32Q_LeftPoint_1D_X = 0;	///< 2016.04.02 Clark Add
		g_enQRSPairArray[i].i32S_RightPoint_1D_X = 0;	///< 2016.04.02 Clark Add
		g_enQRSPairArray[i].i32MIN_Point_1D_X = 0;		///< 2016.04.02 Clark Add
		g_enQRSPairArray[i].i32Q_Point_X = 0;
		g_enQRSPairArray[i].i32R_Point_X = 0;
		g_enQRSPairArray[i].i32S_Point_X = 0;
		g_enQRSPairArray[i].i32R_Point_Y = 0;
		g_enQRSPairArray[i].i32Q_Point_Y = 0;
		g_enQRSPairArray[i].i32S_Point_Y = 0;
		g_enQRSPairArray[i].i32R_Point_1D_Y = 0;
		g_enQRSPairArray[i].i32Q_Point_1D_Y = 0;
		g_enQRSPairArray[i].i32S_Point_1D_Y = 0;
		g_enQRSPairArray[i].i32MIN_Point_1D_Y = 0;
		g_enQRSPairArray[i].i32Q_LeftPoint_1D_Y = 0;	///< 2016.05.07 Clark Add
		g_enQRSPairArray[i].i32S_RightPoint_1D_Y = 0;	///< 2016.05.07 Clark Add
		g_enQRSPairArray[i].i32R_QL_deta_X = 0;			///< 2016.04.02 Clark Add
		g_enQRSPairArray[i].i32R_SR_deta_X = 0;			///< 2016.04.02 Clark Add
		g_enQRSPairArray[i].i32QR_deta_X = 0;
		g_enQRSPairArray[i].i32RS_deta_X = 0;
		g_enQRSPairArray[i].i32QR_deta_Y = 0;
		g_enQRSPairArray[i].i32RS_deta_Y = 0;
		g_enQRSPairArray[i].i32R_Anglel = 0;
		g_enQRSPairArray[i].i32RR_Interval = 0;
		g_enQRSPairArray[i].i32RR_Search_Idx = 0;
	}
}

void APPS_ECG_NewBuffer(void)
{
	if(g_i32ECGInBuffer == NULL)
		g_i32ECGInBuffer = new long[g_i32CalcultedLength];

	if(g_i32ECGOutBuffer == NULL)
		g_i32ECGOutBuffer = new long[g_i32CalcultedLength];

	if(g_i32ECGWorkBuffer == NULL)
		g_i32ECGWorkBuffer = new long[g_i32CalcultedLength];

	if(g_enQRSPairArray == NULL)
		g_enQRSPairArray = new QRS_PARAM[LOCAL_MAX_POINTS_NUM];
}

void APPS_ECG_DeleteBuffer(void)
{
	if(g_i32ECGInBuffer)
	{
		delete []g_i32ECGInBuffer;
		g_i32ECGInBuffer = NULL;
	}

	if(g_i32ECGOutBuffer)
	{
		delete []g_i32ECGOutBuffer;
		g_i32ECGOutBuffer = NULL;
	}

	if(g_i32ECGWorkBuffer)
	{
		delete []g_i32ECGWorkBuffer;
		g_i32ECGWorkBuffer = NULL;
	}

	if(g_enQRSPairArray)
	{
		delete []g_enQRSPairArray;
		g_enQRSPairArray = NULL;
	}
}

void APPS_JudgeHRResult(
	short  currentRRICount,
	short  currentAvgHR,
	short* orignalAvgHR,
	short* improveAvgHR)
{
	short i = 0;
	short j = 0;
	short updateFlag = 0;
	long  avgHR = 0;

	*orignalAvgHR = currentAvgHR;

	if (g_i16HistoryHRIdx == 0)
	{
		if (currentAvgHR != 0)
		{
			g_i16HistoryHRBuf[g_i16HistoryHRIdx] = currentAvgHR;
			g_i16HistoryHRIdx++;
			//*improveAvgHR = currentAvgHR;
			///< 2016.05.08 Clark Add
			*improveAvgHR = 0;
		}
		else	
		{
			*improveAvgHR = 0;
		}
		return;
	}
	else
	{	
		if (g_i16HistoryHRIdx < KEEP_TIME_BUF)
		{
			for (i = 0; i < KEEP_TIME_BUF; i++)
			{
				if (ABS(g_i16HistoryHRBuf[i] - currentAvgHR) <= DYNAMIC_HR_RANGE)
				{
					if (i == (g_i16HistoryHRIdx - 1))
					{
						updateFlag = 0;
						g_i16HistoryHRBuf[g_i16HistoryHRIdx] = currentAvgHR;
						g_i16HistoryHRIdx++;
						*improveAvgHR = currentAvgHR;
						///< 2016.05.08 Clark Add
						//*improveAvgHR = 0;
						return;
					}
				}
				else
				{
					g_i16HistoryHRIdx = 0;
					*improveAvgHR = 0;
					return;
				}
			}
		}
		else
		{
			updateFlag = 0;
			
			for (i = 0; i< g_i16HistoryHRIdx; i++)
			{
				avgHR += g_i16HistoryHRBuf[i];
			}
			
			avgHR /= g_i16HistoryHRIdx;

			if (ABS(currentAvgHR - avgHR) > DYNAMIC_HR_RANGE)
			{
				updateFlag = 0;

				///< 2016.05.08 Clark Add as following:
				if (currentAvgHR != 0)
				{
					g_i16UpdateHRBuf[g_i16UpdateHRIdx] = currentAvgHR;
					g_i16UpdateHRIdx = (g_i16UpdateHRIdx + 1) % CAL_TIME_BUF;

					if (g_i16UpdateHRIdx == 0)
					{
						avgHR = 0;
						
						for (i = 0; i< CAL_TIME_BUF; i++)
						{
							avgHR += g_i16UpdateHRBuf[i];
						}
						
						avgHR /= CAL_TIME_BUF;

						for (i = 0; i< CAL_TIME_BUF; i++)
						{
							if (ABS(g_i16UpdateHRBuf[i] - avgHR) > DYNAMIC_HR_RANGE)
							{
								for (j = 0; j < CAL_TIME_BUF; j++)
								{
									g_i16UpdateHRBuf[j] = 0;
								}
								break;
							}

							if (i == (CAL_TIME_BUF - 1))
							{
								g_i16HistoryHRIdx = KEEP_TIME_BUF;
								
								for (j = 0; j < CAL_TIME_BUF; j++)
								{
									if (j < KEEP_TIME_BUF)
									{
										g_i16HistoryHRBuf[j] = g_i16UpdateHRBuf[j + KEEP_TIME_BUF];
									}
									g_i16UpdateHRBuf[j] = 0;
								}
							}
						}
					}	//else -> if (g_i16UpdateHRIdx == 0)
				}	//else -> if (currentAvgHR != 0)
			}
            else
            {
                    updateFlag = 1;
            }	//else -> if (ABS(currentAvgHR - avgHR) > DYNAMIC_HR_RANGE)
		}//else->	if (g_i16HistoryHRIdx < KEEP_TIME_BUF)
	}
	
	if(updateFlag == 1)
	{
		for (i=0; i<(g_i16HistoryHRIdx - 1); i++)
		{
			g_i16HistoryHRBuf[i] =  g_i16HistoryHRBuf[i + 1];
		}
		g_i16HistoryHRBuf[g_i16HistoryHRIdx - 1] = currentAvgHR;
	}
	*improveAvgHR = g_i16HistoryHRBuf[g_i16HistoryHRIdx - 1];
	
}

long
MY_SearchLocalQRSComplex(
	long 	i32StartX, 
	long 	i32EndX,
	long 	i32Torrance,
	long	i32DynamicRSSlopeTH,
	long*	pi32QRSIdx,
	long*	pi32Buffer, 
	long*	pi32WorkBuffer,
	QRS_PARAM* psQRSComplex) 
{
	
	long	i16ValidQRSPair = 1;//0 is invalid
	long	i = 0;
	long	i32Temp = 0;
	long	i32MaxX = 0;
	long	i32MinX = 0;
	long	i32MaxY = 0;
	long	i32MinY = 0X7FFFFFFF;
	long    i32ScanStart = 0;
	long    i32ScanEnd = 0;
	long	i32NextStartPoint;
	
	short	i16SlopeRiseFlag = 0;
	short	i16SlopeFallFlag = 0;
	short	i16SearchQFlag = 0;
	short	i16SearchRFlag = 0;
	short	i16SearchMinFlag = 0;
	short   i16SearchSFlag = 0;
	short	i16SearchFinalFlag = 0;
	long 	i32QCounter = 0;
	long 	i32RCounter = 0;
	long 	i32MinCounter = 0;
	long	i32SCounter = 0;
	
	
	//Clear the 1-D temp buffer
	for(i=0; i<(ECG_SAMPLE_RATE * CAL_TIME_BUF); i++)
	{
		pi32WorkBuffer[i] = 0;
	}
	
	//Perform 1-D process to find the change of slope for local maximum point
	MY_Perform1StepDiff(	pi32WorkBuffer,
							&pi32Buffer[i32StartX],
							1,
							(i32EndX - i32StartX + 1),
							(i32EndX - i32StartX + 1));
	i16ValidQRSPair = 0;
	i16SearchQFlag = 0;
	i16SearchRFlag = 0;
	i16SearchMinFlag = 0;
	i16SearchSFlag = 0;
	i16SearchFinalFlag = 0;
	
	i32Temp = pi32WorkBuffer[0];

	for (i = 1; i <= (i32EndX - i32StartX); i++)
	{
	
		if (pi32WorkBuffer[i] > i32Temp) 
		{
			i16SlopeRiseFlag = 1;
			i16SlopeFallFlag = 0;

			if ((i16SearchQFlag == 0) &&
				(i16SearchRFlag == 0) &&
				(i16SearchMinFlag == 0) &&
				(i16SearchSFlag == 0) &&
				(i16SearchFinalFlag == 0))
			{
				//First Step->
				i16SearchQFlag = 1;
 			}
			else if (i16SearchQFlag == 1)
			// && //(g_i32Temp1StepDiffBuf[i] >= 0))
			{
				//Step 1->
				i32QCounter++;
			}
			
			if (i16SearchRFlag == 1)
			{
				//Step 5-> Search R point is Failure, return search Q step!
				i16SearchQFlag = 0;
				i16SearchRFlag = 0;
				i16SearchMinFlag = 0;
				i16SearchSFlag = 0;
				i16SearchFinalFlag = 0;

				i32QCounter = 0;               
				i32RCounter = 0;
				i32MinCounter = 0;
				i32SCounter = 0;
			}
			else if (i16SearchMinFlag == 1)
			{
				//Step 9->
				//Find the possible of minmun point!

				psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_X = i + i32StartX - 1;
				psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_Y = pi32WorkBuffer[i-1];
				
				if ((psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_Y <= QRS_1D_MIN_POINT_Y) &&
					((psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y - psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_Y) <= QRS_1D_QRS_DELTA_Y_MAX))// &&
					//(ABS(psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_Y) >= psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y))
					//2016.08.02 Clark to modify the condition for error minimum point.
				{
					//Step 11->
					//Search Min point is Successful, go to next step!
					i16SearchQFlag = 0;
					i16SearchRFlag = 0;
					i16SearchMinFlag = 0;
					i16SearchSFlag = 1;
					i16SearchFinalFlag = 0;
				}
				else
				{
					//Step 10->
					//Search Min point is Failure, return search Q step!
					psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_Y = 0;
					psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X = 0;
					
					i16SearchQFlag = 0;
					i16SearchRFlag = 0;
					i16SearchMinFlag = 0;
					i16SearchSFlag = 0;
					i16SearchFinalFlag = 0;
					
                    i32QCounter = 0;
					i32RCounter = 0;
					i32MinCounter = 0;
					i32SCounter = 0;
				}
			}
			else if (i16SearchSFlag == 1)
			{
				i32SCounter++;

				if (pi32WorkBuffer[i] >= psQRSComplex[*pi32QRSIdx].i32R_Point_1D_Y)//(pi32WorkBuffer[i] >= 0)
				{
					//Step 12->
					//Find the possible of S point!
					psQRSComplex[*pi32QRSIdx].i32S_Point_X = i + i32StartX;
					psQRSComplex[*pi32QRSIdx].i32S_Point_Y = pi32Buffer[i + i32StartX];
					psQRSComplex[*pi32QRSIdx].i32RS_deta_X = psQRSComplex[*pi32QRSIdx].i32S_Point_X - psQRSComplex[*pi32QRSIdx].i32R_Point_X;
					psQRSComplex[*pi32QRSIdx].i32RS_deta_Y = psQRSComplex[*pi32QRSIdx].i32R_Point_Y - psQRSComplex[*pi32QRSIdx].i32S_Point_Y;

					if ((psQRSComplex[*pi32QRSIdx].i32RS_deta_X >= QRS_1D_RS_DELTA_X_MIN) &&
						(psQRSComplex[*pi32QRSIdx].i32RS_deta_X <= QRS_1D_RS_DELTA_X_MAX))
					{
						//Step 14->
						//Search the S point is Successful, go to next step!
						i16SearchQFlag = 0;
						i16SearchRFlag = 0;
						i16SearchMinFlag = 0;
						i16SearchSFlag = 0;
						i16SearchFinalFlag = 1;
					}
					else
					{
						//Step 13->
						//Search S point is Failure, return search Q step!
						psQRSComplex[*pi32QRSIdx].i32S_Point_X = 0;
						psQRSComplex[*pi32QRSIdx].i32S_Point_Y = 0;
						psQRSComplex[*pi32QRSIdx].i32RS_deta_X = 0;
						psQRSComplex[*pi32QRSIdx].i32RS_deta_Y = 0;
					
						i16SearchQFlag = 0;
						i16SearchRFlag = 0;
						i16SearchMinFlag = 0;
						i16SearchSFlag = 0;
						i16SearchFinalFlag = 0;
						
	                    i32QCounter = 0;
						i32RCounter = 0;
						i32MinCounter = 0;
						i32SCounter = 0;
					}
				}
			}
		}
		else
		{
			
			i16SlopeRiseFlag = 0;
			i16SlopeFallFlag = 1;

			if ((i16SearchQFlag == 0) &&
				(i16SearchRFlag == 0) &&
				(i16SearchMinFlag == 0) &&
				(i16SearchSFlag == 0) &&
				(i16SearchFinalFlag == 0))
			{
				//First Step->
				//i16SearchQFlag = 1;
 			}
			else if ((i16SearchQFlag == 1) && (1))
				     //(i32QCounter >= QRS_1D_QQ_DELTA_X_MIN) && (i32QCounter <= QRS_1D_QQ_DELTA_X_MAX))
			{
                //Step 2->
				//Search Q point is Successful, go to next step!
				psQRSComplex[*pi32QRSIdx].i32Q_Point_X = i + i32StartX - 1;
				psQRSComplex[*pi32QRSIdx].i32Q_Point_Y = pi32Buffer[i + i32StartX - 1];
				psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y = pi32WorkBuffer[i - 1];
				psQRSComplex[*pi32QRSIdx].i32Q_LeftPoint_1D_X = psQRSComplex[*pi32QRSIdx].i32Q_Point_X - i32QCounter;
				
				if (/*psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y > 0) &&*/
					(psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y >= QRS_1D_MAX_POINT_Y))
				{

				
					i16SearchQFlag = 0;
					i16SearchRFlag = 1;
					i16SearchMinFlag = 0;
					i16SearchSFlag = 0;
					i16SearchFinalFlag = 0;
	
				}
				else
				{
					psQRSComplex[*pi32QRSIdx].i32Q_Point_X = 0;
					psQRSComplex[*pi32QRSIdx].i32Q_Point_Y = 0;
					psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y = 0;
					psQRSComplex[*pi32QRSIdx].i32Q_LeftPoint_1D_X = 0;
					
					i16SearchQFlag = 1;
					i16SearchRFlag = 0;
					i16SearchMinFlag = 0;
					i16SearchSFlag = 0;
					i16SearchFinalFlag = 0;
					
					i32QCounter = 0;
					i32RCounter = 0;
					i32MinCounter = 0;
					i32SCounter = 0;
					
				}				
			}
			else if ((i16SearchRFlag == 1) &&
					(pi32WorkBuffer[i] <= 0))
			{
				//Step 4->
				//Find the possible of R point!
				psQRSComplex[*pi32QRSIdx].i32R_Point_X = i + i32StartX;
				psQRSComplex[*pi32QRSIdx].i32R_Point_Y = pi32Buffer[i + i32StartX];
				psQRSComplex[*pi32QRSIdx].i32QR_deta_X = psQRSComplex[*pi32QRSIdx].i32R_Point_X - psQRSComplex[*pi32QRSIdx].i32Q_Point_X;
				psQRSComplex[*pi32QRSIdx].i32QR_deta_Y = psQRSComplex[*pi32QRSIdx].i32R_Point_Y - psQRSComplex[*pi32QRSIdx].i32Q_Point_Y;
				psQRSComplex[*pi32QRSIdx].i32R_Point_1D_Y = pi32WorkBuffer[i];
				psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X = psQRSComplex[*pi32QRSIdx].i32R_Point_X - psQRSComplex[*pi32QRSIdx].i32Q_LeftPoint_1D_X;

				if ((psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X <= QRS_1D_RQL_DELTA_X_MAX) &&
					(psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X >= QRS_1D_RQL_DELTA_X_MIN) &&
					(psQRSComplex[*pi32QRSIdx].i32QR_deta_X >= QRS_1D_QR_DELTA_X_MIN) && 
					(psQRSComplex[*pi32QRSIdx].i32QR_deta_X <= QRS_1D_QR_DELTA_X_MAX))// &&
					//(psQRSComplex[*pi32QRSIdx].i32R_Point_Y >= 0))
				{
					//Step 6-> 
					//Search R point is Successful, go to next step!
					i16SearchQFlag = 0;
					i16SearchRFlag = 0;
					i16SearchMinFlag = 1;
					i16SearchSFlag = 0;
					i16SearchFinalFlag = 0;
				}
				else
				{	
					//Step 7-> 
					//Search R point is Failure, return to search Q step!

					psQRSComplex[*pi32QRSIdx].i32R_Point_X = 0;
					psQRSComplex[*pi32QRSIdx].i32R_Point_Y = 0;
					psQRSComplex[*pi32QRSIdx].i32QR_deta_X = 0;
					psQRSComplex[*pi32QRSIdx].i32QR_deta_Y = 0;
					psQRSComplex[*pi32QRSIdx].i32R_Point_1D_Y = 0;
					psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X  = 0;
				
					i16SearchQFlag = 1;
					i16SearchRFlag = 0;
					i16SearchMinFlag = 0;
					i16SearchSFlag = 0;
					i16SearchFinalFlag = 0;
					
                    i32QCounter = 0;
					i32RCounter = 0;
					i32MinCounter = 0;
					i32SCounter = 0;
				}
			}
			else if (i16SearchMinFlag == 1)
			{
				//Step 8->
				//Find the continuous falling points for the minmun trend!
				i32MinCounter++;
			}	
			else if (i16SearchRFlag == 1)
			{
				//Step 3->
				//Find the continuous falling points for R wave!
				i32RCounter++;
			}
			else if (i16SearchFinalFlag == 1)
			{

				//Step 15->
				//Find the possible of S-Right point!
				psQRSComplex[*pi32QRSIdx].i32S_RightPoint_1D_X = i + i32StartX;
				psQRSComplex[*pi32QRSIdx].i32S_RightPoint_1D_Y = pi32WorkBuffer[i];
				psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X = psQRSComplex[*pi32QRSIdx].i32S_RightPoint_1D_X - psQRSComplex[*pi32QRSIdx].i32R_Point_X;

				if ((psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X >= QRS_1D_RSR_DELTA_X_MIN) &&
					(psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X <= QRS_1D_RSR_DELTA_X_MAX))// &&
					//(psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y >= pi32WorkBuffer[i - 1]) &&
					//(pi32WorkBuffer[i - 1] >= 0))
				{
					//Step Finish-> 
					//Search the QRS Complex point is Successful, and to break the loop!
					i16ValidQRSPair = 1;
					break;
				}
				else
				{
					//Step 16->
					//Search Final point is Failure, return search Q step!
					psQRSComplex[*pi32QRSIdx].i32S_RightPoint_1D_X = 0;
					psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X = 0;
					
					i16SearchQFlag = 1;
					i16SearchRFlag = 0;
					i16SearchMinFlag = 0;
					i16SearchSFlag = 0;
					i16SearchFinalFlag = 0;
					
                    i32QCounter = 0;
					i32RCounter = 0;
					i32MinCounter = 0;
					i32SCounter = 0;
				}

			}
			else
			{
				//Step O.W.-> 
				//Return Q wave step because that all condition is invalid!
					//if (i16SearchQFlag == 1)
						//i16SearchQFlag = 0;
					
					if (i16SearchSFlag == 1)
						i16SearchSFlag = 0;
					
				    i32QCounter = 0;
					i32RCounter = 0;
					i32MinCounter = 0;
					i32SCounter = 0;
			}

		}
		i32Temp = pi32WorkBuffer[i];
	}
        
	if (i16ValidQRSPair)
	{
		//psQRSComplex[*pi16QRSIdx].i32R_Anglel = (psQRSComplex[*pi16QRSIdx].i32RS_deta_Y * 10) / psQRSComplex[*pi16QRSIdx].i32RS_deta_X;
		if (psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y > psQRSComplex[*pi32QRSIdx].i32S_RightPoint_1D_Y)
			psQRSComplex[*pi32QRSIdx].i32R_Anglel = (psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y - psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_Y);
		else
			psQRSComplex[*pi32QRSIdx].i32R_Anglel = (psQRSComplex[*pi32QRSIdx].i32S_RightPoint_1D_Y - psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_Y);

		if (psQRSComplex[*pi32QRSIdx].i32R_Anglel >= i32DynamicRSSlopeTH)
		{
			i16ValidQRSPair = 1;
			
			if (*pi32QRSIdx != 0)
			{
				if ((psQRSComplex[*pi32QRSIdx].i32R_Point_X - psQRSComplex[*pi32QRSIdx - 1].i32R_Point_X) <= 65)
				{
					//for HR Max limitation protection
					if (ABS(psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_Y) > ABS(psQRSComplex[*pi32QRSIdx - 1].i32MIN_Point_1D_Y))
					{
						psQRSComplex[*pi32QRSIdx - 1].u8RRI_ValidFlagIdx = psQRSComplex[*pi32QRSIdx].u8RRI_ValidFlagIdx;
						psQRSComplex[*pi32QRSIdx - 1].i32Q_LeftPoint_1D_X = psQRSComplex[*pi32QRSIdx].i32Q_LeftPoint_1D_X;
						psQRSComplex[*pi32QRSIdx - 1].i32MIN_Point_1D_X = psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_X;
						psQRSComplex[*pi32QRSIdx - 1].i32Q_Point_X = psQRSComplex[*pi32QRSIdx].i32Q_Point_X;
						psQRSComplex[*pi32QRSIdx - 1].i32R_Point_X = psQRSComplex[*pi32QRSIdx].i32R_Point_X;
						psQRSComplex[*pi32QRSIdx - 1].i32S_Point_X = psQRSComplex[*pi32QRSIdx].i32S_Point_X;
						psQRSComplex[*pi32QRSIdx - 1].i32Q_Point_Y = psQRSComplex[*pi32QRSIdx].i32Q_Point_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32R_Point_Y = psQRSComplex[*pi32QRSIdx].i32R_Point_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32S_Point_Y = psQRSComplex[*pi32QRSIdx].i32S_Point_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32R_Point_1D_Y = psQRSComplex[*pi32QRSIdx].i32R_Point_1D_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32Q_Point_1D_Y = psQRSComplex[*pi32QRSIdx].i32Q_Point_1D_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32S_Point_1D_Y = psQRSComplex[*pi32QRSIdx].i32S_Point_1D_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32MIN_Point_1D_Y = psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32Q_LeftPoint_1D_Y = psQRSComplex[*pi32QRSIdx].i32Q_LeftPoint_1D_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32S_RightPoint_1D_Y = psQRSComplex[*pi32QRSIdx].i32S_RightPoint_1D_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32R_QL_deta_X = psQRSComplex[*pi32QRSIdx].i32R_QL_deta_X;
						psQRSComplex[*pi32QRSIdx - 1].i32R_SR_deta_X = psQRSComplex[*pi32QRSIdx].i32R_SR_deta_X;
						psQRSComplex[*pi32QRSIdx - 1].i32QR_deta_X = psQRSComplex[*pi32QRSIdx].i32QR_deta_X;
						psQRSComplex[*pi32QRSIdx - 1].i32RS_deta_X = psQRSComplex[*pi32QRSIdx].i32RS_deta_X;
						psQRSComplex[*pi32QRSIdx - 1].i32QR_deta_Y = psQRSComplex[*pi32QRSIdx].i32QR_deta_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32RS_deta_Y = psQRSComplex[*pi32QRSIdx].i32RS_deta_Y;
						psQRSComplex[*pi32QRSIdx - 1].i32R_Anglel = psQRSComplex[*pi32QRSIdx].i32R_Anglel;
						psQRSComplex[*pi32QRSIdx - 1].i32RR_Interval = psQRSComplex[*pi32QRSIdx].i32RR_Interval;
						psQRSComplex[*pi32QRSIdx - 1].i32RR_Search_Idx = psQRSComplex[*pi32QRSIdx].i32RR_Search_Idx;
						*pi32QRSIdx = *pi32QRSIdx - 1;
					}
					else
					{
						i16ValidQRSPair = 0;
					}
				}
			}
		}
		else
		{
			i16ValidQRSPair = 0;
		}
	}

	if (i16ValidQRSPair == 0)
	{
		//Result QRS Complex is Failure!
		//Initial the QRS pair to zero
		psQRSComplex[*pi32QRSIdx].i32Q_Point_X = 0;
		psQRSComplex[*pi32QRSIdx].i32R_Point_X = 0;
		psQRSComplex[*pi32QRSIdx].i32S_Point_X = 0;
		psQRSComplex[*pi32QRSIdx].i32R_Point_Y = 0;
		psQRSComplex[*pi32QRSIdx].i32Q_Point_Y = 0;
		psQRSComplex[*pi32QRSIdx].i32S_Point_Y = 0;
		psQRSComplex[*pi32QRSIdx].i32QR_deta_X = 0;
		psQRSComplex[*pi32QRSIdx].i32RS_deta_X = 0;
		psQRSComplex[*pi32QRSIdx].i32QR_deta_Y = 0;
		psQRSComplex[*pi32QRSIdx].i32RS_deta_Y = 0;
		psQRSComplex[*pi32QRSIdx].i32R_Anglel = 0;
        psQRSComplex[*pi32QRSIdx].i32Q_LeftPoint_1D_Y = 0;	//2016.08.02 Clark Add
        psQRSComplex[*pi32QRSIdx].i32MIN_Point_1D_X = 0; 	//2016.08.02 Clark Add
        psQRSComplex[*pi32QRSIdx].i32Q_LeftPoint_1D_X = 0; 	//2016.08.02 Clark Add

		/* 2016.08.02 Clark Mark
		if (*pi32QRSIdx != 0)
		{
			psQRSComplex[*pi32QRSIdx - 1].i32RR_Interval = 0;
			psQRSComplex[*pi32QRSIdx - 1].u8RRI_ValidFlagIdx = 0;
		}
		*/
		//Initial the next start point
		i32NextStartPoint = i32StartX + 5;//((i16EndX - i16StartX) / 4);
		//i32NextStartPoint = psQRSComplex[*pi16QRSIdx].i32S_Point_X;

	}
	else
	{

        //psQRSComplex[*pi16QRSIdx].u8RRI_ValidFlagIdx = 1;

		//Initial the next start point
		i32NextStartPoint = psQRSComplex[*pi32QRSIdx].i32R_Point_X;

		//Result QRS Complex is Success!
		if (*pi32QRSIdx != 0)
		{
			psQRSComplex[*pi32QRSIdx - 1].i32RR_Interval = psQRSComplex[*pi32QRSIdx].i32R_Point_X - psQRSComplex[*pi32QRSIdx - 1].i32R_Point_X;
			psQRSComplex[*pi32QRSIdx - 1].u8RRI_ValidFlagIdx = 1;
			psQRSComplex[*pi32QRSIdx].u8RRI_ValidFlagIdx = 1;
		}

		*pi32QRSIdx = *pi32QRSIdx + 1;
	}

	return i32NextStartPoint;
}

void 
MY_RemoveFalseRRI(
	short	i16TotalCount, 
	short*	pi16Mean, 
	short*	pi16Count,
	QRS_PARAM* psQRSComplex)
{
	short	i = 0;
	short	j = 0;
	short	k = 0;
	short	tmpRRI = 0;				///< 2016.05.08 Clark Add
	short   tmpCnt = 0;
	short   finalCnt = 0;
	short   tmpRRICnt = 0;
	short	i16CaseICnt = 0;
	short	i16CaseIICnt = 0;
	short 	i16CheckUpdateHRV = 0;	///< 2016.05.08 Clark Add
	long 	i32BaseHR = 0;
	long	i32LocalMean = 0;
	long	i32GlobalMean = 0;
	long    i32TimeIdx = 0;
	long long i64temp_1 = 0;			///< 2016.05.08 Clark Add
	long long i64temp_2 = 0;			///< 2016.05.08 Clark Add

	if (i16TotalCount > MAX_RRI_BUF)
		i16TotalCount = MAX_RRI_BUF;

	if (i16TotalCount == 0)
		return;

	
	for (i = 0; i < i16TotalCount; i++)
	{
		g_i32detaHRArray[i] = 0;
		g_i32finalHRArray[i] = 0;
		g_i32HRArray[i] = 0;
		g_i16CurrentRRIBuf[i] = 0;

		g_i32GroupCnt[i] = 0;
		g_i32GroupHR[i] = 0;
		
		///<2016.06.22 Clark Add for HRV recording
		g_i16TempHRVRPeakBuf[i] = 0;
		
		if (psQRSComplex[i].u8RRI_ValidFlagIdx == 1) 
		{
			g_i32HRArray[i] = psQRSComplex[i].i32R_Point_X;
			tmpCnt++;
		}
	}

	for (i = 0; i < tmpCnt - 1; i++)
	{
		if (g_i32HRArray[i] != 0)
		{
			g_i32detaHRArray[i] =  (g_i32HRArray[i + 1] - g_i32HRArray[i]) << 2;
			g_i32detaHRArray[i] = ((60000 * 10 / g_i32detaHRArray[i]) + 5 )/10;


			///< 2016.05.08 Clark Add for HRV monitor
			if ((g_i32HRArray[i] >= (ECG_SHIFT_SAMPLE * 3)) &&
				(g_i32HRArray[i] < (ECG_SHIFT_SAMPLE * 6)))
			{
				if(	(g_i32detaHRArray[i] <= MAX_HR_LIMITE) &&
					(g_i32detaHRArray[i] >= MIN_HR_LIMITE))
				{
					g_i16CurrentRRIBuf[tmpRRICnt] = (g_i32HRArray[i + 1] - g_i32HRArray[i]) << 2;
					///<2016.06.22 Clark Add for HRV recording
					g_i16TempHRVRPeakBuf[tmpRRICnt] = g_i32HRArray[i];
					tmpRRICnt++;
				}
			}

		}
		
	}

    finalCnt = 0;

	for(i = 0; i< tmpCnt - 1; i++)
	{
		if(	(g_i32detaHRArray[i] > MAX_HR_LIMITE) ||
			(g_i32detaHRArray[i] < MIN_HR_LIMITE))
		{
			continue;
		}
		else
		{
			g_i32finalHRArray[finalCnt] = g_i32detaHRArray[i];
			finalCnt++;	
		}
	}
	
	for (i=0; i<finalCnt; i++)
	{
		g_i16GroupMark[i] = 0;
		g_i32GroupHR[i] = 0;
		g_i32GroupCnt[i] = 0;
	}

	k = 0;
    tmpCnt = 0;
	for (i=0; i<finalCnt; i++)
	{
		if ((g_i16GroupMark[i] == 0) && (g_i32finalHRArray[i] != 0))
		{
			i32BaseHR = g_i32finalHRArray[i];
			g_i32GroupHR[k] += i32BaseHR;
			tmpCnt++;
			
			for (j = i + 1; j<finalCnt; j++)
			{
				if((ABS(g_i32finalHRArray[j] - i32BaseHR) <= LOCAL_HR_RANGE) && 
					(g_i32finalHRArray[j] != 0))
				{
					g_i16GroupMark[j] = 1;
					g_i32GroupHR[k] += g_i32finalHRArray[j];
					tmpCnt++;
				}
			}
			g_i32GroupCnt[k] = tmpCnt;
			tmpCnt = 0;
			k++;
		}
	}

	//Bubble sorting from big to small
	for (i=0; i<k; i++)
	{
		for (j=i; j<k; j++)
		{
			if(g_i32GroupCnt[j] > g_i32GroupCnt[i])
			{
				tmpCnt = g_i32GroupCnt[j];
				i32BaseHR = g_i32GroupHR[j];
				g_i32GroupCnt[j] = g_i32GroupCnt[i];
				g_i32GroupHR[j] = g_i32GroupHR[i];
				g_i32GroupCnt[i] = tmpCnt;
				g_i32GroupHR[i] = i32BaseHR;
			}
		}
	}

	//Check false HR to group
	i32GlobalMean = g_i32GroupHR[0] / SAFE_DIVIDE32(g_i32GroupCnt[0]);
	i16CaseICnt = g_i32GroupHR[0];
	i16CaseIICnt = g_i32GroupHR[0];
	
	for (i=1; i<k; i++)
	{
		i32LocalMean = g_i32GroupHR[i] / SAFE_DIVIDE32(g_i32GroupCnt[i]);
		
		if (ABS(i32GlobalMean - 2 * i32LocalMean) <= LOCAL_HR_RANGE)
		{
			i16CaseICnt += g_i32GroupCnt[i];
		}

		if ((ABS(2 * i32GlobalMean - i32LocalMean) <= LOCAL_HR_RANGE) &&
                    (2 * g_i32GroupCnt[i] >= g_i32GroupCnt[0]))
		{
			i16CaseIICnt += g_i32GroupCnt[i];
		}
	}
	
	if ( i16CaseICnt >= i16CaseIICnt)
	{
		for (i=1; i<k; i++)
		{
			i32LocalMean = g_i32GroupHR[i] / SAFE_DIVIDE32(g_i32GroupCnt[i]);
			
			if (ABS(i32GlobalMean - 2 * i32LocalMean) <= LOCAL_HR_RANGE)
			{
				g_i32GroupHR[0] += 2 * g_i32GroupHR[i];
				g_i32GroupCnt[0] += g_i32GroupCnt[i];
			}

			if (ABS(i32GlobalMean - i32LocalMean) <= LOCAL_HR_RANGE)
			{
				g_i32GroupHR[0] += g_i32GroupHR[i];
				g_i32GroupCnt[0] += g_i32GroupCnt[i];
			}
		}
	}
	else
	{

		g_i32GroupHR[0] *= 2;
		i32GlobalMean = g_i32GroupHR[0];
		
		for (i=1; i<k; i++)
		{
			i32LocalMean = g_i32GroupHR[i] / SAFE_DIVIDE32(g_i32GroupCnt[i]);
			
			if (ABS(i32GlobalMean - i32LocalMean) <= LOCAL_HR_RANGE)
			{
				g_i32GroupHR[0] += g_i32GroupHR[i];
				g_i32GroupCnt[0] += g_i32GroupCnt[i];
			}
		}
		
	}

	for (i=0; i<k; i++)
	{
		//g_i32GroupHR[i] /= SAFE_DIVIDE32(g_i32GroupCnt[i]);
                g_i32GroupHR[i] =  ((g_i32GroupHR[i] * 10) / SAFE_DIVIDE32(g_i32GroupCnt[i]) + 5) / 10;
	}

	if (g_i32GroupCnt[0] * 2 >= finalCnt)
	{
		*pi16Mean = (short) g_i32GroupHR[0];
	}
	else
	{
		*pi16Mean = 0;
	}
	*pi16Count = finalCnt;
	
	///< 2016.05.08 Clark Add for HRV monitor
	if(g_i16RealTimeMonitorHRVFlag)
	{
		finalCnt = 0;
		i32GlobalMean = 0;

		if (g_i16HistoryHRIdx == KEEP_TIME_BUF)
		{	
			for (i=0; i<tmpRRICnt; i++)
			{
				if (ABS((60000 /(SAFE_DIVIDE16(g_i16CurrentRRIBuf[i])) - (g_i16HistoryHRBuf[g_i16HistoryHRIdx - 1]))) <= 10)
				{
					g_i16HistoryRRIBuf[g_i16HistoryRRIIdx] = g_i16CurrentRRIBuf[i];
					
					///<2016.06.22 Clark Add for HRV recording
					i32TimeIdx = (g_i32RecordHRVFrameIdx * ECG_SHIFT_SAMPLE + g_i16TempHRVRPeakBuf[i]) * 4;
					if (g_i16RecordRRIIdx)
					{
						for (j=0; j < g_i16RecordRRIIdx; j++)
						{
							if (g_i32RecordHRVTimeBuf[j] == i32TimeIdx)
							{
								break;
							}
							else
							{
								if (j == (g_i16RecordRRIIdx - 1))
								{
									g_i32RecordHRVTimeBuf[g_i16RecordRRIIdx] = i32TimeIdx;
									g_i16RecordHRVRRIBuf[g_i16RecordRRIIdx] = g_i16HistoryRRIBuf[g_i16HistoryRRIIdx];
									g_i16RecordRRIIdx++;
									break;
								}
							}
						}
					}
					else
					{
						g_i32RecordHRVTimeBuf[g_i16RecordRRIIdx] = i32TimeIdx;
						g_i16RecordHRVRRIBuf[g_i16RecordRRIIdx] = g_i16HistoryRRIBuf[g_i16HistoryRRIIdx];
						g_i16RecordRRIIdx++;
					}
					
					
					g_i16HistoryRRIIdx = (g_i16HistoryRRIIdx + 1) % HRV_RRI_MAX_BUF;
					i16CheckUpdateHRV = 1;
				}
			}
		}

		if (i16CheckUpdateHRV)
		{
			
			if (g_i16HRVTimer < HRV_CAL_TIME_BUF)
			{
				g_i16HRVTimer++;
			}
			
			if ((g_i16HRVTimer >= HRV_FIRST_TIME_BUF) &&
				(g_i16HRVTimer < HRV_CAL_TIME_BUF))
			{
				g_i16HRVFirstFlag = 0;
				
				for (i=0; i<g_i16HistoryRRIIdx; i++)
				{
					if (g_i16HistoryRRIBuf[i] != 0)
					{
						//Calculated SDNN
						i32GlobalMean += g_i16HistoryRRIBuf[i];
						finalCnt++;
					}
				}
			}
			else if (g_i16HRVTimer == HRV_CAL_TIME_BUF)
			{	
				for (i=0; i<HRV_RRI_MAX_BUF; i++)
				{
					if (g_i16HistoryRRIBuf[i]  != 0)
					{
						//Calculated SDNN
						i32GlobalMean += g_i16HistoryRRIBuf[i];
						finalCnt++;
					}
				}
			}
			else
			{
				//Keep HRV
				return;
			}

			//Calculated SDNN
			i32GlobalMean /= SAFE_DIVIDE16(finalCnt);

			for (i=0; i<finalCnt; i++)
			{

				//SDNN
				if (g_i16HistoryRRIBuf[i] != 0)
				{
					i64temp_1 += (g_i16HistoryRRIBuf[i] - i32GlobalMean) * (g_i16HistoryRRIBuf[i] - i32GlobalMean);
				}

				if (i < finalCnt-1)
				{
					//rMSSD
					if ((g_i16HistoryRRIBuf[i] != 0) && (g_i16HistoryRRIBuf[i + 1] != 0))
					{
						i64temp_2 += (g_i16HistoryRRIBuf[i + 1] - g_i16HistoryRRIBuf[i]) * (g_i16HistoryRRIBuf[i + 1] - g_i16HistoryRRIBuf[i]);
					}
				}
			}		
			i64temp_1 /= SAFE_DIVIDE16(finalCnt);
			i64temp_2 /= SAFE_DIVIDE16(finalCnt-1);
			g_i16HRV_SDNN = (short)(sqrt(i64temp_1) * 10 + 5) / 10;
			g_i16HRV_RMSSD = (short)(sqrt(i64temp_2) * 10 + 5) / 10;
		}
	}
}

void 
MY_RemoveFalseQRSComplex(
	short	i16TotalCount, 
	short	i16SigmaThreshold, 
	short	i16CountThreshold,
	short*	pi16Mean, 
	short*	pi16Sigma, 
	short*	pi16Count,
	QRS_PARAM* psQRSComplex)
{
	short	i = 0;
	short	i16MaxDiff = 0;
	short	i16SkipIndex = -1;
	short	i16SkipCount = 0;
	short	i16Count = 0;
	long	i32Mean = 0;
	long	i32Sigma = 0;

	do {
			i32Mean = 0;
			i16Count = 0;
			i16MaxDiff = 0;

			
			/**********************************************
			 ** Recalculate Mean and Count
			 **********************************************/
			for (i = 0; i < i16TotalCount; i++) 
			{
				//First Check the RRI wether is valid or not
				if (psQRSComplex[i].u8RRI_ValidFlagIdx == 1) 
				{
				 	if(psQRSComplex[i].i32RR_Interval > MAX_RRI_LIMITE)
						psQRSComplex[i].u8RRI_ValidFlagIdx = 0;

					if(psQRSComplex[i].i32RR_Interval < MIN_RRI_LIMITE)
						psQRSComplex[i].u8RRI_ValidFlagIdx = 0;
				}
				
				if (psQRSComplex[i].u8RRI_ValidFlagIdx == 1) 
				{
					i32Mean += psQRSComplex[i].i32RR_Interval;
					i16Count++;
				}
			}

			i32Mean /= SAFE_DIVIDE32(i16Count);

			/**********************************************
			 ** Get one max value and its index
			 **********************************************/
			for (i = 0; i < i16TotalCount; i++) 
			{
				if (psQRSComplex[i].u8RRI_ValidFlagIdx == 1) 
				{
					if (ABS(psQRSComplex[i].i32RR_Interval - i32Mean) > i16MaxDiff) 
					{
						i16MaxDiff = ABS(psQRSComplex[i].i32RR_Interval - i32Mean);
						i16SkipIndex = i;
					}
				}
			}

			/**********************************************
			 ** Recalculate sigma value
			 **********************************************/
			i32Sigma = 0;
			for (i = 0; i < i16TotalCount; i++) {
				if (psQRSComplex[i].u8RRI_ValidFlagIdx == 1) {
					i32Sigma += MY_GetDiffSquare(psQRSComplex[i].i32RR_Interval, (short) i32Mean);
				}
			}

			i32Sigma = (long) sqrt((float) (i32Sigma / SAFE_DIVIDE32(i16Count)));

			if (i32Sigma > i16SigmaThreshold) 
			{
				i16SkipCount++;
				psQRSComplex[i16SkipIndex].u8RRI_ValidFlagIdx = 0;
			}

			//if (i16Count <= i16CountThreshold)
				//break;

	} while (i32Sigma > i16SigmaThreshold);

	*pi16Mean = (short) i32Mean;
	*pi16Sigma = (short) i32Sigma;
	*pi16Count = i16Count;
}

void 
MY_FindECGWave(
	long		i32StartPoint,
	long		i32EndPoint,
	long*		pi32DynamicRSSlopeThreshold,
	long*		pi32QRSIndex,
	short*		pi16Mean, 
	short*		pi16Sigma, 
	short*		pi16Count,
	long* 		pi32SrcBuf,
	long*		pi32WorkBuf,
	QRS_PARAM* 	psQRSArray)
{
	short i = 0;
	
	
	short i16CheckRSAmpThresholdFlag = 0;
	long  i32LocalAvg = 0;
	long  i32LocalMax = 0;
	long  i32LocalTemp = 0;
	long  i32NextStartPoint = i32StartPoint;
	long  pi32StartSDPairIdx = *pi32QRSIndex;
	long  pi32EndSDPairIdx = *pi32QRSIndex;
	long  i32QRSLastIndex =  *pi32QRSIndex;
	
	//Step 2:
	//Find these correction of QRS pairs during the calculated buffer
	while ((i16CheckRSAmpThresholdFlag == 0) || (i32NextStartPoint + ECG_SEARCH_DURATION) <= i32EndPoint)
	{

		if ((i16CheckRSAmpThresholdFlag == 0) &&
			(i32NextStartPoint + ECG_SEARCH_DURATION) > i32EndPoint)
		{

			//Dynamic to modify the threshold of RS's Amplitude for first try run QRS's alogrithm.
			i16CheckRSAmpThresholdFlag = 1;
			i32NextStartPoint = i32StartPoint;

			if(*pi32QRSIndex > 1)
				i32LocalAvg /= (*pi32QRSIndex - 1);

            if(i32LocalMax > 0)
				*pi32DynamicRSSlopeThreshold = i32LocalMax * RS_DYNAMIC_FACTOR;

			for(i = pi32StartSDPairIdx; i < (*pi32QRSIndex); i++)
			{
				psQRSArray[i].u8RRI_ValidFlagIdx = 0;
				psQRSArray[i].i32Q_LeftPoint_1D_X = 0;		///< 2016.04.02 Clark Add
				psQRSArray[i].i32S_RightPoint_1D_X = 0;		///< 2016.04.02 Clark Add
				psQRSArray[i].i32MIN_Point_1D_X = 0;		///< 2016.04.02 Clark Add
				psQRSArray[i].i32Q_Point_X = 0;
				psQRSArray[i].i32R_Point_X = 0;
				psQRSArray[i].i32S_Point_X = 0;
				psQRSArray[i].i32R_Point_Y = 0;
				psQRSArray[i].i32Q_Point_Y = 0;
				psQRSArray[i].i32S_Point_Y = 0;
				psQRSArray[i].i32R_Point_1D_Y = 0;
				psQRSArray[i].i32Q_Point_1D_Y = 0;
				psQRSArray[i].i32S_Point_1D_Y = 0;
				psQRSArray[i].i32MIN_Point_1D_Y = 0;
				psQRSArray[i].i32Q_LeftPoint_1D_Y = 0;		///< 2016.05.07 Clark Add
				psQRSArray[i].i32S_RightPoint_1D_Y = 0;		///< 2016.05.07 Clark Add
				psQRSArray[i].i32R_QL_deta_X = 0;			///< 2016.04.02 Clark Add
				psQRSArray[i].i32R_SR_deta_X = 0;			///< 2016.04.02 Clark Add
				psQRSArray[i].i32QR_deta_X = 0;
				psQRSArray[i].i32RS_deta_X = 0;
				psQRSArray[i].i32QR_deta_Y = 0;
				psQRSArray[i].i32RS_deta_Y = 0;
				psQRSArray[i].i32R_Anglel = 0;
				psQRSArray[i].i32RR_Interval = 0;
				psQRSArray[i].i32RR_Search_Idx = 0;
			}
                        
            *pi32QRSIndex = pi32StartSDPairIdx;
            i32QRSLastIndex = pi32StartSDPairIdx;
			continue;
		}
		
		i32NextStartPoint = MY_SearchLocalQRSComplex( 	i32NextStartPoint,
														(i32NextStartPoint + ECG_SEARCH_DURATION),
														QRS_SEARCH_DURATION,
														(*pi32DynamicRSSlopeThreshold),
														pi32QRSIndex,
														pi32SrcBuf, 
														pi32WorkBuf,
														psQRSArray);

		if (i16CheckRSAmpThresholdFlag == 0)
		{
			if (i32QRSLastIndex == (*pi32QRSIndex - 1))
			{	
				i32LocalTemp = (psQRSArray[*pi32QRSIndex - 1].i32Q_Point_1D_Y - psQRSArray[*pi32QRSIndex - 1].i32MIN_Point_1D_Y);

				i32LocalAvg += i32LocalTemp;

				if(i32LocalTemp > i32LocalMax)
					i32LocalMax = i32LocalTemp;
				
				i32QRSLastIndex = *pi32QRSIndex;
			}
		}
	}

	if(*pi32QRSIndex >= LOCAL_MAX_POINTS_NUM)
		return;

	pi32EndSDPairIdx = (*pi32QRSIndex - 1);
	//Step 3:
	//Remove these incorrection of QRS pairs during the possible ECG wave
	MY_RemoveFalseRRI(	(pi32EndSDPairIdx - pi32StartSDPairIdx),//is only N - 1 for the RRI of number
						pi16Mean,
						pi16Count,
						&psQRSArray[pi32StartSDPairIdx]);
	//non-use
	#if 0	
	//Step 4:
	//Remove these incorrection of QRS pairs during the possible ECG wave
	MY_RemoveFalseQRSComplex(	(pi32EndSDPairIdx - pi32StartSDPairIdx),//is only N - 1 for the RRI of number
								STD_RRI_THRESHOLD,
								1,
								pi16Mean,
								pi16Sigma,
								pi16Count,
								&psQRSArray[pi32StartSDPairIdx]);
	#endif
}

void 
APPS_ShowHRResult(
	short i16RRIRealCount,
	short i16RRITotalCount,
	short i16HRSigama,
	short i16HROrignial,
	short i16HRFinal)
{
#if defined(USE_PC_SIMULATE)
	BCB_ShowHRResult(	i16RRIRealCount,
						i16RRITotalCount,
						i16HRSigama,
						i16HROrignial,
						i16HRFinal);
#endif
}

short APPS_ECG_SimulationRTOS(int *i32ECGRawBuffer)
{	
	short checkRRIMean = 0;
	short checkRRISigma = 0;
	short checkRRICount = 0;
	long  i = 0;
	long  checkQRSIndex = 0;
	long  i32DynamicRSSlopeThreshold = R_ANGLE_LIMIT;

	
	//Step 0: Allocated these buffer which is used to calculated in RTOS.
	APPS_ECG_NewBuffer();

	//Step 1: Initial these buffer which is used to calculated in RTOS.
	APPS_ECG_Initial();

	//Step 2: Put in the raw data to g_i32ECGInBuffer from i32ECGRawBuffer.
	for(i=0; i<g_i32CalcultedLength; i++)
	{
		g_i32ECGInBuffer[i]= i32ECGRawBuffer[i];
	}

	//Step 3: Perform the signal pre-processing in RTOS.

	//Step 3-1: Perofrm the Overflow protection
	MY_PerformOFProtect(	g_i32ECGOutBuffer,
							g_i32ECGInBuffer,
							g_i32CalcultedLength);

    //Step 3-2: Perofrm the Median Filter to remove the part of "BaseLine Wonder" & "Muscle Artifact" part  
	MY_PerformECGMedian(g_i32ECGInBuffer,
						g_i32ECGOutBuffer,
						g_i32CalcultedLength,
						g_i32CalcultedLength,
						ECG_MEDIAN_POINT);

	//Step 3-3: Perofrm the FIR Filter to remove the "High Frequency" component
	MY_PerformECGFIR(	g_i32ECGOutBuffer,
						g_i32ECGInBuffer,
						g_i32CalcultedLength,
						g_i32CalcultedLength,
						31,
						LPF_FIR_COEFF);

	//Step 3-4: Perofrm the Mean Filter to smooth the curve
	MY_PerformECGMean(	g_i32ECGInBuffer,
						g_i32ECGOutBuffer,
						g_i32CalcultedLength,
						g_i32CalcultedLength,
						ECG_MEAN_POINT);
#if 0 // non-use
	//Step 3-5: Perofrm the Special Filter to avoid the overflow for local area
	MY_PerformSpecialFiter(	g_i32ECGOutBuffer,
							g_i32ECGInBuffer,
							g_i32CalcultedLength,
							0);

	//Step 3-6: Perofrm the Differential Filter to check the maximum slope
	MY_Perform1StepDiff(	g_i32ECGInBuffer,
							g_i32ECGOutBuffer,
							1,
							g_i32CalcultedLength,
							g_i32CalcultedLength);
#endif
	//Step 4: Perform the ECG Algorithm in RTOS.
	MY_FindECGWave( 0,
					g_i32CalcultedLength,
					&i32DynamicRSSlopeThreshold,
					&checkQRSIndex,
					&checkRRIMean,
					&checkRRISigma,
					&checkRRICount,
					g_i32ECGInBuffer,
					g_i32ECGWorkBuffer,
					g_enQRSPairArray);

	//Step 5: Judge the HR result during the time frame
	APPS_JudgeHRResult(	checkRRICount,
						checkRRIMean,
						&g_i16OriginalAvgHR,
						&g_i16FinalAvgHR);

	//Step 5-1: Debug the HR result for BCB software
	//APPS_ShowHRResult(	checkRRICount,
						//(checkQRSIndex - 1),
						//checkRRISigma,
						//g_i16OriginalAvgHR,
						//g_i16FinalAvgHR);

	//Step 5-2: Debug the QRS Complex for BCB software 
#if defined(USE_PC_SIMULATE)
	MY_SaveECGFeatrue(g_enQRSPairArray, checkQRSIndex);

	MY_CalECAverageSNR(g_i32ECGInBuffer, g_enQRSPairArray, checkQRSIndex);
#endif
	//Step 6: Free these buffer which is used to calculated in RTOS. 
	APPS_ECG_DeleteBuffer();

	//Step 7: Add the counter for time frame
	g_i32RecordHRVFrameIdx++;
	return g_i16FinalAvgHR;
}

float APPS_ECG_GetSDNN() {
	return g_i16HRV_SDNN;
}

float APPS_ECG_GetRMSSD() {
	return g_i16HRV_RMSSD;
}

void APPS_ECG_RRI_DATA(double *rriAry, double *timeAry) {
	int i = 0;

	for (i = 0; i < HRV_RRI_MAX_BUF; i++) {
		rriAry[i] = g_i16HistoryRRIBuf[i];
		timeAry[i] = g_i32RecordHRVTimeBuf[i];
	}
}

short* GET_CURRENT_RRI_BUF(){

	return g_i16CurrentRRIBuf;
}

#if 0
//For Example
#define TOTAL_SAMPLE_TEST				(15 * ECG_SAMPLE_RATE)	//15 second for testing
long g_i32RTOSExampleBuf1[900000] = {0};
long g_i32RTOSExampleBuf2[1500] = {0};
short MY_ECG_ALGO_EXAMPLE(long *FIR_out_buff)
{
	short totalLoop = 0;
	long frameIdx = 0;
	long sampleIdx = 0;
	long i32EndScanPoint = 0;

	//Rounding
	totalLoop = ((TOTAL_SAMPLE_TEST * 10) /ECG_SHIFT_SAMPLE + 5) / 10;
	
	for(sampleIdx = 0; sampleIdx < TOTAL_SAMPLE_TEST; sampleIdx++)
	{
		g_i32RTOSExampleBuf1[sampleIdx] = (long)FIR_out_buff[sampleIdx];
	}
	
	//Step A:
	APPS_ECG_InitialForModeChange();
	
	for(frameIdx=0; frameIdx<(long)totalLoop; frameIdx++)
	{

		i32EndScanPoint = (CAL_TIME_BUF * ECG_SHIFT_SAMPLE) + ((frameIdx * ECG_SHIFT_SAMPLE));

		if(i32EndScanPoint > TOTAL_SAMPLE_TEST)
		{
			i32EndScanPoint = TOTAL_SAMPLE_TEST;	
			totalLoop = frameIdx + 1;
		}

		for(sampleIdx = 0; sampleIdx < g_i32CalcultedLength; sampleIdx++)
		{
			g_i32RTOSExampleBuf2[sampleIdx] = (long)g_i32RTOSExampleBuf1[(frameIdx *ECG_SHIFT_SAMPLE) + sampleIdx];
		}

		//Step B:
		APPS_ECG_SimulationRTOS(&g_i32RTOSExampleBuf2[0]);
	}
	return 0;
}
#endif

