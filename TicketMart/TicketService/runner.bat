@echo off
set klass=%1
shift
set targ=%klass%
set BASE=.
set LIB=.
set cplist=.
setlocal ENABLEDELAYEDEXPANSION
for %%f in (.\target\*.jar) do set cplist=!cplist!;%%f
for %%f in (.\lib\*.jar) do set cplist=!cplist!;%%f

@echo on
rem echo %cplist%

java -Xmx512m -classpath "%cplist%" %targ% %*
