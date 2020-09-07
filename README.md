# README
## Configuring colors
This program reads the cfg.ini file to configure colors.
It will read the first 8 characters of the first line.
The first character determines the color mode.   All examples shown denote the default configuration.
**0** is ansi, and **1** is RGB mode. Not every terminal will support RGB. If so, use javaw to utilize RGB terminals.

####Characters and what they do
*1*: ANSI mode
*2*: board white color  
*3*: board black color  
*4*: piece white color  
*5*: piece black color
*6*: text color
*7*: background color
*8*: highlight white color  
*9*: highlight black color  
Characters are also seperated by space.

### Configuring ANSI
*b*: blue  
*c*: cyan  
*d*: default terminal color
*g*: green  
*m*: magenta  
*r*: red  
*w*: white  
*y*: yellow  
*l*: black  
Capital letters denote the bright version of the color.  
**Example**:  
> 0 y Y l w y L

### Configuring RGB
All characters are replaced with 9 digit numbers, 
the first 3 characters denoting red,
second 3 denoting green,
and the last 3 denoting blue.
**All numbers must be 9 digits long, no exceptions**
Default is still *d*.  
**Example**:  
> 1 d d 25525585 000000000 128128000 128128128  