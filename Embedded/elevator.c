Program Code

/******************************************************************************/
/* ARM_CortexM3_NXP_Stepmotor.C: Stepmotor run                                */
/******************************************************************************/
/* This file is part of the uVision/ARM development tools.                    */
/* Copyright (c) 2009 HUINS. All rights reserved.    		                  */
/* This software may only be used under the terms of a valid, current,        */
/* end user licence from KEIL for a compatible version of KEIL software       */
/* development tools. Nothing else gives you the right to use this software.  */
/******************************************************************************/

#include "LPC17xx.h"                    /* LPC17xx definitions                */
#include "delay.h"                      /* Delay definitions                */
#include "EXT_IO.h"
#include "stepmotor.h" 

#include "lpc17xx_uart.h"
#include "lpc17xx_libcfg_default.h"
#include "lpc17xx_nvic.h"
#include "lpc17xx_pinsel.h"
#include "lpc17xx_clkpwr.h"

#include "lpc17xx_gpio.h"
#include "lpc17xx_pwm.h"
#include "lpc17xx_mcpwm.h"
#include "lpc17xx_libcfg_default.h"
#include "lpc17xx_adc.h"
#include "debug_frmwrk.h"
#include "lpc17xx_dac.h"

#include "LPC17xx.H"                    /* LPC17xx definitions                */
#include "FND.H"      

#define TEST_UART LPC_UART0




uint8_t menu0[] = " 1 Floor\n\r";
uint8_t menu1[] = " 2 Floor\n\r";
uint8_t menu2[] = " 3 Floor\n\r";
uint8_t menu3[] = " 4 Floor\n\r";
uint8_t menu4[] = " 5 Floor\n\r";
uint8_t menu5[] = "Program is over";

const unsigned long led_mask[] = { 1 << 28, 1 << 29, 1UL << 31, 1 << 2, 1 << 3 };


uint32_t idx, len;				//키 값 설정
__IO FlagStatus exitflag;			//종료를 위한 변수 설정
uint8_t buffer[10];

uint32_t led;


void Stepmotor_Test() {
	if (motor_cnt < 2000) Stepmotor_run('A', 'F', 100);				//Stepmotor('EXT_IO Port', 'Direction', Speed);
	else if (motor_cnt < 4000) Stepmotor_run('A', 'B', 100);		//Stepmotor('EXT_IO Port', 'Direction', Speed);
	else if (motor_cnt < 6000) Stepmotor_run('A', 'F', 70);			//Stepmotor('EXT_IO Port', 'Direction', Speed);
	else if (motor_cnt < 8000) Stepmotor_run('A', 'B', 70);			//Stepmotor('EXT_IO Port', 'Direction', Speed);
	else motor_cnt = 0;
}



static int flag;



int main(void) {                       /* Main Program                       */

	int k;
	int current = '1';
	static int i;

	UART_CFG_Type UARTConfigStruct;			//UART 타입 구조체 생성
	UART_FIFO_CFG_Type UARTFIFOConfigStruct;	//UART FIFO 타입 구조체 생성

	PINSEL_CFG_Type PinCfg;				//핀 타입 구조체 생성
	PINSEL_CFG_Type PinCfg2;

	uint32_t time;
	float rate = 0;

	SystemInit();

	SystemCoreClockUpdate();
	SysTick_Config(SystemCoreClock / 1000); /* Generate interrupt each 1 ms   */
	EXT_IO_CS_init();
	Stepmotor_init();

	PinCfg.Portnum = 0;
	PinCfg.Pinnum = 2;
	PinCfg.Funcnum = 1;
	PinCfg.Pinmode = 0;
	PinCfg.OpenDrain = 0;				//포트 0번의 2번핀 설정

	PINSEL_ConfigPin(&PinCfg);			//2번 핀 설정 저장

	PinCfg2.Portnum = 0;
	PinCfg2.Pinnum = 26;
	PinCfg2.Funcnum = 2;
	PinCfg2.Pinmode = 0;
	PinCfg2.OpenDrain = 0;

	PINSEL_ConfigPin(&PinCfg2);

	PinCfg.Pinnum = 3;				//3번 핀(UART0 RX) 설정
	PINSEL_ConfigPin(&PinCfg);			//3번 핀 설정 저장

	UART_ConfigStructInit(&UARTConfigStruct);		//UARTConfigStruct로 UART 초기화
	UART_Init(TEST_UART, &UARTConfigStruct);		//UART0 초기화
	UART_FIFOConfigStructInit(&UARTFIFOConfigStruct);  	//UARTFIFOConfigStruct 멤버들의 값으로 초기화
	UART_FIFOConfig(TEST_UART, &UARTFIFOConfigStruct); 	//LPC_UART0 에 FIFO 기능을 설정
	UART_TxCmd(TEST_UART, ENABLE);				//UART TxD 핀의 통신 활성화


	GPIO_SetDir(1, 0xB0000000, 1);
	GPIO_SetDir(2, 0x0000007C, 1);

	GPIO_SetValue(1, 0x10000000);

	DAC_Init(LPC_DAC);

	exitflag = RESET;

	while (exitflag == RESET)
	{
		len = 0;
		while (len == 0)
		{
			len = UART_Receive(TEST_UART, buffer, sizeof(buffer), NONE_BLOCKING); // LPC_UART0에서 입력 버퍼로부터 DATA(buffer)를 받는다 (uint32_t로 반환된다)
		}
		idx = 0;
		while (idx < len)
		{
			if (buffer[idx] == 27) 							// buffer[idx] 값이 ESC 이면
			{
				UART_Send(TEST_UART, menu5, sizeof(menu5), BLOCKING);		// LPC_UART0를 통해 "Program is over"를 전송한다. (결과 값은 하이퍼 터미널에 출력)
				exitflag = SET;							// exitflag = SET으로 설정, 반복문 종료
			}

			if (buffer[idx] > current)		//올라갈때****************************************************************************************************
			{
				if (buffer[idx] == '2')				//2층갈때****************************************************************************
				{

					flag = buffer[idx];

					UART_Send(TEST_UART, menu1, sizeof(menu1), BLOCKING); // LPC_UART0를 통해 "entered A \n\r"를 전송한다. (결과 값은 하이퍼 터미널에 출력)
					for (k = 0; k < 1000000 * (flag - current); k++)
					{
						if (current == '1')
						{
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}
						}
						if (motor_cnt < 2000) Stepmotor_run('A', 'F', 100);
						else motor_cnt = 0;

					}

					for (k = 0; k < 100; k++)
					{
						for (time = 1; time < 0x3FF; time++)
							DAC_UpdateValue(LPC_DAC, (uint32_t)(time*rate));

						rate = 0.2;
						current = '2';
					}


				}
				else if (buffer[idx] == '3')				//3층갈때****************************************************************************
				{
					flag = buffer[idx];
					UART_Send(TEST_UART, menu2, sizeof(menu2), BLOCKING);		// LPC_UART0를 통해 "entered S \n\r"를 전송한다.
					SystemInit();

					for (k = 0; k < 1000000 * (flag - current); k++)
					{
						if (current == '1')
						{
							if (k == 500000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[2]);
							}
						}
						if (current == '2')
						{
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[2]);
							}
						}

						if (motor_cnt < 2000) Stepmotor_run('A', 'F', 100);
						else motor_cnt = 0;
					}
					for (k = 0; k < 100; k++)
					{
						for (time = 1; time < 0x3FF; time++)
							DAC_UpdateValue(LPC_DAC, (uint32_t)(time*rate));

						rate = 0.2;
						current = '3';
					}
				}
				else if (buffer[idx] == '4')					//4층갈때****************************************************************************
				{
					flag = buffer[idx];
					UART_Send(TEST_UART, menu3, sizeof(menu3), BLOCKING);		// LPC_UART0를 통해 "entered S \n\r"를 전송한다.
					SystemInit();

					for (k = 0; k < 1000000 * (flag - current); k++)
					{
						if (current == '1')
						{
							if (k == 330000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}
							if (k == 660000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[2]);
							}
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(2, led_mask[3]);
							}
						}
						if (current == '2')
						{
							if (k == 450000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[2]);
							}
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(2, led_mask[3]);
							}
						}
						if (current == '3')
						{
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(2, led_mask[3]);
							}
						}

						if (motor_cnt < 2000) Stepmotor_run('A', 'F', 100);
						else motor_cnt = 0;
					}
					for (k = 0; k < 100; k++)
					{
						for (time = 1; time < 0x3FF; time++)
							DAC_UpdateValue(LPC_DAC, (uint32_t)(time*rate));

						rate = 0.2;
						current = '4';
					}
				}
				else if (buffer[idx] == '5')					//5층갈때****************************************************************************
				{

					flag = buffer[idx];
					UART_Send(TEST_UART, menu4, sizeof(menu4), BLOCKING);		// LPC_UART0를 통해 "entered S \n\r"를 전송한다.
					SystemInit();

					for (k = 0; k < 1000000 * (flag - current); k++)
					{
						if (current == '1')
						{
							if (k == 250000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}

							if (k == 500000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[2]);
							}
							if (k == 750000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(2, led_mask[3]);
							}
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(2, led_mask[4]);
							}

						}
						if (current == '2')
						{
							if (k == 330000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[2]);
							}
							if (k == 660000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(2, led_mask[3]);
							}
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(2, led_mask[4]);
							}
						}
						if (current == '3')
						{
							if (k == 450000 * (flag - current))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(2, led_mask[3]);
							}
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(2, led_mask[4]);
							}
						}
						if (current == '4')
						{
							if (k == 990000 * (flag - current))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(2, led_mask[4]);
							}
						}

						if (motor_cnt < 2000) Stepmotor_run('A', 'F', 100);
						else motor_cnt = 0;

					}

					for (k = 0; k < 100; k++)
					{
						for (time = 1; time < 0x3FF; time++)
							DAC_UpdateValue(LPC_DAC, (uint32_t)(time*rate));

						rate = 0.2;
						current = '5';
					}
				}

			}


			else if (buffer[idx] < current)		//내려갈때***********************************************************************************************************
			{
				if (buffer[idx] == '1')				//1층갈때****************************************************************************
				{
					flag = buffer[idx];

					UART_Send(TEST_UART, menu0, sizeof(menu0), BLOCKING); // LPC_UART0를 통해 "entered A \n\r"를 전송한다. (결과 값은 하이퍼 터미널에 출력)
					for (k = 0; k < 1000000 * (current - flag); k++)
					{
						if (current == '5')
						{
							if (k == 250000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(2, led_mask[3]);
							}

							if (k == 500000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(1, led_mask[2]);
							}
							if (k == 750000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[0]);
							}

						}
						if (current == '4')
						{
							if (k == 330000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(1, led_mask[2]);
							}
							if (k == 660000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[0]);
							}
						}
						if (current == '3')
						{
							if (k == 450000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[0]);
							}
						}
						if (current == '2')
						{
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[0]);
							}
						}
						if (motor_cnt < 2000) Stepmotor_run('A', 'B', 100);
						else motor_cnt = 0;
					}
					for (k = 0; k < 100; k++)
					{
						for (time = 1; time < 0x3FF; time++)
							DAC_UpdateValue(LPC_DAC, (uint32_t)(time*rate));

						rate = 0.2;
						current = '1';
					}
				}
				else if (buffer[idx] == '2')												//2층갈때****************************************************************************
				{
					flag = buffer[idx];

					UART_Send(TEST_UART, menu1, sizeof(menu1), BLOCKING); // LPC_UART0를 통해 "entered A \n\r"를 전송한다. (결과 값은 하이퍼 터미널에 출력)
					for (k = 0; k < 1000000 * (current - flag); k++)
					{
						if (current == '5')
						{
							if (k == 330000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(2, led_mask[3]);
							}
							if (k == 660000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(1, led_mask[2]);
							}
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}

						}
						if (current == '4')
						{
							if (k == 450000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(1, led_mask[2]);
							}
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}
						}
						if (current == '3')
						{
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(1, 0xB0000000);
								GPIO_SetValue(1, led_mask[1]);
							}
						}
						if (motor_cnt < 2000) Stepmotor_run('A', 'B', 100);
						else motor_cnt = 0;
					}
					for (k = 0; k < 100; k++)
					{
						for (time = 1; time < 0x3FF; time++)
							DAC_UpdateValue(LPC_DAC, (uint32_t)(time*rate));

						rate = 0.2;
						current = '2';
					}
				}
				else if (buffer[idx] == '3')				//3층갈때****************************************************************************
				{
					flag = buffer[idx];

					UART_Send(TEST_UART, menu2, sizeof(menu2), BLOCKING); // LPC_UART0를 통해 "entered A \n\r"를 전송한다. (결과 값은 하이퍼 터미널에 출력)
					for (k = 0; k < 1000000 * (current - flag); k++)
					{
						if (current == '5')
						{
							if (k == 450000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(2, led_mask[3]);
							}
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(1, led_mask[2]);
							}

						}
						if (current == '4')
						{
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(1, led_mask[2]);
							}
						}

						if (motor_cnt < 2000) Stepmotor_run('A', 'B', 100);
						else motor_cnt = 0;
					}
					for (k = 0; k < 100; k++)
					{
						for (time = 1; time < 0x3FF; time++)
							DAC_UpdateValue(LPC_DAC, (uint32_t)(time*rate));

						rate = 0.2;
						current = '3';
					}
				}
				else if (buffer[idx] == '4')			//4층갈때****************************************************************************
				{
					flag = buffer[idx];

					UART_Send(TEST_UART, menu3, sizeof(menu3), BLOCKING); // LPC_UART0를 통해 "entered A \n\r"를 전송한다. (결과 값은 하이퍼 터미널에 출력)
					for (k = 0; k < 1000000 * (current - flag); k++)
					{
						if (current == '5')
						{
							if (k == 990000 * (current - flag))
							{
								GPIO_ClearValue(2, 0x0000007C);
								GPIO_SetValue(2, led_mask[3]);
							}
						}
						if (motor_cnt < 2000) Stepmotor_run('A', 'B', 100);
						else motor_cnt = 0;
					}
					for (k = 0; k < 100; k++)
					{
						for (time = 1; time < 0x3FF; time++)
							DAC_UpdateValue(LPC_DAC, (uint32_t)(time*rate));

						rate = 0.2;
						current = '4';
					}
				}

			}
			else									// 다른 모든 입력에 대해
			{
				UART_Send(TEST_UART, &buffer[idx], 1, BLOCKING);		// 입력된 데이터를 되보낸다 ***(echo back)*** => 자료의 정확도를 확인 
			}
			idx++;

		}
	}

	while (UART_CheckBusy(TEST_UART) == SET);		//현재 전송이 완료될때까지 기다린다
	UART_DeInit(TEST_UART);				//UART 종료

	return 1;

}
