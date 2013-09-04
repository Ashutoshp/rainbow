package org.sa.rainbow.core.management.ports.eseb.converters;

import incubator.pval.Ensure;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sa.rainbow.core.models.DescriptionAttributes;
import org.sa.rainbow.core.models.ProbeDescription;
import org.sa.rainbow.core.models.EffectorDescription.EffectorAttributes;
import org.sa.rainbow.core.models.ProbeDescription.ProbeAttributes;
import org.sa.rainbow.translator.effectors.IEffector;
import org.sa.rainbow.translator.probes.IProbe;

import edu.cmu.cs.able.typelib.jconv.TypelibJavaConversionRule;
import edu.cmu.cs.able.typelib.jconv.TypelibJavaConverter;
import edu.cmu.cs.able.typelib.jconv.ValueConversionException;
import edu.cmu.cs.able.typelib.prim.PrimitiveScope;
import edu.cmu.cs.able.typelib.scope.AmbiguousNameException;
import edu.cmu.cs.able.typelib.struct.Field;
import edu.cmu.cs.able.typelib.struct.StructureDataType;
import edu.cmu.cs.able.typelib.struct.StructureDataValue;
import edu.cmu.cs.able.typelib.struct.UnknownFieldException;
import edu.cmu.cs.able.typelib.type.DataType;
import edu.cmu.cs.able.typelib.type.DataValue;

public class DescriptionAttributesConverter implements TypelibJavaConversionRule {
    // Thesea are the current types converted
    //struct probe_description {string name; string location; string kind_name; string kind; string alias; map<string,string> info, map<string,list<string>> arrays;}
    //struct effector_description {string name; string location; string kind_name; string kind; map<string,string> info, map<string,list<string>> arrays;}

    private final PrimitiveScope m_scope;

    public DescriptionAttributesConverter (PrimitiveScope scope) {
        m_scope = scope;
    }

    @Override
    public <T> T to_java (DataValue value, Class<T> cls, TypelibJavaConverter converter)
            throws ValueConversionException {
        if (value instanceof StructureDataValue) {
            try {
                StructureDataValue sdv = (StructureDataValue )value;
                StructureDataType sdt = (StructureDataType )sdv.type ();
                String name = converter.<String> to_java (sdv.value (sdt.field ("name")), String.class);
                String location = converter.<String> to_java (sdv.value (sdt.field ("location")), String.class);
                String kindName = converter.<String> to_java (sdv.value (sdt.field ("kind_name")), String.class);
                String kind = converter.<String> to_java (sdv.value (sdt.field ("kind")), String.class);
                Map<String, String> info = converter.<Map> to_java (sdv.value (sdt.field ("info")), Map.class);
                Map<String, List<String>> arrays = converter
                        .<Map> to_java (sdv.value (sdt.field ("arrays")), Map.class);
                DescriptionAttributes atts;
                if ("probe_description".equals (sdt.name ()) || cls == ProbeDescription.ProbeAttributes.class) {
                    String alias = converter.<String> to_java (sdv.value (sdt.field ("alias")), String.class);

                    ProbeDescription.ProbeAttributes pd = new ProbeDescription.ProbeAttributes ();
                    pd.kind = IProbe.Kind.valueOf (kind);
                    atts = pd;
                }
                else if ("effector_description".equals (sdt.name ()) || EffectorAttributes.class == cls) {
                    EffectorAttributes ed = new EffectorAttributes ();
                    ed.kind = IEffector.Kind.valueOf (kind);
                    atts = ed;
                }
                else
                    throw new ValueConversionException ("Cannot convert a value of " + value + " to "
                            + cls.getCanonicalName ());
                atts.arrays = new HashMap<> ();
                for (Entry<String, List<String>> e : arrays.entrySet ()) {
                    atts.arrays.put (e.getKey (), e.getValue ().toArray (new String[0]));
                }
                atts.info = info;
                atts.kindName = kindName;
                atts.location = location;
                atts.name = name;
                @SuppressWarnings ("unchecked")
                T t = (T )atts;
                return t;
            }
            catch (UnknownFieldException | AmbiguousNameException e) {
                throw new ValueConversionException (MessageFormat.format ("Could not convert from {0} to {1}",
                        value.toString (), cls.getCanonicalName ()), e);
            }
        }
        throw new ValueConversionException (MessageFormat.format ("Could not convert from {0} to {1}",
                value.toString (), cls.getCanonicalName ()));
    }

    @Override
    public boolean handles_typelib (DataValue value, Class<?> cls) {
        Ensure.not_null (value);
        if (value.type ().name ().equals ("probe_description") || value.type ().name ().equals ("effector_description")) {
            if (cls == null || DescriptionAttributes.class.isAssignableFrom (cls)) return true;
        }
        return false;
    }

    @Override
    public boolean handles_java (Object value, DataType dst) {
        if (value instanceof ProbeAttributes || value instanceof EffectorAttributes)
            return dst == null || dst.name ().equals ("probe_description")
            || dst.name ().equals ("effector_description");
        return false;
    }

    @Override
    public DataValue from_java (Object value, DataType dst, TypelibJavaConverter converter)
            throws ValueConversionException {
        try {
            if ((dst == null || dst instanceof StructureDataType) && value instanceof ProbeAttributes) {
                ProbeAttributes pa = (ProbeAttributes )value;
                StructureDataType sdt = (StructureDataType )dst;
                if (sdt == null) {
                    sdt = (StructureDataType )m_scope.find ("probe_description");
                }
                Field alias = sdt.field ("alias");
                Field kind = sdt.field ("kind");
                Map<Field, DataValue> fields = from_java (pa, sdt, converter);

                fields.put (alias, converter.from_java (pa.alias, m_scope.string ()));
                fields.put (kind, converter.from_java (pa.kind.name (), m_scope.string ()));
                StructureDataValue sdv = sdt.make (fields);
                return sdv;
            }
            else if ((dst == null || dst instanceof StructureDataType) && value instanceof EffectorAttributes) {
                EffectorAttributes ea = (EffectorAttributes )value;
                StructureDataType sdt = (StructureDataType )dst;
                if (sdt == null) {
                    sdt = (StructureDataType )m_scope.find ("effector_description");
                }
                Field kind = sdt.field ("kind");
                Map<Field, DataValue> fields = from_java (ea, sdt, converter);

                fields.put (kind, converter.from_java (ea.kind.name (), m_scope.string ()));
                StructureDataValue sdv = sdt.make (fields);
                return sdv;
            }
        }
        catch (UnknownFieldException | AmbiguousNameException e) {
            throw new ValueConversionException (MessageFormat.format ("Could not convert from {0} to {1}", value
                    .getClass ().getCanonicalName (), (dst == null ? "probe_description or effector_description" : dst
                            .absolute_hname ().toString ())), e);
        }
        throw new ValueConversionException (MessageFormat.format ("Could not convert from {0} to {1}", value
                .getClass ().getCanonicalName (), (dst == null ? "probe_description or effector_description" : dst
                        .absolute_hname ()
                        .toString ())));

    }

    private Map<Field, DataValue> from_java (DescriptionAttributes da,
            StructureDataType sdt,
            TypelibJavaConverter converter) throws AmbiguousNameException, ValueConversionException {
        Map<Field, DataValue> fields = new HashMap<> ();

        Field name = sdt.field ("name");
        Field location = sdt.field ("location");
        Field kind_name = sdt.field ("kind_name");
        Field info = sdt.field ("info");
        Field arrays = sdt.field ("arrays");

        fields.put (name, converter.from_java (da.name, m_scope.string ()));
        fields.put (location, converter.from_java (da.location, m_scope.string ()));
        fields.put (kind_name, converter.from_java (da.kindName, m_scope.string ()));
        fields.put (info, converter.from_java (da.info, m_scope.find ("map<string,string>")));

        Map<String, List<String>> arraysC = new HashMap<> ();
        for (Entry<String, String[]> e : da.arrays.entrySet ()) {
            arraysC.put (e.getKey (), Arrays.asList (e.getValue ()));
        }
        fields.put (arrays, converter.from_java (arraysC, m_scope.find ("map<string,list<string>>")));
        return fields;
    }

}
