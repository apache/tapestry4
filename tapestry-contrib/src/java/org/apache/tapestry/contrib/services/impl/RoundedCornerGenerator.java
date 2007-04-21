package org.apache.tapestry.contrib.services.impl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for bulk of java2d manipulation work when used in the {@link RoundedCornerService}. 
 */
public class RoundedCornerGenerator {

    // css2 color spec - http://www.w3.org/TR/REC-CSS2/syndata.html#color-units
    private static final Map _cssSpecMap = new HashMap();

    static {
        _cssSpecMap.put("aqua", new Color(0,255,255));
        _cssSpecMap.put("black", Color.black);
        _cssSpecMap.put("blue", Color.blue);
        _cssSpecMap.put("fuchsia", new Color(255,0,255));
        _cssSpecMap.put("gray", Color.gray);
        _cssSpecMap.put("green", Color.green);
        _cssSpecMap.put("lime", new Color(0,255,0));
        _cssSpecMap.put("maroon", new Color(128,0,0));
        _cssSpecMap.put("navy", new Color(0,0,128));
        _cssSpecMap.put("olive", new Color(128,128,0));
        _cssSpecMap.put("purple", new Color(128,0,128));
        _cssSpecMap.put("red", Color.red);
        _cssSpecMap.put("silver", new Color(192,192,192));
        _cssSpecMap.put("teal", new Color(0,128,128));
        _cssSpecMap.put("white", Color.white);
        _cssSpecMap.put("yellow", Color.yellow);
    }

    public static final String TOP_LEFT = "tl";
    public static final String TOP_RIGHT = "tr";
    public static final String BOTTOM_LEFT = "bl";
    public static final String BOTTOM_RIGHT = "br";

    // holds pre-built binaries for previously generated colors
    private Map _imageCache = new HashMap();

    public byte[] buildCorner(String color, String backgroundColor, int width, int height, String angle)
    throws Exception
    {
        String hashKey = color + backgroundColor + width + height + angle;

        byte[] ret = (byte[])_imageCache.get(hashKey);
        if (ret != null)
            return ret;
        
        Color arcColor = decodeColor(color);
        Color bgColor = backgroundColor == null ? null : decodeColor(backgroundColor);
        float startAngle = getStartAngle(angle);

        BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) img.createGraphics();

        Arc2D.Float fillArea = new Arc2D.Float(0f, 0f, (float)width, (float)height, startAngle, 90, Arc2D.PIE);
        
        // fill background color

        if (bgColor != null) {
            
            g2.setClip(fillArea.getBounds2D());
            g2.setColor(bgColor);
            g2.fillRect(0, 0, width, height);
        }

        // draw arc
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(arcColor);
        g2.setComposite(AlphaComposite.Src);
        g2.fill(fillArea);
        
        g2.dispose();

        ByteArrayOutputStream bo = null;

        try {
            ImageIO.setUseCache(false);

            bo = new ByteArrayOutputStream();

            ImageIO.write(img, "gif", bo);

            ret = bo.toByteArray();

            _imageCache.put(hashKey, ret);

            return ret;

        } finally {
            if (bo != null) {
                bo.close();
            }
        }
    }

    /**
     * Matches the incoming string against one of the constants defined; tl, tr, bl, br.
     *
     * @param code The code for the angle of the arc to generate, if no match is found the default is
     *          {@link #TOP_RIGHT} - or 0 degrees.
     * @return The pre-defined 90 degree angle starting degree point.
     */
    public float getStartAngle(String code)
    {
        if (TOP_LEFT.equalsIgnoreCase(code))
            return 90f;
        if (TOP_RIGHT.equalsIgnoreCase(code))
            return 0f;
        if (BOTTOM_LEFT.equalsIgnoreCase(code))
            return 180f;
        if (BOTTOM_RIGHT.equalsIgnoreCase(code))
            return 270f;

        return 0f;
    }

    /**
     * Decodes the specified input color string into a compatible awt color object. Valid inputs
     * are any in the css2 color spec or hex strings.
     * 
     * @param color The color to match.
     * @return The decoded color object, may be black if decoding fails.
     */
    public Color decodeColor(String color)
    {
        Color specColor = (Color) _cssSpecMap.get(color);
        if (specColor != null)
            return specColor;

        String hexColor = color.startsWith("0x") ? color : "0x" + color;
        
        return Color.decode(hexColor);
    }
}
