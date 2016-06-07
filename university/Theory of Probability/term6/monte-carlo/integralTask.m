function [] = integralTask()
  printf("Task 2:\nEstimate the definite integral of function f(x)\na) f(x) = ln(1 + x^2) with limits [2,5]\n");
  [int1, left1, right1] = integralLog(10000);
  printf("n = %d: %f <= I <= %f\n", 10000, left1, right1);
  [int2, left2, right2] = integralLog(1000000);
  printf("n = %d: %f <= I <= %f\n", 1000000, left2, right2);
  
  printf("\nb) f(x) = cos(x)exp(-(x+3)^2/4) with limits [-inf, inf]\n");
  [int3, left3, right3] = integralExp(10000);
  printf("n = %d: %f <= I <= %f\n", 10000, left3, right3);
  [int4, left4, right4]= integralExp(1000000);
  printf("n = %d: %f <= I <= %f\n", 1000000, left4, right4);
endfunction

function [integral, left, right] = integralLog(n)
  X = unifrnd(2, 5, 1, n);
  y = logFunction(X) * 3;
  expectation = mean(y);
  integral = expectation;
  [left, right] = confidenceInt(y);
endfunction

function res = logFunction(X)
  res = log(1 + X .^ 2);
endfunction

function [res, left, right] = integralExp(n)
  X = unifrnd(0, 1, 1, n);
  y = expFunction(X);
  res = mean(y);
  [left, right] = confidenceInt(y);
endfunction
  
function res = expFunction(t)
  one = cos(2 ./ t - 5);
  two = t.^ 2;
  four = cos(-2 ./ t - 1);
  three =(1 - t).^2 ./ two;
  res = 2 * (one + four) .* exp(-three) ./ two;
endfunction

