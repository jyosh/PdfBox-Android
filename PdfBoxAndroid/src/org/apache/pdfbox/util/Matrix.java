package org.apache.pdfbox.util;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSNumber;

import android.graphics.PointF;

/**
 * This class will be used for matrix manipulation.
 *
 * @author Ben Litchfield
 */
public final class Matrix implements Cloneable
{
	static final float[] DEFAULT_SINGLE =
		{
		1,0,0,  //  a  b  0
		0,1,0,  //  c  d  0
		0,0,1   //  tx ty 1
		};

	private float[] single;

	/**
	 * Constructor.
	 */
	public Matrix()
	{
		single = new float[DEFAULT_SINGLE.length];
		reset();
	}

	/**
	 * Constructor.
	 */
	public Matrix(COSArray array)
	{
		single = new float[DEFAULT_SINGLE.length];
		single[0] = ((COSNumber)array.get(0)).floatValue();
		single[1] = ((COSNumber)array.get(1)).floatValue();
		single[3] = ((COSNumber)array.get(2)).floatValue();
		single[4] = ((COSNumber)array.get(3)).floatValue();
		single[6] = ((COSNumber)array.get(4)).floatValue();
		single[7] = ((COSNumber)array.get(5)).floatValue();
	}

	/**
	 * Constructor.
	 */
	public Matrix(float a, float b, float c, float d, float e, float f)
	{
		single = new float[DEFAULT_SINGLE.length];
		single[0] = a;
		single[1] = b;
		single[3] = c;
		single[4] = d;
		single[6] = e;
		single[7] = f;
		single[8] = 1;
	}

	/**
	 * This method resets the numbers in this Matrix to the original values, which are
	 * the values that a newly constructed Matrix would have.
	 */
	public void reset()
	{
		System.arraycopy(DEFAULT_SINGLE, 0, single, 0, DEFAULT_SINGLE.length);
	}

	/**
	 * Create an affine transform from this matrix's values.
	 *
	 * @return An affine transform with this matrix's values.
	 */
	public android.graphics.Matrix createAffineTransform()
	{
		android.graphics.Matrix retval = new android.graphics.Matrix();
		//        		(
		//            single[0], single[1],
		//            single[3], single[4],
		//            single[6], single[7] );
		retval.setValues(single);
		return retval;
	}

	/**
	 * Set the values of the matrix from the AffineTransform.
	 *
	 * @param af The transform to get the values from.
	 * @deprecated This method is due to be removed, please contact us if you make use of it.
	 */
	@Deprecated
	public void setFromAffineTransform( android.graphics.Matrix af )
	{
		float[] values = new float[9];
		af.getValues(values);
		single[0] = (float)values[android.graphics.Matrix.MSCALE_X];
		single[1] = (float)values[android.graphics.Matrix.MSKEW_Y];
		single[3] = (float)values[android.graphics.Matrix.MSKEW_X];
		single[4] = (float)values[android.graphics.Matrix.MSCALE_Y];
		single[6] = (float)values[android.graphics.Matrix.MTRANS_X];
		single[7] = (float)values[android.graphics.Matrix.MTRANS_Y];
	}

	/**
	 * This will get a matrix value at some point.
	 *
	 * @param row The row to get the value from.
	 * @param column The column to get the value from.
	 *
	 * @return The value at the row/column position.
	 */
	public float getValue( int row, int column )
	{
		return single[row*3+column];
	}

	/**
	 * This will set a value at a position.
	 *
	 * @param row The row to set the value at.
	 * @param column the column to set the value at.
	 * @param value The value to set at the position.
	 */
	public void setValue( int row, int column, float value )
	{
		single[row*3+column] = value;
	}

	/**
	 * Return a single dimension array of all values in the matrix.
	 *
	 * @return The values ot this matrix.
	 */
	public float[][] getValues()
	{
		float[][] retval = new float[3][3];
		retval[0][0] = single[0];
		retval[0][1] = single[1];
		retval[0][2] = single[2];
		retval[1][0] = single[3];
		retval[1][1] = single[4];
		retval[1][2] = single[5];
		retval[2][0] = single[6];
		retval[2][1] = single[7];
		retval[2][2] = single[8];
		return retval;
	}

	/**
	 * Return a single dimension array of all values in the matrix.
	 *
	 * @return The values ot this matrix.
	 * @deprecated Use {@link float[][] #getValues} instead.
	 */
	@Deprecated
	public double[][] getValuesAsDouble()
	{
		double[][] retval = new double[3][3];
		retval[0][0] = single[0];
		retval[0][1] = single[1];
		retval[0][2] = single[2];
		retval[1][0] = single[3];
		retval[1][1] = single[4];
		retval[1][2] = single[5];
		retval[2][0] = single[6];
		retval[2][1] = single[7];
		retval[2][2] = single[8];
		return retval;
	}

	/**
	 * Concatenates (premultiplies) the given matrix to this matrix.
	 *
	 * @param matrix The matrix to concatenate.
	 */
	public void concatenate(Matrix matrix)
	{
		matrix.multiply(this, this);
	}

	/**
	 * Translates this matrix by the given vector.
	 *
	 * @param vector 2D vector
	 */
	public void translate(Vector vector)
	{
		Matrix m = Matrix.getTranslatingInstance(vector.getX(), vector.getY());
		concatenate(m);
	}

	/**
	 * This will take the current matrix and multipy it with a matrix that is passed in.
	 *
	 * @param b The matrix to multiply by.
	 *
	 * @return The result of the two multiplied matrices.
	 */
	public Matrix multiply( Matrix b )
	{
		return this.multiply(b, new Matrix());
	}

	/**
	 * This method multiplies this Matrix with the specified other Matrix, storing the product in the specified
	 * result Matrix. By reusing Matrix instances like this, multiplication chains can be executed without having
	 * to create many temporary Matrix objects.
	 * <p/>
	 * It is allowed to have (other == this) or (result == this) or indeed (other == result) but if this is done,
	 * the backing float[] matrix values may be copied in order to ensure a correct product.
	 *
	 * @param other the second operand Matrix in the multiplication
	 * @param result the Matrix instance into which the result should be stored. If result is null, a new Matrix
	 *               instance is created.
	 * @return the product of the two matrices.
	 */
	public Matrix multiply( Matrix other, Matrix result )
	{
		if (result == null)
		{
			result = new Matrix();
		}

		if (other != null && other.single != null)
		{
			// the operands
			float[] thisOperand = this.single;
			float[] otherOperand = other.single;

			// We're multiplying 2 sets of floats together to produce a third, but we allow
			// any of these float[] instances to be the same objects.
			// There is the possibility then to overwrite one of the operands with result values
			// and therefore corrupt the result.

			// If either of these operands are the same float[] instance as the result, then
			// they need to be copied.

			if (this == result)
			{
				final float[] thisOrigVals = new float[this.single.length];
				System.arraycopy(this.single, 0, thisOrigVals, 0, this.single.length);

				thisOperand = thisOrigVals;
			}
			if (other == result)
			{
				final float[] otherOrigVals = new float[other.single.length];
				System.arraycopy(other.single, 0, otherOrigVals, 0, other.single.length);

				otherOperand = otherOrigVals;
			}

			result.single[0] = thisOperand[0] * otherOperand[0]
					+ thisOperand[1] * otherOperand[3]
							+ thisOperand[2] * otherOperand[6];
			result.single[1] = thisOperand[0] * otherOperand[1]
					+ thisOperand[1] * otherOperand[4]
							+ thisOperand[2] * otherOperand[7];
			result.single[2] = thisOperand[0] * otherOperand[2]
					+ thisOperand[1] * otherOperand[5]
							+ thisOperand[2] * otherOperand[8];
			result.single[3] = thisOperand[3] * otherOperand[0]
					+ thisOperand[4] * otherOperand[3]
							+ thisOperand[5] * otherOperand[6];
			result.single[4] = thisOperand[3] * otherOperand[1]
					+ thisOperand[4] * otherOperand[4]
							+ thisOperand[5] * otherOperand[7];
			result.single[5] = thisOperand[3] * otherOperand[2]
					+ thisOperand[4] * otherOperand[5]
							+ thisOperand[5] * otherOperand[8];
			result.single[6] = thisOperand[6] * otherOperand[0]
					+ thisOperand[7] * otherOperand[3]
							+ thisOperand[8] * otherOperand[6];
			result.single[7] = thisOperand[6] * otherOperand[1]
					+ thisOperand[7] * otherOperand[4]
							+ thisOperand[8] * otherOperand[7];
			result.single[8] = thisOperand[6] * otherOperand[2]
					+ thisOperand[7] * otherOperand[5]
							+ thisOperand[8] * otherOperand[8];
		}

		return result;
	}

	/**
	 * Transforms the given point by this matrix.
	 *
	 * @param point point to transform
	 */
	public void transform(PointF point) {
		float x = (float)point.x;
		float y = (float)point.y;
		float a = single[0];
		float b = single[1];
		float c = single[3];
		float d = single[4];
		float e = single[6];
		float f = single[7];
		point.set(x * a + y * c + e, x * b + y * d + f);
	}

	/**
	 * Transforms the given point by this matrix.
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public PointF transformPoint(double x, double y) {
		float a = single[0];
		float b = single[1];
		float c = single[3];
		float d = single[4];
		float e = single[6];
		float f = single[7];
		return new PointF((float)(x * a + y * c + e), (float)(x * b + y * d + f));
	}

	/**
	 * Transforms the given point by this matrix.
	 *
	 * @param vector @2D vector
	 */
	public Vector transform(Vector vector) {
		float a = single[0];
		float b = single[1];
		float c = single[3];
		float d = single[4];
		float e = single[6];
		float f = single[7];
		float x = vector.getX();
		float y = vector.getY();
		return new Vector(x * a + y * c + e, x * b + y * d + f);
	}

	/**
    - * This will take the current matrix and multipy it with a matrix that is passed in.
    + * Scales this matrix by the given factors.
    + *
    + * @param sx x-scale
    + * @param sy y-scale
    + */
	public void scale(float sx, float sy)
	{
		Matrix m = Matrix.getScaleInstance(sx, sy);
		concatenate(m);
	}

	/**
	 * Create a new matrix with just the scaling operators.
	 *
	 * @return A new matrix with just the scaling operators.
	 * @deprecated This method is due to be removed, please contact us if you make use of it.
	 */
	@Deprecated
	public Matrix extractScaling()
	{
		Matrix retval = new Matrix();

		retval.single[0] = this.single[0];
		retval.single[4] = this.single[4];

		return retval;
	}

	/**
	 * Convenience method to create a scaled instance.
	 *
	 * @param x The xscale operator.
	 * @param y The yscale operator.
	 * @return A new matrix with just the x/y scaling
	 */
	public static Matrix getScaleInstance( float x, float y)
	{
		Matrix retval = new Matrix();

		retval.single[0] = x;
		retval.single[4] = y;

		return retval;
	}

	/**
	 * Create a new matrix with just the translating operators.
	 *
	 * @return A new matrix with just the translating operators.
	 * @deprecated This method is due to be removed, please contact us if you make use of it.
	 */
	@Deprecated
	public Matrix extractTranslating()
	{
		Matrix retval = new Matrix();

		retval.single[6] = this.single[6];
		retval.single[7] = this.single[7];

		return retval;
	}

	/**
	 * Convenience method to create a translating instance.
	 *
	 * @param x The x translating operator.
	 * @param y The y translating operator.
	 * @return A new matrix with just the x/y translating.
	 */
	public static Matrix getTranslatingInstance( float x, float y)
	{
		Matrix retval = new Matrix();

		retval.single[6] = x;
		retval.single[7] = y;

		return retval;
	}

	/**
	 * Produces a copy of the first matrix, with the second matrix concatenated.
	 *
	 * @param a The matrix to copy.
	 * @param b The matrix to concatenate.
	 */
	public static Matrix concatenate(Matrix a, Matrix b)
	{
		Matrix copy = a.clone();
		copy.concatenate(b);
		return copy;
	}

	/**
	 * Clones this object.
	 * @return cloned matrix as an object.
	 */
	@Override
	public Matrix clone()
	{
		Matrix clone = new Matrix();
		System.arraycopy( single, 0, clone.single, 0, 9 );
		return clone;
	}

	/**
	 * Get the xscaling factor of this matrix. This is a deprecated method which actually
	 * returns the x-scaling factor multiplied by the x-shear.
	 * @return The x-scale.\
	 * @deprecated Use {@link #getScaleX} instead
	 */
	@Deprecated
	public float getXScale()
	{
		float xScale = single[0];

		/**
		 * BM: if the trm is rotated, the calculation is a little more complicated
		 *
		 * The rotation matrix multiplied with the scaling matrix is:
		 * (   x   0   0)    ( cos  sin  0)    ( x*cos x*sin   0)
		 * (   0   y   0) *  (-sin  cos  0)  = (-y*sin y*cos   0)
		 * (   0   0   1)    (   0    0  1)    (     0     0   1)
		 *
		 * So, if you want to deduce x from the matrix you take
		 * M(0,0) = x*cos and M(0,1) = x*sin and use the theorem of Pythagoras
		 *
		 * sqrt(M(0,0)^2+M(0,1)^2) =
		 * sqrt(x2*cos2+x2*sin2) =
		 * sqrt(x2*(cos2+sin2)) = <- here is the trick cos2+sin2 is one
		 * sqrt(x2) =
		 * abs(x)
		 */
		if( !(single[1]==0.0f && single[3]==0.0f) )
		{
			xScale = (float)Math.sqrt(Math.pow(single[0], 2)+
					Math.pow(single[1], 2));
		}
		return xScale;
	}

	/**
	 * Get the y scaling factor of this matrix. This is a deprecated method which actually
	 * returns the y-scaling factor multiplied by the y-shear.
	 * @return The y-scale factor.
	 * @deprecated Use {@link #getScaleY} instead
	 */
	@Deprecated
	public float getYScale()
	{
		float yScale = single[4];
		if( !(single[1]==0.0f && single[3]==0.0f) )
		{
			yScale = (float)Math.sqrt(Math.pow(single[3], 2)+
					Math.pow(single[4], 2));
		}
		return yScale;
	}

	/**
	 *  * Returns the x-scaling factor of this matrix.
	 */
	public float getScaleX()
	{
		return single[0];
	}

	/**
	 * Returns the y-shear factor of this matrix.
	 */
	public float getShearY()
	{
		return single[1];
	}

	/**
	 * Returns the x-shear factor of this matrix.
	 */
	public float getShearX()
	{
		return single[3];
	}

	/**
	 * Returns the y-scaling factor of this matrix.
	 */
	public float getScaleY()
	{
		return single[4];
	}

	/**
	 * Returns the x-translation of this matrix.
	 */
	public float getTranslateX()
	{
		return single[6];
	}

	/**
	 * Returns the y-translation of this matrix.
	 */
	public float getTranslateY()
	{
		return single[7];
	}

	/**
	 * Get the x position in the matrix. This method is deprecated as it is incorrectly named.
	 * @return The x-position.
	 * @deprecated Use {@link #getTranslateX} instead
	 */
	@Deprecated
	public float getXPosition()
	{
		return single[6];
	}

	/**
	 * Get the y position. This method is deprecated as it is incorrectly named.
	 * @return The y position.
	 * @deprecated Use {@link #getTranslateY} instead
	 */
	@Deprecated
	public float getYPosition()
	{
		return single[7];
	}

	/**
	 * Returns a COS array which represents this matrix.
	 */
	public COSArray toCOSArray()
	{
		COSArray array = new COSArray();
		array.add(new COSFloat(0));
		array.add(new COSFloat(1));
		array.add(new COSFloat(3));
		array.add(new COSFloat(4));
		array.add(new COSFloat(6));
		array.add(new COSFloat(7));
		return array;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer( "" );
		sb.append("[");
		sb.append(single[0] + ",");
		sb.append(single[1] + ",");
		sb.append(single[3] + ",");
		sb.append(single[4] + ",");
		sb.append(single[6] + ",");
		sb.append(single[7] + "]");
		return sb.toString();
	}
}
