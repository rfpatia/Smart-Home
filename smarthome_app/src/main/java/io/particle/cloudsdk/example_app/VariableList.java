package io.particle.cloudsdk.example_app;

import java.util.ArrayList;

/**
 * Created by Reyhan on 9/24/2016.
 */

public class VariableList
{
    private ArrayList<Integer> intvariable;
    private ArrayList<Double> doublevariable;

    VariableList()
    {
        intvariable = new ArrayList<>();
        doublevariable = new ArrayList<>();
    }

    public int getIntvariable(int index)
    {
        if(!intvariable.isEmpty())
            return intvariable.get(index);
        else
            return -1;
    }

    public double getDoublevariable(int index)
    {
        if(!doublevariable.isEmpty())
            return doublevariable.get(index);
        else
            return -1;
    }

    public void addInteger(int variable)
    {
        try
        {
            intvariable.add(variable);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }

    public void addDouble(double variable)
    {
        try
        {
            doublevariable.add(variable);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isEmpty()
    {
        if(intvariable.isEmpty() && doublevariable.isEmpty())
            return true;
        else
            return false;
    }
}
