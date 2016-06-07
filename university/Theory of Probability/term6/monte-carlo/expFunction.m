function retVal = expFunction(t)
  one = cos(2 ./ t - 5);
  two = t.^ 2;
  four = cos(-2 ./ t - 1);
  three = (1 - t).^2 ./ two;
  retVal = (one + four) .* exp(-three) ./ two;
  
endfunction