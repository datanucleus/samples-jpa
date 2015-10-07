package mydomain.model;

import javax.persistence.*;

public class Boolean10Converter implements AttributeConverter<Boolean, Short>
{
    public Short convertToDatabaseColumn (Boolean attribute)
    {
        if (attribute == null)
        {
            return null;
        }
        return (attribute == Boolean.TRUE ? Short.valueOf((short) 1) : Short.valueOf((short)0));
    }
    public Boolean convertToEntityAttribute (Short dbData)
    {
        if (dbData == null)
        {
            return null;
        }

        if (dbData == 1)
        {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
