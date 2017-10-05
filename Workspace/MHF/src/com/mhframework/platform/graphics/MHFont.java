package com.mhframework.platform.graphics;

import java.util.ArrayList;

import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.platform.MHPlatform;

public abstract class MHFont
{
    public static enum Type
    {
        TTF,
        IMAGE
    }
    
    public static final String FONT_DIR = "./assets/engine/fonts/";
    public static int DEFAULT_SIZE = 12;
    private static MHFont DEFAULT_FONT;
    
    private String name;
    private Type type;

    
    protected MHFont(String fontName, Type fontType)
    {
        this.name = fontName;
        this.type = fontType;
    }
    
    
    public String getFontName()
    {
        return name;
    }

    
    public Type getFontType()
    {
        return type;
    }

    
    public static MHFont getDefaultFont()
    {
        if (DEFAULT_FONT == null)
            DEFAULT_FONT = MHPlatform.createFont("tahomabd.ttf");
        
        return DEFAULT_FONT.clone();
    }

    
    public String[] splitLines(String text, int lineWidthPx)
    {
        if (text == null) return new String[] {""};
        
        ArrayList<String> lines = new ArrayList<String>();
        String[] words = text.split(" ");
        String line = "";
        
        for (int i = 0; i < words.length; i++)
        {
            if (stringWidth(line) + stringWidth(words[i]) >= lineWidthPx)
            {
                lines.add(line.trim());
                line = words[i] + " ";
            }
            else
                line += words[i] + " ";
        }
        if (line.trim().length() > 0)
            lines.add(line.trim());
        
        String[] result = new String[lines.size()];
        for (int s = 0; s < result.length; s++)
            result[s] = lines.get(s);

        return result;
    }

    
    public abstract void drawString(MHGraphicsCanvas g, String text, double x, double y);
    public abstract MHVector centerOn(MHRectangle r, MHGraphicsCanvas g, String text);
    public abstract int getHeight();
    public abstract int stringWidth(String text);
    public abstract void setHeight(int fontSize);
    //public abstract void setStyle(int fontStyle);
    public abstract MHFont clone();
}
