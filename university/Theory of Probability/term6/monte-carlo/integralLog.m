function [integral, disp] = integralLog(n)
  X = unifrnd(2, 5, n, 1);
  y = logFunction(X);
  expectation = mean(y);
  disp = dispersion(y) * 9;
  integral = expectation * 3;
endfunction
