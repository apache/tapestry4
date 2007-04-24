package org.apache.tapestry.timetracker.component;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

/**
 *
 */
public abstract class Locale extends AbstractComponent {

    @Parameter(required = true)
    public abstract boolean isSelected();

    public void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        boolean selected = isSelected();

        renderRow(writer, "selectedRoundtop", "roundtop", "tl", selected);

        super.renderBody(writer, cycle);

        renderRow(writer, "selectedRoundbottom", "roundbottom", "bl", selected);
    }

    void renderRow(IMarkupWriter writer, String selectedCssClass, String cssClass, String anchor, boolean selected)
    {
         writer.begin("div");
        writer.attribute("class", selected ? selectedCssClass :cssClass);

        writer.beginEmpty("img");
        writer.attribute("src", getRoundedUrl(anchor, selected));
        writer.attribute("width", "8");
        writer.attribute("height", "8");
        writer.attribute("class", "corner");
        writer.attribute("style", "display:none");

        writer.end("div");
    }

    String getRoundedUrl(String anchor, boolean selected)
    {
        return "rounded?c=" +
               (selected ? "efefef" : "2A78B0")
               + "&bc=white&w=8&h=8&a=" + anchor;
    }
}
