function [] = integralTask()
  printf("Task 2:\nEstimate the definite integral of function f(x)\na) f(x) = ln(1 + x^2) with limits [2,5]\n");
  [int1, disp1] = integralLog(10000);
  [left1, right1] = confidenceInt(int1, 0.64, disp1, 10000);
  printf("n = %d: %f <= I <= %f\n", 10000, left1, right1);
  [int2, disp2] = integralLog(1000000);
  [left2, right2] = confidenceInt(int2, 0.64, disp2, 1000000);
  printf("n = %d: %f <= I <= %f\n", 1000000, left2, right2);
  
  printf("\nb) f(x) = cos(x)exp(-(x+3)^2/4) with limits [-inf, inf]\n");
  [int3, disp3] = integralExp(10000);
  [left3, right3] = confidenceInt(int3, 0.64, disp3, 10000);
  printf("n = %d: %f <= I <= %f\n", 10000, left3, right3);
  [int4, disp4]= integralExp(1000000);
  [left4, right4] = confidenceInt(int4, 0.64, disp4, 1000000);
  printf("n = %d: %f <= I <= %f\n", 1000000, left4, right4);
endfunction

function [integral, disp] = integralLog(n)
  X = unifrnd(2, 5, n, 1);
  y = logFunction(X);
  expectation = mean(y);
  disp = dispersion(y) * 9;
  integral = expectation * 3;
endfunction

function res = logFunction(X)
  res = log(1 + X .^ 2);
endfunction

function [res, disp] = integralExp(n)
  X = unifrnd(0, 1, 1, n);
  y = expFunction(X);
  res = mean(y * 2);
  disp = dispersion(y * 2);
endfunction
  
function res = expFunction(t)
  one = cos(2 ./ t - 5);
  two = t.^ 2;
  four = cos(-2 ./ t - 1);
  three = (1 - t).^2 ./ two;
  res = (one + four) .* exp(-three) ./ two;
endfunction

