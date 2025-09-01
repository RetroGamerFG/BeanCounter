//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// FontPresets - a static class that formats text and JavaFX inputs to pre-defined variables such as font and color.

package UI;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class FontPresets
{
    static String globalFont = "Helvetica";
    static String secondaryFont = "Arial";

    static Color primaryColor = Color.GREEN;
    static String primaryColorStr = "green";

    static Color secondaryColor = Color.RED;
    static String secondaryColorStr = "red";
    
    static int fontSizeTitle = 24;
    static int fontSizeHeader = 18;
    static int fontSizeContent = 12;

    static int thickness = 1;

    public static void setFormatting(TextFlow passedTextFlow)
    {
        passedTextFlow.setTextAlignment(TextAlignment.LEFT);
        passedTextFlow.setLineSpacing(4.0f);
    }

    public static void setTitle(Text passed)
    {
        passed.setFont(Font.font(globalFont, FontWeight.BOLD, fontSizeTitle));
        passed.setFill(primaryColor);
    }

    public static void setTitle(Label passed)
    {
        passed.setFont(Font.font(globalFont, fontSizeTitle));
        passed.setStyle(
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + primaryColorStr + ";"
        );

        //"-fx-stroke: " + primaryColorStr + ";"
        //"-fx-stroke-width: " + thickness + ";"
    }
    
    public static void setHeader(Text passed)
    {
        passed.setFont(Font.font(globalFont, FontWeight.BOLD, fontSizeHeader));
    }

    public static void setHeader(Label passed)
    {
        passed.setFont(Font.font(globalFont, FontWeight.BOLD, fontSizeHeader));
    }

    public static void setContent(Text passed)
    {
        passed.setFont(Font.font(globalFont, fontSizeContent));
    }

    public static void setContent(Label passed)
    {
        passed.setFont(Font.font(globalFont, fontSizeContent));
    }

    public static void setTextField(TextField passedInput)
    {
        passedInput.setStyle(
            "-fx-control-inner-background: white;" +
            "-fx-text-fill: black;"
        );
    }

    public static void setComboBox(ComboBox passedInput)
    {
        passedInput.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: black;" +
            "-fx-border-color: #bfc2c7;" +
            "-fx-border-width: 1;"
        );
    }

//
// Incorrect Fields (During Validation)
//

    public static void setIncorrectTextField(TextField passedInput)
    {
        passedInput.setStyle(
            "-fx-control-inner-background: lightcoral;" +
            "-fx-text-fill: red;"
        );
    }

    public static void setIncorrectComboBox(ComboBox passedInput)
    {
        passedInput.setStyle(
            "-fx-background-color: lightcoral;"
        );
    }
}
