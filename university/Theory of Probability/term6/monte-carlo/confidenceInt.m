function [left, right] = confidenceInt(mX, t, disp, n)
  left = mX - t * sqrt(disp) / n;
  right = mX + t * sqrt(disp) / n;
endfunction

