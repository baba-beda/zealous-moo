function [left, right] = confidenceInt(X)
  disp = dispersion(X);
  n = size(X)(2);
  t = 0.64;
  mX = mean(X);
  left = mX - t * sqrt(disp) / n;
  right = mX + t * sqrt(disp) / n;
endfunction

