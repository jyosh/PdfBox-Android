package org.apache.pdfbox.pdmodel.interactive.form;

import java.io.IOException;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;

/**
 * A check box toggles between two states, on and off.
 *
 * @author Ben Litchfield
 * @author sug
 */
public final class PDCheckbox extends PDButton
{

    private COSName value;

    /**
     * Constructor.
     * 
     * @param theAcroForm The form that this field is part of.
     * @param field the PDF object to represent as a field.
     * @param parentNode the parent node of the node to be created
     */
    public PDCheckbox( PDAcroForm theAcroForm, COSDictionary field, PDFieldTreeNode parentNode)
    {
        super( theAcroForm, field, parentNode);
        COSDictionary ap = (COSDictionary) field.getDictionaryObject(COSName.AP);
        if( ap != null )
        {
            COSBase n = ap.getDictionaryObject(COSName.N);

            if( n instanceof COSDictionary )
            {
                for( COSName name : ((COSDictionary)n).keySet() )
                {
                    if( !name.equals( COSName.OFF ))
                    {
                        value = name;
                    }
                }

            }
        }
        else
        {
            value = (COSName)getDictionary().getDictionaryObject( COSName.V);
        }
    }

    /**
     * This will tell if this radio button is currently checked or not.
     *
     * @return true If the radio button is checked.
     */
    public boolean isChecked()
    {
        boolean retval = false;
        String onValue = getOnValue();
        COSName radioValue = (COSName)getDictionary().getDictionaryObject( COSName.AS );
        if( radioValue != null && value != null && radioValue.getName().equals( onValue ) )
        {
            retval = true;
        }

        return retval;
    }

    /**
     * Checks the radiobutton.
     */
    public void check()
    {
        getDictionary().setItem(COSName.AS, value);
    }

    /**
     * Unchecks the radiobutton.
     */
    public void unCheck()
    {
        getDictionary().setItem(COSName.AS, COSName.OFF);
    }

    /**
     * This will get the value of the radio button.
     *
     * @return The value of the radio button.
     */
    public String getOffValue()
    {
        return COSName.OFF.getName();
    }

    /**
     * This will get the value of the radio button.
     *
     * @return The value of the radio button.
     */
    public String getOnValue()
    {
        String retval = null;
        COSDictionary ap = (COSDictionary) getDictionary().getDictionaryObject(COSName.AP);
        COSBase n = ap.getDictionaryObject(COSName.N);

        //N can be a COSDictionary or a COSStream
        if( n instanceof COSDictionary )
        {
            for( COSName key :((COSDictionary)n).keySet() )
            {
                if( !key.equals( COSName.OFF) )
                {
                    retval = key.getName();
                }
            }
        }
        return retval;
    }
    
    @Override
    public COSName getDefaultValue() throws IOException
    {
    	COSBase attribute = getInheritableAttribute(getDictionary(), COSName.V);
    	
    	if (attribute instanceof COSName)
    	{
    		return (COSName) attribute;
    	}
    	else
    	{
    		throw new IOException("Expected a COSName entry but got " + attribute.getClass().getName());
    	}
    }
    
    /**
     * Set the fields default value.
     * 
     * The field value holds a name object which is corresponding to the 
     * appearance state representing the corresponding appearance 
     * from the appearance directory.
     * 
     * The default value is used to represent the initial state of the
     * checkbox or to revert when resetting the form.
     * 
     * @param defaultValue the COSName object to set the field value.
     */
    public void setDefaultValue(COSName defaultValue)
    {
    	if (defaultValue == null)
    	{
    		getDictionary().removeItem(COSName.DV);
    	}
    	else
    	{
    		getDictionary().setItem(COSName.DV, defaultValue);
    	}
    }

    @Override
    public COSName getValue()
    {
        return getDictionary().getCOSName( COSName.V );
    }

    /**
     * Set the field value.
     * 
     * The field value holds a name object which is corresponding to the 
     * appearance state representing the corresponding appearance 
     * from the appearance directory.
     * 
     * The default value is Off.
     * 
     * @param value the COSName object to set the field value.
     */
    public void setValue(COSName value)
    {
        if (value == null)
        {
            getDictionary().removeItem(COSName.V);
            getDictionary().setItem( COSName.AS, COSName.OFF );
        }
        else
        {
        	getDictionary().setItem(COSName.V, value);
        	getDictionary().setItem( COSName.AS, value);
        }
    }
    
}
