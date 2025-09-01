//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// SceneTabs - used for the first-time setup. Allows setting the navigation of a VBox array, and a progress bar for each step.

package UI;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SceneTabs
{
    private int currentTab;
    private int maxTabs;
    
    public SceneTabs(int maxTabs)
    {
        currentTab = 0;
        this.maxTabs = maxTabs;
    }

    public void incrementScene()
    {
        if(currentTab < maxTabs)
        {
            currentTab++;
        }
    }

    public void decrementScene()
    {
        if(currentTab > 0)
        {
            currentTab--;
        }
    }

    public int getCurrentTab()
    {
        return currentTab;
    }

    public int getMaxTabs()
    {
        return maxTabs;
    }

    public void setPreviousButtonActive(Button passedButton)
    {
        if(currentTab > 0)
        {
            passedButton.setDisable(false);
        }
        else
        {
            passedButton.setDisable(true);
        }
    }

    public void setNextButtonText(Button passedButton)
    {
        //change "next" to "finish" upon reaching last tab
        if(currentTab == maxTabs - 1)
        {
            passedButton.setText("Finish");
        }
        else
        {
            passedButton.setText("Next");
        }
    }

    public VBox setActiveTab(VBox[] contentArray)
    {
        for(int c = 0; c < maxTabs; c++)
        {
            if(currentTab == c)
            {
                return contentArray[c];
            }
        }

        return contentArray[0];
    }

    public HBox createProgressBar()
    {
        HBox output = new HBox();
        
        for(int c = 0; c < maxTabs; c++)
        {
            Circle addCircle = new Circle(8);
            setCircleStyle(addCircle, c);
            output.getChildren().add(addCircle);
        }

        return output;
    }

    public void updateProgressBar(HBox passedBar)
    {
        for(int c = 0; c < maxTabs; c++)
        {
            Node node = passedBar.getChildren().get(c);

            if(node instanceof Circle circle)
            {
                setCircleStyle(circle, c);
            }
        }
    }

    private void setCircleStyle(Circle passedCircle, int index)
    {
        if(index <= currentTab)
        {
            passedCircle.setFill(Color.GREEN);
            passedCircle.setStroke(Color.TRANSPARENT);
            passedCircle.setStrokeWidth(3);
        }
        else
        {
            passedCircle.setFill(Color.TRANSPARENT);
            passedCircle.setStroke(Color.LIGHTGRAY);
            passedCircle.setStrokeWidth(3);
        }
    }
}
