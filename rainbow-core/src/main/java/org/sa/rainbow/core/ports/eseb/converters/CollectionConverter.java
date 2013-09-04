package org.sa.rainbow.core.ports.eseb.converters;

import incubator.pval.Ensure;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.cmu.cs.able.typelib.comp.ListDataType;
import edu.cmu.cs.able.typelib.comp.ListDataValue;
import edu.cmu.cs.able.typelib.comp.SetDataType;
import edu.cmu.cs.able.typelib.comp.SetDataValue;
import edu.cmu.cs.able.typelib.jconv.TypelibJavaConversionRule;
import edu.cmu.cs.able.typelib.jconv.TypelibJavaConverter;
import edu.cmu.cs.able.typelib.jconv.ValueConversionException;
import edu.cmu.cs.able.typelib.type.DataType;
import edu.cmu.cs.able.typelib.type.DataValue;

public class CollectionConverter implements TypelibJavaConversionRule {

    @Override
    public boolean handles_java (Object value, DataType dst) {
        if (value == null || dst == null) return false;

        return (dst instanceof ListDataType || dst instanceof SetDataType) && value instanceof Collection;
    }

    @Override
    public boolean handles_typelib (DataValue value, Class<?> cls) {
        Ensure.not_null (value);

        if (cls != Collection.class && make_instance_set (cls) == null && make_instance_list (cls) == null)
            return false;

        return value instanceof SetDataValue || value instanceof ListDataValue;
    }

    @Override
    public DataValue from_java (Object value, DataType dst, TypelibJavaConverter converter)
            throws ValueConversionException {
        Ensure.not_null (dst);
        Ensure.is_true (dst instanceof SetDataType || dst instanceof ListDataType);
        Ensure.not_null (value);
        Ensure.is_instance (value, Collection.class);
        if (dst instanceof SetDataType) {
            SetDataType sdt = (SetDataType )dst;
            SetDataValue sdv = sdt.make ();
            for (Object o : (Collection<?> )value) {
                sdv.add (converter.from_java (o, null));
            }

            return sdv;
        }
        else {
            ListDataType ldt = (ListDataType )dst;
            ListDataValue ldv = ldt.make ();
            for (Object o : (Collection<?> )value) {
                ldv.add (converter.from_java (o, null));
            }
            return ldv;
        }
    }

    @Override
    public <T> T to_java (DataValue value, Class<T> cls, TypelibJavaConverter converter)
            throws ValueConversionException {
        Ensure.not_null (value);
        Ensure.is_true (value instanceof SetDataValue || value instanceof ListDataValue);


        Set<Object> set = make_instance_set (cls);
        List<Object> list = make_instance_list (cls);

        Collection<DataValue> col = null;
        if (value instanceof SetDataValue) {
            col = ((SetDataValue )value).all ();
            if (set == null && Collection.class == cls) {
                set = new HashSet ();
            }
        }
        else {
            col = ((ListDataValue )value).all ();
            if (list == null && Collection.class == cls) {
                list = new ArrayList ();
            }
        }

        if (set != null) {

            for (DataValue v : col) {
                set.add (converter.to_java (v, null));
            }

            @SuppressWarnings ("unchecked")
            T t = (T )set;
            return t;
        }
        else {
            Ensure.not_null (list);
            for (DataValue v : col) {
                list.add (converter.to_java (v, null));
            }

            @SuppressWarnings ("unchecked")
            T t = (T )list;
            return t;
        }
    }

    /**
     * Creates an instance of the given class, which must be a subclass of Set.
     * 
     * @param cls
     *            the class; if <code>null</code> a default set will be created
     * @return the instance
     */
    private Set<Object> make_instance_set (Class<?> cls) {
        if (cls == null || cls == Set.class) return new HashSet<> ();

        if (!Set.class.isAssignableFrom (cls)) return null;

        try {
            Set<?> s = Set.class.cast (cls.getConstructor ().newInstance ());
            @SuppressWarnings ("unchecked")
            Set<Object> so = (Set<Object> )s;
            return so;
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | ClassCastException e) {
            return null;
        }
    }

    /**
     * Creates an instance of the given class, which must be a subclass of List.
     * 
     * @param cls
     *            the class; if <code>null</code> a default set will be created
     * @return the instance
     */
    private List<Object> make_instance_list (Class<?> cls) {
        if (cls == null || cls == List.class) return new ArrayList<> ();

        if (!List.class.isAssignableFrom (cls)) return null;

        try {
            List<?> l = List.class.cast (cls.getConstructor ().newInstance ());
            @SuppressWarnings ("unchecked")
            List<Object> lo = (List<Object> )l;
            return lo;
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | ClassCastException e) {
            return null;
        }
    }

}