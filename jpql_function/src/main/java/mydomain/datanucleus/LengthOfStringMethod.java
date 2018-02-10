package mydomain.datanucleus;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.NumericExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;
import org.datanucleus.store.rdbms.sql.method.SQLMethod;

/**
 * Expression handler to evaluate LENGTHOFSTRING({expression}).
 * Returns a NumericExpression.
 */
public class LengthOfStringMethod implements SQLMethod
{
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression expr, List<SQLExpression> args)
    {
        if (args == null || args.size() == 0)
        {
            throw new NucleusUserException("Cannot invoke LENGTHOFSTRING without an argument");
        }

        SQLExpression arg0Expr = (SQLExpression)args.get(0);
        if (arg0Expr == null)
        {
            throw new NucleusUserException("Cannot invoke LENGTHOFSTRING with null argument");
        }
        if (arg0Expr instanceof StringExpression)
        {
            ArrayList funcArgs = new ArrayList();
            funcArgs.add(arg0Expr);
            return new NumericExpression(stmt, stmt.getSQLExpressionFactory().getMappingForType(int.class, true), "CHAR_LENGTH", funcArgs);
        }
        else
        {
            throw new NucleusUserException("Argument for LENGTHOFSTRING has to be a StringExpression/StringLiteral - invalid to pass " + arg0Expr);
        }
    }
}
