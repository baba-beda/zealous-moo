function [volume, disp] = functionVolume(n, k, c, a)
  X = unifrnd(0, 1, k, n);
  y = sum(X .^ a);
  res = y <= c;
  volume = mean(res);
  disp = dispersion(res);
endfunction
 