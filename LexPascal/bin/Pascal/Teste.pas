program Teste;

var
  x, y: integer;
  nome: string;
  resultado: real;

begin
  x := 10;
  y := 20;
  nome := 'Joao';
  resultado := (x + y) / 2.0;

  if resultado > 15 then
    writeln('Resultado maior que quinze')
  else
    writeln('Resultado menor ou igual a quinze');

end.