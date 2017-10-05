package com.mhframework.platform.event;

import com.mhframework.platform.MHPlatform;

public class MHKeyEvent
{
    private int keyCode;
    private boolean ctrl = false;
    private boolean alt = false;
    private boolean shift = false;
    
    
    public MHKeyEvent(int keyCode)
    {
        this.keyCode = keyCode;
    }
    
    
    public MHKeyEvent(int keyCode, boolean shift, boolean ctrl, boolean alt)
    {
        this.keyCode = keyCode;
        this.shift = shift;
        this.ctrl = ctrl;
        this.alt = alt;
    }
    
    
    public int getKeyCode()
    {
        return keyCode;
    }
    
    
    public boolean isShiftPressed()
    {
        return shift;
    }
    
    
    public boolean isControlPressed()
    {
        return ctrl;
    }
    
    
    public boolean isAltPressed()
    {
        return alt;
    }


    public char getKeyChar()
    {
        MHKeyCodes codes = MHPlatform.getKeyCodes(); 
        if (keyCode == codes.keyPeriod()) return '.';
        if (keyCode == codes.keySpace()) return ' ';
        if (shift)
        {
            if (keyCode == codes.keyA()) return 'A';
            if (keyCode == codes.keyB()) return 'B';
            if (keyCode == codes.keyC()) return 'C';
            if (keyCode == codes.keyD()) return 'D';
            if (keyCode == codes.keyE()) return 'E';
            if (keyCode == codes.keyF()) return 'F';
            if (keyCode == codes.keyG()) return 'G';
            if (keyCode == codes.keyH()) return 'H';
            if (keyCode == codes.keyI()) return 'I';
            if (keyCode == codes.keyJ()) return 'J';
            if (keyCode == codes.keyK()) return 'K';
            if (keyCode == codes.keyL()) return 'L';
            if (keyCode == codes.keyM()) return 'M';
            if (keyCode == codes.keyN()) return 'N';
            if (keyCode == codes.keyO()) return 'O';
            if (keyCode == codes.keyP()) return 'P';
            if (keyCode == codes.keyQ()) return 'Q';
            if (keyCode == codes.keyR()) return 'R';
            if (keyCode == codes.keyS()) return 'S';
            if (keyCode == codes.keyT()) return 'T';
            if (keyCode == codes.keyU()) return 'U';
            if (keyCode == codes.keyV()) return 'V';
            if (keyCode == codes.keyW()) return 'W';
            if (keyCode == codes.keyX()) return 'X';
            if (keyCode == codes.keyY()) return 'Y';
            if (keyCode == codes.keyZ()) return 'Z';
            if (keyCode == codes.key0()) return ')';
            if (keyCode == codes.key1()) return '!';
            if (keyCode == codes.key2()) return '@';
            if (keyCode == codes.key3()) return '#';
            if (keyCode == codes.key4()) return '$';
            if (keyCode == codes.key5()) return '%';
            if (keyCode == codes.key6()) return '^';
            if (keyCode == codes.key7()) return '&';
            if (keyCode == codes.key8()) return '*';
            if (keyCode == codes.key9()) return '(';
            if (keyCode == codes.keyForwardSlash()) return '/';
        }
        else
        {
            if (keyCode == codes.keyA()) return 'a';
            if (keyCode == codes.keyB()) return 'b';
            if (keyCode == codes.keyC()) return 'c';
            if (keyCode == codes.keyD()) return 'd';
            if (keyCode == codes.keyE()) return 'e';
            if (keyCode == codes.keyF()) return 'f';
            if (keyCode == codes.keyG()) return 'g';
            if (keyCode == codes.keyH()) return 'h';
            if (keyCode == codes.keyI()) return 'i';
            if (keyCode == codes.keyJ()) return 'j';
            if (keyCode == codes.keyK()) return 'k';
            if (keyCode == codes.keyL()) return 'l';
            if (keyCode == codes.keyM()) return 'm';
            if (keyCode == codes.keyN()) return 'n';
            if (keyCode == codes.keyO()) return 'o';
            if (keyCode == codes.keyP()) return 'p';
            if (keyCode == codes.keyQ()) return 'q';
            if (keyCode == codes.keyR()) return 'r';
            if (keyCode == codes.keyS()) return 's';
            if (keyCode == codes.keyT()) return 't';
            if (keyCode == codes.keyU()) return 'u';
            if (keyCode == codes.keyV()) return 'v';
            if (keyCode == codes.keyW()) return 'w';
            if (keyCode == codes.keyX()) return 'x';
            if (keyCode == codes.keyY()) return 'y';
            if (keyCode == codes.keyZ()) return 'z';
            if (keyCode == codes.key0()) return '0';
            if (keyCode == codes.key1()) return '1';
            if (keyCode == codes.key2()) return '2';
            if (keyCode == codes.key3()) return '3';
            if (keyCode == codes.key4()) return '4';
            if (keyCode == codes.key5()) return '5';
            if (keyCode == codes.key6()) return '6';
            if (keyCode == codes.key7()) return '7';
            if (keyCode == codes.key8()) return '8';
            if (keyCode == codes.key9()) return '9';
            if (keyCode == codes.keyForwardSlash()) return '?';
        }
        
        return 0;
    }
}
