function res = dispersion(y)
  aver = mean(y);
  res = sum((y - aver) .^ 2);
endfunction