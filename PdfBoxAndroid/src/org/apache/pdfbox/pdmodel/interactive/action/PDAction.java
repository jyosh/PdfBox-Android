package org.apache.pdfbox.pdmodel.interactive.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;

import org.apache.pdfbox.pdmodel.common.COSArrayList;
import org.apache.pdfbox.pdmodel.common.PDDestinationOrAction;

/**
 * This represents an action that can be executed in a PDF document.
 *
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @author Panagiotis Toumasis (ptoumasis@mail.gr)
 * @version $Revision: 1.3 $
 */
public abstract class PDAction implements PDDestinationOrAction
{
    /**
     * The type of PDF object.
     */
    public static final String TYPE = "Action";

    /**
     * The action dictionary.
     */
    protected COSDictionary action;

    /**
     * Default constructor.
     */
    public PDAction()
    {
        action = new COSDictionary();
        setType( TYPE );
    }

    /**
     * Constructor.
     *
     * @param a The action dictionary.
     */
    public PDAction( COSDictionary a )
    {
        action = a;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSBase getCOSObject()
    {
        return action;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSDictionary getCOSDictionary()
    {
        return action;
    }

    /**
     * This will get the type of PDF object that the actions dictionary describes.
     * If present must be Action for an action dictionary.
     *
     * @return The Type of PDF object.
     */
    public String getType()
    {
       return action.getNameAsString( "Type" );
    }

    /**
     * This will set the type of PDF object that the actions dictionary describes.
     * If present must be Action for an action dictionary.
     *
     * @param type The new Type for the PDF object.
     */
    public void setType( String type )
    {
       action.setName( "Type", type );
    }

    /**
     * This will get the type of action that the actions dictionary describes.
     * If present, must be Action for an action dictionary.
     *
     * @return The S entry of actions dictionary.
     */
    public String getSubType()
    {
       return action.getNameAsString( "S" );
    }

    /**
     * This will set the type of action that the actions dictionary describes.
     * If present, must be Action for an action dictionary.
     *
     * @param s The new type of action.
     */
    public void setSubType( String s )
    {
       action.setName( "S", s );
    }

    /**
     * This will get the next action, or sequence of actions, to be performed after this one.
     * The value is either a single action dictionary or an array of action dictionaries
     * to be performed in order.
     *
     * @return The Next action or sequence of actions.
     */
    public List getNext()
    {
        List retval = null;
        COSBase next = action.getDictionaryObject( "Next" );
        if( next instanceof COSDictionary )
        {
            PDAction pdAction = PDActionFactory.createAction( (COSDictionary) next );
            retval = new COSArrayList(pdAction, next, action, COSName.getPDFName( "Next" ));
        }
        else if( next instanceof COSArray )
        {
            COSArray array = (COSArray)next;
            List actions = new ArrayList();
            for( int i=0; i<array.size(); i++ )
            {
                actions.add( PDActionFactory.createAction( (COSDictionary) array.getObject( i )));
            }
            retval = new COSArrayList( actions, array );
        }

        return retval;
    }

    /**
     * This will set the next action, or sequence of actions, to be performed after this one.
     * The value is either a single action dictionary or an array of action dictionaries
     * to be performed in order.
     *
     * @param next The Next action or sequence of actions.
     */
    public void setNext( List next )
    {
        action.setItem( "Next", COSArrayList.converterToCOSArray( next ) );
    }
}
