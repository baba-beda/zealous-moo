function [] = volumeTask(k, c, a)
  printf("Task 1: \nEstimate the volume of {F(x) = f(x_1) + ... + f(x_k) <= c}, where x_i is from [0, 1], k - dimensionality, \nif c = %f, k = %d, f(t) = t^%f\n", c, k, a); 
  [vol1, left1, right1] = functionVolume(10000, k, c, a);
  printf("n = %d: %f <= V <= %f\n", 10000, left1, right1);
  
  [vol2, left2, right2] = functionVolume(1000000, k, c, a);
  printf("n = %d: %f <= V <= %f\n", 1000000, left2, right2);
endfunction

function [volume, left, right] = functionVolume(n, k, c, a)
  X = unifrnd(0, 1, k, n);
  y = sum(X .^ a);
  res = y <= c;
  volume = mean(res);
  [left, right] = confidenceInt(res);
endfunction
 