package mydomain.model;

import javax.persistence.*;

public class BooleanYNConverter implements AttributeConverter<Boolean, Character>
{
    public Character convertToDatabaseColumn (Boolean attribute)
    {
        if (attribute == null)
        {
            return null;
        }
        return (attribute == Boolean.TRUE ? Character.valueOf('Y') : Character.valueOf('N'));
    }
    public Boolean convertToEntityAttribute (Character dbData)
    {
        if (dbData == null)
        {
            return null;
        }

        if (dbData.equals(Character.valueOf('Y')))
        {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
