package com.example.asus.freingo.models;

/**
 * Created by ASUS on 27/03/2019.
 */

public class Periods {
    private Close close;

    private Open open;

    public Close getClose ()
    {
        return close;
    }

    public void setClose (Close close)
    {
        this.close = close;
    }

    public Open getOpen ()
    {
        return open;
    }

    public void setOpen (Open open)
    {
        this.open = open;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [close = "+close+", open = "+open+"]";
    }
}
