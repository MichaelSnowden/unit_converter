grammar Arithmetic;

//program: expression EOF;

expression: expression op=('+' | '-') expression | term;

term: term op=('*' | '/') term | term factor | NEG* factor;

factor: factor op='^' NEG* factor | number | function | '(' expression ')' | string;

function: IDENTIFIER '(' expression (',' expression)* ')';

number: DecimalFloatingPointLiteral;

string: IDENTIFIER;

NEG : '-' ;

IDENTIFIER: [a-zA-Z]+;

DecimalFloatingPointLiteral
    :   Digits '.' Digits? ExponentPart? FloatTypeSuffix?
    |   '.' Digits ExponentPart? FloatTypeSuffix?
    |   Digits ExponentPart FloatTypeSuffix?
    |   Digits FloatTypeSuffix
    ;

fragment
IntegerTypeSuffix
    :   [lL]
    ;

fragment
DecimalNumeral
    :   '0'
    |   NonZeroDigit (Digits? | Underscores Digits)
    ;

fragment
Digits
    :   Digit (DigitOrUnderscore* Digit)?
    ;

fragment
Digit
    :   '0'
    |   NonZeroDigit
    ;

fragment
NonZeroDigit
    :   [1-9]
    ;

fragment
DigitOrUnderscore
    :   Digit
    |   '_'
    ;

fragment
Underscores
    :   '_'+
    ;

fragment
ExponentPart
    :   ExponentIndicator SignedInteger
    ;

fragment
ExponentIndicator
    :   [eE]
    ;

fragment
SignedInteger
    :   Sign? Digits
    ;

fragment
Sign
    :   [+-]
    ;

fragment
FloatTypeSuffix
    :   [fFdD]
    ;

fragment
ZeroToThree
    :   [0-3]
    ;

WHITESPACE: [ \n\r\t\u000C]+ -> skip;