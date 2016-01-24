grammar Arithmetic;

//program: expression EOF;

expression: expression op=('+' | '-') expression | term;

term: term op=('*' | '/') term | term factor | NEG* factor;

factor: factor op='^' NEG* factor | number | function | '(' expression ')' | string;

function: IDENTIFIER '(' expression (',' expression)* ')';

number: INTEGER | FLOAT;

string: IDENTIFIER;

NEG : '-' ;

INTEGER: '0' | [1-9] [0-9]*;

FLOAT: ('0' | [1-9] [0-9]*)'.' [0-9]+;

IDENTIFIER: [a-zA-Z]+;

WHITESPACE: [ \n\r\t]+ -> skip;