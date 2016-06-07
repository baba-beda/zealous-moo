function [] = integralTask()
  printf("Task 2:\nEstimate the definite integral of function f(x)\na) f(x) = ln(1 + x^2) with limits [2,5]\n");
  [int1, disp1] = integralLog(10000);
  left1 = int1 - 0.64 * sqrt(disp1) / 10000;
  right1 = int1 + 0.64 * sqrt(disp1) / 10000;
  printf("n = %d: %f <= I <= %f\n", 10000, left1, right1);
  [int2, disp2] = integralLog(1000000);
  left2 = int2 - 0.64 * sqrt(disp2) / 1000000;
  right2 = int2 + 0.64 * sqrt(disp2) / 1000000;
  printf("n = %d: %f <= I <= %f\n", 1000000, left2, right2);
  
  printf("\nb) f(x) = cos(x)exp(-(x+3)^2/4) with limits [-inf, inf]\n");
  [int3, disp3] = integralExp(10000);
  left3 = int3 - 0.64 * sqrt(disp3) / 10000;
  right3 = int3 + 0.64 * sqrt(disp3) / 10000;
  printf("n = %d: %f <= I <= %f\n", 10000, left3, right3);
  [int4, disp4] = integralExp(1000000);
  left4 = int4 - 0.64 * sqrt(disp4) / 1000000;
  right4 = int4 + 0.64 * sqrt(disp4) / 1000000;
  printf("n = %d: %f <= I <= %f\n", 1000000, left4, right4);
  
endfunction