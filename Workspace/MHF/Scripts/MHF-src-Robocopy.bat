REM #################################################################
REM # Note that it is expected that this batch file will be in a    #
REM # Scripts directory inside the workspace directory.             #
REM #################################################################

cd ..
md bin

robocopy src ..\..\Botz\MHF\src /MIR

pause
