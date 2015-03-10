package mydomain.datanucleus;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.sql.expression.NumericExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;
import org.datanucleus.store.rdbms.sql.method.AbstractSQLMethod;

/**
 * Expression handler to evaluate LENGTHOFSTRING({expression}).
 * Returns a NumericExpression.
 */
public class LengthOfStringMethod extends AbstractSQLMethod
{
    public SQLExpression getExpression(SQLExpression ignore, List args)
    {
        if (args == null || args.size() == 0)
        {
            throw new NucleusUserException("Cannot invoke LENGTHOFSTRING without an argument");
        }

        SQLExpression expr = (SQLExpression)args.get(0);
        if (expr == null)
        {
            throw new NucleusUserException("Cannot invoke LENGTHOFSTRING with null argument");
        }
        if (expr instanceof StringExpression)
        {
            ArrayList funcArgs = new ArrayList();
            funcArgs.add(expr);
            return new NumericExpression(stmt, getMappingForClass(int.class), "CHAR_LENGTH", funcArgs);
        }
        else
        {
            throw new NucleusUserException("Argument for LENGTHOFSTRING has to be a StringExpression/StringLiteral - invalid to pass " + expr);
        }
    }
}
