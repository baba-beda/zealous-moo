function [] = volumeTask()
  printf("Task 1: \nEstimate the volume of {F(x) = f(x_1) + ... + f(x_k) <= c}, where x_i is from [0, 1], k - dimensionality, \nif c = %f, k = %d, f(t) = t^%d\n", 1.4, 6, 3); 
  [vol1, disp1] = functionVolume(10000, 6, 1.4, 3);
  [left1, right1] = confidenceInt(vol1, 0.64, disp1, 10000);
  printf("n = %d: %f <= V <= %f\n", 10000, left1, right1);
  
  [vol2, disp2] = functionVolume(1000000, 6, 1.4, 3);
  [left2, right2] = confidenceInt(vol2, 0.64, disp2, 1000000);
  printf("n = %d: %f <= V <= %f\n", 1000000, left2, right2);
endfunction

function [volume, disp] = functionVolume(n, k, c, a)
  X = unifrnd(0, 1, k, n);
  y = sum(X .^ a);
  res = y <= c;
  volume = mean(res);
  disp = dispersion(res);
endfunction
 