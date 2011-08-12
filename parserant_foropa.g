grammar parserant_foropa;

options {
	output = AST;
	//backtrack = true;
}

start:	 type_declaration+ EOF!;

/*
decoup
	:	(	type_declaration
		//|	WS
		|	~(TYPE | '=')+ '=' .* TYPE 
		)* EOF!
	;
*/

type_declaration
	:	TYPE (WS type_name type_arguments? WS? '=' WS? type)?
	;
	
type_name	: ID | RAW;
	
type_arguments
	:	'(' WS? TYPEVARIABLE WS? (',' WS? TYPEVARIABLE WS?)* ')'
	;
	
type:	TYPEVARIABLE
	|	type_tuple
	|	type_named
	| 	type_record
	;
	
type_tuple	
	: 	'(' WS? type WS? (',' WS? type WS?)* ')'
	;
	
type_named
	:	type_name ('(' WS? type_named_argument WS? (',' WS? type_named_argument WS?)* ')')?
	;

type_named_argument
	:	TYPEVARIABLE
	|	type_tuple
	|	type_named
	;
	
type_record options { greedy = true; }
	:	'{' WS?  field_definition* '}' 
	;

field_definition
	:	field_name WS? (':' WS?  type WS?)?	(';' WS?)?
	;
	
field_name
	:	ID | RAW
	;

TYPE:	'type';

any	:	~TYPE;

TYPEVARIABLE	: '\'' ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* 
				| '\'' RAW
				;

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'.')*
    ;
    
RAW	: '`' ~('`')* '`';

/*
WS  :   ( ' ' | '\t' | '\r' | '\n')+ ; //{ skip(); };
*/
WS  :   SPACE+ ; //{ skip(); };

fragment 
SPACE
	: 	( ' ' | '\t' | '\r' | '\n')
	|	COMMENT_LINE | COMMENT_BLOCK //COMMENT
	;
/*	
COMMENT
	:	COMMENT_LINE | COMMENT_BLOCK
	;
*/
fragment
COMMENT_LINE
	:	'//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
	;
	
fragment	
COMMENT_BLOCK
	:	'/*' ( options {greedy=false;} : ( COMMENT_BLOCK | .) )* '*/' {$channel=HIDDEN;}
    ;

CHAR :	.;

/*
STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
*/
