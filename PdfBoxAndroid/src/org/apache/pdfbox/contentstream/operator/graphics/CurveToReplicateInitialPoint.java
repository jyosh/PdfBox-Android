package org.apache.pdfbox.contentstream.operator.graphics;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import android.graphics.PointF;

/**
 * v Append curved segment to path with the initial point replicated.
 *
 * @author Ben Litchfield
 */
public class CurveToReplicateInitialPoint extends GraphicsOperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> operands) throws IOException
    {
        COSNumber x2 = (COSNumber)operands.get(0 );
        COSNumber y2 = (COSNumber)operands.get(1);
        COSNumber x3 = (COSNumber)operands.get(2);
        COSNumber y3 = (COSNumber)operands.get(3);

        PointF currentPoint = context.getCurrentPoint();

        PointF point2 = context.transformedPoint(x2.doubleValue(), y2.doubleValue());
        PointF point3 = context.transformedPoint(x3.doubleValue(), y3.doubleValue());

        context.curveTo((float) currentPoint.x, (float) currentPoint.y,
                        (float) point2.x, (float) point2.y,
                        (float) point3.x, (float) point3.y);
    }

    @Override
    public String getName()
    {
        return "v";
    }
}
