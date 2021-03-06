package org.apache.pdfbox.pdmodel.graphics.pattern;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;

/**
 * A tiling pattern dictionary.
 * @author Andreas Lehmk�hler
 */
public class PDTilingPattern extends PDAbstractPattern implements PDContentStream
{
	/** paint type 1 = colored tiling pattern. */
	public static final int PAINT_COLORED = 1;

	/** paint type 2 = uncolored tiling pattern. */
	public static final int PAINT_UNCOLORED = 2;

	/** tiling type 1 = constant spacing.*/
	public static final int TILING_CONSTANT_SPACING = 1;

	/**  tiling type 2 = no distortion. */
	public static final int TILING_NO_DISTORTION = 2;

	/** tiling type 3 = constant spacing and faster tiling. */
	public static final int TILING_CONSTANT_SPACING_FASTER_TILING = 3;

	/**
	 * Creates a new tiling pattern.
	 */
	public PDTilingPattern()
	{
		super();
		getCOSDictionary().setInt(COSName.PATTERN_TYPE, PDAbstractPattern.TYPE_TILING_PATTERN);
	}

	/**
	 * Creates a new tiling pattern from the given COS dictionary.
	 * @param resourceDictionary The COSDictionary for this pattern resource.
	 */
	public PDTilingPattern(COSDictionary resourceDictionary)
	{
		super(resourceDictionary);
	}

	@Override
	public int getPatternType()
	{
		return PDAbstractPattern.TYPE_TILING_PATTERN;
	}

	/**
	 * This will set the length of the content stream.
	 * @param length The new stream length.
	 */
	@Override
	public void setLength(int length)
	{
		getCOSDictionary().setInt(COSName.LENGTH, length);
	}

	/**
	 * This will return the length of the content stream.
	 * @return The length of the content stream
	 */
	@Override
	public int getLength()
	{
		return getCOSDictionary().getInt( COSName.LENGTH, 0 );
	}

	/**
	 * This will set the paint type.
	 * @param paintType The new paint type.
	 */
	@Override
	public void setPaintType(int paintType)
	{
		getCOSDictionary().setInt(COSName.PAINT_TYPE, paintType);
	}

	/**
	 * This will return the paint type.
	 * @return The paint type
	 */
	public int getPaintType()
	{
		return getCOSDictionary().getInt( COSName.PAINT_TYPE, 0 );
	}

	/**
	 * This will set the tiling type.
	 * @param tilingType The new tiling type.
	 */
	public void setTilingType(int tilingType)
	{
		getCOSDictionary().setInt(COSName.TILING_TYPE, tilingType);
	}

	/**
	 * This will return the tiling type.
	 * @return The tiling type
	 */
	public int getTilingType()
	{
		return getCOSDictionary().getInt( COSName.TILING_TYPE, 0 );
	}

	/**
	 * This will set the XStep value.
	 * @param xStep The new XStep value.
	 */
	public void setXStep(float xStep)
	{
		getCOSDictionary().setFloat(COSName.X_STEP, xStep);
	}

	/**
	 * This will return the XStep value.
	 * @return The XStep value
	 */
	public float getXStep()
	{
		return getCOSDictionary().getFloat( COSName.X_STEP, 0 );
	}

	/**
	 * This will set the YStep value.
	 * @param yStep The new YStep value.
	 */
	public void setYStep(float yStep)
	{
		getCOSDictionary().setFloat(COSName.Y_STEP, yStep);
	}

	/**
	 * This will return the YStep value.
	 * @return The YStep value
	 */
	public float getYStep()
	{
		return getCOSDictionary().getFloat( COSName.Y_STEP, 0 );
	}

	@Override
	public COSStream getContentStream() {
		return (COSStream)getCOSObject();
	}

	/**
	 * This will get the resources for this pattern.
	 * This will return null if no resources are available at this level.
	 * @return The resources for this pattern.
	 */
	public PDResources getResources()
	{
		PDResources retval = null;
		COSDictionary resources = (COSDictionary)getCOSDictionary()
				.getDictionaryObject( COSName.RESOURCES );
		if( resources != null )
		{
			retval = new PDResources( resources );
		}
		return retval;
	}

	/**
	 * This will set the resources for this pattern.
	 * @param resources The new resources for this pattern.
	 */
	public void setResources( PDResources resources )
	{
		if (resources != null)
		{
			getCOSDictionary().setItem( COSName.RESOURCES, resources );
		}
		else
		{
			getCOSDictionary().removeItem( COSName.RESOURCES );
		}
	}

	/**
	 * An array of four numbers in the form coordinate system (see
	 * below), giving the coordinates of the left, bottom, right, and top edges,
	 * respectively, of the pattern's bounding box.
	 *
	 * @return The BBox of the pattern.
	 */
	@Override
	public PDRectangle getBBox()
	{
		PDRectangle retval = null;
		COSArray array = (COSArray)getCOSDictionary().getDictionaryObject( COSName.BBOX );
		if( array != null )
		{
			retval = new PDRectangle( array );
		}
		return retval;
	}

	/**
	 * This will set the BBox (bounding box) for this Pattern.
	 * @param bbox The new BBox for this Pattern.
	 */
	public void setBBox(PDRectangle bbox)
	{
		if( bbox == null )
		{
			getCOSDictionary().removeItem( COSName.BBOX );
		}
		else
		{
			getCOSDictionary().setItem( COSName.BBOX, bbox.getCOSArray() );
		}
	}

	/**
	 * This will get the optional Matrix of a Pattern. It maps the form space to user space.
	 * @return the form matrix
	 */
	@Override
	public Matrix getMatrix()
	{
		Matrix matrix = null;
		COSArray array = (COSArray)getCOSDictionary().getDictionaryObject(COSName.MATRIX);
		if (array != null)
		{
			//TODO: does this map to the right values?
			matrix = new Matrix();
			matrix.setValue(0, 0, ((COSNumber) array.get(0)).floatValue());
			matrix.setValue(0, 1, ((COSNumber) array.get(1)).floatValue());
			matrix.setValue(1, 0, ((COSNumber) array.get(2)).floatValue());
			matrix.setValue(1, 1, ((COSNumber) array.get(3)).floatValue());
			matrix.setValue(2, 0, ((COSNumber) array.get(4)).floatValue());
			matrix.setValue(2, 1, ((COSNumber) array.get(5)).floatValue());

			// repair mechanism for invalid matrices, this is based on pure guesswork based on the
			// PoolCompPDFA.pdf from PDFBOX-1265 which renders fine in Acrobat despite having a
			// scaling factor of zero.
			if (matrix.getScaleX() == 0)
			{
				matrix.setValue(0, 0, ((COSNumber) array.get(1)).floatValue()); // scale x -> skew x
				matrix.setValue(1, 0, 0); // skew x -> 0
			}
			if (matrix.getScaleY() == 0)
			{
				matrix.setValue(1, 1, ((COSNumber) array.get(2)).floatValue()); // scale y -> skew y
				matrix.setValue(0, 1, 0); // skew y -> 0
			}
			if (matrix.getScaleX() == 0)
			{
				matrix.setValue(0, 0, 1);
			}
			if (matrix.getScaleY() == 0)
			{
				matrix.setValue(1, 1, 1);
			}
		} else {
			// default value is the identity matrix
			matrix = new Matrix();
		}
		return matrix;
	}

	/**
	 * Sets the optional Matrix entry for the Pattern.
	 * @param transform the transformation matrix
	 */
	public void setMatrix(android.graphics.Matrix transform)
	{
		COSArray matrix = new COSArray();
		float[] values = new float[9];
		transform.getValues(values);
		for (float v : values)
		{
			matrix.add(new COSFloat((float)v));
		}
		getCOSDictionary().setItem(COSName.MATRIX, matrix);
	}
}
