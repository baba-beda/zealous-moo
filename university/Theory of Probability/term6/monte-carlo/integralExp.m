function [res, disp] = integralExp(n)
  X = unifrnd(0, 1, 1, n);
  y = expFunction(X);
  res = mean(y * 2);
  disp = dispersion(y * 2);
  
endfunction
  