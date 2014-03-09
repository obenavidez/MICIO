package com.panzyma.nm.auxiliar;

public class Holder<T>
{
    private T value;

    public Holder(T _value)
    {
        value = _value;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }
}