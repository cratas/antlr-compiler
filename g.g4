grammar g;

/** The start rule; begin parsing here. */
program: line* ;

line
    : declaration
    | assigment
    | exp
    | condition
    | loop
    | print ';'
    | read ';'
    | COMMENT
    | ';'
    ;

declaration : datatype ID (',' ID)*;

assigment : exp
    | ID '=' assigment
    ;

print : 'write ' (output)+ ;

output : STRING(',' exp ',')*(',' exp)*
    ;

read : 'read' ID (',' ID)*;

condition
    : 'if ('exp')' block ('else' elseBlock)*
    ;

elseBlock
    : block
    | condition
    ;

loop
    : 'while(' exp ')' block
    ;

block
    : line
    | '{' line+ '}'
    ;

exp
    : INT                                   # int
    | FLOAT                                 # float
    | BOOL                                  # bool
    | STRING                                # string
    | ID                                    # identifier
    | '(' exp ')'                           # par
    | '!(' exp ')'                          # not
    | exp op=('*'|'/'|'%') exp              # mul
    | exp op=('+'|'-') exp                  # a
    | STRING ('.') STRING                   # concat
    | exp comp=('<'|'>'|'=='|'!=') exp      # comp
    | exp '&&' exp                          # and
    | exp '||' exp                          # or
    ;

datatype
    : 'int'
    | 'string'
    | 'float'
    | 'bool'
    ;

// Char sequence recognition by regex
INT : ('-')?[1-9]*[0-9]+ ;
FLOAT : ('-')?[0-9]+'.'[0-9]+ ;
BOOL : 'true' | 'false' ;
STRING : ('"' ~'"'* '"') ;
ID : [a-zA-Z_][a-zA-Z0-9_]* ;

// Skippers
WS : [ \t\r\n]+ -> skip ;
COMMENT : '//' ~( '\r' | '\n' )* -> skip;


