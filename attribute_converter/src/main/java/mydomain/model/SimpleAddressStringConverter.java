package mydomain.model;

import javax.persistence.*;

@Converter(autoApply=true)
public class SimpleAddressStringConverter implements AttributeConverter<SimpleAddress, String>
{
    public String convertToDatabaseColumn(SimpleAddress attribute)
    {
        if (attribute == null)
        {
            return null;
        }
        return attribute.getStreet() + ":" + attribute.getCity();
    }

    public SimpleAddress convertToEntityAttribute(String dbData)
    {
        if (dbData == null)
        {
            return null;
        }

        int separatorPos = dbData.indexOf(':');
        return new SimpleAddress(dbData.substring(0, separatorPos), dbData.substring(separatorPos+1));
    }
}
