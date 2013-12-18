package org.cloudifysource.restDoclet.generation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ClassUtils;
import org.cloudifysource.restDoclet.docElements.DocParameter;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.primitives.Primitives;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

/**
 * @author edward
 */
public class QueryParamGenerator {

  static final Collection<String> wrapperClassNames;

  static {
    wrapperClassNames = Collections2.transform(Primitives.allWrapperTypes(), new Function<Class<?>, String>() {
      @Override
      public String apply(final Class<?> input) {
        return input.getName();
      }
    });
  }

  public QueryParamGenerator() {
  }

  public List<DocParameter> createParamList(Parameter parameter) throws ClassNotFoundException, IntrospectionException {
    List<DocParameter> parameters = new ArrayList<DocParameter>();
    Type type = parameter.type();
    if (type.isPrimitive() || wrapperClassNames.contains(type.qualifiedTypeName())) {
      Class<?> clazz = ClassUtils.getClass(type.qualifiedTypeName());
      parameters.add(new DocParameter(parameter.name(), clazz, "RequestParam"));
    }
    else if (List.class.getName().equals(type.qualifiedTypeName())) {
      parameters.add(new DocParameter(parameter.name(), List.class, "RequestParam"));
    }
    else if (Set.class.getName().equals(type.qualifiedTypeName())) {
      parameters.add(new DocParameter(parameter.name(), Set.class, "RequestParam"));
    }
    else {
      BeanInfo info = Introspector.getBeanInfo(Class.forName(type.qualifiedTypeName()), Object.class);
      for (PropertyDescriptor prop : info.getPropertyDescriptors()) {
        parameters.add(new DocParameter(prop.getName(), prop.getPropertyType(), "RequestParam"));
      }
    }
    return parameters;
  }
}