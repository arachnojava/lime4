@echo off
rem call ftt_makejar.bat
call wtt_makejar.bat
cd ..

md Release

copy bin\*.jar Release
copy bin\*.jar Scripts
