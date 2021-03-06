package org.apache.pdfbox.pdmodel.interactive.form;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;

/**
 * A text field is a box or space for text fill-in data typically entered from a keyboard.
 * The text may be restricted to a single line or may be permitted to span multiple lines
 *
 * @author sug
 */
public final class PDTextField extends PDVariableText
{
	/**
	 * @see PDFieldTreeNode#PDFieldTreeNode(PDAcroForm)
	 *
	 * @param theAcroForm The acroform.
	 */
	public PDTextField(PDAcroForm theAcroForm)
	{
		super( theAcroForm );
	}

	/**
	 * Constructor.
	 * 
	 * @param theAcroForm The form that this field is part of.
	 * @param field the PDF object to represent as a field.
	 * @param parentNode the parent node of the node to be created
	 */
	public PDTextField(PDAcroForm theAcroForm, COSDictionary field, PDFieldTreeNode parentNode)
	{
		super( theAcroForm, field, parentNode);
	}

	/**
	 * Returns the maximum number of characters of the text field.
	 * 
	 * @return the maximum number of characters, returns -1 if the value isn't present
	 */
	public int getMaxLen()
	{
		return getDictionary().getInt(COSName.MAX_LEN);
	}

	/**
	 * Sets the maximum number of characters of the text field.
	 * 
	 * @param maxLen the maximum number of characters
	 */
	public void setMaxLen(int maxLen)
	{
		getDictionary().setInt(COSName.MAX_LEN, maxLen);
	}

	/**
	 * setValue sets the default value for the field.
	 *
	 * @param value the default value
	 *
	 */
	public void setDefaultValue(String value)
	{
		if (value != null)
		{
			if (value instanceof String)
			{
				String stringValue = (String)value;
				COSString fieldValue = new COSString(stringValue);
				setInheritableAttribute(getDictionary(), COSName.DV, fieldValue);
			}
			// TODO stream instead of string
		}
		else
		{
			removeInheritableAttribute(getDictionary(),COSName.DV);
		}
	}

	/**
	 * setValue sets the entry "V" to the given value.
	 * 
	 * @param value the value
	 * 
	 */
	public void setValue(Object value)
	{
		if (value != null)
		{
			if (value instanceof String)
			{
				String stringValue = (String)value;
				COSString fieldValue = new COSString(stringValue);
				setInheritableAttribute(getDictionary(), COSName.V, fieldValue);
			}
			// TODO stream instead of string
		}  
		else
		{
			removeInheritableAttribute(getDictionary(),COSName.DV);
		}
		updateFieldAppearances();
	}

	/**
	 * getValue gets the value of the "V" entry.
	 * 
	 * @return The value of this entry.
	 * 
	 */
	@Override
	public Object getValue()
	{
		return getInheritableAttribute(getDictionary(), COSName.V);
	}
}