package com.klm.KLMPortal.data;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;


public class PropertyFile {

    private Properties propertyFile;
    private String propertyFileName;


    /**
     * Create PropertyFile instance and initialize it.
     *
     * @param is the input stream to the property file
     */
    public PropertyFile(final InputStream is)
    {
        initProperties(is);
    }


    /**
     * Get the property value for the given key.
     *
     * @param key the key which value is returned
     * @return the value for the given key
     */
    public String getValue(final String key)
    {
        final String value;

        if (propertyFile.containsKey(key))
        {
            value = propertyFile.getProperty(key);
        }
        else
        {
            value = "";
        }
        return value;

    }


    /**
     * Initialize the property file.
     *
     * @param is the input stream to the property file
     * @return the property file properties set
     */
    private Properties initProperties(final InputStream is)
    {
        propertyFile = new Properties();

        try
        {
            propertyFile.load(is);
        }
        catch (IOException e)
        {
            System.out.println("The properties file " + propertyFileName + " is not found");
        }
        return propertyFile;
    }

    public Properties getProperties()
    {
        return propertyFile;
    }
}
